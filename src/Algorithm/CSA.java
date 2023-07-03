package Algorithm;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

import java.util.List;

public class CSA {
    public int[] allocatedTasks;

    public double makespan;

    // Randomly allocate tasks to VM
    public void randomAllocateTasksToVm ( int M, int N, List<Cloudlet> cloudletList, List<Vm> vmlist ) {
        int[] Task = new int[ N ];
        double ExecutionTime;
        double ER;
        double[] TaskRunTime = new double[ N ];
        double[] CompletionTime = new double[ M ];
        for ( int i = 0; i < N; i++ ) {
            int k = ( int ) ( Math.random ( ) * M );
            Task[ i ] = k;
            ExecutionTime = cloudletList.get ( i ).getCloudletLength ( ) / ( vmlist.get ( Task[ i ] ).getMips ( ) * vmlist.get ( Task[ i ] ).getNumberOfPes ( ) );
            ER = ( double ) cloudletList.get ( i ).getCloudletFileSize ( ) / vmlist.get ( Task[ i ] ).getBw ( );
            TaskRunTime[ i ] = ExecutionTime + ER;
            CompletionTime[ Task[ i ] ] += TaskRunTime[ i ];
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
