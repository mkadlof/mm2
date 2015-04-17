// niech "x_vec" oznacza polozenie (jest to wektor)
// i niech x := norm(x_vec)
// wowczas:
//
// V(x_vec) = 0.5*f*(L-x)^2
//
// F(x_vec) = 0, dla x<L 
// F(x_vec) = -(dV/dx_vec) = f*(L-x)*(x_vec/x), dla x>=L
//
public class SoftWalls extends ForceFieldDecorator {

	private double f;
	private double l;

	public SoftWalls(ForceField ff, double f, double l) {
		super(ff);
		this.f = f;
		this.l = l;
	}


	protected void single(Atom a) {
		ff.single(a);
//System.out.printf("%s\n",this);

		VecNd currX = a.getX();
		double norm = currX.norm();

		if( norm>=l ) {
			VecNd fContrib = new VecNd();

			fContrib.mult( 
							f*(l-norm)/norm, currX 
						);

			a.incrF(fContrib);
		}
	}

	protected void singleEnergy(Atom a) {
		ff.singleEnergy(a);

		double norm = a.getX().norm();

		if( norm>=l ) {
			a.incrPotE(
						0.5*f*Math.pow(l-norm,2.0)
					);
		}	
	}

	protected void pair(Atom a, Atom b) {
		ff.pair(a,b);
	}

	protected void pairEnergy(Atom a, Atom b) {
		ff.pairEnergy(a,b);
	}
	public String toString() {
		return String.format("soft .... f=%.2f ..... l=%.2f",f,l,ff);
	}	
}
