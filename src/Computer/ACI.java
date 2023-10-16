package Computer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import Terminal.Monitor;

public class ACI extends ROM {
	
	private RAM Memory;
	private CPU6502 CPU;
	
	private ROM IntegerBASIC = new ROM(12 , "INTEGERBASIC");
	private ROM[] Cassettes = new ROM[2];
	private ROM CurrentCassette;
	
	private short shCassetteLocation;
	private int iCassetteByteSize;
	
	JFileChooser FileUI;
	FileNameExtensionFilter BinaryFileFilter;
	
	public ACI(int iSizeInBytes , String szFileName , RAM activeMemory , CPU6502 activeCPU)
	{
		super(iSizeInBytes , szFileName);
			
		resetACI();
		initaliseACI(activeMemory , activeCPU);
		
		Memory.bootstrapROMS(getROM() , (short) 0xC100);
		
		//enableStorageHandlers();
		
	} 
	
	public void resetACI() 
	{		
		Memory = null;
		CPU = null;

	}

	public void initaliseACI(RAM activeMemory , CPU6502 activeCPU) 
	{
		Memory = activeMemory;
		CPU = activeCPU;
		
	}
	
//	public void enableStorageHandlers()
//	{
//		Screen.getLoadCassette().setVisible(true);
//		Screen.getClearCassette().setVisible(false);
//		
//		clearCassetteListener();
//		loadCassetteListener();
//		 
//	}
//	
//	public void clearCassetteListener()
//	{
//		Screen.getClearCassette().addActionListener( new ActionListener() 
//		{
//			public void actionPerformed(ActionEvent e) 
//			{
//				Screen.getLoadCassette().setVisible(true);
//				Screen.getClearCassette().setVisible(false);
//				
//				for (int i = Short.toUnsignedInt(shCassetteLocation); i <= (Short.toUnsignedInt(shCassetteLocation) + CurrentCassette.getROM().length); i++) 
//				{
//					Memory.write((short) i, (byte) 0x00);
//					
//				}
//				
//				CPU.resetVector();
//				
////				for (int i = 0xE000; i <= 0xEFFF; i++) 
////				{
////					System.out.println(Integer.toHexString(i).toUpperCase() + " : " + Integer.toHexString(Memory.read(i)).toUpperCase());
////					
////				}
//				
//			}
//
//		});
//		
//	}
//	
//	public void loadCassetteListener()
//	{
//		Screen.getIntegerBASIC().addActionListener( new ActionListener() 
//		{
//			public void actionPerformed(ActionEvent e) 
//			{
//				CurrentCassette = IntegerBASIC;
//				shCassetteLocation = (short) 0xE000;
//				
//				Screen.getLoadCassette().setVisible(false);
//				Screen.getClearCassette().setVisible(true);
//								
//				Memory.bootstrapROMS(IntegerBASIC.getROM() , shCassetteLocation);
//				
////				for (int i = Short.toUnsignedInt(shCassetteLocation); i <= (Short.toUnsignedInt(shCassetteLocation) + CurrentCassette.getROM().length); i++) 
////				{
////					System.out.println(Integer.toHexString(i).toUpperCase() + " : " + Integer.toHexString(Memory.read(i)).toUpperCase());
////					
////				}
//				
//			}
//
//		});
//		
//		Screen.getOther().addActionListener( new ActionListener() 
//		{
//			public void actionPerformed(ActionEvent e) 
//			{ 
//				FileUI = new JFileChooser();
//				FileUI.setAcceptAllFileFilterUsed(false);
//				BinaryFileFilter = new FileNameExtensionFilter("BIN" , "bin");
//				FileUI.addChoosableFileFilter(BinaryFileFilter);
//				FileUI.showOpenDialog(Screen.getWindow());
//				
//				if (FileUI.getSelectedFile() != null)
//				{
//					short shBootstrapLocation = Short.parseShort(FileUI.getName().substring(0, 4));
//					
//					ROM ChosenFile = new ROM(16 , FileUI.getName());
//
//					
//					
//					
//					Memory.bootstrapROMS(ChosenFile.getROM() , shBootstrapLocation);
//				}
//				
//			}
//
//		});
//		
	
	
}
