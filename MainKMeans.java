import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Classe principale pour traiter l'image, détecter les biomes et les écosystèmes, et afficher les résultats.
 */
public class MainKMeans {

    public static void main(String[] args) {
        try {
            File input = new File("Planete 1.jpg");
            BufferedImage image = ImageIO.read(input);

            // Extraire les données des pixels de l'image
            double[][] pixelData = extractPixelData(image);

            // Appliquer l'algorithme de clustering pour détecter les biomes avec KMeans
            ClusteringAlgorithm kmeansAlgorithm = new KMeans(10, 100); // 10 clusters pour les biomes
            EcosystemDetection kmeansDetection = new EcosystemDetection(kmeansAlgorithm);
            int[] kmeansClusters = kmeansDetection.detectEcosystems(pixelData);

            // Visualiser les clusters (biomes) avec KMeans
            BufferedImage kmeansBiomeImage = visualizeClusters(image, kmeansClusters, 10);
            ImageIO.write(kmeansBiomeImage, "png", new File("biome_output_kmeans.png"));

            // Détecter et afficher les écosystèmes pour chaque biome (KMeans)
            visualizeEcosystems(image, kmeansClusters, 10, "kmeans");

            System.out.println("Images générées avec KMeans avec succès.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour extraire les données des pixels de l'image.
     *
     * @param image L'image dont on veut extraire les données des pixels.
     * @return Un tableau 2D contenant les valeurs des couleurs normalisées des pixels.
     */
    private static double[][] extractPixelData(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double[][] data = new double[width * height][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb);
                data[y * width + x][0] = color.getRed() / 255.0;
                data[y * width + x][1] = color.getGreen() / 255.0;
                data[y * width + x][2] = color.getBlue() / 255.0;
            }
        }

        return data;
    }

    /**
     * Méthode pour visualiser les clusters en coloriant chaque pixel selon son cluster.
     *
     * @param image L'image originale.
     * @param clusters Tableau contenant les numéros de cluster pour chaque pixel.
     * @param k Le nombre de clusters.
     * @return Une nouvelle image avec les clusters colorés.
     */
    private static BufferedImage visualizeClusters(BufferedImage image, int[] clusters, int k) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage clusteredImage = new BufferedImage(width, height, image.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int clusterId = clusters[y * width + x];
                Color closestColor = findClosestColor(new Color(image.getRGB(x, y)));
                clusteredImage.setRGB(x, y, closestColor.getRGB());
            }
        }

        return clusteredImage;
    }

    /**
     * Trouver la couleur la plus proche dans la palette de biomes.
     *
     * @param color La couleur d'origine.
     * @return La couleur la plus proche dans la palette.
     */
    private static Color findClosestColor(Color color) {
        Color closestColor = null;
        double minDistance = Double.MAX_VALUE;

        for (Color paletteColor : Palette.BIOME_COLORS) {
            double distance = NormeCielab.distanceCouleur(color, paletteColor);
            if (distance < minDistance) {
                minDistance = distance;
                closestColor = paletteColor;
            }
        }

        return closestColor;
    }

    /**
     * Méthode pour détecter et visualiser les écosystèmes pour chaque biome.
     *
     * @param image L'image originale.
     * @param clusters Tableau contenant les numéros de cluster pour chaque pixel.
     * @param k Le nombre de biomes.
     * @param algorithmType Le type d'algorithme utilisé (pour nommer les fichiers de sortie).
     */
    private static void visualizeEcosystems(BufferedImage image, int[] clusters, int k, String algorithmType) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int biome = 0; biome < k; biome++) {
            // Extraire les positions des pixels appartenant au biome
            double[][] positions = new double[width * height][2];
            int count = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (clusters[y * width + x] == biome) {
                        positions[count][0] = x;
                        positions[count][1] = y;
                        count++;
                    }
                }
            }
            double[][] biomePositions = new double[count][2];
            System.arraycopy(positions, 0, biomePositions, 0, count);

            // Appliquer un nouvel algorithme de clustering pour détecter les écosystèmes
            ClusteringAlgorithm ecosystemAlgorithm = new KMeans(3, 100); // 3 clusters pour les écosystèmes
            EcosystemDetection ecosystemDetection = new EcosystemDetection(ecosystemAlgorithm);
            int[] ecosystemClusters = ecosystemDetection.detectEcosystems(biomePositions);

            // Visualiser les écosystèmes en utilisant la couleur la plus proche dans la palette
            BufferedImage ecosystemImage = visualizeEcosystemClusters(image, biomePositions, ecosystemClusters);
            try {
                ImageIO.write(ecosystemImage, "png", new File("ecosystem_output_" + algorithmType + "_biome_" + biome + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Méthode pour visualiser les écosystèmes en coloriant chaque pixel selon son écosystème, avec la couleur la plus proche dans la palette.
     *
     * @param image L'image originale.
     * @param positions Les positions des pixels appartenant aux écosystèmes.
     * @param clusters Tableau contenant les numéros de cluster pour chaque position.
     * @return Une nouvelle image avec les écosystèmes colorés.
     */
    private static BufferedImage visualizeEcosystemClusters(BufferedImage image, double[][] positions, int[] clusters) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage ecosystemImage = new BufferedImage(width, height, image.getType());

        for (int i = 0; i < positions.length; i++) {
            int x = (int) positions[i][0];
            int y = (int) positions[i][1];
            Color closestColor = findClosestColor(new Color(image.getRGB(x, y)));
            ecosystemImage.setRGB(x, y, closestColor.getRGB());
        }

        return ecosystemImage;
    }
}
