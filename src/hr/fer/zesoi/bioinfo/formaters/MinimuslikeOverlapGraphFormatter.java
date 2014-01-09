package hr.fer.zesoi.bioinfo.formaters;

import hr.fer.zesoi.bioinfo.models.OverlapGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class MinimuslikeOverlapGraphFormatter implements IOverlapGraphFormatter {

	private BufferedReader reader;

	@Override
	public OverlapGraph overlapGraphFromFile(File file) throws IOException {
		reader = new BufferedReader(new FileReader(file));
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
				this.readObjectFromMap(currentReadMap);
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
		return null;
	}
	
	private Object readObjectFromMap(HashMap<String, String> readMap) throws FormatterException{
		
		int ahg = this.intFromMap("ahg", readMap);
		int bhg = this.intFromMap("bhg", readMap);
		
		//do something
		
		return null;
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
		// TODO Auto-generated method stub
		
	}

}
