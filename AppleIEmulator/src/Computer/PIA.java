package Computer;


//I AM A NERD++++

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;

import Terminal.Monitor;

public class PIA {

	private CPU6502 CPU;
	private RAM Memory;
	private Monitor Screen;

	//PIA variables in RAM - Check Apple 1 owners manual!
	private short shKBD = (short) 0xD010;
	private short shKBDCR = (short) 0xD011;
	private short shDSP = (short) 0xD012;
	private short shDSPCR = (short) 0xD013;

	public PIA(CPU6502 activeCPU , RAM activeRAM , Monitor activeScreen) throws InterruptedException 
	{
		resetPIA();

		initalisePIA(activeCPU , activeRAM , activeScreen);

		enableKeyHandlers();

	}

	public void resetPIA() 
	{		
		CPU = null;
		Memory = null;
		Screen = null;

	}

	public void initalisePIA(CPU6502 activeCPU , RAM activeRAM , Monitor activeScreen) 
	{
		CPU = activeCPU;
		Memory = activeRAM;
		Screen = activeScreen;

		Memory.write((short) Short.toUnsignedInt(shKBD) , (byte) Byte.toUnsignedInt((byte) (Memory.read(Short.toUnsignedInt(shKBD)) + 0b10000000)));
		
	}

	private void enableKeyHandlers() 
	{
		keyboardHandler();
		switchHandler();
		//otherkeysHandler();

	}
	
	private void debug()
	{
		System.out.println("0xD010 - KBD:	" + Integer.toBinaryString(Memory.read(Short.toUnsignedInt(shKBD))) + "   :   " + Integer.toHexString(Memory.read(Short.toUnsignedInt(shKBD))) + "   :   " + (Memory.read(Short.toUnsignedInt(shKBD))));
		System.out.println("0xD011 - KBDCR:	" + Integer.toBinaryString(Memory.read(Short.toUnsignedInt(shKBDCR))) + "   :   " + Integer.toHexString(Memory.read(Short.toUnsignedInt(shKBDCR))) + "   :   " + (Memory.read(Short.toUnsignedInt(shKBDCR))));
		System.out.println("0xD012 - DSP:	" + Integer.toBinaryString(Memory.read(Short.toUnsignedInt(shDSP))) + "   :   " + Integer.toHexString(Memory.read(Short.toUnsignedInt(shDSP))) + "   :   " + (Memory.read(Short.toUnsignedInt(shDSP))));
		System.out.println("0xD013 - DSPCR:	" + Integer.toBinaryString(Memory.read(Short.toUnsignedInt(shDSPCR))) + "   :   " + Integer.toHexString(Memory.read(Short.toUnsignedInt(shDSPCR))) + "   :   " + (Memory.read(Short.toUnsignedInt(shDSPCR))));
		
	}
	
	public void refreshDisplay() throws InterruptedException
	{
		if((Memory.read(Short.toUnsignedInt(shDSP)) & 0b10000000) == 0b00000000 && (Memory.read(Short.toUnsignedInt(shKBDCR)) & 0b10000000) == 0b10000000)
		//if((Memory.read(Short.toUnsignedInt(shDSP)) & 0b10000000) == 0b00000000)
		{
			 debug();
			
//				//Conversion for escape
//				if(shKBDCR == 0x8A) shKBDCR = (byte) 0x0D;
//				///Conversion for carriage return
//				if(shKBDCR == 0x88) shKBDCR = 0x0F;
						
			if((Memory.read(Short.toUnsignedInt(shDSP)) & 0b01111111) == 0x0D)
			{
				Screen.carriageReturn();
				
			}
			else
			{
				Screen.drawNextCharacter((byte) (Memory.read(Short.toUnsignedInt(shDSP)) & 0b01111111));	
				
			}
			
			TimeUnit.MILLISECONDS.sleep((long) 16.7);
			
			Memory.write((short) Short.toUnsignedInt(shDSP) , (byte) (Memory.read(Short.toUnsignedInt(shDSP)) + 0b10000000));
			
			Memory.write((short) Short.toUnsignedInt(shKBDCR) , (byte) Byte.toUnsignedInt((byte) ((byte) (Memory.read(Short.toUnsignedInt(shKBDCR)) & 0b10000000) - 0b10000000)));
			//Memory.write((short) Short.toUnsignedInt(shKBD) , (byte) ((byte) (Memory.read(Short.toUnsignedInt(shKBD)) & 0b10000000) + 0b10000000));
			Memory.write((short) Short.toUnsignedInt(shKBD) , (byte) 0b10000000);

		}
		
	}

	private void keyboardHandler() 
	{
		Screen.getWindow().addKeyListener(new KeyListener() 
		{
			public void keyTyped(KeyEvent e) 
			{
				if(Screen != null && Screen.getIsResetted() == true && CPU.getisRunning() == true) 
				{							
					Memory.write((short) Short.toUnsignedInt(shKBDCR) , (byte) Byte.toUnsignedInt((byte) (Memory.read(Short.toUnsignedInt(shKBDCR)) + 0b10000000)));
										
//					if((Memory.read(Short.toUnsignedInt(shKBDCR)) & 0b10000000) == 0b00000000)
//					{
						//Memory.write((short) Short.toUnsignedInt(shKBDCR) , (byte) Byte.toUnsignedInt((byte) (Memory.read(Short.toUnsignedInt(shKBDCR)) + 0b10000000)));
						
						char chCharacterPressed = e.getKeyChar();
						byte byASCIIValue = (byte) chCharacterPressed;
										
//						//Conversion for carriage return OG: 0x0D
						if(byASCIIValue == 0x0A) byASCIIValue = (byte) 0x0D;
						//Conversion for backspace OG: 0x0F
						if(byASCIIValue == 0x08) byASCIIValue = (byte) 0x0F;
						
						System.out.println("Key Typed: '" + chCharacterPressed + "' (ASCII value: " + Byte.toUnsignedInt(byASCIIValue) + ")");
						
						Memory.write((short) Short.toUnsignedInt(shKBD) , (byte) (byASCIIValue));
						
						debug();
												
				//	}
					
				}

			}
			public void keyPressed(KeyEvent e) 
			{

			}
			public void keyReleased(KeyEvent e) 
			{

			}

		});

	}

	private void switchHandler() 
	{
		Screen.getResetButton().addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("RESET");

				if(Screen != null && CPU != null)
				{
					Screen.setIsResetted(true);
					Screen.resetMonitor();
					Screen.setCursorActive(true);

					CPU.resetCPU();
					CPU.resetVector();

					CPU.setisRunning(true);

				}

			}

		});

		Screen.getCLSButton().addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(Screen != null)
				{
					Screen.resetMonitor();

				}

			}

		});

	}

//	private void otherkeysHandler()
//	{
//		Screen.getCR().addActionListener(new ActionListener() 
//		{
//			public void actionPerformed(ActionEvent e) 
//			{
//				byte iASCIIValue = 0x0D;
//				
//			}
//
//		});
//		
//		Screen.getLF().addActionListener(new ActionListener() 
//		{
//			public void actionPerformed(ActionEvent e) 
//			{
//				byte iASCIIValue = 0x0A;
//				
//			}
//
//		});
//		
//		Screen.getRO().addActionListener(new ActionListener() 
//		{
//			public void actionPerformed(ActionEvent e) 
//			{
//				byte iASCIIValue = 0x7F;
//				
//			}
//
//		});
//
//	}

}