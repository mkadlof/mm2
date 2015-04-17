import java.util.Random;

public class Andersen extends Integrator {

	private double nu;
	private double sigma2;
	private Random rGen;

	public Andersen(double dt, double nu, double T) {
		this.dt = dt;
		this.nu = nu;
		sigma2 = T;
		rGen = new Random();
	}

	public void step(TheSystem system, ForceField ff) {
// calkowanie
		Atom[] atoms = system.getAtoms();		
		VecNd tmp = new VecNd();
		
		double dt2 = dt*dt;
		

		for( Atom a : atoms ) {
			VecNd currX = a.getX();
			VecNd currV = a.getV();
			VecNd currA = a.getA();
			VecNd prevA = a.getAPrev();

			VecNd newV = new VecNd();
			
			if( rGen.nextDouble() < nu*dt ) 
				newV = VecNd.gaussian(rGen,0,sigma2);	
			else {
				newV.mult( 0.5*dt/a.getMass(), currA );
				newV.add( currV );
			}

			VecNd newX = new VecNd();
			
			
			newX.mult(dt,newV);
			tmp.mult( 0.5*dt2/a.getMass(), prevA );
			newX.add(tmp);
			newX.add(currX);
			
			tmp.mult( 0.5*dt/a.getMass(), prevA );
			newV.add(tmp);

/*	
		System.out.printf("newV=%f\n",newV.norm());
		System.out.printf("newX=%f\n",newX.norm());
		System.out.printf("currA=%f\n",currA.norm());
		System.out.printf("prevA=%f\n",prevA.norm());
*/

			a.updateAll( newX, newV, new VecNd() );
		}
	}
}
