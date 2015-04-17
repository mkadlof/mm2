public class ForceFieldDecorator extends ForceField {

	protected ForceField ff; // pole silowe, ktore bedziemy dekorowac

	public ForceFieldDecorator(ForceField ff) {
		this.ff = ff;
	}

	protected void single(Atom a) { ff.single(a); }
	protected void pair(Atom a, Atom b) { ff.pair(a,b); }

	protected void singleEnergy(Atom a) { ff.single(a); }
	protected void pairEnergy(Atom a, Atom b) { ff.pair(a,b); }

	public String toString() {
		return "";
	}

}
