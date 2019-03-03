package org.SirTobiSwobi.c3.teek.core;

import java.io.IOException;

import org.SirTobiSwobi.c3.teek.api.TCCategory;
import org.SirTobiSwobi.c3.teek.api.TCRelationship;
import org.SirTobiSwobi.c3.teek.api.TCSuggestions;
import org.SirTobiSwobi.c3.teek.db.Category;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.SearchDirection;
import org.SirTobiSwobi.c3.teek.db.WordEmbedding;

public class Suggestor {
	
	private ReferenceHub refHub;
	
	public Suggestor(ReferenceHub refHub) {
		this.refHub = refHub;
	}

	public TCSuggestions generateOutput(long cat, int m) throws IOException{
		long[] path = refHub.getCategoryManager().findAllImplicitCatIds(cat, SearchDirection.Ascending);
		VectorStreamPredictor predictor;
		if(refHub.getActiveModel().getConfiguration().getAlgorithm().equals("avsp")){
			predictor = new ArithmeticVectorStreamPredictor();
		}else{
			predictor = new RegressionVectorStreamPredictor();
		}	
		WordEmbedding we = WordEmbedding.buildFromLocalFile(refHub.getActiveModel().getWordEmbedding());
		BamFeatureExtractor fe = new BamFeatureExtractor(we);
		double[][] vectorstream = new double[path.length][we.getDimensions()];
		for(int i=0;i<path.length;i++){
			Category cate = refHub.getCategoryManager().getByAddress(path[i]);
			vectorstream[path.length-1-i]=fe.getVector(cate.getLabel()+" "+cate.getDescription());
		}
		double[] nextVector = predictor.getNextVector(vectorstream);
		String[] closestWords = we.getClosestWords(nextVector, m);
		TCCategory[] categories = new TCCategory[m];
		TCRelationship[] relationships = new TCRelationship[m];
		long maxCat = refHub.getCategoryManager().getMaxCatId();
		long maxRel = refHub.getCategoryManager().getMaxRelId();
		for(int i=0;i<m;i++){			
			categories[i]=new TCCategory(maxCat+i, closestWords[i], "");
			relationships[i]=new TCRelationship(maxRel+i,cat,maxCat+i, "Sub");
		}
		
		TCSuggestions output = new TCSuggestions(categories, relationships);
		
		return output;
		
	}

}
