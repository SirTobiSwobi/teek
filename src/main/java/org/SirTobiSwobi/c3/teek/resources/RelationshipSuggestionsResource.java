package org.SirTobiSwobi.c3.teek.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.teek.api.TCRelationships;
import org.SirTobiSwobi.c3.teek.api.TCSuggestions;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;

import com.codahale.metrics.annotation.Timed;

@Path("/categories/{cat}/suggestions/{m}/relationships")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RelationshipSuggestionsResource {
	private ReferenceHub refHub;

	public RelationshipSuggestionsResource(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}
	
	@GET
    @Timed
	public Response getCategory(@PathParam("cat") long cat, @PathParam("m") int m){
		if(!refHub.getCategoryManager().containsCategory(cat)){
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		try{
			TCSuggestions output = refHub.getSuggestor().generateOutput(cat, m);
			TCRelationships relationships = new TCRelationships(output.getRelationships());
			
			return Response.ok(relationships).build();
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
			return Response.serverError().build();
		}
		
		
	}

}
