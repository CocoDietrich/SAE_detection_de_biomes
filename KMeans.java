import java.util.Random;

/**
 * Classe implémentant l'algorithme de clustering KMeans.
 */
public class KMeans implements ClusteringAlgorithm {
    private int k;
    private int maxIterations;

    /**
     * Constructeur pour initialiser le nombre de clusters et le nombre maximal d'itérations.
     *
     * @param k Le nombre de clusters.
     * @param maxIterations Le nombre maximal d'itérations.
     */
    public KMeans(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
    }

    @Override
    public int[] cluster(double[][] data) {
        int n = data.length;
        int[] clusterAssignments = new int[n];
        double[][] centroids = new double[k][data[0].length];

        Random rand = new Random();
        for (int i = 0; i < k; i++) {
            centroids[i] = data[rand.nextInt(n)];
        }

        boolean changed = true;
        int iterations = 0;

        while (changed && iterations < maxIterations) {
            changed = false;
            iterations++;

            // Assigner chaque point au cluster le plus proche
            for (int i = 0; i < n; i++) {
                int nearestCluster = getNearestCluster(data[i], centroids);
                if (nearestCluster != clusterAssignments[i]) {
                    clusterAssignments[i] = nearestCluster;
                    changed = true;
                }
            }

            // Recalculer les centroids des clusters
            double[][] newCentroids = new double[k][data[0].length];
            int[] counts = new int[k];

            for (int i = 0; i < n; i++) {
                int cluster = clusterAssignments[i];
                for (int j = 0; j < data[0].length; j++) {
                    newCentroids[cluster][j] += data[i][j];
                }
                counts[cluster]++;
            }

            for (int i = 0; i < k; i++) {
                if (counts[i] == 0) continue;
                for (int j = 0; j < data[0].length; j++) {
                    newCentroids[i][j] /= counts[i];
                }
            }

            centroids = newCentroids;
        }

        return clusterAssignments;
    }

    /**
     * Méthode pour trouver le cluster le plus proche d'un point donné.
     *
     * @param point Le point dont on veut trouver le cluster le plus proche.
     * @param centroids Les centroids des clusters.
     * @return L'index du cluster le plus proche.
     */
    private int getNearestCluster(double[] point, double[][] centroids) {
        double minDist = Double.MAX_VALUE;
        int nearestCluster = -1;

        for (int i = 0; i < centroids.length; i++) {
            double dist = distance(point, centroids[i]);
            if (dist < minDist) {
                minDist = dist;
                nearestCluster = i;
            }
        }

        return nearestCluster;
    }

    /**
     * Méthode pour calculer la distance euclidienne entre deux points.
     *
     * @param point1 Premier point.
     * @param point2 Deuxième point.
     * @return La distance euclidienne entre les deux points.
     */
    private double distance(double[] point1, double[] point2) {
        double sum = 0;
        for (int i = 0; i < point1.length; i++) {
            double diff = point1[i] - point2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}
