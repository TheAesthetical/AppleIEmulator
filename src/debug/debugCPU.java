/*
 * 
 */
package debug;

import Computer.CPU6502;
import Computer.RAM;
import Main.Utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.journaldev.jackson.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Scanner;

public class debugCPU
{
	private String szPath;
	
	private String log;
	private boolean bTestPass;
	private boolean bOverallPass;
	private int iVerbosity;
	private int iTestsRan;
	private int iTestsPassed;
	
	private RAM Memory;
	private CPU6502 CPU;
	
	// ===========================================================================================
	// Constructor 
	// ===========================================================================================
	
	public debugCPU(String szFilePath)
	{
		this.szPath = szFilePath;
		this.Memory = new RAM(65536);
		this.CPU = new CPU6502(this.Memory);
		
		Scanner szKeyboard = new Scanner(System.in);
		Scanner iKeyboard = new Scanner(System.in);
		String szUserInput;
		
		System.out.println("---Luke Watson's modified nes6502 debug tester using TomHartes JSON tests---\n");
		
		System.out.print("Please enter verbosity level (0, 1, 2): ");
		iVerbosity = iKeyboard.nextInt();
		
		System.out.print("Would you like to run every test (Y/N): ");
		szUserInput = szKeyboard.nextLine();
		
		if (szUserInput.equalsIgnoreCase("y"))
		{
			for (int i = 0; i <= 0xFF; i++)
			{
				executeTest(i);
			}
		}
		else
		{
			System.out.print("What test would you like to run: ");
			szUserInput = szKeyboard.nextLine().toLowerCase();
			
			executeTest(Integer.decode("0X" + szUserInput));
		}
		System.out.println("Testing completed!");
		System.out.println("Passed " + iTestsPassed + "/" + iTestsRan + " tests.");
		
		szKeyboard.close();
		iKeyboard.close();
	}
	
	// ===========================================================================================
	// Getters 
	// ===========================================================================================
	
	private int getCpuPC()
	{
		return this.CPU.shProgramCounter & 0xFFFF;
	}
	
	private int getCpuSP()
	{
		return this.CPU.byStackPointer & 0xFF;
	}
	
	private int getCpuA()
	{
		return this.CPU.byAccumulator & 0xFF;
	}
	
	private int getCpuX()
	{
		return this.CPU.byIndexX & 0xFF;
	}
	
	private int getCpuY()
	{
		return this.CPU.byIndexY & 0xFF;
	}
	
	private int getCpuP()
	{
		return this.CPU.byStatusFlags & 0xFF;
	}
	
	private int getCpuRam(int iAddress)
	{
		return this.Memory.read((short) iAddress);
	}
	
	// ===========================================================================================
	// Setters 
	// ===========================================================================================
	
	private void setCpuPC(int input)
	{
		this.CPU.shProgramCounter = (short) (input & 0xFFFF);
	}
	
	private void setCpuSP(int input)
	{
		this.CPU.byStackPointer = (byte) (input & 0xFF);
	}
	
	private void setCpuA(int input)
	{
		this.CPU.byAccumulator = (byte) (input & 0xFF);
	}
	
	private void setCpuX(int input)
	{
		this.CPU.byIndexX = (byte) (input & 0xFF);
	}
	
	private void setCpuY(int input)
	{
		this.CPU.byIndexY = (byte) (input & 0xFF);
	}
	
	private void setCpuP(int input)
	{
		this.CPU.byStatusFlags = (byte) (input & 0xFF);
	}
	
	private void setCpuRam(int iAddress, int iData)
	{
		this.Memory.write((short) (iAddress & 0xFFFF), (byte) (iData & 0xFF));
	}
	
	// ===========================================================================================
	// Utilities 
	// ===========================================================================================
	
	
	private void executeTest(int iSelection)
	{
		// Create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();
		DebugTest[] testCase;
		byte[] jsonData;
		
		try
		{
			if (iSelection <= 0xF)
			{
				// Read json file data to String
				jsonData = Files.readAllBytes(Paths.get(szPath + "0" + Integer.toHexString(iSelection) + ".json"));
			}
			else
			{
				// Read json file data to String
				jsonData = Files.readAllBytes(Paths.get(szPath + Integer.toHexString(iSelection) + ".json"));
			}
				
			// Convert json string to object
			testCase = objectMapper.readValue(jsonData, DebugTest[].class);
			
			this.bOverallPass = true;
			
			for (int j = 0; j < testCase.length; j++)
			{
				this.bTestPass = true;
				this.CPU.resetCPU();
				
				setCpuPC(testCase[j].getInitial().getPC());
				setCpuSP(testCase[j].getInitial().getS());
				setCpuA(testCase[j].getInitial().getA());
				setCpuX(testCase[j].getInitial().getX());
				setCpuY(testCase[j].getInitial().getY());
				setCpuP(testCase[j].getInitial().getP());
				
				for (int k = 0; k < testCase[j].getInitial().getRam().length; k++)
				{
					setCpuRam(testCase[j].getInitial().getRam()[k][0], 
							testCase[j].getInitial().getRam()[k][1]);
				}
				
				
				checkForInitialDiscrepancies(testCase, j);
				
				this.CPU.executeInstruction();
				
				checkForFinalDiscrepancies(testCase, j);
//				logCycles(cpu, testCase, j);
				
				if (this.bTestPass == false)
				{
					this.bOverallPass = false;
				}

				if (this.bTestPass && this.iVerbosity == 1)
				{
					System.out.println("Test: " + testCase[j].getName() + " - " + "PASSED!");
				}
				else if (this.bTestPass == false && this.iVerbosity == 1)
				{
					System.out.println("Test: " + testCase[j].getName() + " - " + "FAIL!");
					this.bOverallPass = false;
				}
				else if (this.bTestPass == false && this.iVerbosity == 2)
				{
					this.log = "Discrepancies detected in below test!" + "\n" + log;
					System.out.print(this.log);
					this.bOverallPass = false;
				}
			}
			
			if (this.iVerbosity != 0)
			{
				System.out.println("");
			}
			
			if (this.bOverallPass == true)
			{
				System.out.println(Integer.toHexString(iSelection).toUpperCase() + ".json - " + CPU.OpcodeMatrix[iSelection].getOperation() + " " + CPU.OpcodeMatrix[iSelection].getAddressingMode() + " - PASSED!");
				iTestsPassed++;
			}
			else if (this.bOverallPass == false)
			{
				System.out.println(Integer.toHexString(iSelection).toUpperCase() + ".json - " + CPU.OpcodeMatrix[iSelection].getOperation() + " " + CPU.OpcodeMatrix[iSelection].getAddressingMode() + " - FAIL!");
			}
			
			if (this.iVerbosity != 0)
			{
				System.out.println("");
			}
			
			this.iTestsRan++;
		}
		catch (NoSuchFileException e) {}
		catch (IOException e) {}
	}
	
	private void checkForInitialDiscrepancies(DebugTest[] testCase, int iSelection)
	{
		logInitial(testCase, iSelection);
		
		if ((testCase[iSelection].getInitial().getPC() != getCpuPC()) ||
				(testCase[iSelection].getInitial().getS() != getCpuSP()) ||
				(testCase[iSelection].getInitial().getA() != getCpuA()) ||
				(testCase[iSelection].getInitial().getX() != getCpuX()) ||
				(testCase[iSelection].getInitial().getY() != getCpuY()) ||
				(testCase[iSelection].getInitial().getP() != getCpuP()))
		{
			this.bTestPass = false;
		}
		else
		{
			for (int j = 0; j < testCase[iSelection].getInitial().getRam().length; j++)
			{
				if ((testCase[iSelection].getInitial().getRam()[j][1] != 
						getCpuRam(testCase[iSelection].getInitial().getRam()[j][0])))
				{		
					this.bTestPass = false;
					break;
				}
			}
		}
	}
	
	private void checkForFinalDiscrepancies(DebugTest[] testCase, int iSelection)
	{
		logFinal(testCase, iSelection);
		
		if ((testCase[iSelection].getFinal().getPC() != getCpuPC()) ||
				(testCase[iSelection].getFinal().getS() != getCpuSP()) ||
				(testCase[iSelection].getFinal().getA() != getCpuA()) ||
				(testCase[iSelection].getFinal().getX() != getCpuX()) ||
				(testCase[iSelection].getFinal().getY() != getCpuY()) ||
				(testCase[iSelection].getFinal().getP() != getCpuP()))
		{
			this.bTestPass = false;
		}
		else
		{
			for (int j = 0; j < testCase[iSelection].getFinal().getRam().length; j++)
			{
				if ((testCase[iSelection].getFinal().getRam()[j][1] != 
						getCpuRam(testCase[iSelection].getFinal().getRam()[j][0])))
				{
					this.bTestPass = false;
					break;
				}
			}
		}
	}
	
	private void logInitial(DebugTest[] testCase, int iSelection)
	{
		
		this.log = String.format("Name: %s\nInitial:\n	PC: 0x%04X | 0x%04X\n	S : 0x%02X   | 0x%02X\n"
				+ "	A : 0x%02X   | 0x%02X\n	X : 0x%02X   | 0x%02X\n	Y : 0x%02X   | 0x%02X\n	P : 0x%02X   | 0x%02X"
				+ "\n\n	ram:\n",
				testCase[iSelection].getName(),
				testCase[iSelection].getInitial().getPC(), getCpuPC(),
				testCase[iSelection].getInitial().getS(), getCpuSP(),
				testCase[iSelection].getInitial().getA(), getCpuA(),
				testCase[iSelection].getInitial().getX(), getCpuX(),
				testCase[iSelection].getInitial().getY(), getCpuY(),
				testCase[iSelection].getInitial().getP(), getCpuP());

		for (int j = 0; j < testCase[iSelection].getInitial().getRam().length; j++)
		{
			this.log = this.log + String.format("		[0x%04X, 0x%02X | 0x%02X]\n", 
					testCase[iSelection].getInitial().getRam()[j][0], 
					testCase[iSelection].getInitial().getRam()[j][1],
					getCpuRam(testCase[iSelection].getInitial().getRam()[j][0]));
		}
	}
	
	private void logFinal(DebugTest[] testCase, int iSelection)
	{
		this.log = this.log + String.format("Final:\n	PC: 0x%04X | 0x%04X\n	S : 0x%02X   | 0x%02X\n	A : 0x%02X   | 0x%02X\n	"
				+ "X : 0x%02X   | 0x%02X\n	Y : 0x%02X   | 0x%02X\n	P : 0x%02X   | 0x%02X\n\n	ram:\n",
				testCase[iSelection].getFinal().getPC(), getCpuPC(),
				testCase[iSelection].getFinal().getS(), getCpuSP(),
				testCase[iSelection].getFinal().getA(), getCpuA(),
				testCase[iSelection].getFinal().getX(), getCpuX(),
				testCase[iSelection].getFinal().getY(), getCpuY(),
				testCase[iSelection].getFinal().getP(), getCpuP());

		for (int j = 0; j < testCase[iSelection].getFinal().getRam().length; j++)
		{
			this.log = this.log + String.format("		[0x%04X, 0x%02X | 0x%02X]\n", 
					testCase[iSelection].getFinal().getRam()[j][0], 
					testCase[iSelection].getFinal().getRam()[j][1],
					getCpuRam(testCase[iSelection].getFinal().getRam()[j][0]));
		}
		
		this.log = this.log + "\n";
	}
	
//	private static void logCycles(CPU cpu, DebugTest[] testCase, int iSelection)
//	{
//		String cyclesJSON;
//		String cyclesCPU;
//		log = log + String.format("Cycles:\n");
//		
//		for (int j = 0; j < testCase[iSelection].getCycles().length; j++)
//		{
//			cyclesJSON = String.format("0x%04X, 0x%02X, %s", 
//					Integer.parseInt(testCase[iSelection].getCycles()[j][0]), 
//					Integer.parseInt(testCase[iSelection].getCycles()[j][1]),
//					testCase[iSelection].getCycles()[j][2]);
//			
//			if (j < cpu.getMainMemory().getCyclesDebug().size())
//			{
//				cyclesCPU = cpu.getMainMemory().getCyclesDebug().get(j);
//			}
//			else
//			{
//				cyclesCPU = "N/A";
//			}
//				
//			log = log + "		[" + cyclesJSON + " | " + cyclesCPU + String.format("]\n");
//			
//			if (cyclesJSON.equals(cyclesCPU) == false)
//			{
//				bTestPass = false;
//			}s
//		}
//		log = log + "\n";
//	}
	
	public static void main(String[] args)
	{
		Utilities Utils = new Utilities();
		
		//TEST
		//debugCPU debug = new debugCPU(Utils.getDirectoryName() + "\\Normal\\");
		
		//SCHOOL - 302 - 700
		//debugCPU debug = new debugCPU("D:\\Personalemente\\NormalOpcodeTests\\");
		
		//SCHOOL - 202
		//debugCPU debug = new debugCPU("E:\\Personalemente\\NormalOpcodeTests\\");
		
		//HOME
		debugCPU debug = new debugCPU("G:\\Personalemente\\NormalOpcodeTests\\");
		
	}
}