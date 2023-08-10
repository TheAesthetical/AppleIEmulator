package Computer;

class Opcode {
	
	private byte Operation;
	private byte AddressingMode;
	
	private int iClockCycles;
	
	public Opcode(byte Operation , byte AddressingMode , int iClockCycles)
	{
		Operation = this.Operation;
		AddressingMode = this.AddressingMode;
		
		iClockCycles = this.iClockCycles;
		
	}
	
}


public class CPU6502 implements Runnable{
	
	private Thread CPUThread;
	
	private final int iDataBus = 8;
	private final int iAddressBus = 16;

	//!!!DELETE STATIC MODIFIERS AFTER COMPLETION!!!
	
	private RAM DRAM;

	private byte Accumulator = (byte) 0x00;

	private short ProgramCounter = (byte) 0x0000;
	private static byte StackPointer = (byte) 0x00;

	private byte IndexX = (byte) 0x00;
	private byte IndexY = (byte) 0x00;

	private byte StatusFlags = (byte) 0x00;

	private boolean bInterrupt = false;
	private boolean bNonMaskableInterrupt = false;

	//===================================================================
	//Runtime stuff
	//===================================================================
	
	public CPU6502(RAM ComputerRAM)
	{
		DRAM = ComputerRAM;
		
		start();
		
		//Create opcodes in the constructor using inner class matrix
		
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
//		setStatusFlag('o',true);
//		setStatusFlag('b',true);
		setStatusFlag('d',true);
//		setStatusFlag('i',true);
		setStatusFlag('z',true);
//		setStatusFlag('-',true);
//		setStatusFlag('c',true);
//		System.out.println(Byte.toUnsignedInt(StatusFlags));
		System.out.println(getStatusFlag('-'));
		
	}
	
	//===================================================================
	//Status Flag Register
	//===================================================================
	
	private byte setBit(byte BinaryWord, int iBitPosition , boolean bBitValue) 
	{
		if(bBitValue == true)
		{
			BinaryWord |= (byte) (0x00 + Math.pow(2 , iBitPosition));
			
		}
		else
		{
			BinaryWord &= (byte) (0xFF - Math.pow(2 , iBitPosition));
			
		}
		
		return BinaryWord;
		
	}
	
	private boolean getBit(byte BinaryWord, int iBitPosition) 
	{		
		 return ((BinaryWord >> iBitPosition) & 1) == 1;

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
			//UNUSED
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
	
	private boolean getStatusFlag(char chFlag)
	{
		chFlag = Character.toUpperCase(chFlag);

		switch(chFlag) {
		case('C'):
			return getBit(StatusFlags , 0);
			
		case('Z'):
			return getBit(StatusFlags , 1);
			
		case('I'):
			return getBit(StatusFlags , 2);
			
		case('D'):
			return getBit(StatusFlags , 3);
			
		case('B'):
			return getBit(StatusFlags , 4);
			
			//UNUSED
		case('-'):
			return getBit(StatusFlags , 5);
			
		case('O'):
			return getBit(StatusFlags , 6);
			
		case('N'):
			return getBit(StatusFlags , 7);
		
		default:
			return false;
		
		}

	}

}
