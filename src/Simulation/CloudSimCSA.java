package Simulation;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.DoubleStream;

import Algorithm.CSA;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicySimple;
import org.cloudbus.cloudsim.power.models.PowerModelLinear;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import static Algorithm.FitnessFunction.calculateFitness;

public class CloudSimCSA {
    // Set number of task
    private static final int nTask = 1000;

    // Set dataset directory path
    private static final String datasetName = "Stratified";
//    private static final String datasetName = "Simple";
//    private static final String datasetName = "SDSC";

    // Declare the variables for Datacenter
    private static PowerDatacenter datacenter1, datacenter2, datacenter3, datacenter4, datacenter5, datacenter6;

    // Declare the variables for List of Cloudlet
    private static List<Cloudlet> cloudletList;

    // Declare the variables for List of VM
    private static List<Vm> vmlist;

    // Method to create VMs
    private static List<Vm> createVM ( int userId, int vms ) {

        // Creates a container to store VMs. This listOfVM is passed to the broker later
        LinkedList<Vm> listOfVM = new LinkedList<Vm> ( );

        // Defines VM configuration parameters
        long size = 10000; // Storage (MB)
        int[] ram = {512, 1024, 2048}; // RAM (MB)
        int[] mips = {400, 500, 600}; // MIPS per CPU
        long bw = 1000; // Bandwidth (Mbps)
        int pesNumber = 1; // Number of CPU cores (Processing Elements, PEs)
        String vmm = "Xen"; // VMM

        // Creates VMs
        Vm[] vm = new Vm[ vms ];

        // Creates VMs with configuration parameters and add them to the listOfVM
        for ( int i = 0; i < vms; i++ ) {
            vm[ i ] = new Vm ( i, userId, mips[ i % 3 ], pesNumber, ram[ i % 3 ], bw, size, vmm, new CloudletSchedulerSpaceShared ( ) );
            listOfVM.add ( vm[ i ] );
        }

        return listOfVM;
    }

    // Method to store Cloudlet (Task) Data
    private static ArrayList<Double> getTaskValue ( int taskNum ) {

        // Creating listOfTask to store Tasks from the dataset
        ArrayList<Double> listOfTask = new ArrayList<Double> ( );

        try {
            if ( Objects.equals ( datasetName, "SDSC" ) ) {
                File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSA/SDSC/SDSC" + nTask + ".txt" );

                Scanner readFile = new java.util.Scanner ( fobj );

                while ( readFile.hasNextLine ( ) && taskNum > 0 ) {
                    // Adding the data to the listOfTask
                    listOfTask.add ( readFile.nextDouble ( ) );
                    taskNum--;
                }
                readFile.close ( );
            } else if ( Objects.equals ( datasetName, "Simple" ) ) {
                File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSA/Random/Simple/RandSimple" + nTask + ".txt" );

                Scanner readFile = new java.util.Scanner ( fobj );

                while ( readFile.hasNextLine ( ) && taskNum > 0 ) {
                    // Adding the data to the listOfTask
                    listOfTask.add ( readFile.nextDouble ( ) );
                    taskNum--;
                }
                readFile.close ( );
            } else {
                File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSA/Random/Stratified/RandStratified" + nTask + ".txt" );

                Scanner readFile = new Scanner ( fobj );

                while ( readFile.hasNextLine ( ) && taskNum > 0 ) {
                    // Adding the data to the listOfTask
                    listOfTask.add ( readFile.nextDouble ( ) );
                    taskNum--;
                }
                readFile.close ( );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace ( );
        }

        return listOfTask;
    }

    // Method to create Cloudlet (Task)
    private static List<Cloudlet> createCloudlet ( int userId, int cloudlets ) {

        // Creating listOfTask to store Tasks
        ArrayList<Double> listOfTask = getTaskValue ( cloudlets );

        // Creates a container to store Cloudlets. This listOfCloudlet is passed to the broker later
        LinkedList<Cloudlet> listOfCloudlet = new LinkedList<Cloudlet> ( );

        // Cloudlet properties
        long fileSize = 300; // Cloudlet file size (MB)
        long outputSize = 300; // Cloudlet file size (MB)
        int pesNumber = 1; // Cloudlet CPU needed to process
        UtilizationModel utilizationModel = new UtilizationModelFull ( );

        Cloudlet[] cloudlet = new Cloudlet[ cloudlets ];

        for ( int i = 0; i < cloudlets; i++ ) {
            long length = listOfTask.get ( i ).longValue ( );

            // Creating the cloudlet with all the parameter listed
            cloudlet[ i ] = new Cloudlet ( i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel );

            // Setting the owner of these Cloudlets
            cloudlet[ i ].setUserId ( userId );
            listOfCloudlet.add ( cloudlet[ i ] );
        }

        return listOfCloudlet;
    }

    // Main method to run the simulation
    public static void main ( String[] args ) {
        long startTime = System.nanoTime ( );
        Log.printLine ( "Starting Cloud Simulation Crow Search Algorithm ..." );

        try {
            // 1. Initialize the CloudSim package. It should be called before creating any entities.
            int num_user = 1;   // Number of grid users
            Calendar calendar = Calendar.getInstance ( ); // Calendar whose fields have been initialized with the current date and time.
            boolean trace_flag = false;  // Mean trace events
            int hostId = 0; // Starting host ID

            int vmNumber = 54; // The number of VMs created
            int cloudletNumber = nTask; // The number of Tasks created

            CloudSim.init ( num_user, calendar, trace_flag ); // Initialize the CloudSim library

            // 2. Create Datacenters. Datacenters are the resource providers in CloudSim.
            // We need at least one of them to run a CloudSim simulation
            datacenter1 = createDatacenter ( "DataCenter_1", hostId );
            hostId = 3;
            datacenter2 = createDatacenter ( "DataCenter_2", hostId );
            hostId = 6;
            datacenter3 = createDatacenter ( "DataCenter_3", hostId );
            hostId = 9;
            datacenter4 = createDatacenter ( "DataCenter_4", hostId );
            hostId = 12;
            datacenter5 = createDatacenter ( "DataCenter_5", hostId );
            hostId = 15;
            datacenter6 = createDatacenter ( "DataCenter_6", hostId );


            // 3. Create Broker
            DatacenterBroker broker = createBroker ( );
            assert broker != null;
            int brokerId = broker.getId ( );


            // 4. Create VMs and Cloudlets
            vmlist = createVM ( brokerId, vmNumber ); // Creating VMs
            cloudletList = createCloudlet ( brokerId, cloudletNumber ); // Creating Cloudlets


            // 5. Send VMs and Cloudlets to Broker
            broker.submitVmList ( vmlist );
            broker.submitCloudletList ( cloudletList );

            // 6. Use Crow Search Algorithm (CSA) to allocate Cloudlets to VMs

            // Initializing parameters for CSA (flightLength, awarenessProbability, maxIteration)
            double flightLength = 0.5; // Flight length
            double awarenessProbability = 0.1; // Awareness probability
            int maxIteration = 20; // Maximum number of iterations

            // Define variables to store the allocated tasks and makespan
            int[] allocatedTasks;
            double makespan;

            // Define variables to store random number
            Random rand = new Random ( );

            // Create an object of CSA class
            CSA csa = new CSA ( );

            System.out.println ( "CSA Initialize Allocation Started!" );

            // Generate allocation using CSA
            csa.randomAllocateTasksToVm ( vmNumber, cloudletNumber, cloudletList, vmlist );

            // Get the allocated tasks and makespan
            allocatedTasks = csa.getAllocatedTasks ( );
            makespan = csa.getMakespan ( );

            // Store the allocated tasks to file
            BufferedWriter outputWriter;

            if ( Objects.equals ( datasetName, "SDSC" ) ) {
                outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSA/SDSC/SDSCAllocationFromCSA" + nTask + ".txt" ) );

                for ( int i = 0; i < cloudletNumber; i++ ) {
                    outputWriter.write ( Integer.toString ( allocatedTasks[ i ] ) );
                    outputWriter.newLine ( );
                }

                outputWriter.flush ( );
                outputWriter.close ( );
            } else if ( Objects.equals ( datasetName, "Simple" ) ) {
                outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA" + nTask + ".txt" ) );

                for ( int i = 0; i < cloudletNumber; i++ ) {
                    outputWriter.write ( Integer.toString ( allocatedTasks[ i ] ) );
                    outputWriter.newLine ( );
                }

                outputWriter.flush ( );
                outputWriter.close ( );
            } else {
                outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA" + nTask + ".txt" ) );

                for ( int i = 0; i < cloudletNumber; i++ ) {
                    outputWriter.write ( Integer.toString ( allocatedTasks[ i ] ) );
                    outputWriter.newLine ( );
                }

                outputWriter.flush ( );
                outputWriter.close ( );
            }

            System.out.println ( "CSA Initialize Allocation Finished!" );
            System.out.println ( "CSA Iteration Started!" );

            // Running CSA Iteration
            while ( maxIteration > 0 ) {
                System.out.println ( "Running CSA Iteration: " + ( 21 - maxIteration ) );
                for ( int i = 0; i < cloudletNumber; i++ ) {

                    // Select a random task to follow
                    int taskToFollow = rand.nextInt ( cloudletNumber );

                    // Define a variable to store the alternative VM
                    int vmAlternative;

                    // Initialize ri and rj (random number between 0 and 1)
                    double ri = rand.nextDouble ( );
                    double rj = rand.nextDouble ( );

                    // Case 1 for CSA
                    if ( rj >= awarenessProbability ) {
                        vmAlternative = ( int ) ( allocatedTasks[ i ] + ri * flightLength * ( allocatedTasks[ taskToFollow ] - allocatedTasks[ i ] ) );
                    }

                    // Case 2 for CSA
                    else {
                        vmAlternative = rand.nextInt ( vmNumber );
                    }

                    // Calculate Fitness Function
                    int[] tempAT = allocatedTasks.clone ( );
                    tempAT[ i ] = vmAlternative;

                    System.out.println ( "Calculate fitness function Iteration: " + ( 21 - maxIteration ) );
                    double newMakespan = calculateFitness ( cloudletList, vmlist, tempAT );

                    // Feasibility check
                    System.out.println ( "Feasibility check Iteration: " + ( 21 - maxIteration ) );
                    if ( newMakespan < makespan ) {
                        makespan = newMakespan;
                        allocatedTasks = tempAT;
                    }
                }
                maxIteration--;
            }

            System.out.println ( "CSA Iteration Finished!" );

            // 7. Send the allocated tasks to broker
            for ( int i = 0; i < cloudletNumber; i++ ) {
                broker.bindCloudletToVm ( cloudletList.get ( i ).getCloudletId ( ), vmlist.get ( allocatedTasks[ i ] ).getId ( ) );
            }

            // 8. Starts the simulation
            CloudSim.startSimulation ( );

            // 9. Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList ( );

            CloudSim.stopSimulation ( );

            printCloudletList ( newList );

            Log.printLine ( "Cloud Simulation Crow Search Algorithm finished!" );
        } catch (Exception e) {
            e.printStackTrace ( );
            Log.printLine ( "The simulation has been terminated due to an unexpected error" );
        }
        // Print the total time taken to run the program
        long endTime = System.nanoTime ( );
        long totalTime = endTime - startTime;
        System.out.println ( "Total Time to Run Program : " + totalTime / 1_000_000_000 + " seconds" );
    }

    // Method to create Datacenter
    private static PowerDatacenter createDatacenter ( String name, int hostId ) {

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more machines
        List<PowerHost> hostList = new ArrayList<PowerHost> ( );


        // 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
        // create a list to store these PEs before creating a Machine.
        List<Pe> peList1 = new ArrayList<Pe> ( );
        List<Pe> peList2 = new ArrayList<Pe> ( );
        List<Pe> peList3 = new ArrayList<Pe> ( );

        int mipsunused = 300; // Unused core, only 3 cores will be able to process Cloudlets for this simulation
        int mips1 = 400; // The MIPS Must be bigger than the VMs
        int mips2 = 500;
        int mips3 = 600;


        // 3. Create PEs and add these into the list.
        // for a quad-core machine, a list of 4 PEs is required:
        peList1.add ( new Pe ( 0, new PeProvisionerSimple ( mips1 ) ) ); // need to store Pe id and MIPS Rating, Must be bigger than the VMs
        peList1.add ( new Pe ( 1, new PeProvisionerSimple ( mips1 ) ) );
        peList1.add ( new Pe ( 2, new PeProvisionerSimple ( mips1 ) ) );
        peList1.add ( new Pe ( 3, new PeProvisionerSimple ( mipsunused ) ) );
        peList2.add ( new Pe ( 4, new PeProvisionerSimple ( mips2 ) ) );
        peList2.add ( new Pe ( 5, new PeProvisionerSimple ( mips2 ) ) );
        peList2.add ( new Pe ( 6, new PeProvisionerSimple ( mips2 ) ) );
        peList2.add ( new Pe ( 7, new PeProvisionerSimple ( mipsunused ) ) );
        peList3.add ( new Pe ( 8, new PeProvisionerSimple ( mips3 ) ) );
        peList3.add ( new Pe ( 9, new PeProvisionerSimple ( mips3 ) ) );
        peList3.add ( new Pe ( 10, new PeProvisionerSimple ( mips3 ) ) );
        peList3.add ( new Pe ( 11, new PeProvisionerSimple ( mipsunused ) ) );


        // 4. Create Hosts with its id and list of PEs and add them to the list of machines
        // defines Host configuration parameters
        int ram = 128000; // RAM (MB), Must be bigger than the VMs
        long storage = 1000000; // Storage (MB)
        int bw = 10000; // Bandwith
        int maxpower = 117; // Max Power
        int staticPowerPercentage = 50; // Static Power Percentage

        hostList.add (
                new PowerHostUtilizationHistory (
                        hostId, new RamProvisionerSimple ( ram ),
                        new BwProvisionerSimple ( bw ),
                        storage,
                        peList1,
                        new VmSchedulerTimeShared ( peList1 ),
                        new PowerModelLinear ( maxpower, staticPowerPercentage ) ) );
        hostId++;

        hostList.add (
                new PowerHostUtilizationHistory (
                        hostId, new RamProvisionerSimple ( ram ),
                        new BwProvisionerSimple ( bw ),
                        storage,
                        peList2,
                        new VmSchedulerTimeShared ( peList2 ),
                        new PowerModelLinear ( maxpower, staticPowerPercentage ) ) );
        hostId++;

        hostList.add (
                new PowerHostUtilizationHistory (
                        hostId, new RamProvisionerSimple ( ram ),
                        new BwProvisionerSimple ( bw ),
                        storage,
                        peList3,
                        new VmSchedulerTimeShared ( peList3 ),
                        new PowerModelLinear ( maxpower, staticPowerPercentage ) ) );


        // 5. Create a DatacenterCharacteristics object that stores the
        // properties of a data center: architecture, OS, list of
        // Machines, allocation policy: time- or space-shared, time zone
        // and its price (G$/Pe time unit).
        String arch = "x86";            // System architecture
        String os = "Linux";            // Operating system
        String vmm = "Xen";             // VMM
        double time_zone = 10.0;        // Time zone this resource located
        double cost = 3.0;              // The cost of using processing in this resource
        double costPerMem = 0.05;       // The cost of using memory in this resource
        double costPerStorage = 0.1;    // The cost of using storage in this resource
        double costPerBw = 0.1;         // The cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage> ( );

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics (
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw );


        // 6. Finally, we need to create a PowerDatacenter object.
        PowerDatacenter datacenter = null;
        try {
            datacenter = new PowerDatacenter ( name, characteristics, new PowerVmAllocationPolicySimple ( hostList ), storageList, 9 ); // 15 --> is the cloud scheduling interval
        } catch (Exception e) {
            e.printStackTrace ( );
        }

        return datacenter;
    }

    // Method to create Datacenter Broker
    private static DatacenterBroker createBroker ( ) {

        DatacenterBroker broker;
        try {
            broker = new DatacenterBroker ( "Broker" );
        } catch (Exception e) {
            e.printStackTrace ( );
            return null;
        }
        return broker;
    }

    //
    private static void printCloudletList ( List<Cloudlet> list ) throws FileNotFoundException {

        // Initializing the printed output to zero
        int size = list.size ( );
        Cloudlet cloudlet = null;

        // Print output to tabular form

        Formatter formatterOutput = new Formatter ( );
        Log.printLine ( "========== OUTPUT ==========" );
        formatterOutput.format ( "%15s %15s %15s %15s %15s %15s %15s %15s\n", "Cloudlet ID", "STATUS", "Data center ID", "VM ID", "Time", "Start Time", "Finish Time", "Waiting Time" );
        formatterOutput.format ( "%15s %15s %15s %15s %15s %15s %15s %15s\n", "-----------", "-----------", "-----------", "-----------", "-----------", "-----------", "-----------", "-----------" );

        double waitTimeSum = 0.0;
        double CPUTimeSum = 0.0;
        int totalValues = 0;
        DecimalFormat dft = new DecimalFormat ( "###.##" );

        double[] response_time = new double[ size ];

        // Printing all the status of the Cloudlets
        for ( int i = 0; i < size; i++ ) {
            cloudlet = list.get ( i );

            if ( cloudlet.getCloudletStatus ( ) == Cloudlet.SUCCESS ) {
                CPUTimeSum = CPUTimeSum + cloudlet.getActualCPUTime ( );
                waitTimeSum = waitTimeSum + cloudlet.getWaitingTime ( );

                formatterOutput.format ( "%15s %15s %15s %15s %15s %15s %15s %15s\n", cloudlet.getCloudletId ( ), "SUCCESS", ( cloudlet.getResourceId ( ) - 1 ), cloudlet.getVmId ( ), dft.format ( cloudlet.getActualCPUTime ( ) ), dft.format ( cloudlet.getExecStartTime ( ) ), dft.format ( cloudlet.getFinishTime ( ) ), dft.format ( cloudlet.getWaitingTime ( ) ) );
                totalValues++;

                response_time[ i ] = cloudlet.getActualCPUTime ( );
            }
        }
        Log.printLine ( formatterOutput );

        DoubleSummaryStatistics stats = DoubleStream.of ( response_time ).summaryStatistics ( );

        // Show the parameters and print them out
        Log.printLine ( );
        Log.printLine ( "Total CPU Time : " + CPUTimeSum );
        Log.printLine ( "Total Wait Time : " + waitTimeSum );
        Log.printLine ( "Total Cloudlets Finished : " + totalValues );

        // Average Cloudlets Finished
        Log.printLine ( "Average Cloudlets Finished : " + ( CPUTimeSum / totalValues ) );

        // Average Start Time
        double totalStartTime = 0.0;
        for ( int i = 0; i < size; i++ ) {
            totalStartTime += list.get ( i ).getExecStartTime ( );
        }
        double avgStartTime = totalStartTime / size;
        Log.printLine ( "Average Start Time: " + avgStartTime );

        // Average Execution Time
        double totalExecTime = 0.0;
        for ( int i = 0; i < size; i++ ) {
            totalExecTime += list.get ( i ).getActualCPUTime ( );
        }
        double avgExecTime = totalExecTime / size;
        Log.printLine ( "Average Execution Time: " + avgExecTime );

        // Average Finish Time
        double totalFinishTime = 0.0;
        for ( int i = 0; i < size; i++ ) {
            totalFinishTime += cloudletList.get ( i ).getFinishTime ( );
        }
        double avgFinishTime = totalFinishTime / size;
        Log.printLine ( "Average FinishTime: " + avgFinishTime );

        // Average Waiting Time
        double totalWaitingTime = 0.0;
        for ( int i = 0; i < size; i++ ) {
            totalWaitingTime += cloudletList.get ( i ).getWaitingTime ( );
        }
        double avgWaitingTime = totalWaitingTime / size;
        Log.printLine ( "Average Waiting time: " + avgWaitingTime );

        // Throughput
        double maxFinishTime = 0.0;
        for ( int i = 0; i < size; i++ ) {
            double currentFT = cloudletList.get ( i ).getFinishTime ( );
            if ( currentFT > maxFinishTime ) {
                maxFinishTime = currentFT;
            }
        }
        double throughput = size / maxFinishTime;
        Log.printLine ( "Throughput: " + throughput );

        // Makespan
        assert cloudlet != null;
        double makespan = cloudlet.getFinishTime ( );
        Log.printLine ( "Makespan: " + makespan );

        // Imbalance Degree
        double imbalanceDegree = ( stats.getMax ( ) - stats.getMin ( ) ) / ( CPUTimeSum / totalValues );
        Log.printLine ( "Imbalance Degree: " + imbalanceDegree );

        // Scheduling Length
        double schedulingLength = waitTimeSum + makespan;
        Log.printLine ( "Total Scheduling Length: " + schedulingLength );

        // Resource Utilization
        double resourceUtilization = ( CPUTimeSum / ( makespan * 54 ) ) * 100;
        Log.printLine ( "Resouce Utilization: " + resourceUtilization );

        // Energy Consumption
        Log.printLine ( String.format ( "Total Energy Consumption: %.2f kWh",
                ( datacenter1.getPower ( ) + datacenter2.getPower ( ) + datacenter3.getPower ( ) + datacenter4.getPower ( ) + datacenter5.getPower ( ) + datacenter6.getPower ( ) ) / ( 3600 * 1000 ) ) );

        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSA/" + datasetName + "Result" + nTask + ".txt" ) );
        System.setOut ( o );

        System.out.println ( "Total CPU Time : " + CPUTimeSum );
        System.out.println ( "Total Wait Time : " + waitTimeSum );
        System.out.println ( "Total Cloudlets Finished : " + totalValues );
        System.out.println ( "Average Cloudlets Finished : " + ( totalValues / size ) );
        System.out.println ( "Average StartTime: " + avgStartTime );
        System.out.println ( "Average Execution Time: " + avgExecTime );
        System.out.println ( "Average Finish Time: " + avgFinishTime );
        System.out.println ( "Average Waiting time: " + avgWaitingTime );
        System.out.println ( "Throughput: " + throughput );
        System.out.println ( "Makespan: " + makespan );
        System.out.println ( "Imbalance Degree: " + imbalanceDegree );
        System.out.println ( "Total Scheduling Length: " + schedulingLength );
        System.out.println ( "Resouce Utilization: " + resourceUtilization );
        System.out.printf ( "Total Energy Consumption: %.2f kWh%n", ( datacenter1.getPower ( ) + datacenter2.getPower ( ) + datacenter3.getPower ( ) + datacenter4.getPower ( ) + datacenter5.getPower ( ) + datacenter6.getPower ( ) ) / ( 3600 * 1000 ) );

    }
}
