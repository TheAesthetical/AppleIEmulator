package Main;

import java.awt.Dimension;

import javax.swing.JFrame;

import Terminal.Monitor;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Monitor Screen = new Monitor(3 , "Apple 1 Emulator - PROTOTYPE - ");
			
		//for (int i = 32; i <= 95; i++) 
//		for (int i = -1111; i <= 0; i++) 
//		{
//			Thread.sleep(15);
//			Screen.drawNextCharacter(i);
//			
//		} 
		
//		Thread.sleep(5000);
//		Screen.drawNextCharacter(72);
//		Thread.sleep(400);
//		Screen.drawNextCharacter(73);
//		Thread.sleep(400);
//		Screen.drawNextCharacter(32);
//		Thread.sleep(400);
//		Screen.drawNextCharacter(65);
//		Thread.sleep(400);
//		Screen.drawNextCharacter(76);
//		Thread.sleep(400);
//		Screen.drawNextCharacter(89);
//		Thread.sleep(400);
//		Screen.drawNextCharacter(88);
//		Thread.sleep(400);
//		Screen.drawNextCharacter(33);
		
		Screen.drawNextCharacter(97);

	}

}
