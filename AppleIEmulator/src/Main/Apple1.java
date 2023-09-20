package Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

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

		InOut.resetPIA();

		//Screen.setCursorActive(true);

		emulate();

	}

	private void emulate()
	{
		Screen.getWindow().setVisible(true);
		
		Screen.getOn().setVisible(true);
		Screen.getOff().setVisible(false);

		Screen.getOn().addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("Power ON pressed!");

				if (bEmulatorRun == false)
				{
					mainEmulatorThread = new Thread(mainPowerOn);
					bEmulatorPaused = false;
					bEmulatorRun = true;
					mainEmulatorThread.start();

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
				System.out.println("Power OFF pressed!");

				bEmulatorRun = false;
				Screen.setIsResetted(true);
				Screen.setCursorActive(false);
				
				Screen.resetMonitor();
				InOut.resetPIA();
				CPU.resetCPU();

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
			Screen.resetMonitor();
			Screen.setIsResetted(false);

			InOut.initalisePIA(CPU , Memory , Screen);

			try 
			{
				Screen.powerOnMonitor();

			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();

			}

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
						long preCycleTime = System.nanoTime();

						CPU.executeInstruction();

						long postCycleTime = System.nanoTime();

						long CycleTime = (postCycleTime - preCycleTime) / 1000;

						TimeUnit.MICROSECONDS.sleep(CPU.getCurrentClockCycles() - CycleTime);

						Thread.sleep(100);
						
						InOut.refreshDisplay();
						
					}
					catch (InterruptedException e) 
					{

					}
					
					//HAS TO BE AN EVEN INTEGER IN EXPRESSION OTHERWISE THERE WILL BE AN INFINITE LOOP
				//} while (CPU.getPC() != 0xFF0C);
					
				//			if (CPU.getClockCycles() < 29830)
				//			{
//								try 
				//				{
				//
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

				//}

//				try 
//				{
//					Thread.sleep(25);
//
//				} 
//				catch (InterruptedException e) 
//				{
//					e.printStackTrace();
//
//				}

			}

		} while (bEmulatorRun == true);

	};

	public static void main(String[] args) throws InterruptedException
	{		
		Properties SmootherGUI = System.getProperties();
		SmootherGUI.put("sun.java2d.d3d" , "false");
		System.setProperties(SmootherGUI);

		//You can adjust this accordingly to your eyesight needs
		final int iMonitorScale = 3;

		Monitor Screen = new Monitor(iMonitorScale , "Apple 1 Emulator - as useful as an actual apple rn");

		RAM MemoryRAM = new RAM(16);
		
		ROM StorageROM = new ROM(8 , "ROM");
		ACI CassetteInterface = new ACI(8 , "ACI" , Screen , MemoryRAM);
		
		MemoryRAM.bootstrapROMS(StorageROM.getROM() , (short) 0xFF00);
		//MemoryRAM.bootstrapROMS(CassetteInterface.getROM() , (short) 0xC100);

		CPU6502 CPU = new CPU6502(MemoryRAM);
		PIA InOut = new PIA(CPU , MemoryRAM , Screen);
		
		Apple1 Apple1Emulator = new Apple1(Screen , MemoryRAM , CPU , InOut);

	}

}
