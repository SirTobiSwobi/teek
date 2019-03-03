package org.SirTobiSwobi.c3.teek.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.SirTobiSwobi.c3.teek.api.TCConfiguration;
import org.SirTobiSwobi.c3.teek.api.TCConfigurations;
import org.SirTobiSwobi.c3.teek.api.TCHash;
import org.SirTobiSwobi.c3.teek.db.Configuration;
import org.SirTobiSwobi.c3.teek.db.ConfigurationManager;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.SelectionPolicy;

@Path("/configurations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConfigurationsResource {
	private ReferenceHub refHub;

	public ConfigurationsResource(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}
	
	@GET
	public Response getConfigurations(@QueryParam("hash") String hash){
		if(hash!=null&&hash.equals("1")){
			TCHash h = new TCHash("configurations",refHub.getConfigurationManager().getConfigurationHash());
			return Response.ok(h).build();
		}else{
			Configuration[] confArray=refHub.getConfigurationManager().getConfigurationArray();
			TCConfiguration[] outputArray = new TCConfiguration[confArray.length];
			for(int i=0; i<confArray.length;i++){
				
				outputArray[i]=new TCConfiguration(confArray[i].getId(),
						confArray[i].getAlgorithm(),
						confArray[i].getWordEmbeddingId()
						);
			}
			TCConfigurations output = new TCConfigurations(outputArray);
			return Response.ok(output).build();
		}	
	}
	
	@POST
	public Response addConfiguration(@NotNull @Valid TCConfigurations configurations){
		
		if(configurations.getConfigurations().length==0){
			Response response = Response.status(400).build();
			return response;
		}
		else if(configurations.getConfigurations().length>0){
			for(int i=0; i<configurations.getConfigurations().length; i++){
				TCConfiguration conf=configurations.getConfigurations()[i];
				
				if(conf.getId()>=0){
					
					Configuration config = new Configuration(conf.getId(), 
															conf.getAlgorithm(),
															conf.getWordEmbeddingId()
															);
					refHub.getConfigurationManager().setConfiguration(config);
				}else{			
					refHub.getConfigurationManager().addConfigurationWithoutId(conf.getAlgorithm(),
																				conf.getWordEmbeddingId());
				}
			}
		}
		
		Response response = Response.status(201).build();
		return response;
	}
	
	@DELETE
	public Response deleteAllConfigurations(){
		refHub.setConfigurationManager(new ConfigurationManager());
		Response response = Response.ok().build();
		return response;
	}
}
