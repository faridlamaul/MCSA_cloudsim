package Algorithm;

import java.util.Random;

public class CSA {
    public static void csa ( int n_task, int n_vm, double AP, double fl ) {
        // Inisialisasi posisi awal Task ke i pada VM ke j
        double[][] x = Init.init ( n_task, n_vm );

        // Inisialisasi pemetaan antara task ke i pada VM ke j
        double[][] mem = x.clone ( );

        // Inisialisasi fitness
        double[] ft_mem = Fitness.fitness ( mem, n_task, n_vm );

        // Maximum number of iterations
        int max_iter = 10;

        for ( int t = 1; t <= max_iter; t++ ) {
            int[] num = new int[ n_task ];
            Random rand = new Random ( );

            for ( int i = 0; i < n_task; i++ ) {
                num[ i ] = rand.nextInt ( n_task );
                // System.out.println ( "num[" + i + "] : " + num[ i ] );
            }

            double[][] xnew = new double[ n_task ][ n_vm ];
            // Generate random kandidat task untuk memori
            for ( int i = 0; i < n_task; i++ ) {
                // Generation posisi baru untuk task ke i pada VM ke j - Kasus 1
                if ( rand.nextDouble ( ) > AP ) {
                    for ( int j = 0; j < n_vm; j++ ) {
                        xnew[ i ][ j ] = x[ i ][ j ] + rand.nextDouble ( ) * fl * ( mem[ num[ i ] ][ j ] - x[ i ][ j ] );
                        // System.out.println ( "> AP xnew[" + i + "][" + j + "] : " + xnew[ i ][ j ] );
                    }
                }
                // Generation posisi baru secara random untuk task ke i pada VM ke j - Kasus 2
                else {
                    double l = -1;
                    double u = 1;
                    for ( int j = 0; j < n_vm; j++ ) {
                        xnew[ i ][ j ] = l - ( l - u ) * rand.nextDouble ( );
                        // System.out.println ( "< AP xnew[" + i + "][" + j + "] : " + xnew[ i ][ j ] );
                    }
                }
            }
            double[][] xn = xnew.clone ( );
            double[] ft = Fitness.fitness ( xn, n_task, n_vm );

            // Update posisi dan memori
            for ( int i = 0; i < n_task; i++ ) {
                boolean isUpdated = true;
                for ( int j = 0; j < n_vm; j++ ) {
                    if ( xnew[ i ][ j ] < -1 || xnew[ i ][ j ] > 1 ) {
                        isUpdated = false;
                        break;
                    }
                }

                if ( isUpdated ) {
                    for ( int j = 0; j < n_vm; j++ ) {
                        // Update posisi
                        x[ i ][ j ] = xnew[ i ][ j ];
                    }
                    if ( ft[ i ] < ft_mem[ i ] ) {
                        // Update memori
                        for ( int j = 0; j < n_vm; j++ ) {
                            mem[ i ][ j ] = xnew[ i ][ j ];
                        }
                        ft_mem[ i ] = ft[ i ];
                    }
                }
            }
        }

        // Cetak hasil
        int best = 0;
        for ( int i = 0; i < n_task; i++ ) {
            System.out.println ( "ft_mem[" + i + "] : " + ft_mem[ i ] );
            if ( ft_mem[ i ] < ft_mem[ best ] ) {
                best = i;
            }
        }
        System.out.println ( "Best : " + best );
        System.out.println ( "ft_mem[" + best + "] : " + ft_mem[ best ] );
        System.out.println ( "----------------------------------------" );
    }
}
