import java.util.Random;

public class VecNd {

	private static int dim; 

	private double coords[];

	public static void setDim(int n) {
		if( dim==0 ) 
			dim = n;
		else
			throw new DimensionalityException("Proba zmiany wymiarowosci!?\n");
	}

	public VecNd() {
		coords = new double[dim]; // zakladam, ze wartosci beda zerowe, nie inicjalizuje ich
	}

	public VecNd(double[] coords) {
		this.coords = new double[dim];

		for(int i=0;i<dim;i++)
			this.coords[i] = coords[i];
	}

	public static int getDim() {
		return dim;
	}

	public static VecNd gaussian(Random rGen, double mean, double sigma2) {
		VecNd out = new VecNd();

		for(int i=0;i<dim;i++)
			out.coords[i] = mean + sigma2*rGen.nextGaussian();

		return out;
	}

	public double getX() {
		return coords[0];
	}

	public void zeroOut() {
		for(int i=0;i<dim;i++)
			coords[i] = 0.0;
	}

	public double norm() {
		double sum = 0.0;
		for(int i=0;i<dim;i++)
			sum += coords[i]*coords[i];

		return( Math.sqrt(sum) );
	}

	public static double dot(VecNd a, VecNd b) {
		double sum = 0.0;	

		for(int i=0;i<dim;i++)
			sum += a.coords[i]*b.coords[i];

		return sum;
	}

	public void add(VecNd a, VecNd b) { // ustawia wartosc na a+b
		for(int i=0;i<dim;i++)
			coords[i] = a.coords[i] + b.coords[i];
	}

	public void sub(VecNd a, VecNd b) { // "subtract"; jak wyzej, tylko a-b
		for(int i=0;i<dim;i++)
			coords[i] = a.coords[i] - b.coords[i];
	}

	public void add(VecNd b) { // ustawia wartosc na this+b
		for(int i=0;i<dim;i++)
			coords[i] += b.coords[i];
	}

	public void sub(VecNd b) { // "subtract"; ustawia wartosc na this-b
		for(int i=0;i<dim;i++)
			coords[i] -= b.coords[i];
	}

	public void mult(double scalar) {
		for(int i=0;i<dim;i++)
			coords[i] *= scalar;
	}
	
	public void div(double scalar) {
		for(int i=0;i<dim;i++)
			coords[i] /= scalar;
	}

	public void mult(double scalar, VecNd a) {
		for(int i=0;i<dim;i++)
			coords[i] = scalar * a.coords[i];
	}

	public void div(double scalar, VecNd a) {
		for(int i=0;i<dim;i++)
			coords[i] = a.coords[i] / scalar;
	}

	public String toString() { // to jest kiepsko napisane, pewnie da sie poprawic
// problem polega na tym, ze format "xyz" wymaga wypisania dokladnie trzech liczb
// opisujacych polozenie - wiec co jak dim==1, a powinno zostac wypisane 0.0 w kolumnach II i III?
		if( dim==1 )
			return String.format("%.3f %.3f %.3f",coords[0],0.0,0.0);
		else if( dim==2 )
			return String.format("%.3f %.3f %.3f",coords[0],coords[1],0.0);
		else if( dim==3 )
			return String.format("%.3f %.3f %.3f",coords[0],coords[1],coords[2]);

		return "cos sie nie powiodlo w \"toString\" klasy <<VecNd>>\n";
	}
	
	public String myToString() {
		String out = "[ ";
		for(int i=0;i<dim-1;i++)
			out += String.format("%.3f , ",coords[i]);

		out += String.format("%.3f ]",coords[dim-1]);

		return out;
	}
}

class DimensionalityException extends RuntimeException {

	public DimensionalityException() {
		super();
	}

	public DimensionalityException(String msg) {
		super(msg);
	}
}
