package org.SirTobiSwobi.c3.teek.core;

import java.util.ArrayList;

import org.SirTobiSwobi.c3.teek.api.TCSvmNode;


public class Utilities {
	
	public static boolean isIn(int[] array, int value){
		boolean isIn=false;
		if(array!=null){
			for(int i=0;i<array.length;i++){
				if(array[i]==value){
					isIn=true;
				}
			}
		}
		return isIn;
	}
	
	public static boolean isIn(long[] array, long value){
		boolean isIn=false;
		if(array!=null){
			for(int i=0;i<array.length;i++){
				if(array[i]==value){
					isIn=true;
				}
			}
		}
		return isIn;
	}
	
	public static double[] addVectors(double[] vecA, double[] vecB){
		assert vecA.length==vecB.length;
		double[] newVector=new double[vecA.length];
		for(int i=0;i<newVector.length;i++){
			newVector[i]=vecA[i]+vecB[i];
		}
		return newVector;
	}
	
	public static double[] subtractVectors(double[] vecA, double[] vecB){
		assert vecA.length==vecB.length;
		double[] newVector=new double[vecA.length];
		for(int i=0;i<newVector.length;i++){
			newVector[i]=vecA[i]-vecB[i];
		}
		return newVector;
	}
	
	public static double[] multiplyVectorWithScalar(double[] vector, double scalar){
		for(int i=0;i<vector.length;i++){
			vector[i]=vector[i]*scalar;
		}
		return vector;
	}
	
	public static long[] increaseAndAddValueIfNotIn(long value, long[] array){	
		if(array==null){ //if the array does not exist, nothing can be in it
			array = new long[1];
			array[0] = value;
			return array;
		}
		if(!isIn(array, value)){
			long[] newArray = new long[array.length+1];
			for(int i=0; i<array.length;i++){
				newArray[i]=array[i];
			}
			newArray[array.length]=value;
			return newArray;
			
		}else{
			return array;
		}
	}
	
	public static long[] arrayUnionWithoutDuplicates(long[] a, long[] b){
		if(a==null){
			return b;
		}
		if(b==null){
			return a;
		}
		for(int i=0;i<a.length;i++){
			b = increaseAndAddValueIfNotIn(a[i],b);
		}
		return b;
	}
	
	public static int indexOf(long[] array, long element){
		int index = -1;
		for(int i=0;i<array.length;i++){
			if(array[i]==element){
				index=i;
			}
		}
		return index;
	}
	
	/**
	 * Computes all nodes reachable from a start node in a graph. 
	 * Nodes are addressed by longs.
	 * Edges are encoded as array of longs where edges[i][0] is the starting node and edges[i][1] is the ending node of edge i.
	 * @param edges
	 * @param start
	 * @return
	 */
	public static long[] BFSreachable(long[][] edges, long start){
		ArrayList<Long> visited = new ArrayList<Long>();
		ArrayList<Long> queue = new ArrayList<Long>();	
		
		visited.add(start);
		queue.add(start);
		while(!queue.isEmpty()){
			long node = queue.get(0);
			queue.remove(0);
			for(int i=0;i<edges.length;i++){
				if(edges[i][0]==node&&!visited.contains(edges[i][1])){
					queue.add(edges[i][1]);
					visited.add(edges[i][1]);
				}
			}
		}
		
		long[] result = new long[visited.size()];
		for(int i=0; i<visited.size(); i++){
			result[i]=visited.get(i);
		}
			
		return result;
	}	
	
	public static String sanitizeText(String text){
		text = text.replaceAll("<.*?>",  ""); //removes any xml tags
		text = text.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase();
		text = text.replaceAll("\\s"," ");
		text = text.replaceAll("\\s{2,}", " ").trim();
		text = text.toLowerCase();
		return text;
	}
	
	public static double cosineSimilarity(double[] vec1, double[] vec2){
		double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < vec1.length; i++) {
	        dotProduct += vec1[i] * vec2[i];
	        normA += Math.pow(vec1[i], 2);
	        normB += Math.pow(vec2[i], 2);
	    }   
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}
	
	public static double cosineDistance(double[] vec1, double[] vec2){
		return 1-Utilities.cosineSimilarity(vec1, vec2);
	}
	
	
}
