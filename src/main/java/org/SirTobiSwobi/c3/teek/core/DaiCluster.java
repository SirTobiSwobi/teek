package org.SirTobiSwobi.c3.teek.core;

public class DaiCluster {
	private double[] average;
	private int terms;
	
	public DaiCluster(double[] vector){
		average = vector;
		terms=1;
	}
	
	public void addVector(double[] vector){
		assert(average.length==vector.length);
		for(int i=0; i<vector.length; i++){
			average[i]=average[i]*terms+vector[i]/terms+1;
		}
		terms++;
	}

	public double[] getAverage() {
		return average;
	}

}
