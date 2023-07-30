package Main;

import java.awt.Dimension;

import javax.swing.JFrame;

//import Display.CursorControl;
import Display.Monitor;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		JFrame Window = new JFrame("Apple 1 Emulator");
		Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Window.setResizable(false);
        
		Monitor Screen = new Monitor();
		
		Screen.setPreferredSize(new Dimension(Monitor.getGridWidth() * Monitor.getCellSize(), Monitor.getGridHeight() * Monitor.getCellSize()));

		Window.getContentPane().add(Screen);
		Window.pack();
		Window.setVisible(true);
		
		for (int j = 0; j < 20; j++) 
		{
			
		for (int i = 0; i < 64; i++) 
		{
			Thread.sleep(10);
			Screen.drawNextCharacter(i);
			
//			if(i == 4)
//			{
//				Screen.carriageReturn();
//				Screen.carriageReturn();
//				Screen.carriageReturn();
//				
//			}
			
		} 
		
		Thread.sleep(5);
		
		}
		
		System.out.println(Screen.getiRowShift());

	}

}
