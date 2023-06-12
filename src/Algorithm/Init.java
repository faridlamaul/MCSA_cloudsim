package Algorithm;

import java.util.Random;

public class Init {
    // Method untuk inisialisasi posisi awal CSA
    public static double[][] init(int NTask, int NVM) {
        double[][] x = new double[NTask][NVM];
        // batas bawah dan atas
        double l = -1;
        double u = 1;
        Random random = new Random();
        for (int i = 0; i < NTask; i++) {
            for (int j = 0; j < NVM; j++) {
                // Generate posisi awal CSA secara random
                x[i][j] = l - (l - u) * random.nextDouble();
            }
        }
        return x;
    }
}
