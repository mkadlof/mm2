public class Vec {
	private static final Vec[] e = { new Vec(1,0), new Vec(0,1), new Vec(-1,0), new Vec(0,-1) };

	private static final int[][][] rot = {
		{ {1,0} , {0,1} }, // identycznosc
		{ {0,-1} , {1,0} }, // 90 st. w lewo
		{ {-1,0} , {0,-1} }, // 180 st. w lewo
		{ {0,1} , {-1,0} }, // 270 st. w lewo
	};

	

	private int x;
	private int y;

	public Vec() {
		x = 0;
		y = 0;
	}

	public Vec(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vec(Vec patre) { // construktor kopiujacy
		x = patre.x;
		y = patre.y;
	}

	public static int rotate(int baseVecNumber, int rotNumber) {
		return ( baseVecNumber + rotNumber ) % 4;
	}

	public static Vec e(int baseVecNumber) {
		return new Vec(e[baseVecNumber].x,e[baseVecNumber].y);
	}

	public void add(Vec v) {
		x += v.x;
		y += v.y;
	}

	public void add(int baseVecNumber) {
		x += e[baseVecNumber].x;
		y += e[baseVecNumber].y;
	}

	public static int getNOfRot() {
		return rot.length;
	}

	public boolean neighbouring(Vec other) {
		if( Math.abs(this.x-other.x)+Math.abs(this.y-other.y) == 1 )
			return true;
		else
			return false;
	}
	
	public boolean equals(Object o) { // potrzebne do Set
		return (o instanceof Vec) && ( x==((Vec)o).x && y==((Vec)o).y );
	}

	public int hashCode() { // potrzebne do HashSet
		return 1000*x+y; 
	}

	public int getX() {
		return x;
	}	

	public int getY() {
		return y;
	}

	public String toString() {
		return String.format("%8.3f%8.3f%8.3f\n",(float)x,(float)y,0.0);
	}

	



	public static void main(String[] argv) {
		System.out.printf("mozliwych obrotow: %d\n",getNOfRot());

		Vec right = new Vec(1,0);
		Vec up = new Vec(0,1);
		Vec left = new Vec(-1,0);
		Vec down = new Vec(0,-1);
		Vec diagonal = new Vec(1,1);
		Vec center = new Vec(0,0);
		
		System.out.printf("wektory "+right+" oraz "+diagonal+" sa w sasiedztwie: "+right.neighbouring(diagonal)+"\n");
		System.out.printf("wektory "+center+" oraz "+diagonal+" sa w sasiedztwie: "+center.neighbouring(diagonal)+"\n");
		System.out.printf("wektory "+left+" oraz "+diagonal+" sa w sasiedztwie: "+left.neighbouring(diagonal)+"\n");
		System.out.printf("wektory "+up+" oraz "+diagonal+" sa w sasiedztwie: "+up.neighbouring(diagonal)+"\n");
	
		System.out.printf("right = "+right);
		System.out.printf("up = "+up);
		System.out.printf("left = "+left);
		System.out.printf("down = "+down);

		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++)
				System.out.printf(""+i+" X rot[%d]="+Vec.rotate(i,j)+"\n",j);

		Vec down2 = new Vec(0,-1);
		Vec left2 = new Vec(-1,0);
		System.out.printf("wektory %s oraz %s sa rowne: "+down.equals(down2),down,down2);
		System.out.println();
		System.out.printf("wektory %s oraz %s sa rowne: "+down.equals(left2),down,left2);
		System.out.println();

		
	}
}
