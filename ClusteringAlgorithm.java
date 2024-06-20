/**
 * Interface représentant un algorithme de clustering.
 */
public interface ClusteringAlgorithm {
    /**
     * Méthode pour effectuer le clustering sur les données fournies.
     *
     * @param data Tableau de valeurs numériques en deux dimensions.
     * @return Tableau contenant les numéros de cluster pour chaque objet.
     */
    int[] cluster(double[][] data);
}
