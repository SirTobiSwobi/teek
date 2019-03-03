package org.SirTobiSwobi.c3.teek.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.teek.api.TCConfiguration;
import org.SirTobiSwobi.c3.teek.api.TCModel;
import org.SirTobiSwobi.c3.teek.api.TCProgress;
import org.SirTobiSwobi.c3.teek.api.TCSvmModel;
import org.SirTobiSwobi.c3.teek.api.TCSvmNode;
import org.SirTobiSwobi.c3.teek.api.TCWordEmbedding;
import org.SirTobiSwobi.c3.teek.core.Utilities;
import org.SirTobiSwobi.c3.teek.db.Configuration;
import org.SirTobiSwobi.c3.teek.db.Model;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.SelectionPolicy;

@Path("/models/{mod}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModelResource {
	private ReferenceHub refHub;
	
	public ModelResource(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}

	@GET
	public Response getModel(@PathParam("mod") long mod){
		if(!refHub.getModelManager().containsModel(mod)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		Model model = refHub.getModelManager().getModelByAddress(mod);
		if(model.getProgress()<1.0){
			TCProgress output = new TCProgress("/models/"+mod,model.getProgress());
			return Response.ok(output).build();
		}else{
			
			TCModel output = buildTCModel(model, refHub);
			return Response.ok(output).build();
		}
		
		
		
	}
	
	@DELETE
	public Response deleteModel(@PathParam("mod") long mod){
		if(!refHub.getModelManager().containsModel(mod)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		refHub.getModelManager().deleteModel(mod);
		Response response = Response.ok().build();
		return response;
	}
	
	public static TCModel buildTCModel(Model model, ReferenceHub refHub){
		if(model==null){
			return null;
		}
		
		//long id, int size, int dimensions, String[] terms, double[][] vectors
		/* Too big to handle this way. Requires multipart form for uplaod. Only reference
		TCWordEmbedding wordEmbedding = new TCWordEmbedding(model.getWordEmbedding().getId(),
															model.getWordEmbedding().getSize(),
															model.getWordEmbedding().getDimensions(),
															model.getWordEmbedding().getTerms(),
															model.getWordEmbedding().getVectors());
															*/
		long configId = model.getConfiguration().getId();
		long wordEmbeddingId = refHub.getConfigurationManager().getByAddress(configId).getWordEmbeddingId();
		String wordEmbeddingLocalFilePath = refHub.getWordEmbeddingManager().getByAddress(wordEmbeddingId).getLocalFilePath();
		Configuration conf = model.getConfiguration();
		TCConfiguration configuration = new TCConfiguration(conf.getId(), conf.getAlgorithm(), conf.getWordEmbeddingId());
		TCModel output = new TCModel(model.getId(), model.getConfiguration().getId(), model.getProgress(), model.getTrainingLog(), configuration, wordEmbeddingLocalFilePath);
		return output;
	}
	
}
