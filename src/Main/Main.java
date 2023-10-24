package Main;

import java.util.Properties;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws InterruptedException
	{		
		Properties SmootherGUI = System.getProperties();
		SmootherGUI.put("sun.java2d.d3d" , "false");
		System.setProperties(SmootherGUI);

		//You can adjust this accordingly to your eyesight needs
		final int iDisplayScale = 4;

		Apple1 Apple1Emulator = new Apple1(iDisplayScale);

	}

}
