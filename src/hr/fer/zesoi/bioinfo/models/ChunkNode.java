package hr.fer.zesoi.bioinfo.models;

import java.util.ArrayList;
import java.util.List;

public class ChunkNode implements INode {

	private int id;
	private List<ChunkEdge<ChunkNode>> edges;
	private List<Read> reads;
	
	/**
	 * Creates a new instance of ChunkNode with given id containing the given node
	 * @param id Chunk id
	 * @param read Initial node in the chunk
	 */
	public ChunkNode(int id, Read read){
		this.id = id;
		this.edges = new ArrayList<ChunkEdge<ChunkNode>>();
		this.reads = new ArrayList<Read>(1);
		this.reads.add(read);
	}
	
	@Override
	public int getId() {
		return this.id;
	}
	
	/**
	 * Removes the given edge from the list of edges
	 * @param edge Edge to remove
	 */
	public void removeEdge(ChunkEdge<ChunkNode> edge){
		this.edges.remove(edge);
	}
	
	/**
	 * Adds the given edge to the list of edges
	 * @param edge Edge to add
	 */
	public void addEdge(ChunkEdge<ChunkNode> edge){
		this.edges.add(edge);
	}
	
	/**
	 * Merges with the give chunk node. This method makes sure the edges between chunk nodes and their inner Reads are dealt with properly
	 * @param chunkNodeToMergeWith ChunkNode to merge with
	 */
	public void mergeWithChunkNode(ChunkNode chunkNodeToMergeWith){
		//create the same edge between this chunk and the chunk that's connected with the chunk we want to merge with
		//get the wanted edge
		ChunkEdge<ChunkNode> targetChunkEdge = null;
		//there should exist only 2 edges at this point, the one between this chunk and the given one, 
		//and the one between the given chunk and the next one
		for(ChunkEdge<ChunkNode> iterateTroughtEdges : chunkNodeToMergeWith.edges){
			if(!iterateTroughtEdges.getA().equals(this) && !iterateTroughtEdges.getB().equals(this)){
				targetChunkEdge = iterateTroughtEdges;
				break;
			}
		}
		//make sure everything is ok
		if(targetChunkEdge == null){
			throw new IllegalStateException("Illegal state. Each chunk should only have 2 edges at this point!");
		}
		
		//determine if the ChunkNode we want to merge with is A or B in the edge
		boolean chunkNodeToMergeWithIsA = targetChunkEdge.getA().equals(chunkNodeToMergeWith);
		
		//get the node in the edge that is not the one we are merging with
		ChunkNode targetChunkNode = chunkNodeToMergeWithIsA ? targetChunkEdge.getB() : targetChunkEdge.getA();
		
		//create the new edge
		ChunkEdge<ChunkNode> newEdge = new ChunkEdge<ChunkNode>(chunkNodeToMergeWithIsA ? this : targetChunkNode
				, chunkNodeToMergeWithIsA ? targetChunkNode : this);
		//copy edge properties
		//TODO
		//remove the old chunk edge
		targetChunkEdge.remove();
		//add the new edge
		this.addEdge(newEdge);
		targetChunkNode.addEdge(newEdge);
		
		
		//establish the connection between the last read in this chunk and the first read in the next chunk
		//get the edge between this chunk and chunk to merge with
		ChunkEdge<ChunkNode> chunkEdgeWithTheNodeToMergeWith = null;
		boolean thisChunkIsA = false;
		//should only be 2
		for(ChunkEdge<ChunkNode> iterateTroughtOwnEdges : this.edges){
			if(iterateTroughtOwnEdges.getA().equals(this) && iterateTroughtOwnEdges.getB().equals(chunkNodeToMergeWith)){
				chunkEdgeWithTheNodeToMergeWith = iterateTroughtOwnEdges;
				thisChunkIsA = true;
				break;
			}else if(iterateTroughtOwnEdges.getA().equals(chunkNodeToMergeWith) && iterateTroughtOwnEdges.getB().equals(this)){
				chunkEdgeWithTheNodeToMergeWith = iterateTroughtOwnEdges;
				thisChunkIsA = false;
				break;
			}
		}
		
		//make sure everything is ok
		if(chunkEdgeWithTheNodeToMergeWith == null){
			throw new IllegalStateException("Illegal state. Merging chunks that dont have an edge connecting them!");
		}
		//always merge b to a
		if(!thisChunkIsA){
			throw new IllegalStateException("Illegal state. Always merge B into A!");
		}
		//create the edge
		Read a = this.reads.get(this.reads.size() - 1);
		Read b = this.reads.get(0);
		
		Edge<Read> newReadEdge = new Edge<Read>(a, b);
		a.addEdge(newReadEdge);
		b.addEdge(newReadEdge);
		
		//add reads from another instance to this one
		for(Read read : chunkNodeToMergeWith.reads){
			this.reads.add(read);
		}
		
		
	}
	
}
