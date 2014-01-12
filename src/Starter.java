import hr.fer.zesoi.bioinfo.Unitigging;
import hr.fer.zesoi.bioinfo.formaters.FormatterException;
import hr.fer.zesoi.bioinfo.formaters.MinimuslikeOverlapGraphFormatter;
import hr.fer.zesoi.bioinfo.formaters.IOverlapGraphFormatter;
import hr.fer.zesoi.bioinfo.models.OverlapGraph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



public class Starter {

	/**
	 * @param args - argument
	 *  first argument - required - path to file containing overlap information
	 *  second argument - required - path to file containing reads infomration
	 *  Optional arguments:
	 *  Unitig layout file path - you can specify a path to a file in witch the unitig layout will be written.
	 *  If the file exists, it will be overwritten, if not, it will be created.
	 *  If this argument is not provided, the unitig layout will be written to the standard output
	 *  Use -oLayout=filepath to specify this argument
	 *  Unitig layout overlaps file path - you can specify a path to a file in witch the ovelap information from the unitig layout will be written.
	 *  The same writing rules as in the unitig layout file path argument apply;
	 *  Use -oOverlaps=filepath to specify this argument
	 *  Epsilon - real number value in [0,1] interval used in the transitive edge removal. defaults to 0.1
	 *  Use -epsilon=value to specify this argument
	 *  Alpha - positive integer value constant used in the transitive edge removal, defaults to 3
	 *  user -alpha=value to specify this argument
	 */
	public static void main(String[] args) {
		//get the needed parameters
		String overlapInformationFilePath = null;
		String readsInformationFilePath = null;
		String outputLayoutFilePath = null;
		String outputOverlapsFilePath = null;
		//transitive edges removal parameter
		double epsilon = 0.1;
		int alpha = 3;
		
		
		for(int argumentIterator = 0; argumentIterator < args.length; argumentIterator++){
			if(argumentIterator == 0){
				//first argument - overlap information file path
				overlapInformationFilePath = args[argumentIterator];
			}else if(argumentIterator == 1){
				//second argument - reads infomration
				readsInformationFilePath = args[argumentIterator];
			}else{
				//optional arguments
				String argumentString = args[argumentIterator];
				int indexOfEquals = argumentString.indexOf("=");
				if(argumentString.startsWith("-") && indexOfEquals != -1){
					//valid optional parameter
					String argumentName = argumentString.substring(1, indexOfEquals).toLowerCase();
					if(argumentName.equals("olayout")){
						outputLayoutFilePath = argumentString.substring(indexOfEquals + 1);
					}else if(argumentName.equals("ooverlaps")){
						outputOverlapsFilePath = argumentString.substring(indexOfEquals + 1);
					}else if(argumentName.equals("epsilon")){
						try {
							epsilon = Double.parseDouble(argumentString.substring(indexOfEquals + 1));
							if(epsilon < 0 || epsilon > 1){
								System.out.println("Epsilon must be in the [0,1] interval!");
								return;
							}
						} catch (NumberFormatException e) {
							System.out.println("Epsilon must be a real number value!");
							return;
						}
					}else if(argumentName.equals("alpha")){
						try {
							alpha = Integer.parseInt(argumentString.substring(indexOfEquals + 1));
							if(alpha < 0){
								System.out.println("Alpha must be a positive value!");
								return;
							}
						} catch (NumberFormatException e) {
							System.out.println("Alpha must be an integer!");
							return;
						}
					}
				}
			}
		}
		if(overlapInformationFilePath == null){
			System.out.println("Missing overlap information file path!");
		}else if(readsInformationFilePath == null){
			System.out.println("Missing reads information file path");
		}else{
			//got required parameters
			File overlapsInputFile = new File(overlapInformationFilePath);
			if(!overlapsInputFile.exists() || overlapsInputFile.isDirectory()){
				System.out.println("Invalid overlaps information file!");
				return;
			}
			File readsInputFile = new File(readsInformationFilePath);
			if(!readsInputFile.exists() || readsInputFile.isDirectory()){
				System.out.println("Invalid reads information file!");
			}
			
			//get output streams
			BufferedWriter layoutWriter = null;
			if(outputLayoutFilePath != null){
				//the user specified the layout file
				try {
					layoutWriter = new BufferedWriter(new FileWriter(new File(outputLayoutFilePath), false));
				} catch (IOException e) {
					System.err.println("Error while getting a connection to layout output file!");
					return;
				}
			}else{
				layoutWriter = new BufferedWriter(new PrintWriter(System.out));
			}
			
			BufferedWriter overlapWriter = null;
			if(outputOverlapsFilePath != null){
				//the user specified the layout file
				try {
					overlapWriter = new BufferedWriter(new FileWriter(new File(outputOverlapsFilePath), false));
				} catch (IOException e) {
					System.err.println("Error while getting a connection to overlap output file!");
					return;
				}
			}else{
				overlapWriter = new BufferedWriter(new PrintWriter(System.out));
			}
			
			//got all needed information, start the procedure
			//create a formatter to use
			IOverlapGraphFormatter formatter = new MinimuslikeOverlapGraphFormatter();
			
			//read the input overlap graph
			OverlapGraph inputOverlapGraph = null;
			try {
				inputOverlapGraph = formatter.overlapGraphFromOverlapFileAndReadsFile(overlapsInputFile, readsInputFile);
			} catch (IOException ioException) {
				System.err.println("Error while reading from the input file! ");
				return;
			}catch (FormatterException formatterException) {
				System.err.println("Input file contains an error - \""+formatterException.getMessage()+"\"");
				return;
			}
			//simplify the graph
			OverlapGraph simplifiedGraph = Unitigging.simplifiedOverlapGraphFromGraphWithParameters(inputOverlapGraph, epsilon, alpha);
			
			//write it to output
			try {
				formatter.formatAndWriteOverlapGraph(simplifiedGraph, layoutWriter, overlapWriter);
			} catch (IOException e1) {
				System.err.println("Error while writing to output!");
				return;
			}
		}
	}

}
