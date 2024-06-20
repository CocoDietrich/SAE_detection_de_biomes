public class OutilCouleur {

    public static int[] getTabColor(int rgb) {
        int blue = rgb & 0xff;
        int green = (rgb & 0xff00) >> 8;
        int red = (rgb & 0xff0000) >> 16;
        return new int[] {red, green, blue};
    }
}
