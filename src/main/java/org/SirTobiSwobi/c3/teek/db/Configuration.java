package org.SirTobiSwobi.c3.teek.db;

/**
 * This class is meant to be extended in each classifier trainer implementation
 * @author Tobias Eljasik-Swoboda
 *
 */
public class Configuration {
	
	private long id;
	private int folds; // of n-fold cross-validation. Default = 2;
	private boolean includeImplicits;
	private double assignmentThreshold;
	private SelectionPolicy selectionPolicy;
	private String algorithm;
	private String distanceMeasure;
	private long wordEmbeddingId;
	private int apd; //assignments per category

	
	public Configuration(long id) {
		super();
		this.id = id;
		this.folds=2;
		this.includeImplicits=true;
		this.assignmentThreshold=0.5;
		this.selectionPolicy=SelectionPolicy.MicroaverageF1;
	}
	
	public Configuration(long id, int folds){
		super();
		this.id = id;
		this.folds = folds;
	}
	
	

	public Configuration(long id, int folds, boolean includeImplicits, double assignmentThreshold,
			SelectionPolicy selectionPolicy, String algorithm, String distanceMeasure, long wordEmbeddingId, int apd ) {
		super();
		this.id = id;
		this.folds = folds;
		this.includeImplicits = includeImplicits;
		this.assignmentThreshold = assignmentThreshold;
		this.selectionPolicy = selectionPolicy;
		this.algorithm = algorithm;
		this.distanceMeasure = distanceMeasure;
		this.wordEmbeddingId = wordEmbeddingId;
		this.apd = apd;
	}

	public Configuration() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public int getFolds() {
		return folds;
	}

	public void setFolds(int folds) {
		this.folds = folds;
	}

	public String toString(){
		return ""+id+" "+folds;
	}

	public boolean isIncludeImplicits() {
		return includeImplicits;
	}

	public void setIncludeImplicits(boolean includeImplicits) {
		this.includeImplicits = includeImplicits;
	}

	public double getAssignmentThreshold() {
		return assignmentThreshold;
	}

	public void setAssignmentThreshold(double assignmentThreshold) {
		this.assignmentThreshold = assignmentThreshold;
	}

	public SelectionPolicy getSelectionPolicy() {
		return selectionPolicy;
	}

	public void setSelectionPolicy(SelectionPolicy selectionPolicy) {
		this.selectionPolicy = selectionPolicy;
	}

	public long getWordEmbeddingId() {
		return wordEmbeddingId;
	}

	public void setWordEmbeddingId(long wordEmbeddingId) {
		this.wordEmbeddingId = wordEmbeddingId;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public int getApd() {
		return apd;
	}

	public void setApd(int apd) {
		this.apd = apd;
	}

	public String getDistanceMeasure() {
		return distanceMeasure;
	}

	public void setDistanceMeasure(String distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}
	
	

	
	

}
