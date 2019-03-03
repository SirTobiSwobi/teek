package org.SirTobiSwobi.c3.teek.db;

import org.SirTobiSwobi.c3.teek.core.Suggestor;

public class ReferenceHub {
	private CategoryManager categoryManager;
	private DocumentManager documentManager;
	private ConfigurationManager configurationManager;
	private ModelManager modelManager;
	private Model activeModel;
	private boolean needsRetraining;
	private WordEmbeddingManager wordEmbeddingManager;
	private FileSystemManager fileSystemManager;
	private WordEmbedding activeWordEmbedding;
	private Suggestor suggestor;
	
	public ReferenceHub(CategoryManager categoryManager, DocumentManager documentManager,
			ConfigurationManager configurationManager, ModelManager modelManager, 
			Model activeModel, WordEmbeddingManager wordEmbeddingManager) {
		super();
		this.categoryManager = categoryManager;
		this.documentManager = documentManager;
		this.configurationManager = configurationManager;
		this.modelManager = modelManager;
		this.activeModel = activeModel;
		this.needsRetraining = false;
		this.wordEmbeddingManager = wordEmbeddingManager;
		this.fileSystemManager = new FileSystemManager();
		this.activeWordEmbedding = null;
		this.suggestor = new Suggestor(this);
	}
	public CategoryManager getCategoryManager() {
		return categoryManager;
	}
	public void setCategoryManager(CategoryManager categoryManager) {
		this.categoryManager = categoryManager;
	}
	public DocumentManager getDocumentManager() {
		return documentManager;
	}
	public void setDocumentManager(DocumentManager documentManager) {
		this.documentManager = documentManager;
	}
	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}
	public ModelManager getModelManager() {
		return modelManager;
	}
	public void setModelManager(ModelManager modelManager) {
		this.modelManager = modelManager;
	}
	public Model getActiveModel() {
		return activeModel;
	}
	public void setActiveModel(Model activeModel) {
		this.activeModel = activeModel;
	}
	public boolean needsRetraining() {
		return needsRetraining;
	}
	public void setNeedsRetraining(boolean needsRetraining) {
		this.needsRetraining = needsRetraining;
	}
	public WordEmbeddingManager getWordEmbeddingManager() {
		return wordEmbeddingManager;
	}
	public void setWordEmbeddingManager(WordEmbeddingManager wordEmbeddingManager) {
		this.wordEmbeddingManager = wordEmbeddingManager;
	}
	public FileSystemManager getFileSystemManager() {
		return fileSystemManager;
	}
	public void setFileSystemManager(FileSystemManager fileSystemManager) {
		this.fileSystemManager = fileSystemManager;
	}
	public WordEmbedding getActiveWordEmbedding() {
		return activeWordEmbedding;
	}
	public void setActiveWordEmbedding(WordEmbedding activeWordEmbedding) {
		this.activeWordEmbedding = activeWordEmbedding;
	}
	public Suggestor getSuggestor() {
		return suggestor;
	}
	public void setSuggestor(Suggestor suggestor) {
		this.suggestor = suggestor;
	}
}
