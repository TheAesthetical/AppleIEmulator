package Computer;

public class RAM {
	
	private byte[] ComputerRAM;
	
	//C R U D
	
	public RAM(int iWordSize) 
	{
		ComputerRAM = new byte[((int) Math.pow(2 , iWordSize))];
		
		for (int i = 0; i < ComputerRAM.length; i++) 
		{
			ComputerRAM[i] = 0x00;
			
		}
		
	}
	
	public void updateMemoryLocation(short MemoryLocation , byte Data) 
	{
		ComputerRAM[MemoryLocation] = Data;
		
	}
	
	public byte getMemoryLocation(short MemoryLocation) 
	{
		byte DataAtLocation;
		
		DataAtLocation = ComputerRAM[MemoryLocation];
		
		return DataAtLocation;
		
	}
	
	public void resetMemoryLocation(short MemoryLocation) 
	{	
		ComputerRAM[MemoryLocation] = 0x00;
		
	}
	
	public void romBootstrapper() 
	{	
		
	}

}
