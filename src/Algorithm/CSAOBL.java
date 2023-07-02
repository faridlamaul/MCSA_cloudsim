package Algorithm;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

import java.util.List;

public class OppositionBasedLearning {
    public int[] AT;

    public double[] CT;

    // Allocate tasks to VMs using min-min algorithm
    public void allocateTasksToVm ( int M, int N, List<Cloudlet> cloudletList, List<Vm> vmlist ) {
        double[][] minMin = new double[ M ][ N ];
        for ( int i = 0; i < M; i++ ) {
            for ( int j = 0; j < N; j++ ) {
                minMin[ i ][ j ] = cloudletList.get ( j ).getCloudletLength ( ) / ( vmlist.get ( i ).getMips ( ) * vmlist.get ( i ).getNumberOfPes ( ) );
            }
        }
        // This array will hold the answer
        int[] resultTask = new int[ N ];
        int[] resultMachine = new int[ N ];
        int ptr = -1; // Indicates if result set is full or not
        while ( ptr < N - 1 ) {
            double[] time = new double[ N ];
            int[] vm = new int[ N ]; // stores minimum time w.r.t vm of each task
            for ( int j = 0; j < N; j++ ) {
                double minimum = Integer.MAX_VALUE;
                int pos = -1;
                for ( int i = 0; i < M; i++ ) {
                    if ( minMin[ i ][ j ] < minimum ) {
                        minimum = minMin[ i ][ j ];
                        pos = i;
                    }
                }
                time[ j ] = minimum;
                vm[ j ] = pos;
            }

            // Now we find the task with the minimum time
            double minimum = Integer.MAX_VALUE;
            int pos = -1;

            for ( int j = 0; j < N; j++ ) {
                if ( time[ j ] < minimum ) {
                    minimum = time[ j ];
                    pos = j;
                }
            }

            resultTask[ ++ptr ] = pos;
            resultMachine[ ptr ] = vm[ pos ];

            // Resetting states
            for ( int i = 0; i < M; i++ ) {
                for ( int j = 0; j < N; j++ ) {
                    if ( j == resultTask[ ptr ] )
                        minMin[ i ][ j ] = Integer.MAX_VALUE;
                    else if ( i == resultMachine[ ptr ] && minMin[ i ][ j ] != Integer.MAX_VALUE )
                        minMin[ i ][ j ] += minimum;
                }
            }
        }

        // Store allocation task to VM in AT and TAT
        this.AT = resultMachine;

        double ET = 0;
        double ER = 0;
        double[] CompletionTime = new double[ N ];

        for ( int i = 0; i < N; i++ ) {
            ET = cloudletList.get ( i ).getCloudletLength ( ) / ( vmlist.get ( AT[ i ] ).getMips ( ) * vmlist.get ( AT[ i ] ).getNumberOfPes ( ) );
            ER = ( double ) cloudletList.get ( i ).getCloudletFileSize ( ) / vmlist.get ( AT[ i ] ).getBw ( );
            CompletionTime[ i ] = ET + ER;
        }

        this.CT = CompletionTime;
    }

    // Randomly allocate tasks to VM
    public void randomAllocateTasksToVm ( int M, int N, List<Cloudlet> cloudletList, List<Vm> vmlist ) {
        int[] AllocationTask = new int[ N ];
        double[] CompletionTime = new double[ N ];
        double ET = 0;
        double ER = 0;
        for ( int i = 0; i < N; i++ ) {
            int k = ( int ) ( Math.random ( ) * M );
            AllocationTask[ i ] = M - k;
            ET = cloudletList.get ( i ).getCloudletLength ( ) / ( vmlist.get ( AllocationTask[ i ] ).getMips ( ) * vmlist.get ( AllocationTask[ i ] ).getNumberOfPes ( ) );
            ER = ( double ) cloudletList.get ( i ).getCloudletFileSize ( ) / vmlist.get ( AllocationTask[ i ] ).getBw ( );
            CompletionTime[ i ] = ET + ER;
        }
        this.AT = AllocationTask;
        this.CT = CompletionTime;
    }

    public int[] getAT ( ) {
        return AT;
    }

    public void setAT ( int[] AT ) {
        this.AT = AT;
    }

    public double[] getCT ( ) {
        return CT;
    }

    public void setCT ( double[] CT ) {
        this.CT = CT;
    }
}
