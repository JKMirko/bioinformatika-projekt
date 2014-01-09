package hr.fer.zesoi.bioinfo.models;

import java.util.ArrayList;
import java.util.List;


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
