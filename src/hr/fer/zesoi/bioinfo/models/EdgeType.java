package hr.fer.zesoi.bioinfo.models;

public enum EdgeType {
	//----->
	//   ---->
	NORMAL,
	// <------
	//    <----
	ANTI_NORMAL,
	//----->
	//   <-----
	INNIE,
	//<------
	//    ----->
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


