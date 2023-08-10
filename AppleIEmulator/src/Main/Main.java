package Main;

import Computer.*;
import Terminal.Monitor;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		//Monitor Screen = new Monitor(3 , "Apple 1 Emulator - PROTOTYPE - ");
		//RAM DRAM = new RAM(16);
		//CPU CPU6502 = new CPU(DRAM);
		ROM WozROM = new ROM(8 , "ROMCLEANED3");
		
		//ASCII codes nathaniel... dont be alarmed you havent fucked up yet
//		for (int i = 32; i <= 95; i++) 
//		{
//			Thread.sleep(20);
//			Screen.drawNextCharacter(i);
//			
//		} 
		
//		Screen.drawNextCharacter(71);
//		Screen.drawNextCharacter(69);
//		Screen.drawNextCharacter(77);
//		Screen.drawNextCharacter(77);
//		Screen.drawNextCharacter(65);
//		Screen.drawNextCharacter(33);
		
	}

}
