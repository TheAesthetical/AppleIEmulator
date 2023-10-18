package Main;

import java.util.Properties;

import Computer.ACI;
import Computer.CPU6502;
import Computer.PIA6522;
import Computer.RAM;
import Computer.ROM;
import Terminal.Monitor;

public class Main {

	public static void main(String[] args) throws InterruptedException
	{		
		Properties SmootherGUI = System.getProperties();
		SmootherGUI.put("sun.java2d.d3d" , "false");
		System.setProperties(SmootherGUI);

		//You can adjust this accordingly to your eyesight needs
		final int iDisplayScale = 3;

		Apple1 Apple1Emulator = new Apple1(iDisplayScale);

	}

}
