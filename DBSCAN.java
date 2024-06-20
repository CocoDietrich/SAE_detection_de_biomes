import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe implémentant l'algorithme de clustering DBSCAN.
 */
public class DBSCAN implements ClusteringAlgorithm {
    private double eps;
    private int minPts;

    /**
     * Constructeur pour initialiser les paramètres de DBSCAN.
     *
     * @param eps Le rayon de voisinage.
     * @param minPts Le nombre minimum de points pour former un cluster.
     */
    public DBSCAN(double eps, int minPts) {
        this.eps = eps;
        this.minPts = minPts;
    }

    @Override
    public int[] cluster(double[][] data) {
        int n = data.length;
        int[] labels = new int[n];
        int clusterId = 0;

        for (int i = 0; i < n; i++) {
            if (labels[i] != 0) continue; // Déjà visité

            Set<Integer> neighbors = regionQuery(data, i);
            if (neighbors.size() < minPts) {
                labels[i] = -1; // Bruit
            } else {
                clusterId++;
                expandCluster(data, labels, i, neighbors, clusterId);
            }
        }

        return labels;
    }

    private void expandCluster(double[][] data, int[] labels, int pointIndex, Set<Integer> neighbors, int clusterId) {
        labels[pointIndex] = clusterId;
        Set<Integer> uniqueNeighbors = new HashSet<>(neighbors);

        while (!neighbors.isEmpty()) {
            int neighborIndex = neighbors.iterator().next();
            neighbors.remove(neighborIndex);

            if (labels[neighborIndex] == -1) {
                labels[neighborIndex] = clusterId;
            }

            if (labels[neighborIndex] == 0) {
                labels[neighborIndex] = clusterId;
                Set<Integer> newNeighbors = regionQuery(data, neighborIndex);
                if (newNeighbors.size() >= minPts) {
                    uniqueNeighbors.addAll(newNeighbors);
                }
            }
        }
        neighbors.addAll(uniqueNeighbors);
    }

    private Set<Integer> regionQuery(double[][] data, int pointIndex) {
        Set<Integer> neighbors = new HashSet<>();
        for (int i = 0; i < data.length; i++) {
            if (distance(data[pointIndex], data[i]) <= eps) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    private double distance(double[] point1, double[] point2) {
        double sum = 0;
        for (int i = 0; i < point1.length; i++) {
            double diff = point1[i] - point2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}
