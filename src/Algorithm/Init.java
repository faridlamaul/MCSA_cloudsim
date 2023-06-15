package Algorithm;

import java.util.Random;

public class Init {
    // Method untuk inisialisasi posisi awal secara random pada CSA
    public static double[][] init ( int n_task, int n_vm, double lb, double ub ) {
        // Deklarasi array 2 dimensi untuk posisi awal
        double[][] x = new double[ n_task ][ n_vm ];
        Random rand = new Random ( );

        for ( int i = 0; i < n_task; i++ ) {
            for ( int j = 0; j < n_vm; j++ ) {
                // Generate posisi awal secara random
                x[ i ][ j ] = lb - ( lb - ub ) * rand.nextDouble ( );
            }
        }

        return x;
    }
}
