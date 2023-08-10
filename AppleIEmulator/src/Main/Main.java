package Main;

import Computer.*;
import Terminal.Monitor;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Monitor Screen = new Monitor(3 , "Apple 1 Emulator - PROTOTYPE - ");
		ROM MemoryROM = new ROM(8 , "ROM4");
		RAM MemoryRAM = new RAM(16 , MemoryROM);
		CPU6502 CPU = new CPU6502(MemoryRAM);
		
		
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
