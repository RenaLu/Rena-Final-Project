
package areaundercurve;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;


public class Trapezoid {
    //Fields
    Polynomial f;
    double deltaX;
    JPanel drawingPanel;
    int width;
    int length;
    
    //Constructor
    public Trapezoid(double delX, Polynomial function, JPanel jp){
        this.deltaX = delX;
        this.f = function;
        this.width = jp.getWidth();
        this.length = jp.getHeight();
        this.drawingPanel = jp;
    }
    
    //Calculate area of one trapezoid at a given point
    public double calcTrapeArea(double x1){
        double x2 = x1 + deltaX;
        double y1 = f.calcY(x1);
        double y2 = f.calcY(x2);
        
        double area = (y1+y2)*deltaX/2;
        return area;
    }
    
    //Approximate area under curve by adding up trapezoids at different points
    public double approxAreaTrape(double xMin, double xMax){
        double area = 0;
        for (double i = xMin; i < xMax; i+=deltaX) {
            area += calcTrapeArea(i);
        }
        return area;
    }
    
        /*******************************Graphing Portion*****************************************/
    //FIELDS FOR GRAPHING
    
    //Dimensions for window (set by user)
    int xMin;
    int xMax;
    int yMin;
    int yMax;
    
    //position of axes
    int yPos, xPos;

    //Calculates increments of x and y values
    //Each pixel across the screen has an increment of x or y based on the values calculated here
    double incrementY = ((double)(yMax-yMin))/(double)length;
    double incrementX = ((double)(xMax-xMin))/(double)width;
    

    public void paintImage(){
            Graphics g = drawingPanel.getGraphics();
            Image img = getImage();
            g.drawImage(img, 0, 0, drawingPanel);
        }

        public Image getImage(){
            BufferedImage trap = new BufferedImage(width, length, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) trap.getGraphics();
            
            //Draws background
            g.setColor(Color.white);
            g.fillRect(0, 0, width, length);
            
            //Draws x and y axes
            xPos = Math.abs(width/(xMax-xMin)*xMin);
            yPos = length-Math.abs(length/(yMax-yMin)*yMin);
            g.setColor( Color.gray );
            g.drawLine(0, yPos, width, yPos);
            g.drawLine(xPos, 0, xPos, length);
            
            //draws trapezoids
            for (double i = (double)xMin; i < (double)xMax; i+=deltaX) {
                double x1 = i;
                double x2 = x1 + deltaX;
                double y1 = f.calcY(x1);
                double y2 = f.calcY(x2);

                int drawX1 = (int)(x1 * (double)(width)/(double)(xMax-xMin)) + xPos;
                int drawX2 = (int)(x2 * (double)(width)/(double)(xMax-xMin)) + xPos;
                int drawY1 = length - (int)Math.round((y1-yMin)*length/(yMax-yMin));
                int drawY2 = length - (int)Math.round((y2-yMin)*length/(yMax-yMin));
                int[] xPoints = {drawX1, drawX1, drawX2, drawX2};
                int[] yPoints = {yPos, drawY1, drawY2,yPos};
                g.setColor(new Color(95, 255, 186));
                g.fillPolygon(xPoints, yPoints, 4);
                g.setColor(new Color(56, 176, 124));
                g.drawPolygon(xPoints, yPoints, 4);
                
            }
            
            //draws original function
            g.setColor(new Color(255, 178, 43));
            f.plotPoints(width, xMin, ((double)(xMax-xMin))/(double)width);
            for(int i = 0; i < width-1; i++){
                int yVal = length - (int)Math.round((f.yval[i]-yMin)*length/(yMax-yMin));
                int yValNext = length - (int)Math.round((f.yval[i+1]-yMin)*length/(yMax-yMin)); //Calculates they coordinate of the next pixel to fill 
                g.drawLine(i, yVal-1, i+1, yValNext-1); 
                g.drawLine(i, yVal, i+1, yValNext);  
                g.drawLine(i, yVal+1, i+1, yValNext+1); //draw functions above and below the original to make it thicker
            }
            
            //prints x and y maximums and minimums at the edge of the window
            g.drawString(String.valueOf(yMax), xPos, 20);
            g.drawString(String.valueOf(yMin), xPos, length-10);
            g.drawString(String.valueOf(xMax), width-20, yPos);
            g.drawString(String.valueOf(xMin), 10, yPos);
            
            return trap;
        }    
}
