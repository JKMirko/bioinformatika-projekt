package hr.fer.zesoi.bioinfo.models;

/**
 * Class representing an edge between two INode objects
 * 
 */
public class Edge {
	
	private int idA;
	private int idB;
	private int originalHangA;
	private int originalHangB;
	private boolean sufA;
	private boolean sufB;
	
	private EdgeType type;
	
	private int length;

	public Edge(int idA, int idB, int originalHangA, int originalHangB, boolean sufA,
			boolean sufB, EdgeType type) {
		super();
		this.idA = idA;
		this.idB = idB;
		this.originalHangA = originalHangA;
		this.originalHangB = originalHangB;
		this.sufA = sufA;
		this.sufB = sufB;
		this.type = type;
	}

	public int getIdA() {
		return idA;
	}

	public int getIdB() {
		return idB;
	}

	public void setIdA(int idA) {
		this.idA = idA;
	}

	public void setIdB(int idB) {
		this.idB = idB;
	}

	public int getHangA() {
		return this.originalHangA < 0 ? -this.originalHangA : this.originalHangA;
	}
	
	public int getOriginalHangA(){
		return this.originalHangA;
	}

	public int getHangB() {
		return this.originalHangB < 0 ? -this.originalHangB : this.originalHangB;
	}
	
	public int getOriginalHangB(){
		return this.originalHangB;
	}

	public boolean isSufA() {
		return sufA;
	}

	public boolean isSufB() {
		return sufB;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public EdgeType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Edge [idA=" + idA + ", idB=" + idB + ", hangA=" + this.getHangA()
				+ ", hangB=" + this.getHangB() + ", sufA=" + sufA + ", sufB=" + sufB
				+ ", length=" + length + "]";
	}
	
	
	//helpers
	public Integer getOtherId(int id){
		if(id == this.idA){
			return new Integer(this.idB);
		}else{
			return new Integer(this.idA);
		}
	}
	public boolean shouldBeRemoved = false;
	
}
