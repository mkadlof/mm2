// ta wersja ma umozliwiac obliczanie sil, a POTEM wykonanie kroku
public class VelocityVerlet3 extends Integrator {

	private double dt;

	public VelocityVerlet3(double dt) {
		this.dt = dt;
	}

	public void step(TheSystem system, ForceField ff) {
// calkowanie
		Atom[] atoms = system.getAtoms();		
		VecNd tmp = new VecNd();

// aktualizacja:
		for( Atom a : atoms ) {
			VecNd currV = a.getV();
			VecNd currA = a.getA();

			VecNd prevA = a.getAPrev();

			VecNd newV = new VecNd();
// newV := currV + (prevA+currA)*dt/2
			tmp.add(prevA,currA); // przypisanie
			tmp.mult(dt/2); // modyfikacja
			newV.add(currV,tmp); // przypisanie

			a.updateV(newV);		
		}


// nowe polozenia
		for( Atom a : atoms ) {
			VecNd currX = a.getX();
			VecNd currV = a.getV();
			VecNd currA = a.getA();

			VecNd newX = new VecNd();
// newX := currX + currV*dt + currA*dt*dt/2
			tmp.mult(dt,currV); // przypisanie
			newX.add(currX,tmp); // przypisanie

			tmp.mult(dt*dt/2,currA); // przypisanie
			newX.add(tmp); // modyfikacja

			a.updateX(newX);
			a.updateA( new VecNd() );
		}

		
	}
}
