package hr.fer.zesoi.bioinfo.models;

/**
 * Class representing an edge between two INode objects
 * 
 */
public class Edge<T extends INode> {
	
	//TODO add documentation to this class
	
	private T a;
	private T b;
	
	public Edge(T a, T b){
		this.a = a;
		this.b = b;
	}
	
	public T getA(){
		return this.a;
	}
	
	public T getB(){
		return this.b;
	}
	
}
