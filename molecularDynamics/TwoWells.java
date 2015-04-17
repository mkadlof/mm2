public class TwoWells extends ForceFieldDecorator {
	private double a;
	private double b;
	private double c;
	private double d;

	private double x1;
	private double x2;
	private double shift;

	private final double pi = Math.PI;

	public TwoWells(ForceField ff, double a, double b, double c, double d) {
		super(ff);

		if( a<=0 || b<=0 || c<=0 || d<=0 )
			System.out.printf("ktorys ze wspolczynnikow jest <=0\npotencjal jest do niczego!\n");

		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;

		x1 = 3*pi/(2*b);
		x2 = x1 + 3*pi/(2*d);
		shift = a - c - c*d*x2;
	}


	public void single(Atom at) {
		super.single(at);

		double x = at.getX().getX();
		double out;

		if( x<=0 ) {
			out = a*b;
		}
		else if( x<x1 ) {
			out = a*b*Math.cos(b*x);
		} 
		else if( x<x2 ) {
			double tmp = d*(x-x1);
			out = c*d*Math.sin(tmp);
		}
		else {	
		 out = -c*d;
		}

	
		double coords[] = {out,0.0,0.0};
		at.incrF(new VecNd(coords) );	
	}


	public void singleEnergy(Atom at) {
		super.singleEnergy(at);

		double x = at.getX().getX();
		double out;

		if( x<=0 ) {
			out = -a*b*x;
		}
		else if( x<x1 ) {
			out = -a*Math.sin(b*x);
		} 
		else if( x<x2 ) {
			double tmp = d*(x-x1);
			out = c*(Math.cos(tmp) - 1) + a;
		}
		else {		
			out = c*d*x+shift;	
		}

		at.incrPotE(out);
	}

	protected void pair(Atom a, Atom b) {
		super.pair(a,b);
	}

	protected void pairEnergy(Atom a, Atom b) {
		super.pairEnergy(a,b);
	}

	public String toString() {
		return String.format("two_wells a=%.2f ..... b=%.2f ..... c=%.2f ..... d=%.2f\n%s",a,b,c,d,super.ff);
	}
}
