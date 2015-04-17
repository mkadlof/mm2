import java.util.*;
import java.io.*;

public class Constraints {

	private int nOfAA;
	private boolean[][] allowed;
	private final int nOfRot = 4; // przypadek 2D


	
	public Constraints(String file) throws FileNotFoundException {
		Scanner sc = new Scanner( new FileReader(file) );

		try {
			String line = sc.nextLine();
			nOfAA = Integer.parseInt(line);
			allowed = new boolean[nOfAA-1][nOfRot];

			String[] words;
			for(int i=0;i<nOfAA-1;i++) {

				line = sc.nextLine();
				//System.out.printf("wczytalem linie nr %d: %s\n",i,line);			
				words = line.split(" ");
				for(int j=0;j<words.length;j++)
					allowed[i][Integer.parseInt(words[j])] = true;
		
			}	
		
		} finally {
			sc.close();
		}		
	}

	public Constraints(int nOfAA) {
		this.nOfAA = nOfAA;
		allowed = new boolean[nOfAA-1][nOfRot];

		for(int i=0;i<nOfAA-1;i++)
			for(int j=0;j<nOfRot;j++)
				allowed[i][j]=true;
	}

	public int[][] allowedMicrostatesSeq() {
		int possibilities = (int)Math.pow((double)nOfRot,(double)nOfAA-1);
	
		int[][] out = new int[possibilities][nOfAA];

		for(int i=0;i<possibilities;i++) {
			int number = i;
			int index = 0;
			int reminder = 1;
			do {	
				reminder = number % 4;
				if( !allowed[index][reminder] ) {
					out[i]=null;
					break;
				} 
				out[i][index] = reminder;
				number /= 4;
				index++;
			} while( index<allowed.length );
		}	

		int nLinii = 0;
		for(int i=0;i<possibilities;i++) {
			if( out[i]!=null ) {
				System.out.printf("linia nr %d: ",nLinii);
				for(int j=nOfAA-2;j>=0;j--) System.out.printf("%d, ",out[i][j]);
				System.out.println();	
				nLinii++;
			}
		}

		return out;
	}
	

	public String toString() {
		String out = "";
		for(int i=0;i<nOfAA-1;i++) {
			for(int j=0;j<nOfRot;j++) 
				out += String.format("allowed[%d][%d]=%s\n",i,j,allowed[i][j]);	

			out += "\n";
		}
			
		return out;
	}



	public static void main(String[] argv) {

		Constraints c = new Constraints(8);

//		System.out.printf("wypisuje :\n"+c);		

		//List<List<Integer>> out = c.allowedMicrostatesRecur(0,tmp);
//		int[][] out = c.allowedMicrostatesSeq();

/*
		int aaNumber = 0;
		for( List<Integer> underlist : out ) {
			System.out.printf("aaNumber = %d: ",aaNumber);
			for( Integer i : underlist ) {
				System.out.printf("%d ",i);
			}

			aaNumber++;
		}
*/
		try{ 
			Constraints c2 = new Constraints("testConstr4.txt");
			//System.out.printf("wypisuje :\n"+c2);		

			int[][] out2 = c2.allowedMicrostatesSeq();
		} catch(FileNotFoundException e) {
			System.out.printf("Cos jest nie tak z plikiem\n"+e);	
		}
	}

}
