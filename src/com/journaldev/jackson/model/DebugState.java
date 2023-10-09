package com.journaldev.jackson.model;


public class DebugState
{
	private int PC;
	private int S;
	private int A;
	private int X;
	private int Y;
	private int P;
	private int[][] ram;
	
	// ===========================================================================================
	// Setters 
	// ===========================================================================================

	public void setPC(int input)
	{
		this.PC = input;
	}
	
	public void setS(int input)
	{
		this.S = input;
	}
	
	public void setA(int input)
	{
		this.A = input;
	}
	
	public void setX(int input)
	{
		this.X = input;
	}
	
	public void setY(int input)
	{
		this.Y = input;
	}
	
	public void setP(int input)
	{
		this.P = input;
	}
	
	public void setRam(int[][] input)
	{
		this.ram = input;
	}
	
	// ===========================================================================================
	// Getters 
	// ===========================================================================================

	public int getPC()
	{
		return this.PC;
	}
	
	public int getS()
	{
		return this.S;
	}
	
	public int getA()
	{
		return this.A;
	}
	
	public int getX()
	{
		return this.X;
	}
	
	public int getY()
	{
		return this.Y;
	}
	
	public int getP()
	{
		return this.P;
	}
	
	public int[][] getRam()
	{
		return this.ram;
	}
}
