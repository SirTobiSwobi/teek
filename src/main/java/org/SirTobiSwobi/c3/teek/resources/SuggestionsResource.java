package org.SirTobiSwobi.c3.teek.resources;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.teek.api.TCCategory;
import org.SirTobiSwobi.c3.teek.api.TCRelationship;
import org.SirTobiSwobi.c3.teek.api.TCSuggestions;
import org.SirTobiSwobi.c3.teek.core.ArithmeticVectorStreamPredictor;
import org.SirTobiSwobi.c3.teek.core.BamFeatureExtractor;
import org.SirTobiSwobi.c3.teek.core.RegressionVectorStreamPredictor;
import org.SirTobiSwobi.c3.teek.core.VectorStreamPredictor;
import org.SirTobiSwobi.c3.teek.db.Category;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.SearchDirection;
import org.SirTobiSwobi.c3.teek.db.WordEmbedding;

import com.codahale.metrics.annotation.Timed;

@Path("/categories/{cat}/suggestions/{m}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SuggestionsResource {
	private ReferenceHub refHub;

	public SuggestionsResource(ReferenceHub refHub) {
		this.refHub = refHub;
	}
	
	@GET
    @Timed
	public Response getCategory(@PathParam("cat") long cat, @PathParam("m") int m){
		if(!refHub.getCategoryManager().containsCategory(cat)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		/*TCCategory output = new TCCategory(refHub.getCategoryManager().getByAddress(cat).getId(),
				refHub.getCategoryManager().getByAddress(cat).getLabel(),
				refHub.getCategoryManager().getByAddress(cat).getDescription());*/
		try{
			TCSuggestions output = generateOutput(cat, m);
			
			return Response.ok(output).build();
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
			return Response.serverError().build();
		}
		
		
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
