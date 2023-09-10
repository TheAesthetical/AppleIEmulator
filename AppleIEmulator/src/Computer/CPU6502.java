package Computer;

import java.util.concurrent.TimeUnit;

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

	public String getOperation() 
	{
		return szOperation;

	}

	public String getAddressingMode() 
	{
		return szAddressingMode;
	}

	public int getClockCycles() 
	{
		return iClockCycles;

	}

}

public class CPU6502 implements Runnable {

	private final int ClockSpeedHZ = 1023000;

	//!!!DELETE STATIC MODIFIERS AFTER COMPLETION!!!

	private RAM Memory;

	private byte Accumulator = (byte) 0x00;

	private short ProgramCounter = (short) 0x0000;
	private byte StackPointer = (byte) 0x00; //0x0100 - 0x01FF

	private byte IndexX = (byte) 0x00;
	private byte IndexY = (byte) 0x00;

	private byte StatusFlags = (byte) 0x00;

	private boolean bInterrupt = false;
	private boolean bNonMaskableInterrupt = false;

	private Opcode[] OpcodeMatrix = new Opcode[255];

	private byte LoByte = (byte) 0x00;
	private byte HiByte = (byte) 0x00;

	private long ClockCycles = 0;

	private void initaliseOpcodeMatrix() 
	{
		//Modes:
		//ACC - Accumulator
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
		OpcodeMatrix[0x39] = new Opcode("SEC" , "IMP" , 2);

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
		Accumulator = 0x00;

		StackPointer = 0x00;

		IndexX = 0x00;
		IndexY = 0x00;

		StatusFlags = 0x00;

		bInterrupt = false;
		bNonMaskableInterrupt = false;

		ClockCycles = 0;

		initaliseOpcodeMatrix();
		

	}
	
	public void resetPC()
	{
		ProgramCounter = resetVector((short) 0xFFFC , (short) 0xFFFD);
		
	}

	private short resetVector(short LoByteLocation , short HiByteLocation)
	{
		return (short) ((Memory.readFromMemoryLocation(Short.toUnsignedInt(LoByteLocation)) + 256 * (Memory.readFromMemoryLocation(Short.toUnsignedInt(HiByteLocation)))));

	}
	
	//===================================================================
	//Getters
	//===================================================================
	
	private int getA() 
	{
		return Byte.toUnsignedInt(Accumulator);
		
	}
	
	private int getPC() 
	{
		return Short.toUnsignedInt(ProgramCounter);
		
	}
	
	private int getSP() 
	{
		return Short.toUnsignedInt((short) ((0x01) + 256 * (Byte.toUnsignedInt(StackPointer))));
		
	}
	
	private int getX() 
	{
		return Byte.toUnsignedInt(IndexX);
		
	}
	
	private int getY() 
	{
		return Byte.toUnsignedInt(IndexY);
		
	}
	
//	private int getSF() 
//	{
//		return Byte.toUnsignedInt(StatusFlags);
//		
//	}
	
	private int getHI() 
	{
		return Byte.toUnsignedInt(HiByte);
		
	}
	
	private int getLO() 
	{
		return Byte.toUnsignedInt(LoByte);
		
	}


	//===================================================================
	//Runtime stuff
	//===================================================================

	public CPU6502(RAM ComputerRAM) throws InterruptedException
	{
		Memory = ComputerRAM;
		
		resetCPU();
		
		//Test for ACI
		//ProgramCounter = (short) 0xC100;

	}

	@Override
	public void run() 
	{

	}

	//===================================================================
	//Debug
	//===================================================================

	private void disassemble()
	{

	}

	public void dumpCPU()
	{
		System.out.print("\n");
		System.out.println("---------- CPU STATS ----------");
		System.out.println("A : " + Integer.toHexString(Byte.toUnsignedInt(Accumulator)).toUpperCase());
		System.out.println("X : " + Integer.toHexString(Byte.toUnsignedInt(IndexX)).toUpperCase());
		System.out.println("Y : " + Integer.toHexString(Byte.toUnsignedInt(IndexY)).toUpperCase());
		System.out.print("\n");

		System.out.println("PC : " + Integer.toHexString(Short.toUnsignedInt(ProgramCounter)).toUpperCase() + " (0x0000 - 0xFFFF)");
		System.out.println("SP : " + Integer.toHexString(getSP()).toUpperCase() + " (0x0100 - 0x01FF)");
		System.out.print("\n");

		System.out.println("SF : " + Integer.toHexString(Byte.toUnsignedInt(StatusFlags)));
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

		System.out.println("Clock : " + ClockCycles);
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

	//Arithmetic Status Flag checks that would be done by the ALU

	private void checkCarry(byte Register)
	{
		setStatusFlag('C' , (Register & 0x0001) == 0x0001);

	}

	private void checkZero(byte Register)
	{
		setStatusFlag('Z' , Byte.toUnsignedInt(Register) == 0x00);

	}

	private void checkOverflow(byte RequestedRegister , byte FetchedByte)
	{
		setStatusFlag('O' , (~ ((short) Accumulator ^ (short)FetchedByte) & ((short) Accumulator ^ (short) RequestedRegister) & 0x0080) == 0x0080);

	}

	private void checkNegative(byte Register)
	{
		setStatusFlag('N' , (Register & 0x80) == 0x80);
	}

	private int oppositeComplement(int Word) 
	{
		return (Word < 128) ? Word : -1 * ((Word ^ 0xff) + 1);

	}

	private int binaryToBCD(int Word) 
	{
		return (((Word / 10) % 10) << 4) | (Word % 10);
	}

	private int bcdTo1Binary(int Word) 
	{
		return ((Word >> 4) * 10) + (Word & 0xf);

	}

	//===================================================================
	//Fetch-Decode-Execute Cycle
	//===================================================================

	public void startCycle() throws InterruptedException
	{
		do
		{

			long preCycleTime = System.nanoTime();

			cycle();

			long postCycleTime = System.nanoTime();

			long CycleTime = (postCycleTime - preCycleTime) / 1000;
			//System.out.println(CycleTime);
			//System.out.println(ClockCycles - CycleTime);

			TimeUnit.MICROSECONDS.sleep(ClockCycles - CycleTime);

			//HAS TO BE AN EVEN INTEGER IN EXPRESSION OTHERWISE THERE WILL BE AN INFINITE LOOP
		} while (getPC() != 0x0000);
		
		dumpCPU();

	}

	private void cycle() throws InterruptedException
	{
		byte Opcode = (byte) 0x00;

		short Operand = (short) 0x0000;
		HiByte = 0x00;
		LoByte = 0x00;

		System.out.print(Integer.toHexString(Short.toUnsignedInt(ProgramCounter)).toUpperCase() + "	:   ");

		Opcode = fetchOpcode();
		incrementPC();

		//System.out.print(Integer.toHexString(Byte.toUnsignedInt(Opcode)).toUpperCase() + " ");
		System.out.print(OpcodeMatrix[Byte.toUnsignedInt(Opcode)].getOperation() + " ");

		Operand = getOperandByAddressMode(Opcode , Operand);

		System.out.println(Integer.toHexString(Short.toUnsignedInt(Operand)).toUpperCase());

		//executeInstruction(Opcode , Operand);
		
		//For while im completing the instructions this is going to have to work to increment the PC if a BRK occurs
		if(OpcodeMatrix[Byte.toUnsignedInt(Opcode)].getOperation().equalsIgnoreCase("BRK")) incrementPC();

		ClockCycles = OpcodeMatrix[Byte.toUnsignedInt(Opcode)].getClockCycles();

	}

	private void incrementPC()
	{
		ProgramCounter = (short) (getPC() + 1);

	}

	private byte fetchOpcode()
	{
		return (byte) Memory.readFromMemoryLocation(getPC());

	}

	private short getOperandByAddressMode(byte Opcode , short Operand)
	{
		//Modes:
		//ACC - Accumulator
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

		switch(OpcodeMatrix[Byte.toUnsignedInt(Opcode)].getAddressingMode())
		{
		case("ACC"):
			Operand = (short) getA();

		break;
		case("ABS"):
			LoByte = (byte) Memory.readFromMemoryLocation(getPC());
		incrementPC();

		HiByte = (byte) Memory.readFromMemoryLocation(getPC());
		incrementPC();

		Operand = (short) (getLO() + 256 * getHI());

		break;
		case("ABX"):
			LoByte = (byte) Memory.readFromMemoryLocation(getPC());
		incrementPC();

		HiByte = (byte) Memory.readFromMemoryLocation(getPC());
		incrementPC();

		Operand = (short) (((getLO() + 256 * getHI())) + getX());

		if ((Operand & 0xFF00) != (HiByte << 8))
		{
			ClockCycles++;

		}

		break;
		case("ABY"):
			LoByte = (byte) Memory.readFromMemoryLocation(getPC());
		incrementPC();

		HiByte = (byte) Memory.readFromMemoryLocation(getPC());
		incrementPC();

		Operand = (short) (((getLO() + 256 * getHI())) + getY());

		if ((Operand & 0xFF00) != (HiByte << 8))
		{
			ClockCycles++;

		}

		break;
		case("IMM"):
			LoByte = (byte) Memory.readFromMemoryLocation(getPC());
		incrementPC();

		Operand = (short) getLO();

		break;
		case("IMP"):
			//Completed

			break;
		case("IND"):
			LoByte = (byte) Memory.readFromMemoryLocation(getPC());
		incrementPC();

		HiByte = (byte) Memory.readFromMemoryLocation(getPC());
		incrementPC();

		Operand = (short) Memory.readFromMemoryLocation((getLO() + 256 * getHI()));

		break;
		case("XIN"):
			LoByte = (byte) Memory.readFromMemoryLocation(getPC());
		incrementPC();

		Operand = (short) Memory.readFromMemoryLocation((getLO() + getX()));

		break;
		case("INY"):
			LoByte = (byte) Memory.readFromMemoryLocation(getPC());
		incrementPC();

		Operand = (short) (Memory.readFromMemoryLocation(getLO()));

		Operand = (short) (Operand + getY());

		if ((Operand & 0xFF00) != (HiByte << 8))
		{
			ClockCycles++;

		}

		break;
		case("REL"):
			LoByte = (byte) Memory.readFromMemoryLocation(getPC());
		incrementPC();

		Operand = (short) (getLO() + getPC());

		if ((Operand & 0x80) == 0x80)
		{
			Operand |= 0xFF00;

		}

		break;
		case("ZPG"):
			Operand = (short) Memory.readFromMemoryLocation(getPC());
		incrementPC();

		break;
		case("ZPX"):
			Operand = (short) (Memory.readFromMemoryLocation(getPC()) + getX());
		incrementPC();

		break;
		case("ZPY"):
			Operand = (short) (Memory.readFromMemoryLocation(getPC()) + getY());
		incrementPC();

		break;
		default:
			System.err.println("Fatal error when trying to get the operand by addressing mode!");
			dumpCPU();

			break;

		}

		return Operand;

	}

	private void executeInstruction(byte Opcode , short Operand)
	{
		//Instructions

		switch(OpcodeMatrix[Byte.toUnsignedInt(Opcode)].getOperation())
		{
		case("ADC"):
			//apparently hard

			break;
		case("AND"):
			Accumulator = (byte) (getA() & Short.toUnsignedInt(Operand));

			break;
		case("ASL"):
			byte Carry = (byte) ((Short.toUnsignedInt(Operand) * 2) - (Short.toUnsignedInt(Operand) << 1));
		
		
			
			break;
		case("BCC"):
			if(getStatusFlag('C') == false)
			{
				ProgramCounter = (short) (getPC() + Short.toUnsignedInt(Operand));
				
			}

			break;
		case("BCS"):
			if(getStatusFlag('C') == true)
			{
				ProgramCounter = (short) (getPC() + Short.toUnsignedInt(Operand));
				
			}

			break;
		case("BEQ"):
			if(getStatusFlag('Z') == true)
			{
				ProgramCounter = (short) (getPC() + Short.toUnsignedInt(Operand));
				
			}

			break;
		case("BIT"):
			byte Bit6 = (byte) ((Operand & 0xBF) - 64);
			byte Bit7 = (byte) ((Operand & 0x7F) - 128);

			break;
		case("BMI"):

			break;
		case("BNE"):

			break;
		case("BPL"):

			break;
		case("BRK"):
			incrementPC();

		break;
		case("BVC"):

			break;
		case("BVS"):

			break;
		case("CLC"):

			break;
		case("CLD"):

			break;
		case("CLI"):

			break;
		case("CLV"):

			break;
		case("CMP"):

			break;
		case("CPX"):

			break;
		case("CPY"):

			break;
		case("DEC"):

			break;
		case("DEX"):

			break;
		case("DEY"):

			break;
		case("EOR"):

			break;
		case("INC"):

			break;
		case("INX"):

			break;
		case("INY"):

			break;
		case("JMP"):

			break;
		case("JSR"):

			break;
		case("LDA"):

			break;
		case("LDX"):

			break;
		case("LDY"):

			break;
		case("LSR"):

			break;
		case("NOP"):
			//Complete - nothing

			break;
		case("ORA"):

			break;
		case("PHA"):

			break;
		case("PHP"):

			break;
		case("PLA"):

			break;
		case("PLP"):

			break;
		case("ROL"):

			break;
		case("ROR"):

			break;
		case("RTI"):

			break;
		case("RTS"):

			break;
		case("SBC"):

			break;
		case("SEC"):

			break;
		case("SED"):

			break;
		case("SEI"):

			break;
		case("STA"):

			break;
		case("STX"):

			break;
		case("STY"):

			break;
		case("TAX"):

			break;
		case("TAY"):

			break;
		case("TSX"):

			break;
		case("TXA"):

			break;
		case("TXS"):

			break;
		case("TYA"):

			break;
		case("XXX"):
			System.err.println("Error: Illegal and unrecognised Opcode encountered!");
		dumpCPU();

		break;
		default:
			System.err.println("Fatal error.");
			dumpCPU();

			break;

		}

	}

}
