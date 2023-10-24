package Computer;

public class ACI extends ROM {
	
	private RAM Memory;
	
	private ROM CurrentCassette;
	
	private ROM IntegerBASIC = new ROM(4096 , "INTEGERBASIC.bin");
	private ROM[] Cassettes = {CurrentCassette , IntegerBASIC};
	
	public ACI(int iSizeInBytes , String szFileName , RAM activeMemory)
	{
		super(iSizeInBytes , szFileName);
		
		Memory = activeMemory;
		
		Memory.bootstrapROM(getROM() , (short) 0xC100);
		
	} 
	
	public void loadSelectedCassette(int iCassetteNumber , short shLocation) 
	{
		CurrentCassette = Cassettes[iCassetteNumber];
		
		Memory.bootstrapROM(CurrentCassette.getROM() , shLocation);
		
	}
	
	public void loadFile(String szFileName , int iFileSize ,  short shLocation) 
	{
		CurrentCassette = new ROM(iFileSize , szFileName);
		
		Memory.bootstrapROM(CurrentCassette.getROM() , shLocation);
		
	}
	
	public byte[] getByteStream(short shStartAddress , short shEndAddress) 
	{
		byte[] byMemoryBuffer = new byte[(Short.toUnsignedInt(shEndAddress) - Short.toUnsignedInt(shStartAddress)) + 1];
		
		for (int i = 0; i < byMemoryBuffer.length; i++) 
		{
			byMemoryBuffer[i] = (byte) Memory.read((short) (shStartAddress + i));
			
		}
		
		return byMemoryBuffer;
		
	}
	
}
