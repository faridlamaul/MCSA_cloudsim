package Algorithm;

import java.util.Random;

public class CSA {
    public static void csa ( int n_task, int n_vm, double AP, double fl, int max_iter, double lb, double ub ) {
        // Inisialisasi posisi awal secara random
        double[][] x = Init.init ( n_task, n_vm, lb, ub );

        // Inisialisasi memori dengan posisi awal
        double[][] mem = x.clone ( );

        // Inisialisasi fitness function dari memori
        double[] ft_mem = Fitness.fitness ( mem, n_task, n_vm );

        // Mulai iterasi
        for ( int t = 1; t <= max_iter; t++ ) {
            // System.out.println ( "----------------------------------------" );
            int[] num = new int[ n_task ];
            Random rand = new Random ( );

            // Generate random kandidat task untuk memori
            for ( int i = 0; i < n_task; i++ ) {
                num[ i ] = ( int )  ( n_task * rand.nextDouble ( ) );
                 System.out.println ( "num[" + i + "] : " + num[ i ] );
            }

            // Deklarasi array 2 dimensi untuk posisi baru
            double[][] xnew = new double[ n_task ][ n_vm ];

            // Generate posisi baru untuk setiap task pada setiap VM
            for ( int i = 0; i < n_task; i++ ) {
                // Kasus 1
                if ( rand.nextDouble ( ) > AP ) {
                    for ( int j = 0; j < n_vm; j++ ) {
                        xnew[ i ][ j ] = x[ i ][ j ] + fl * rand.nextDouble ( ) * ( mem[ num[ i ] ][ j ] - x[ i ][ j ] );
                        // System.out.println ( "> AP xnew[" + i + "][" + j + "] : " + xnew[ i ][ j ] );
                    }
                }
                // Kasus 2
                else {
                    for ( int j = 0; j < n_vm; j++ ) {
                        xnew[ i ][ j ] = lb - ( lb - ub ) * rand.nextDouble ( );
                        // System.out.println ( "> fl xnew[" + i + "][" + j + "] : " + xnew[ i ][ j ] );
                    }
                }
            }

            double[][] xn = xnew.clone ( );

            // Hitung fitness function dari posisi baru
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
