package hr.fer.zesoi.bioinfo.formaters;

import hr.fer.zesoi.bioinfo.models.Chunk;
import hr.fer.zesoi.bioinfo.models.Edge;
import hr.fer.zesoi.bioinfo.models.OverlapGraph;
import hr.fer.zesoi.bioinfo.models.Read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MinimuslikeOverlapGraphFormatter implements IOverlapGraphFormatter {

	private BufferedReader reader;
	
	@Override
	public OverlapGraph overlapGraphFromOverlapFileAndReadsFile(
			File overlapsFile, File readsFile) throws IOException,
			FormatterException {
		List<Edge> edgesFromFile = new LinkedList<Edge>();
		//see OverlapGraph for key-value details
		HashMap<Integer, List<Edge>> containmentInfo = new HashMap<Integer, List<Edge>>();
		//set for storing id of the reads that are contained by another read
		Set<Integer> containedReadsIds = new HashSet<Integer>();
		
		reader = new BufferedReader(new FileReader(overlapsFile));
		String currentLine = null;
		//map used for storing data for current reads
		//since some of the fields can be multiline, we cannot store the values directly after every read
		HashMap<String, String> currentReadMap = new HashMap<String, String>();
		String currentMapKey = null;
		String currentMapValue = null;
		//read all lines
		while((currentLine = reader.readLine()) != null ){
			if(currentLine.equals("{OVL")){
				//empty the map for next read
				currentReadMap.clear();
			}else if(currentLine.equals("}")){
				//end of a read, store the last key-value pair and process data
				//store last key-value pair
				if(currentMapKey != null){
					currentReadMap.put(currentMapKey, currentMapValue);
				}
				
				Edge edge = this.getEdgeFromMap(currentReadMap, containmentInfo, containedReadsIds);
				if(edge != null){
					edgesFromFile.add(edge);
				}
			}else if(currentLine.matches("[a-z][a-z][a-z]:.*")){
				//start of a new key-pair value
				//store old key=value pair
				if(currentMapKey != null){
					currentReadMap.put(currentMapKey, currentMapValue);
				}
				//start with the new one
				int indexOfSeparator = 3;
				currentMapKey = currentLine.substring(0, indexOfSeparator);
				currentMapValue = currentLine.substring(indexOfSeparator+1, currentLine.length()); 
			}else{
				//append to the current read
				if(currentMapValue != null){
					currentMapValue += "\n" + currentLine;
				}
			}
		}
		//close the reader
		reader.close();
		
		//got the edges, read the reads now
		//prepare the storage for reads
		HashMap<Integer, Read> readMap = new HashMap<Integer, Read>();
		//differentiate the contained reads from the start
		HashMap<Integer, Read> containedReads = new HashMap<Integer, Read>();
		reader = new BufferedReader(new FileReader(readsFile));
		String readLine = null;
		boolean isLineOdd = true;
		while((readLine = reader.readLine()) != null){
			if(isLineOdd){
				//read info
				//example : >small/reads.2k.10x_000000000_000000001_L000001549:000000029-000001577:F
				String[] splitted = readLine.split("_");
				try {
					Integer id = Integer.parseInt(splitted[splitted.length - 2]);
					int length = Integer.parseInt(splitted[splitted.length -1].split(":")[0].substring(1));
					if(containedReadsIds.contains(id)){
						containedReads.put(id, new Read(id.intValue(), length));
					}else{
						readMap.put(id, new Read(id.intValue(), length));
					}
				} catch (Exception e) {
					throw new FormatterException("Invalid read info format!");
				}
			}
			isLineOdd = !isLineOdd;
		}
		
		reader.close();
		
		//connect the edges and reads
		for(Edge edge : edgesFromFile){
			Integer idA = new Integer(edge.getIdA());
			Integer idB = new Integer(edge.getIdB());
			//do not add edges for contained reads - same as removing them
			if(containedReadsIds.contains(idA) || containedReadsIds.contains(idB)){
				continue;
			}
			Read a = readMap.get(idA);
			Read b = readMap.get(idB);
			//calculate the edge length
			edge.setLength(( (a.getLength() - edge.getHangA()) + (b.getLength() - edge.getHangB()) )/2);
			//add edge to reads
			a.addEdge(edge);
			b.addEdge(edge);
		}
		
		return new OverlapGraph(readMap, containedReads, containmentInfo);
	}
	
	/**
	 * Creates an Edge from the information contained in the provided map, or stores the containment info to the containers
	 * @param readMap Map containing the values needed to create the Edge
	 * @param containmentInfo Map in witch to store the containment info - the Edge that represents the containment
	 * @param containedReadsIds Set in witch the store the ID of the read that is contained by another read
	 * @return newly created edge, or null if the edge represents containment. 
	 * If the return value is null, the containment info is added to the list, and the contained id is stored in the set
	 * @throws FormatterException throws a FormatterException if the info in the read map is invalid, or some of the information is missing
	 */
	private Edge getEdgeFromMap(HashMap<String, String> readMap, HashMap<Integer, List<Edge>> containmentInfo, Set<Integer> containedReadsIds) throws FormatterException{
		
		int ahg = this.intFromMap("ahg", readMap);
		int bhg = this.intFromMap("bhg", readMap);
		
		//get reads ids
		String rdsVale = readMap.get("rds");
		if(rdsVale == null){
			throw new FormatterException("Missing \"rds\" parameter!");
		}
		String[] splitted = readMap.get("rds").split(",");
		if(splitted.length != 2){
			throw new FormatterException("\"rds\" parameter must contaiin 2 read ids separated by \",\"");
		}
		Integer idA = null;
		Integer idB = null;
		
		try {
			idA = Integer.parseInt(splitted[0]);
			idB = Integer.parseInt(splitted[1]);
		} catch (NumberFormatException e) {
			throw new FormatterException("Read ids in the \"rds\" paramamter must be integers!");
		}
		
		//remove edges that represent containment
		if(ahg * bhg <= 0){
			//add the edge to set
			//TODO add containment data calculations
			Edge edgeToAdd = null;
			Integer idOfTheReadThatContains = null;
			Integer idOfTheReadThatIsContained = null;
			if(ahg <= 0){
				idOfTheReadThatContains = idB;
				idOfTheReadThatIsContained = idA;
			}else{
				idOfTheReadThatContains = idA;
				idOfTheReadThatIsContained = idB;
			}
			containedReadsIds.add(idOfTheReadThatIsContained);
			edgeToAdd = new Edge(idA.intValue(), idB.intValue(), Math.abs(ahg), Math.abs(bhg), true, true);
			if(containmentInfo.get(idOfTheReadThatContains) == null){
				List<Edge> edgeList = new ArrayList<Edge>();
				edgeList.add(edgeToAdd);
				containmentInfo.put(idOfTheReadThatContains, edgeList);
			}else{
				containmentInfo.get(idOfTheReadThatContains).add(edgeToAdd);
			}
			return null;
		}
			
		boolean sufA = false;
		boolean sufB = false;
		String adj = readMap.get("adj");
		//get sufs
		if(adj.equals("N")){
			if(ahg > 0 && bhg > 0){
				sufA = true;
				sufB = false;
			}else{
				sufA = false;
				sufB = true;
			}
		}else if(adj.equals("A")){
			if(ahg > 0 && bhg > 0){
				sufA = false;
				sufB = true;
			}else{
				sufA = true;
				sufB = false;
			}
		}else if(adj.equals("I")){
			if(ahg > 0 && bhg > 0){
				sufA = true;
				sufB = true;
			}else{
				sufA = false;
				sufB = false;
			}
		}else if(adj.equals("O")){
			if(ahg > 0 && bhg > 0){
				sufA = false;
				sufB = false;
			}else{
				sufA = true;
				sufB = true;
			}
		}else{
			throw new FormatterException("Excpected \"N\",\"A\",\"I\" or \"O\" for adj, got \""+adj+"\"");
		}
		
		
		if(splitted.length != 2){
			throw new FormatterException("Overlap requires 2 reads!");
		}
		
		return new Edge(idA, idB, Math.abs(ahg), Math.abs(bhg), sufA, sufB);
		
	}
	
	/**
	 * Reads an integer from a map
	 * @param key Key under witch the wanted string is stored
	 * @param map Map containing the wanted value as a String under the key - key
	 * @return parsed int value
	 * @throws FormatterException Throws a FormatterException if the value is not stored in the map, or the value cannot be parsed as an int
	 */
	private int intFromMap(String key, HashMap<String, String> map) throws FormatterException{
		try {
			return Integer.parseInt(map.get(key));
		} catch (NumberFormatException e) {
			throw new FormatterException("Expected int value for \""+key+"\" got \""+map.get(key)+"\"");
		}
	}

	
	@Override
	public void formatAndWriteOverlapGraph(OverlapGraph graph,
			Writer layoutInformationWriter, Writer overlapInformationWriter)
			throws IOException, FormatterException {		
		//write the layout information to the output
		PrintWriter printWriter = new PrintWriter(layoutInformationWriter);
		//get the reads
		HashMap<Integer, List<Edge>> containmentInfo = graph.getContainmentInfo();
		HashMap<Integer, Read> containedReads = graph.getContainedReads();
		
		for(Chunk chunk : graph.getChunksInGraph().values()){
			printWriter.println("{LAY");
			List<Read> reads = chunk.getReads();
			for(int readIterator = 0; readIterator < reads.size(); readIterator++){
				printWriter.println("{KAVELJ");
				Read read = reads.get(readIterator);
				int offset = 0;
				if(readIterator == chunk.getReads().size() - 1){
					//last read
				}else{
					//determine the orientation
					int nextReadId = reads.get(readIterator + 1).getId();
					Edge edgeConnectingWithTheNextEdge = null;
					
				}
				
				printWriter.println("NISAM CONTAINAN!");
				printWriter.println("}");
				//check for contained reads
				if(containmentInfo.containsKey(new Integer(read.getId()))){
					for(Edge edgeThatRepresentsContainment : containmentInfo.get(new Integer(read.getId()))){
						printWriter.println("{KAVELJ");
						printWriter.println("CONTAINAN SAM!");
						printWriter.println("}");
					}
				}
			}
			printWriter.println("}");
		}
		printWriter.close();
	}


	

}
