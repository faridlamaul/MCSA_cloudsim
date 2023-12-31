package Algorithm;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

import java.util.ArrayList;
import java.util.List;

public class CSAOBL {
    public int[] allocatedTasks;

    public double makespan;

    // Randomly allocate tasks to VM based on opposite allocation from CSA
    public void randomAllocateTasksToVm ( int M, int N, List<Cloudlet> cloudletList, List<Vm> vmlist, ArrayList<Double> csaAllocation ) {
        int[] Task = new int[ N ];
        double ExecutionTime;
        double TransmittingTime;
        double[] TaskRuntime = new double[ N ];
        double[] CompletionTime = new double[ M ];
        for ( int i = 0; i < N; i++ ) {
            int k = csaAllocation.get ( i ).intValue ( );
            Task[ i ] = ( M - 1 ) - k;
            ExecutionTime = cloudletList.get ( i ).getCloudletLength ( ) / ( vmlist.get ( Task[ i ] ).getMips ( ) * vmlist.get ( Task[ i ] ).getNumberOfPes ( ) );
            TransmittingTime = ( double ) cloudletList.get ( i ).getCloudletFileSize ( ) / vmlist.get ( Task[ i ] ).getBw ( );
            TaskRuntime[ i ] = ExecutionTime + TransmittingTime;
            CompletionTime[ Task[ i ] ] += TaskRuntime[ i ];
        }
        this.allocatedTasks = Task;

        for ( int i = 0; i < M; i++ ) {
            if ( CompletionTime[ i ] > makespan )
                this.makespan = CompletionTime[ i ];
        }
    }

    public int[] getAllocatedTasks ( ) {
        return allocatedTasks;
    }

    public double getMakespan ( ) {
        return makespan;
    }
}
