public class VelocityVerlet extends Integrator {

	private double dt;

	public VelocityVerlet(double dt) {
		this.dt = dt;
	}

	public void step(TheSystem system, ForceField ff) {
// calkowanie
		Atom[] atoms = system.getAtoms();		
		VecNd tmp = new VecNd();

		for( Atom a : atoms ) {	
			VecNd newX = new VecNd();
			VecNd newV = new VecNd();

			VecNd currX = a.getX();
			VecNd currV = a.getV();
			VecNd currA = a.getA();

			VecNd prevA = a.getAPrev();


// velocity step:
// newV := currV + (prevA+currA)*dt/2
			tmp.add(prevA,currA); // przypisanie
			tmp.mult(dt/2); // modyfikacja
			newV.add(currV,tmp); // przypisanie


// coord step:
// newX := currX + newV*dt + currA*dt*dt/2
			tmp.mult(dt,newV); // przypisanie
			newX.add(currX,tmp); // przypisanie

			tmp.mult(dt*dt/2,currA); // przypisanie
			newX.add(tmp); // modyfikacja


			a.updateAll( newX, newV, new VecNd() );	// istotne: nowa sila musi byc zerowa, bo w wyniku dzialania metod <<single>>, <<pair>> itd. w ForceField sila jest inkrementowana, a zerowanie nigdzie poza obecna metoda nie zachodzi	
		}
	}
}
