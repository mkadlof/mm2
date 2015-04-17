public abstract class Integrator {

	protected double dt;

	public abstract void step(TheSystem system, ForceField ff);

}
