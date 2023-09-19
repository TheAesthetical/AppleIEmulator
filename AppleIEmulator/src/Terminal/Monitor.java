package Terminal;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import java.awt.*;

import Main.Utilities;

public class Monitor extends JPanel implements Runnable{

	private static final long serialVersionUID = 8168022962633428025L;

	private Utilities Utils = new Utilities();

	//Calculated dimensions of the Apple 1 Terminal - they don't change hence final modifier
	private final int iGridWidth = 240;
	private final int iGridHeight = 192;

	private final CharacterGenerator CharacterSet = new CharacterGenerator();
	private final int iCharHeight = CharacterSet.getCharacterROM()[0].length;
	private final int iCharWidth = CharacterSet.getCharacterROM()[0][0].length;

	private final byte iCursorIndex = 64;
	private final byte iSpaceIndex = 32;
	private final int iCursorSleepTimeMS = 333;

	private final int iTotalCharsOnLine = iGridWidth / (iCharWidth + 1);
	private final int iTotalLinesOnScreen = (iGridHeight / iCharHeight) + 1;

	private int iCellSize;

	private Thread CursorThread;
	private boolean bCursorActive = false;

	private int iColumnShift = 0;
	private int iRowShift = 0;
	private int iCharsOnLine = 0;
	private int iLinesOnScreen = 1;

	private boolean bIsResetted = false;

	private boolean[][] bPixelGrid = new boolean[iGridHeight][iGridWidth];

	private JFrame Window;

	JMenuItem On;
	JMenuItem Off;
	
	JMenuItem ResetButton;
	JMenuItem CLSButton;
	
//	JMenuItem LF;
//	JMenuItem CR;
//	JMenuItem RO;
	
	JMenuItem LoadCassette;
	JMenuItem ClearCassette;

	public Container getWindow() 
	{
		return Window;

	}

	public JMenuItem getOn() 
	{
		return On;

	}

	public JMenuItem getOff() 
	{
		return Off;

	}

	public JMenuItem getResetButton() 
	{
		return ResetButton;

	}

	public JMenuItem getCLSButton() 
	{
		return CLSButton;

	}

//	public JMenuItem getLF() 
//	{
//		return LF;
//
//	}
//
//	public JMenuItem getCR() 
//	{
//		return CR;
//
//	}
//	
//	public JMenuItem getRO() 
//	{
//		return RO;
//
//	}

	public boolean getCursorActive() 
	{
		return bCursorActive;

	}

	public void setCursorActive(boolean bCursorActive) 
	{
		this.bCursorActive = bCursorActive;

	}

	public boolean getIsResetted() 
	{
		return bIsResetted;

	}

	public void setIsResetted(boolean bIsResetted) 
	{
		this.bIsResetted = bIsResetted;

	}

	public Thread getCursorThread() 
	{
		return CursorThread;

	}

	public Monitor(int iReqCellSize , String szWindowTitle) throws InterruptedException 
	{
		resetMonitor();

		//createVRAM();

		iCellSize = iReqCellSize;

		createGUI(szWindowTitle);
		bCursorActive = false;

		CursorThread = new Thread (this , "Cursor Thread");
		CursorThread.start();

	}

	private void createGUI(String szWindowTitle) 
	{
		Window = new JFrame(szWindowTitle);

		ImageIcon WindowFavicon = new ImageIcon(Utils.getDirectoryName() + "\\windowfavicon.png");
		Window.setIconImage(WindowFavicon.getImage());

		Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Window.setResizable(false);

		JMenuBar MenuBar = new JMenuBar();
		MenuBar.setSize(iGridWidth * iCellSize , 10);

		JMenu PowerSwitch = new JMenu("Power");

		On = new JMenuItem("On");
		Off = new JMenuItem("Off");

		PowerSwitch.add(On);
		PowerSwitch.add(Off);

		MenuBar.add(PowerSwitch);

		JMenu Switches = new JMenu("Switches");

		ResetButton = new JMenuItem("RESET");
		CLSButton = new JMenuItem("CLS");

		Switches.add(ResetButton);
		Switches.add(CLSButton);

		MenuBar.add(Switches);

//		JMenu SpecialKeys = new JMenu("Other Keys");
//
//		CR = new JMenuItem("Carriage Return");
//		LF = new JMenuItem("Line Feed");
//		RO = new JMenuItem("Rub Out");
//
//		SpecialKeys.add(LF);
//		SpecialKeys.add(CR);
//		SpecialKeys.add(RO);
//
//		MenuBar.add(SpecialKeys);
		
		JMenu ACIInterface = new JMenu("ACI");
		
		LoadCassette = new JMenuItem("Load");
		ClearCassette = new JMenuItem("Clear");
		
		ACIInterface.add(LoadCassette);
		ACIInterface.add(ClearCassette);
		
		MenuBar.add(ACIInterface);

		Window.add(MenuBar , BorderLayout.SOUTH);

		this.setPreferredSize(new Dimension(iGridWidth * iCellSize , iGridHeight * iCellSize));
		Window.getContentPane().add(this);
		//Screen.add(this);

		Window.pack();
		Window.setVisible(false);

	}

	//	trying to achieve the weird uninitialised look about it
	public void powerOnMonitor() throws InterruptedException
	{
		byte byUnderscoreIndex = 95;
		byte byBlinkIndex = 64;

		do 
		{
			for (int i = 0; i < iTotalLinesOnScreen; i++) 
			{
				for (int j = 0; j < (iTotalCharsOnLine / 2); j++) 
				{
					drawNextCharacter(byUnderscoreIndex);
					drawNextCharacter(byBlinkIndex);

				} 

			}

			if(byBlinkIndex == 64) 
			{
				byBlinkIndex = (byte) (byBlinkIndex - 32);

			}
			else if (byBlinkIndex == 32)
			{
				byBlinkIndex = (byte) (byBlinkIndex + 32);
			}

			Thread.sleep(iCursorSleepTimeMS);
			
			resetMonitor();

		} while(bIsResetted == false);

	}

	public void resetMonitor()
	{	
		iColumnShift = 0;
		iRowShift = 0;
		iCharsOnLine = 0;
		iLinesOnScreen = 1;

		for (int i = 0; i < iGridHeight; i++) 
		{
			for (int j = 0; j < iGridWidth; j++) 
			{
				bPixelGrid[i][j] = false; 

			}

		}

		repaint();


	}

	public void carriageReturn()
	{
		int iCurrentCharsOnLine = iCharsOnLine;

		for (int i = 0; i < iTotalCharsOnLine - iCurrentCharsOnLine; i++) 
		{
			drawNextCharacter(iSpaceIndex);

		}

	}

	public void drawNextCharacter(byte iReqCharIndex) {

		displayCharacter(iReqCharIndex);

		iCharsOnLine++;

		if(iCharsOnLine == iTotalCharsOnLine)
		{
			iColumnShift = iColumnShift + iCharHeight;
			iRowShift = 0;
			iCharsOnLine = 0;
			iLinesOnScreen++;

		}
		else
		{
			iRowShift = iRowShift + (iCharWidth + 1);
		}

		if(iLinesOnScreen == iTotalLinesOnScreen)
		{
			iLinesOnScreen--;
			iColumnShift = iColumnShift - iCharHeight;

			for (int i = 0; i < (iGridHeight - iCharHeight); i++) 
			{
				for (int j = 0; j < iGridWidth; j++) 
				{
					bPixelGrid[i][j] = bPixelGrid[i + iCharHeight][j];

				}

			}

			for (int i = 0; i < iGridWidth; i++) 
			{
				for (int j = (iGridHeight - iCharHeight); j < iGridHeight; j++) 
				{
					if(bPixelGrid[j][i] == true)
					{
						bPixelGrid[j][i] = false;

					}

				}

			} 		

		}

		repaint();

	}

	private void displayCharacter(byte iReqCharIndex) 
	{
		byte iCharIndex = CharacterSet.convertCharASCIIIndex(iReqCharIndex);
		//System.out.println(Integer.toBinaryString(iCharIndex));

		for (int i = 0; i < iCharHeight; i++) 
		{

			for (int j = 0; j < iCharWidth; j++) 
			{
				bPixelGrid[i + iColumnShift][j + iRowShift] = CharacterSet.getCharacterROM()[iCharIndex][i][j];

			}

		}

	}

	@Override
	public void run() 
	{
		//System.out.println("CursorThread RAN");

		try 
		{
			do 
			{
				//System.out.println("Active? " + bCursorActive);
				while (bCursorActive == true) 
				{
					displayCharacter(iCursorIndex);
					Thread.sleep(iCursorSleepTimeMS);
					repaint();
					//System.out.println("CursorThread DISPLAY");

					displayCharacter(iSpaceIndex);
					Thread.sleep(iCursorSleepTimeMS);
					repaint();

					//System.out.println("CursorThread BLINK");

				}

				//System.out.println("Alive? " + CursorThread.isAlive());
				//System.out.println("CursorThread UNALIVE");

				Thread.sleep(50);

			} while(CursorThread.isAlive());

			System.out.println("CursorThread EXIT");

		} 
		catch (InterruptedException consumed)
		{

		}

	}

	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);

		for (int i = 0; i < iGridHeight; i++) 
		{
			for (int j = 0; j < iGridWidth; j++) 
			{
				if (bPixelGrid[i][j] == true) 
				{				
					g.setColor(Color.getHSBColor((120.0f / 360) , 1.0f , 0.4f));
					g.fillRect((j * iCellSize) , (i * iCellSize) , iCellSize , iCellSize);

					g.setColor(Color.GREEN);
					g.fillRect((j * iCellSize) , (i * iCellSize) , iCellSize , (iCellSize / 2));

				} 
				else 
				{
					g.setColor(Color.BLACK);
					g.fillRect((j * iCellSize) , (i * iCellSize) , iCellSize , iCellSize);

				}

			}

		}

	}

}
