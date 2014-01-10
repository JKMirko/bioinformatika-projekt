package hr.fer.zesoi.bioinfo;

import hr.fer.zesoi.bioinfo.models.Chunk;
import hr.fer.zesoi.bioinfo.models.Edge;
import hr.fer.zesoi.bioinfo.models.OverlapGraph;
import hr.fer.zesoi.bioinfo.models.Read;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Unitigging {
	
	
	private static final double epsilon = 0.1;
	private static final double alpha = 3;
	
	public static OverlapGraph simplifiedOverlapGraphFromGraph(OverlapGraph graphToSimplitfy){
		//step 2
		//transitive edge removal
		HashMap<Integer, Read> readMap = graphToSimplitfy.getReadMap();
		
		//for each read
		for(Read f : readMap.values()){
			//reverse 
			for(int tauCounter = f.getEdges().size() - 1; tauCounter >= 0; tauCounter--){
				Edge tau = f.getEdges().get(tauCounter);
				Read g = readMap.get(tau.getOtherId(f.getId()));
				for(int tauPrimCounter = g.getEdges().size() - 1; tauPrimCounter >= 0; tauPrimCounter --){
					Edge tauPrim = g.getEdges().get(tauPrimCounter);
					Read h = readMap.get(tauPrim.getOtherId(g.getId()));
					for(int piCounter = h.getEdges().size() - 1; piCounter >= 0; piCounter--){
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
		
		//iterate over the edges, remove flagged ones
		for(Read read : readMap.values()){
			for(int removalCounter = read.getEdges().size() - 1; removalCounter >= 0; removalCounter --){
				Edge edgeToRemove = read.getEdges().get(removalCounter);
				if(edgeToRemove.shouldBeRemoved){
					readMap.get(new Integer(edgeToRemove.getIdA())).removeEdge(edgeToRemove);
					readMap.get(new Integer(edgeToRemove.getIdB())).removeEdge(edgeToRemove);
				}
			}
		}
		//lets play with chunks
		//every Read is a chunk!
		//every chunk will have the same id as its initial read
		HashMap<Integer, Chunk> chunkMap = new HashMap<Integer, Chunk>();
		List<Integer> chunkIds = new ArrayList<Integer>();
		for(Read read : readMap.values()){
			//do not use reads with no edges
			if(read.getEdges().size() == 0){
				continue;
			}
			Chunk newChunk =  new Chunk(read);
			chunkMap.put(new Integer(newChunk.getId()),	newChunk);
			chunkIds.add(new Integer(newChunk.getId()));
		}
		
		for(int chunkIterator = 0;chunkIterator < chunkIds.size(); chunkIterator++){
			Integer currentChunkId = chunkIds.get(chunkIterator);
			if(!chunkMap.containsKey(currentChunkId)){
				//we merged this chunk into another one, do not process it
				continue;
			}
			Chunk chunk = chunkMap.get(currentChunkId);
			//go trough every edge in the current chunk
			for(int edgeIterator = 0; edgeIterator < chunk.getEdges().size(); edgeIterator++){
				boolean canMergeChunks = true;
				Edge edge = chunk.getEdges().get(edgeIterator);
				for(Edge edgeInTheTheLeftChunk : chunkMap.get(new Integer(edge.getIdA())).getEdges()){
					if(edgeInTheTheLeftChunk == edge){
						continue;
					}
					if(edge.isSufA() == edgeInTheTheLeftChunk.isSufB()){
						canMergeChunks = false;
						break;
					}
				}
				if(!canMergeChunks){
					continue;
				}
				for(Edge edgeInTheTheRightChunk : chunkMap.get(new Integer(edge.getIdB())).getEdges()){
					if(edgeInTheTheRightChunk == edge){
						continue;
					}
					if(edge.isSufB() == edgeInTheTheRightChunk.isSufA()){
						canMergeChunks = false;
						break;
					}
				}
				if(canMergeChunks){
					Chunk a = chunkMap.get(edge.getIdA());
					Chunk b = chunkMap.get(edge.getIdB());
					a.mergeWithChunkOnEdge(b, edge);
					chunkMap.remove(new Integer(b.getId()));
					edgeIterator--;
				}
			}
		}
		
		graphToSimplitfy.setChunkMap(chunkMap);
		return graphToSimplitfy;
	}
	
	private static boolean valueWithinValues(double value, double from, double to){
		return value >= from && value <= to;
	}
	
}
