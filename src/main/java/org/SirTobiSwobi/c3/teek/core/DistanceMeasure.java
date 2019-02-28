package org.SirTobiSwobi.c3.teek.core;

import org.SirTobiSwobi.c3.teek.db.WordEmbedding;

public abstract class DistanceMeasure {
	private WordEmbedding wordEmbedding;

	public DistanceMeasure(WordEmbedding wordEmbedding) {
		super();
		this.wordEmbedding = wordEmbedding;
	}

	public WordEmbedding getWordEmbedding() {
		return wordEmbedding;
	}

	public void setWordEmbedding(WordEmbedding wordEmbedding) {
		this.wordEmbedding = wordEmbedding;
	}
	
	public abstract double getDistance(String a, String b);
}
