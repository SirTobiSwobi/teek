package org.SirTobiSwobi.c3.teek;

import java.io.IOException;

import javax.ws.rs.client.Client;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.SirTobiSwobi.c3.teek.api.TCModel;
import org.SirTobiSwobi.c3.teek.api.TCSvmModel;
import org.SirTobiSwobi.c3.teek.api.TCSvmNode;
import org.SirTobiSwobi.c3.teek.api.TCWordEmbedding;
import org.SirTobiSwobi.c3.teek.core.Utilities;
import org.SirTobiSwobi.c3.teek.db.Category;
import org.SirTobiSwobi.c3.teek.db.CategoryManager;
import org.SirTobiSwobi.c3.teek.db.Configuration;
import org.SirTobiSwobi.c3.teek.db.ConfigurationManager;
import org.SirTobiSwobi.c3.teek.db.Document;
import org.SirTobiSwobi.c3.teek.db.DocumentManager;
import org.SirTobiSwobi.c3.teek.db.MlrType;
import org.SirTobiSwobi.c3.teek.db.Model;
import org.SirTobiSwobi.c3.teek.db.ModelManager;
import org.SirTobiSwobi.c3.teek.db.ReferenceHub;
import org.SirTobiSwobi.c3.teek.db.RelationshipType;
import org.SirTobiSwobi.c3.teek.db.SelectionPolicy;
import org.SirTobiSwobi.c3.teek.db.WordEmbedding;
import org.SirTobiSwobi.c3.teek.db.WordEmbeddingManager;
import org.SirTobiSwobi.c3.teek.db.WordEmbeddingMetadata;
import org.SirTobiSwobi.c3.teek.health.ConfigHealthCheck;
import org.SirTobiSwobi.c3.teek.resources.ActiveModelResource;
import org.SirTobiSwobi.c3.teek.resources.CategoriesResource;
import org.SirTobiSwobi.c3.teek.resources.CategoryResource;
import org.SirTobiSwobi.c3.teek.resources.ConfigurationResource;
import org.SirTobiSwobi.c3.teek.resources.ConfigurationsResource;
import org.SirTobiSwobi.c3.teek.resources.DocumentResource;
import org.SirTobiSwobi.c3.teek.resources.DocumentsResource;
import org.SirTobiSwobi.c3.teek.resources.MetadataResource;
import org.SirTobiSwobi.c3.teek.resources.ModelResource;
import org.SirTobiSwobi.c3.teek.resources.ModelsResource;
import org.SirTobiSwobi.c3.teek.resources.RelationshipResource;
import org.SirTobiSwobi.c3.teek.resources.RelationshipsResource;
import org.SirTobiSwobi.c3.teek.resources.RetrainingResource;
import org.SirTobiSwobi.c3.teek.resources.SuggestionsResource;
import org.SirTobiSwobi.c3.teek.resources.WordEmbeddingResource;
import org.SirTobiSwobi.c3.teek.resources.WordEmbeddingsResource;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class TeekApplication extends Application<TeekConfiguration> {

	public static void main(String[] args) throws Exception {
		new TeekApplication().run(args);
	}
	
	@Override
	public String getName() {
		return "teek";
	}
	
	@Override
	public void initialize(Bootstrap<TeekConfiguration> bootstrap){
	
		bootstrap.addBundle(new AssetsBundle("/assets/", "/html/", "index.html"));
	}

	@Override
	public void run(TeekConfiguration configuration, Environment environment){
		/*
		 * Initializing data structures
		 */
		
		DocumentManager docMan = new DocumentManager();
		CategoryManager catMan = new CategoryManager(); 
		ConfigurationManager confMan = new ConfigurationManager();
		ModelManager modMan = new ModelManager();
		Model activeModel = null;
		WordEmbeddingManager weMan = new WordEmbeddingManager();
		ReferenceHub refHub = new ReferenceHub(catMan, docMan, confMan, modMan, activeModel, weMan);
		modMan.setRefHub(refHub);
		weMan.setRefHub(refHub);
		
		
		/*
		 * Initializing HTTP client
		 */
		
		final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration()).build(getName());
		
		/*
		 * Initializing resources requiring data structures and clients. 
		 */
	
		final MetadataResource metadata = new MetadataResource(configuration);
		final DocumentsResource documents = new DocumentsResource(refHub,client);
		final DocumentResource document = new DocumentResource(refHub,client);
		final CategoriesResource categories = new CategoriesResource(refHub);
		final CategoryResource category = new CategoryResource(refHub);
		final RelationshipsResource relationships = new RelationshipsResource(refHub);
		final RelationshipResource relationship = new RelationshipResource(refHub);
		final ActiveModelResource activeModelResource = new ActiveModelResource(refHub, client);
		final RetrainingResource retraining = new RetrainingResource(refHub);
		
		if(configuration.getRunType().equals("trainer")){
			final ConfigurationsResource configurations = new ConfigurationsResource(refHub);
			final ConfigurationResource configurationR = new ConfigurationResource(refHub);
			final ModelsResource models = new ModelsResource(refHub);
			final ModelResource model = new ModelResource(refHub);
			final WordEmbeddingsResource wordEmbeddings = new WordEmbeddingsResource(refHub);
			final WordEmbeddingResource wordEmbedding = new WordEmbeddingResource(refHub);
			final SuggestionsResource suggestions = new SuggestionsResource(refHub);
			
	
			environment.jersey().register(configurations);
			environment.jersey().register(configurationR);
			environment.jersey().register(models);
			environment.jersey().register(model);
			environment.jersey().register(MultiPartFeature.class);
			environment.jersey().register(wordEmbeddings);
			environment.jersey().register(wordEmbedding);
			environment.jersey().register(suggestions);
			
			
		}
	
		
		
		/*
		 * Initializing health checks
		 */
		
		final ConfigHealthCheck configHealth = new ConfigHealthCheck(configuration);
		
		/*
		 * Registering everything 
		 */
		
		environment.healthChecks().register("config", configHealth);
		environment.jersey().register(metadata);
		environment.jersey().register(documents);
		environment.jersey().register(document);
		environment.jersey().register(categories);
		environment.jersey().register(category);
		environment.jersey().register(relationships);
		environment.jersey().register(relationship);
		environment.jersey().register(activeModelResource);
		environment.jersey().register(retraining);
		
		/**
		 * Setting up default configuration for configuration and model
		 */
		
		WordEmbeddingMetadata wordEmbedding=WordEmbeddingMetadata.buildWordEmbeddingMetadata(1, 
				"text", 
				MlrType.WordEmbedding, 
				"Wed Oct 10 16:28:09 CEST 2018", 
				"200 dimensions. Forgot the rest.", 
				"Can be part of WE-SVM", 
				"English", 
				"Wikipedia", 
				"skip-gram-wiki1stbill.txt", 
				"Tobias Eljasik-Swoboda", 
				"Wikipedia, Skip-Gram", 
				200, 
				218317, 
				"Skip-Gram", 
				"/opt/wordembeddings/skip-gram-wiki1stbill.txt");
		refHub.getWordEmbeddingManager().setWordEmbedding(wordEmbedding); //the actual file must be in the file system!
		Configuration config = new Configuration(0, "avsp", 1);
		refHub.getConfigurationManager().setConfiguration(config);
		Model model = new Model(0, config, wordEmbedding.getLocalFilePath(), "");
		refHub.getModelManager().setModel(model);
		refHub.setActiveModel(model);
		try {
			refHub.setActiveWordEmbedding(WordEmbedding.buildFromLocalFile(wordEmbedding.getLocalFilePath()));
		} catch (IOException e) {
			System.out.println("Initialization error. Cannot load default word embedding");
			e.printStackTrace();
		}
		
		
		if(configuration.getDebugExamples().equals("true")){
			/*
			 * Generating example data for manual testing during development
			 */
			
			Category cat = new Category(0,"Diseases","");
			catMan.setCategory(cat);
			cat = new Category(4,"Neoplasms","");
			catMan.setCategory(cat);
			cat = new Category(557,"Neoplasms by Histologic Type","");
			catMan.setCategory(cat);
			cat = new Category(665,"Nevi and Melanomas","");
			catMan.setCategory(cat);
			cat = new Category(510,"Melanoma","");
			catMan.setCategory(cat);
			cat = new Category(385,"Hutchinson's Melanotic Freckle","");
			catMan.setCategory(cat);
			cat = new Category(515,"Melanoma, Amelanotic","");
			catMan.setCategory(cat);
			cat = new Category(525,"Melanoma, Experimental","");
			catMan.setCategory(cat);
			cat = new Category(2,"Virus Diseases","");
			catMan.setCategory(cat);
			cat = new Category(256,"DNA Virus Infections","");
			catMan.setCategory(cat);
			cat = new Category(466,"Herpesviridae Infections","");
			catMan.setCategory(cat);
			cat = new Category(382,"Herpes Simplex","");
			catMan.setCategory(cat);
			cat = new Category(465,"Keratitis, Herpetic","");
			catMan.setCategory(cat);
			cat = new Category(450,"Keratitis, Dendritic","");
			catMan.setCategory(cat);
			
			
			catMan.addRelatonshipWithoutId(0, 4, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(4, 557, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(557, 665, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(665, 510, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(510, 385, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(510, 515, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(510, 525, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(0, 2, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(2, 256, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(256, 466, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(466, 382, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(382, 465, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(465, 450, RelationshipType.Sub);
			catMan.addRelatonshipWithoutId(525, 256, RelationshipType.Equality);
			
			docMan.setDocument(new Document(0,"Neoplasms document label","first content"));
			docMan.setDocument(new Document(1,"Melanoma document label","second content"));
			docMan.setDocument(new Document(2,"Virus diseases document label","third content"));
			docMan.setDocument(new Document(3,"Keratitis, Dendritic document label","200th content"));
			docMan.setDocument(new Document(4,"525 document","Melanoma, Experimental and other stuff"));
	
			
			
			
			
			
		}
		
		
			
	}
	
	

	

}
