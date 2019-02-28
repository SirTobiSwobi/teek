package org.SirTobiSwobi.c3.teek.api;

import org.SirTobiSwobi.c3.teek.db.MlrType;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCMlrMetadata {
	private String fileType;
	private String mlrType;
	private String creationDate;
	private String technicalInformation;
	private String structuralMetadata;
	private String naturalLanguage;
	private String modelSubject;
	private String modelName;
	private String creator;
	private String keyWords;
	
	public TCMlrMetadata(){
		//Jackson deserialization
	}

	public TCMlrMetadata(String fileType, String mlrType, String creationDate, String technicalInformation,
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

	@JsonProperty
	public String getFileType() {
		return fileType;
	}

	@JsonProperty
	public String getMlrType() {
		return mlrType;
	}

	@JsonProperty
	public String getCreationDate() {
		return creationDate;
	}

	@JsonProperty
	public String getTechnicalInformation() {
		return technicalInformation;
	}

	@JsonProperty
	public String getStructuralMetadata() {
		return structuralMetadata;
	}

	@JsonProperty
	public String getNaturalLanguage() {
		return naturalLanguage;
	}

	@JsonProperty
	public String getModelSubject() {
		return modelSubject;
	}

	@JsonProperty
	public String getModelName() {
		return modelName;
	}

	@JsonProperty
	public String getCreator() {
		return creator;
	}

	@JsonProperty
	public String getKeyWords() {
		return keyWords;
	}
	
	
}
