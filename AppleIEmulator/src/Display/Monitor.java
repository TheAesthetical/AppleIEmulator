package Display;

import javax.swing.*;
import java.awt.*;

public class Monitor extends JPanel{

	    private static final int CELL_SIZE = 5;
	    private static final int GRID_WIDTH = 200;
	    private static final int GRID_HEIGHT = 168;
	    
	    private int iColumnShift = 0;
	    private int iRowShift = 0;

	    private boolean[][] pixelGrid;

	    public Monitor() {
	        pixelGrid = new boolean[GRID_HEIGHT][GRID_WIDTH];
	        
	        for (int i = 0; i < GRID_HEIGHT; i++) 
	        {
	        	
	            for (int j = 0; j < GRID_WIDTH; j++) 
	            {
	                pixelGrid[i][j] = false; 
	                
	            }
	            
	        }
	        
	        drawCharacter(0);
	        drawCharacter(1);
	        
	        //pixelGrid[100][100]= true;
	        
	    }
	    
	    public void drawCharacter(int Chara) {
	    	CharacterGenerator chars = new CharacterGenerator();
	    	for (int i = 0; i < 8; i++) 
	        {
	        	
	            for (int j = 0; j < 5; j++) 
	            {
	    	pixelGrid[i + iColumnShift][j + iRowShift] = chars.CharacterROMS[Chara][i][j];
	    	//System.out.print(chars.CharacterROMS[0][i][j]);
	    	
	            }
	            
	        }
	    	
	    	iRowShift = iRowShift + 6;
	    	
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

	    public static void main(String[] args) {
	        JFrame frame = new JFrame("Pixel Grid");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setResizable(false);

	        Monitor pixelGridPanel = new Monitor();
	        
	        pixelGridPanel.setPreferredSize(new Dimension(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE));
	        
	        frame.getContentPane().add(pixelGridPanel);
	        frame.pack();
	        frame.setVisible(true);
	    }
	
}
