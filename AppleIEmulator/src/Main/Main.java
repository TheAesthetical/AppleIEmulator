package Main;

import java.awt.Dimension;

import javax.swing.JFrame;

import Terminal.Monitor;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Monitor Screen = new Monitor(3 , "Apple 1 Emulator");
			
		for (int i = 32; i <= 95; i++) 
		{
			Thread.sleep(15);
			Screen.drawNextCharacter(i);
			
		} 

	}

}
