// do dokonczenia

public class Simulation
{
	private int nOfSteps;
	private TheSystem system;
	private ForceField ff;
	private Integrator intg;
	private TrajectoryAndStats trajAndStats;
	private int trajInterval;
	private int energyInterval;
	


	public void carryOutTheSimulation() {

		for(int i=0;i<nOfSteps;i++) {

			ff.forcesAndEnergies(system);

			intg.step(system); // tu update'owane sa polozenia, predkosci i sily, stawiany jest kolejny krok dynamiki	
	
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




	// main... 

}
