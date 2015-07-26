
package areaundercurve;

//This class is used for displaying non-integer coefficients of a function in fraction form
public class Fraction {
    //Fields
    int numerator, denominator;
    double doubleVal;
    
    //Constructors
    public Fraction(int n, int d){
        this.numerator = n;
        this.denominator = d;
        this.doubleVal = (double)n/(double)d;
    }
    
    //Allows the coefficients of function to be appended as a double value
    public Fraction(double a){
        this.doubleVal = a;
    }
    
    //Find greatest common denominator of two numbers using Euclid's method
    public static int findGCD(int num1, int num2){
        int max, min;
        if (num1 > num2){
            max = num1;
            min = num2;
        }
        else{
            max = num2;
            min = num1;
        }
        int remainder = max % min;
        while (remainder > 0){
            max = min;
            min = remainder;
            remainder = max % min;
        }
        
        return min;
    }
    
    //Reduce the fraction
    public Fraction reduceFraction(){
        int gcd = findGCD(numerator, denominator);
        int newNum = numerator/gcd;
        int newDen = denominator/gcd;
        return new Fraction(newNum, newDen);
    }
    
    //Put the values into fraction form
    public String displayFraction(){
        if (numerator!=denominator){
            return String.valueOf(Math.abs(numerator))+"/"+String.valueOf(Math.abs(denominator));
        }
        else{
            return "1";
        }
    }
}
