import java.util.*;

public class Chain {

	class TriedTooManyTimesToMakeALegalMoveException extends RuntimeException {}
	class SequenceNotSetException extends RuntimeException {}

	private static int nOfAA;
	private static char[] sequence; // to sa dwa statyczne atrybuty i ustawic je mozna jedynie przez statyczny setter o nazwie setStaticParams()

	private Configuration config;

	private int contacts;


	public static void setStaticParams(String seq) {
		sequence = seq.toCharArray();
		nOfAA = seq.length();
	}


	public Chain() throws SequenceNotSetException { 
		if( sequence==null )
			throw new SequenceNotSetException();

		config = new Configuration();
	
		contacts = 0;
	}

	public Chain(Chain c) {
	
		contacts = c.contacts;

		config = new Configuration( c.config );

	}


	private int globalContacts() {
		Vec[] vTab = config.getVecConfig();
		
		int cont = 0;

		for(int i=0;i<nOfAA-1;i++)
			for(int j=i+1;j<nOfAA;j++)
				if( j-i>2 && vTab[i].neighbouring(vTab[j]) )
					cont++;

		return cont;
	}





// BEGIN( NAJWAZNIEJSZA METODA TEJ KLASY )
	public Chain makeLegalMove(Random rGen) throws TriedTooManyTimesToMakeALegalMoveException {
		int rotNumber;
		int center;
		int nOfRots = Vec.getNOfRot(); // na wypadek gdyby naszla mnie kiedys chec napisania wersji ogolnej, z mozliwoscia wyboru wektorow 3D

	
		for(int i=0;true;i++) { // zakladam, ze JAKAS legalna transformacja istnieje

			if( i==1000 )
				throw new TriedTooManyTimesToMakeALegalMoveException();

			center = rGen.nextInt(nOfAA-1); // ma byc z zakresu [0,nOfAA-2]			
			rotNumber = rGen.nextInt(nOfRots);			

			Configuration newConfig = config.transform(center,rotNumber);

			if( moveLegal(newConfig,center) ) {
				int newContacts = this.contacts + deltaContacts(config,newConfig,center);
				
//System.out.printf("\n\nnewContacts\t=\t%d\n",newContacts);
//System.out.printf("center=%d\trotNumber=%d\n",center,rotNumber);
//if( !newConfig.isContinuous() )
//	System.out.printf("NOT CONTINUOUS\n");
			
				return new Chain(newConfig,newContacts);
			}
		}
	} 
// END( NAJWAZNIEJSZA METODA TEJ KLASY )





	private Chain(Configuration newConf, int newContacts) {
		config = newConf;
		contacts = newContacts;
	}

	private int deltaContacts(Configuration config1, Configuration config2, int center) { 
// "conf1" to konfiguracja PRZED, a "conf2" to konfiguracja PO

		int nOfContacts1 = contactsWithin(config1,center);
		int nOfContacts2 = contactsWithin(config2,center);

		return nOfContacts2 - nOfContacts1;
	}

	private int contactsWithin(Configuration query, int center) {
		int cont = 0;
		Vec[] vTab = query.getVecConfig();

		for(int i=center+1;i<nOfAA;i++)
			for(int j=0;j<=center;j++)
				if( i-j>2 && sequence[i]=='H' && sequence[j]=='H' && vTab[i].neighbouring(vTab[j]) )
					cont++;

		return cont;
	}

	private boolean moveLegal(Configuration newConfig, int center) {
		Vec[] newVecConfig = newConfig.getVecConfig();
		Set<Vec> set = new HashSet<Vec>();


		for(int i=0;i<=center;i++)
			set.add( newVecConfig[i] );

		for(int i=center+1;i<nOfAA;i++)
			if( set.contains(newVecConfig[i]) )
				return false;

		return true;
	}

	public Vec[] getVecConfig() {
		return config.getVecConfig();
	}

	public int getContacts() {
		return contacts;
	}

	public char[] getSequence() {
		return sequence;
	}

	public int getNOfAA() {
		return nOfAA;
	}

	public double computeGyr() {
		return config.computeGyr();
	}


	public static void main(String[] args) {
		Random rGen = new Random();
		
		Chain.setStaticParams("PHPPHPHHPHPPP");

		Chain newChain = new Chain();
		TrajectoryAndStats tmp = new TrajectoryAndStats();

		int i = 0;

		try {
			for(;i<10;i++) {
					newChain = newChain.makeLegalMove(rGen);
					System.out.printf( "%d\t----\t%d\n\n",i,newChain.getContacts() );

					tmp.outputOnePDB("./chainaTesty/plik"+i,newChain);

			}
		} catch( Exception e ) {
			System.out.printf("cos sie nie powiodlo przy wypisie do pliku o numerze %d\n",i);
		}
	}
}



