package Algorithm;

import java.util.Random;

public class Init {
    // Method untuk inisialisasi posisi awal CSA
    public static double[][] init ( int n_task, int n_vm ) {
        double[][] x = new double[ n_task ][ n_vm ];
        // batas bawah dan atas
        double l = -1;
        double u = 1;
        Random random = new Random ( );
        for ( int i = 0; i < n_task; i++ ) {
            for ( int j = 0; j < n_vm; j++ ) {
                // Generate posisi awal CSA secara random
                x[ i ][ j ] = l - ( l - u ) * random.nextDouble ( );
            }
        }
        return x;
    }
}
