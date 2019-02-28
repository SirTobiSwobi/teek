package org.SirTobiSwobi.c3.teek.core;

import java.io.IOException;

import org.SirTobiSwobi.c3.teek.db.Document;
import org.SirTobiSwobi.c3.teek.db.Model;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.WordEmbedding;

public class BamFeatureExtractor {
	private ReferenceHub refHub;
	private String wordEmbeddingPath;
	private WordEmbedding wordEmbedding;
	private Model model; // for debug log purposes
	public BamFeatureExtractor(ReferenceHub refHub, String wordEmbeddingPath) {
		super();
		this.refHub = refHub;
		this.wordEmbeddingPath = wordEmbeddingPath;
		try {
			this.wordEmbedding = WordEmbedding.buildFromLocalFile(wordEmbeddingPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BamFeatureExtractor(WordEmbedding wordEmbedding){
		this.wordEmbedding=wordEmbedding;
	}
	
	public double[] getVector(long docId){
		String append="";
		if(model!=null){
			append="Vector for "+docId+" ";
		}
		double[] average = new double[wordEmbedding.getDimensions()];
		Document document = refHub.getDocumentManager().getByAddress(docId);
		String text = document.getLabel()+" "+document.getContent();
		text = text.replaceAll("<.*?>",  ""); //removes any xml tags
		text = text.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase();
		text = text.replaceAll("\\s"," ");
		text = text.replaceAll("\\s{2,}", " ").trim();
		text = text.toLowerCase();
		append+=text+" ";
		String words[] = text.split(" ");
		for(int i=0;i<words.length;i++){
			int termIndex = wordEmbedding.getTermIndex(words[i]);
			double[] vector = new double[average.length];
			if(termIndex!=-1){
				vector=wordEmbedding.getVectors()[termIndex];
			}else{
				if(i>0&&i<words.length-1){
					wordEmbedding.generateVectorFromContext(words[i], words[i-1], words[i+1]);
					vector=wordEmbedding.getVectorForTerm(words[i]);
				}
			}
			for(int j=0; j<average.length; j++){
				if(j==0&&model!=null){
					append+="average word: "+i+" = "+average[j]+" * "+i+" + "+vector[j]+" / "+(i+1);
				}
				average[j]=(average[j]*i+vector[j])/(i+1);
			}
		}
		if(model!=null){
			
			for(int j=0; j<average.length; j++){
				append+= average[j]+" ";
			}
			//model.appendToTrainingLog(append);
		}
		return average;
	}
	
	public double[] getVector(String text){
		double[] average = new double[wordEmbedding.getDimensions()];
		text = text.replaceAll("<.*?>",  ""); //removes any xml tags
		text = text.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase();
		text = text.replaceAll("\\s"," ");
		text = text.replaceAll("\\s{2,}", " ").trim();
		text = text.toLowerCase();
		String words[] = text.split(" ");
		for(int i=0;i<words.length;i++){
			int termIndex = wordEmbedding.getTermIndex(words[i]);
			double[] vector = new double[average.length];
			if(termIndex!=-1){
				vector=wordEmbedding.getVectors()[termIndex];
			}else{
				if(i>0&&i<words.length-1){
					wordEmbedding.generateVectorFromContext(words[i], words[i-1], words[i+1]);
					vector=wordEmbedding.getVectorForTerm(words[i]);
				}
			}
			for(int j=0; j<average.length; j++){
				average[j]=(average[j]*i+vector[j])/(i+1);
			}
		}
		return average;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	
	
	
}
