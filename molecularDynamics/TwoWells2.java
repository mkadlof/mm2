public class TwoWells2 extends ForceFieldDecorator {
	private double a;
	private double b;
	private double c;
	private double d;

	public TwoWells2(ForceField ff, double a, double b, double c, double d) {
		super(ff);

		if( a<=0 || b<=0 || c<=0 || d<=0 )
			System.out.printf("ktorys ze wspolczynnikow jest <=0\npotencjal jest do niczego!\n");

		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}


	public void single(Atom at) {
		ff.single(at);
		
		double x = at.getX().getX();
		double out;
		
		double xMinus1 = x-1;
		double xPlus1 = x+1;

		out = -2*a*b*xMinus1*Math.exp(-b*xMinus1*xMinus1) -
				2*c*xPlus1*Math.exp(-xPlus1*xPlus1) -
				4*d*x*x*x;
	
		double coords[] = {out,0.0,0.0};
		at.incrF(new VecNd(coords) );	
	}


	public void singleEnergy(Atom at) {
		ff.singleEnergy(at);

		double x = at.getX().getX();
		double out;

		double xMinus1 = x-1;
		double xPlus1 = x+1;
		double x2 = x*x;
	
		out = -a*Math.exp(-b*xMinus1*xMinus1) -
				c*Math.exp(-xPlus1*xPlus1) +
				d*x2*x2;

		at.incrPotE(out);
	}


	protected void pair(Atom a, Atom b) {
		ff.pair(a,b);
	}

	protected void pairEnergy(Atom a, Atom b) {
		ff.pairEnergy(a,b);
	}

	public String toString() {
		return String.format("twoWells2 .... a=%.2f .... b=%.2f .... c=%.2f .... d=%.2f\n%s",a,b,c,d,ff);
	}
}
