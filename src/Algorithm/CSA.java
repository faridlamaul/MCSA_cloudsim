package Algorithm;

public class CSA {
    public static void main ( String[] args ) {
        int NTask = 5;
        int NVM = 5;
        double AP = 0.1;
        double fl = 0.1;
        csa ( NTask, NVM, AP, fl );
    }

    public static void csa ( int NTask, int NVM, double AP, double fl) {
        // Inisialisasi posisi awal Task dan VM
        double[][] x = Init.init ( NTask, NVM );
        for ( int i = 0; i < NTask; i++ ) {
            for ( int j = 0; j < NVM; j++ ) {
                System.out.println ( "x[" + i + "][" + j + "] : " + x[i][j] );
            }
        }

        // Inisialisasi memori (pemetaan antara task ke i pada VM ke j)
        

    }
}
