package hr.fer.zesoi.bioinfo.models;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
	
	private List<Read> reads;
	private List<Edge> edges;
	private int id;
	
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

	public List<Read> getReads() {
		return reads;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public int getId() {
		return id;
	}
	
	
	
}
