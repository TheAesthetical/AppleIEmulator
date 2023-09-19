package Computer;

public class RAM {
	
	private byte[] ComputerRAM;

	public RAM(int iAddressBusWidth) 
	{
		ComputerRAM = new byte[((int) Math.pow(2 , iAddressBusWidth))];
		
	}
	
	public void write(short MemoryLocation , byte Data) 
	{
		ComputerRAM[Short.toUnsignedInt(MemoryLocation)] = Data;
		
	}
	
	//Int instead of byte because of change of signage, reduces boilerplate later
	public int read(int MemoryLocation) 
	{		
		return Byte.toUnsignedInt(ComputerRAM[MemoryLocation]);
		
	}
	
	public void resetMemoryLocation(short MemoryLocation) 
	{	
		ComputerRAM[Short.toUnsignedInt(MemoryLocation)] = 0x00;
		
	}
	
	public void resetMemory() 
	{	
		for (int i = 0; i < ComputerRAM.length; i++) 
		{
			ComputerRAM[i] = 0x00;
			
		}
		
	}
	
	public void bootstrapROMS(byte[] ContentStream , short StartMemoryLocation) 
	{	
		for (int i = 0; i < ContentStream.length; i++) 
		{
			ComputerRAM[i + Short.toUnsignedInt(StartMemoryLocation)] = ContentStream[i];
				
		}
		
	}

}
