package org.SirTobiSwobi.c3.teek.core;

import org.SirTobiSwobi.c3.teek.db.WordEmbedding;

public class WordMoverDistance extends DistanceMeasure {

	
	public WordMoverDistance(WordEmbedding wordEmbedding) {
		super(wordEmbedding);
	}

	@Override
	public double getDistance(String a, String b) {
		a = Utilities.sanitizeText(a);
		b = Utilities.sanitizeText(b);
		double wmd=0.0;
		String[] A = a.split(" ");
		String[] B = b.split(" ");
		//always check distance from shorter text to longer text. Swich texts if necessary.
		if(B.length<A.length){
			String[] C=A;
			A=B;
			B=C;
		}
		for(int aw=0; aw<A.length; aw++){
			double wordDistance=10000000.0;
			int ia=this.getWordEmbedding().getTermIndex(A[aw]);
			double[] vecA = new double[this.getWordEmbedding().getDimensions()];
			if(ia!=-1){
				vecA=this.getWordEmbedding().getVectors()[ia];
			}else{
				if(aw>0&&aw<A.length-1){
					this.getWordEmbedding().generateVectorFromContext(A[aw], A[aw-1], A[aw+1]);
					vecA=this.getWordEmbedding().getVectorForTerm(A[aw]);
				}
			}
			for(int bw=0; bw<B.length; bw++){
				int ib=this.getWordEmbedding().getTermIndex(B[bw]);
				double[] vecB = new double[this.getWordEmbedding().getDimensions()];
				if(ib!=-1){
					vecB=this.getWordEmbedding().getVectors()[ib];
				}else{
					if(bw>0&&bw<A.length-1){
						this.getWordEmbedding().generateVectorFromContext(A[bw], A[bw-1], A[bw+1]);
						vecB=this.getWordEmbedding().getVectorForTerm(A[bw]);
					}
				}
				double distance = Utilities.cosineDistance(vecA, vecB);
				if(A[aw].equals(B[bw])){
					distance = 0.0;
				}
				if(distance < wordDistance){
					wordDistance = distance; 
				}		
			}
			wmd=wmd+wordDistance;
			
		}
		
		return wmd;
	}

}
