package Computer;

public class RAM {
	
	private byte[] ComputerRAM;
	
	public RAM(int iAddressBusWidth , ROM BootstrapROM) 
	{
		ComputerRAM = new byte[((int) Math.pow(2 , iAddressBusWidth))];
		
		for (int i = 0; i < ComputerRAM.length; i++) 
		{
			ComputerRAM[i] = 0x00;
			
		}
		
		bootstrapROM(BootstrapROM , (short) 0xFF00);
		
	}
	
	public void writeToMemoryLocation(short MemoryLocation , byte Data) 
	{
		ComputerRAM[Short.toUnsignedInt(MemoryLocation)] = Data;
		
	}
	
	public byte readFromMemoryLocation(short MemoryLocation) 
	{		
		return ComputerRAM[Short.toUnsignedInt(MemoryLocation)];
		
	}
	
	public void resetMemoryLocation(short MemoryLocation) 
	{	
		ComputerRAM[Short.toUnsignedInt(MemoryLocation)] = 0x00;
		
	}
	
	public void bootstrapROM(ROM LoadROM , short StartMemoryLocation) 
	{	
		ROM ComputerROM = LoadROM;
		
		for (int i = 0; i < ComputerROM.getComputerROM().length; i++) 
		{
			ComputerRAM[i + Short.toUnsignedInt(StartMemoryLocation)] = ComputerROM.getComputerROM()[i];
				
		}
		
		//System.out.println(Byte.toUnsignedInt(ComputerRAM[0xFFFF]));
//		System.out.println(toLittleEndian((short)0xFF00));
		
	}

}
