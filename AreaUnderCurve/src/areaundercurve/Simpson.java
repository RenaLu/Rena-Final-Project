
package areaundercurve;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Simpson extends JFrame {
    //fields
    JPanel drawingPanel;
    int width;
    int length;
    double deltaX;
    Polynomial f;
    
    //constructor
    public Simpson(double delX, Polynomial function, JPanel jp){
        this.deltaX = delX;
        this.f = function;
        this.width = jp.getWidth();
        this.length = jp.getHeight();
        this.drawingPanel = jp;
    }
    
    //Find solution to a linear system with three equations
    public static Polynomial solveMatrix (double[] q1, double[] q2, double[] q3){
        double a, b, c;

        for (int i = 0; i < 4; i++) {
            q2[i] = q1[i] - q2[i];
            q3[i] = q1[i] - q3[i];
        }
        
        double multFact = q2[1]/q3[1];
        
        for (int i = 0; i < 4; i++) {
            q3[i] *= multFact;
            q3[i] = q3[i] - q2[i];
        }
        
        a = q3[3]/q3[0];
        b = (q2[3] - a*q2[0])/q2[1];
        c = (q1[3] - a*q1[0] - b*q1[1])/q1[2];
        
        //Append answers to a new polynomial
        ArrayList<Fraction> coefficients = new ArrayList();
        coefficients.add(new Fraction(a));
        coefficients.add(new Fraction(b));
        coefficients.add(new Fraction(c));
        ArrayList<Integer> exponents = new ArrayList();
        exponents.add(2);
        exponents.add(1);
        exponents.add(0);
        
        return new Polynomial(coefficients, exponents);
    }
    
    //Calculate equation of parabola with three points, starting from a given point
    public Polynomial calcParabola(double startingX){
        double x1, x2, x3;
        double y1, y2, y3;
        
        x1 = startingX;
        x2 = startingX + deltaX;
        x3 = x2 + deltaX;

        y1 = f.calcY(x1);
        y2 = f.calcY(x2);
        y3 = f.calcY(x3);
        
        //In order to find the function of the parabola, we need to find the coefficients in front of the x^2 term, x term and constant term. 
        //Subsitute all three x and y values into the parabola equation ax^2+bx+c = y, we get three different euqations
        //Coefficients of the parabola function are solutions to the linear system 
        //Which takes x^2 as the coefficient in front of the first term, x as the coefficient of the second term and 1 as the coefficient of the last term
        double[] q1 = {x1*x1, x1, 1, y1};
        double[] q2 = {x2*x2, x2, 1, y2};
        double[] q3 = {x3*x3, x3, 1, y3};
        
        //Call solveMatrix function to solve the system
        Polynomial parabolaFunction = solveMatrix(q1, q2, q3);
        return parabolaFunction;
    }
    
    //Calculate the area under a parabola over three points/two delta X's using the Simpson's Rule formula
    //takes the function of the parabola as an argument
    public double calcAreaUnderParabola(Polynomial p, double startingX){
        double x1, x2, x3;
        double y1, y2, y3;
        
        x1 = startingX;
        x2 = startingX + deltaX;
        x3 = x2 + deltaX;

        y1 = f.calcY(x1);
        y2 = f.calcY(x2);
        y3 = f.calcY(x3);
        
        double area = deltaX/3*(y1+4*y2+y3);
        return area;
    }
    
    //Approximate area over the interval between x minimum and x maximum
    public double approxAreaSimp(double xMin, double xMax){
        double area = 0;
        for (double i = (double)xMin; i < (double)xMax; i+=deltaX*2) {
            //Calculate parabola funciton every two delta x's and approximate the area under it
            Polynomial parabolaFunc = calcParabola(i);
            area += calcAreaUnderParabola(parabolaFunc, i);
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
    
    //Take function of estimated parabola and returns values on the parabola
    public double[] drawParabola(Polynomial p, double startingX){
        
        int points = (int)(2*deltaX/(((double)(xMax-xMin))/(double)width));
        double[] yvalParabola = new double[points];

        //Append y-coordinates calculated by calcY()of points on function into an array
        for (int i = 0; i < points; i++) {
            yvalParabola[i] = p.calcY(i*(((double)(xMax-xMin))/(double)width)+startingX);
        }

        return yvalParabola;
    }
    

    public void paintImage(){
            Graphics g = drawingPanel.getGraphics();
            Image img = getImage();
            g.drawImage(img, 0, 0, drawingPanel);
        }

        public Image getImage(){
            BufferedImage simp = new BufferedImage(width, length, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) simp.getGraphics();
            
            //Draws background
            g.setColor(Color.white);
            g.fillRect(0, 0, width, length);
            
            //Draws x and y axes
            xPos = Math.abs(width/(xMax-xMin)*xMin);
            yPos = length-Math.abs(length/(yMax-yMin)*yMin);
            g.setColor( Color.gray );
            g.drawLine(0, yPos, width, yPos);
            g.drawLine(xPos, 0, xPos, length);

            //fills the estimated porabola
            for (double i = (double)xMin; i < (double)xMax; i+=deltaX*2) {
                Polynomial parabola = calcParabola(i);
                double[] yValPara = drawParabola(parabola, i);
                for (int j = 0; j < yValPara.length; j++) {
                    int currX = (int)(i * (double)(width)/(double)(xMax-xMin))+j + xPos;
                    int currY = length - (int)Math.round((yValPara[j]-yMin)*length/(yMax-yMin)); //Calculates the y coordinate of the pixel to fill 
                    g.setColor(new Color(255, 153, 204));
                    g.drawLine(currX, currY, currX, yPos);
                    g.setColor(new Color(255, 102, 178));
                    g.fillRect(currX, currY, 1, 1);

                }
            }
            
            //draw lines that separate delta x's
            g.setColor(new Color(255, 102, 178));
            for (double i = (double)xMin; i < (double)xMax; i+=deltaX) {
                int drawX = (int)(i * (double)(width)/(double)(xMax-xMin)) + xPos;
                int drawY = length - (int)Math.round((f.calcY(i)-yMin)*length/(yMax-yMin));
                g.drawLine(drawX, yPos, drawX, drawY);
            }
            
            //draws original function
            g.setColor(new Color(102, 178, 255));
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
            return simp;
        }    
        
}
