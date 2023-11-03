package Computer;

public class ACI extends ROM {
	
	private RAM Memory;
	
	private ROM CurrentCassette;
	
	public ACI(int iSizeInBytes , String szFileName , RAM activeMemory)
	{
		super(iSizeInBytes , szFileName);
		
		Memory = activeMemory;
				
	} 
	
	public void loadFileCassette(String szFileName , int iFileSize ,  short shLocation) 
	{
		CurrentCassette = new ROM(iFileSize , szFileName);
		
		Memory.bootstrapROM(CurrentCassette.getROM() , shLocation);
		
	}
	
	public byte[] getMemoryByteStream(short shStartAddress , short shEndAddress) 
	{
		byte[] byMemoryBuffer = new byte[(Short.toUnsignedInt(shEndAddress) - Short.toUnsignedInt(shStartAddress)) + 1];
		
		for (int i = 0; i < byMemoryBuffer.length; i++) 
		{
			byMemoryBuffer[i] = (byte) Memory.read((short) (shStartAddress + i));
			
		}
		
		return byMemoryBuffer;
		
	}
	
}
