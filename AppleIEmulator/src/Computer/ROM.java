package Computer;
import Main.Utilities;

import java.io.FileInputStream;
import java.io.IOException;

public class ROM {

	private Utilities Utils = new Utilities();
	private byte[] ComputerROM;

	public ROM(int iAddressBusSize , String szROMFileName) {

		ComputerROM = new byte[((int) Math.pow(2 , iAddressBusSize))];

		try (FileInputStream inputStream = new FileInputStream(Utils.getDirectoryName() + "\\" + szROMFileName + ".bin")) 
		{
           // while ((BytesRead = inputStream.read(ComputerROM)) != -1);
			
			inputStream.read(ComputerROM, 0, ComputerROM.length);

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
