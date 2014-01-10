package hr.fer.zesoi.bioinfo.models;

/**
 * Class representing an edge between two INode objects
 * 
 */
public class Edge {
	
	private int idA;
	private int idB;
	private int hangA;
	private int hangB;
	private boolean sufA;
	private boolean sufB;
	
	private int length;

	public Edge(int idA, int idB, int hangA, int hangB, boolean sufA,
			boolean sufB) {
		super();
		this.idA = idA;
		this.idB = idB;
		this.hangA = hangA;
		this.hangB = hangB;
		this.sufA = sufA;
		this.sufB = sufB;
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
		return hangA;
	}

	public int getHangB() {
		return hangB;
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

	@Override
	public String toString() {
		return "Edge [idA=" + idA + ", idB=" + idB + ", hangA=" + hangA
				+ ", hangB=" + hangB + ", sufA=" + sufA + ", sufB=" + sufB
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
