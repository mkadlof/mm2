// dwa mozliwe sposoby zapisu konfiguracji lancucha to poprzez:
// -> wektory wzgledne (int[] intConfig)
// -> punkty 2D (Vec[] vecConfig)
// pierwsza reprezentacja umozliwia szybsze obliczanie nowej konfiguracji po transformacji
// druga jest potrzebna do efektywnego algorytmu sprawdzajacego czy konfiguracja po transformacji jest legalna
class Configuration {

	class ConfigurationsNOfAANotSetException extends RuntimeException { }
	// RuntimeException to klasa wyjatkow, ktorych nie mamy obowiazku obslugiwac (jest to zarowno zaleta, jak i wada)

	private static int nOfAA; // to jest statyczna zmienna i mozna ja ustalic jedynie statycznym setterem

	private int[] intConfig; // sprawia, ze "transform" jest super-duper-szybkie
	private Vec[] vecConfig; // przydaje sie w "moveLegal"


	public Configuration(int nOfAA) { // do testow
		setStaticParams(nOfAA);	

		intConfig = new int[nOfAA-1]; // wektorow wzglednych jest nOfAA-1

		vecConfig = new Vec[nOfAA];
		vecConfig[0] = new Vec(); // pierwszy aa jest w (0,0)
		for(int i=1;i<nOfAA;i++) {
			Vec next = new Vec();
			next.add( vecConfig[i-1] );
			next.add( intConfig[i-1] );
			vecConfig[i] = next;
		}
	}

	public Configuration() throws ConfigurationsNOfAANotSetException { // konstruktor tworzacy pierwsza konfiguracje
		if( nOfAA==0 )
			throw new ConfigurationsNOfAANotSetException();

		intConfig = new int[nOfAA-1]; // wektorow wzglednych jest nOfAA-1

		vecConfig = new Vec[nOfAA];
		vecConfig[0] = new Vec(); // pierwszy aa jest w (0,0)
		for(int i=1;i<nOfAA;i++) {
			Vec next = new Vec();
			next.add( vecConfig[i-1] );
			next.add( intConfig[i-1] );
			vecConfig[i] = next;
		}
	}

	public Configuration(Configuration config) { // konstruktor kopiujacy
		intConfig = new int[nOfAA-1];
		for(int i=0;i<nOfAA-1;i++)
			intConfig[i] = config.intConfig[i];

		vecConfig = new Vec[nOfAA];
		for(int i=0;i<nOfAA;i++) 
			vecConfig[i] = new Vec( config.vecConfig[i]	);

	}

	public static void setStaticParams(int n) {
		nOfAA = n;
	}
	
// BEGIN( NAJWAZNIEJSZA METODA TEJ KLASY )
	public Configuration transform(int center, int rotNumber) {
		int[] newIntConfig = new int[nOfAA-1];

		for(int i=0;i<center;i++)
			newIntConfig[i] = intConfig[i];
		
		for(int i=center;i<nOfAA-1;i++) 
			newIntConfig[i] = Vec.rotate( intConfig[i], rotNumber );

		Vec[] newVecConfig = new Vec[nOfAA];



		for(int i=0;i<=center;i++)
			newVecConfig[i] = vecConfig[i]; // GROZNE, ale szybsze 
		// GROZNE, bo newVecConfig[i] oraz vecConfig[i] wskazuja na TEN SAM ADRES W PAMIECI
		// jezeli zmodyfikuje ten obiekt poprzez newVecConfig[i], to zmianie ulegnie takze vecConfig[i]
		// (tzn. formalnie zmianie ulegnie nie vecConfig[i], ale to na co wskazuje)
		// SZYBSZE, bo nie robie kopii (moznaby zrobic
		// 		newVecConfig[i] = new Vec( vecConfig[i] );
		// ale bede tworzyc mnostwo nowych obiektow, a ta metoda jest czesto tworzona)



		for(int i=center+1;i<nOfAA;i++) {
			Vec next = new Vec();
			next.add( newVecConfig[i-1] );
			next.add( newIntConfig[i-1] );
			newVecConfig[i] = next;
		}

		return new Configuration( newIntConfig, newVecConfig );
	}
// END( NAJWAZNIEJSZA METODA TEJ KLASY )

	private Configuration(int[] newIntConfig, Vec[] newVecConfig) {
		intConfig = newIntConfig;
		vecConfig = newVecConfig;
	}

	public Vec[] getVecConfig() {
		return vecConfig;
	}

	public double computeGyr() {
		double I = 0.0;
		VecDouble center = centerOfMass();		

		for(int i=0;i<nOfAA;i++) {
			VecDouble tmp = new VecDouble( vecConfig[i] );
			tmp.sub( center );

			I += tmp.squared();
		}

		return I;
	}

	public VecDouble centerOfMass() {
		VecDouble center = new VecDouble();

		for(int i=0;i<nOfAA;i++)
			center.add( vecConfig[i] );

		center.div(nOfAA);

		return center;
	}

	public String toString() {
		String out = "";

		for(int i=0;i<nOfAA;i++) 
			out += String.format("%d:\t\t%s\n",i,vecConfig[i]);

		return out;
	}



	public static void main(String[] args) {
		Configuration one = new Configuration(4);	

		System.out.printf("\ntaka konfiguracje stworzylem:\n%s",one);
		System.out.printf("jej srodek masy to: %s",one.centerOfMass());
		System.out.printf("a moment bezwladnosci to %f\n",one.computeGyr());

		int center = 2;
		int rotNumber = 3;
		Configuration two = one.transform(center,rotNumber);

		System.out.printf("\nPO OBROCIE:\n%s",two);
		System.out.printf("jej srodek masy to: %s",two.centerOfMass());
		System.out.printf("a moment bezwladnosci to %f\n",two.computeGyr());
	}

	public boolean isContinuous() {
		for(int i=1;i<nOfAA;i++)
			if( !vecConfig[i-1].neighbouring(vecConfig[i]) )
				return false;
	
		return true;
	}
}
