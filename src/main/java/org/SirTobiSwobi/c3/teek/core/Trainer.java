package org.SirTobiSwobi.c3.teek.core;

import java.util.ArrayList;
import java.util.Arrays;

import org.SirTobiSwobi.c3.teek.db.Assignment;
import org.SirTobiSwobi.c3.teek.db.Category;
import org.SirTobiSwobi.c3.teek.db.Configuration;
import org.SirTobiSwobi.c3.teek.db.Document;
import org.SirTobiSwobi.c3.teek.db.Evaluation;
import org.SirTobiSwobi.c3.teek.db.Model;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.SelectionPolicy;
import org.SirTobiSwobi.c3.teek.db.TrainingSession;


public class Trainer {
	private ReferenceHub refHub;
	private int openEvaluations;
	private long trainingSessionId;
	private long modelId;
	private long configId;
	
	
	public Trainer(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	
	}
	
	private long[] computeTrainingIdsFromEvaluationIds(long[] allIds, long[] evaluationIds){
		long [] output = new long[allIds.length-evaluationIds.length];
		ArrayList<Long> allIdsList = new ArrayList<Long>();
		ArrayList<Long> evaluationIdsList = new ArrayList<Long>();
		for(int i=0;i<allIds.length;i++){
			allIdsList.add(allIds[i]);
		}
		for(int i=0;i<evaluationIds.length;i++){
			evaluationIdsList.add(evaluationIds[i]);
		}
		allIdsList.removeAll(evaluationIdsList);
		for(int i=0;i<output.length;i++){
			output[i]=allIdsList.get(i);
		}
		return output;
	}

	public synchronized void startTraining(long configId, long modelId){
		this.configId = configId;
		refHub.getModelManager().setTrainingInProgress(true);
		Configuration config = refHub.getConfigurationManager().getByAddress(configId);
		int folds = config.getFolds();
		this.openEvaluations=folds;
		this.modelId=modelId; // There is always only one active training session per microservice. 
		Assignment[] assignments = refHub.getTargetFunctionManager().getAssignmentArray();
		ArrayList<Long> relevantDocIds = new ArrayList<Long>();
		for(int i=0; i<assignments.length; i++){
			long id = assignments[i].getDocumentId();
			if(!relevantDocIds.contains(id)){
				relevantDocIds.add(id);
			}
		}
		
		
		/*
		Document[] allDocs=refHub.getDocumentManager().getDocumentArray();
		int overallSteps = allDocs.length*folds;
		refHub.getModelManager().getModelByAddress(modelId).setSteps(overallSteps);
		*/
		trainingSessionId = refHub.getEvaluationManager().addTrainingSessionWithoutId(modelId, "");
		TrainingSession trainingSession = refHub.getEvaluationManager().getTrainingSessionByAddress(trainingSessionId);
		/*
		long[] allIds = new long[allDocs.length];
		for(int i=0;i<allIds.length;i++){
			allIds[i]=allDocs[i].getId();
		}
		*/
		long[] allIds = new long[relevantDocIds.size()];
		int overallSteps = allIds.length*folds;
		refHub.getModelManager().getModelByAddress(modelId).setSteps(overallSteps);
		for(int i=0; i<allIds.length;i++){
			allIds[i]=relevantDocIds.get(i);
		}
		if(folds==1){
			overallSteps = allIds.length*2; //because the same documents are used for training and for evaluation purposes. 
			refHub.getModelManager().getModelByAddress(modelId).setSteps(overallSteps);
			(new NtfcFold(refHub, allIds, allIds, 0, modelId, trainingSession, this, configId)).start(); //only changed what fold is started.
		}else{
			for(int i=0; i<folds;i++){
				int start=(allIds.length/folds)*i;
				int end=((allIds.length/folds)*(i+1));
				if(i==folds-1){
					end=allIds.length;
				}
				
				long[] evaluationIds = Arrays.copyOfRange(allIds, start, end);
				long[] trainingIds = computeTrainingIdsFromEvaluationIds(allIds,evaluationIds);
				
				(new NtfcFold(refHub, trainingIds, evaluationIds, i, modelId, trainingSession, this, configId)).start(); //only changed what fold is started.
			}	
		}
		
		
	}
	
	public synchronized void selectBestEvaluation(){
		/*
		 * Implementing a semaphore so that the selection only takes place when all folds have been computed.
		 */
		openEvaluations--;
		System.out.println(openEvaluations+" unfinished folds remaining.");
		if(openEvaluations==0){
			TrainingSession trainingSession = refHub.getEvaluationManager().getTrainingSessionByAddress(trainingSessionId);
			String appendString="";
			Model model = refHub.getModelManager().getModelByAddress(modelId);
			SelectionPolicy selectionPolicy = refHub.getConfigurationManager().getByAddress(configId).getSelectionPolicy();
			Evaluation[] evaluations = trainingSession.getEvaluationArray();	
			model.appendToTrainingLog("There are "+evaluations.length+" evaluations.");
			double maxValue=0.0;
			int maxId=0;
			for(int i=0; i<evaluations.length; i++){
				Evaluation eval = evaluations[i];
				appendString = "<br /> Evaluation: "+eval.getFoldId();
				appendString = appendString+"<br /> Microaverage Precision: "+eval.getMicroaveragePrecision()+" Microaverage Recall: "+eval.getMicroaverageRecall()+" Microaverage F1 "+eval.getMicroaverageF1();
				appendString = appendString+"<br /> Macroaverage Precision: "+eval.getMacroaveragePrecision()+" Macroaverage Recall: "+eval.getMacroaverageRecall()+" Macroaverage F1 "+eval.getMacroaverageF1(); 
				Category[] categories = refHub.getCategoryManager().getCategoryArray();
				for(int j=0; j<categories.length; j++){
					appendString = appendString + "<br /> Category: "+categories[j].getId()+
							" TP: "+eval.getTP(categories[j].getId())+" FP: "+eval.getFP(categories[j].getId())+
							" FN: "+eval.getFN(categories[j].getId())+
							" precision: "+eval.getPrecision(categories[j].getId())+" recall "+eval.getRecall(categories[j].getId())+
							" F1: "+eval.getF1(categories[j].getId());
				}
				model.appendToTrainingLog(appendString);
				if(selectionPolicy==SelectionPolicy.MicroaverageF1){
					if(eval.getMicroaverageF1()>maxValue){
						maxValue=eval.getMicroaverageF1();
						maxId=i;
					}
				}else if(selectionPolicy==SelectionPolicy.MicroaveragePrecision){
					if(eval.getMicroaveragePrecision()>maxValue){
						maxValue=eval.getMicroaveragePrecision();
						maxId=i;
					}
				}else if(selectionPolicy==SelectionPolicy.MicroaverageRecall){
					if(eval.getMicroaverageRecall()>maxValue){
						maxValue=eval.getMicroaverageRecall();
						maxId=i;
					}
				}else if(selectionPolicy==SelectionPolicy.MacroaverageF1){
					if(eval.getMacroaverageF1()>maxValue){
						maxValue=eval.getMacroaverageF1();
						maxId=i;
					}
				}else if(selectionPolicy==SelectionPolicy.MacroaveragePrecision){
					if(eval.getMacroaveragePrecision()>maxValue){
						maxValue=eval.getMacroaveragePrecision();
						maxId=i;
					}
				}else if(selectionPolicy==SelectionPolicy.MacroaverageRecall){
					if(eval.getMacroaverageRecall()>maxValue){
						maxValue=eval.getMacroaverageRecall();
						maxId=i;
					}
				}
			}
			model.appendToTrainingLog("<br /> Best evaluation following the "+selectionPolicy.toString()+" Policy is: "+evaluations[maxId].getFoldId());
			System.out.println(" Best evaluation following the "+selectionPolicy.toString()+" Policy is: "+evaluations[maxId].getFoldId());
			long weId=refHub.getConfigurationManager().getByAddress(configId).getWordEmbeddingId();
			String localFilePath = refHub.getWordEmbeddingManager().getByAddress(weId).getLocalFilePath();
			model.setWordEmbedding(localFilePath);
			refHub.getModelManager().setTrainingInProgress(false);
		}
	}
	
	
}
