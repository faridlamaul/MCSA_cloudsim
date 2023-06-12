package Algorithm;

public class Fitness {
    public static double[] fitness(double[][] xn, int NTask, int NVM) {
        double[] ft = new double[NTask];
        for (int i = 0; i < NTask; i++) {
            double sum = 0;
            for (int j = 0; j < NVM; j++) {
                sum += xn[i][j] * xn[i][j];
            }
            ft[i] = sum; // Sphere function
        }
        return ft;
    }
}
