package org.SirTobiSwobi.c3.teek.core;

import org.SirTobiSwobi.c3.teek.db.Category;
import org.SirTobiSwobi.c3.teek.db.Configuration;
import org.SirTobiSwobi.c3.teek.db.Document;
import org.SirTobiSwobi.c3.teek.db.Model;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.SearchDirection;
import org.SirTobiSwobi.c3.teek.db.WordEmbedding;

public class CategorizationThread extends Thread {
	protected ReferenceHub refHub;
	protected Document document;

	public CategorizationThread(ReferenceHub refHub, long docId) {
		super();
		this.refHub = refHub;
		this.document = refHub.getDocumentManager().getByAddress(docId);
	}
	
	public void run(){
		/**
		 * One could simply inherit from this class and implement the performClassification method differently.
		 */
		performCategorization();
	}
	
	/**
	 * This is a dummy example classification that assigns all documents with even ids to categories with even ids. 
	 */
	private void performCategorization(){
		Category[] categories = refHub.getCategoryManager().getCategoryArray();
		for(int i=0;i<categories.length;i++){
			if(categories[i].getId()%2==document.getId()%2){ //if both, the document and the category ID are odd or even.
				if(!refHub.getCategorizationManager().containsCategorizationOf(document.getId(), categories[i].getId())){ //only assign if not already there
					refHub.getCategorizationManager().addCategorizationWithoutId(document.getId(), categories[i].getId(), 0.8, "");
				}
				
				if(refHub.getActiveModel().isIncludeImplicits()){ //if implicits are to be assigned
					long[] implicitCategorizations=refHub.getTargetFunctionManager().findAllImplicitCatIds(categories[i].getId(), SearchDirection.Ascending);
					if(implicitCategorizations!=null){
						for(int k=0; k<implicitCategorizations.length; k++){
							if(!refHub.getCategorizationManager().containsCategorizationOf(document.getId(), implicitCategorizations[k])){
								refHub.getCategorizationManager().addCategorizationWithoutId(document.getId(), implicitCategorizations[k], 0.8, "");
							}
						}
					}
				}
			}
		}
	}

	
	
}
