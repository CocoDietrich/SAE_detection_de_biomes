import java.awt.*;

/**
 * Classe pour calculer la distance entre deux couleurs
 */
public class NormeCielab {

    public NormeCielab() {

    }

    public static double distanceCouleur(Color a, Color b) {
        int[] tab1 = OutilCouleur.getTabColor(a.getRGB());
        int[] tab2 = OutilCouleur.getTabColor(b.getRGB());
        int[] lab1 = Lab.rgb2lab(tab1[0], tab1[1], tab1[2]);
        int[] lab2 = Lab.rgb2lab(tab2[0], tab2[1], tab2[2]);
        return Math.sqrt(Math.pow(lab2[0]-lab1[0], 2) + Math.pow(lab2[1]-lab1[1], 2) + Math.pow(lab2[2]-lab1[2], 2));
    }
}
