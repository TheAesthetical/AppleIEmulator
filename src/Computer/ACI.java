package Computer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import Terminal.Monitor;

public class ACI extends ROM {
	
	private Monitor Screen;
	private RAM Memory;
	
	private ROM IntegerBASIC = new ROM(12 , "INTEGERBASIC");
	private ROM CurrentCassette;
	
	private short shCassetteLocation;
	private int iCassetteByteSize;
	
	public ACI(int iSizeInBytes , String szFileName , Monitor activeScreen , RAM activeMemory)
	{
		super(iSizeInBytes , szFileName);
			
		resetACI();
		
		initaliseACI(activeScreen , activeMemory);
		
		Memory.bootstrapROMS(getROM() , (short) 0xC100);
		
		enableStorageHandlers();
		
	} 
	
	public void resetACI() 
	{		
		Memory = null;
		Screen = null;

	}

	public void initaliseACI(Monitor activeScreen , RAM activeMemory) 
	{
		Screen = activeScreen;
		Memory = activeMemory;
		
	}
	
	public void enableStorageHandlers()
	{
		Screen.getLoadCassette().setVisible(true);
		Screen.getClearCassette().setVisible(false);
		
		clearCassetteListener();
		loadCassetteListener();
		 
	}
	
	public void clearCassetteListener()
	{
		Screen.getClearCassette().addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Screen.getLoadCassette().setVisible(true);
				Screen.getClearCassette().setVisible(false);
				
				for (int i = shCassetteLocation; i <= IntegerBASIC.getROM().length; i++) 
				{
					Memory.write((short) i, (byte) 0x00);
					
				}
				
				for (int i = 0xE000; i <= 0xEFFF; i++) 
				{
					System.out.println(Integer.toHexString(i).toUpperCase() + " : " + Integer.toHexString(Memory.read(i)).toUpperCase());
					
				}
				
			}

		});
		
	}
	
	public void loadCassetteListener()
	{
		Screen.getIntegerBASIC().addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				shCassetteLocation = (short) 0xE000;
				
				System.out.println("p");
				Screen.getLoadCassette().setVisible(false);
				Screen.getClearCassette().setVisible(true);
				System.out.println("p");
				
				Memory.bootstrapROMS(IntegerBASIC.getROM() , shCassetteLocation);
				System.out.println("s");
				
				for (int i = 0xE000; i <= 0xEFFF; i++) 
				{
					System.out.println(Integer.toHexString(i).toUpperCase() + " : " + Integer.toHexString(Memory.read(i)).toUpperCase());
					
				}
				
			}

		});
		
		Screen.getOther().addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				//i dont know if i will use this? if time allows it
				
			}

		});
		
	}
	
}
