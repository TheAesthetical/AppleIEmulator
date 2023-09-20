package Computer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import Terminal.Monitor;

public class ACI extends ROM {
	
	Monitor Screen;
	RAM Memory;
	
	public ACI(int iSizeInBytes , String szFileName , Monitor activeScreen , RAM activeMemory)
	{
		super(iSizeInBytes , szFileName);
			
		resetACI();
		
		initaliseACI(activeScreen , activeMemory);
		
		Memory.bootstrapROMS(getROM() , (short) 0xC100);
		
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
	
	public void clearCassetteListener()
	{
		Screen.getClearCassette().addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				
				
			}

		});
		
	}
	
	public void loadCassetteListener()
	{
		Screen.getIntegerBASIC().addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				
				
			}

		});
		
		Screen.getOther().addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				
				
			}

		});
		
	}
	
}
