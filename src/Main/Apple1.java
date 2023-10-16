package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Computer.*;
import Terminal.Monitor;

public class Apple1 extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	//Calculated dimensions of the Apple 1 Terminal - they don't change hence final modifier
	private final int iGridWidth = 240;
	private final int iGridHeight = 192;
	
	private final int iCellSize;
	
	Utilities Utils = new Utilities();
	
	private JFrame Window;

	private JMenuItem On;
	private JMenuItem Off;
	
	private JMenuItem ResetButton;
	private JMenuItem CLSButton;

	private JMenu LoadCassette;
	private JMenuItem IntegerBASIC;
	private JMenuItem Other;
	private JMenuItem SaveCassette;
	private JMenuItem ClearCassette;

	private Monitor Screen;
	private RAM Memory;
	private CPU6502 CPU;
	private PIA InOut;
	private ROM StorageROM;
	private ACI Storage;

	private boolean bEmulatorPaused;
	private boolean bEmulatorRun;

	private Thread MainEmulatorThread;

	public Apple1(int iMonitorScale) throws InterruptedException
	{
		iCellSize = iMonitorScale;
		
		Screen = new Monitor(iGridWidth , iGridHeight);
		
		createGUI("Apple 1 Emulator");

		Memory = new RAM(16);
		StorageROM = new ROM(8 , "ROM");
		
		CPU = new CPU6502(Memory);
		
		Storage = new ACI(12 , "ACI" , Memory , CPU);
		InOut = new PIA(CPU , Memory , Screen);
		
		InOut.resetPIA();

		initaliseMemory();

		emulate();

	}

	private void emulate()
	{
		Window.setVisible(true);

		On.setVisible(true);
		Off.setVisible(false);

		On.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("Power ON pressed!");

				if (bEmulatorRun == false)
				{
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

				Screen.setIsResetted(true);
				Screen.setCursorActive(false);

				Memory.resetMemory();
				Screen.resetMonitor();
				InOut.resetPIA();
				CPU.resetCPU();
				Storage.resetACI();

				On.setVisible(true);
				Off.setVisible(false);

			}

		});
		
		Window.addKeyListener(new KeyListener() 
		{
			public void keyTyped(KeyEvent e) 
			{				
				if(Screen != null && Screen.getIsResetted() == true && CPU.getisRunning() == true) 
				{		
					InOut.setKeyPressed(true);
					InOut.setCharacterPressed(e.getKeyChar());

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
				System.out.println("RESET");

				if(Screen != null && CPU != null)
				{
					Screen.setIsResetted(true);
					Screen.resetMonitor();
					Screen.setCursorActive(true);

					CPU.resetCPU();
					CPU.resetVector();

					CPU.setisRunning(true);

				}

			}

		});

		CLSButton.addActionListener(new ActionListener() 
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

	Runnable MainPowerOn = () ->
	{

		//iStartTime = System.nanoTime();

		//		do
		//		{
		if (bEmulatorPaused == false)
		{
//						System.out.println("hicdasdasdasdasccc");
//						System.exit(0);

			Screen.resetMonitor();
			Screen.setIsResetted(false);

			InOut.initalisePIA(CPU , Memory , Screen);
			Storage.initaliseACI(Memory , CPU);

			try 
			{
				Screen.powerOnMonitor();

			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();

			}

			initaliseMemory();

			CPU.resetCPU();
			CPU.resetVector();

		}

		while (bEmulatorPaused == false && bEmulatorRun == true )
		{
			if(CPU.getisRunning() == true)
			{

				//				do
				//				{

				try 
				{	
					long lPreCycleTime = System.nanoTime();

					CPU.executeInstruction();

					long lPostCycleTime = System.nanoTime();

					long lCycleTime = (lPostCycleTime - lPreCycleTime) / 1000;

					TimeUnit.MICROSECONDS.sleep(CPU.getCurrentClockCycles() - lCycleTime);

					//Thread.sleep(400);
					//Thread.sleep(200);
					
					InOut.refreshDisplay();

				}
				catch (InterruptedException e) 
				{

				}

			}

		} while (bEmulatorRun == true);

	};
	
	private void createGUI(String szWindowTitle) throws InterruptedException 
	{
		Window = new JFrame(szWindowTitle);

		ImageIcon WindowFavicon = new ImageIcon(Utils.getDirectoryName() + "\\windowfavicon.png");
		Window.setIconImage(WindowFavicon.getImage());

		Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Window.setResizable(false);

		JMenuBar MenuBar = new JMenuBar();
		MenuBar.setSize(iGridWidth * iCellSize , 10);

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
		
		ClearCassette = new JMenuItem("Clear");
		
		SaveCassette = new JMenuItem("Save");
		
		ACIInterface.add(LoadCassette);
		ACIInterface.add(ClearCassette);
		ACIInterface.add(SaveCassette);
		
		MenuBar.add(ACIInterface);

		Window.add(MenuBar , BorderLayout.SOUTH);
		
		Screen.setGUI().setPreferredSize(new Dimension(iGridWidth * iCellSize , iGridHeight * iCellSize));
        //CustomComponent customComponent = new CustomComponent(Screen.getPixelGrid());
        Window.getContentPane().add(Screen.setGUI());
     

		Window.pack();
		Window.setVisible(true);

	}

	private void initaliseMemory()
	{
		Memory.bootstrapROMS(StorageROM.getROM() , (short) 0xFF00);
		Memory.bootstrapROMS(Storage.getROM() , (short) 0xC100);

	}
	
}
