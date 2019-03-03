package org.SirTobiSwobi.c3.teek.core;

public class RegressionVectorStreamPredictor extends VectorStreamPredictor {

	@Override
	public double[] getNextVector(double[][] vectorstream) {
		double[] averageY=new double[vectorstream[0].length];
		for(int i=0;i<vectorstream.length; i++){
			for(int k=0; k<vectorstream[i].length; k++){
				averageY[k]=averageY[k]+vectorstream[i][k];
			}
		}
		for(int k=0; k<averageY.length; k++){
			averageY[k]=averageY[k]/(double)vectorstream.length;
		}
		double X=(double)vectorstream.length-1; 
		double averageX=(double)(X*X+X)/2.0; //Gaussian sum formula
		averageX=averageX/(double)vectorstream.length;
		double[] b= new double[vectorstream[0].length];
		double[] a= new double[vectorstream[0].length];
		double[] numeratorB= new double[vectorstream[0].length];
		double denominatorB=0.0;
		for(int x=0; x<vectorstream.length; x++){
			for(int i=0; i<numeratorB.length; i++){
				numeratorB[i]=numeratorB[i]+((double)x-averageX)*(vectorstream[x][i]-averageY[i]);	
			}
			denominatorB=denominatorB+((double)x-averageX)*((double)x-averageX);
		}
		for(int i=0; i<b.length; i++){
			b[i]=numeratorB[i]/denominatorB;
			a[i]=averageY[i]-b[i]*averageX;
		}
		double[] nextVector = new double[vectorstream[0].length];
		for(int i=0; i<nextVector.length; i++){
			nextVector[i]=a[i]+b[i]*(double)vectorstream.length; //x_0=0; x_1=1; -> length=2 -> x_2=2.
		}
		
		return nextVector;
	}
	
}
