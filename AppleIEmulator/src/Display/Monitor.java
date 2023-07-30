package Display;

import javax.swing.*;
import java.awt.*;

public class Monitor extends JPanel implements Runnable{
	
	private CharacterGenerator CharacterROM = new CharacterGenerator();

	//Calculated dimensions of the Apple 1 Terminal - they don't change hence final modifier
	    private static final int CELL_SIZE = 3;
		private static final int GRID_WIDTH = 240;
	    private static final int GRID_HEIGHT = 192;
	    
	    public static int getCellSize() 
	    {
			return CELL_SIZE;
			
		}

		public static int getGridWidth() 
		{
			return GRID_WIDTH;
			
		}

		public static int getGridHeight() 
		{
			return GRID_HEIGHT;
			
		}
	    
		private int iColumnShift = 0;
	    private int iRowShift = 0;
	    private int iCharsOnLine = 0;
	    private int iLinesOnScreen = 1;

	    private boolean[][] pixelGrid = new boolean[GRID_HEIGHT][GRID_WIDTH];;
	    
	    public int getiColumnShift() 
	    {
			return iColumnShift;
			
		}

		public void setiColumnShift(int iColumnShift) 
		{
			this.iColumnShift = iColumnShift;
			
		}

		public int getiRowShift() 
		{
			return iRowShift;
			
		}

		public void setiRowShift(int iRowShift) {
			this.iRowShift = iRowShift;
		}

		public int getiCharsOnLine() {
			return iCharsOnLine;
		}

		public void setiCharsOnLine(int iCharsOnLine) 
		{
			this.iCharsOnLine = iCharsOnLine;
			
		}

		public int getiLinesOnScreen() 
		{
			return iLinesOnScreen;
			
		}

		public void setiLinesOnScreen(int iLinesOnScreen) 
		{
			this.iLinesOnScreen = iLinesOnScreen;
			
		}

		public boolean[][] getPixelGrid() 
		{
			return pixelGrid;
			
		}

		public void setPixelGrid(boolean[][] pixelGrid) 
		{
			this.pixelGrid = pixelGrid;
			
		}

	    public Monitor() throws InterruptedException 
	    {
	    	resetMonitor();
	    	
	        start();
	        
	    }
	    
	    public void resetMonitor()
	    {
	    	iColumnShift = 0;
		    iRowShift = 0;
		    iCharsOnLine = 0;
		    iLinesOnScreen = 1;
	    	
	        for (int i = 0; i < GRID_HEIGHT; i++) 
	        {
	        	
	            for (int j = 0; j < GRID_WIDTH; j++) 
	            {
	                pixelGrid[i][j] = false; 
	                
	            }
	            
	        }
	        
	        repaint();
	    	
	    }
	    
	    public void carriageReturn() throws InterruptedException
	    {
	    	int iCurrentCharsOnLine = iCharsOnLine;
	    	
	    	for (int i = 0; i < 40 - iCurrentCharsOnLine; i++) 
	        {
	    		drawNextCharacter(32);
	    		
	        }
	    	
	    }
	    
	    public void drawNextCharacter(int Chara) throws InterruptedException {
	    	
	    	for (int i = 0; i < 8; i++) 
	        {
	        	
	            for (int j = 0; j < 5; j++) 
	            {
	    	pixelGrid[i + iColumnShift][j + iRowShift] = CharacterROM.CharacterROMS[Chara][i][j];
	    	
	            }
	            
	        }
	    	
	    	iCharsOnLine++;
	    	
	    	if(iCharsOnLine == 40)
	    	{
	    		iColumnShift = iColumnShift + 8;
	    		iRowShift = 0;
	    		iCharsOnLine = 0;
	    		iLinesOnScreen++;
	    		
	    	}
	    	else
	    	{
	    		iRowShift = iRowShift + 6;
	    	}
	    	
	    	if(iLinesOnScreen == 25)
	    	{
	    		iLinesOnScreen--;
	    		iColumnShift = iColumnShift - 8;
	    		
	    		for (int i = 0; i < 184; i++) 
		        {
		        	
		            for (int j = 0; j < 240; j++) 
		            {
		            			pixelGrid[i][j] = pixelGrid[i + 8][j];
		            	
		            }
		            
		        }
	    		
	    		for (int i = 0; i < 240; i++) 
		        {
		        	
		            for (int j = 184; j < 192; j++) 
		            {

		            		pixelGrid[j][i] = false;
		    	
		            }
		            
		        } 		
	    		
	    	}
	    	
	    	repaint();
	    	
	    }
	    
		private Thread CursorThread;

		private void displayCharacter(int chara) {

			for (int i = 0; i < 8; i++) 
			{

				for (int j = 0; j < 5; j++) 
				{
					pixelGrid[i + iColumnShift][j + iRowShift] = CharacterROM.CharacterROMS[chara][i][j];

				}

			}
 
			repaint();
		}

		@Override
		public void run() 
		{
			int iCursorTime = 300;
			
			do {

				try {

					displayCharacter(0);
					Thread.sleep(iCursorTime);

					displayCharacter(32);
					Thread.sleep(iCursorTime);

				} 
				catch (InterruptedException e) 
				{
					System.out.println("Error");
					e.printStackTrace();

				}

			}while(1 > 0);

		}

		public void start() {

			CursorThread = new Thread (this, "Cursor Thread");
			CursorThread.start();

		}

	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);

	        for (int i = 0; i < GRID_HEIGHT; i++) 
	        {
	            for (int j = 0; j < GRID_WIDTH; j++) 
	            {
	                if (pixelGrid[i][j] == true) 
	                {
	                    g.setColor(Color.GREEN);
	                    
	                } else 
	                {
	                    g.setColor(Color.BLACK);
	                    
	                }
	                
	                g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
	                
	            }
	            
	        }
	        
	    }
	
}
