package Computer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Terminal.Monitor;

public class PIA {

	private CPU6502 CPU;
	private RAM Memory;
	private Monitor Screen;

	public PIA(CPU6502 activeCPU , RAM activeRAM , Monitor activeScreen) throws InterruptedException 
	{
		reset();

		CPU = activeCPU;
		Memory = activeRAM;
		Screen = activeScreen;

	}

	public void reset() 
	{
		CPU = null;
		Memory = null;
		Screen = null;

	}
	
	public void enableHandlers() 
	{
		keyboardHandler();
		
		switchHandler();
		otherkeysHandler();
		
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

	private void switchHandler() 
	{
		Screen.getResetButton().addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("RESET");
				
				Screen.resetMonitor();
				
				CPU.resetCPU();
				CPU.resetPC();
				
				try 
				{
					CPU.startCycle();
					
				} 
				catch (InterruptedException e1) 
				{
					e1.printStackTrace();
					
				}

			}

		});

		Screen.getCLSButton().addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Screen.resetMonitor();

			}

		});

	}

	private void otherkeysHandler()
	{
		Screen.getLF().addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{

			}

		});

		Screen.getCR().addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{

			}

		});
		
	}

}