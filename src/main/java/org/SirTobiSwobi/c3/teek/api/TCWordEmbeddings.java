package org.SirTobiSwobi.c3.teek.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCWordEmbeddings {
	private TCWordEmbeddingMetadata[] wordEmbeddings;
	
	public TCWordEmbeddings(){
		//Jackson deserialization
	}

	public TCWordEmbeddings(TCWordEmbeddingMetadata[] wordEmbeddings) {
		super();
		this.wordEmbeddings = wordEmbeddings;
	}

	@JsonProperty
	public TCWordEmbeddingMetadata[] getWordEmbeddings() {
		return wordEmbeddings;
	}
	
	
}
