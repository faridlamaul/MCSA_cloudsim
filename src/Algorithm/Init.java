package Algorithm;

import java.util.Random;

public class Init {
    public static double[][] init(int N, int pd) {
        double[][] x = new double[N][pd];
        double l = -100;
        double u = 100;
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < pd; j++) {
                x[i][j] = l - (l - u) * random.nextDouble();
            }
        }
        return x;
    }
}
