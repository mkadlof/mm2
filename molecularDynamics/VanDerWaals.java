public class VanDerWaals extends ForceFieldDecorator {
	private double r;
	private double epsilon;

	public VanDerWaals(ForceField ff, double r, double epsilon) {
		super(ff);
		this.r = r;
		this.epsilon = epsilon;
	}

	protected void pair(Atom a, Atom b) {
		ff.pair(a,b);
//System.out.printf("%s\n",this);

		VecNd forceContrib = new VecNd(); // wklad do sily, dzialajacej na atom "a" z oddzialywania "b->a"

		VecNd currXa = a.getX();
		VecNd currXb = b.getX();

		VecNd tmp = new VecNd(); // bedzie pelnic role wektora kierunkowego


		tmp.sub(currXb,currXa);
		double norm = tmp.norm();
		tmp.div(norm);

		double r_div_norm_6 = Math.pow(r/norm,6.0);

// F_ij := -(dV/dxij_vec) = -( 12*epsilon*r_div_norm_6*(r_div_norm_6-1)*(xi_vec-xj_vec)/norm(xi_vec-xj_vec) )
		forceContrib.mult(
				-12.0*epsilon*r_div_norm_6*(r_div_norm_6 - 1.0)/norm, 
				tmp
				);


		a.incrF( forceContrib ); // kluczowe

		forceContrib.mult( -1.0 );	
		b.incrF( forceContrib );
}
	
	protected void pairEnergy(Atom a, Atom b) {
		ff.pairEnergy(a,b);

		VecNd currXa = a.getX();
		VecNd currXb = b.getX();

		VecNd tmp = new VecNd();

		tmp.sub(currXa,currXb); // przypisanie
		double norm = tmp.norm();
		double r_div_norm_6 = Math.pow(r/norm,6.0);
		
// V_ij := epsilon*[(r_div_norm_6)^2 - 2*(r_div_norm_6)]	
		double dE = epsilon*r_div_norm_6*(r_div_norm_6 - 2.0);

		a.incrPotE(dE/2);
		b.incrPotE(dE/2);	
	}

	protected void single(Atom a) {
		ff.single(a);
	}

	protected void singleEnergy(Atom a) {
		ff.singleEnergy(a);
	}

	public String toString() {
		return String.format("vdW ..... r=%.2f ..... epsilon=%.2f\n%s", r,epsilon,ff);
	}
}

