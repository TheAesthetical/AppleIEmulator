package Computer;

@SuppressWarnings("unused")
public class CPU implements Runnable{
	
	private Thread CPUThread;

	//!!!DELETE STATIC MODIFIERS AFTER COMPLETION!!!

	private byte Accumulator = (byte) 0x00;

	private short ProgramCounter = (byte) 0xFFFF;
	private static byte StackPointer = (byte) 0x00;

	private byte IndexX = (byte) 0x00;
	private byte IndexY = (byte) 0x00;

	private byte StatusFlags = (byte) 0x00;

	private boolean bInterrupt = false;
	private boolean bNonMaskableInterrupt = false;

	public CPU()
	{
		start();
		
	}
	
	public void start() 
	{
		CPUThread = new Thread (this , "CPU Thread");
		CPUThread.start();
		
	}

	@Override
	public void run() 
	{
		setStatusFlag('n',true);
		setStatusFlag('o',true);
		setStatusFlag('b',true);
		setStatusFlag('d',true);
		setStatusFlag('i',true);
		setStatusFlag('z',true);
		setStatusFlag('-',true);
		setStatusFlag('c',true);
		System.out.println(toUnsignedByte(StatusFlags));
		
	}
	
	private byte setBit(byte Word, int iBitPosition , boolean bBitValue) 
	{
		if(bBitValue == true)
		{
			Word |= (byte) (0x00 + Math.pow(2 , iBitPosition));
			
		}
		else
		{
			Word &= (byte) (0xFF - Math.pow(2 , iBitPosition));
			
		}
		
		return Word;
		
	}

	private int toUnsignedByte (byte ByteValue) 
	{
		return ByteValue & 0xFF;

	}

	private int toUnsignedShort (short ShortValue) 
	{
		return ShortValue & 0xFFFF;

	}

	private void setStatusFlag(char chFlag , boolean bBit)
	{
		chFlag = Character.toUpperCase(chFlag);

		switch(chFlag) {
		case('C'):
			StatusFlags = setBit(StatusFlags , 0 , bBit);
			
			break;
		case('Z'):
			StatusFlags = setBit(StatusFlags , 1 , bBit);
			
			break;
		case('I'):
			StatusFlags = setBit(StatusFlags , 2 , bBit);
			
			break;
		case('D'):
			StatusFlags = setBit(StatusFlags , 3 , bBit);
			
			break;
		case('B'):
			StatusFlags = setBit(StatusFlags , 4 , bBit);
			
			break;
		case('-'):
			StatusFlags = setBit(StatusFlags , 5 , bBit);
			
			break;
		case('O'):
			StatusFlags = setBit(StatusFlags , 6 , bBit);
			
			break;
		case('N'):
			StatusFlags = setBit(StatusFlags , 7 , bBit);
			
			break;
		
		}

	}

}
