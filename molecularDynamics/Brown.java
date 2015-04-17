import java.util.Random;


public class Brown extends Integrator {

	private double dt;
	private double gamma;
	private double T;
	private Random rGen;

	public Brown(double dt, double gamma, double T) {
		this.dt = dt;
		this.gamma = gamma;
		this.T = T;
		rGen = new Random();
	}


	public void step(TheSystem system, ForceField ff) {
// calkowanie
		Atom[] atoms = system.getAtoms();		
		VecNd tmp = new VecNd();

		double e_GammaDt = Math.exp(-gamma*dt);
		double sigmaR2 = dt*T/gamma * (2-(3-4*e_GammaDt+e_GammaDt*e_GammaDt)/gamma/dt);

		for( Atom a : atoms ) {
			VecNd currX = a.getX();
			VecNd currV = a.getV();
			VecNd currA = a.getA();

			VecNd newX = new VecNd();

// newX := currX + currA*dt/gamma + rand
			tmp.mult(dt/gamma,currA);
			tmp.add( VecNd.gaussian(rGen,0,sigmaR2) );

			newX.add(currX,tmp);

			a.updateX(newX);
		}
	}
}
