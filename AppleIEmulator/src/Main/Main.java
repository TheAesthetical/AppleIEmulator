package Main;

import java.awt.Dimension;

import javax.swing.JFrame;

import Terminal.Monitor;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Monitor Screen = new Monitor(3 , "Apple 1 Emulator");
			
		for (int i = 0; i < 64; i++) 
		{
			Thread.sleep(2);
			Screen.drawNextCharacter(i);
			
			if(i == 4 ||i == 52)
			{
				Screen.carriageReturn();
				Screen.carriageReturn();
				Screen.carriageReturn();
				
			}
			
		} 

	}

}
