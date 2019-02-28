package org.SirTobiSwobi.c3.teek.core;

import java.util.ArrayList;

import org.SirTobiSwobi.c3.teek.db.Category;
import org.SirTobiSwobi.c3.teek.db.Configuration;
import org.SirTobiSwobi.c3.teek.db.Document;
import org.SirTobiSwobi.c3.teek.db.MlrMetadata;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.SearchDirection;
import org.SirTobiSwobi.c3.teek.db.WordEmbedding;

public class NtfcThread extends CategorizationThread{

	public NtfcThread(ReferenceHub refHub, long docId) {
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
		int apd = config.getApd(); //Likely assignments per category //ToDo: refactor this into the configuration!
		WordEmbedding we = refHub.getActiveWordEmbedding();
		MlrMetadata weMeta = refHub.getWordEmbeddingManager().getByAddress(config.getWordEmbeddingId());
		DistanceMeasure dm=null;
		if(config.getDistanceMeasure().equals("BAM")){
			dm = new BamDistance(we);
		}else{
			dm = new WordMoverDistance(we);
		}
		Category[] categories = refHub.getCategoryManager().getCategoryArray();
		double[] minDistance = new double[apd];
		int[] minIndex = new int[apd];
		ArrayList<Integer> usedIndeces = new ArrayList<Integer>();
		double[] maxDistance = new double[apd];
		for(int n=0;n<apd;n++){
			minDistance[n]= 1000000.0;
			minIndex[n]=-1;
			maxDistance[n]=0;
			for(int j=0;j<categories.length; j++){;
				Category cat = categories[j];
				String docString = document.getLabel()+" "+document.getContent();
				String catString = cat.getLabel()+" "+cat.getDescription();
				double distance = dm.getDistance(docString, catString);
				if(distance<minDistance[n]&&!usedIndeces.contains(j)){
					minDistance[n] = distance;
					minIndex[n] = j;
				}
				if(distance>maxDistance[n]){
					maxDistance[n] = distance;
				}
			}
			double probability = 1.0-(minDistance[n]/maxDistance[n]);
			if(minIndex[n]!=-1&&probability>assignmentThreshold){
				if(!refHub.getCategorizationManager().containsCategorizationOf(document.getId(), categories[minIndex[n]].getId())){
					String explanation = "The document is considered to belong to category \""+categories[minIndex[n]].getLabel()+"\", because the words making up the document were "
							+ "semantically similar to to the category when using the "+config.getDistanceMeasure()+" distance measure. "
							+ "Similarity is measured by leveraging information learned from "+weMeta.getModelName()+" available at /wordembeddings/"+config.getWordEmbeddingId();
					refHub.getCategorizationManager().addCategorizationWithoutId(document.getId(), categories[minIndex[n]].getId(), probability, explanation);
					usedIndeces.add(minIndex[n]);
				}else if(refHub.getCategorizationManager().getCategorizationWithDocAndCat(document.getId(), categories[minIndex[n]].getId()).getProbability()<probability){
					refHub.getCategorizationManager().getCategorizationWithDocAndCat(document.getId(), categories[minIndex[n]].getId()).setProbability(probability);
				}
				if(includeImplicits){
					long[] implicitCategorizations=refHub.getTargetFunctionManager().findAllImplicitCatIds(categories[minIndex[n]].getId(), SearchDirection.Ascending);
					if(implicitCategorizations!=null){
						for(int k=0; k<implicitCategorizations.length; k++){
							String explanation = "The document is considered to belong to category \""+refHub.getCategoryManager().getByAddress(implicitCategorizations[k]).getLabel()+"\", ";
							explanation +=" because the category relationships imply that it belongs to this category.";
							if(!refHub.getCategorizationManager().containsCategorizationOf(document.getId(), implicitCategorizations[k])){
								refHub.getCategorizationManager().addCategorizationWithoutId(document.getId(), implicitCategorizations[k], probability, explanation);
							}
						}
					}
				}
			}
			
		}
		
	}
}
