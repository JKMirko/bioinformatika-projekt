import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



public class Starter {

	/**
	 * @param args - argument
	 *  first argument - required - path to file containing overlap information
	 *  second argument - optional - path to file in witch the unitig layout will be written. 
	 *  If the file exists, it will be overwritten, if not, it will be created.
	 *  If this argument is not provided, the unitig layout will be written to the standard output
	 */
	public static void main(String[] args) {
		if(args.length == 0){
			//invalid parameters, write appropriate message
			System.out.println("Invalid arguments!\nAvailable arguments:\n[REQUIRED] 1st argument - path to file containing overlap information\n[OPTIONAL] 2nd argument - path to file in witch the unitig layout will be written");
		}else{
			//has parameters, check validity
			String inputFilePath = args[0];
			File inputFile = new File(inputFilePath);
			if(!inputFile.exists() || inputFile.isDirectory()){
				//invalid file path
				System.out.println("Invalid input file path \""+inputFilePath+"\"!");
			}else{
				//ok, get the output stream to file if the output file path is provided, use standard output otherwise
				BufferedWriter outputWriter = null;
				if(args.length >= 2){
					String outputFileName = args[1];
					File outputFile = new File(outputFileName);
					try {
						outputWriter = new BufferedWriter(new FileWriter(outputFile, false));
					} catch (IOException e) {
						System.out.println("Error while getting a connection to output file!");
						return;
					}
				}else{
					outputWriter = new BufferedWriter(new PrintWriter(System.out));
				}
				
				//test, change this with some real code after
				try {
					outputWriter.write("Test test");
					outputWriter.flush();
				} catch (IOException e) {
					System.err.println("Error while writing to output!");
				}
			}
		}
	}

}
