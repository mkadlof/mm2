public class SimulatedAnnealing {


	private Thermostat thermostat;
	private double Tinit;
	private double Tfinal;
	private double dT;
	private int triesPerT;
	
	private String directory;


	public SimulatedAnnealing(double Tinit, double Tfinal, double dT, int triesPerT, String directory) {

		this.Tinit = Tinit;
		this.Tfinal = Tfinal;
		this.dT = dT;
		this.triesPerT = triesPerT;
		this.directory = directory;

	}

	public void initializeThermostat(Thermostat thermostat) {
		this.thermostat = thermostat;
	}
	
	public void initializeDirectory(String directory) {
		this.directory = directory;
	}

	public void carryOut() {
		
		double T = Tinit;
	
		while( T>=Tfinal ) {
			System.out.printf("pracuje z T=%.2f\n",T);
			thermostat.setT(T);
		
			String Tshort = String.format("%.2f",T);
			String trajFileName = directory + "/traj" + Tshort;
			thermostat.setUpOutput(trajFileName);


			thermostat.transformKtimes(triesPerT);


			thermostat.outputLowestEnergyConfiguration(directory+"/lowEnerConfT"+Tshort);

			String histFileName = directory + "/hist" + Tshort;
			thermostat.outputHist(histFileName);	


			thermostat.finalize();

			T -= dT;
		}

		String meanGyrFileName = directory + "/meanGyr";
		thermostat.outputMeanGyrs(meanGyrFileName,Tinit,dT);

		String CvFileName = directory + "/Cv";
		thermostat.outputCv(CvFileName,Tinit,dT);

		thermostat.finalize();
	}



	public static void main(String[] args) {

		double Tinit = 1.0;
		double Tfinal = 0.1;
		double dT = 0.05;
		int triesPerT = 10000;
		double epsilon = 1.0;

		String directory = "./SA_default_"+Tinit+"-"+Tfinal;
//		String directory = "./niezwijajace";

		String sequence = "PHPPHPPHHPPHHPPHPPHP";
//		String sequence = "HPPPHHPPHPHHHHHH";
//		String sequence = "HPPPHHPPHPHHPHHH";

// WCZYTUJE DANE Z LINII KOMEND:
		if( args.length==0 ) {
			System.out.printf("przykladowe uruchomienie: \n");
			System.out.printf("java SimulatedAnnealing HPPHPH ./przedrostekKatalogu\n");
		} 
		else if( args.length==1 ) 
			sequence = args[0];		
		else if( args.length>1 ) 
			directory = args[1]+Tinit+"-"+Tfinal;
	
		System.out.printf("sekwencja:             %s\n",sequence);		
		System.out.printf("katalog z outputem:    %s\n",directory);


// USTAWIAM STATYCZNE ATRYBUTY KLAS
		Thermostat.setStaticParams(sequence);


// NAJPIERW TWORZE TERMOSTAT...
		Thermostat thermostat = new Thermostat(Tinit,epsilon, new Chain() );

// ...NASTEPNIE OBIEKT SYMULOWANEGO WYZARZANIA...
		SimulatedAnnealing testSA = new SimulatedAnnealing(Tinit,Tfinal,dT,triesPerT,directory);

// ...PO CZYM PRZEKAZUJE TERMOSTAT SYMULATOROWI
		testSA.initializeThermostat(thermostat);



		testSA.carryOut();
	}
}
