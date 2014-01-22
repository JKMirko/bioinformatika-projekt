package hr.fer.zesoi.bioinfo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a chunk in the chunk graph. It contains the reads within the chunk,
 * list of edges with other chunks and a unique id. This class also provides a convenient method
 * for merging one chunk into another.
 * @author Bioinfo team
 *
 */
public class Chunk {
	
	private List<Read> reads;
	private List<Edge> edges;
	private int id;
	
	/**
	 * Creates a chunk that contains just one read
	 * @param initialRead Initial read
	 */
	public Chunk(Read initialRead){
		this.reads = new ArrayList<Read>();
		this.reads.add(initialRead);
		this.edges = new ArrayList<Edge>();
		for(Edge edge : initialRead.getEdges()){
			this.edges.add(edge);
		}
		initialRead.clearEdges();
		this.id = initialRead.getId();
	}
	
	/**
	 * Merges another chunk into this one on given edge. 
	 * The edge on witch they merge will be removed from the chunk graph and will be used to connect the nodes inside the chunk.
	 * This chunk will take all of the reads and edges from the provided one, and will alter the edges so that the other edges that the provided chunk had now point to this chunk.
	 * @param chunk Chunk to merge into this one
	 * @param edge Edge on witch to merge
	 */
	public void mergeWithChunkOnEdge(Chunk chunk, Edge edge){
		//get the reads
		Read rightReadInThisChunk = this.reads.get(this.reads.size() - 1);
		Read leftInOther = chunk.reads.get(0);
		//establish a connection between them, reuse the edge
		edge.setIdA(rightReadInThisChunk.getId());
		edge.setIdB(leftInOther.getId());
		rightReadInThisChunk.addEdge(edge);
		leftInOther.addEdge(edge);
		//remove the edge from chunks
		this.edges.remove(edge);
		chunk.edges.remove(edge);
		if(this == chunk){
			System.out.println("SAM SA SOBOM!");
			System.out.println("EDGE "+edge);
		}
		//add the reads from other chunk to this one
		for(Read readInOtherChunk : chunk.reads){
			this.reads.add(readInOtherChunk);
		}
		//redirect the edges from other chunk to this one
		for(Edge edgeInOtherChunk : chunk.edges){
			if(edgeInOtherChunk.getIdA() == chunk.getId()){
				edgeInOtherChunk.setIdA(this.id);
			}else{
				edgeInOtherChunk.setIdB(this.id);
			}
			this.edges.add(edgeInOtherChunk);
		}
	}
	
	/**
	 * Gets the Edge object representing the edge between the provided chunk
	 * @param another Another chunk
	 * @return Edge between chunks or null
	 */
	public Edge getEdgeWithChunk(Chunk another){
		for(Edge edge : this.edges){
			if((edge.getIdA() == this.id && edge.getIdB() == another.getId())
					|| (edge.getIdB() == this.id && edge.getIdA() == another.getId())){
				return edge;
			}
		}
		return null;
	}

	public List<Read> getReads() {
		return reads;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Chunk [readsCount=" + reads.size() + ", edgesCount=" + edges.size() + ", id=" + id
				+ "]";
	}
	
	
	
}
