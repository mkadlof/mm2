import java.io.*;
import java.util.*;


// PRZED UZYCIEM nalezy:
// 1. wywolac setStaticParams(String sekwencja)
// 2. wywolac setUpOutput(String trajFileName) 
// tym razem jest tylko jeden ciagle otwarty strumien BufferedWriter, bo tylko trajektoria jest na tyle duza, aby trzeba byla ja na biezaco wypisywac do pliku
// NATOMIAST, gdy chcemy wypisac histogram/Cv/meanGyr - wywolujemy odpowiednia metode, ktora otwiera strumien

public class TrajectoryAndStats {

	class FrameIndexExceeded extends RuntimeException { }

	private static final int MAX_ENERGY = 100; // wieksze uklady sa i tak nieosiagalne
	private static final int BUFFER_SIZE = 10000; 
	private static char[] sequence; // sekwencja aminokwasowa lancucha; okazuje sie, ze warto to miec pod reka


	private int frameNumber;
	private int modelNumber; // do numerowania od poczatku (od 1), zeby wypisywac numer modelu w pliku PDB


	private Vec[][] traj;
	private int[] nOfContacts; // zeby wypisywac do plikow PyMOLowych liczbe kontaktow
	private BufferedWriter trajOutputStream;


	private List<Double> meanE;
	private List<Double> meanE2;
	private int[] hist;


	private List<Double> gyr;
	private List<Double> meanGyrs;


	private Chain lowestEnergyChain;







	public TrajectoryAndStats() { /* do testowania w Chain TYLKO */ }


	public TrajectoryAndStats(String trajFileName) throws IOException { 

		frameNumber = 0;
		modelNumber = 1; // bo w pymolu modele numeruje sie od 1

		int nOfAA = sequence.length;
		traj = new Vec[BUFFER_SIZE][nOfAA]; // wprowadzilem to ograniczenie, zeby nie zezrec przypadkiem calej pamieci w przypadku bardzo dlugiej symulacji
		

		nOfContacts = new int[BUFFER_SIZE];


		meanE = new ArrayList<Double>();
		meanE2 = new ArrayList<Double>();
		hist = new int[MAX_ENERGY];
		for(int i=0;i<MAX_ENERGY;i++)
			hist[i] = 0;	


		gyr = new ArrayList<Double>();
		meanGyrs = new ArrayList<Double>();


		setUpOutput(trajFileName);
	}

	public static void setStaticParams(char[] s) {
		sequence = s;
	}

	public void setUpOutput(String trajFileName) throws IOException {
		closeStream(); // na wszelki

		trajOutputStream = new BufferedWriter( new FileWriter( trajFileName ) );
	}


// BEGIN: NAJWAZNIEJSZA METODA TEJ KLASY
	public void update(Chain c) throws FrameIndexExceeded, IOException {
		
		if( frameNumber>=BUFFER_SIZE )
			throw new FrameIndexExceeded();


		updateLowest(c);
		

		updateTraj(c);


		updateHist(c);


		updateGyr(c);


		frameNumber++; // istotne
	}
// END: NAJWAZNIEJSZA METODA TEJ KLASY


	private void updateLowest(Chain c) {

		if( lowestEnergyChain==null )
			lowestEnergyChain = new Chain( c );
		else if( c.getContacts()>lowestEnergyChain.getContacts() ) 
			lowestEnergyChain = new Chain( c );

	}

	private void updateTraj(Chain c) throws IOException {

		Vec[] conf = c.getVecConfig();

		for(int i=0;i<conf.length;i++) 
			traj[frameNumber][i] = new Vec( conf[i] );


		nOfContacts[frameNumber] = c.getContacts();	

		
		if( frameNumber==BUFFER_SIZE-1 )
			outputPDB();
	}

	private void updateHist(Chain c) {
		int contacts = c.getContacts();
		
		hist[contacts]++;
	}

	private void updateGyr(Chain c) {
		gyr.add( c.computeGyr() );
	}

	public void outputPDB() throws IOException {

		for(int j=0;j<frameNumber;j++) {
			trajOutputStream.write("MODEL "+modelNumber+"\n");
			modelNumber++;

			trajOutputStream.write("COMMENT:	nOfContacts="+nOfContacts[j]+"\n");

			outputLoop(trajOutputStream,traj[j]);		
	
			trajOutputStream.write("ENDMDL\n");
		}

		frameNumber = 0; // istotne
	}

	public void outputLowestEnergyConfiguration(String fileName) throws IOException {
		
		BufferedWriter outputStream = null;
		
		try {
			outputStream = new BufferedWriter( new FileWriter( fileName ) );

			outputStream.write("COMMENT:	nOfContacts="+lowestEnergyChain.getContacts()+"\n");

			outputLoop(outputStream,lowestEnergyChain.getVecConfig());

		} finally {
			if( outputStream!=null )
			outputStream.close();
		}

	}

	public void outputOnePDB(String fileName, Chain c) throws IOException { // do testow, glownie
		Vec[] vecConfig = c.getVecConfig();

		BufferedWriter outputStream = null;

		try {
			outputStream = new BufferedWriter( new FileWriter( fileName ) );	

			outputStream.write("COMMENT: 	nOfContacts = "+c.getContacts()+"\n");

			outputLoop( outputStream , vecConfig );

		} finally {
			if( outputStream!=null ) 
				outputStream.close();
		}
	}


	private void outputLoop(Writer outputStream, Vec[] vecConfig) throws IOException {	

		for(int i=0;i<vecConfig.length;i++) {
			String tmpAA = "";
			if( sequence[i]=='H' )
				 tmpAA = "ALA";
			else
				tmpAA = "LYS";
	
			outputStream.write(String.format("ATOM  %5d  CA  %3s%4d  %c  %s",i,tmpAA,i,'A',vecConfig[i]));
		}
	}

	public void outputHist(String fileName) throws IOException {
		int max = lowestEnergyChain.getContacts();

		BufferedWriter outputStream = null;

		try {
			outputStream = new BufferedWriter( new FileWriter( fileName ) );

			for(int i=0;i<=max+1;i++)
				outputStream.write(String.format("%d	%d\n",i,hist[i]));	

		} finally {
			if( outputStream!=null )
				outputStream.close();
		}
	}

	public void outputMeanGyrs(String fileName, double Tinit, double dT) throws IOException {

		BufferedWriter outputStream = null;

		try {
			outputStream = new BufferedWriter( new FileWriter( fileName ) );

			double T = Tinit;
			Iterator<Double> it = meanGyrs.iterator();

			while( it.hasNext() ) {
				outputStream.write(String.format("%f	%f\n",T,it.next()));
				it.remove();
				T -= dT;
			}
		} finally {
			if( outputStream!=null )
				outputStream.close();
		}
	}

	public void outputCv(String fileName, double Tinit, double dT) throws IOException {
		
		BufferedWriter outputStream = null;

		try {
			outputStream = new BufferedWriter( new FileWriter( fileName ) );

			double Cv;
			double T = Tinit;
			Iterator<Double> itE = meanE.iterator();
			Iterator<Double> itE2 = meanE2.iterator();

			while( itE.hasNext() ) {
				Cv = ( itE2.next() - Math.pow( itE.next(), 2.0 ) ) /T/T;
				outputStream.write(String.format("%f	%f\n",T,Cv));

				itE.remove();
				itE2.remove();

				T -= dT;
			}
		} finally {
			if( outputStream!=null )
				outputStream.close();
		}
	}

	private double computeMeanGyr() {
		
		double I = 0.0;
		int size = gyr.size();

		Iterator<Double> it = gyr.iterator();
		while( it.hasNext() ) {
			I += it.next();
			it.remove();
		}
	
		I /= size;

		return I;
	}

	private double computeMeanE() {
		double meanE = 0.0;
		int counts = hist[0];

		for(int i=1;i<MAX_ENERGY;i++) {
			meanE += i*hist[i];
			counts += hist[i];
		}

		return meanE/counts;
	}

	private double computeMeanE2() {
		double meanE2 = 0.0;
		int counts = hist[0];


		for(int i=1;i<MAX_ENERGY;i++) {
			meanE2 += i*i*hist[i];
			counts += hist[i];
		}

		return meanE2/counts;
	}

	private void closeStream() {
	
		if( trajOutputStream!=null && frameNumber!=0 ) {
			try {
				outputPDB();
			} catch(IOException e) {
				System.out.printf("blad przy outputPDB w closeStream: %s\n",e);
			}
		}

		try {
			if( trajOutputStream!=null ) 
				trajOutputStream.close();
		} catch(IOException e) {
			System.out.printf("blad przy zamykaniu strumieni w closeStream:\n%s\n",e);
		}
	}

	public void finalize() {
		closeStream();


		meanE.add( computeMeanE() );
		meanE2.add( computeMeanE2() );
//System.out.printf("meanE:   %f\n",computeMeanE());
//System.out.printf("meanE2:  %f\n",computeMeanE2());


		frameNumber = 0;
		modelNumber = 1;
		lowestEnergyChain = null;


		for(int i=0;i<MAX_ENERGY;i++)
			hist[i] = 0;	
		

		meanGyrs.add( computeMeanGyr() );
		if( gyr.size()!=0)
			System.out.printf("NIE oprozniono gyr!\n");
	}


	public static void main(String args[]) {

		String sequence = "HPHPHPH";
		Thermostat.setStaticParams(sequence);

		TrajectoryAndStats t = null;
		Chain c = new Chain();

		try{
			t = new TrajectoryAndStats("trajTrajectoryAndStatsTest");
			t.update(c);
			t.outputPDB();
			t.outputHist("histTrajectoryAndStatsTest");
		} catch(IOException e) {
			System.out.printf("dostalem IOException w mainie TrajectoryAndStats:\n%s\n",e);
		} finally {
			t.closeStream();
		}
	}
}


