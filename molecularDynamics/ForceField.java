public abstract class ForceField
{
	abstract protected void single(Atom a);
	abstract protected void pair(Atom a, Atom b);
//	abstract protected void triple(Atom a, Atom b, Atom c);
//	abstract protected void quadruple(Atom a, Atom b, Atom c, Atom d);


	abstract protected void singleEnergy(Atom a);
	abstract protected void pairEnergy(Atom a, Atom b);
//	abstract protected void tripleEnergy(Atom a, Atom b);
//	abstract protected void quadrupleEnergy(Atom a, Atom b);


	public void forcesAndEnergies(TheSystem system) {
			system.zeroOutPotEnergies();
			oneBodyContributions(system);
			twoBodyContributions(system);
	}

	private void oneBodyContributions(TheSystem system) {
		Atom[] atoms = system.getAtoms();
		for( Atom a : atoms ) {
			single(a);
			singleEnergy(a);
		}
	}

	private void twoBodyContributions(TheSystem system) {
		Atom[] atoms = system.getAtoms();
		int nOfAtoms = atoms.length;

		for(int i=0;i<nOfAtoms-1;i++) {
			for(int j=i+1;j<nOfAtoms;j++) {
				pair(atoms[i],atoms[j]);
				pairEnergy(atoms[i],atoms[j]);
			}
		}
	}


}

