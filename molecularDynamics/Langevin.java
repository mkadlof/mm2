import java.util.Random;
// nie dziala!
public class Langevin extends Integrator {

	private double dt;
	private double gamma;
	private double T;
	private Random rGen;

	public Langevin(double dt, double gamma, double T) {
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
		double c0 = e_GammaDt;
		double c1 = (1-c0)/gamma/dt;
		double c2 = (1-c1)/gamma/dt;
		double sigmaR = dt*T/gamma*(2-(3-4*e_GammaDt+e_GammaDt*e_GammaDt)/gamma/dt); // masy sa te same, stala Boltzmanna jest 1


		for( Atom a : atoms ) {
			VecNd currX = a.getX();
			VecNd currV = a.getV();
			VecNd currA = a.getA();
			VecNd prevA = a.getAPrev();

			VecNd newV = new VecNd();
// newV := c0*currV + (c1-c2)*dt*prevA + c2*currA + rand

// newX := currX + c1*currV*dt + c2*currA*dt*dt/2 + rand
		}
	}
}
