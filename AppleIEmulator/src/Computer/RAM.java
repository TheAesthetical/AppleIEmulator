package Computer;

public class RAM {
	
	private byte[] ComputerRAM;
	
	public RAM(int iAddressBusWidth , ROM BootstrapROM , ACI BootstrapACI) 
	{
		ComputerRAM = new byte[((int) Math.pow(2 , iAddressBusWidth))];
		
		initaliseMemory(BootstrapROM , BootstrapACI);
		
	}
	
	public void writeToMemoryLocation(short MemoryLocation , byte Data) 
	{
		ComputerRAM[Short.toUnsignedInt(MemoryLocation)] = Data;
		
	}
	
	//Int instead of byte because of change of signage, reduces boilerplate later
	public int readFromMemoryLocation(int MemoryLocation) 
	{		
		return Byte.toUnsignedInt(ComputerRAM[MemoryLocation]);
		
	}
	
	public void resetMemoryLocation(short MemoryLocation) 
	{	
		ComputerRAM[Short.toUnsignedInt(MemoryLocation)] = 0x00;
		
	}
	
	private void initaliseMemory(ROM BootstrapROM , ACI BootstrapACI) 
	{	
		for (int i = 0; i < ComputerRAM.length; i++) 
		{
			ComputerRAM[i] = 0x00;
			
		}
		 
		bootstrapROMS(BootstrapROM.getROM() , (short) 0xFF00);
		//TODO ACI bootstrapping into C100
		bootstrapROMS(BootstrapACI.getROM() , (short) 0xC100);
		
	}
	
	private void bootstrapROMS(byte[] ContentStream , short StartMemoryLocation) 
	{	
		for (int i = 0; i < ContentStream.length; i++) 
		{
			ComputerRAM[i + Short.toUnsignedInt(StartMemoryLocation)] = ContentStream[i];
				
		}
		
		//System.out.println(Byte.toUnsignedInt(ComputerRAM[0xFFFF]));
//		System.out.println(toLittleEndian((short)0xFF00));
		
	}

}
