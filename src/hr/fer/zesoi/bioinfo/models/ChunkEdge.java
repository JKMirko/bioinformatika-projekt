package hr.fer.zesoi.bioinfo.models;

/**
 * Class representing an edge in the chunk graph
 *
 */
public class ChunkEdge<T extends ChunkNode> extends Edge<T> {
	
	public ChunkEdge(T a, T b) {
		super(a, b);
	}

	/**
	 * Removes the edge by removing itself from the ChuckNodes that contain it
	 */
	@SuppressWarnings("unchecked")
	public void remove(){
		this.getA().removeEdge((ChunkEdge<ChunkNode>) this);
		this.getB().removeEdge((ChunkEdge<ChunkNode>) this);
	}
	
}
