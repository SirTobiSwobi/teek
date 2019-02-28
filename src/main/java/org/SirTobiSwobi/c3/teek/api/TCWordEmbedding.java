package org.SirTobiSwobi.c3.teek.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCWordEmbedding {
	private long id;
	private int size;
	private int dimensions;
	private String[] terms;
	private double[][] vectors;
	
	public TCWordEmbedding(){
		//Jackson deserialization
	}

	public TCWordEmbedding(int size, int dimensions, String[] terms, double[][] vectors) {
		super();
		this.id=-1;
		this.size = size;
		this.dimensions = dimensions;
		this.terms = terms;
		this.vectors = vectors;
	}
	
	

	public TCWordEmbedding(long id, int size, int dimensions, String[] terms, double[][] vectors) {
		super();
		this.id = id;
		this.size = size;
		this.dimensions = dimensions;
		this.terms = terms;
		this.vectors = vectors;
	}
	
	
	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public int getSize() {
		return size;
	}

	@JsonProperty
	public int getDimensions() {
		return dimensions;
	}

	@JsonProperty
	public String[] getTerms() {
		return terms;
	}

	@JsonProperty
	public double[][] getVectors() {
		return vectors;
	}

	
	
}
