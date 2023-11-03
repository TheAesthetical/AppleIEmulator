package Main;

public class test {

	public static byte binaryToBCD(byte binary) {
		byte bcd = 0;
		byte byOnes = 0;
		byte byTens = 0;
		byte byHundreds = 0;

		byOnes = (byte) (Byte.toUnsignedInt(binary) / 1);
		byTens = (byte) (Byte.toUnsignedInt(binary) / 10);
		byHundreds = (byte) (Byte.toUnsignedInt(binary) / 100);

		//        for (int i = 0; i < Integer.toBinaryString(Byte.toUnsignedInt(bcd)).length(); i++) 
		//        {

		while(binary != 0)
		{
			if (Byte.toUnsignedInt(byOnes) <= 5) 
			{
				bcd = (byte) (Byte.toUnsignedInt(bcd) + 3);
				
			}
			else if(Byte.toUnsignedInt(byTens) <= 5)
			{


			}
			else if(Byte.toUnsignedInt(byHundreds) <= 5)
			{


			}
			else
			{
				binary = (byte) (binary << 1);

			}

		}

		return bcd;

	}


	public static void main(String[] args) 
	{
		byte binaryNumber = 32;
		byte bcdNumber = binaryToBCD(binaryNumber);
		System.out.println("BCD: " + Integer.toBinaryString(Byte.toUnsignedInt(bcdNumber)));

	}

}
