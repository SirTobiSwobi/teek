package org.SirTobiSwobi.c3.teek.db;

/**
 * This class is meant to be extended in each classifier trainer implementation
 * @author Tobias Eljasik-Swoboda
 *
 */
public class Configuration {
	
	private long id;
	private String algorithm;
	private long wordEmbeddingId;

	
	public Configuration(long id) {
		super();
		this.id = id;
	}
	
	public Configuration(long id, int folds){
		super();
		this.id = id;
	}
	
	public Configuration(long id, String algorithm, long wordEmbeddingId) {
		super();
		this.id = id;
		this.algorithm = algorithm;
		this.wordEmbeddingId = wordEmbeddingId;
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

}
