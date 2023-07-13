package Algorithm;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

import java.util.List;

public class FitnessFunction {
    // Calculate makespan based on the allocation of tasks to VMs
    public static double calculateFitness ( List<Cloudlet> cloudletList, List<Vm> vmlist, int[] allocatedTasks ) {
        int N = cloudletList.size ( );
        int M = vmlist.size ( );

        double ExecutionTime, TransmittingTime;
        double[] CompletionTime = new double[ M ];
        double[] TaskRuntime = new double[ N ];
        double makespan = 0;

        for ( int i = 0; i < N; i++ ) {
            int vmId = allocatedTasks[ i ];
            ExecutionTime = cloudletList.get ( i ).getCloudletLength ( ) / ( vmlist.get ( vmId ).getMips ( ) * vmlist.get ( vmId ).getNumberOfPes ( ) );
            TransmittingTime = ( double ) cloudletList.get ( i ).getCloudletFileSize ( ) / vmlist.get ( vmId ).getBw ( );
            TaskRuntime[ i ] = ExecutionTime + TransmittingTime;
            CompletionTime[ vmId ] += TaskRuntime[ i ];
        }

        for ( int i = 0; i < M; i++ ) {
            if ( CompletionTime[ i ] > makespan ) {
                makespan = CompletionTime[ i ];
            }
        }

        return makespan;
    }
}
