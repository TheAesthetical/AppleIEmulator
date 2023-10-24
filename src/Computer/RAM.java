package Computer;

public class RAM {
	
	private byte[] byComputerRAM;

	public RAM(int iByteSize) 
	{
		byComputerRAM = new byte[iByteSize];
		
	}
	
	// Debug
//	public RAM()
//	{
//		ComputerRAM = new byte[0x10000];
//	}
	
	public void write(short shMemoryLocation , byte byData) 
	{
		byComputerRAM[Short.toUnsignedInt(shMemoryLocation)] = (byte) Byte.toUnsignedInt(byData);
		
	}
	
	public int read(short shMemoryLocation) 
	{		
		return Byte.toUnsignedInt(byComputerRAM[Short.toUnsignedInt(shMemoryLocation)]);
		
	}
	
	public void resetLocation(short shMemoryLocation) 
	{	
		byComputerRAM[Short.toUnsignedInt(shMemoryLocation)] = 0x00;
		
	}
	
	public void resetRAM() 
	{	
		for (int i = 0; i < byComputerRAM.length; i++) 
		{
			byComputerRAM[i] = 0x00;
			
		}
		
	}
	
	public void bootstrapROM(byte[] byROMContentStream , short shStartMemoryLocation) 
	{	
		for (int i = 0; i < byROMContentStream.length; i++) 
		{
			byComputerRAM[i + Short.toUnsignedInt(shStartMemoryLocation)] = byROMContentStream[i];
			
			if((i + Short.toUnsignedInt(shStartMemoryLocation) + 1) > 0xFFFF) break;
				
		}
		
	}

}
