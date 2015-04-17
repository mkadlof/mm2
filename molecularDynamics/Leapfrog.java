public class Leapfrog extends Integrator {

	private double dt;

	public Leapfrog(double dt) {
		this.dt = dt;
	}

	public void step(TheSystem system, ForceField ff) {
// calkowanie:
		Atom[] atoms = system.getAtoms();		
	
		VecNd tmp = new VecNd();

		for( Atom a : atoms ) {

			VecNd currX = a.getX();
			VecNd currV = a.getV();
			VecNd currA = a.getA();


			VecNd newV = new VecNd();
// velocity step:
// newV := currV + dt*currA
			tmp.mult(dt,currA);
			newV.add(currV,tmp);


			VecNd newX = new VecNd();
// coord step:
// newX := currX + dt*newV
			tmp.mult(dt,newV); // nie trzeba zerowac (nieistotne, co wczesniej mialo <<tmp>>)
			newX.add(currX,tmp); // tak samo 


// step finalization:
			a.updateAll( newX, newV, new VecNd() );
		}
	}

}
