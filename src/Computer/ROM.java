package Computer;
import Main.Utilities;

import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

public class ROM {

	private Utilities Utils = new Utilities();
	private byte[] byComputerROM;

	public ROM(int iByteSize , String szROMFileName) {

		byComputerROM = new byte[iByteSize];
		
		try (FileInputStream ByteStream = new FileInputStream(Utils.getDirectoryName() + szROMFileName)) 
		{	
			ByteStream.read(byComputerROM , 0 , byComputerROM.length);

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.err.println("File error when trying to get ROM!\n" + e.getMessage());

		}

	}
	
	public byte[] getROM() 
	{
		return byComputerROM;
		
	}

}
