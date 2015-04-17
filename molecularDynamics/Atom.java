import java.util.*;

public class Atom
{
	private int number; // kazdy atom bedzie mial unikatowy numer, poczynajac od zera, co ulatwi wpisywanie polozen poszczegolnych atomow do trajektorii
	private static int staticIterator;

//	private char type; // na razie zbedne, a chodzi tu o C, O, N, H, czy inny atom
	private double mass;


	private VecNd x; // obecne polozenie
	private VecNd v; // obecna predkosc 
	private VecNd a; // obecne przyspieszenie

	private VecNd xPrev; // potrzebne
	private VecNd vPrev; // potrzebne
	private VecNd aPrev; // potrzebne

	private VecNd aPrevPrev; // przydaje sie w algorytmi Beemana

	private double potE; // energia potencjalna
	private double kinE;

	public Atom(double mass, VecNd initX, VecNd initV) {
		number = staticIterator;
		staticIterator++;

		this.mass = mass;

		x = initX;
		xPrev = initX; // domyslnie 

		v = initV;
		vPrev = initV; // domyslnie

		a = new VecNd(); // przyspieszenia inicjalizuje zerowymi wartosciami
		aPrev = new VecNd(); // domyslnie
		aPrevPrev = new VecNd(); // do Beemana
	}

	public int getNumber() {
		return number;
	}

	public VecNd getX() {
		return x;
	}

	public VecNd getXPrev() {
		return xPrev;
	}

	public VecNd getV() {
		return v;
	}

	public VecNd getVPrev() {
		return vPrev;
	}

	public VecNd getA() {
		return a;
	}

	public VecNd getAPrev() {
		return aPrev;
	}

	public VecNd getAPrevPrev() {
		return aPrevPrev;
	}

	public void updateAll(VecNd newX, VecNd newV, VecNd newA) {
		updateX( newX );
		updateV( newV );
		updateA( newA );
	}

	public void updateX(VecNd newX) {
		xPrev = x;
		x = newX;
	}

	public void updateV(VecNd newV) {
		vPrev = v;
		v = newV;
	}

	public void updateA(VecNd newA) {
		aPrevPrev = aPrev;
		aPrev = a;
		a = newA;
	}
	

	public double getMass() {
		return mass;
	}

	public void incrF(VecNd forceContrib) {
		VecNd tmp = new VecNd();
		tmp.div(mass,forceContrib);
		a.add( tmp );
	}

	public void zeroOutPotE() {
		potE = 0.0;
	}

	public double getPotE() {
		return potE;
	}

	public double getKinE() {
		return 0.5*mass*Math.pow(vPrev.norm(),2.0);
	}

	public void incrPotE(double dE) {
		potE += dE;
	}
}
