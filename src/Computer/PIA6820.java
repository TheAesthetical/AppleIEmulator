package Computer;

import java.util.concurrent.TimeUnit;

import Terminal.Monitor;

public class PIA6820 {

	private RAM Memory;
	private Monitor Screen;

	//PIA variables in RAM - Check Apple 1 owners manual!
	private final short shKBD = (short) 0xD010;
	private final short shKBDCR = (short) 0xD011;
	private final short shDSP = (short) 0xD012;
	private final short shDSPCR = (short) 0xD013;

	private boolean bKeyPressed = false;
	private char chCharacterPressed;
	
	public void setKeyPressed(boolean bKeyPressed) 
	{
		this.bKeyPressed = bKeyPressed;
		
	}

	public void setCharacterPressed(char chCharacterPressed) 
	{
		this.chCharacterPressed = chCharacterPressed;
		
	}

	public PIA6820(RAM activeRAM , Monitor activeScreen) throws InterruptedException 
	{
		Memory = activeRAM;
		Screen = activeScreen;

	}
	
	public void resetPIA() 
	{
		Memory.resetLocation(shKBD);
		Memory.resetLocation(shKBDCR);
		Memory.resetLocation(shDSP);
		Memory.resetLocation(shDSPCR);
		
	}

//	public void debug()
//	{
//		System.out.println("0xD010 - KBD:	" + Integer.toBinaryString(Memory.read(Short.toUnsignedInt(shKBD))) + "   :   " + Integer.toHexString(Memory.read(Short.toUnsignedInt(shKBD))) + "   :   " + (Memory.read(Short.toUnsignedInt(shKBD))));
//		System.out.println("0xD011 - KBDCR:	" + Integer.toBinaryString(Memory.read(Short.toUnsignedInt(shKBDCR))) + "   :   " + Integer.toHexString(Memory.read(Short.toUnsignedInt(shKBDCR))) + "   :   " + (Memory.read(Short.toUnsignedInt(shKBDCR))));
//		System.out.println("0xD012 - DSP:	" + Integer.toBinaryString(Memory.read(Short.toUnsignedInt(shDSP))) + "   :   " + Integer.toHexString(Memory.read(Short.toUnsignedInt(shDSP))) + "   :   " + (Memory.read(Short.toUnsignedInt(shDSP))));
//		System.out.println("0xD013 - DSPCR:	" + Integer.toBinaryString(Memory.read(Short.toUnsignedInt(shDSPCR))) + "   :   " + Integer.toHexString(Memory.read(Short.toUnsignedInt(shDSPCR))) + "   :   " + (Memory.read(Short.toUnsignedInt(shDSPCR))));
//
//	}

	public void refreshPeripherals() throws InterruptedException
	{
		//if(Screen != null && Screen.getIsResetted() == true && CPU.getisRunning() == true && Memory != null) 
		//if(Screen.getCleared() == true && CPU.getisRunning() == true) 
		if(Screen.getCleared() == true) 
		{	
			refreshKeyboard();
			refreshDisplay();

		}

	}
	
	public void refreshKeyboard() 
	{
//		if(bKeyPressed == true && (Memory.read(shKBDCR) & 0b10000000) == 0b00000000)
		if(bKeyPressed == true)
		{
			Memory.write(shKBD , (byte) 0b100000000);
			Memory.write(shKBDCR , (byte) 0b10000000);

			byte byASCIIValue = (byte) Character.toUpperCase(chCharacterPressed);

			//ASCII conversion for carriage return and delete keys
			if(byASCIIValue == 0x0A) byASCIIValue = (byte) 0x0D;
			if(byASCIIValue == 0x08 || byASCIIValue == 0x7F) byASCIIValue = (byte) 0b01011111;

			//System.out.println("Key Typed: '" + chCharacterPressed + "' (ASCII value: " + Byte.toUnsignedInt(byASCIIValue) + ")");

			Memory.write(shKBD , (byte) (byASCIIValue + 0b10000000));
			
			bKeyPressed = false;

		}
		
	}
	
	public void refreshDisplay() throws InterruptedException 
	{		
		if(Memory.read(shDSP) > 0 && Memory.read(shDSP) != 0b01111111)
		{
			if((Memory.read(shDSP) & 0b01111111) == 0x0D)
			{
				Screen.carriageReturn();

			}
			else
			{
				Screen.drawNextCharacter((byte) (Memory.read(shDSP) & 0b01111111));	

			}
			
			//TimeUnit.MILLISECONDS.sleep((long) 16.7);
			
			//Somewhat of a hardware timer
			//TimeUnit.MILLISECONDS.sleep((long) 4);

			if((Memory.read(shKBDCR) & 0b10000000) == 0b10000000)
			{	
				Memory.resetLocation(shKBDCR);

			}

			Memory.resetLocation(shDSP);
			
		}
		
	}

}
