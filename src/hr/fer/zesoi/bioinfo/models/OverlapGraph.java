package hr.fer.zesoi.bioinfo.models;

import java.util.HashMap;



public class OverlapGraph {
		
	//key read ID, value Read
	private HashMap<Integer, Read> readMap;
	//key - id of the contained read, value - id of the read that contains
	private HashMap<Integer, Integer> containments;
	
	public OverlapGraph(HashMap<Integer, Read> readMap,
			HashMap<Integer, Integer> containments) {
		super();
		this.readMap = readMap;
		this.containments = containments;
	}

	public HashMap<Integer, Read> getReadMap() {
		return readMap;
	}

	public HashMap<Integer, Integer> getContainments() {
		return containments;
	}
	
}
