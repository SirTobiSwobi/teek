package org.SirTobiSwobi.c3.teek.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.teek.api.TCMlrMetadata;
import org.SirTobiSwobi.c3.teek.api.TCWordEmbedding;
import org.SirTobiSwobi.c3.teek.api.TCWordEmbeddingMetadata;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.WordEmbedding;
import org.SirTobiSwobi.c3.teek.db.WordEmbeddingMetadata;


@Path("/wordembeddings/{we}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WordEmbeddingResource {
	ReferenceHub refHub;

	public WordEmbeddingResource(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}
	
	@GET
	public Response getWordEmbedding(@PathParam("we") long we){
		if(!refHub.getWordEmbeddingManager().containsWordEmbedding(we)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		WordEmbeddingMetadata wordEmbedding = refHub.getWordEmbeddingManager().getByAddress(we);
		TCMlrMetadata metadata = new TCMlrMetadata(wordEmbedding.getFileType(), wordEmbedding.getMlrTypeString(), wordEmbedding.getCreationDate(), wordEmbedding.getTechnicalInformation(),
				wordEmbedding.getStructuralMetadata(), wordEmbedding.getNaturalLanguage(), wordEmbedding.getModelSubject(), wordEmbedding.getModelName(), wordEmbedding.getCreator(), wordEmbedding.getKeyWords());
		TCWordEmbeddingMetadata output = new TCWordEmbeddingMetadata(metadata, wordEmbedding.getId(), wordEmbedding.getAlgorithm(), wordEmbedding.getDimensions(), wordEmbedding.getTerms(), wordEmbedding.getLocalFilePath());
		
		/*
		TCWordEmbedding output =  new TCWordEmbedding(
				refHub.getWordEmbeddingManager().getByAddress(we).getId(), 
				refHub.getWordEmbeddingManager().getByAddress(we).getSize(), 
				refHub.getWordEmbeddingManager().getByAddress(we).getDimensions(), 
				refHub.getWordEmbeddingManager().getByAddress(we).getTerms(), 
				refHub.getWordEmbeddingManager().getByAddress(we).getVectors()
				);
		*/
		return Response.ok(output).build();
		
	}
	
	/**
	 * 
	 * Only supporting POST. ID can be set there. 
	 */
	/*
	@PUT
	public Response setWordEmbedding(@PathParam("we") long we, @NotNull @Valid TCWordEmbedding wordEmbedding){
		if(wordEmbedding.getId()!=we){
			Response response = Response.status(400).build();
			return response;
		}
		
		refHub.getWordEmbeddingManager().setWordEmbedding(new WordEmbedding(wordEmbedding.getId(), 
																			wordEmbedding.getSize(), 
																			wordEmbedding.getDimensions(), 
																			wordEmbedding.getTerms(), 
																			wordEmbedding.getVectors()));
		Response response = Response.ok().build();
		return response;
	}
	*/
	@DELETE
	public synchronized Response deleteWordEmbedding(@PathParam("we") long we){
		if(!refHub.getWordEmbeddingManager().containsWordEmbedding(we)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		String localFilepath = refHub.getWordEmbeddingManager().getByAddress(we).getLocalFilePath();
		refHub.getFileSystemManager().deleteWordEmbedding(localFilepath);
		refHub.getWordEmbeddingManager().deleteWordEmbedding(we);	
		Response response = Response.ok().build();
		return response;
	}
	
	
}
