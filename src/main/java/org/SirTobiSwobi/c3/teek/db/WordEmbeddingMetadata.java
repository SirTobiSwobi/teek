package org.SirTobiSwobi.c3.teek.db;

public class WordEmbeddingMetadata extends MlrMetadata {
	private int dimensions;
	private int terms;
	private String algorithm;
	private String localFilePath;
	private long id;
	public WordEmbeddingMetadata(long id, String fileType, MlrType mlrType, String creationDate, String technicalInformation,
			String structuralMetadata, String naturalLanguage, String modelSubject, String modelName, String creator,
			String keyWords, int dimensions, int terms, String algorithm, String localFilePath) {
		super(fileType, mlrType, creationDate, technicalInformation, structuralMetadata, naturalLanguage, modelSubject,
				modelName, creator, keyWords);
		this.dimensions = dimensions;
		this.terms = terms;
		this.algorithm = algorithm;
		this.localFilePath = localFilePath;
		this.id=id;
	}
	public static WordEmbeddingMetadata buildWordEmbeddingMetadata(long id, String fileType, MlrType mlrType, String creationDate, String technicalInformation,
			String structuralMetadata, String naturalLanguage, String modelSubject, String modelName, String creator,
			String keyWords, int dimensions, int terms, String algorithm, String localFilePath){
		return new WordEmbeddingMetadata(id, fileType, mlrType, creationDate, technicalInformation,
				structuralMetadata, naturalLanguage, modelSubject, modelName, creator,
				keyWords, dimensions, terms, algorithm, localFilePath);
	}
	public int getDimensions() {
		return dimensions;
	}
	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}
	public int getTerms() {
		return terms;
	}
	public void setTerms(int terms) {
		this.terms = terms;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public String getLocalFilePath() {
		return localFilePath;
	}
	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String toString(){
		String rep="";
		rep+=this.getId()+" ";
		rep+=this.getCreationDate()+" ";
		rep+=this.getCreator()+" ";
		rep+=this.getFileType()+" ";
		rep+=this.getKeyWords()+" ";
		rep+=this.getMlrTypeString()+" ";
		rep+=this.getModelName()+" ";
		rep+=this.getModelSubject()+" ";
		rep+=this.getNaturalLanguage()+" ";
		rep+=this.getStructuralMetadata()+" ";
		rep+=this.getTechnicalInformation()+" ";
		rep+=this.getAlgorithm()+" ";
		rep+=this.getDimensions()+" ";
		rep+=this.getLocalFilePath()+" ";
		rep+=this.getTerms();
		return rep;
	}
	
}
