import java.io.*;
import java.util.*;

public class Simulation
{
	private int nOfSteps;
	private TheSystem system;
	private ForceField ff;
	private double dt;
	private Integrator intg;
	private TrajectoryAndStats trajAndStats;
	private int trajInterval;
	private int energyInterval;

	public Simulation() {
	}

	public void setNOfSteps(int nOfSteps) {
		this.nOfSteps = nOfSteps;
	}

	public void setTrajInterval(int trajInterval) {
		this.trajInterval = trajInterval;
	}

	public void setEnergyInterval(int energyInterval) {
		this.energyInterval = energyInterval;
	}

	public void setTheSystem(TheSystem system) {
		this.system = system;
	}

	public void setForceField(ForceField ff) {
		this.ff = ff;
	}

	public void setDt(double dt) {
		this.dt = dt;
	}

	public void setTrajectoryAndStats(TrajectoryAndStats trajAndStats) {
		this.trajAndStats = trajAndStats;
	}

	public void setIntegrator(Integrator intg) {
		this.intg = intg;
	}




	public void carryOutTheSimulation() {

		for(int i=0;i<nOfSteps;i++) {

			ff.forcesAndEnergies(system);

			intg.step(system,ff); // tu update'owane sa polozenia, predkosci i sily, stawiany jest kolejny krok dynamiki	
	
			if( i%trajInterval==0 )
				trajAndStats.updateTraj(system);
			if( i%energyInterval==0 )
				trajAndStats.updateEnergy(system);
			if( i%10000==0 || i==nOfSteps-1 ) 
				System.out.printf("%.1f procent symulacji przeprowadzono\n",100.*i/nOfSteps);
		}
		System.out.printf("Symulacja zakonczona pomyslnie!\n");
		trajAndStats.finalize();
	}







	public static void main(String[] args) {
		if( args.length==0 )
			throw new ArgumentyException("Potrzebuje initFile! \nPrzykladowe uruchomienie:\njava Simulation initExample.txt ./output/domyslnyPlik.xyz ./output/energie.txt\n");
		if( args.length>3 )
			throw new ArgumentyException("Za duzo argumentow");			


		String initFile = args[0];
		String outputTrajFile;
		String outputEnergyFile;

		if( args.length<2 ) {
			outputTrajFile = "./output/domyslnyPlik.xyz";
			outputEnergyFile = "./output/domyslnyPlikZEnergiami.txt";
		}
		else if( args.length<3 ) {
				outputTrajFile = args[1];
				outputEnergyFile = "./output/domyslnyPlikZEnergiami.txt";
		}
		else {
				outputTrajFile = args[1];
				outputEnergyFile = args[2];
		}
		

		try {
			Output.setUpOutputStreams(outputTrajFile,outputEnergyFile); // TODO: sprawdzenie czy plik istnieje; poinformuj, ze jest nadpisywany!

			Simulation sim = Input.uploadSimulation(null,initFile);


// sim WCZYTANE, STRUMIENIE POOTWIERANE, CZAS NA SYMULACJE:
//////////////////////////////////////////////////////////////
//															//
//															//
			sim.carryOutTheSimulation();					//
//															//
//															//
//////////////////////////////////////////////////////////////
			
			Output.closeStreams();

		} catch(FileNotFoundException e) {
				System.out.printf("Nie ma pliku ---> %s\n",e.getMessage());
		} catch(IOException e) {
				System.out.printf("<<Output>> nie udalo sie otworzyc strumienia wyjsciowego!(to raczej powazne)\n");
		}	finally {

				try{
					Output.closeStreams();
				}	catch(IOException e) { 
					System.out.printf("<<Output>> nie udalo sie zamknac strumieni wyjsciowy!(to raczej powazne)\n");
				}
		}



	}
}

class ArgumentyException extends RuntimeException {


	public ArgumentyException() {
		super();
	}	

	public ArgumentyException(String msg) {
		super(msg);
	}
}
