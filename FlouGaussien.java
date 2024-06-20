import java.awt.image.BufferedImage;

/**
 * Classe pour appliquer un flou gaussien sur une image.
 */
public class FlouGaussien {

    /**
     * Applique un flou gaussien sur une image.
     *
     * @param image L'image à flouter.
     * @param blurLevel Le niveau de flou gaussien (taille du noyau).
     * @return L'image floutée.
     */
    public static BufferedImage applyGaussianBlur(BufferedImage image, int blurLevel) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage blurredImage = new BufferedImage(width, height, image.getType());

        // Générer le filtre gaussien basé sur le niveau de flou
        float[] filter = generateGaussianKernel(blurLevel);

        int offset = blurLevel / 2;
        for (int y = offset; y < height - offset; y++) {
            for (int x = offset; x < width - offset; x++) {
                float r = 0, g = 0, b = 0;
                for (int j = -offset; j <= offset; j++) {
                    for (int i = -offset; i <= offset; i++) {
                        int rgb = image.getRGB(x + i, y + j);
                        r += ((rgb >> 16) & 0xFF) * filter[(j + offset) * blurLevel + (i + offset)];
                        g += ((rgb >> 8) & 0xFF) * filter[(j + offset) * blurLevel + (i + offset)];
                        b += (rgb & 0xFF) * filter[(j + offset) * blurLevel + (i + offset)];
                    }
                }

                int newR = Math.min(Math.max((int) r, 0), 255);
                int newG = Math.min(Math.max((int) g, 0), 255);
                int newB = Math.min(Math.max((int) b, 0), 255);
                int newRGB = (newR << 16) | (newG << 8) | newB;

                blurredImage.setRGB(x, y, newRGB);
            }
        }
        return blurredImage;
    }

    /**
     * Génère un noyau gaussien basé sur l'écart-type et la taille du noyau.
     *
     * @param size La taille du noyau de convolution.
     * @return Le noyau gaussien normalisé.
     */
    private static float[] generateGaussianKernel(int size) {
        float[] kernel = new float[size * size];
        int center = size / 2;
        float sigma = size / 3.0f;
        float sum = 0;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int dx = x - center;
                int dy = y - center;
                float value = (float) Math.exp(-(dx * dx + dy * dy) / (2 * sigma * sigma)) / (2 * (float) Math.PI * sigma * sigma);
                kernel[y * size + x] = value;
                sum += value;
            }
        }

        for (int i = 0; i < kernel.length; i++) {
            kernel[i] /= sum;
        }

        return kernel;
    }
}
