package hr.fer.zesoi.bioinfo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a single read. Since we don't need the exact bases in the algorithm,
 * this class only has the length of the read, its id in the input file, and a list of edges
 * representing overlaps between reads.
 * @author Bioinfo team
 *
 */
public class Read {

	private int id;
	private int length;
	private List<Edge> edges;
	
	public Read(int id, int length){
		this.id = id;
		this.length = length;
		this.edges = new ArrayList<Edge>();
	}
	
	public void addEdge(Edge edge){
		this.edges.add(edge);
	}
	
	public void removeEdge(Edge edge){
		this.edges.remove(edge);
	}

	public int getId() {
		return id;
	}

	public int getLength() {
		return length;
	}

	public List<Edge> getEdges() {
		return edges;
	}
	
	public void clearEdges(){
		this.edges.clear();
	}
	
	/**
	 * Gets the Edge object that represents the edge between the provided read
	 * @param another Another read
	 * @return Edge between reads or null
	 */
	public Edge getEdgeWithRead(Read another){
		for(Edge edge : this.edges){
			if((edge.getIdA() == this.id && edge.getIdB() == another.getId())
					|| (edge.getIdB() == this.id && edge.getIdA() == another.getId())){
				return edge;
			}
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Read)){
			return false;
		}
		Read another = (Read)obj;
		return this.id == another.id;
	}

	@Override
	public String toString() {
		return "Read [id=" + id + ", length=" + length + ", edgesCount=" + edges.size()
				+ "]";
	}
	
	
	
}
