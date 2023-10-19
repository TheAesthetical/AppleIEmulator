package Computer;

public class CPU6502 {

	public class Opcode {

		private String szOperation;
		private String szAddressingMode;

		private int iOpcodeClockCycles;

		public Opcode(String szOpcodeOperation , String szOpcodeAddressingMode , int iOpcodeClockCycles)
		{
			this.szOperation = szOpcodeOperation;
			this.szAddressingMode = szOpcodeAddressingMode;

			this.iOpcodeClockCycles = iOpcodeClockCycles;

		}

		public String getOperation() 
		{
			return szOperation;

		}

		public String getAddressingMode() 
		{
			return szAddressingMode;
		}

		public int getOpcodeClockCycles() 
		{
			return iOpcodeClockCycles;

		}

	}

	//private final int iClockSpeedHZ = 1023000;

	private RAM Memory;
	//
	//	private byte byAccumulator = (byte) 0x00;
	//
	//	private short shProgramCounter = (short) 0x0000;
	//	private byte byStackPointer = (byte) 0xFD; //0x0100 - 0x01FF
	//
	//	private byte byIndexX = (byte) 0x00;
	//	private byte byIndexY = (byte) 0x00;
	//
	//	private byte byStatusFlags = (byte) 0b00100000;

	// -----------------------------------------------
	// Debug
	public byte byAccumulator = (byte) 0x00;

	public short shProgramCounter = (short) 0x0000;
	public byte byStackPointer = (byte) 0x00; //0x0100 - 0x01FF

	public byte byIndexX = (byte) 0x00;
	public byte byIndexY = (byte) 0x00;

	public byte byStatusFlags = (byte) 0x00;

	public Opcode[] OpcodeMatrix = new Opcode[255];

	// -----------------------------------------------

	private boolean bInterrupt = false;
	private boolean bNonMaskableInterrupt = false;

	//private Opcode[] OpcodeMatrix = new Opcode[255];

	private byte byLoByte = (byte) 0x00;
	private byte byHiByte = (byte) 0x00;

	private long lCurrentClockCycles = 0;

	private boolean bRunning = false;

	private void initaliseOpcodeMatrix() 
	{
		//Modes:
		//ACC - byAccumulator
		//ABS - Absolute
		//ABX - Absolute, X-indexed
		//ABY - Absolute, Y-indexed
		//IMM - Immediate
		//IMP - Implied
		//IND - Indirect
		//XIN - X-indexed, indirect
		//INY - Indirect, Y-indexed	
		//REL - Relative
		//ZPG - Zero page
		//ZPX - Zero page, X-indexed	
		//ZPY - Zero page, Y-indexed	

		//Assuming all Opcodes in the matrix are illegal unless initialised 
		for (int i = 0; i < OpcodeMatrix.length; i++) 
		{
			OpcodeMatrix[i] = new Opcode("XXX" , "XXX" , 0);

		}

		//Opcodes ordered alphabetically from A-Z:
		
		//OpcodeMatrix[0x-LoNibble-HiNibble] = new Opcode("Operation" , "AddressingMode" , DefaultNumberOfCycles);

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

		//PHA
		OpcodeMatrix[0x48] = new Opcode("PHA" , "IMP" , 3);

		//PHP
		OpcodeMatrix[0x08] = new Opcode("PHP" , "IMP" , 3);

		//PLA
		OpcodeMatrix[0x68] = new Opcode("PLA" , "IMP" , 4);

		//PLP
		OpcodeMatrix[0x28] = new Opcode("PLP" , "IMP" , 4);

		//ROL
		OpcodeMatrix[0x2A] = new Opcode("ROL" , "ACC" , 2);
		OpcodeMatrix[0x26] = new Opcode("ROL" , "ZPG" , 5);
		OpcodeMatrix[0x36] = new Opcode("ROL" , "ZPX" , 6);
		OpcodeMatrix[0x2E] = new Opcode("ROL" , "ABS" , 6);
		OpcodeMatrix[0x3E] = new Opcode("ROL" , "ABX" , 7);

		//ROR
		OpcodeMatrix[0x6A] = new Opcode("ROR" , "ACC" , 2);
		OpcodeMatrix[0x66] = new Opcode("ROR" , "ZPG" , 5);
		OpcodeMatrix[0x76] = new Opcode("ROR" , "ZPX" , 6);
		OpcodeMatrix[0x6E] = new Opcode("ROR" , "ABS" , 6);
		OpcodeMatrix[0x7E] = new Opcode("ROR" , "ABX" , 7);

		//RTI
		OpcodeMatrix[0x40] = new Opcode("RTI" , "IMP" , 6);

		//RTS
		OpcodeMatrix[0x60] = new Opcode("RTS" , "IMP" , 6);

		//SBC
		OpcodeMatrix[0xE9] = new Opcode("SBC" , "IMM" , 2);
		OpcodeMatrix[0xE5] = new Opcode("SBC" , "ZPG" , 3);
		OpcodeMatrix[0xF5] = new Opcode("SBC" , "ZPX" , 4);
		OpcodeMatrix[0xED] = new Opcode("SBC" , "ABS" , 4);
		OpcodeMatrix[0xFD] = new Opcode("SBC" , "ABX" , 4);
		OpcodeMatrix[0xF9] = new Opcode("SBC" , "ABY" , 4);
		OpcodeMatrix[0xE1] = new Opcode("SBC" , "XIN" , 6);
		OpcodeMatrix[0xF1] = new Opcode("SBC" , "INY" , 5);

		//SEC
		OpcodeMatrix[0x38] = new Opcode("SEC" , "IMP" , 2);

		//SED
		OpcodeMatrix[0xF8] = new Opcode("SED" , "IMP" , 2);

		//SEI
		OpcodeMatrix[0x78] = new Opcode("SEI" , "IMP" , 2);

		//STA
		OpcodeMatrix[0x85] = new Opcode("STA" , "ZPG" , 3);
		OpcodeMatrix[0x95] = new Opcode("STA" , "ZPX" , 4);
		OpcodeMatrix[0x8D] = new Opcode("STA" , "ABS" , 4);
		OpcodeMatrix[0x9D] = new Opcode("STA" , "ABX" , 5);
		OpcodeMatrix[0x99] = new Opcode("STA" , "ABY" , 5);
		OpcodeMatrix[0x81] = new Opcode("STA" , "XIN" , 6);
		OpcodeMatrix[0x91] = new Opcode("STA" , "INY" , 6);

		//STX
		OpcodeMatrix[0x86] = new Opcode("STX" , "ZPG" , 3);
		OpcodeMatrix[0x96] = new Opcode("STX" , "ZPY" , 4);
		OpcodeMatrix[0x8E] = new Opcode("STX" , "ABS" , 4);

		//STY
		OpcodeMatrix[0x84] = new Opcode("STY" , "ZPG" , 3);
		OpcodeMatrix[0x94] = new Opcode("STY" , "ZPX" , 4);
		OpcodeMatrix[0x8C] = new Opcode("STY" , "ABS" , 4);

		//TAX
		OpcodeMatrix[0xAA] = new Opcode("TAX" , "IMP" , 2);

		//TAY
		OpcodeMatrix[0xA8] = new Opcode("TAY" , "IMP" , 2);

		//TSX
		OpcodeMatrix[0xBA] = new Opcode("TSX" , "IMP" , 2);

		//TXA
		OpcodeMatrix[0x8A] = new Opcode("TXA" , "IMP" , 2);

		//TXS
		OpcodeMatrix[0x9A] = new Opcode("TXS" , "IMP" , 2);

		//TYA
		OpcodeMatrix[0x98] = new Opcode("TYA" , "IMP" , 2);

	}

	public void resetCPU()
	{
		byAccumulator = 0x00;

		byStackPointer = (byte) 0xFD;

		byIndexX = 0x00;
		byIndexY = 0x00;

		byStatusFlags = 0b00100000;

		bInterrupt = false;
		bNonMaskableInterrupt = false;

		lCurrentClockCycles = 0;

	}

	//===================================================================
	//Interrupts
	//===================================================================

	public void nonMaskableInterrupt()
	{
		setStatusFlag('I' , true);

		Memory.write((short) getSP(), (byte) (shProgramCounter >> 8));
		byStackPointer--;

		Memory.write((short) getSP(), (byte) (shProgramCounter));
		byStackPointer--;

		Memory.write((short) getSP() , byStatusFlags);
		byStackPointer--;

		nmiVector();
	}

	private void interrupt()
	{	
		Memory.write((short) getSP(), (byte) (shProgramCounter >> 8));
		byStackPointer = (byte) (getSP() - 1);

		Memory.write((short) getSP(), (byte) (shProgramCounter));
		byStackPointer = (byte) (getSP() - 1);

		setStatusFlag('B' , true);
		setStatusFlag('U' , true);
		
		Memory.write((short) getSP() , byStatusFlags);
		byStackPointer = (byte) (getSP() - 1);
		
		setStatusFlag('I' , true);

		irqVector();

	}

	//===================================================================
	//Program Vectors
	//===================================================================

	private void nmiVector()
	{
		setStatusFlag('I' , true);
		shProgramCounter = getVector((short) 0xFFFA , (short) 0xFFFB);

	}

	private void irqVector()
	{
		shProgramCounter = getVector((short) 0xFFFE , (short) 0xFFFF);

	}

	public void resetVector()
	{
		resetCPU();

		byStackPointer = (byte) 0xFD;
		byStatusFlags = (byte) (0x00 | (getStatusFlag('-') ? 0b00000100 : 0));

		shProgramCounter = getVector((short) 0xFFFC , (short) 0xFFFD);

	}

	private short getVector(short byLoByteLocation , short byHiByteLocation)
	{
		return (short) ((Memory.read(Short.toUnsignedInt(byLoByteLocation)) + 256 * (Memory.read(Short.toUnsignedInt(byHiByteLocation)))));

	}

	//===================================================================
	//Getters
	//===================================================================

	private int getA() 
	{
		return Byte.toUnsignedInt(byAccumulator);

	}

	public int getPC() 
	{
		return Short.toUnsignedInt(shProgramCounter);

	}

	private int getSP() 
	{
		return Short.toUnsignedInt((short) ((0x0100) + Byte.toUnsignedInt(byStackPointer)));

	}

	private int getX() 
	{
		return Byte.toUnsignedInt(byIndexX);

	}

	private int getY() 
	{
		return Byte.toUnsignedInt(byIndexY);

	}

	private int getHI() 
	{
		return Byte.toUnsignedInt(byHiByte);

	}

	private int getLO() 
	{
		return Byte.toUnsignedInt(byLoByte);

	}

	public long getCurrentClockCycles() 
	{
		return lCurrentClockCycles;

	}

	public boolean getisRunning() 
	{
		return bRunning;

	}

	//===================================================================
	//Setters
	//===================================================================

	public void setisRunning(boolean bRunning) 
	{
		this.bRunning = bRunning;

	}

	//===================================================================
	//Runtime stuff
	//===================================================================

	public CPU6502(RAM ComputerRAM)
	{
		Memory = ComputerRAM;

		initaliseOpcodeMatrix();

		resetCPU();

	}

	//===================================================================
	//Debug
	//===================================================================

	public void dumpCPU()
	{
		System.out.print("\n");
		System.out.println("---------- CPU STATS ----------");
		System.out.println("A : " + Integer.toHexString(Byte.toUnsignedInt(byAccumulator)).toUpperCase());
		System.out.println("X : " + Integer.toHexString(Byte.toUnsignedInt(byIndexX)).toUpperCase());
		System.out.println("Y : " + Integer.toHexString(Byte.toUnsignedInt(byIndexY)).toUpperCase());
		System.out.print("\n");

		System.out.println("PC : " + Integer.toHexString(Short.toUnsignedInt(shProgramCounter)).toUpperCase() + " (0x0000 - 0xFFFF)");
		System.out.println("SP : " + Integer.toHexString(getSP()).toUpperCase() + " (0x0100 - 0x01FF)");
		System.out.print("\n");

		System.out.println("SF : " + Integer.toHexString(Byte.toUnsignedInt(byStatusFlags)));
		System.out.println("C (0) : " + getStatusFlag('C'));
		System.out.println("Z (1) : " + getStatusFlag('Z'));
		System.out.println("I (2) : " + getStatusFlag('I'));
		System.out.println("D (3) : " + getStatusFlag('D'));
		System.out.println("B (4) : " + getStatusFlag('B'));
		System.out.println("- (5) : " + getStatusFlag('-'));
		System.out.println("O (6) : " + getStatusFlag('O'));
		System.out.println("N (7) : " + getStatusFlag('N'));
		System.out.print("\n");

		System.out.println("IRQ? : " + bInterrupt);
		System.out.println("NMI? : " + bNonMaskableInterrupt);
		System.out.print("\n");

		System.out.println("Clock : " + lCurrentClockCycles);
		System.out.println("---------- END CPU STATS ----------");
		System.out.print("\n");

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
			byStatusFlags = setBit(byStatusFlags , 0 , bBit);

		break;
		case('Z'):
			byStatusFlags = setBit(byStatusFlags , 1 , bBit);

		break;
		case('I'):
			byStatusFlags = setBit(byStatusFlags , 2 , bBit);

		break;
		case('D'):
			byStatusFlags = setBit(byStatusFlags , 3 , bBit);

		break;
		case('B'):
			byStatusFlags = setBit(byStatusFlags , 4 , bBit);

		break;
		//UNUSED
		case('U'):
			byStatusFlags = setBit(byStatusFlags , 5 , bBit);

		break;
		case('O'):
			byStatusFlags = setBit(byStatusFlags , 6 , bBit);

		break;
		case('N'):
			byStatusFlags = setBit(byStatusFlags , 7 , bBit);

		break;

		}

	}

	private boolean getStatusFlag(char chFlag)
	{
		chFlag = Character.toUpperCase(chFlag);

		switch(chFlag) {
		case('C'):
			return getBit(byStatusFlags , 0);

		case('Z'):
			return getBit(byStatusFlags , 1);

		case('I'):
			return getBit(byStatusFlags , 2);

		case('D'):
			return getBit(byStatusFlags , 3);

		case('B'):
			return getBit(byStatusFlags , 4);

		//UNUSED
		case('U'):
			return getBit(byStatusFlags , 5);

		case('O'):
			return getBit(byStatusFlags , 6);

		case('N'):
			return getBit(byStatusFlags , 7);

		default:
			return false;

		}

	}

	private void checkZero(byte byRegister)
	{
		setStatusFlag('Z' , (Byte.toUnsignedInt(byRegister) == 0x00));

	}

	private void checkNegative(byte byRegister)
	{
		setStatusFlag('N' , (byRegister & 0x80) == 0x80);

	}

	private int binaryToBCD(byte byByte) 
	{
			return (((byByte / 10) % 10) << 4) | (byByte % 10);

	}

	private int bcdToBinary(byte byByte) 
	{
		return ((byByte >> 4) * 10) + (byByte & 0xf);

	}

	//===================================================================
	//Fetch-Decode-Execute Cycle
	//===================================================================

	public void executeInstruction()
	{
		byte byInstruction = (byte) 0x00;

		short shOperand = (short) 0x0000;

		byHiByte = 0x00;
		byLoByte = 0x00;

				//System.out.print(Integer.toHexString(Short.toUnsignedInt(shProgramCounter)).toUpperCase() + "	:   ");

		byInstruction = fetchNextInstruction();
		incrementPC();

				//System.out.print(OpcodeMatrix[Byte.toUnsignedInt(byInstruction)].getOperation() + " ");

		shOperand = getOperandByAddressMode(byInstruction , shOperand);

				//System.out.println(Integer.toHexString(Short.toUnsignedInt(shOperand)).toUpperCase());

		executeOperation(byInstruction , shOperand);

		lCurrentClockCycles = OpcodeMatrix[Byte.toUnsignedInt(byInstruction)].getOpcodeClockCycles();

	}

	private void incrementPC()
	{
		shProgramCounter = (short) (getPC() + 1);

	}

	private byte fetchNextInstruction()
	{
		return (byte) Memory.read(getPC());

	}

	//===================================================================
	//Getting Operand
	//===================================================================

	private short getOperandByAddressMode(byte byInstruction , short shOperand)
	{
		//Modes:
		//ACC - byAccumulator
		//ABS - Absolute
		//ABX - Absolute, X-indexed
		//ABY - Absolute, Y-indexed
		//IMM - Immediate
		//IMP - Implied
		//IND - Indirect
		//XIN - X-indexed, indirect
		//INY - Indirect, Y-indexed	
		//REL - Relative
		//ZPG - Zero page
		//ZPX - Zero page, X-indexed	
		//ZPY - Zero page, Y-indexed	

		switch(OpcodeMatrix[Byte.toUnsignedInt(byInstruction)].getAddressingMode())
		{
		case("ACC"):
			shOperand = (short) getA();

		break;
		case("ABS"):
			byLoByte = (byte) Memory.read(getPC());
		incrementPC();

		byHiByte = (byte) Memory.read(getPC());
		incrementPC();

		shOperand = (short) (getLO() + 256 * getHI());

		break;
		case("ABX"):
			byLoByte = (byte) Memory.read(getPC());
		incrementPC();

		byHiByte = (byte) Memory.read(getPC());
		incrementPC();

		shOperand = (short) (getLO() + 256 * getHI() + getX());

		if ((shOperand & 0xFF00) != (byHiByte << 8)) lCurrentClockCycles++;

		break;
		case("ABY"):
			byLoByte = (byte) Memory.read(getPC());
		incrementPC();

		byHiByte = (byte) Memory.read(getPC());
		incrementPC();

		shOperand = (short) (getLO() + 256 * getHI() + getY());

		if ((shOperand & 0xFF00) != (byHiByte << 8)) lCurrentClockCycles++;

		break;
		case("IMM"):
			byLoByte = (byte) Memory.read(getPC());
		incrementPC();

		shOperand = (short) getLO();

		break;
		case("IMP"):
			//Completed

			break;
		case("IND"):
			byLoByte = (byte) Memory.read(getPC());
		incrementPC();

		byHiByte = (byte) Memory.read(getPC());
		incrementPC();

		short shPointer = (short) ((byHiByte << 8) | byLoByte);

		shOperand = (short) (Byte.toUnsignedInt((byte) (byte) Memory.read(Short.toUnsignedInt((short) (shPointer + 1)))) * 256 + Byte.toUnsignedInt((byte) Memory.read(Short.toUnsignedInt(shPointer))));

		break;
		case("XIN"):
			byte byOffset = (byte) Memory.read(getPC());
		incrementPC();

		byLoByte = (byte) Memory.read((short) ((byOffset + getX()) & 0x00FF));
		byHiByte = (byte) Memory.read((short) ((byOffset + getX() + 1) & 0x00FF));

		shOperand = (short) (Byte.toUnsignedInt(byLoByte) + 256 * Byte.toUnsignedInt(byHiByte));

		break;
		case("INY"):
			byOffset = (byte) Memory.read(getPC());
		incrementPC();

		byLoByte = (byte) Memory.read((short) (byOffset & 0x00FF));
		byHiByte = (byte) Memory.read((short) ((byOffset + 1) & 0x00FF));

		shOperand = (short) (Byte.toUnsignedInt(byLoByte) + 256 * Byte.toUnsignedInt(byHiByte) + getY());

		if ((shOperand & 0xFF00) != (byHiByte << 8)) lCurrentClockCycles++;

		break;
		case("REL"):
			shOperand = (short) Memory.read(getPC());
		incrementPC();

		//Operand = (short) (byLoByte + getPC());

		if ((shOperand & 0x80) == 0x80)
		{
			shOperand |= 0xFF00;

		}

		break;
		case("ZPG"):
			shOperand = (short) Memory.read(getPC());
		incrementPC();

		shOperand = (short) (shOperand & 0x00FF);

		break;
		case("ZPX"):
			shOperand = (short) (Memory.read(getPC()));
		incrementPC();

		shOperand = (short) (Short.toUnsignedInt(shOperand) + getX());

		shOperand = (short) (shOperand & 0x00FF);

		break;
		case("ZPY"):
			shOperand = (short) (Memory.read(getPC()));
		incrementPC();

		shOperand = (short) (Short.toUnsignedInt(shOperand) + getY());

		shOperand = (short) (shOperand & 0x00FF);

		break;
		default:
			System.err.println("Fatal error when trying to get the operand by addressing mode!");
			dumpCPU();

			break;

		}

		return shOperand;

	}

	private byte getOperand(byte byOpcode , short shAddress)
	{
		byte byFetchedOperand = 0x00;

		if ((OpcodeMatrix[Byte.toUnsignedInt(byOpcode)].getAddressingMode().equalsIgnoreCase("ACC")) || (OpcodeMatrix[Byte.toUnsignedInt(byOpcode)].getAddressingMode().equalsIgnoreCase("IMM")) || (OpcodeMatrix[Byte.toUnsignedInt(byOpcode)].getAddressingMode().equalsIgnoreCase("REL")))
		{
			byFetchedOperand = (byte) shAddress;

		}
		else if(!(OpcodeMatrix[Byte.toUnsignedInt(byOpcode)].getAddressingMode().equalsIgnoreCase("IMP")))
		{
			byFetchedOperand = (byte) Memory.read(Short.toUnsignedInt(shAddress));

		}

		return byFetchedOperand;

	}

	//===================================================================
	//Executing Instruction
	//===================================================================

	private void executeOperation(byte byInstruction , short shAddress)
	{
		byte byFetchedOperand = (byte) 0x00;

		switch(OpcodeMatrix[Byte.toUnsignedInt(byInstruction)].getOperation())
		{
		case("ADC"):
			byFetchedOperand = getOperand(byInstruction , shAddress); 

		byAccumulator = add((byte) (byFetchedOperand));

		break;
		case("AND"):
			byFetchedOperand = getOperand(byInstruction , shAddress);

		byAccumulator = (byte) (getA() & byFetchedOperand);

		checkZero(byAccumulator);
		checkNegative(byAccumulator);

		break;
		case("ASL"):
			short shShiftingOperand;

		shShiftingOperand = (short) getOperand(byInstruction , shAddress);

		shShiftingOperand = (short) (shShiftingOperand << 1);

		setStatusFlag('C' , ((shShiftingOperand & 0b0000000100000000) == 0b0000000100000000));
		checkZero((byte) shShiftingOperand);
		checkNegative((byte) shShiftingOperand);

		if(OpcodeMatrix[Byte.toUnsignedInt(byInstruction)].getAddressingMode().equalsIgnoreCase("ACC"))
		{
			byAccumulator = (byte) shShiftingOperand;

		}
		else
		{
			Memory.write((short) Short.toUnsignedInt(shAddress) , (byte) Byte.toUnsignedInt((byte) shShiftingOperand));

		}

		break;
		case("BCC"):
			if(getStatusFlag('C') == false)
			{
				shProgramCounter = (short) ((short) Short.toUnsignedInt(shAddress) + getPC());

				if ((((short) Short.toUnsignedInt(shAddress) + getPC()) & 0xFF00) != (getPC() & 0xFF00)) lCurrentClockCycles++;

			}

		break;
		case("BCS"):
			if(getStatusFlag('C') == true)
			{
				shProgramCounter = (short) ((short) Short.toUnsignedInt(shAddress) + getPC());

				if ((((short) Short.toUnsignedInt(shAddress) + getPC()) & 0xFF00) != (getPC() & 0xFF00)) lCurrentClockCycles++;

			}

		break;
		case("BEQ"):
			if(getStatusFlag('Z') == true)
			{
				shProgramCounter = (short) ((short) Short.toUnsignedInt(shAddress) + getPC());

				if ((((short) Short.toUnsignedInt(shAddress) + getPC()) & 0xFF00) != (getPC() & 0xFF00)) lCurrentClockCycles++;

			}

		break;
		case("BIT"):	
			byFetchedOperand = getOperand(byInstruction , shAddress);	

		checkZero((byte) (byAccumulator & byFetchedOperand));
		setStatusFlag('N' , ((byFetchedOperand & 0b10000000) == 0b10000000));
		setStatusFlag('O' , (byFetchedOperand & 0b01000000) == 0b01000000);

		break;
		case("BMI"):
			if(getStatusFlag('N') == true)
			{
				shProgramCounter = (short) ((short) Short.toUnsignedInt(shAddress) + getPC());

				if ((((short) Short.toUnsignedInt(shAddress) + getPC()) & 0xFF00) != (getPC() & 0xFF00)) lCurrentClockCycles++;

			}

		break;
		case("BNE"):
			if(getStatusFlag('Z') == false)
			{
				shProgramCounter = (short) ((short) Short.toUnsignedInt(shAddress) + getPC());

				if ((((short) Short.toUnsignedInt(shAddress) + getPC()) & 0xFF00) != (getPC() & 0xFF00)) lCurrentClockCycles++;

			}

		break;
		case("BPL"):
			if(getStatusFlag('N') == false)
			{
				shProgramCounter = (short) ((short) Short.toUnsignedInt(shAddress) + getPC());

				if ((((short) Short.toUnsignedInt(shAddress) + getPC()) & 0xFF00) != (getPC() & 0xFF00)) lCurrentClockCycles++;

			}

		break;
		case("BRK"):
			incrementPC();

		interrupt();

		break;
		case("BVC"):
			if(getStatusFlag('O') == false)
			{
				shProgramCounter = (short) ((short) Short.toUnsignedInt(shAddress) + getPC());

				if ((((short) Short.toUnsignedInt(shAddress) + getPC()) & 0xFF00) != (getPC() & 0xFF00)) lCurrentClockCycles++;

			}

		break;
		case("BVS"):
			if(getStatusFlag('O') == true)
			{
				shProgramCounter = (short) ((short) Short.toUnsignedInt(shAddress) + getPC());

				if ((((short) Short.toUnsignedInt(shAddress) + getPC()) & 0xFF00) != (getPC() & 0xFF00)) lCurrentClockCycles++;

			}

		break;
		case("CLC"):
			setStatusFlag('C' , false);

		break;
		case("CLD"):
			setStatusFlag('D' , false);

		break;
		case("CLI"):
			setStatusFlag('I' , false);

		break;
		case("CLV"):
			setStatusFlag('O' , false);

		break;
		case("CMP"):
			byte byCompareResult;

		byFetchedOperand = getOperand(byInstruction , shAddress);	

		byCompareResult = (byte) (getA() - Byte.toUnsignedInt(byFetchedOperand));

		setStatusFlag('C' , (Byte.toUnsignedInt(byFetchedOperand) <= getA()));
		checkZero(byCompareResult);
		checkNegative(byCompareResult);

		break;
		case("CPX"):
			byFetchedOperand = getOperand(byInstruction , shAddress);	

		byCompareResult = (byte) (getX() - Byte.toUnsignedInt(byFetchedOperand));

		setStatusFlag('C' , (Byte.toUnsignedInt(byFetchedOperand) <= getX()));
		checkZero(byCompareResult);
		checkNegative(byCompareResult);


		break;
		case("CPY"):
			byFetchedOperand = getOperand(byInstruction , shAddress);	

		byCompareResult = (byte) (getY() - Byte.toUnsignedInt(byFetchedOperand));

		setStatusFlag('C' , (Byte.toUnsignedInt(byFetchedOperand) <= getY()));
		checkZero(byCompareResult);
		checkNegative(byCompareResult);


		break;
		case("DEC"):
			byFetchedOperand = getOperand(byInstruction , shAddress);	

		Memory.write((short) Short.toUnsignedInt(shAddress) , (byte) Byte.toUnsignedInt((byte) (byFetchedOperand - 1)));

		checkZero((byte) Memory.read(Short.toUnsignedInt(shAddress)));
		checkNegative((byte) Memory.read(Short.toUnsignedInt(shAddress)));

		break;
		case("DEX"):
			byIndexX = (byte) (getX() - 1);

		checkZero(byIndexX);
		checkNegative(byIndexX);

		break;
		case("DEY"):
			byIndexY = (byte) (getY() - 1);

		checkZero(byIndexY);
		checkNegative(byIndexY);

		break;
		case("EOR"):
			byFetchedOperand = getOperand(byInstruction , shAddress);	

		byAccumulator = (byte) (byAccumulator ^ Byte.toUnsignedInt(byFetchedOperand));

		checkZero(byAccumulator);
		checkNegative(byAccumulator);

		break;
		case("INC"):
			byFetchedOperand = getOperand(byInstruction , shAddress);	

		Memory.write((short) Short.toUnsignedInt(shAddress) , (byte) Byte.toUnsignedInt((byte) (byFetchedOperand + 1)));

		checkZero((byte) Memory.read(Short.toUnsignedInt(shAddress)));
		checkNegative((byte) Memory.read(Short.toUnsignedInt(shAddress)));

		break;
		case("INX"):
			byIndexX = (byte) (getX() + 1);

		checkZero(byIndexX);
		checkNegative(byIndexX);

		break;
		case("INY"):
			byIndexY = (byte) (getY() + 1);

		checkZero(byIndexY);
		checkNegative(byIndexY);

		break;
		case("JMP"):
			shProgramCounter = shAddress;

		break;
		case("JSR"):
			shProgramCounter--;

		Memory.write((short) ((short) 0x0100+Byte.toUnsignedInt(byStackPointer)) , (byte) ((shProgramCounter >> 8) & 0x00FF));
		byStackPointer--;

		Memory.write((short) ((short) 0x0100+Byte.toUnsignedInt(byStackPointer)) , (byte) (shProgramCounter & 0x00FF));
		byStackPointer--;

		shProgramCounter = shAddress;

		break;
		case("LDA"):
			byFetchedOperand = getOperand(byInstruction , shAddress);

		byAccumulator = byFetchedOperand;

		//System.out.println(Short.toUnsignedInt(Operand));
		//System.out.println(Integer.toBinaryString(byAccumulator));
		checkZero(byAccumulator);
		checkNegative(byAccumulator);

		break;
		case("LDX"):
			byFetchedOperand = getOperand(byInstruction , shAddress);

		byIndexX = byFetchedOperand;

		checkZero(byIndexX);
		checkNegative(byIndexX);

		break;
		case("LDY"):
			byFetchedOperand = getOperand(byInstruction , shAddress);

		byIndexY = byFetchedOperand;

		checkZero(byIndexY);
		checkNegative(byIndexY);

		break;
		case("LSR"):
			byFetchedOperand = getOperand(byInstruction , shAddress);

		setStatusFlag('C',(byFetchedOperand & 0x0001) == 0x0001);
		short temp = (short)((0x00FF & byFetchedOperand) >> 1);
		setStatusFlag('Z',(temp & 0x00FF)==0x0000);
		setStatusFlag('N',(temp & 0x0080)==0x0080);

		if(OpcodeMatrix[Byte.toUnsignedInt(byInstruction)].getAddressingMode().equalsIgnoreCase("ACC"))
		{
			byAccumulator = (byte) temp;

		}
		else
		{
			Memory.write((short) Short.toUnsignedInt(shAddress) , (byte) Short.toUnsignedInt(temp));

		}


		break;
		case("NOP"):

			break;
		case("ORA"):
			byFetchedOperand = getOperand(byInstruction , shAddress);

		byAccumulator = (byte) (getA() | Byte.toUnsignedInt(byFetchedOperand));

		checkZero(byAccumulator);
		checkNegative(byAccumulator);

		break;
		case("PHA"):
			Memory.write((short) getSP() , byAccumulator);

		byStackPointer--;
		
		break;
		case("PHP"):
			Memory.write((short) getSP() , (byte) (byStatusFlags | 0b00110000));
			//Memory.write((short) getSP() , byStatusFlags);

		byStackPointer--;
		
		setStatusFlag('B' , false);
		setStatusFlag('U' , false);

		break;
		case("PLA"):
			byStackPointer++;

		byAccumulator = (byte) Memory.read(getSP());

		checkZero(byAccumulator);
		checkNegative(byAccumulator);

		break;
		case("PLP"):
			byStackPointer++;

		byStatusFlags = (byte) Memory.read(getSP());

		setStatusFlag('U' , true);

		break;
		case("ROL"):

			shShiftingOperand = (short) getOperand(byInstruction , shAddress);

		shShiftingOperand = (short) (shShiftingOperand << 1);

		boolean byTemp = ((shShiftingOperand & 0b0000000100000000) == 0b0000000100000000);
		shShiftingOperand = setBit((byte) shShiftingOperand , 0 , getStatusFlag('C'));
		setStatusFlag('C' , byTemp);

		checkZero((byte) shShiftingOperand);
		checkNegative((byte) shShiftingOperand);

		if(OpcodeMatrix[Byte.toUnsignedInt(byInstruction)].getAddressingMode().equalsIgnoreCase("ACC"))
		{
			byAccumulator = (byte) shShiftingOperand;

		}
		else
		{
			Memory.write((short) Short.toUnsignedInt(shAddress) , (byte) Byte.toUnsignedInt((byte) shShiftingOperand));

		}

		break;
		case("ROR"):
			byte byShiftingOperand = getOperand(byInstruction , shAddress);

	 temp = (short)(((0x00FF&byShiftingOperand)>>1) | (short)(getStatusFlag('C') ? 0x0080 : 0));

		setStatusFlag('C',(byShiftingOperand & 0x01) == 0x01);
		setStatusFlag('Z',(temp & 0x00FF) == 0x0000);
		setStatusFlag('N',(temp & 0x0080) == 0x0080);

		if(OpcodeMatrix[Byte.toUnsignedInt(byInstruction)].getAddressingMode().equalsIgnoreCase("ACC"))
		{
			byAccumulator = (byte) temp;

		}
		else
		{
			Memory.write((short) Short.toUnsignedInt(shAddress) , (byte) Byte.toUnsignedInt((byte) temp));

		}

		break;
		case("RTI"):
			byStackPointer++;
			
			byStatusFlags = (byte) Memory.read(getSP());
			byStatusFlags = (byte)(byStatusFlags & (getStatusFlag('B') ? 0b11101111 : 0));
			byStatusFlags = (byte)(byStatusFlags & (getStatusFlag('-') ? 0b11011111 : 0));

			byStackPointer++;
		byLoByte = (byte) Memory.read(getSP());
		
		byStackPointer++;
		byHiByte = (byte) Memory.read(getSP());

		shProgramCounter = (short) ((Byte.toUnsignedInt(byHiByte) * 256) + Byte.toUnsignedInt(byLoByte));

		break;
		case("RTS"):
			byStackPointer++;
			byLoByte = (byte) Memory.read(getSP());

		byStackPointer++;
		byHiByte = (byte) Memory.read(getSP());

		shProgramCounter = (short) (getLO() + 256 * getHI());

		incrementPC();

		break;
		case("SBC"):
			byFetchedOperand = getOperand(byInstruction , shAddress);

		byAccumulator = add((byte) (~byFetchedOperand));

		break;
		case("SEC"):
			setStatusFlag('C' , true);

		break;
		case("SED"):
			setStatusFlag('D' , true);

		break;
		case("SEI"):
			setStatusFlag('I' , true);

		break;
		case("STA"):
			Memory.write((short) Short.toUnsignedInt(shAddress) , byAccumulator);

		break;
		case("STX"):
			Memory.write((short) Short.toUnsignedInt(shAddress) , byIndexX);

		break;
		case("STY"):
			Memory.write((short) Short.toUnsignedInt(shAddress) , byIndexY);

		break;
		case("TAX"):
			byIndexX = byAccumulator;

		checkZero(byIndexX);
		checkNegative(byIndexX);

		break;
		case("TAY"):
			byIndexY = byAccumulator;

		checkZero(byIndexY);
		checkNegative(byIndexY);

		break;
		case("TSX"):
			byIndexX = byStackPointer;

		checkZero(byIndexX);
		checkNegative(byIndexX);

		break;
		case("TXA"):
			byAccumulator = byIndexX;

		checkZero(byAccumulator);
		checkNegative(byAccumulator);

		break;
		case("TXS"):
			byStackPointer = byIndexX;

		break;
		case("TYA"):
			byAccumulator = byIndexY;

		checkZero(byAccumulator);
		checkNegative(byAccumulator);

		break;
		case("XXX"):
			System.err.println("Error: Illegal or unrecognised Opcode encountered!");
		dumpCPU();

		break;
		default:
			System.err.println("Fatal error.");
			dumpCPU();

			break;

		}

	}
	
	private byte add(byte byFetchedByte) 
	{
		if(getStatusFlag('D') == true)
		{
			byFetchedByte = (byte) bcdToBinary(byFetchedByte);
		}
		
		short shAdcTemp = (short) ((short) getA() + (byFetchedByte & 0xFF) + (getStatusFlag('C') ? 1 : 0));

		setStatusFlag('C' , shAdcTemp > 255);
		setStatusFlag('Z' , (shAdcTemp & 0x00FF) == 0);
		setStatusFlag('N' , (shAdcTemp & 0x80) == 0x80);
		setStatusFlag('O' , (~((short) byAccumulator ^ (short) byFetchedByte) & ((short) byAccumulator ^ (short) shAdcTemp) & 0x0080) == 0x0080);
		
		if(getStatusFlag('D') == true)
		{
			shAdcTemp = (short) binaryToBCD((byte) shAdcTemp);
		}
		
		return (byte) shAdcTemp;
		
	}

}
