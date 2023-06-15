package Algorithm;

public class Fitness {
    // Method untuk menghitung fitness function
    public static double[] fitness ( double[][] xn, int n_task, int n_vm ) {
        double[] ft = new double[ n_task ];

        for ( int i = 0; i < n_task; i++ ) {
            double sum = 0;

            for ( int j = 0; j < n_vm; j++ ) {
                sum += xn[ i ][ j ] * xn[ i ][ j ];
            }

            ft[ i ] = sum;
        }

        return ft;
    }
}
