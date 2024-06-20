import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Classe principale pour exécuter le clustering avec l'algorithme DBSCAN.
 */
public class MainDBSCAN {

    public static void main(String[] args) {
        try {
            File input = new File("dbscan.jpg");
            BufferedImage image = ImageIO.read(input);

            // Extraire les données des pixels de l'image
            double[][] pixelData = extractPixelData(image);

            // Appliquer l'algorithme de clustering pour détecter les biomes avec DBSCAN
            ClusteringAlgorithm dbscanAlgorithm = new DBSCAN(0.1, 5); // Paramètres DBSCAN
            EcosystemDetection dbscanDetection = new EcosystemDetection(dbscanAlgorithm);
            int[] dbscanClusters = dbscanDetection.detectEcosystems(pixelData);

            // Visualiser les clusters (biomes) avec DBSCAN
            BufferedImage dbscanBiomeImage = visualizeClusters(image, dbscanClusters);
            ImageIO.write(dbscanBiomeImage, "png", new File("biome_output_dbscan.png"));

            // Détecter et afficher les écosystèmes pour chaque biome (DBSCAN)
            visualizeEcosystems(image, dbscanClusters, "dbscan");

            System.out.println("Images générées avec DBSCAN avec succès.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private static BufferedImage visualizeClusters(BufferedImage image, int[] clusters) {
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
            double distance = colorDistance(color, paletteColor);
            if (distance < minDistance) {
                minDistance = distance;
                closestColor = paletteColor;
            }
        }

        return closestColor;
    }

    /**
     * Calculer la distance entre deux couleurs.
     *
     * @param c1 La première couleur.
     * @param c2 La deuxième couleur.
     * @return La distance euclidienne entre les deux couleurs.
     */
    private static double colorDistance(Color c1, Color c2) {
        int r1 = c1.getRed();
        int g1 = c1.getGreen();
        int b1 = c1.getBlue();
        int r2 = c2.getRed();
        int g2 = c2.getGreen();
        int b2 = c2.getBlue();
        return Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));
    }

    private static void visualizeEcosystems(BufferedImage image, int[] clusters, String algorithmType) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int biome = 0; biome < Palette.BIOME_COLORS.length; biome++) {
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
            if (count == 0) continue;

            double[][] biomePositions = new double[count][2];
            System.arraycopy(positions, 0, biomePositions, 0, count);

            // Appliquer DBSCAN pour détecter les écosystèmes
            ClusteringAlgorithm ecosystemAlgorithm = new DBSCAN(5.0, 3); // Paramètres DBSCAN pour les écosystèmes
            EcosystemDetection ecosystemDetection = new EcosystemDetection(ecosystemAlgorithm);
            int[] ecosystemClusters = ecosystemDetection.detectEcosystems(biomePositions);

            // Visualiser les écosystèmes en utilisant la couleur la plus proche dans la palette
            BufferedImage ecosystemImage = visualizeEcosystemClusters(image, biomePositions, ecosystemClusters);
            try {
                ImageIO.write(ecosystemImage,"png", new File("ecosystem_output_" + algorithmType + "_biome_" + biome + ".png"));
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

