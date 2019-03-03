package org.SirTobiSwobi.c3.teek.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;

import org.SirTobiSwobi.c3.teek.core.Utilities;


public class WordEmbedding {
	private long id;
	private int size;
	private int dimensions;
	private String[] terms;
	private double[][] vectors;
	public WordEmbedding(int size, int dimensions, String[] terms, double[][] vectors) {
		super();
		this.size = size;
		this.dimensions = dimensions;
		this.terms = terms;
		this.vectors = vectors;
		this.id=-1;
	}
	
	public WordEmbedding(long id, int size, int dimensions, String[] terms, double[][] vectors) {
		super();
		this.id = id;
		this.size = size;
		this.dimensions = dimensions;
		this.terms = terms;
		this.vectors = vectors;
	}
	
	public String toString(){
		String toString=""+id+" "+size+" "+dimensions+" ";
		for(int i=0;i<size;i++){
			toString+=terms[i]+" ";
			for(int j=0;j<dimensions;j++){
				toString+=vectors[i][j]+" ";
			}
		}
		return toString;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getDimensions() {
		return dimensions;
	}
	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}
	public String[] getTerms() {
		return terms;
	}
	public void setTerms(String[] terms) {
		this.terms = terms;
	}
	public double[][] getVectors() {
		return vectors;
	}
	public void setVectors(double[][] vectors) {
		this.vectors = vectors;
	}
	
	public double[] getVectorForTerm(String term){
		int index = -1;
		for(int i=0;i<size;i++){
			if(term.equals(terms[i])){
				index=i;
			}
		}
		if(index==-1){
			return new double[dimensions];
		}
		return vectors[index];
	}
	
	public int getTermIndex(String term){
		int index = -1;
		for(int i=0;i<size;i++){
			if(term.equals(terms[i])){
				index=i;
			}
		}
		
		return index;
	}
	
	public synchronized void generateVectorFromContext(String unknownTerm, String before, String after){
		double[] newVector = new double[dimensions];
		if(getTermIndex(unknownTerm)==-1){
			if(getTermIndex(before)==-1){
				newVector=getVectorForTerm(after);
			}else if(getTermIndex(after)==-1){
				newVector=getVectorForTerm(before);
			}else if(getTermIndex(before)!=-1&&getTermIndex(after)!=-1){
				double[] vecBefore=getVectorForTerm(before);
				double[] vecAfter=getVectorForTerm(after);
				for(int i=0;i<dimensions;i++){
					newVector[i]=(vecBefore[i]+vecAfter[i])/2.0;
				}
			}
		}
		size++;
		double[][] newVectors = new double[size][dimensions];
		String[] newTerms = new String[size];
		for(int i=0;i<size-1;i++){
			newVectors[i]=vectors[i];
			newTerms[i]=terms[i];
		}
		newVectors[size-1]=newVector;
		newTerms[size-1]=unknownTerm;
		this.vectors=newVectors;
		this.terms=newTerms;
		
		
	}
	
	public static WordEmbedding buildFromLocalFile(String filePath) throws IOException{
		int size=0;
		int dimensions=0;
		double vectors[][] = null;
		String terms[] = null;
		WordEmbedding wordEmbedding = null;
		
		String[] splitResult;
		int vectorCounter=1;
		boolean countLines = false;
		
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine(); //gets the first line of the file
		splitResult=line.split(" ");
		if(Array.getLength(splitResult)!=2){ // is in GloVe format
			countLines = true;
		}
		br.close();
		
		
		if(countLines){
			size=0;
			dimensions=0;
			
			br = new BufferedReader(new FileReader(filePath));
			while((line = br.readLine())!=null){
				splitResult=line.split(" ");
				size++;
				dimensions=Array.getLength(splitResult)-1; //first is the word
			}
			vectors = new double[size][dimensions];
			terms = new String[size];
			br.close();
			br = new BufferedReader(new FileReader(filePath));
			while((line = br.readLine())!=null){
				splitResult=line.split(" ");
				terms[vectorCounter-1]=splitResult[0];
				 // 1. Entry is the actual word, all other entries are coordinates
				for(int i=1;i<Array.getLength(splitResult);i++){
					vectors[vectorCounter-1][i-1]=Double.parseDouble(splitResult[i]);
				}
				vectorCounter++;
			}
			br.close();
			wordEmbedding = new WordEmbedding(size, dimensions, terms, vectors);
			
		}else{
		
			
			br = new BufferedReader(new FileReader(filePath));
			while((line = br.readLine())!=null){
				splitResult=line.split(" ");
				if(Array.getLength(splitResult)==2){ //only happens in the first line of a word2vec file
					size=Integer.parseInt(splitResult[0]);
					dimensions=Integer.parseInt(splitResult[1]);
					vectors = new double[size][dimensions];
					terms = new String[size];
				}
				else{ 
					terms[vectorCounter-1]=splitResult[0];
					 // 1. Entry is the actual word, all other entries are coordinates
					for(int i=1;i<Array.getLength(splitResult);i++){
						vectors[vectorCounter-1][i-1]=Double.parseDouble(splitResult[i]);
					}
					vectorCounter++;
				}	
			}
			br.close();
			wordEmbedding = new WordEmbedding(size, dimensions, terms, vectors);
			
		}
		return wordEmbedding;
	}
	
	public double getCosineSimilary(int wordIndex1, int wordIndex2){
		double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < this.vectors[wordIndex1].length; i++) {
	        dotProduct += this.vectors[wordIndex1][i] * vectors[wordIndex2][i];
	        normA += Math.pow(this.vectors[wordIndex1][i], 2);
	        normB += Math.pow(this.vectors[wordIndex2][i], 2);
	    }   
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}
	
	public double getCosineDistance(int wordIndex1, int wordIndex2){
		return 1-getCosineSimilary(wordIndex1, wordIndex2);
	}
	
	public double getCosineSimilarity(int wordIndex1, double[] vector){
		assert(this.vectors[wordIndex1].length==vector.length);
		double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < this.vectors[wordIndex1].length; i++) {
	        dotProduct += this.vectors[wordIndex1][i] * vector[i];
	        normA += Math.pow(this.vectors[wordIndex1][i], 2);
	        normB += Math.pow(vector[i], 2);
	    }   
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}
	
	public double getCosineDistance(int wordIndex1, double[] vector){
		return 1-getCosineSimilarity(wordIndex1, vector);
	}
	
	public String[] getClosestWords(double[] coordinate, int n){
		String[] closestWords = new String[n];
		int[] closestIndices = new int[n];
		for(int i=0; i<n; i++){
			int closestIndex=-1;
			double closestDistance = 200000.0;
			for(int j=0; j<size; j++){
				double distance = getCosineDistance(j, coordinate);
				if(distance<closestDistance&&!Utilities.isIn(closestIndices, j)){
					closestDistance=distance;
					closestIndex=j;
				}
			}
			if(closestIndex!=-1){
				closestIndices[i]=closestIndex;
			}
		}
		for(int i=0; i<n; i++){
			closestWords[i]=terms[closestIndices[i]];
		}
		
		return closestWords;
	}
	
	public String[] getClosestWords(String word, int n){
		double[] coordinates=getVectorForTerm(word);
		return getClosestWords(coordinates,n);
	}
	
}
