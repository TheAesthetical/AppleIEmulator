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

	private JMenuItem On;
	private JMenuItem Off;

	private JMenuItem ResetButton;
	private JMenuItem CLSButton;

	private JMenu LoadCassette;
	private JMenuItem IntegerBASIC;
	private JMenuItem Other;
	private JMenuItem SaveCassette;

	private File SaveFile;
	private JFileChooser FileUI;
	private FileNameExtensionFilter BinaryFileFilter;

	private JFrame SaveFrame;
	private JTextField StartAddress;
	private JTextField EndAddress;
	private JTextField BootstrapAddress;
	private JButton saveButton;

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
	private boolean bEmulatorRun;

	private Thread MainEmulatorThread;

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
		PIA = new PIA6820(CPU , Memory , Screen);

		emulate();

	}

	private void emulate()
	{
		Screen.getScreen().setVisible(true);

		On.setVisible(true);
		Off.setVisible(false);

		On.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("Power ON pressed!");

				if (bEmulatorRun == false)
				{
					initaliseMemory();

					MainEmulatorThread = new Thread(MainPowerOn);
					bEmulatorPaused = false;
					bEmulatorRun = true;
					MainEmulatorThread.start();

				}
				else
				{
					System.err.println("EMULATOR ALREADY RUNNING!");
				}

				On.setVisible(false);
				Off.setVisible(true);

			}

		});

		Off.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{			
				System.out.println("Power OFF pressed!");

				bEmulatorRun = false;

				Screen.setCleared(false);
				Screen.setCursorActive(false);
				Screen.resetMonitor();

				Memory.resetMemory();
				CPU.resetCPU();
				CPU.setisRunning(false);

				On.setVisible(true);
				Off.setVisible(false);

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
				if(bEmulatorPaused == false && bEmulatorRun == true)
				{
					System.out.println("RESET");

					Screen.resetMonitor();
					Screen.setCursorActive(true);

					CPU.resetCPU();
					CPU.resetVector();

					Screen.setCleared(true);
					CPU.setisRunning(true);

				}

			}

		});

		CLSButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(bEmulatorPaused == false && bEmulatorRun == true)				
				{
					System.out.println("CLEAR SCREEN");

					Screen.resetMonitor();
					Screen.setCleared(true);
					Screen.setCursorActive(true);

				}

			}

		});

		SaveCassette.addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(bEmulatorPaused == false && bEmulatorRun == true)				
				{
					saveToStorage();

				}

			}

		});

		IntegerBASIC.addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{	
				if(bEmulatorPaused == false && bEmulatorRun == true)				
				{
					Storage.loadCassette(1 , (short) 0xE000);

					JOptionPane.showMessageDialog(Screen.getScreen() , "Integer BASIC bootstrapped to memory at address E000");

				}

			}

		});

		Other.addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{ 
				if(bEmulatorPaused == false && bEmulatorRun == true)				
				{
					FileUI = new JFileChooser();

					FileUI.setAcceptAllFileFilterUsed(false);
					BinaryFileFilter = new FileNameExtensionFilter("BIN" , "bin");
					FileUI.addChoosableFileFilter(BinaryFileFilter);

					FileUI.showOpenDialog(Screen.getParent());

					if (FileUI.getSelectedFile() != null)
					{
						String szInvalidBootstrapLocation = FileUI.getSelectedFile().getName().substring(0, 4);

						if(Utils.Validate("^[0-9a-fA-F]{4}$" , szInvalidBootstrapLocation) == true)
						{

							short shBootstrapLocation = (short) Integer.parseInt(szInvalidBootstrapLocation , 16);

							Storage.loadFile(FileUI.getSelectedFile().getName() , (int) FileUI.getSelectedFile().length() , shBootstrapLocation);

							JOptionPane.showMessageDialog(Screen.getScreen() , FileUI.getSelectedFile().getName() + " bootstrapped to memory at address " + FileUI.getSelectedFile().getName().substring(0, 4));

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

		saveButton.addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{ 
				if(bEmulatorPaused == false && bEmulatorRun == true)				
				{
					String szStartAddress = StartAddress.getText();
					String szEndAddress = EndAddress.getText();
					String szBootLocation = BootstrapAddress.getText();

					if(Utils.Validate("^[0-9a-fA-F]{4}$" , szStartAddress) == true && Utils.Validate("^[0-9a-fA-F]{4}$" , szEndAddress) == true && Utils.Validate("^[0-9a-fA-F]{4}$" , szBootLocation) == true )
					{
						short shStartAddressValid = (short) Integer.parseInt(szStartAddress , 16);
						short shEndAddressValid = (short) Integer.parseInt(szEndAddress , 16);

						if(Short.toUnsignedInt(shStartAddressValid) <= Short.toUnsignedInt(shEndAddressValid))
						{      
							SaveFrame.setVisible(false);

							FileUI = new JFileChooser();
							FileUI.setAcceptAllFileFilterUsed(false);
							BinaryFileFilter = new FileNameExtensionFilter("BIN" , "bin");
							FileUI.addChoosableFileFilter(BinaryFileFilter);
							FileUI.showSaveDialog(Screen.getParent());

							if (FileUI.getSelectedFile() != null)
							{
								try (FileOutputStream ByteStream = new FileOutputStream(FileUI.getSelectedFile())) 
								{	
									ByteStream.write(Storage.getByteStream(shStartAddressValid , shEndAddressValid));

									SaveFile = new File(szBootLocation + FileUI.getSelectedFile().getName());
									FileUI.getSelectedFile().renameTo(SaveFile);

									JOptionPane.showMessageDialog(SaveFrame , "Memory addresses saved successfully!\nTo file: " + SaveFile + ".bin");

								} 
								catch (IOException e1) 
								{
									e1.printStackTrace();
									System.err.println("Error writing bytes to file: " + e1.getMessage());

								}

							}
							else
							{
								JOptionPane.showMessageDialog(SaveFrame , "Error!\nAddresses not saved successfully");
								SaveFrame.setVisible(false);

							}

						}
						else
						{
							JOptionPane.showMessageDialog(SaveFrame , "Error!\nAddresses not saved successfully");
							SaveFrame.setVisible(false);

						}

					}
					else
					{
						JOptionPane.showMessageDialog(SaveFrame , "Error!\nAddresses not saved successfully");
						SaveFrame.setVisible(false);

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
				Screen.powerOnMonitor();

			} 
			catch (InterruptedException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();

			}

		}

		while (bEmulatorPaused == false && bEmulatorRun == true )
		{
			if(CPU.getisRunning() == true || Screen.getCleared() == true)
			{
				try 
				{	
					long lPreCycleTime = System.nanoTime();

					CPU.executeInstruction();

					long lPostCycleTime = System.nanoTime();

					long lCycleTime = (lPostCycleTime - lPreCycleTime) / 1000;

					TimeUnit.MICROSECONDS.sleep(CPU.getCurrentClockCycles() - lCycleTime);

					//Thread.sleep(400);
					//Thread.sleep(200);

					PIA.refresh();

				}
				catch (InterruptedException e) 
				{

				}

			}

		} while (bEmulatorRun == true);
			
		

	};

	private void initaliseMemory()
	{
		Memory.bootstrapROMS(StorageROM.getROM() , (short) 0xFF00);

	}


	private void createMainGUI(String szWindowTitle) throws InterruptedException 
	{
		Screen.getScreen().setTitle(szWindowTitle);

		Screen.getScreen().setIconImage(WindowFavicon.getImage());

		Screen.getScreen().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Screen.getScreen().setResizable(false);

		JMenuBar MenuBar = new JMenuBar();
		MenuBar.setSize(iGridWidth , 10);

		JMenu PowerSwitch = new JMenu("Power");

		On = new JMenuItem("On");
		Off = new JMenuItem("Off");

		PowerSwitch.add(On);
		PowerSwitch.add(Off);

		MenuBar.add(PowerSwitch);

		JMenu Switches = new JMenu("Switches");

		ResetButton = new JMenuItem("RESET");
		CLSButton = new JMenuItem("CLS");

		Switches.add(ResetButton);
		Switches.add(CLSButton);

		MenuBar.add(Switches);

		JMenu ACIInterface = new JMenu("ACI");

		LoadCassette = new JMenu("Load...");
		IntegerBASIC = new JMenuItem("Integer BASIC");
		Other = new JMenuItem("Other...");

		LoadCassette.add(IntegerBASIC);
		LoadCassette.add(Other);

		SaveCassette = new JMenuItem("Save...");

		ACIInterface.add(LoadCassette);
		ACIInterface.add(SaveCassette);

		MenuBar.add(ACIInterface);

		Screen.getScreen().add(MenuBar , BorderLayout.SOUTH);

		Screen.getScreen().pack();
		Screen.getScreen().setVisible(false);

	}

	private void createSaveGUI() 
	{
		SaveFrame = new JFrame("Save Memory to File");
		SaveFrame.setLayout(new GridLayout(4 , 2));

		StartLocation = new JLabel("Start Address: ");
		StartAddress = new JTextField();

		EndLocation = new JLabel("End Address: ");
		EndAddress = new JTextField();

		BootstrapLocation = new JLabel("Bootstrap to Memory At: ");
		BootstrapAddress = new JTextField();

		saveButton = new JButton("Ok");

		SaveFrame.add(StartLocation);
		SaveFrame.add(StartAddress);

		SaveFrame.add(EndLocation);
		SaveFrame.add(EndAddress);

		SaveFrame.add(BootstrapLocation);
		SaveFrame.add(BootstrapAddress);

		SaveFrame.add(new JLabel());
		SaveFrame.add(saveButton);

		SaveFrame.setSize(350, 150);
		SaveFrame.setResizable(false);
		SaveFrame.setIconImage(WindowFavicon.getImage());

		SaveFrame.setVisible(true);

	}

}
