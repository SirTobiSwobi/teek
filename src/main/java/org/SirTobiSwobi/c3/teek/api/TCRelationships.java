package org.SirTobiSwobi.c3.teek.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TCRelationships {
	private TCRelationship[] relationships;
	
	public TCRelationships(){
		//Jackson deserialization
	}
	

	public TCRelationships(TCRelationship[] relationships) {
		this.relationships = relationships;
	}



	@JsonProperty
	public TCRelationship[] getRelationships() {
		return relationships;
	}
	
	
}
