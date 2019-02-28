package org.SirTobiSwobi.c3.teek.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCModel {
	private long id;
	private long configurationId;
	private double progress;
	private String trainingLog;
	private String wordEmbedding;
	private TCConfiguration configuration;
	
	public TCModel(){
		//Jackson deserialization
	}

	public TCModel(long id, long configurationId, double progress, String trainingLog, TCConfiguration configuration, String wordEmbedding) {
		super();
		this.id = id;
		this.configurationId = configurationId;
		this.progress = progress;
		this.trainingLog = trainingLog;
		this.configuration = configuration;
		this.wordEmbedding = wordEmbedding;
	}
	
	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public long getConfigurationId() {
		return configurationId;
	}

	@JsonProperty
	public double getProgress() {
		return progress;
	}

	@JsonProperty
	public String getTrainingLog() {
		return trainingLog;
	}

	@JsonProperty
	public TCConfiguration getConfiguration() {
		return configuration;
	}

	@JsonProperty
	public String getWordEmbedding() {
		return wordEmbedding;
	}

}
