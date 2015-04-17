public class TrajectoryAndStats {

	private VecNd[][] traj;


	private double[] ePot;
	private double[] eKin;
	private double[] eTot;



	private static int trajFrameNumber;
	private static int energyFrameNumber;

	private final int BUFFER_SIZE = 10000; 

	public TrajectoryAndStats(int nOfAtoms, int nOfSteps) { 
		traj = new VecNd[nOfAtoms][BUFFER_SIZE]; // wprowadzilem to ograniczenie, zeby nie zezrec przypadkiem calej pamieci w przypadku bardzo dlugiej symulacji

		ePot = new double[nOfSteps];
		eKin = new double[nOfSteps];
		eTot = new double[nOfSteps];
	}


	

	public void updateTraj(TheSystem system) throws FrameIndexExceeded {

		if( trajFrameNumber>=BUFFER_SIZE )
			throw new FrameIndexExceeded();

		Atom[] atoms = system.getAtoms();
		for( Atom a : atoms ) 
			traj[a.getNumber()][trajFrameNumber] = a.getX();
	
	
		trajFrameNumber++; // istotne

		
		if( trajFrameNumber==BUFFER_SIZE )
			emptyTrajBuffer();
	}

	public void updateEnergy(TheSystem system) {

		ePot[energyFrameNumber] = system.getPotEnergy();
		eKin[energyFrameNumber] = system.getKinEnergySameMasses();
		eTot[energyFrameNumber] = ePot[energyFrameNumber] + eKin[energyFrameNumber];	

//System.out.printf("%f		%f		%f\n",ePot[energyFrameNumber],eKin[energyFrameNumber],eTot[energyFrameNumber]);

		energyFrameNumber++; // istotne

	}

	private int getNOfAtoms() { 
		return traj.length; 
	}

	public void finalize() {
		System.out.printf("Oprozniam bufor z klatkami trajektorii\n");
		emptyTrajBuffer();

		System.out.printf("Sporzadzam statystyki energetyczne\n");
/*
		double[] ePotMean = getMeans(ePot);
		double[] eKinMean = getMeans(eKin);
		double[] eTotMean = getMeans(eTot);

		System.out.printf("Wypisuje srednie energie do pliku\n");
		for(int i=0;i<energyFrameNumber;i++) 
//			Output.writeEnergy( String.format("%f	%f	%f",ePotMean[i],eKinMean[i],eTotMean[i]) );
*/
		for(int i=0;i<energyFrameNumber;i++) {
//			Output.writeEnergy( String.format("%f	%f	%f",ePotMean[i],eKinMean[i],eTotMean[i]) );
			Output.writeEnergy( String.format("%f	%f	%f",ePot[i],eKin[i],eTot[i]) );
		}
	}

	private double[] getMeans(double[] energies) { // ta wersja moze miec "numeryczne problemy" (aczkolwiek przy bardzo dlugich symulacjach i/lub bardzo wysokich energiach)
		int nOfSteps = energies.length;

		double[] means = new double[nOfSteps];
		means[0] = energies[0];		

		for(int i=1;i<nOfSteps;i++) 
			means[i] = means[i-1] + energies[i];

		for(int i=1;i<nOfSteps;i++) 
			means[i] /= (i+1);

		return means;
	}

	private double[] getMeans2(double[] energies) { // ta wersja nie ma numerycznych problemow, ale jest mniej wydajna
		int nOfSteps = energies.length;

		double[] means = new double[nOfSteps];
		means[0] = energies[0];		

		for(int i=1;i<nOfSteps;i++) {
			double frac = ((double)i)/(i+1);
			means[i] = means[i-1]*frac + energies[i]/(i+1);
		}

		return means;
	}

	private void emptyTrajBuffer() { // wywolywana zarowno gdy przepelni sie bufor, jak i gdy zamykany jest strumien
		int nOfAtoms = getNOfAtoms();

		for(int i=0;i<trajFrameNumber;i++) {
			Output.writeTraj(""+nOfAtoms);
			Output.writeTraj("komentarz");	
			for(int j=0;j<nOfAtoms;j++) {
				Output.writeTraj(""+j+" "+traj[j][i]);
			}
		}

		trajFrameNumber = 0;
	}

}

class FrameIndexExceeded extends RuntimeException {

	public FrameIndexExceeded() {
		super();
	}
}

