public class NoField extends ForceField {

	protected void single(Atom a) { }
	protected void pair(Atom a, Atom b) { }

	protected void singleEnergy(Atom a) { }
	protected void pairEnergy(Atom a, Atom b) { }

	public String toString() {
		return "NoField";
	}
}
