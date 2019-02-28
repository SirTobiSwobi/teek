package org.SirTobiSwobi.c3.teek.core;

import org.SirTobiSwobi.c3.teek.db.Configuration;
import org.SirTobiSwobi.c3.teek.db.Model;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.WordEmbedding;

public class Classifier {
	private ReferenceHub refHub;

	public Classifier(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}
	
	public boolean categorizeDocument(long docId){
		boolean categorizationPossible=isCategorizationPossible(docId);
		if(categorizationPossible){
			Model model = refHub.getActiveModel();
			WordEmbedding we = refHub.getActiveWordEmbedding();
			Configuration config = refHub.getActiveModel().getConfiguration();
			if(config.getAlgorithm().equals("dai")){
				new DaiThread(refHub,docId).run();
			}else{	//default is ntfc
				new NtfcThread(refHub,docId).run();
			}
		}
		
		
		//spawn classification thread;
		
		return categorizationPossible;
	}
	
	private boolean isCategorizationPossible(long docId){
		boolean categorizationPossible=false;
		if(refHub.getDocumentManager().containsDocument(docId)&&refHub.getCategoryManager().getSize()>0){
			categorizationPossible=true;
		}
		return categorizationPossible;
	}
}
