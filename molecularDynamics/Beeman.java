public class Beeman extends Integrator {

	private double dt;

	public Beeman(double dt) {
		this.dt = dt;
	}

	public void step(TheSystem system, ForceField ff) {
// calkowanie
		Atom[] atoms = system.getAtoms();		
		VecNd tmp = new VecNd();
		VecNd tmp2 = new VecNd();

		for( Atom a : atoms ) {
			VecNd currV = a.getV();
			VecNd currA = a.getA();
			VecNd prevA = a.getAPrev();
			VecNd prevPrevA = a.getAPrevPrev();

			VecNd newV = new VecNd();
			
// newV := currV + (2*currA + 5*prevA - prevPrevA)*dt/6
			newV.mult(2,currA); // przypisanie
			tmp.mult(5,prevA); // przypisanie
			newV.add(tmp); // modyfikacja
			newV.sub(prevPrevA); // modyfikacja
			newV.mult(dt/6); // modyfikacja
			newV.add(currV); // modyfikacja

			a.updateV(newV);		
		}


		for( Atom a : atoms ) {
			VecNd currX = a.getX();
			VecNd currV = a.getV();
			VecNd currA = a.getA();
			VecNd prevA = a.getAPrev();

			VecNd newX = new VecNd();
// newX := currX + currV*dt + (4*currA - prevA)*dt*dt/6
			tmp.mult(dt,currV); // przypisanie
			newX.add(currX,tmp); // przypisanie

			tmp.mult(4,currA); // przypisanie
			tmp.sub(tmp,prevA); // przypisanie
			tmp.mult(dt*dt/6); // modyfikacja
			newX.add(tmp); // modyfikacja

			a.updateX(newX);
			a.updateA( new VecNd() );
		}

		
	}
}
