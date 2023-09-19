package Computer;

import java.util.concurrent.TimeUnit;

public class testCPU {

	public static void main(String[] args) throws InterruptedException {
		RAM MemoryRAM = new RAM(16);
		
		ROM StorageROM = new ROM(8 , "6502TESTPROGRAM");
		
		MemoryRAM.bootstrapROMS(StorageROM.getROM() , (short) 0x0000);
		
		CPU6502 CPU = new CPU6502(MemoryRAM);
		
		CPU.resetVector();
		
		System.out.println(CPU.getPC());
		
		
		do
		{

			try 
			{				
				long preCycleTime = System.nanoTime();

				CPU.executeInstruction();

				long postCycleTime = System.nanoTime();

				long CycleTime = (postCycleTime - preCycleTime) / 1000;

				TimeUnit.MICROSECONDS.sleep(CPU.getCurrentClockCycles() - CycleTime);

				Thread.sleep(100);
				
			}
			catch (InterruptedException e) 
			{

			}
			
			//HAS TO BE AN EVEN INTEGER IN EXPRESSION OTHERWISE THERE WILL BE AN INFINITE LOOP
		} while (CPU.getPC() != 0xFFFF);
			
		CPU.dumpCPU();

	}

}
