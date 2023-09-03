package Computer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Terminal.Monitor;

public class PIA {

	public PIA(CPU6502 CPU, Monitor Screen) throws InterruptedException 
	{
		keyboardHandler(Screen);

	}
	
	private void keyboardHandler(Monitor Screen) 
	{
	Screen.getWindow().addKeyListener(new KeyListener() 
	{
		public void keyTyped(KeyEvent e) 
		{
			char chCharacterPressed = e.getKeyChar();
			byte iASCIIValue = (byte) chCharacterPressed;
			
			//System.out.println("Key Typed: '" + chCharacterPressed + "' (ASCII value: " + iASCIIValue + ")");
			
			if(iASCIIValue < 32)
			{
				iASCIIValue = 32;
				
			}
			
			if(iASCIIValue > 95)
			{
				iASCIIValue = 95;
				
			}
			
			Screen.drawNextCharacter(Byte.toUnsignedInt(iASCIIValue));
			
		}

		public void keyPressed(KeyEvent e) 
		{
			
		}

		public void keyReleased(KeyEvent e) 
		{
			
		}

	});
	
	}
}