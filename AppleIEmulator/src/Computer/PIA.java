package Computer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Terminal.Monitor;

public class PIA {

	public PIA(CPU6502 CPU, Monitor Screen) 
	{
		keyboardHandler(Screen);

	}
	
	private void keyboardHandler(Monitor Screen) 
	{
	Screen.getWindow().addKeyListener(new KeyListener() {
		
		public void keyTyped(KeyEvent e) {
			char chCharacterPressed = e.getKeyChar();
			int iASCIIValue = (int) chCharacterPressed;
			
			//System.out.println("Key Typed: '" + chCharacterPressed + "' (ASCII value: " + iASCIIValue + ")");
			
			try 
			{
				Screen.drawNextCharacter(iASCIIValue);
				
			} 
			catch (InterruptedException e1) 
			{
				e1.printStackTrace();
				
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
}