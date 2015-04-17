public class Verlet extends Integrator {


	public Verlet(double dt) {
		this.dt = dt;
	}

	public void step(TheSystem system, ForceField ff) {
// calkowanie:
		Atom[] atoms = system.getAtoms();

		for( Atom a : atoms ) {	
			VecNd tmp = new VecNd();

			VecNd currX = a.getX();
			VecNd currA = a.getA();

			VecNd prevX = a.getXPrev();

			VecNd newX = new VecNd();
// newX := 2*currX - prevX + currA*dt*dt 
			newX.mult(2.0,currX);
			newX.sub(prevX);
			tmp.mult(dt*dt,currA);
			newX.add(tmp);


// predkosc w przypadku tej wersji algorytmu Verleta nie jest do niczego potrzebna -- sluzy (gdzie indziej) do wyznaczenia energii kinetycznej
			VecNd newV = new VecNd(); 
			newV.sub(currX,prevX);
			newV.div(dt);


			a.updateAll( newX, newV , new VecNd() ); // nowa sila jest zerowa - i to akurat jest istotne, bo nigdzie indziej nie bedzie okazji wyzerowac sily

		}
	}

}
