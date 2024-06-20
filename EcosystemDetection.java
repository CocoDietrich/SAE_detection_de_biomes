/**
 * Classe pour détecter les écosystèmes en utilisant un algorithme de clustering.
 */
public class EcosystemDetection {
    private ClusteringAlgorithm algorithm;

    /**
     * Constructeur pour initialiser avec un algorithme de clustering.
     *
     * @param algorithm L'algorithme de clustering à utiliser.
     */
    public EcosystemDetection(ClusteringAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Méthode pour détecter les écosystèmes dans les données fournies.
     *
     * @param data Les données des pixels de l'image.
     * @return Un tableau contenant les numéros de cluster pour chaque objet.
     */
    public int[] detectEcosystems(double[][] data) {
        return algorithm.cluster(data);
    }
}
