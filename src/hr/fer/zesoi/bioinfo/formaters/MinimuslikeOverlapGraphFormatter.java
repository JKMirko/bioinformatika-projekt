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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MinimuslikeOverlapGraphFormatter implements IOverlapGraphFormatter {

	private BufferedReader reader;
	
	@Override
	public OverlapGraph overlapGraphFromOverlapFileAndReadsFile(
			File overlapsFile, File readsFile) throws IOException,
			FormatterException {
		List<Edge> edgesFromFile = new LinkedList<Edge>();
		HashMap<Integer, Integer> containedReads = new HashMap<Integer, Integer>();
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
				
				Edge edge = this.getEdgeFromMap(currentReadMap, containedReads);
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
		//prepare the storage
		HashMap<Integer, Read> readMap = new HashMap<Integer, Read>();
		reader = new BufferedReader(new FileReader(readsFile));
		String readLine = null;
		boolean isLineOdd = true;
		while((readLine = reader.readLine()) != null){
			if(isLineOdd){
				//read info
				//example : >small/reads.2k.10x_000000000_000000001_L000001549:000000029-000001577:F
				String[] splitted = readLine.split("_");
				try {
					int id = Integer.parseInt(splitted[splitted.length - 2]);
					int length = Integer.parseInt(splitted[splitted.length -1].split(":")[0].substring(1));
					
					readMap.put(new Integer(id), new Read(id, length));
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
			if(containedReads.containsKey(idA) || containedReads.containsKey(idB)){
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

		return new OverlapGraph(readMap, containedReads);
	}
	
	private Edge getEdgeFromMap(HashMap<String, String> readMap, HashMap<Integer, Integer> containedReads) throws FormatterException{
		
		int ahg = this.intFromMap("ahg", readMap);
		int bhg = this.intFromMap("bhg", readMap);
		
		//get reads ids
		String[] splitted = readMap.get("rds").split(",");
		Integer idA = Integer.parseInt(splitted[0]);
		Integer idB = Integer.parseInt(splitted[1]);
		
		//remove reads that represent contaiment
		if(ahg * bhg <= 0){
			//add the edge to set
			if(idA == 103 || idB == 103) System.out.println(ahg + " : "+bhg);
			if(ahg <= 0){
				containedReads.put(idA, idB);
			}else{
				containedReads.put(idB, idA);
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
	
	private int intFromMap(String key, HashMap<String, String> map) throws FormatterException{
		try {
			return Integer.parseInt(map.get(key));
		} catch (NumberFormatException e) {
			throw new FormatterException("Expected int value for \""+key+"\" got \""+map.get(key)+"\"");
		}
	}

	@Override
	public void formatAndWriteOverlapGraph(OverlapGraph graph, Writer writer)
			throws IOException {
		PrintWriter printWriter = new PrintWriter(writer);
		for(Chunk chunk : graph.getChunkMap().values()){
			printWriter.println("{LAY");
			List<Read> reads = chunk.getReads();
			for(int readIterator = 0; readIterator < reads.size(); readIterator++){
				if(readIterator == chunk.getReads().size()){
					//last read
				}else{
					//determine the orientation
					int nextReadId = reads.get(readIterator + 1).getId();
					Edge edgeConnectingWithTheNextEdge = null;
					
				}
			}
			printWriter.println("}");
		}
		printWriter.close();
	}

	

}
