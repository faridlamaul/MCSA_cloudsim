package Simulation;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.DoubleStream;

import Algorithm.CSAOBL;
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

public class CloudsimCSAOBL {

    private static PowerDatacenter datacenter1, datacenter2, datacenter3, datacenter4, datacenter5, datacenter6;
    /**
     * The cloudlet list.
     */
    private static List<Cloudlet> cloudletList;

    /**
     * The vmlist.
     */
    private static List<Vm> vmlist;

    private static List<Vm> createVM ( int userId, int vms ) {

        //Creates a container to store VMs.
        //This list is passed to the broker later
        LinkedList<Vm> list = new LinkedList<Vm> ( );

        //VM Parameters
        long size = 10000; //Image size (MB)
        int[] ram = {512, 1024, 2048}; //VM memory (MB)
        int[] mips = {400, 500, 600}; //VM processing power (MIPS)
        long bw = 1000; //VM bandwith
        int pesNumber = 1; //Number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        Vm[] vm = new Vm[ vms ];

        for ( int i = 0; i < vms; i++ ) {
            //For loop to create a VM with a time shared scheduling policy for cloudlets:
            vm[ i ] = new Vm ( i, userId, mips[ i % 3 ], pesNumber, ram[ i % 3 ], bw, size, vmm, new CloudletSchedulerSpaceShared ( ) );
            list.add ( vm[ i ] );
        }

        return list;
    }

    private static ArrayList<Double> getSeedValue ( int cloudletcount ) {

        // Creating an arraylist to store Cloudlet Datasets
        ArrayList<Double> seed = new ArrayList<Double> ( );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/SDSC/SDSC.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple1000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple2000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple3000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple4000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple5000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple6000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple7000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple8000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple9000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple10000.txt" );
        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified1000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified2000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified3000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified4000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified5000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified6000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified7000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified8000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified9000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified10000.txt" );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratifiedTest100.txt" );


        try {
            // Opening and scanning the file
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/SDSC/SDSC.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple1000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple2000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple3000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple4000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple5000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple6000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple7000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple8000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple9000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Simple/RandSimple10000.txt" );
            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified1000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified2000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified3000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified4000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified5000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified6000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified7000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified8000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified9000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratified10000.txt" );
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/data/CSAOBL/Random/Stratified/RandStratifiedTest100.txt" );
            java.util.Scanner readFile = new java.util.Scanner ( fobj );

            while ( readFile.hasNextLine ( ) && cloudletcount > 0 ) {
                // Adding the file to the arraylist
                seed.add ( readFile.nextDouble ( ) );
                cloudletcount--;
            }
            readFile.close ( );

        } catch (FileNotFoundException e) {
            e.printStackTrace ( );
        }

        return seed;
    }

    private static ArrayList<Double> getAllocationTaskCSAValue ( int cloudletcount ) {

        ArrayList<Double> seed = new ArrayList<Double> ( );
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/SDSC/SDSCAllocationFromCSA.txt" ); // SDSC
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA1000.txt" ); // RandSimple1000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA2000.txt" ); // RandSimple2000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA3000.txt" ); // RandSimple3000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA4000.txt" ); // RandSimple4000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA5000.txt" ); // RandSimple5000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA7000.txt" ); // RandSimple7000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA9000.txt" ); // RandSimple9000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA10000.txt" ); // RandSimple10000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA6000.txt" ); // RandSimple6000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA8000.txt" ); // RandSimple8000
        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA1000.txt" ); // RandStratified1000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA2000.txt" ); // RandStratified2000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA3000.txt" ); // RandStratified3000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA4000.txt" ); // RandStratified4000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA5000.txt" ); // RandStratified5000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA6000.txt" ); // RandStratified6000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA7000.txt" ); // RandStratified7000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA8000.txt" ); // RandStratified8000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA9000.txt" ); // RandStratified9000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA10000.txt" ); // RandStratified10000
//        Log.printLine ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSATest100.txt" ); // RandStratifiedTest100

        try {
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/SDSC/SDSCAllocationFromCSA.txt" ); // SDSC
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA1000.txt" ); // RandSimple1000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA2000.txt" ); // RandSimple2000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA3000.txt" ); // RandSimple3000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA4000.txt" ); // RandSimple4000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA5000.txt" ); // RandSimple5000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA7000.txt" ); // RandSimple7000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA9000.txt" ); // RandSimple9000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA10000.txt" ); // RandSimple10000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA6000.txt" ); // RandSimple6000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Simple/SimpleRandomAllocationFromCSA8000.txt" ); // RandSimple8000
            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA1000.txt" ); // RandStratified1000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA2000.txt" ); // RandStratified2000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA3000.txt" ); // RandStratified3000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA4000.txt" ); // RandStratified4000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA5000.txt" ); // RandStratified5000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA6000.txt" ); // RandStratified6000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA7000.txt" ); // RandStratified7000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA8000.txt" ); // RandStratified8000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA9000.txt" ); // RandStratified9000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSA10000.txt" ); // RandStratified10000
//            File fobj = new File ( System.getProperty ( "user.dir" ) + "/allocation/CSA/Random/Stratified/StratifiedRandomAllocationFromCSATest100.txt" ); // RandStratifiedTest100
            java.util.Scanner readFile = new java.util.Scanner ( fobj );

            while ( readFile.hasNextLine ( ) && cloudletcount > 0 ) {
                seed.add ( readFile.nextDouble ( ) );
                cloudletcount--;
            }
            readFile.close ( );

        } catch (FileNotFoundException e) {
            e.printStackTrace ( );
        }

        return seed;
    }

    private static List<Cloudlet> createCloudlet ( int userId, int cloudlets ) {

        ArrayList<Double> randomSeed = getSeedValue ( cloudlets );

        // Creates a container to store Cloudlets
        LinkedList<Cloudlet> list = new LinkedList<Cloudlet> ( );

        //Cloudlet parameters
        long fileSize = 300; // Cloudlet file size (MB)
        long outputSize = 300; // Cloudlet file size (MB)
        int pesNumber = 1; // Cloudlet CPU needed to process
        UtilizationModel utilizationModel = new UtilizationModelFull ( );

        Cloudlet[] cloudlet = new Cloudlet[ cloudlets ];

        for ( int i = 0; i < cloudlets; i++ ) {
            long length = randomSeed.get ( i ).longValue ( );

            // Creating the cloudlet with all the parameter listed
            cloudlet[ i ] = new Cloudlet ( i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel );

            // setting the owner of these Cloudlets
            cloudlet[ i ].setUserId ( userId );
            list.add ( cloudlet[ i ] );
        }

        return list;
    }

    /**
     * Creates main() to run this example
     */
    public static void main ( String[] args ) {
        long startTime = System.nanoTime ( );
        Log.printLine ( "Starting Cloud Simulation Crow Search Algorithm - Opposition Based Learning ..." );

        try {
            // First step: Initialize the CloudSim package. It should be called before creating any entities.
            int num_user = 1;   // Number of grid users
            Calendar calendar = Calendar.getInstance ( );
            boolean trace_flag = false;  // Mean trace events
            int hostId = 0; // Starting host ID
            BufferedWriter outputWriter;

//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/SDSC/SDSCAllocationFromCSA.txt" ) ); // SDSC
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Simple/SimpleRandomAllocationFromCSA1000.txt" ) ); // RandSimple1000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Simple/SimpleRandomAllocationFromCSA2000.txt" ) ); // RandSimple2000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Simple/SimpleRandomAllocationFromCSA3000.txt" ) ); // RandSimple3000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Simple/SimpleRandomAllocationFromCSA4000.txt" ) ); // RandSimple4000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Simple/SimpleRandomAllocationFromCSA5000.txt" ) ); // RandSimple5000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Simple/SimpleRandomAllocationFromCSA6000.txt" ) ); // RandSimple6000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Simple/SimpleRandomAllocationFromCSA7000.txt" ) ); // RandSimple7000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Simple/SimpleRandomAllocationFromCSA8000.txt" ) ); // RandSimple8000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Simple/SimpleRandomAllocationFromCSA9000.txt" ) ); // RandSimple9000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Simple/SimpleRandomAllocationFromCSA10000.txt" ) ); // RandSimple10000
            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Stratified/StratifiedRandomAllocationFromCSA1000.txt" ) ); // RandStratified1000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Stratified/StratifiedRandomAllocationFromCSA2000.txt" ) ); // RandStratified2000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Stratified/StratifiedRandomAllocationFromCSA3000.txt" ) ); // RandStratified3000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Stratified/StratifiedRandomAllocationFromCSA4000.txt" ) ); // RandStratified4000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Stratified/StratifiedRandomAllocationFromCSA5000.txt" ) ); // RandStratified5000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Stratified/StratifiedRandomAllocationFromCSA6000.txt" ) ); // RandStratified6000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Stratified/StratifiedRandomAllocationFromCSA7000.txt" ) ); // RandStratified7000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Stratified/StratifiedRandomAllocationFromCSA8000.txt" ) ); // RandStratified8000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Stratified/StratifiedRandomAllocationFromCSA9000.txt" ) ); // RandStratified9000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Stratified/StratifiedRandomAllocationFromCSA10000.txt" ) ); // RandStratified10000
//            outputWriter = new BufferedWriter ( new FileWriter ( System.getProperty ( "user.dir" ) + "/allocation/CSAOBL/Random/Stratified/StratifiedRandomAllocationFromCSATest100.txt" ) ); // RandStratifiedTest100

            int vmNumber = 54; // The number of VMs created
//            int cloudletNumber = 7395; // The number of Tasks created SDSC
            int cloudletNumber = 1000; // The number of Tasks created RandSimple1000, RandStratified1000
//            int cloudletNumber = 2000; // The number of Tasks created RandSimple2000, RandStratified2000
//            int cloudletNumber = 3000; // The number of Tasks created RandSimple3000, RandStratified3000
//            int cloudletNumber = 4000; // The number of Tasks created RandSimple4000, RandStratified4000
//            int cloudletNumber = 5000; // The number of Tasks created RandSimple5000, RandStratified5000
//            int cloudletNumber = 6000; // The number of Tasks created RandSimple6000, RandStratified6000
//            int cloudletNumber = 7000; // The number of Tasks created RandSimple7000, RandStratified7000
//            int cloudletNumber = 8000; // The number of Tasks created RandSimple8000, RandStratified8000
//            int cloudletNumber = 9000; // The number of Tasks created RandSimple9000, RandStratified9000
//            int cloudletNumber = 10000; // The number of Tasks created RandSimple10000, RandStratified10000
//            int cloudletNumber = 100; // The number of Tasks created RandSimpleTest100, RandStratifiedTest100

            CloudSim.init ( num_user, calendar, trace_flag ); // Initialize the CloudSim library

            //Second step: Create Datacenters. Datacenters are the resource providers in CloudSim. We need at least one of them to run a CloudSim simulation
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


            //Third step: Create Broker
            DatacenterBroker broker = createBroker ( );
            assert broker != null;
            int brokerId = broker.getId ( );


            //Fourth step: Create VMs and Cloudlets
            vmlist = createVM ( brokerId, vmNumber ); //Creating vms
            cloudletList = createCloudlet ( brokerId, cloudletNumber ); // Creating cloudlets


            //Fifth step: Send VMs and Cloudlets to broker
            broker.submitVmList ( vmlist );
            broker.submitCloudletList ( cloudletList );

            //Sixth step: Use Crow Search Algorithm to create the initial population

            // Initializing parameters for Crow Search Algorithm
            double flightLength = 0.5; // Flight length
            double awarenessProbability = 0.1; // Awareness probability
            int maxIteration = 20; // Maximum number of iterations
            int[] allocatedTasks;
            double makespan;

            // Define random number generator
            Random rand = new Random ( );

            ArrayList<Double> csaAllocation = getAllocationTaskCSAValue ( cloudletNumber );

            CSAOBL csaobl = new CSAOBL ( );
            System.out.println ( "CSA - OBL Initialize Allocation Started!" );
            csaobl.randomAllocateTasksToVm ( vmNumber, cloudletNumber, cloudletList, vmlist, csaAllocation );

            allocatedTasks = csaobl.getAllocatedTasks ( );
            makespan = csaobl.getMakespan ( );

            for ( int i = 0; i < cloudletNumber; i++ ) {
                outputWriter.write ( Integer.toString ( allocatedTasks[ i ] ) );
                outputWriter.newLine ( );
            }

            outputWriter.flush ( );
            outputWriter.close ( );

            System.out.println ( "CSA - OBL Initialize Allocation Finished!" );
            System.out.println ( "CSA - OBL Iteration Started!" );
            while ( maxIteration > 0 ) {
                System.out.println ( "Running CSA - OBL Iteration: " + ( 21 - maxIteration ) );
                for ( int i = 0; i < cloudletNumber; i++ ) {
                    // Random select Task (to follow)
                    int taskToFollow = rand.nextInt ( cloudletNumber );
                    int vmToAssign;

                    // Initialize ri and rj
                    double ri = rand.nextDouble ( );
                    double rj = rand.nextDouble ( );

                    // Case 1: If rj > awarenessProbability, then vmToAssign = ATi + ri * flightLength * (ATj - ATi)
                    if ( rj >= awarenessProbability ) {
                        vmToAssign = ( int ) ( allocatedTasks[ i ] + ri * flightLength * ( allocatedTasks[ taskToFollow ] - allocatedTasks[ i ] ) );
                    }

                    // Case 2: If rj <= awarenessProbability, then vmToAssign = rand(0, vmNumber)
                    else {
                        vmToAssign = rand.nextInt ( vmNumber );
                    }

                    // Calculate Fitness Function
                    int[] tempAT = allocatedTasks.clone ( );
                    tempAT[ i ] = vmToAssign;

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

            System.out.println ( "CSA - OBL Iteration Finished!" );

            for ( int i = 0; i < cloudletNumber; i++ ) {
                broker.bindCloudletToVm ( cloudletList.get ( i ).getCloudletId ( ), vmlist.get ( allocatedTasks[ i ] ).getId ( ) );
            }

            //Seventh step: Starts the simulation
            CloudSim.startSimulation ( );

            //Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList ( );

            CloudSim.stopSimulation ( );

            printCloudletList ( newList );

            Log.printLine ( "Cloud Simulation Crow Search Algorithm - Opposition Based Learning finished!" );
        } catch (Exception e) {
            e.printStackTrace ( );
            Log.printLine ( "The simulation has been terminated due to an unexpected error" );
        }
        long endTime = System.nanoTime ( );
        long totalTime = endTime - startTime;
        System.out.println ( "Total Time to Run Program : " + totalTime / 1_000_000_000 + " seconds" );
    }

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
        //for a quad-core machine, a list of 4 PEs is required:
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


        //4. Create Hosts with its id and list of PEs and add them to the list of machines
        int ram = 128000; //Host memory (MB), Must be bigger than the VMs
        long storage = 1000000; //Host storage (MB)
        int bw = 10000; //Host bandwith
        int maxpower = 117; // Host Max Power
        int staticPowerPercentage = 50; // Host Static Power Percentage

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
        String vmm = "Xen";                // Name
        double time_zone = 10.0;        // Time zone this resource located
        double cost = 3.0;              // The cost of using processing in this resource
        double costPerMem = 0.05;        // The cost of using memory in this resource
        double costPerStorage = 0.1;    // The cost of using storage in this resource
        double costPerBw = 0.1;            // The cost of using bw in this resource
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


    private static DatacenterBroker createBroker ( ) {

        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker ( "Broker" );
        } catch (Exception e) {
            e.printStackTrace ( );
            return null;
        }
        return broker;
    }


    /**
     * Prints the Cloudlet objects
     *
     * @param list list of Cloudlets
     * @throws FileNotFoundException
     */
    private static void printCloudletList ( List<Cloudlet> list ) throws FileNotFoundException {

        // Initializing the printed output to zero
        int size = list.size ( );
        Cloudlet cloudlet = null;

        String indent = "    ";
        Log.printLine ( );
        Log.printLine ( "========== OUTPUT ==========" );
        Log.printLine ( "Cloudlet ID" + indent + "STATUS" + indent +
                "Data center ID" + indent + "VM ID" + indent + "Time"
                + indent + "Start Time" + indent + "Finish Time" + indent + "Waiting Time" );

        double waitTimeSum = 0.0;
        double CPUTimeSum = 0.0;
        int totalValues = 0;
        DecimalFormat dft = new DecimalFormat ( "###.##" );

        double[] response_time = new double[ size ];

        // Printing all the status of the Cloudlets
        for ( int i = 0; i < size; i++ ) {
            cloudlet = list.get ( i );
            Log.print ( cloudlet.getCloudletId ( ) + indent + indent );

            if ( cloudlet.getCloudletStatus ( ) == Cloudlet.SUCCESS ) {
                Log.print ( "SUCCESS" );
                CPUTimeSum = CPUTimeSum + cloudlet.getActualCPUTime ( );
                waitTimeSum = waitTimeSum + cloudlet.getWaitingTime ( );
                Log.printLine ( indent + indent + indent + ( cloudlet.getResourceId ( ) - 1 ) + indent + indent + indent + cloudlet.getVmId ( ) +
                        indent + indent + dft.format ( cloudlet.getActualCPUTime ( ) ) + indent + indent + dft.format ( cloudlet.getExecStartTime ( ) ) +
                        indent + indent + dft.format ( cloudlet.getFinishTime ( ) ) + indent + indent + indent + dft.format ( cloudlet.getWaitingTime ( ) ) );
                totalValues++;

                response_time[ i ] = cloudlet.getActualCPUTime ( );
            }
        }
        DoubleSummaryStatistics stats = DoubleStream.of ( response_time ).summaryStatistics ( );

        // Show the parameters and print them out
        Log.printLine ( );
        Log.printLine ( "Total CPU Time : " + CPUTimeSum );
        Log.printLine ( "Total Wait Time : " + waitTimeSum );
        Log.printLine ( "Total Cloudlets Finished : " + totalValues );

        // Average Cloudlets Finished
        Log.printLine ( "Average Cloudlets Finished : " + ( totalValues / size ) );

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

        // Save the output to a file
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/SDSCResult.txt" ) ); // SDSC
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/SimpleRandomResult1000.txt" ) ); // RandSimple1000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/SimpleRandomResult2000.txt" ) ); // RandSimple2000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/SimpleRandomResult3000.txt" ) ); // RandSimple3000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/SimpleRandomResult4000.txt" ) ); // RandSimple4000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/SimpleRandomResult5000.txt" ) ); // RandSimple5000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/SimpleRandomResult6000.txt" ) ); // RandSimple6000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/SimpleRandomResult7000.txt" ) ); // RandSimple7000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/SimpleRandomResult8000.txt" ) ); // RandSimple8000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/SimpleRandomResult9000.txt" ) ); // RandSimple9000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/SimpleRandomResult10000.txt" ) ); // RandSimple10000
        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/StratifiedRandomResult1000.txt" ) ); // RandStratified1000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/StratifiedRandomResult2000.txt" ) ); // RandStratified2000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/StratifiedRandomResult3000.txt" ) ); // RandStratified3000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/StratifiedRandomResult4000.txt" ) ); // RandStratified4000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/StratifiedRandomResult5000.txt" ) ); // RandStratified5000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/StratifiedRandomResult6000.txt" ) ); // RandStratified6000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/StratifiedRandomResult7000.txt" ) ); // RandStratified7000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/StratifiedRandomResult8000.txt" ) ); // RandStratified8000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/StratifiedRandomResult9000.txt" ) ); // RandStratified9000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/StratifiedRandomResult10000.txt" ) ); // RandStratified10000
//        PrintStream o = new PrintStream ( new File ( System.getProperty ( "user.dir" ) + "/output/CSAOBL/StratifiedRandomResultTest100.txt" ) ); // RandStratifiedTest100
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
