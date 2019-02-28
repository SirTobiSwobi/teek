package org.SirTobiSwobi.c3.teek.db;

import java.io.File;
import java.util.ArrayList;

public class WordEmbeddingManager {
	private AVLTree<WordEmbeddingMetadata> wordEmbeddings;
	private ReferenceHub refHub;
	
	public WordEmbeddingManager(){
		this.wordEmbeddings = new AVLTree<WordEmbeddingMetadata>();
	}
	
	public long getSize(){
		/*
		ArrayList<File> allWordEmbeddings = refHub.getFileSystemManager().getAllFiles("/opt/wordembeddings/");
		return (long)allWordEmbeddings.size();
		*/
		return wordEmbeddings.getSize();
	}
	
	public WordEmbeddingMetadata getByAddress(long address){	
		return wordEmbeddings.getContent(address);
	}
	
	public void setWordEmbedding(WordEmbeddingMetadata wordEmbedding){
		if(wordEmbedding.getId()==-1){
			addWordEmbeddingWithoutId(wordEmbedding);
		}else{
			wordEmbeddings.setContent(wordEmbedding, wordEmbedding.getId());
		}
		
	}
	
	
	public synchronized long addWordEmbeddingWithoutId(WordEmbeddingMetadata wordEmbedding){
		long id = wordEmbeddings.getMaxId()+1;
		wordEmbedding.setId(id);
		wordEmbeddings.setContent(wordEmbedding,id);
		return id;
	}
	
	public synchronized void deleteWordEmbedding(long id){
		if(this.wordEmbeddings.getSize()>0){ //Otherwise null pointer exceptions can occur if there are categories but no relationships.
			wordEmbeddings.deleteNode(id);
		}				
	}
	
	public WordEmbeddingMetadata[] getWordEmbeddingArray(){
		ArrayList<WordEmbeddingMetadata> allContent = wordEmbeddings.toArrayList();
		WordEmbeddingMetadata[] wordEmbeddingArray = allContent.toArray(new WordEmbeddingMetadata[0]);
		return wordEmbeddingArray;
	}
	
	public boolean containsWordEmbedding(long id){
		return wordEmbeddings.containsId(id);
	}
	
	public String getHash(){
		byte[] contentHash = wordEmbeddings.getContentHash();
		String result="";
		for(int i=0; i<contentHash.length;i++){
			result = result + Integer.toHexString(contentHash[i] & 255);
		}
		return result;
	}

	public ReferenceHub getRefHub() {
		return refHub;
	}

	public void setRefHub(ReferenceHub refHub) {
		this.refHub = refHub;
	}
	
	
}
