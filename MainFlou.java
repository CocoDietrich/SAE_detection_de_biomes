import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class MainFlou {
    public static void main(String[] args) {
        try {
            // Définir le chemin de l'image et les niveaux de flou
            String imagePath = "Planete 1.jpg";
            int meanBlurLevel = 7; // Niveau de flou par moyenne
            int gaussianBlurLevel = 7; // Niveau de flou gaussien

            // Charger l'image
            BufferedImage image = ImageIO.read(new File(imagePath));

            // Appliquer le flou par moyenne
            BufferedImage meanBlurImage = FlouMoyen.applyMeanBlur(image, meanBlurLevel);
            ImageIO.write(meanBlurImage, "png", new File("flou_moyen_image.png"));

            // Appliquer le flou gaussien
            BufferedImage gaussianBlurImage = FlouGaussien.applyGaussianBlur(image, gaussianBlurLevel);
            ImageIO.write(gaussianBlurImage, "png", new File("flou_gaussien_image.png"));

            System.out.println("Images avec flous générées avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
