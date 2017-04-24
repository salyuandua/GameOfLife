import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.applet.Applet;

import javax.swing.*;



public class GameOfLife extends Applet{
	private PaintPane gridPanel;
    public GameOfLife() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }

                JFrame myFrame = new JFrame();
				JPanel aPanel = new JPanel();
				JButton aButton = new JButton("Next Generation");
				JButton clearButton = new JButton("Clear Board");

				aButton.setSize(150, 150);
				aButton.addActionListener(new ButtonListener());
				myFrame.setSize(200, 200);
				aPanel.setSize(200, 200);
				aPanel.add(aButton);
				aPanel.add(clearButton);
				clearButton.addActionListener(new ClearButtonListener());
				myFrame.add(aPanel);
				myFrame.setVisible(true);
                
                JFrame frame = new JFrame("Life Game");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gridPanel=new PaintPane();
                frame.add(gridPanel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			
			Graphics g = gridPanel.getGraphics();
			List<Rectangle> gridList=gridPanel.grid;
			List<Rectangle> fillList=gridPanel.fill;
			List<Rectangle> newCells=new ArrayList<Rectangle>();
			//scan whole grid
			for(int i=0;i<gridList.size();i++){
				Rectangle cell=gridList.get(i);
				//find all neighbors
				List<Rectangle> neighbors=gridPanel.findneighbors(cell);
				//find live neighbors
				int liveNeighborCount=0;
				for(int j=0;j<neighbors.size();j++){
					Rectangle oneNeighbor=neighbors.get(j);
					if(gridPanel.isLive(oneNeighbor)){
						liveNeighborCount++;
					}
				}
				if(liveNeighborCount==2){//live
					//System.out.println(cell.x+"**"+cell.y);
					if(gridPanel.isLive(cell)){
						newCells.add(cell);
					}
				}
				if(liveNeighborCount==3){
					newCells.add(cell);
				}
			}
			gridPanel.fill=newCells;
			gridPanel.repaint();
			
		}
	}
	
	
	private class ClearButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Graphics g = gridPanel.getGraphics();
			gridPanel.fill=new ArrayList<>();
			gridPanel.repaint();
			
		}
	}
	
	
    public static void main(String[] args) {
        new GameOfLife();
    }



    public class PaintPane extends JPanel {

        private List<Rectangle> grid;
        private List<Rectangle> fill;

		public PaintPane() {
            grid = new ArrayList<>();
            fill = new ArrayList<>();
            
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (Rectangle shape : grid) {
                        if (shape.contains(e.getPoint())) {
                            if (fill.contains(shape)) {
                            	fill.remove(shape);
                            } else {
                                fill.add(shape);
                            }
                        }
                    }
                    repaint();
                }
            });

            int colWidth = 500 / 50;
            int rowHeight = 500 / 50;

            for (int row = 0; row < 50; row++) {
                for (int col = 0; col < 50; col++) {
                    grid.add(new Rectangle(colWidth * col, rowHeight * row, colWidth, rowHeight));
                }
            }

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(500, 500);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); 
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);
            for (Shape cell : fill) {
                g2d.fill(cell);
            }
            g2d.setColor(Color.BLACK);
            for (Shape cell : grid) {
                g2d.draw(cell);
            }
        }    /**
         * Find all neighbors this rectangle has 
         * @param rectangle
         * @return
         */
        private List<Rectangle> findneighbors(Rectangle rectangle){
        	int x=rectangle.x;
        	int y=rectangle.y;
        	List<Rectangle> neighbors=new ArrayList<Rectangle>(8);
        		for(int i=x-10;i<=x+10;i+=10){
        			for(int j=y-10;j<=y+10;j+=10){
        				if(!(i==x&&j==y)){//remove itself
           				
        					int tempX=i;
        					int tempY=j;
        					if(i<0){
        						tempX=490;
        					}
        					if(j<0){
        						tempY=490;
        					}
        					if(i>490){
        						tempX=0;
        					}
        					if(j>490){
        						tempY=0;
        					}
        					//System.out.println(tempX+"****"+tempY);
            				neighbors.add(new Rectangle(tempX, tempY, 10, 10));
       				}

        			}
        		}
        	return neighbors;
        }
        private boolean isLive(Rectangle cell){
        	for(int i=0;i<this.fill.size();i++){
        		if(fill.get(i).equals(cell)){
        			return true;
        		}
        	}
        	return false;
        	
        }
    }

}