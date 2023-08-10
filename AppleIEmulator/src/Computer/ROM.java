package Computer;

import java.io.FileInputStream;
import java.io.IOException;

public class ROM {
	
	private byte[] ComputerROM;
	
	public ROM(int iWordSize , String szROMFileName) {
		
		ComputerROM = new byte[((int) Math.pow(2 , iWordSize))];
		
		String szProjectFolderPath = System.getProperty("user.dir");
        String[] szPathComponents = szProjectFolderPath.split("\\\\");
        String szProjectFolderName = szPathComponents[szPathComponents.length - 1];
		
        try (FileInputStream inputStream = new FileInputStream(szProjectFolderName + "\\" + szROMFileName + ".bin")) 
        {
        	int bytesRead;

            while ((bytesRead = inputStream.read(ComputerROM)) != -1) 
            {
                for (int i = 0; i < bytesRead; i++) 
                {
                    // Invert the sign bit using XOR operation with 0x80
                    byte invertedByte = (byte) (ComputerROM[i]);

                    System.out.println(Integer.toBinaryString(invertedByte));
                    
                    ComputerROM[i] = invertedByte;
                    
                }
                
            }
            
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            
        }
        
        
//        for (int i = 0; i < ComputerROM.length; i++) 
//        {
//				System.out.println(Integer.toHexString(ComputerROM[i]).toUpperCase());
//        }
        
		
	}

}
