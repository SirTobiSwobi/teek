package org.SirTobiSwobi.c3.teek.core;

import java.util.ArrayList;

import org.SirTobiSwobi.c3.teek.db.WordEmbedding;

public class DaiClusterer {
	private WordEmbedding wordEmbedding;

	public DaiClusterer(WordEmbedding wordEmbedding) {
		super();
		this.wordEmbedding = wordEmbedding;
	}

	public WordEmbedding getWordEmbedding() {
		return wordEmbedding;
	}

	public void setWordEmbedding(WordEmbedding wordEmbedding) {
		this.wordEmbedding = wordEmbedding;
	}
	
	public ArrayList<DaiCluster> getDaiClusters(String text){
		ArrayList<DaiCluster> clusters = new ArrayList<DaiCluster>();
		text = Utilities.sanitizeText(text);
		int n=1;
		double p=1.0/(1.0+(double)n);
		String[] words = text.split(" ");	
		for(int i=0;i<words.length;i++){
			int ia=this.getWordEmbedding().getTermIndex(words[i]);
			double[] vecA = new double[this.getWordEmbedding().getDimensions()];
			if(ia!=-1){
				vecA=this.getWordEmbedding().getVectors()[ia];
			}else{
				if(i>0&&i<words.length-1){
					this.getWordEmbedding().generateVectorFromContext(words[i], words[i], words[i+1]);
					vecA=this.getWordEmbedding().getVectorForTerm(words[i]);
				}
			}
			double r = Math.random();
			if(r<p||i==0){
				n++;
				p=1.0/(1.0+(double)n);
				clusters.add(new DaiCluster(vecA));
				
			}else{
				double minDistance = 1000000.0;
				int minIndex = 0;
				for(int j=0; j<clusters.size(); j++){
					double distance = Utilities.cosineDistance(vecA, clusters.get(j).getAverage());
					if(distance < minDistance){
						minDistance = distance;
						minIndex = j;
					}
				}
				clusters.get(minIndex).addVector(vecA);
			}
		}
		
		return clusters;
		
	}

}
