package Main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import Computer.*;
import Terminal.Monitor;

public class Apple1 extends JPanel{

	private static final long serialVersionUID = 1L;

	//Calculated dimensions of the Apple 1 Terminal
	private final int iGridWidth = 240;
	private final int iGridHeight = 192;

	private Utilities Utils = new Utilities();

	private ImageIcon WindowFavicon = new ImageIcon(Utils.getDirectoryName() + "\\windowfavicon.png");

	private JMenuItem OnButton;
	private JMenuItem OffButton;

	private JMenuItem ResetButton;
	private JMenuItem CLSButton;

	private JMenuItem IntegerBASIC;
	private JMenuItem Apple30th;
	private JMenuItem Microchess;
	private JMenuItem LunarLander;
	private JMenuItem Codebreaker;
	
	private JMenuItem OtherFileButton;
	private JMenuItem MenuSaveButton;

	private File SaveFile;
	private JFileChooser FileUI;
	private FileNameExtensionFilter BinaryFileFilter;

	private JFrame SaveInterfaceFrame;
	private JTextField StartLocationInput;
	private JTextField EndLocationInput;
	private JTextField BootstrapLocationInput;
	private JButton SaveButton;

	private JLabel StartLocation;
	private JLabel EndLocation;
	private JLabel BootstrapLocation;

	private Monitor Screen;
	private RAM Memory;
	private CPU6502 CPU;
	private PIA6820 PIA;
	private ROM StorageROM;
	private ACI Storage;

	private boolean bEmulatorPaused;
	private boolean bEmulatorRunning;

	private Thread EmulatorThread;

	public Apple1(int iMonitorScale) throws InterruptedException
	{
		Screen = new Monitor(iGridWidth , iGridHeight , iMonitorScale);
		Screen.setCursorIndex((byte) 64);
		Screen.setCursorSleepTimeMS(333);

		createMainGUI("Apple 1 Emulator");

		Memory = new RAM(65536);
		StorageROM = new ROM(256 , "ROM.bin");

		CPU = new CPU6502(Memory);

		Storage = new ACI(256 , "ACI.bin" , Memory);
		PIA = new PIA6820(Memory , Screen);

		startEmulator();

	}

	private void startEmulator()
	{
		Screen.getScreen().setVisible(true);

		OnButton.setVisible(true);
		OffButton.setVisible(false);

		OnButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (bEmulatorRunning == false)
				{
					EmulatorThread = new Thread(MainPowerOn);
					bEmulatorPaused = false;
					bEmulatorRunning = true;
					EmulatorThread.start();

				}

				OnButton.setVisible(false);
				OffButton.setVisible(true);

			}

		});

		OffButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{			
				bEmulatorRunning = false;

				Screen.setCleared(true);
				Screen.setCursorActive(false);
				Screen.resetMonitor();

				Memory.resetRAM();
				CPU.resetCPU();
				CPU.setRunning(false);

				OnButton.setVisible(true);
				OffButton.setVisible(false);

			}

		});

		Screen.getScreen().addKeyListener(new KeyListener() 
		{
			public void keyTyped(KeyEvent e) 
			{				
				if(bEmulatorPaused == false)
				{		
					PIA.setKeyPressed(true);
					PIA.setCharacterPressed(e.getKeyChar());

				}

			}
			public void keyPressed(KeyEvent e) 
			{

			}
			public void keyReleased(KeyEvent e) 
			{

			}

		});

		ResetButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(bEmulatorPaused == false && bEmulatorRunning == true)
				{
					Screen.resetMonitor();
					Screen.setCursorActive(true);

					CPU.resetCPU();
					CPU.resetVector();

					Screen.setCleared(true);
					CPU.setRunning(true);

				}

			}

		});

		CLSButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(bEmulatorPaused == false && bEmulatorRunning == true)				
				{
					Screen.resetMonitor();
					Screen.setCleared(true);
					Screen.setCursorActive(true);

				}

			}

		});

		MenuSaveButton.addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(bEmulatorPaused == false && bEmulatorRunning == true)				
				{
					saveToStorage();

				}

			}

		});

		IntegerBASIC.addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{	
				if(bEmulatorPaused == false && bEmulatorRunning == true)				
				{
					Storage.loadFileCassette("E000INTEGERBASIC.bin" , 4096 , (short) 0xE000);

					JOptionPane.showMessageDialog(Screen.getScreen() , "Integer BASIC bootstrapped to memory at address E000");

				}

			}

		});
		
		Apple30th.addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{	
				if(bEmulatorPaused == false && bEmulatorRunning == true)				
				{
					Storage.loadFileCassette("0280APPLE30TH.bin" , 3456 , (short) 0x0280);

					JOptionPane.showMessageDialog(Screen.getScreen() , "Apple's 30th bootstrapped to memory at address 0280");

				}

			}

		});
		
		Microchess.addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{	
				if(bEmulatorPaused == false && bEmulatorRunning == true)				
				{
					Storage.loadFileCassette("0300MICROCHESS.bin" , 2248 , (short) 0x0300);

					JOptionPane.showMessageDialog(Screen.getScreen() , "Microchess bootstrapped to memory at address 0300");

				}

			}

		});
		
		LunarLander.addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{	
				if(bEmulatorPaused == false && bEmulatorRunning == true)				
				{
					Storage.loadFileCassette("0300LUNARLANDER.bin" , 1721 , (short) 0x0300);

					JOptionPane.showMessageDialog(Screen.getScreen() , "Lunar Lander bootstrapped to memory at address 0300");

				}

			}

		});
		
		Codebreaker.addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{	
				if(bEmulatorPaused == false && bEmulatorRunning == true)				
				{
					Storage.loadFileCassette("0280CODEBREAKER1.bin" , 2432 , (short) 0x0280);
					Storage.loadFileCassette("E000CODEBREAKER2.bin" , 4096 , (short) 0xE000);

					JOptionPane.showMessageDialog(Screen.getScreen() , "Codebreaker bootstrapped to memory at address 0280");

				}

			}

		});

		OtherFileButton.addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{ 
				if(bEmulatorPaused == false && bEmulatorRunning == true)				
				{
					FileUI = new JFileChooser();

					FileUI.setAcceptAllFileFilterUsed(false);
					BinaryFileFilter = new FileNameExtensionFilter("BIN" , "bin");
					FileUI.addChoosableFileFilter(BinaryFileFilter);

					FileUI.showOpenDialog(Screen.getParent());

					if (FileUI.getSelectedFile() != null)
					{
						String szInvalidBootstrapLocation = FileUI.getSelectedFile().getName().substring(0, 4);

						if(szInvalidBootstrapLocation.matches("^[0-9a-fA-F]{4}$") == true)
						{
							short shBootstrapLocation = (short) Integer.parseInt(szInvalidBootstrapLocation , 16);

							Storage.loadFileCassette(FileUI.getSelectedFile().getName() , (int) FileUI.getSelectedFile().length() , shBootstrapLocation);

							JOptionPane.showMessageDialog(Screen.getScreen() , FileUI.getSelectedFile().getName() + " bootstrapped to memory at address " + szInvalidBootstrapLocation);

						}
						else
						{
							JOptionPane.showMessageDialog(Screen.getScreen() , "Error in bootstrapping file!\nThe file name needs to start with the memory address to bootstrap to as a 4 character hexadecimal address");

						}

					}

				}
			}

		});

	}

	private void saveToStorage()
	{
		createSaveGUI();

		SaveButton.addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{ 
				if(bEmulatorPaused == false && bEmulatorRunning == true)				
				{
					String szStartAddress = StartLocationInput.getText();
					String szEndAddress = EndLocationInput.getText();
					String szBootLocation = BootstrapLocationInput.getText();

					if(szStartAddress.matches("^[0-9a-fA-F]{4}$") == true && szEndAddress.matches("^[0-9a-fA-F]{4}$") == true && szBootLocation.matches("^[0-9a-fA-F]{4}$"))
					{
						short shStartAddressValid = (short) Integer.parseInt(szStartAddress , 16);
						short shEndAddressValid = (short) Integer.parseInt(szEndAddress , 16);

						if(Short.toUnsignedInt(shStartAddressValid) <= Short.toUnsignedInt(shEndAddressValid))
						{      
							SaveInterfaceFrame.setVisible(false);

							FileUI = new JFileChooser();
							FileUI.setAcceptAllFileFilterUsed(false);
							BinaryFileFilter = new FileNameExtensionFilter("BIN" , "bin");
							FileUI.addChoosableFileFilter(BinaryFileFilter);
							FileUI.showSaveDialog(Screen.getParent());

							if (FileUI.getSelectedFile() != null)
							{
								SaveFile = new File(szBootLocation + FileUI.getSelectedFile().getName() + ".bin");

								try (FileOutputStream ByteStream = new FileOutputStream(SaveFile)) 
								{	
									ByteStream.write(Storage.getMemoryByteStream(shStartAddressValid , shEndAddressValid));

									JOptionPane.showMessageDialog(SaveInterfaceFrame , "Memory addresses saved successfully!\nTo file: " + SaveFile + ".bin");

								} 
								catch (IOException e1) 
								{
									JOptionPane.showMessageDialog(SaveInterfaceFrame , "File error!\n" + e1.getMessage());
									SaveInterfaceFrame.setVisible(false);
									
								}

							}
							else
							{
								JOptionPane.showMessageDialog(SaveInterfaceFrame , "Error!\nYou need to select a save file to");
								SaveInterfaceFrame.setVisible(false);

							}

						}
						else
						{
							JOptionPane.showMessageDialog(SaveInterfaceFrame , "Error!\nThe start address is bigger than the end address");
							SaveInterfaceFrame.setVisible(false);

						}

					}
					else
					{
						JOptionPane.showMessageDialog(SaveInterfaceFrame , "Error!\nPlease make sure you are writing the addresses as a 4 digit hexadecimal number\neg. E000 or 0280");
						SaveInterfaceFrame.setVisible(false);

					}

				}

			}

		});

	}

	private Runnable MainPowerOn = () ->
	{
		if (bEmulatorPaused == false)
		{
			try 
			{
				initaliseMemory();
				Screen.setCleared(false);
				Screen.powerOnMonitor();

			} 
			catch (InterruptedException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();

			}

		}

		if(CPU.getRunning() == true || Screen.getCleared() == true)
		{

			while (bEmulatorPaused == false && bEmulatorRunning == true )
			{
				long lPreCycleTime = System.nanoTime();
				
				try 
				{	

					CPU.executeInstruction();
					
					PIA.refreshPeripherals();

					long lPostCycleTime = System.nanoTime();

					long lCycleTime = (lPostCycleTime - lPreCycleTime) / 1000;

					TimeUnit.MICROSECONDS.sleep(CPU.getCurrentClockCycles() - lCycleTime);

					//Thread.sleep(400);
					//Thread.sleep(200);

				}
				catch (InterruptedException e) 
				{

				}



			} while (bEmulatorRunning == true);

		}

	};

	private void initaliseMemory()
	{
		Memory.bootstrapROM(StorageROM.getROM() , (short) 0xFF00);

	}

	private void createMainGUI(String szWindowTitle) throws InterruptedException 
	{
		Screen.getScreen().setTitle(szWindowTitle);

		Screen.getScreen().setIconImage(WindowFavicon.getImage());

		Screen.getScreen().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Screen.getScreen().setResizable(false);

		JMenuBar MenuBar = new JMenuBar();
		MenuBar.setSize(iGridWidth , 10);

		JMenu PowerOptions = new JMenu("Power");

		OnButton = new JMenuItem("On");
		OffButton = new JMenuItem("Off");

		PowerOptions.add(OnButton);
		PowerOptions.add(OffButton);

		MenuBar.add(PowerOptions);

		JMenu SwitchOptions = new JMenu("Switches");

		ResetButton = new JMenuItem("RESET");
		CLSButton = new JMenuItem("CLS");

		SwitchOptions.add(ResetButton);
		SwitchOptions.add(CLSButton);

		MenuBar.add(SwitchOptions);

		JMenu ACInterface = new JMenu("ACI");

		JMenu LoadDropdown = new JMenu("Load...");
		IntegerBASIC = new JMenuItem("Integer BASIC");
		Apple30th = new JMenuItem("Apple's 30th");
		Microchess = new JMenuItem("Microchess");
		LunarLander = new JMenuItem("Lunar Lander");
		Codebreaker = new JMenuItem("Codebreaker");
		
		OtherFileButton = new JMenuItem("Other...");

		LoadDropdown.add(IntegerBASIC);
		LoadDropdown.add(Apple30th);
		LoadDropdown.add(Microchess);
		LoadDropdown.add(LunarLander);
		LoadDropdown.add(Codebreaker);
		
		LoadDropdown.add(OtherFileButton);

		MenuSaveButton = new JMenuItem("Save...");

		ACInterface.add(LoadDropdown);
		ACInterface.add(MenuSaveButton);

		MenuBar.add(ACInterface);

		Screen.getScreen().add(MenuBar , BorderLayout.SOUTH);

		Screen.getScreen().pack();
		Screen.getScreen().setVisible(false);

	}

	private void createSaveGUI() 
	{
		SaveInterfaceFrame = new JFrame("Save Memory to File");
		SaveInterfaceFrame.setLayout(new GridLayout(4 , 2));

		StartLocation = new JLabel("Start Address: ");
		StartLocationInput = new JTextField();

		EndLocation = new JLabel("End Address: ");
		EndLocationInput = new JTextField();

		BootstrapLocation = new JLabel("Bootstrap to Memory At: ");
		BootstrapLocationInput = new JTextField();

		SaveButton = new JButton("Ok");

		SaveInterfaceFrame.add(StartLocation);
		SaveInterfaceFrame.add(StartLocationInput);

		SaveInterfaceFrame.add(EndLocation);
		SaveInterfaceFrame.add(EndLocationInput);

		SaveInterfaceFrame.add(BootstrapLocation);
		SaveInterfaceFrame.add(BootstrapLocationInput);

		SaveInterfaceFrame.add(new JLabel());
		SaveInterfaceFrame.add(SaveButton);

		SaveInterfaceFrame.setSize(350, 150);
		SaveInterfaceFrame.setResizable(false);
		SaveInterfaceFrame.setIconImage(WindowFavicon.getImage());

		SaveInterfaceFrame.setVisible(true);

	}

}
