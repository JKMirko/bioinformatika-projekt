package hr.fer.zesoi.bioinfo.models;

import java.util.ArrayList;
import java.util.List;

public class Read implements INode {

	private int id;
	private List<Edge<Read>> edges;
	
	/**
	 * Creates a new Read with given ID
	 * @param id Read ID
	 */
	public Read(int id){
		this.id = id;
		this.edges = new ArrayList<Edge<Read>>();
	}
	
	@Override
	public int getId() {
		return this.id;
	}
	
	/**
	 * Adds an edge to the list of edges
	 * @param edge Edge to add
	 */
	public void addEdge(Edge<Read> edge){
		this.edges.add(edge);
	}
	
}
