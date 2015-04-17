// do dokonczenia

public class VecNd {

	private static int dim; 

	private double coords[];



	public static void setDim(int n) {
		if( dim==0 ) 
			dim = n;
		else
			throw new DimensionalityException("Proba zmiany wymiarowosci!?\n");
	}


	public String toString() { 
		if( dim==1 )
			return String.format("%.3f %.3f %.3f",coords[0],0.0,0.0);
		else if( dim==2 )
			return String.format("%.3f %.3f %.3f",coords[0],coords[1],0.0);
		else if( dim==3 )
			return String.format("%.3f %.3f %.3f",coords[0],coords[1],coords[2]);

		return "Cos sie nie udalo w \"toString\" klasy <<VecNd>>\n";
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
