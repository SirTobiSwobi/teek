package org.SirTobiSwobi.c3.teek.core;

import java.util.ArrayList;

import org.SirTobiSwobi.c3.teek.db.Categorization;
import org.SirTobiSwobi.c3.teek.db.Category;
import org.SirTobiSwobi.c3.teek.db.Configuration;
import org.SirTobiSwobi.c3.teek.db.MlrMetadata;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.SearchDirection;
import org.SirTobiSwobi.c3.teek.db.WordEmbedding;

public class DaiThread extends CategorizationThread {

	public DaiThread(ReferenceHub refHub, long docId) {
		super(refHub, docId);
		// TODO Auto-generated constructor stub
	}

	public void run(){
		performCategorization();
	}

	protected void performCategorization(){
		Configuration config = refHub.getActiveModel().getConfiguration();
		boolean includeImplicits = config.isIncludeImplicits();
		double assignmentThreshold = config.getAssignmentThreshold(); 
		WordEmbedding we = refHub.getActiveWordEmbedding();
		MlrMetadata weMeta = refHub.getWordEmbeddingManager().getByAddress(config.getWordEmbeddingId());
		DaiClusterer dc = new DaiClusterer(we);
		BamFeatureExtractor fe = new BamFeatureExtractor(we);
		
		Category[] categories = refHub.getCategoryManager().getCategoryArray();
		String docString = document.getLabel()+" "+document.getContent();
		ArrayList<DaiCluster> clusters = dc.getDaiClusters(docString);
		for(int j=0; j<clusters.size(); j++){
			for(int k=0;k<categories.length; k++){
				Category cat = categories[k];
				String catString = cat.getLabel()+" "+cat.getDescription();
				double[] catVec = fe.getVector(catString);
				double distance = Utilities.cosineDistance(catVec, clusters.get(j).getAverage());
				double probability = 1.0 -(-distance+1)/2;
				if(probability>assignmentThreshold){
					if(refHub.getCategorizationManager().containsCategorizationOf(document.getId(), cat.getId())){
						Categorization czn = refHub.getCategorizationManager().getCategorizationWithDocAndCat(document.getId(), cat.getId());
						if(czn.getProbability()<probability){
							czn.setProbability(probability);
						}
						refHub.getCategorizationManager().setCategorization(czn);
					}else{
						String explanation = "The document is considered to belong to category \""+cat.getLabel()+"\", because the words making up the document were "
								+ "grouped in "+clusters.size()+" clusters of similar words. One of these clusters was similar enough to this category, that it was assigned to it."
								+ "Similarity is measured by leveraging information learned from "+weMeta.getModelName()+" available at /wordembeddings/"+config.getWordEmbeddingId();
						refHub.getCategorizationManager().addCategorizationWithoutId(document.getId(), cat.getId(), probability, explanation);
					}
					if(includeImplicits){
						long[] implicitCategorizations=refHub.getTargetFunctionManager().findAllImplicitCatIds(cat.getId(), SearchDirection.Ascending);
						if(implicitCategorizations!=null){
							for(int l=0; l<implicitCategorizations.length; l++){
								if(!refHub.getCategorizationManager().containsCategorizationOf(document.getId(), implicitCategorizations[l])){
									String explanation = "The document is considered to belong to category \""+refHub.getCategoryManager().getByAddress(implicitCategorizations[l]).getLabel()+"\", ";
									explanation +=" because the category relationships imply that it belongs to this category.";
									refHub.getCategorizationManager().addCategorizationWithoutId(document.getId(), implicitCategorizations[l], probability, explanation);
								}else{
									Categorization czn = refHub.getCategorizationManager().getCategorizationWithDocAndCat(document.getId(), cat.getId());
									if(czn.getProbability()<probability){
										czn.setProbability(probability);
									}
								}
							}
						}
					}
					
				}
				
			}
		}
	
		
	}
}
