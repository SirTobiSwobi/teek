package org.SirTobiSwobi.c3.teek.db;

public class MlrMetadata {	
	private String fileType;
	private MlrType mlrType;
	private String creationDate;
	private String technicalInformation;
	private String structuralMetadata;
	private String naturalLanguage;
	private String modelSubject;
	private String modelName;
	private String creator;
	private String keyWords;
	public MlrMetadata(String fileType, MlrType mlrType, String creationDate, String technicalInformation,
			String structuralMetadata, String naturalLanguage, String modelSubject, String modelName, String creator,
			String keyWords) {
		super();
		this.fileType = fileType;
		this.mlrType = mlrType;
		this.creationDate = creationDate;
		this.technicalInformation = technicalInformation;
		this.structuralMetadata = structuralMetadata;
		this.naturalLanguage = naturalLanguage;
		this.modelSubject = modelSubject;
		this.modelName = modelName;
		this.creator = creator;
		this.keyWords = keyWords;
	}
	public static MlrMetadata buildMlrMetadata(String fileType, MlrType mlrType, String creationDate, String technicalInformation,
			String structuralMetadata, String naturalLanguage, String modelSubject, String modelName, String creator,
			String keyWords){
		return new MlrMetadata(fileType, mlrType, creationDate, technicalInformation, structuralMetadata, naturalLanguage, modelSubject, modelName, creator, keyWords);
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public MlrType getMlrType() {
		return mlrType;
	}
	public void setMlrType(MlrType mlrType) {
		this.mlrType = mlrType;
	}
	public String getMlrTypeString(){
		String type="";
		if(this.mlrType==MlrType.ControlledVocabulary){
			type = "Controlled vocabularry";
		}else if(this.mlrType==MlrType.KeyWordList){
			type = "Key word list";
		}else if(this.mlrType==MlrType.KnowledgeOrganizationSystem){
			type = "Knowledge organization system";
		}else if(this.mlrType==MlrType.Ontology){
			type = "Ontology";
		}else if(this.mlrType==MlrType.SetOfCategories){
			type = "Set of categories";
		}else if(this.mlrType==MlrType.StopWordList){
			type = "Stop word list";
		}else if(this.mlrType==MlrType.VectorRepresentation){
			type = "Vector representations";
		}else if(this.mlrType==MlrType.WordEmbedding){
			type = "Word embedding";
		}
		return type;
	}
	public void setMlrTypeByString(String type){
		if(type.equals("Controlled vocabularry")){
			this.mlrType=MlrType.ControlledVocabulary;
		}else if(type.equals("Key word list")){
			this.mlrType=MlrType.KeyWordList;
		}else if(type.equals("Knowledge organization system")){
			this.mlrType=MlrType.KnowledgeOrganizationSystem;
		}else if(type.equals("Ontology")){
			this.mlrType=MlrType.Ontology;
		}else if(type.equals("Set of categories")){
			this.mlrType=MlrType.SetOfCategories;
		}else if(type.equals("Stop word list")){
			this.mlrType=MlrType.StopWordList;
		}else if(type.equals("Vector representations")){
			this.mlrType=MlrType.VectorRepresentation;
		}else if(type.equals("Word embedding")){
			this.mlrType=MlrType.WordEmbedding;
		}
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getTechnicalInformation() {
		return technicalInformation;
	}
	public void setTechnicalInformation(String technicalInformation) {
		this.technicalInformation = technicalInformation;
	}
	public String getStructuralMetadata() {
		return structuralMetadata;
	}
	public void setStructuralMetadata(String structuralMetadata) {
		this.structuralMetadata = structuralMetadata;
	}
	public String getNaturalLanguage() {
		return naturalLanguage;
	}
	public void setNaturalLanguage(String naturalLanguage) {
		this.naturalLanguage = naturalLanguage;
	}
	public String getModelSubject() {
		return modelSubject;
	}
	public void setModelSubject(String modelSubject) {
		this.modelSubject = modelSubject;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	
}
