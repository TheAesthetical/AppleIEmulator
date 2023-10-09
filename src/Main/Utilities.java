package Main;

public class Utilities {

	public String getDirectoryName()
	{
		String szProjectFolderPath = System.getProperty("user.dir");
		String[] szPathComponents = szProjectFolderPath.split("\\\\");
		
		String szProjectFolderName = szPathComponents[0];
		
		for (int i = szPathComponents.length - 1; i > 0; i--) 
		{
			szProjectFolderName = szProjectFolderName + "\\" + szPathComponents[i];
			
		}
		
		szProjectFolderName = szProjectFolderName + "\\";

		return szProjectFolderName; 
		
	}
	
	//Could be used again, idk yet
	
	public int toUnsignedByte (byte ByteValue) 
	{
		return ByteValue & 0xFF;

	}

	public int toUnsignedShort (short ShortValue) 
	{
		return ShortValue & 0xFFFF;

	}
	
	public short toOppositeEndian(short MemoryLocation) 
	{	
		String szMemoryLocation = Integer.toHexString(Short.toUnsignedInt(MemoryLocation));
		
		szMemoryLocation = szMemoryLocation.substring(2, 4) + szMemoryLocation.substring(0, 2);

		return Short.parseShort(szMemoryLocation , 16); 
		
	}
	
}
