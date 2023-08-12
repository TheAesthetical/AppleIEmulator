package Computer;

class Opcode {

	private String szOperation;
	private String szAddressingMode;
	
	private int iClockCycles;

	public Opcode(String szOpcodeOperation , String szOpcodeAddressingMode , int iOpcodeClockCycles)
	{
		this.szOperation = szOpcodeOperation;
		this.szAddressingMode = szOpcodeAddressingMode;
		
		this.iClockCycles = iOpcodeClockCycles;
		
	}
	
	public String getSzOperation() 
	{
		return szOperation;
		
	}

	public String getSzAddressingMode() 
	{
		return szAddressingMode;
	}

	public int getiClockCycles() 
	{
		return iClockCycles;
		
	}
	
	public void setiClockCycles(int iClockCycles) 
	{
		this.iClockCycles = iClockCycles;
		
	}
	
}

public class CPU6502 implements Runnable{
	
	private Thread CPUThread;
	
	private final int iDataBusSize = 8;
	private final int iAddressBusSize = 16;

	//!!!DELETE STATIC MODIFIERS AFTER COMPLETION!!!
	
	private RAM DRAM;

	private byte Accumulator = (byte) 0x00;

	private short ProgramCounter = (short) 0x0000;
	private static byte StackPointer = (byte) 0x00;

	private byte IndexX = (byte) 0x00;
	private byte IndexY = (byte) 0x00;

	private byte StatusFlags = (byte) 0x00;

	private boolean bInterrupt = false;
	private boolean bNonMaskableInterrupt = false;
	
	private void initaliseOpcodeMatrix() 
	{
		Opcode[] OpcodeMatrix = new Opcode[255];
		
		//ACC - Accumulator
		//ABS - absolute
		//ABX - absolute, X-indexed
		//ABY - absolute, Y-indexed
		//IMM - immediate
		//IMP - implied
		//IND - indirect
		//XIN - X-indexed, indirect
		//INY - indirect, Y-indexed	
		//REL - relative
		//ZPG - zero page
		//ZPX - zero page, X-indexed	
		//ZPY - zero page, Y-indexed	
		
		//ADC
		OpcodeMatrix[0x69] = new Opcode("ADC" , "IMM" , 2);
		OpcodeMatrix[0x65] = new Opcode("ADC" , "ZPG" , 3);
		OpcodeMatrix[0x75] = new Opcode("ADC" , "ZPX" , 4);
		OpcodeMatrix[0x6D] = new Opcode("ADC" , "ABS" , 4);
		OpcodeMatrix[0x7D] = new Opcode("ADC" , "ABX" , 4);
		OpcodeMatrix[0x79] = new Opcode("ADC" , "ABY" , 4);
		OpcodeMatrix[0x61] = new Opcode("ADC" , "XIN" , 6);
		OpcodeMatrix[0x71] = new Opcode("ADC" , "INY" , 5);
		
		//AND
		OpcodeMatrix[0x29] = new Opcode("AND" , "IMM" , 2);
		OpcodeMatrix[0x25] = new Opcode("AND" , "ZPG" , 3);
		OpcodeMatrix[0x35] = new Opcode("AND" , "ZPX" , 4);
		OpcodeMatrix[0x2D] = new Opcode("AND" , "ABS" , 4);
		OpcodeMatrix[0x3D] = new Opcode("AND" , "ABX" , 4);
		OpcodeMatrix[0x39] = new Opcode("AND" , "ABY" , 4);
		OpcodeMatrix[0x21] = new Opcode("AND" , "XIN" , 6);
		OpcodeMatrix[0x31] = new Opcode("AND" , "INY" , 5);
		
		//ASL
		OpcodeMatrix[0x0A] = new Opcode("ASL" , "ACC" , 2);
		OpcodeMatrix[0x06] = new Opcode("ASL" , "ZPG" , 5);
		OpcodeMatrix[0x16] = new Opcode("ASL" , "ZPX" , 6);
		OpcodeMatrix[0x0E] = new Opcode("ASL" , "ABS" , 6);
		OpcodeMatrix[0x1E] = new Opcode("ASL" , "ABX" , 7);
		
		//BCC
		OpcodeMatrix[0x90] = new Opcode("BCC" , "REL" , 2);
		
		//BCS
		OpcodeMatrix[0xB0] = new Opcode("BCS" , "REL" , 2);
		
		//BEQ
		OpcodeMatrix[0xF0] = new Opcode("BEQ" , "REL" , 2);
		
		//BIT
		OpcodeMatrix[0x24] = new Opcode("BIT" , "ZPG" , 3);
		OpcodeMatrix[0x2C] = new Opcode("BIT" , "ABS" , 4);
		
		//BMI
		OpcodeMatrix[0x30] = new Opcode("BMI" , "REL" , 2);
		
		//BNE
		OpcodeMatrix[0xD0] = new Opcode("BNE" , "REL" , 2);
		
		//BPL
		OpcodeMatrix[0x10] = new Opcode("BPL" , "REL" , 2);
		
		//BRK
		OpcodeMatrix[0x00] = new Opcode("BRK" , "IMP" , 7);
		
		//BVC
		OpcodeMatrix[0x50] = new Opcode("BVC" , "REL" , 2);
		
		//BVS
		OpcodeMatrix[0x70] = new Opcode("BVS" , "REL" , 2);
		
		//CLC
		OpcodeMatrix[0x18] = new Opcode("CLC" , "IMP" , 2);
		
		//CLD
		OpcodeMatrix[0xD8] = new Opcode("CLD" , "IMP" , 2);
		
		//CLI
		OpcodeMatrix[0x58] = new Opcode("CLI" , "IMP" , 2);
		
		//CLV
		OpcodeMatrix[0xB8] = new Opcode("CLV" , "IMP" , 2);
		
		//CMP
		OpcodeMatrix[0xC9] = new Opcode("CMP" , "IMM" , 2);
		OpcodeMatrix[0xC5] = new Opcode("CMP" , "ZPG" , 3);
		OpcodeMatrix[0xD5] = new Opcode("CMP" , "ZPX" , 4);
		OpcodeMatrix[0xCD] = new Opcode("CMP" , "ABS" , 4);
		OpcodeMatrix[0xDD] = new Opcode("CMP" , "ABX" , 4);
		OpcodeMatrix[0xD9] = new Opcode("CMP" , "ABY" , 4);
		OpcodeMatrix[0xC1] = new Opcode("CMP" , "XIN" , 6);
		OpcodeMatrix[0xD1] = new Opcode("CMP" , "INY" , 5);
		
		//CPX
		OpcodeMatrix[0xE0] = new Opcode("CPX" , "IMM" , 2);
		OpcodeMatrix[0xE4] = new Opcode("CPX" , "ZPG" , 3);
		OpcodeMatrix[0xEC] = new Opcode("CPX" , "ABS" , 4);
		
		//CPY
		OpcodeMatrix[0xC0] = new Opcode("CPY" , "IMM" , 2);
		OpcodeMatrix[0xC4] = new Opcode("CPY" , "ZPG" , 3);
		OpcodeMatrix[0xCC] = new Opcode("CPY" , "ABS" , 4);
		
		//DEC
		OpcodeMatrix[0xC6] = new Opcode("DEC" , "ZPG" , 5);
		OpcodeMatrix[0xD6] = new Opcode("DEC" , "ZPX" , 6);
		OpcodeMatrix[0xCE] = new Opcode("DEC" , "ABS" , 6);
		OpcodeMatrix[0xDE] = new Opcode("DEC" , "ABX" , 7);
		
		//DEX
		OpcodeMatrix[0xCA] = new Opcode("DEX" , "IMP" , 2);
		
		//DEY
		OpcodeMatrix[0x88] = new Opcode("DEY" , "IMP" , 2);
		
		//EOR
		OpcodeMatrix[0x49] = new Opcode("EOR" , "IMM" , 2);
		OpcodeMatrix[0x45] = new Opcode("EOR" , "ZPG" , 3);
		OpcodeMatrix[0x55] = new Opcode("EOR" , "ZPX" , 4);
		OpcodeMatrix[0x4D] = new Opcode("EOR" , "ABS" , 4);
		OpcodeMatrix[0x5D] = new Opcode("EOR" , "ABX" , 4);
		OpcodeMatrix[0x59] = new Opcode("EOR" , "ABY" , 4);
		OpcodeMatrix[0x41] = new Opcode("EOR" , "XIN" , 6);
		OpcodeMatrix[0x51] = new Opcode("EOR" , "INY" , 5);
		
		//INC
		OpcodeMatrix[0xE6] = new Opcode("INC" , "ZPG" , 5);
		OpcodeMatrix[0xF6] = new Opcode("INC" , "ZPX" , 6);
		OpcodeMatrix[0xEE] = new Opcode("INC" , "ABS" , 6);
		OpcodeMatrix[0xFE] = new Opcode("INC" , "ABX" , 7);
		
		//INX
		OpcodeMatrix[0xE8] = new Opcode("INX" , "IMP" , 2);
		
		//INY
		OpcodeMatrix[0xC8] = new Opcode("INY" , "IMP" , 2);
		
		//JMP
		OpcodeMatrix[0x4C] = new Opcode("JMP" , "ABS" , 3);
		OpcodeMatrix[0x6C] = new Opcode("JMP" , "IND" , 5);
		
		//JSR
		OpcodeMatrix[0x20] = new Opcode("JSR" , "ABS" , 6);
		
		//LDA
		OpcodeMatrix[0xA9] = new Opcode("LDA" , "IMM" , 2);
		OpcodeMatrix[0xA5] = new Opcode("LDA" , "ZPG" , 3);
		OpcodeMatrix[0xB5] = new Opcode("LDA" , "ZPX" , 4);
		OpcodeMatrix[0xAD] = new Opcode("LDA" , "ABS" , 4);
		OpcodeMatrix[0xBD] = new Opcode("LDA" , "ABX" , 4);
		OpcodeMatrix[0xB9] = new Opcode("LDA" , "ABY" , 4);
		OpcodeMatrix[0xA1] = new Opcode("LDA" , "XIN" , 6);
		OpcodeMatrix[0xB1] = new Opcode("LDA" , "INY" , 5);
		
		//LDX
		OpcodeMatrix[0xA2] = new Opcode("LDX" , "IMM" , 2);
		OpcodeMatrix[0xA6] = new Opcode("LDX" , "ZPG" , 3);
		OpcodeMatrix[0xB6] = new Opcode("LDX" , "ZPY" , 4);
		OpcodeMatrix[0xAE] = new Opcode("LDX" , "ABS" , 4);
		OpcodeMatrix[0xBE] = new Opcode("LDX" , "ABY" , 4);
		
		//LDY
		OpcodeMatrix[0xA0] = new Opcode("LDY" , "IMM" , 2);
		OpcodeMatrix[0xA4] = new Opcode("LDY" , "ZPG" , 3);
		OpcodeMatrix[0xB4] = new Opcode("LDY" , "ZPX" , 4);
		OpcodeMatrix[0xAC] = new Opcode("LDY" , "ABS" , 4);
		OpcodeMatrix[0xBC] = new Opcode("LDY" , "ABX" , 4);
		
		//LSR
		OpcodeMatrix[0x4A] = new Opcode("LSR" , "ACC" , 2);
		OpcodeMatrix[0x46] = new Opcode("LSR" , "ZPG" , 5);
		OpcodeMatrix[0x56] = new Opcode("LSR" , "ZPX" , 6);
		OpcodeMatrix[0x4E] = new Opcode("LSR" , "ABS" , 6);
		OpcodeMatrix[0x5E] = new Opcode("LSR" , "ABX" , 7);
		
		//NOP
		OpcodeMatrix[0xEA] = new Opcode("NOP" , "IMP" , 2);
		
		//ORA
		OpcodeMatrix[0x09] = new Opcode("ORA" , "IMM" , 2);
		OpcodeMatrix[0x05] = new Opcode("ORA" , "ZPG" , 3);
		OpcodeMatrix[0x15] = new Opcode("ORA" , "ZPX" , 4);
		OpcodeMatrix[0x0D] = new Opcode("ORA" , "ABS" , 4);
		OpcodeMatrix[0x1D] = new Opcode("ORA" , "ABX" , 4);
		OpcodeMatrix[0x19] = new Opcode("ORA" , "ABY" , 4);
		OpcodeMatrix[0x01] = new Opcode("ORA" , "XIN" , 6);
		OpcodeMatrix[0x11] = new Opcode("ORA" , "INY" , 5);
		
		//TODO push opcodes
		//
		
	}

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
