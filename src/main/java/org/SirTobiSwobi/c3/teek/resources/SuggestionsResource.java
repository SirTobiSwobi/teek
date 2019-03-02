package org.SirTobiSwobi.c3.teek.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.teek.api.TCSuggestions;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;

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
		
		TCSuggestions output = new TCSuggestions();
		
		return Response.ok(output).build();
		
	}

}
