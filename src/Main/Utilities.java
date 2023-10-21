package Main;

public class Utilities {

	public String getDirectoryName()
	{
		String szProjectFolderPath = System.getProperty("user.dir");
		String[] szPathComponents = szProjectFolderPath.split("\\\\");
		
		String szProjectFolderName = szPathComponents[0];
		
		for (int i = 1; i < szPathComponents.length; i++) 
		{
			szProjectFolderName = szProjectFolderName + "\\" + szPathComponents[i];
		}
		
		szProjectFolderName = szProjectFolderName + "\\";
		
		return szProjectFolderName; 
		
	}
	
	public boolean Validate(String szRegexEq , String szCheckValue) 
	{
		boolean bValid;

		if(szCheckValue.matches(szRegexEq))
		{
			bValid = true;

		}
		else
		{
			bValid = false;

		}

		return bValid;

	}
	
}
