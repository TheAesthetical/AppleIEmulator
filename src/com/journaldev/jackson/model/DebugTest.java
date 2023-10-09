package com.journaldev.jackson.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DebugTest
{
	private String name;
	private DebugState initial;
	@JsonProperty("final")
	private DebugState finalState;
	@JsonIgnore
	private String[][] cycles;
	
	// ===========================================================================================
	// Constructor 
	// ===========================================================================================

	
	
	// ===========================================================================================
	// Setters 
	// ===========================================================================================

	public void setName(String input)
	{
		this.name = input;
	}
	
	public void setInitial(DebugState input)
	{
		this.initial = input;
	}
	
	public void setFinal(DebugState input)
	{
		this.finalState = input;
	}
	
	public void setCycles(String[][] input)
	{
		this.cycles = input;
	}
	
	// ===========================================================================================
	// Getters 
	// ===========================================================================================

	public String getName()
	{
		return this.name;
	}
	
	public DebugState getInitial()
	{
		return this.initial;
	}
	
	public DebugState getFinal()
	{
		return this.finalState;
	}
	
	public String[][] getCycles()
	{
		return this.cycles;
	}
	
	// ===========================================================================================
	// Utilities 
	// ===========================================================================================

	
	public static void main(String[] args)
	{
		

	}
}
