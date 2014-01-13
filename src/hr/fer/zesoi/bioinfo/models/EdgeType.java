package hr.fer.zesoi.bioinfo.models;

public enum EdgeType {
	/**
	 * -----><br />
	 * &nbsp;&nbsp;&nbsp;&nbsp;----->
	 */
	NORMAL,
	/**
	 * <-----<br />
	 * &nbsp;&nbsp;&nbsp;&nbsp;<-----
	 */
	ANTI_NORMAL,
	/**
	 * -----><br />
	 * &nbsp;&nbsp;&nbsp;&nbsp;<-----
	 */
	INNIE,
	/**
	 * <-----<br />
	 * &nbsp;&nbsp;&nbsp;&nbsp;----->
	 */
	OUTIE;
	
	public static String edgeTypeToString(EdgeType type){
		switch (type) {
		case NORMAL:
			return "N";
		case ANTI_NORMAL:
			return "A";
		case INNIE:
			return "I";
		case OUTIE:
			return "O";
		}
		return null;
	}
}


