package org.SirTobiSwobi.c3.teek.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;

public class WordEmbeddingAnalyzer extends Thread {

	private long wordEmbeddingId;
	private String filePath;
	private ReferenceHub refHub;
	public WordEmbeddingAnalyzer(long wordEmbeddingId, String filePath, ReferenceHub refHub) {
		super();
		this.wordEmbeddingId = wordEmbeddingId;
		this.filePath = filePath;
		this.refHub = refHub;
	}
	
	public void run() {
		String[] splitResult;
		boolean countLines = false;
		int size=0;
		int dimensions=0;
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filePath));
	
			String line = br.readLine(); //gets the first line of the file
			splitResult=line.split(" ");
			if(Array.getLength(splitResult)!=2){ // is in GloVe format
				countLines = true;
			}
			br.close();	
			
			if(countLines){
				size=0;
				dimensions=0;
				
				br = new BufferedReader(new FileReader(filePath));
				while((line = br.readLine())!=null){
					splitResult=line.split(" ");
					size++;
					dimensions=Array.getLength(splitResult)-1; //first is the word
				}
			}else{
				br = new BufferedReader(new FileReader(filePath));
				while((line = br.readLine())!=null){
					splitResult=line.split(" ");
					if(Array.getLength(splitResult)==2){ //only happens in the first line of a word2vec file
						size=Integer.parseInt(splitResult[0]);
						dimensions=Integer.parseInt(splitResult[1]);
					}
				}
				
			}
			refHub.getWordEmbeddingManager().getByAddress(wordEmbeddingId).setDimensions(dimensions);
			refHub.getWordEmbeddingManager().getByAddress(wordEmbeddingId).setTerms(size);
			
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
