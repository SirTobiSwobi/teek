package org.SirTobiSwobi.c3.teek.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCConfiguration {

	private long id;
	private String selectionPolicy;
	private String algorithm; //AVSP or RVSP
	private long wordEmbeddingId;

	public TCConfiguration(long id, String algorithm, long wordEmbeddingId) {
		super();
		this.id = id;
		this.algorithm = algorithm;
		this.wordEmbeddingId = wordEmbeddingId;
	}

	public TCConfiguration() {
		//Jackson deserialization
	}

	@JsonProperty
	public long getId() {
		return id;
	}
	
	@JsonProperty
	public long getWordEmbeddingId() {
		return wordEmbeddingId;
	}
	
	@JsonProperty
	public String getAlgorithm() {
		return algorithm;
	}
	
}
