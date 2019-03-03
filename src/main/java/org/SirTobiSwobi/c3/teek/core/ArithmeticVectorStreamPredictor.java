package org.SirTobiSwobi.c3.teek.core;

public class ArithmeticVectorStreamPredictor extends VectorStreamPredictor {

	@Override
	public double[] getNextVector(double[][] vectorstream) {
		
		double[][] differences=new double[vectorstream.length-1][vectorstream[0].length];
		for(int i=1;i<vectorstream.length; i++){
			for(int k=0; k<vectorstream[i].length; k++){
				differences[i-1][k]=vectorstream[i][k]-vectorstream[i-1][k];
			}
		}
		double[] avgDifference=new double[vectorstream[0].length];
		for(int i=0;i<differences.length;i++){
			for(int k=0; k<differences[0].length; k++){
				avgDifference[k]=avgDifference[k]+differences[i][k];
			}
		}
		for(int k=0; k<avgDifference.length;k++){
			avgDifference[k]=avgDifference[k]/(double)differences.length;
		}
		double[] nextVector = new double[vectorstream[0].length];
		for(int k=0; k<nextVector.length; k++){
			nextVector[k]=vectorstream[vectorstream.length-1][k]+avgDifference[k];
		}
		return nextVector;
	}
	
}
