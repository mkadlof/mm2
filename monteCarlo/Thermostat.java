import java.util.Random;
import java.io.*;

public class Thermostat {
	private double T;
	private double epsilon;
	private Chain c;
	private Random rGen;
	private TrajectoryAndStats trajAndStats;



	public Thermostat(double T, double epsilon, Chain c) {
		this.T = T;
		this.epsilon = epsilon;
		this.c = c;
		rGen = new Random();
	}


	public static void setStaticParams(String seq) {
		Chain.setStaticParams(seq);
		Configuration.setStaticParams(seq.length());
		TrajectoryAndStats.setStaticParams(seq.toCharArray());
	}

// BEGIN( NAJWAZNIEJSZA METODA TEJ KLASY )
	public void transformKtimes(int k) {
		Chain tmp = null;

		try {
			while( k>0 ) {

 
				tmp = c.makeLegalMove( rGen );

				double newEnergy = -epsilon * tmp.getContacts();
				double oldEnergy = -epsilon * c.getContacts();
	
				if( newEnergy<=oldEnergy || rGen.nextDouble()<Math.exp(-(newEnergy-oldEnergy)/T) ) 
					c = tmp;


				trajAndStats.update( c );

				k--;
			}
		} catch(IOException e) {
			System.out.printf("wywolalem updateTraj z nastepujacymi parametrami\n");
			System.out.printf("chain poprzedni:\n%s\n",c);
			System.out.printf("chain po transformacji:\n%s\n",tmp);
		}		

	}
// END( NAJWAZNIEJSZA METODA TEJ KLASY )


	public void setUpOutput(String trajFileName) {
		try {
			if( trajAndStats!=null )
				trajAndStats.setUpOutput(trajFileName);
			else
				trajAndStats = new TrajectoryAndStats(trajFileName);

		} catch( IOException e ) {
			System.out.printf("niepowodzenie w setUpOutput() w Thermostat z plikiem o nazwie %s\n",trajFileName);
		}
	}

	public void finalize() {
		trajAndStats.finalize();
	}

	public double getT() {
		return T;
	}
	
	public void setT(double T) {
		this.T = T;
	}

	public void setChain(Chain c) {
		this.c = c;
	}

	public Chain getChain() {
		return c;
	}

	public void outputLowestEnergyConfiguration(String fileName) {
		try {
			trajAndStats.outputLowestEnergyConfiguration(fileName);
		} catch( IOException e ) {
			System.out.printf("niepowodzenie w outputLowestEnergyConfiguration!\n");
		}
	}	

	public void outputHist(String fileName) {
		try {
			trajAndStats.outputHist(fileName);
		} catch( IOException e ) {
			System.out.printf("niepowodzenie w outputHist!\n");
		}
	}

	public void outputMeanGyrs(String fileName, double Tinit, double dT) {
		try {
			trajAndStats.outputMeanGyrs(fileName,Tinit,dT);
		} catch( IOException e ) {
			System.out.printf("niepowodzenie w outputMeanGyrs!\n");
		}
	}

	public void outputCv(String fileName, double Tinit, double dT) {
		try {
			trajAndStats.outputCv(fileName,Tinit,dT);
		} catch( IOException e ) {
			System.out.printf("niepowodzenie w outputCv!\n");
		}
	}


	public static void main(String[] args) {
		String seq = "PHPPHPPHHPPHHPPHPPHP";
		Thermostat.setStaticParams(seq);
		Chain starter = new Chain();

		Thermostat test = new Thermostat(0.25,1.0,starter);
		test.setUpOutput("trajThermostatTest");

		test.transformKtimes(2000);
		test.outputLowestEnergyConfiguration("lowestThermostatTest");
		test.outputHist("histThermostatTest");

		test.finalize();	
	}
}
