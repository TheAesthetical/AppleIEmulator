package Computer;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import Terminal.Monitor;

public class ACI extends ROM {
	
	Monitor Screen;
	RAM Memory;
	
	public ACI(int iSizeInBytes , String szFileName , Monitor activeScreen, RAM activeMemory)
	{
		super(iSizeInBytes , szFileName);
		
		Screen = activeScreen;
		Memory = activeMemory;
	
	} 
	
	public void setupInterface()
	{
		
		
	}
	
}
