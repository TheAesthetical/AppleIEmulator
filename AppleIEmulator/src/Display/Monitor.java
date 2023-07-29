package Display;

import javax.swing.*;
import java.awt.*;

public class Monitor extends JPanel{

	    private static final int CELL_SIZE = 3;
	    private static final int GRID_WIDTH = 240;
	    private static final int GRID_HEIGHT = 192;
	    
	    private int iColumnShift = 0;
	    private int iRowShift = 0;
	    private int iCharsOnLine = 0;
	    private int iLinesOnScreen = 1;

	    private boolean[][] pixelGrid;

	    public Monitor() throws InterruptedException 
	    {
	    	pixelGrid = new boolean[GRID_HEIGHT][GRID_WIDTH];
	        
	        for (int i = 0; i < GRID_HEIGHT; i++) 
	        {
	        	
	            for (int j = 0; j < GRID_WIDTH; j++) 
	            {
	                pixelGrid[i][j] = false; 
	                
	            }
	            
	        }
	        
	    }
	    
	    public void drawCharacter(int Chara) throws InterruptedException {
	    	CharacterGenerator chars = new CharacterGenerator();
	    	for (int i = 0; i < 8; i++) 
	        {
	        	
	            for (int j = 0; j < 5; j++) 
	            {
	    	pixelGrid[i + iColumnShift][j + iRowShift] = chars.CharacterROMS[Chara][i][j];
	    	//System.out.print(chars.CharacterROMS[0][i][j]);
	    	
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
	    	
	    	if(iLinesOnScreen == 24)
	    	{
	    		
	    	}
	    	
	    	System.out.println(iColumnShift);
	    	
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
	    
	    

	    public static void main(String[] args) throws InterruptedException {
	        JFrame frame = new JFrame("Apple 1 Emulator");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setResizable(false);

	        Monitor pixelGridPanel = new Monitor();
	        
	        pixelGridPanel.setPreferredSize(new Dimension(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE));


				frame.getContentPane().add(pixelGridPanel);
				frame.pack();
				frame.setVisible(true);
				
				for (int i = 0; i < 64; i++) 
				{
					//Thread.sleep(200);
					pixelGridPanel.drawCharacter(i);
					frame.repaint();
				}

	    }
	
}
