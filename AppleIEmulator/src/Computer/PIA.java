package Computer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Terminal.Monitor;

public class PIA {
	
	private CPU6502 CPU;
	private Monitor Screen;

	public PIA(CPU6502 activeCPU, Monitor activeScreen) throws InterruptedException 
	{
		CPU = activeCPU;
		Screen = activeScreen;
		
		keyboardHandler();

	}
	
	private void keyboardHandler() 
	{
	Screen.getWindow().addKeyListener(new KeyListener() 
	{
		public void keyTyped(KeyEvent e) 
		{
			boolean bValidChar = true;
			
			char chCharacterPressed = e.getKeyChar();
			byte iASCIIValue = (byte) chCharacterPressed;
			
			//System.out.println("Key Typed: '" + chCharacterPressed + "' (ASCII value: " + iASCIIValue + ")");
			
			if(iASCIIValue == 27)
			{
				resetButton();
				bValidChar = false;
				
			}
//			else if(iASCIIValue < 32)
//			{
//				iASCIIValue = 32;
//				
//			}
//			else if(iASCIIValue > 95)
//			{
//				iASCIIValue = 95;
//				
//			}
			
			if(bValidChar == true) Screen.drawNextCharacter(Byte.toUnsignedInt(iASCIIValue));
			
		}

		public void keyPressed(KeyEvent e) 
		{
			
		}

		public void keyReleased(KeyEvent e) 
		{
			
		}

	});
	
	}
	
	private void resetButton()
	{
		Screen.resetMonitor();
		CPU.resetCPU();
		
	}
	
}