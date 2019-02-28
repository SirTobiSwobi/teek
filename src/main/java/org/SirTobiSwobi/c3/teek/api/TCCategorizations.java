package org.SirTobiSwobi.c3.teek.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCCategorizations {
	private TCCategorization[] categorizations;
	
	public TCCategorizations(){
		//Jackson deserialization
	}

	public TCCategorizations(TCCategorization[] categorizations) {
		super();
		this.categorizations = categorizations;
	}
	@JsonProperty
	public TCCategorization[] getCategorizations() {
		return categorizations;
	}
	
	
}
