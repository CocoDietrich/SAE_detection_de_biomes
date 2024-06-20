import java.awt.image.BufferedImage;

/**
 * Classe pour appliquer un flou par moyenne sur une image.
 */
public class FlouMoyen {

    /**
     * Méthode pour appliquer un flou par moyenne sur une image.
     *
     * @param image L'image à flouter.
     * @param blurLevel Le niveau de flou par moyenne.
     * @return L'image floutée.
     */
    public static BufferedImage applyMeanBlur(BufferedImage image, int blurLevel) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage blurredImage = new BufferedImage(width, height, image.getType());

        // Créer un filtre de taille blurLevel x blurLevel pour le flou par moyenne
        int filterSize = blurLevel * blurLevel;
        float[] filter = new float[filterSize];
        for (int i = 0; i < filterSize; i++) {
            filter[i] = 1.0f / filterSize;
        }

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
}
