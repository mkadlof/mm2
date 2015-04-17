public class VecDouble {

	private double x;
	private double y;

	public VecDouble() {
		x = 0.0;
		y = 0.0;
	}

	public VecDouble(Vec v) {
		x = v.getX();
		y = v.getY();
	}

	public void add(Vec v) {
		x += v.getX();
		y += v.getY();
	}

	public void sub(VecDouble vDouble) {
		x -= vDouble.x;
		y -= vDouble.y;
	}

	public void div(int M) {
		x /= M;
		y /= M;
	}

	public double squared() {
		return x*x + y*y;
	}

	public String toString() {
		return String.format("%f	%f\n",x,y);
	}
}
