package Terminal;

import javax.swing.*;
import java.awt.*;

import Main.Utilities;

public class Monitor extends JPanel implements Runnable{
	
	private static final long serialVersionUID = 8168022962633428025L;

	private Utilities Utils = new Utilities();

	//Calculated dimensions of the Apple 1 Terminal - they don't change hence final modifier
	private final int GRID_WIDTH = 240;
	private final int GRID_HEIGHT = 192;

	private final CharacterGenerator CharacterSet = new CharacterGenerator();
	private final int iCharHeight = CharacterSet.getCharacterROM()[0].length;
	private final int iCharWidth = CharacterSet.getCharacterROM()[0][0].length;
	
	private final int iCursorIndex = 64;
	private final int iSpaceIndex = 32;

	private final int iTotalCharsOnLine = GRID_WIDTH / (iCharWidth + 1);
	private final int iTotalLinesOnScreen = (GRID_HEIGHT / iCharHeight) + 1;

	private int CELL_SIZE;

	private Thread CursorThread;

	private int iColumnShift = 0;
	private int iRowShift = 0;
	private int iCharsOnLine = 0;
	private int iLinesOnScreen = 1;

	private boolean[][] bPixelGrid = new boolean[GRID_HEIGHT][GRID_WIDTH];

	public Monitor(int iReqCellSize , String szWindowTitle) throws InterruptedException 
	{
		CELL_SIZE = iReqCellSize;
		
		JFrame Window = new JFrame(szWindowTitle);
        
		ImageIcon WindowFavicon = new ImageIcon(Utils.getDirectoryName() + "\\windowfavicon.png");
		Window.setIconImage(WindowFavicon.getImage());
		
		Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Window.setResizable(false);
		
		this.setPreferredSize(new Dimension(GRID_WIDTH * CELL_SIZE , GRID_HEIGHT * CELL_SIZE));
		Window.getContentPane().add(this);
		
		Window.pack();
		Window.setVisible(true);

		resetMonitor();
		
		start();

	}

	private void resetMonitor()
	{
		iColumnShift = 0;
		iRowShift = 0;
		iCharsOnLine = 0;
		iLinesOnScreen = 1;

		for (int i = 0; i < GRID_HEIGHT; i++) 
		{
			for (int j = 0; j < GRID_WIDTH; j++) 
			{
				bPixelGrid[i][j] = false; 

			}

		}

		repaint();

	}

	public void carriageReturn() throws InterruptedException
	{
		int iCurrentCharsOnLine = iCharsOnLine;

		for (int i = 0; i < iTotalCharsOnLine - iCurrentCharsOnLine; i++) 
		{
			drawNextCharacter(iSpaceIndex);

		}

	}

	public void drawNextCharacter(int iReqCharIndex) throws InterruptedException {

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

			for (int i = 0; i < (GRID_HEIGHT - iCharHeight); i++) 
			{
				for (int j = 0; j < GRID_WIDTH; j++) 
				{
					bPixelGrid[i][j] = bPixelGrid[i + iCharHeight][j];

				}

			}

			for (int i = 0; i < GRID_WIDTH; i++) 
			{

				for (int j = (GRID_HEIGHT - iCharHeight); j < GRID_HEIGHT; j++) 
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

	private void displayCharacter(int iReqCharIndex) {
		
		int iCharIndex = CharacterSet.convertCharASCIIIndex(iReqCharIndex);

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
		final int iCursorSleepTimeMS = 300;

		do {

			try {

				displayCharacter(iCursorIndex);
				Thread.sleep(iCursorSleepTimeMS);
				repaint();

				displayCharacter(iSpaceIndex);
				Thread.sleep(iCursorSleepTimeMS);
				repaint();

			} 
			catch (InterruptedException e) 
			{
				System.err.println("Error");
				e.printStackTrace();

			}

		} while(CursorThread.isAlive() == true);

	}

	private void start() 
	{	
		CursorThread = new Thread (this , "Cursor Thread");
		CursorThread.start();

	}

	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);

		for (int i = 0; i < GRID_HEIGHT; i++) 
		{
			for (int j = 0; j < GRID_WIDTH; j++) 
			{
				if (bPixelGrid[i][j] == true) 
				{
					g.setColor(Color.GREEN);

				} 
				else 
				{
					g.setColor(Color.BLACK);

				}

				g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);

			}

		}

	}

}
