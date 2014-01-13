package hr.fer.zesoi.bioinfo.models;

import java.util.HashMap;
import java.util.List;

/**
 * Class holding all the needed data to represent an overlap graph.
 * When instantiated, the graph is based on reads and their edges.
 * After the unitigging process, the graph is based on chunks - it's a chunk graph
 * @author Bioinfo team
 *
 */
public class OverlapGraph {
		
	
	private HashMap<Integer, Read> readsInGraph;
	
	private HashMap<Integer, Read> containedReads;
	
	private HashMap<Integer, List<Edge>> containmentInfo;
	
	private HashMap<Integer, Chunk> chunksInGraph;
	
	public OverlapGraph(HashMap<Integer, Read> readsInGraph,
			HashMap<Integer, Read> containedReads,
			HashMap<Integer, List<Edge>> containmentInfo) {
		super();
		this.readsInGraph = readsInGraph;
		this.containedReads = containedReads;
		this.containmentInfo = containmentInfo;
	}

	/**
	 *
	 * Returns a map containing non-contained reads that may or may not be interconnected with each other
	 * Key - Read id
	 * Value - Read
	 * @return map
	 */
	public HashMap<Integer, Read> getReadsInGraph() {
		return readsInGraph;
	}

	/**
	 * Returns a map of reads that are contained by some other read in the graph
	 * Key - Read id
	 * Value Read
	 * @return map
	 */
	public HashMap<Integer, Read> getContainedReads() {
		return containedReads;
	}

	/**
	 * Returns the map containing the containment information from the graph
	 * Key - id of the read that contains others
	 * Value - list of edges that represent that containment
	 * @return map
	 */
	public HashMap<Integer, List<Edge>> getContainmentInfo() {
		return containmentInfo;
	}

	/**
	 * Returns a map containing chunks that may or may not be interconnected with each other
	 * Key - Read id
	 * Value - Chunk
	 * @return map
	 */
	public HashMap<Integer, Chunk> getChunksInGraph() {
		return chunksInGraph;
	}

	/**
	 * Sets the chunks in the read
	 * @param chunksInGraph Chunk map
	 */
	public void setChunksInGraph(HashMap<Integer, Chunk> chunksInGraph) {
		this.chunksInGraph = chunksInGraph;
	}
	
	
	
	
	
}
