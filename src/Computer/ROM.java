package Computer;
import Main.Utilities;

import java.io.FileInputStream;
import java.io.IOException;

public class ROM {

	private Utilities Utils = new Utilities();
	private byte[] ComputerROM;

	public ROM(int iSizePower , String szROMFileName) {

		ComputerROM = new byte[((int) Math.pow(2 , iSizePower))];
		
		try (FileInputStream ByteStream = new FileInputStream(Utils.getDirectoryName() + szROMFileName + ".bin")) 
		{	
			ByteStream.read(ComputerROM , 0 , ComputerROM.length);

		} 
		catch (IOException e) 
		{
			e.printStackTrace();

		}

	}
	
	public byte[] getROM() 
	{
		return ComputerROM;
		
	}

}
