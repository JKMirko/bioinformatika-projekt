package hr.fer.zesoi.bioinfo;

import hr.fer.zesoi.bioinfo.models.Edge;
import hr.fer.zesoi.bioinfo.models.OverlapGraph;
import hr.fer.zesoi.bioinfo.models.Read;

import java.util.HashMap;

public class Unitigging {
	
	
	private static final double epsilon = 0.1;
	private static final double alpha = 0;
	
	public static OverlapGraph simplifiedOverlapGraphFromGraph(OverlapGraph graphToSimplitfy){
		
		//step 2
		//transitive edge removal
		HashMap<Integer, Read> readMap = graphToSimplitfy.getReadMap();
		
		//for each read
		for(Read f : readMap.values()){
			//reverse 
			for(int tauCounter = f.getEdges().size() - 1; tauCounter > 0; tauCounter--){
				Edge tau = f.getEdges().get(tauCounter);
				Read g = readMap.get(tau.getOtherId(f.getId()));
				for(int tauPrimCounter = g.getEdges().size() - 1; tauPrimCounter > 0; tauPrimCounter --){
					Edge tauPrim = g.getEdges().get(tauPrimCounter);
					Read h = readMap.get(tauPrim.getOtherId(g.getId()));
					for(int piCounter = h.getEdges().size() - 1; piCounter > 0; piCounter--){
						Edge pi = h.getEdges().get(piCounter);
						if(pi.getOtherId(h.getId()) == f.getId()){
							//got the wanted connection
							// f <tau> g <tauPrim> h and f <pi> h
							//check conditions for removal
							if(tau.isSufB() != tauPrim.isSufA()
									&& pi.isSufA() == tau.isSufA()
									&& pi.isSufB() == tauPrim.isSufB()
									&& valueWithinValues(tau.getHangA() + tauPrim.getHangA(), pi.getHangA() - epsilon * pi.getLength() + alpha, pi.getHangA() + epsilon * pi.getLength() + alpha)
									&& valueWithinValues(tau.getHangB() + tauPrim.getHangB(), pi.getHangB() - epsilon * pi.getLength() + alpha, pi.getHangB() + epsilon * pi.getLength() + alpha)
									){
								//should remove the edge!
								pi.shouldBeRemoved = true;
							}
						}
					}
				}
			}
		}
		
		int counter = 0;
		int nonRemoved = 0;
		
		//iterate over the edges, remove flagged ones
		for(Read read : readMap.values()){
			for(int removalCounter = read.getEdges().size() - 1; removalCounter > 0; removalCounter --){
				Edge edgeToRemove = read.getEdges().get(removalCounter);
				if(edgeToRemove.shouldBeRemoved){
					counter++;
					readMap.get(new Integer(edgeToRemove.getIdA())).removeEdge(edgeToRemove);
					readMap.get(new Integer(edgeToRemove.getIdB())).removeEdge(edgeToRemove);
				}else{
					nonRemoved++;
				}
			}
		}
		
		System.out.println("Removed "+counter);
		System.out.println("Ostalo "+nonRemoved);
		return null;
	}
	
	private static boolean valueWithinValues(double value, double from, double to){
		return value >= from && value <= to;
	}
	
}
