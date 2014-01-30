package com.example.secrets;

public class Secret {
	
	private String description;

	
	public Secret(String description){
		this.description = description;

	}

	
	public String toString(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	
	

}
