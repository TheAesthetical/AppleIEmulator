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

	//PIA variables in ram - Check Apple 1 owners manual!
	private short KBD = (short) 0xD010;
	private short KBDCR = (short) 0xD011;
	private short DSP = (short) 0xD012;
	private short DSPCR = (short) 0xD013;

	public PIA(CPU6502 activeCPU , RAM activeRAM , Monitor activeScreen) throws InterruptedException 
	{
		resetPIA();

		initalisePIA(activeCPU , activeRAM , activeScreen);
		
		enableKeyHandlers();

	}

	public void resetPIA() 
	{
		//		Screen.getWindow().removeKeyListener(null);
		//		
		//		Screen.getResetButton().removeActionListener(null);
		//		Screen.getCLSButton().removeActionListener(null);
		//		
		//		Screen.getLF().removeActionListener(null);
		//		Screen.getCR().removeActionListener(null);

		CPU = null;
		Memory = null;
		Screen = null;

	}

	public void initalisePIA(CPU6502 activeCPU , RAM activeRAM , Monitor activeScreen) 
	{
		CPU = activeCPU;
		Memory = activeRAM;
		Screen = activeScreen;

	}

	private void enableKeyHandlers() 
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
				char chCharacterPressed = e.getKeyChar();
				byte iASCIIValue = (byte) chCharacterPressed;

				//System.out.println("Key Typed: '" + chCharacterPressed + "' (ASCII value: " + iASCIIValue + ")");

				if(Screen != null && Screen.getIsResetted() == true) 
				{
					Screen.drawNextCharacter(iASCIIValue);

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

					try 
					{
						CPU.startFetchExecute();

					} 
					catch (InterruptedException e1) 
					{
						e1.printStackTrace();

					}

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