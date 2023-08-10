package Main;

public class Utilities {

	public String getDirectoryName()
	{
		String szProjectFolderPath = System.getProperty("user.dir");
		String[] szPathComponents = szProjectFolderPath.split("\\\\");
		String szProjectFolderName = szPathComponents[szPathComponents.length - 1];
		
		return szProjectFolderName; 
		
	}
	
	//Could be used again, idk yet
	
	private int toUnsignedByte (byte ByteValue) 
	{
		return ByteValue & 0xFF;

	}

	private int toUnsignedShort (short ShortValue) 
	{
		return ShortValue & 0xFFFF;

	}
	
}
