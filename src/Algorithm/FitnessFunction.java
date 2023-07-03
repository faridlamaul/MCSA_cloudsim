package Algorithm;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

import java.util.List;

public class FitnessFunction {
    public static double calculateFitness ( List<Cloudlet> cloudletList, List<Vm> vmlist, int[] tempAT ) {
        int N = cloudletList.size ( );
        int M = vmlist.size ( );

        double ET, ER;
        double[] CompletionTime = new double[ M ];
        double[] TaskRunTime = new double[ N ];
        double makespan = 0;

        for ( int i = 0; i < N; i++ ) {
            int vmId = tempAT[ i ];
            ET = cloudletList.get ( i ).getCloudletLength ( ) / ( vmlist.get ( vmId ).getMips ( ) * vmlist.get ( vmId ).getNumberOfPes ( ) );
            ER = ( double ) cloudletList.get ( i ).getCloudletFileSize ( ) / vmlist.get ( vmId ).getBw ( );
            TaskRunTime[ i ] = ET + ER;
            CompletionTime[ vmId ] += TaskRunTime[ i ];
        }

        for ( int i = 0; i < M; i++ ) {
            if ( CompletionTime[ i ] > makespan ) {
                makespan = CompletionTime[ i ];
            }
        }

        return makespan;
    }
}
