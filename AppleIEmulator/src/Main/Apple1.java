package Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Computer.*;
import Terminal.Monitor;

public class Apple1 {

	private Monitor Screen;
	private RAM Memory;
	private CPU6502 CPU;
	private PIA InOut;
	
	private boolean bEmulatorPaused;
	private boolean bEmulatorRun;
	
	Thread mainEmulatorThread;
	
	public Apple1(Monitor tempScreen, RAM tempMemory, CPU6502 tempCPU, PIA tempInOut)
	{
		Screen = tempScreen;
		Memory = tempMemory;
		CPU = tempCPU;
		InOut = tempInOut;
		
		reset();
		
	}
	
	private void reset()
	{
		Screen.getOn().setVisible(true);
		Screen.getOff().setVisible(false);
		
		Screen.getOn().addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("Power ON pressed!");

				if (bEmulatorRun == false)
				{
					//	                    DEBUG - Force Load Game ROMs
					//	                    CPU.getMainMemory().loadRom(new File("C:\\Users\\lukew\\Desktop\\Gameboy Emulator Project\\Documents\\ROMs\\TETRIS.gb"));

					mainEmulatorThread = new Thread(mainPowerOn);
					//	                    saveStateHandlingThread = new Thread(saveStateHandling);
					bEmulatorPaused = false;
					bEmulatorRun = true;
					mainEmulatorThread.start();
					//	                    saveStateHandlingThread.start();
				}
				else
				{
					System.err.println("EMULATOR ALREADY RUNNING!");
				}

				Screen.getOn().setVisible(false);
				Screen.getOff().setVisible(true);
				
			}
			
		});
		
		Screen.getOff().addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Screen.stop();
				Screen.resetMonitor();
				
				InOut.reset();
				
				System.out.println("Power OFF pressed!");

				bEmulatorRun = false;
				
				Screen.getOn().setVisible(true);
				Screen.getOff().setVisible(false);
				
			}
			
		});
		
	}

	Runnable mainPowerOn = () ->
	{

		//iStartTime = System.nanoTime();

//		do
//		{
			if (bEmulatorPaused == false)
			{
				CPU.resetCPU();
				
				InOut.enableHandlers();
				
				Screen.resetMonitor();
				
				Screen.resume();

			}

//			while (bEmulatorPaused == false && bEmulatorRun == true)
//			{
				//	                if (CPU.getCpuCycles() < 29830)
				//	                {
//				try 
//				{
//					//CPU.startCycle();
//					
//				} 
//				catch (InterruptedException e) 
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					
//				}
				//	                }
				//	                else
				//	                {
				//	                    CPU.setCpuCycles(0);
				//	                    
				//	                    bReadyToModifyState = true;
				//	                    
				//	                    while (System.nanoTime() - iStartTime < iFrame)
				//	                    {
				//	                    }
				//	                    
				//	                    iStartTime = System.nanoTime();
				//	                }
//			}

			try 
			{
				Thread.sleep(25);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
//		} while (bEmulatorRun == true);
			
	};

	public static void main(String[] args) throws InterruptedException
	{		
		//You can adjust this accordingly to your eyesight needs
		final int iMonitorScale = 3;

		Monitor Screen = new Monitor(iMonitorScale , "Apple 1 Emulator - PROTOTYPE - ");

		ROM StorageROM = new ROM(8 , "ROM");
		ACI CassetteInterface = new ACI(8 , "ACI");
		RAM MemoryRAM = new RAM(16 , StorageROM , CassetteInterface);

		CPU6502 CPU = new CPU6502(MemoryRAM);
		PIA InOut = new PIA(CPU , MemoryRAM , Screen);

		//Testing multithreading
		//---------------------------------------------------------------
		//		Thread Terminal = new Thread(Screen);
		//		Thread Computer = new Thread(CPU);
		//
		//		Terminal.sleep(100);
		//		// TODO Auto-generated catch block
		//		Computer.sleep(100);
		//
		//		Terminal.start();
		//		Computer.start();

		Apple1 A1Emulator = new Apple1(Screen , MemoryRAM , CPU , InOut);

	}
}