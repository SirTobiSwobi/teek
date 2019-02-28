package org.SirTobiSwobi.c3.teek.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

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
import javax.ws.rs.core.Response.ResponseBuilder;

import org.SirTobiSwobi.c3.teek.api.TCCategories;
import org.SirTobiSwobi.c3.teek.api.TCCategory;
import org.SirTobiSwobi.c3.teek.api.TCHash;
import org.SirTobiSwobi.c3.teek.api.TCMetadata;
import org.SirTobiSwobi.c3.teek.api.TCMlrMetadata;
import org.SirTobiSwobi.c3.teek.api.TCWordEmbedding;
import org.SirTobiSwobi.c3.teek.api.TCWordEmbeddingMetadata;
import org.SirTobiSwobi.c3.teek.api.TCWordEmbeddings;
import org.SirTobiSwobi.c3.teek.db.Category;
import org.SirTobiSwobi.c3.teek.db.MlrType;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.WordEmbedding;
import org.SirTobiSwobi.c3.teek.db.WordEmbeddingAnalyzer;
import org.SirTobiSwobi.c3.teek.db.WordEmbeddingManager;
import org.SirTobiSwobi.c3.teek.db.WordEmbeddingMetadata;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/wordembeddings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WordEmbeddingsResource {
	private ReferenceHub refHub;

	public WordEmbeddingsResource(ReferenceHub refHub) {
		super();
		this.refHub = refHub;
	}
	
	@GET
	public Response getWordEmbeddings(@QueryParam("hash") String hash){
		if(hash!=null&&hash.equals("1")){
			TCHash h = new TCHash("wordembeddings",refHub.getWordEmbeddingManager().getHash());
			return Response.ok(h).build();
		}else{/*
			WordEmbedding[] wordEmbeddings = refHub.getWordEmbeddingManager().getWordEmbeddingArray();
			TCWordEmbedding[] TCWordEmbeddingArray = new TCWordEmbedding[wordEmbeddings.length];
			for(int i=0; i<wordEmbeddings.length;i++){
				WordEmbedding we = wordEmbeddings[i];
				TCWordEmbedding TCwe = new TCWordEmbedding(
						we.getId(), we.getSize(), we.getDimensions(), we.getTerms(), we.getVectors()
						);
				TCWordEmbeddingArray[i]=TCwe;
			}
			*/
			WordEmbeddingMetadata[] wordEmbeddings = refHub.getWordEmbeddingManager().getWordEmbeddingArray();
			TCWordEmbeddingMetadata[] TCWordEmbeddingArray = new TCWordEmbeddingMetadata[wordEmbeddings.length];
			for(int i=0; i<wordEmbeddings.length; i++){
				WordEmbeddingMetadata we = wordEmbeddings[i];
				TCMlrMetadata metadata = new TCMlrMetadata(we.getFileType(), we.getMlrTypeString(), we.getCreationDate(), we.getTechnicalInformation(),
						we.getStructuralMetadata(), we.getNaturalLanguage(), we.getModelSubject(), we.getModelName(), we.getCreator(), we.getKeyWords());
				TCWordEmbeddingMetadata TCwe = new TCWordEmbeddingMetadata(metadata, we.getId(), we.getAlgorithm(), we.getDimensions(), we.getTerms(), we.getLocalFilePath());
				TCWordEmbeddingArray[i]=TCwe;		
		
			}
			TCWordEmbeddings TCwordEmbeddings;
			if(TCWordEmbeddingArray.length>0){
				TCwordEmbeddings = new TCWordEmbeddings(TCWordEmbeddingArray);
			}else{
				TCwordEmbeddings = new TCWordEmbeddings();
			}
			
			return Response.ok(TCwordEmbeddings).build();
		}
	}
	/*
	@POST
	public synchronized Response addWordEmbeddings(@NotNull @Valid TCWordEmbeddings wordEmbeddings){
		if(wordEmbeddings.getWordEmbeddings().length==0){
			Response response = Response.status(400).build();
			return response;
		}
		else if(wordEmbeddings.getWordEmbeddings().length>0){
			for(int i=0; i<wordEmbeddings.getWordEmbeddings().length; i++){
				TCWordEmbedding we=wordEmbeddings.getWordEmbeddings()[i];
				if(we.getId()>=0){
					refHub.getWordEmbeddingManager().setWordEmbedding(new WordEmbedding(we.getId(),we.getSize(), we.getDimensions(), we.getTerms(), we.getVectors()));
				}else{			
					refHub.getWordEmbeddingManager().addWordEmbeddingWithoutId(new WordEmbedding(-1,we.getSize(), we.getDimensions(), we.getTerms(), we.getVectors()));
				}
			}
		}
		Response response = Response.ok().build();
		return response;
	}
	*/
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces( {"text/xml"})
	public Response uploadWordEmbedding(@FormDataParam("file") final InputStream fileInputStream,
        @FormDataParam("file") final FormDataContentDisposition fileDetail, 
        @FormDataParam("id") final long id, 
        @FormDataParam("technicalInformation") final String technicalInformation,
        @FormDataParam("structuralMetadata") final String structuralMetadata,
        @FormDataParam("naturalLanguage") final String naturalLanguage,
        @FormDataParam("modelSubject") final String modelSubject,
        @FormDataParam("creator") final String creator,
        @FormDataParam("keyWords") final String keyWords,
        @FormDataParam("algorithm") final String algorithm,
        @FormDataParam("creationDate") final String creationDate){
		String fileLocation="/opt/wordembeddings/"+fileDetail.getFileName();
		
		try {
			refHub.getFileSystemManager().writeToFile(fileInputStream, fileLocation);
			String fileType = fileDetail.getType(); //only replies "form-data". Not very helpful.
			fileType = "text";
			MlrType mlrType = MlrType.WordEmbedding;
			//String creationDate = fileDetail.getCreationDate().toString();//doesn't work. .getCreationDate() returns null.
			String newCreationDate = "";
			if(creationDate==null||creationDate.length()==0){
				Date newDate = new Date();
				newCreationDate = newDate.toString();
			}else{
				newCreationDate = creationDate;
			}
			String modelName = fileDetail.getFileName();
			int dimensions = 0;
			int terms = 0;
			
			WordEmbeddingMetadata wordEmbedding=WordEmbeddingMetadata.buildWordEmbeddingMetadata(id, fileType, mlrType, newCreationDate, 
					technicalInformation, structuralMetadata, naturalLanguage, modelSubject, 
					modelName, creator, keyWords, dimensions, terms, algorithm, fileLocation);
			
			
			if(id!=-1){
				refHub.getWordEmbeddingManager().setWordEmbedding(wordEmbedding);
				new WordEmbeddingAnalyzer(id,fileLocation,refHub).run();
			}else{
				long newId = refHub.getWordEmbeddingManager().addWordEmbeddingWithoutId(wordEmbedding);
				new WordEmbeddingAnalyzer(newId,fileLocation,refHub).run();
			}
			
			URI uri = new URI("/html/wordembeddings.html");
			ResponseBuilder builder = Response.seeOther(uri);	
			Response redirect = builder.build();
			return redirect;
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(500).build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
		
	}
	
	
	
	@DELETE
	public synchronized Response deleteAllWordEmbeddings(){
		WordEmbeddingMetadata[] wordEmbeddings = refHub.getWordEmbeddingManager().getWordEmbeddingArray();
		for(int i=0; i<wordEmbeddings.length; i++){
			WordEmbeddingMetadata we = wordEmbeddings[i];
			refHub.getFileSystemManager().deleteWordEmbedding(we.getLocalFilePath());
		}
		refHub.setWordEmbeddingManager(new WordEmbeddingManager());
		refHub.getWordEmbeddingManager().setRefHub(refHub);
		Response response = Response.ok().build();
		return response;
	}

}
