//dziala, nic nie trzeba zmieniac
//omowienie na cwiczeniach

import java.io.*;

public class Output {

	private static BufferedWriter outputTrajStream;
	private static BufferedWriter outputEnergyStream;

	public static void setUpOutputStreams(
													String outputTrajFile,
													String outputEnergyFile
																			) throws FileNotFoundException, IOException {
		outputTrajStream = new BufferedWriter( new FileWriter( outputTrajFile ) );	
		outputEnergyStream = new BufferedWriter( new FileWriter( outputEnergyFile ) );	
	}

	public static void writeTraj(String s) {
		try {
			outputTrajStream.write(s);
			outputTrajStream.newLine();
		} catch(IOException e) {
			System.out.printf("Wyjatek przy wypisywaniu do pliku z trajektoria, nic na to nie poradze\n");
		} 
	}
	
	public static void writeEnergy(String s) {
		try {
			outputEnergyStream.write(s);
			outputEnergyStream.newLine();
		} catch(IOException e) {
			System.out.printf("Wyjatek przy wypisywaniu do pliku z energiami, nic na to nie poradze\n");
		} 
	}	

	public static void closeStreams() throws IOException {
		if( outputTrajStream!=null ) 
				outputTrajStream.close();
		if( outputEnergyStream!=null )
			outputEnergyStream.close();
	}
}
