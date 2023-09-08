package Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Computer.*;
import Terminal.Monitor;

public class Main {

//	private static final int iMonitorScale = 3;
//	private static Thread Terminal;
//	private static Thread Computer;
//	private static Monitor Screen;
//
//	private static void powerHandler() throws InterruptedException
//	{
//		Screen = new Monitor(iMonitorScale , "Apple 1 Emulator - PROTOTYPE - ");
//		
//		Screen.getOn().addActionListener(new ActionListener() 
//		{
//
//			public void actionPerformed(ActionEvent e) 
//			{
//
//				try {
//
//					Screen.start();
//
//					ROM StorageROM = new ROM(8 , "ROM");
//					ACI CassetteInterface = new ACI(8 , "ACI");
//					RAM MemoryRAM = new RAM(16 , StorageROM , CassetteInterface);
//
//					CPU6502 CPU = new CPU6502(MemoryRAM);
//					PIA Interface = new PIA(CPU , MemoryRAM , Screen);
//
//					//Testing multithreading
//					//---------------------------------------------------------------
//					Terminal = new Thread(Screen);
//					Computer = new Thread(CPU);
//
//					Terminal.sleep(100);
//					// TODO Auto-generated catch block
//					Computer.sleep(100);
//
//					Terminal.start();
//					Computer.start();
//
//					//---------------------------------------------------------------
//
//				} 
//				catch (InterruptedException e1) 
//				{
//					System.out.println("ERROR");
//					e1.printStackTrace();
//
//				}
//
//			}
//
//		});
//
//		Screen.getOff().addActionListener(new ActionListener() 
//		{
//			public void actionPerformed(ActionEvent e) 
//			{
////				Screen.resetMonitor();
////				
////				try 
////				{
////					main(new String[0]);
////					
////				} 
////				catch (InterruptedException e1) 
////				{
////					e1.printStackTrace();
////					
////				}
//				
//			}
//
//		});
//
//	}
	
	 Runnable mainPowerOn = () ->
	    {
	        
	        CPU.initialize();
	        PPU.initialize();
	        
	        iStartTime = System.nanoTime();
	        
	        do
	        {
	            if (bEmulatorPaused == false)
	            {
	                PPU.reset();
	                CPU.reset();
	            }
	            
	            while (bEmulatorPaused == false && bEmulatorRun == true)
	            {
	                if (CPU.getCpuCycles() < 29830)
	                {
	                    CPU.executeCycleCPU();
	                }
	                else
	                {
	                    CPU.setCpuCycles(0);
	                    
	                    bReadyToModifyState = true;
	                    
	                    while (System.nanoTime() - iStartTime < iFrame)
	                    {
	                    }
	                    
	                    iStartTime = System.nanoTime();
	                }
	            }
	            
	            try 
	            {
	                Thread.sleep(25);
	            } 
	            catch (InterruptedException e) 
	            {
	                e.printStackTrace();
	            }
	        } while (bEmulatorRun == true);
	    };
        Screen.getOn().addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
	                System.out.println("Power ON pressed!");
	                
	                CPU.initialize();
	                PPU.initialize();
	                
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
	                
	                powerOnMI.setVisible(false);
	                powerOffMI.setVisible(true);
	            }
	        });
	        Screen.getOff().addActionListener(new ActionListener() 
	        		{
	        			public void actionPerformed(ActionEvent e) 
	        			{
	                System.out.println("Power OFF pressed!");
	                
	                bEmulatorRun = false;
	            }
	        });

	public static void main(String[] args) throws InterruptedException 
	{		
		final int iMonitorScale = 3;
		
		Monitor Screen = new Monitor(iMonitorScale , "Apple 1 Emulator - PROTOTYPE - ");
		
		ROM StorageROM = new ROM(8 , "ROM");
		ACI CassetteInterface = new ACI(8 , "ACI");
		RAM MemoryRAM = new RAM(16 , StorageROM , CassetteInterface);

		CPU6502 CPU = new CPU6502(MemoryRAM);
		PIA Interface = new PIA(CPU , MemoryRAM , Screen);

		//Testing multithreading
		//---------------------------------------------------------------
		Thread Terminal = new Thread(Screen);
		Thread Computer = new Thread(CPU);

		Terminal.sleep(100);
		// TODO Auto-generated catch block
		Computer.sleep(100);

		Terminal.start();
		Computer.start();

	}

}	

