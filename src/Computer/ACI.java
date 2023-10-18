package Computer;

public class ACI extends ROM {
	
	private RAM Memory;
	private CPU6502 CPU;
	
	private ROM CurrentCassette;
	
	private ROM IntegerBASIC = new ROM(4096 , "INTEGERBASIC.bin");
	private ROM[] Cassettes = {CurrentCassette , IntegerBASIC};
	
	private short shCassetteLocation;
	
	public ACI(int iSizeInBytes , String szFileName , RAM activeMemory , CPU6502 activeCPU)
	{
		super(iSizeInBytes , szFileName);
		
		Memory = activeMemory;
		CPU = activeCPU;
		
		Memory.bootstrapROMS(getROM() , (short) 0xC100);
		
	} 
	
	public void loadCassette(int iCassetteNumber , short shLocation) 
	{
		shCassetteLocation = shLocation;
		CurrentCassette = Cassettes[iCassetteNumber];
		
		Memory.bootstrapROMS(CurrentCassette.getROM() , shCassetteLocation);
		
	}
	
	public void loadFile(String szFileName , int iFileSize ,  short shLocation) 
	{
		shCassetteLocation = shLocation;
		
		CurrentCassette = new ROM(iFileSize , szFileName);
		
		Memory.bootstrapROMS(CurrentCassette.getROM() , shCassetteLocation);
		
		//loadCassette(0 , shCassetteLocation);
		
	}
	
	public void clearCassette() 
	{
		for (int i = Short.toUnsignedInt(shCassetteLocation); i <= (Short.toUnsignedInt(shCassetteLocation) + CurrentCassette.getROM().length); i++) 
		{
			Memory.write((short) i, (byte) 0x00);
			
		}
		
		CPU.resetVector();
		
	}
	
	public byte[] getMemoryStream(short shStartAddress , short shEndAddress) 
	{
//		System.out.println(shStartAddress + "\n\n\n");
//		System.out.println(shEndAddress + "\n\n\n");
		byte[] byMemoryBuffer = new byte[(Short.toUnsignedInt(shEndAddress) - Short.toUnsignedInt(shStartAddress)) + 1];
		
		for (int i = 0; i < byMemoryBuffer.length; i++) 
		{
			byMemoryBuffer[i] = (byte) Memory.read(Short.toUnsignedInt(shStartAddress) + i);
			//System.out.println(byMemoryBuffer[i]);
			
		}
		
		return byMemoryBuffer;
		
	}
	
	
}
