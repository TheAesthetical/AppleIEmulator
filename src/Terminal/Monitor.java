package Terminal;

import javax.swing.*;
import java.awt.*;

public class Monitor extends JPanel implements Runnable{

	private static final long serialVersionUID = 8168022962633428025L;

	private final int iGridWidth;
	private final int iGridHeight;

	private final CharacterGenerator CharacterSet = new CharacterGenerator();
	private final int iCharHeight = CharacterSet.getCharacterROM()[0].length;
	private final int iCharWidth = CharacterSet.getCharacterROM()[0][0].length;

	private byte byCursorIndex;
	private final byte bySpaceIndex = 32;
	private int iCursorSleepTimeMS;

	private final int iTotalCharsOnLine;
	private final int iTotalLinesOnScreen;

	private final int iCellSize;

	private Thread CursorThread;
	private boolean bCursorActive = false;

	private int iColumnShift = 0;
	private int iRowShift = 0;
	private int iCharsOnLine = 0;
	private int iLinesOnScreen = 1;

	private boolean bIsCleared = false;

	private JFrame Screen = new JFrame();
	
	private boolean[][] bPixelGrid;

	public JFrame getScreen() 
	{
		return Screen;

	}

	public boolean[][] getPixelGrid() 
	{
		return bPixelGrid;
		
	}

	public boolean getCursorActive() 
	{
		return bCursorActive;

	}

	public boolean getCleared() 
	{
		return bIsCleared;

	}

	public Thread getCursorThread() 
	{
		return CursorThread;

	}
	
	public void setCursorActive(boolean bCursorActive) 
	{
		this.bCursorActive = bCursorActive;

	}
	
	public void setCleared(boolean bIsCleared) 
	{
		this.bIsCleared = bIsCleared;

	}
	
	public void setCursorSleepTimeMS(int iCursorSleepTimeMS) 
	{
		this.iCursorSleepTimeMS = iCursorSleepTimeMS;

	}
	
	public void setCursorIndex(byte byCursorIndex) 
	{
		this.byCursorIndex = byCursorIndex;

	}

	public Monitor(int iReqWidth , int iReqHeight , int iReqSize) throws InterruptedException 
	{	
		resetMonitor();
		
		iGridWidth = iReqWidth;
		iGridHeight = iReqHeight;
		
		iCellSize = iReqSize;
		
		bPixelGrid = new boolean[iGridHeight][iGridWidth];
		
		makeWindow();

		iTotalCharsOnLine = iGridWidth / (iCharWidth + 1);
		iTotalLinesOnScreen = (iGridHeight / iCharHeight) + 1;

		bCursorActive = false;

		CursorThread = new Thread (this , "Cursor Thread");
		CursorThread.start();

	}
	
	private void makeWindow()
	{
		//this.equals(bPixelGrid);
		
		this.setPreferredSize(new Dimension(iGridWidth * iCellSize , iGridHeight * iCellSize));
		Screen.getContentPane().add(this);
		
		Screen.pack();
		Screen.setVisible(false);
		
	}

	//	trying to achieve the weird uninitialised look about it
	public void powerOnMonitor() throws InterruptedException
	{
		byte byUnderscoreIndex = 95;
		byte byBlinkIndex = byCursorIndex;

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

		} while(bIsCleared == false);

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
			drawNextCharacter(bySpaceIndex);

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
					displayCharacter(byCursorIndex);
					Thread.sleep(iCursorSleepTimeMS);
					repaint();
					//System.out.println("CursorThread DISPLAY");

					displayCharacter(bySpaceIndex);
					Thread.sleep(iCursorSleepTimeMS);
					repaint();

					//System.out.println("CursorThread BLINK");

				}

				//System.out.println("Alive? " + CursorThread.isAlive());
				//System.out.println("CursorThread UNALIVE");

				Thread.sleep(50);

			} while(CursorThread.isAlive());

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
