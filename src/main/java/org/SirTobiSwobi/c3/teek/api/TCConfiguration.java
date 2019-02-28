package org.SirTobiSwobi.c3.teek.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCConfiguration {

	private long id;
	private int folds;
	private boolean includeImplicits;
	private double assignmentThreshold;
	private String selectionPolicy;
	private String algorithm; //ntfc or Dai
	private String distanceMeasure; //WMD or BAM
	private long wordEmbeddingId;
	private int apd; //assignments per category

	public TCConfiguration(long id, int folds, boolean includeImplicits, double assignmentThreshold,
			String selectionPolicy, String algorithm, String distanceMeasure, long wordEmbeddingId, int apd ) {
		super();
		this.id = id;
		this.folds = folds;
		this.includeImplicits = includeImplicits;
		this.assignmentThreshold = assignmentThreshold;
		this.selectionPolicy = selectionPolicy;
		this.algorithm = algorithm;
		this.wordEmbeddingId = wordEmbeddingId;
		this.apd = apd;
		this.distanceMeasure = distanceMeasure;
	}



	public TCConfiguration() {
		//Jackson deserialization
	}

	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public int getFolds() {
		return folds;
	}

	@JsonProperty
	public boolean isIncludeImplicits() {
		return includeImplicits;
	}

	@JsonProperty
	public double getAssignmentThreshold() {
		return assignmentThreshold;
	}

	@JsonProperty
	public String getSelectionPolicy() {
		return selectionPolicy;
	}
	
	@JsonProperty
	public long getWordEmbeddingId() {
		return wordEmbeddingId;
	}
	
	@JsonProperty
	public String getAlgorithm() {
		return algorithm;
	}
	
	@JsonProperty
	public String getDistanceMeasure() {
		return distanceMeasure;
	}

	@JsonProperty
	public int getApd() {
		return apd;
	}

	
	
}
