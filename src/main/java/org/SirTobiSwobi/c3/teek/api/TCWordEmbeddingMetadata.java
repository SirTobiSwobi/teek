package org.SirTobiSwobi.c3.teek.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCWordEmbeddingMetadata {
	private TCMlrMetadata metadata;
	private long id;
	private String algorithm;
	private int dimensions;
	private int terms;
	private String localFilePath;
	
	public TCWordEmbeddingMetadata(){
		//Jackson deserialization
	}
	
	public TCWordEmbeddingMetadata(TCMlrMetadata metadata, long id, String algorithm, int dimensions, int terms,
			String localFilePath) {
		super();
		this.metadata = metadata;
		this.id = id;
		this.algorithm = algorithm;
		this.dimensions = dimensions;
		this.terms = terms;
		this.localFilePath = localFilePath;
	}

	@JsonProperty
	public TCMlrMetadata getMetadata() {
		return metadata;
	}

	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public String getAlgorithm() {
		return algorithm;
	}

	@JsonProperty
	public int getDimensions() {
		return dimensions;
	}

	@JsonProperty
	public int getTerms() {
		return terms;
	}

	@JsonProperty
	public String getLocalFilePath() {
		return localFilePath;
	}

}
