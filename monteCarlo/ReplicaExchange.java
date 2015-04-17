import java.util.Random;

public class ReplicaExchange {

	private Thermostat[] thermostats;
	private double Thighest;
	private int nOfThermostats;
	private double dT;
	private int triesPerCycle;
	private int nOfExchanges;
	private double epsilon;

	private Random rGen;

	private String directory;


	public ReplicaExchange(double Thighest, int nOfThermostats, double dT, int triesPerCycle, int nOfExchanges, double epsilon, String directory) {

		this.Thighest = Thighest;
		this.nOfThermostats = nOfThermostats;
		this.dT = dT;
		this.triesPerCycle = triesPerCycle;
		this.nOfExchanges = nOfExchanges;

		rGen = new Random(44);

		this.directory = directory;
	}

	public void initializeThermostats(Thermostat[] thermostats) {
		this.thermostats = thermostats;
	}


// BEGIN: NAJWAZNIEJSZA METODA TEJ KLASY
	public void carryOut() {

		while( nOfExchanges>0 ) { // nOfExchanges jest dekrementowane w metodzie exchange()

			evolveThermostats();

			if( exchange() )
				nOfExchanges--; // tricky!
		}

		for(int i=0;i<nOfThermostats;i++)
			thermostats[i].finalize();

	}
// END: NAJWAZNIEJSZA METODA TEJ KLASY


	private void evolveThermostats() {
		for(int i=0;i<nOfThermostats;i++) {
			thermostats[i].transformKtimes(triesPerCycle);
//TODO:			thermostats[i].outputLowestEnergyConfiguration(directory+"/lowEnergyConfT"+
		}
	}

	private boolean exchange() {

		int exchangedPair = rGen.nextInt(nOfThermostats-1);

	
		Thermostat a = thermostats[exchangedPair];
		Thermostat b = thermostats[exchangedPair+1];

		double Ta = a.getT();
		double Tb = b.getT();

		Chain chainA = a.getChain();
		Chain chainB = b.getChain();

		double Ea = -epsilon * chainA.getContacts();	
		double Eb = -epsilon * chainB.getContacts();

		double delta = (1/Tb - 1/Ta)*(Ea - Eb);

		if( rGen.nextDouble()<Math.exp(-delta) ) {

			System.out.printf("Zamiana %d<->%d\n",exchangedPair,exchangedPair+1);

			a.setChain(chainB);
			b.setChain(chainA);

			return true;
		}

		return false;
	}

	public static void main(String[] args) {

		double Thighest = 0.8;
		int nOfThermostats = 13;
		double dT = 0.05;
		double Tlowest = Thighest - nOfThermostats*dT;
		int triesPerCycle = 100;
		int nOfExchanges = 100;
		double epsilon = 1.0;

		String directory = "RE_default_"+Thighest+"-"+Tlowest;
		String sequence = "PHPPHPPHHPPHHPPHPPHP";


// WCZYTUJE DANE Z LINII KOMEND:
		if( args.length==0 ) {
			System.out.printf("przykladowe uruchomienie: \n");
			System.out.printf("java ReplicaExchange HPPHPH ./przedrostekKatalogu\n");
		} 
		else if( args.length==1 ) 
			sequence = args[0];		
		else if( args.length>1 ) 
			directory = args[1]+Thighest+"-"+Tlowest;
	
		System.out.printf("sekwencja:             %s\n",sequence);		
		System.out.printf("katalog z outputem:    %s\n",directory);

// USTAWIAM STATYCZNE ATRYBUTY KLAS
		Thermostat.setStaticParams(sequence);


// NAJPIERW TWORZE TABLICE TERMOSTATOW...
		Thermostat[] thermostats = new Thermostat[ nOfThermostats ];
		double T = Thighest;
		for(int i=0;i<nOfThermostats;i++) {
			thermostats[i] = new Thermostat(T,epsilon, new Chain() );

			String Tshort = String.format("%.2f",T);
			String trajFileName = directory + "/traj" + Tshort;
			thermostats[i].setUpOutput(trajFileName);

			T -= dT;
		}

// ...NASTEPNIE OBIEKT WYMIANY REPLIK...
		ReplicaExchange testRE = new ReplicaExchange(Thighest,nOfThermostats,dT,triesPerCycle,nOfExchanges,epsilon,directory);

// ...PO CZYM PRZEKAZUJE TERMOSTATY SYMULATOROWI:
		testRE.initializeThermostats(thermostats);

		testRE.carryOut();

	}
}
