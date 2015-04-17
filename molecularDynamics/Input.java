import java.io.*;
import java.util.Scanner;

public class Input {


	public Input() {

	}



	public static Simulation uploadSimulation(Simulation sim, String initFile) throws FileNotFoundException {
// wartosci domyslne sa nastepujace (kolejnosc wg pliku inicjujacego):
		int dim = 3; // wymiarowosc
		int nOfSteps = 10000; // liczba krokow
		int trajInterval = 1; // co ile krokow zapisywac wspolrzedne
		int energyInterval = 1; // co ile krokow zapisywac energie
		String coordsFile = "coordsFileExample.txt"; // plik wejsciowy ze wspolrzednymi

		// parametry do VanDerWaalsa:
		double r = 4.0; 
		double epsilon = 0.4;
		// parametry do SoftWalls:
		double f = 0.5;
		double l = 12.0;
		// parametry do TwoWells:
		double a = 5.0;
		double b = 1.0;
		double c = 10.0;
		double d = 2.0;


		ForceField ff = new ForceFieldDecorator( new NoField() );


		double dt = 0.001; // krok czasowy
		Integrator intg = new Verlet(dt); // algorytm calkujacy

		if( sim==null )
			sim = new Simulation();	
	
// ZACZYNAMY:
		Scanner sc = new Scanner( new FileReader(initFile) ); // KOMENTARZ IV
		try {

			String line = sc.nextLine();
			String[] words = line.split(" ");


// WCZYTUJE WYMIAR:
			if( words[0].equals("dim:") ) {
				dim = Integer.parseInt(words[1]);
				System.out.printf("Wymiarow: %d\n",dim);

				line = sc.nextLine();
				words = line.split(" ");
			}
			else 
				System.out.printf("Domyslnie wymiarow: 3\n");
// i od razu ustawiam statyczne pole "dim" w klasie "VecNd":
			VecNd.setDim(dim);


// WCZYTUJE LICZBE KROKOW:
			if( words[0].equals("n_of_steps:") ) {
				nOfSteps = Integer.parseInt(words[1]);	
				System.out.printf("Liczba krokow: %d\n",nOfSteps);
	
				line = sc.nextLine();
				words = line.split(" ");
			}	
			else 
				System.out.printf("Domyslna liczba krokow: %d\n",nOfSteps);

			sim.setNOfSteps(nOfSteps);


// WCZYTUJE INTERWALY:
			if( words[0].equals("trajectory_interval:") ) {
				trajInterval = Integer.parseInt(words[1]);
				System.out.printf("Wspolrzedne atomow wypisywane beda co %d krokow dynamiki\n",trajInterval);

				line = sc.nextLine();
				words = line.split(" ");
			}
			else
				System.out.printf("Domyslny trajInterval: %d\n",trajInterval);

			sim.setTrajInterval(trajInterval);


			if( words[0].equals("energy_interval:") ) {
				energyInterval = Integer.parseInt(words[1]);
				System.out.printf("Energie zapamietywane beda co %d krokow dynamiki\n",energyInterval);

				line = sc.nextLine();
				words = line.split(" ");
			}
			else
				System.out.printf("Domyslny energyInterval: %d\n",energyInterval);

			sim.setEnergyInterval(energyInterval);

// WCZYTUJE WSPOLRZEDNE:
			if( words[0].equals("coords:") ) {
				coordsFile = words[1];
				System.out.printf("Wspolrzedne z: %s\n",coordsFile);

				line = sc.nextLine();
				words = line.split(" ");
			}
			else 
				System.out.printf("Domyslny plik ze wspolrzednymi: %s\n",coordsFile);
// i od razu wczytuje uklad
			TheSystem system = new TheSystem();
			uploadTheSystem(system,coordsFile); // "wynik idzie w proznie"

			sim.setTheSystem(system);
// badz, w jednej linijce (zobacz KOMENTARZ V):
//	 		sim.setTheSystem( uploadTheSystem(null,coordsFile) );
			sim.setTrajectoryAndStats( new TrajectoryAndStats( system.getNOfAtoms(), nOfSteps ) );



// WCZYTUJE POLE SILOWE, :
			while( words[0].equals("forcefield:") ) {
				if( words[1].equals("vdW") ) {
					r = Double.parseDouble(words[2]);
					epsilon = Double.parseDouble(words[3]);	

			
					ff = new VanDerWaals(ff,r,epsilon);
				}
				if( words[1].equals("soft") ) {
					f = Double.parseDouble(words[2]);
					l = Double.parseDouble(words[3]);


					ff = new SoftWalls(ff,f,l);
				}
				if( words[1].equals("morse") ) {
					//TODO		
				}
				if( words[1].equals("two_wells") ) {
					a = Double.parseDouble(words[2]);
					b = Double.parseDouble(words[3]);	
					c = Double.parseDouble(words[4]);	
					d = Double.parseDouble(words[5]);	

			
					ff = new TwoWells2(ff,a,b,c,d);
				}
		
				line = sc.nextLine();
				words = line.split(" ");
			}
			//else 
			//	System.out.printf("Domyslne pole silowe: vdW z r=%.2f oraz epsilon=%.2f\n",r,epsilon,f,l);

			System.out.printf("pole silowe:\n%s\n",ff);

			sim.setForceField(ff);



// WCZYTUJE KROK CZASOWY:
			if( words[0].equals("dt:") ) {
				dt = Double.parseDouble(words[1]);
				System.out.printf("Krok czasowy: %f\n",dt);
				line = sc.nextLine();
				words = line.split(" ");
			}
			else
				System.out.printf("Domyslny krok czasowy: %.2f\n",dt);

				sim.setDt(dt);


// WCZYTUJE ALGORYTM CALKUJACY:
			if( words[0].equals("integrator:") ) {
				System.out.printf("Algorytm calkujacy: %s\n",words[1]);

				if( words[1].equals("verlet") ) 
					intg = new Verlet(dt);

				else if( words[1].equals("leapfrog") ) 
					intg = new Leapfrog(dt);

				else if( words[1].equals("velocity_verlet") ) { 
					intg = new VelocityVerlet(dt);
					System.out.printf("To jest przestarzala i niepoprawna wersja algorytmu Velocity Verlet!!\n");
				}

				else if( words[1].equals("velocity_verlet3") ) 
					intg = new VelocityVerlet3(dt);

				else if( words[1].equals("beeman") ) 
					intg = new Beeman(dt);

				else if( words[1].equals("langevin") ) { 
					double gamma = Double.parseDouble(words[2]);	
					double T = Double.parseDouble(words[3]);
					intg = new Langevin(dt,gamma,T);	
				}

				else if( words[1].equals("brown") ) { 
					double gamma = Double.parseDouble(words[2]);	
					double T = Double.parseDouble(words[3]);
					intg = new Brown(dt,gamma,T);	
				}

				else if( words[1].equals("andersen") ) { 
					double nu = Double.parseDouble(words[2]);	
					double T = Double.parseDouble(words[3]);
					intg = new Andersen(dt,nu,T);	
				}

				else
					System.out.printf("nieznane\n");

				line = sc.nextLine();
				words = line.split(" ");
			}
			else {
				intg = new Verlet(dt);
				System.out.printf("Domyslny algorytm calkujacy: verlet\n");
			}

		sim.setIntegrator(intg);
	

			
		} finally { // nie obsluguje zadnych wyjatkow, chce tylko bezpiecznie zamknac otwarty strumien:
			sc.close();
		}



		return sim;
	}	



	private static TheSystem uploadTheSystem(TheSystem system, String coordsFile) throws FileNotFoundException { // KOMENTARZ V
		if( system==null ) 
			system = new TheSystem(); 

		Scanner sc = new Scanner( new FileReader(coordsFile) );
		try { 

			String line = sc.nextLine(); // KOMENTARZ VI

			int nOfAtoms = Integer.parseInt(line); // KOMENTARZ VII
			Atom[] atoms = new Atom[nOfAtoms];

			for(int i=0;i<nOfAtoms;i++) {
				line = sc.nextLine();

				String[] words = line.split(" ");


				int dim = VecNd.getDim();
				double[] tmp = new double[dim];  

				for(int j=0;j<dim;j++)
					tmp[j] = Double.parseDouble(words[j]);
				VecNd initX = new VecNd(tmp);

				for(int j=0;j<dim;j++) 
					tmp[j] = Double.parseDouble(words[3+j]);
				VecNd initV = new VecNd(tmp);


				VecNd zeroF = new VecNd();

				atoms[i] = new Atom(1.0,initX,initV); // TODO: wszystkie atomy maja jednakowa mase i tego na razie sie nie da zmienic
			}

			system.setAtoms(atoms);

		} finally {
			sc.close(); // KOMENTARZ IX
		}

		return system;
	}

/*
	public static void setUpOutputStream(String outputFileWithPath) throws FileNotFoundException, IOException {
		outputStream = new BufferedWriter( new FileWriter( outputFileWithPath ) );	
	}

	public static void write(String s) {
		try {
			outputStream.write(s);
			outputStream.newLine();
		} catch(IOException e) {
			System.out.printf("Wyjatek przy wypisywaniu do pliku, nic na to nie poradze\n");
		} 
	}	

	public static void closeStream() throws IOException {
		if( outputStream!=null ) 
				outputStream.close();
	}
*/
}
