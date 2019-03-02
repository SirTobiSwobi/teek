package org.SirTobiSwobi.c3.teek.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCSuggestions {
	private TCCategory[] categories;
	private TCRelationship[] relationships;
	
	
	
	public TCSuggestions() {
		super();
		// Jackson deserialization
	}

	public TCSuggestions(TCCategory[] categories, TCRelationship[] relationships) {
		this.categories = categories;
		this.relationships = relationships;
	}

	@JsonProperty
	public TCCategory[] getCategories() {
		return categories;
	}

	@JsonProperty
	public TCRelationship[] getRelationships() {
		return relationships;
	}
	
	

}
