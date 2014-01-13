package hr.fer.zesoi.bioinfo.models;

/**
 * Class representing an edge between two objects that have int ids.
 * In the scope of this package, this class is used for representing overlaps between 2 Reads or 2 Chunks.
 * This class contains all the data that is required by the unitigging algorithm
 *  and all the data needed to correctly format the output
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

	/**
	 * Gets the ID of the object associated as A in this edge
	 * @return ID
	 */
	public int getIdA() {
		return idA;
	}

	/**
	 * Gets the ID of the object associated as B in this edge
	 * @return ID
	 */
	public int getIdB() {
		return idB;
	}
	
	/**
	 * Sets the ID of the object associated as A in this edge
	 * @param idA New ID
	 */
	public void setIdA(int idA) {
		this.idA = idA;
	}

	/**
	 * Sets the ID of the object associated as B in this edge
	 * @param idB New ID
	 */
	public void setIdB(int idB) {
		this.idB = idB;
	}

	/**
	 * Gets the hang value of the object associated as A in this Edge
	 * Hang value represents the number of bases not in the overlap.
	 * This method always returns a value greater or equal to 0, as required by the algorithm
	 * @return Hang value
	 */
	public int getHangA() {
		return this.originalHangA < 0 ? -this.originalHangA : this.originalHangA;
	}
	
	/**
	 * Gets the original hang value of the object associated as A in this Edge
	 * In a way, original hang value represents the number of reads not in the overlap.
	 * This value can be negative, thus representing "direction" relative to the overlap.
	 * Since the algorithm requires positive values, use hangA value for calculations.
	 * This value is only used to currently format the output
	 * @return Original hang value
	 */
	public int getOriginalHangA(){
		return this.originalHangA;
	}

	/**
	 * Gets the hang value of the object associated as B in this Edge
	 * Hang value represents the number of bases not in the overlap.
	 * This method always returns a value greater or equal to 0, as required by the algorithm
	 * @return Hang value
	 */
	public int getHangB() {
		return this.originalHangB < 0 ? -this.originalHangB : this.originalHangB;
	}
	
	/**
	 * Gets the original hang value of the object associated as B in this Edge
	 * In a way, original hang value represents the number of reads not in the overlap.
	 * This value can be negative, thus representing "direction" relative to the overlap.
	 * Since the algorithm requires positive values, use hangB value for calculations.
	 * This value is only used to currently format the output
	 * @return Original hang value
	 */
	public int getOriginalHangB(){
		return this.originalHangB;
	}

	/**
	 * Value indicating the direction of the object associated as A in this Edge
	 * @return true if the part of the object associated as A in this Edge contained in this Edge is its suffix, false otherwise
	 */
	public boolean isSufA() {
		return sufA;
	}

	/**
	 * Value indicating the direction of the object associated as B in this Edge
	 * @return true if the part of the object associated as B in this Edge contained in this Edge is its suffix, false otherwise
	 */
	public boolean isSufB() {
		return sufB;
	}

	/**
	 * Gets the length of the overlap calculated as the number of bases in the overlap
	 * @return length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Sets the length of the overlap
	 * @param length New length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Gets the type of this Edge
	 * @return Edge type
	 */
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
