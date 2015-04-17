import java.io.*;
import java.util.*;

public class TheSystem
{
	private Atom[] atoms;


	public TheSystem() {
// i tyle
	}

	public int getNOfAtoms() {
		return atoms.length;
	}

	public void setAtoms(Atom[] atoms) {
		this.atoms = atoms;
	}

	public Atom[] getAtoms() {
		return atoms;
	}
	
	public Atom getNthAtom(int N) {
		return atoms[N];
	}

	public void zeroOutPotEnergies() {
		for( Atom a : atoms )
			a.zeroOutPotE();
	}

	public double getPotEnergy() {
		double potE = 0.0;

		for( Atom a : atoms )
			potE += a.getPotE();

		return potE;
	}

	public double getKinEnergySameMasses() {
		double sum = 0.0;

		for( Atom a : atoms ) {
			VecNd tmpV = a.getV();
			sum += Math.pow( tmpV.norm(), 2.0 );
		}

		// UZYWAC TYLKO, GDY MASY WSZYSTKICH ATOMOW SA TAKIE SAME:
		return sum*0.5*atoms[0].getMass(); 	// wez mase pierwszego z brzegu atomu;
	}

	public double getKinEnergyDiffMasses() {
		double sum = 0.0;

		for( Atom a : atoms )
			sum += a.getKinE();

		return sum;
	}

/* zbedne?
	public double getTotEnergy() {
System.out.printf("eKin: %f\n",getKinEnergy());
		return getKinEnergy()+getPotEnergy();
	}
*/

	public static void main(String args[]) {
// do testowania
	}
}
