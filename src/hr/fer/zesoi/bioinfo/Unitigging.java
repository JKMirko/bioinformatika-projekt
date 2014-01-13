package hr.fer.zesoi.bioinfo;

import hr.fer.zesoi.bioinfo.models.Chunk;
import hr.fer.zesoi.bioinfo.models.Edge;
import hr.fer.zesoi.bioinfo.models.OverlapGraph;
import hr.fer.zesoi.bioinfo.models.Read;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class containing the actual unitigging algorithm
 * @author Bioinfo team
 *
 */
public class Unitigging {
	
	/**
	 * Simplifies the provided graph by removing transitive edges and merging reads into chunks.
	 * Note that the input Graph cannot contain reads that are contained by others in its readsInGraph field
	 * @param graphToSimplitfy Graph to simplify
	 * @return the simplified graph
	 */
	public static OverlapGraph simplifiedOverlapGraphFromGraphWithParameters(OverlapGraph graphToSimplitfy, double epsilon, int alpha){
		//step 2
		//transitive edge removal
		HashMap<Integer, Read> readMap = graphToSimplitfy.getReadsInGraph();
		
		//for each read, get the f,g and h
		for(Read f : readMap.values()){
			//reverse for easier removal
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
					//remove from both edges
					readMap.get(new Integer(edgeToRemove.getIdA())).removeEdge(edgeToRemove);
					readMap.get(new Integer(edgeToRemove.getIdB())).removeEdge(edgeToRemove);
				}
			}
		}
		//lets play with chunks
		//every Read is a chunk!
		//every chunk will have the same id as its initial read thus ensuring that they are mutualy unique
		HashMap<Integer, Chunk> chunkMap = new HashMap<Integer, Chunk>();
		List<Integer> chunkIds = new ArrayList<Integer>();
		for(Read read : readMap.values()){
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
				//check the merging conditions
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
				//can merge them, do it
				if(canMergeChunks){
					Chunk a = chunkMap.get(edge.getIdA());
					Chunk b = chunkMap.get(edge.getIdB());
					a.mergeWithChunkOnEdge(b, edge);
					//remove the chunk that we meged into another
					chunkMap.remove(new Integer(b.getId()));
					if(a.getId() == currentChunkId.intValue()){
						//we merged another chunk into this one
						//and removed the edge on witch they are merged.
						//reduce the counter so that any edges that are inherited from the b chunk are processed
					}else{
						//we merged this chunk into another, stop processing this one
						break;
					}
					//since we removed the current edge, go back one step
					edgeIterator--;
				}
			}
		}
		
		graphToSimplitfy.setChunksInGraph(chunkMap);
		return graphToSimplitfy;
	}
	
	/**
	 * Checks if a value is within bounds
	 * @param value Value to check
	 * @param from Lower bound
	 * @param to Upper bound
	 * @return Result
	 */
	private static boolean valueWithinValues(double value, double from, double to){
		return value >= from && value <= to;
	}
	
}
