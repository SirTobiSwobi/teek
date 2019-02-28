package org.SirTobiSwobi.c3.teek.core;

import java.io.IOException;
import java.util.ArrayList;

import org.SirTobiSwobi.c3.teek.db.Assignment;
import org.SirTobiSwobi.c3.teek.db.Categorization;
import org.SirTobiSwobi.c3.teek.db.CategorizationManager;
import org.SirTobiSwobi.c3.teek.db.Category;
import org.SirTobiSwobi.c3.teek.db.Configuration;
import org.SirTobiSwobi.c3.teek.db.Document;
import org.SirTobiSwobi.c3.teek.db.Evaluation;
import org.SirTobiSwobi.c3.teek.db.Model;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.SearchDirection;
import org.SirTobiSwobi.c3.teek.db.TrainingSession;
import org.SirTobiSwobi.c3.teek.db.WordEmbedding;

public class NtfcFold extends Fold {

	public NtfcFold(ReferenceHub refHub, long[] trainingIds, long[] evaluationIds, int foldId, long modelId,
			TrainingSession trainingSession, Trainer trainer, long configId) {
		super(refHub, trainingIds, evaluationIds, foldId, modelId, trainingSession, trainer, configId);
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
		Model model=refHub.getModelManager().getModelByAddress(modelId);
		Configuration config = refHub.getConfigurationManager().getByAddress(configId);
		boolean includeImplicits = config.isIncludeImplicits();
		double assignmentThreshold = config.getAssignmentThreshold(); 
		int apd = config.getApd(); //Likely assignments per category //ToDo: refactor this into the configuration!
		WordEmbedding we;
		try {
			we = WordEmbedding.buildFromLocalFile(refHub.getWordEmbeddingManager().getByAddress(config.getWordEmbeddingId()).getLocalFilePath());
			// ToDo: Update configuration to ascertain appropriate distance measure
			DistanceMeasure dm=null;
			if(config.getDistanceMeasure().equals("BAM")){
				dm = new BamDistance(we);
			}else{
				dm = new WordMoverDistance(we);
			}
			
			for(int i=0; i<trainingIds.length; i++){
				
				String appendString = "Fold "+foldId+" learned from document "+trainingIds[i]+": ";/*
				Assignment[] explicitAssignments=refHub.getTargetFunctionManager().getDocumentAssignments(trainingIds[i]);
				for(int j=0;j<explicitAssignments.length;j++){
					appendString = appendString +" Explicitly belongs to category "+explicitAssignments[j].getCategoryId()+", ";
				}
				long[] implicitAssignments = refHub.getTargetFunctionManager().getImplicitCatIdsForDocument(trainingIds[i]);
				for(int j=0; j<implicitAssignments.length; j++){
					appendString = appendString +" Implicitly belongs to category "+implicitAssignments[j]+", ";
				}*/
				model.appendToTrainingLog(appendString);
				model.incrementCompletedSteps();
				/*
				try{
					Thread.sleep(5000);
				}catch(InterruptedException e){
					//This is just use to simulate the actual training progress.
				}
				*/
				
			}
			CategorizationManager evalCznMan = new CategorizationManager();
			for(int i=0; i<evaluationIds.length; i++){
				String appendString = "Fold "+foldId+" was evaluated using document "+evaluationIds[i]+" which: ";
				Assignment[] explicitAssignments=refHub.getTargetFunctionManager().getDocumentAssignments(evaluationIds[i]);
				for(int j=0;j<explicitAssignments.length;j++){
					appendString = appendString +" Explicitly belongs to category "+explicitAssignments[j].getCategoryId()+", ";
				}
				long[] implicitAssignments = refHub.getTargetFunctionManager().getImplicitCatIdsForDocument(evaluationIds[i]);
				if(implicitAssignments!=null){
					for(int j=0; j<implicitAssignments.length; j++){
						appendString = appendString +" Implicitly belongs to category "+implicitAssignments[j]+", ";
					}
				}
				
				/*
				 * Here, the actual assignment should take place. This code can be replaced with it.
				 * Automated assignment: Vector of probabilities of one document belonging to a certain category. 
				 * (Document - Category - Probability)
				 */
				
				/*
				*Random Assignment. This just randomly assigns documents to categories for debug and testing purposes.
				Category[] categories = refHub.getCategoryManager().getCategoryArray();
				int randId = (int)(Math.random()*(double)categories.length); //something more sophisticated should take place here ;-)
				double randProb = Math.random();
				evalCznMan.addCategorizationWithoutId(evaluationIds[i], categories[randId].getId(), randProb);
				appendString+="Randomly assigned document "+evaluationIds[i]+" to category "+categories[randId].getId()+
						" with probability "+randProb+". ";
				*/
				/*
				 * Fixed assignment to develop and debug evaluation.
				 */
				Category[] categories = refHub.getCategoryManager().getCategoryArray();
				double[] minDistance = new double[apd];
				int[] minIndex = new int[apd];
				ArrayList<Integer> usedIndeces = new ArrayList<Integer>();
				double[] maxDistance = new double[apd];
				for(int n=0;n<apd;n++){
					minDistance[n]= 1000000.0;
					minIndex[n]=-1;
					maxDistance[n]=0;
					for(int j=0;j<categories.length; j++){
						Document doc = refHub.getDocumentManager().getByAddress(evaluationIds[i]);
						Category cat = categories[j];
						String docString = doc.getLabel()+" "+doc.getContent();
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
						evalCznMan.addCategorizationWithoutId(evaluationIds[i], categories[minIndex[n]].getId(), probability, "");
						appendString+="Assigned document "+evaluationIds[i]+" to category "+categories[minIndex[n]].getId()+" with probability "+probability+". <br />";
						usedIndeces.add(minIndex[n]);
					}
				}
				
				
				
				
				model.appendToTrainingLog(appendString);
				model.incrementCompletedSteps();
				
				if(includeImplicits){
					appendString="Fold "+foldId+" performing implicit categorizations. ";
					Categorization[] czns = evalCznMan.getCategorizationArray();
					for(int j=0; j<czns.length; j++){			
						long[] implicitCategorizations=refHub.getTargetFunctionManager().findAllImplicitCatIds(czns[j].getCategoryId(), SearchDirection.Ascending);
						appendString = appendString +"Performing "+implicitCategorizations.length+" implicit categorizations";
						if(implicitCategorizations!=null){
							for(int k=0; k<implicitCategorizations.length; k++){
								appendString = appendString +("Explicit: "+czns[j].getCategoryId()+" ");
								appendString = appendString +("Implcit: "+implicitCategorizations[k]+" ");
								if(implicitCategorizations[k]!=czns[j].getCategoryId()){
									appendString = appendString +("Implicit addition: Document: "+evaluationIds[i]+" Category: "+implicitCategorizations[k]+" Probability: "+czns[j].getProbability());
									evalCznMan.addCategorizationWithoutId(evaluationIds[i], implicitCategorizations[k], czns[j].getProbability(), "");
								}
							}
						}
					}
					model.appendToTrainingLog(appendString);
				}
				/*
				try{
					Thread.sleep(5000);
				}catch(InterruptedException e){
					//This is just use to simulate the actual training progress.
				}
				*/
				
			}
			
			String appendString = "Fold "+foldId+" summarized categorizations: ";
			for(int i=0; i<evaluationIds.length; i++){
				Categorization[] czn = evalCznMan.getDocumentCategorizations(evaluationIds[i]);
				for(int j=0; j<czn.length;j++){
					appendString = appendString +"Document "+czn[j].getDocumentId()+" was assigned to category "+czn[j].getCategoryId()+
							" with probability "+czn[j].getProbability();
				}
			}
			/*
			for(int i=0; i<evaluationIds.length; i++){
				Categorization[] czn=evalCznMan.getDocumentCategorizations(evaluationIds[i]);
				for(int j=0; j<czn.length;j++){
					appendString = appendString +"Document "+czn[j].getDocumentId()+" was assigned to category "+czn[j].getCategoryId()+
							" with probability "+czn[j].getProbability();
				}
				
			}
			*/
			model.appendToTrainingLog(appendString);
			String evalDescription = "Fold "+foldId;
			
			
			/*
			 * Not all assignments can be taken into account per fold. Otherwise false negatives sky-rocket. If this fold doesn't know a document,
			 * it cannot be evaluated as if it knows it. 
			 */
			Assignment[] relevantAssignments=null;
			for(int i=0; i<evaluationIds.length; i++){
				relevantAssignments = Utilities.arrayUnionWithoutDuplicates(relevantAssignments, refHub.getTargetFunctionManager().getDocumentAssignments(evaluationIds[i])); 
			}
			Evaluation eval = new Evaluation(	relevantAssignments, 
												evalCznMan.getCategorizationArray(), 
												refHub.getCategoryManager().getCategoryArray(), 
												refHub.getCategoryManager().getRelationshipArray(), 
												refHub.getDocumentManager().getDocumentArray(),  
												evalDescription,
												includeImplicits, 
												assignmentThreshold,
												trainingSession,
												foldId);
			trainer.selectBestEvaluation();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			for(int i=0; i<trainingIds.length; i++){
				model.incrementCompletedSteps();
			}
			for(int i=0; i<evaluationIds.length; i++){
				model.incrementCompletedSteps();
			}
			model.appendToTrainingLog("Fold "+foldId+" failed: "+e.getMessage()+" <br />");
			e.printStackTrace();
		}
		
		
		
		
	}

}
