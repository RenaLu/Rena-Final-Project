
package areaundercurve;

import java.util.ArrayList;


public class Polynomial {
    //FIELDS
    ArrayList<Integer> exponents = new ArrayList();
    ArrayList<Fraction> coefficients = new ArrayList();
    
    //Array for y-coordinates of points on function
    double[] yval;//Each pixel across the screen has a different y-coordinate

    //CONSTRUCTORS
    public Polynomial(String p){
        fillArrayList(p);
    }
    
    public Polynomial (ArrayList<Fraction> coef, ArrayList<Integer> expo){
        this.coefficients = coef;
        this.exponents = expo;
    }
    
    //Fills array lists for coefficients and exponents if the constructor that takes a string is called
    public void fillArrayList(String p){
        ArrayList<Integer> indexX = new ArrayList();
        ArrayList<Integer> indexSigns = new ArrayList();
        
        for (int i = 0; i < p.length(); i++) {//Keeps track of index of "x"'s and "+"'s in the polynomial
            String letter = Character.toString(p.charAt(i));

            if (letter.equals("x")){
                indexX.add(i);
            }
            
            if (i==0 && letter.equals("-")){}
            
            else{
                if (letter.equals("+") || letter.equals("-")){
                indexSigns.add(i);
                }
            }
        }
        
        //Reads integers
        if (indexX.isEmpty()){
            coefficients.add(new Fraction(Double.parseDouble(p)));
            exponents.add(0);
        }
        
        else{
            //Appends all of the exponents of x in the function

            //Set different run times for different functions
            int runTimes;

            //If the function ends with an x
            if (indexX.size()>indexSigns.size()) {
                runTimes = indexX.size()-1;
            }

            //If the function does not end with an x
            else {
                runTimes = indexX.size();
            }

            String exponent;
            for (int i = 0; i < runTimes; i++) {
                String check = Character.toString(p.charAt(indexX.get(i)+1));//Checks the character in the string after each x term

                if (check.equals("^")) {//If the x term has an exponent > 1
                    exponent = p.substring((indexX.get(i)+2),indexSigns.get(i));
                }
                else {
                    exponent = "1";
                }
                    exponents.add(Integer.parseInt(exponent)); 
            }

            if (runTimes == indexX.size()-1) {//If the function ends with a x term, not an integer term
                if (!p.substring(p.length()-1, p.length()).equals("x")){//If the last x term has an exponent larger than 1, the last character will not be an x
                    exponent = p.substring((indexX.get(indexX.size()-1)+2), p.length());
                    exponents.add(Integer.parseInt(exponent));
                }
                else{
                    exponents.add(1);
                }
            }

            if (indexSigns.size()==indexX.size()){//last term has no x term, exponent of x is 0 because it is an integer term
                    exponents.add(0);
                }


            //Appends all of the coefficients in the function
            String coef="";

            //Set different run times for different functions

            int runTimes2;
            if(indexSigns.size()==indexX.size()){//If the function ends with an integer term
                runTimes2 = indexSigns.size();
            }

            else {
                runTimes2 = indexX.size();
            }

            int indexSign, indexXterm;
            for (int i = 0; i < runTimes2; i++) {     
                    if (i == 0) {//first term
                        if (p.substring(0, 1).equals("x")){//checks for cases where the coefficient is 1 for the first term
                            coef = "1";
                        }
                        else if (p.substring(0, 2).equals("-x")){//checks for cases where the coefficient for the first term is -1
                            coef = "-1";
                        }
                        else {
                            coef = p.substring(0, indexX.get(i));
                        }
                    }

                    else{
                        indexSign = indexSigns.get(i-1);
                        indexXterm = indexX.get(i);
                        if (!p.substring(indexSign+1, indexSign+2).equals("x")) {//checks for cases where the coefficient is not 1 for the rest of the terms
                            if (p.substring(indexSign, indexSign+1).equals("-")) {
                                coef = "-" + p.substring(indexSign+1, indexXterm);
                            }
                            else{
                                coef = p.substring(indexSign+1, indexXterm);
                            }

                        }
                        else{
                            if (p.substring(indexSign, indexSign+1).equals("-")) {
                                coef = "-1";
                            }
                            else{
                            coef = "1";
                            }
                        }

                    }
                    coefficients.add(new Fraction(Integer.parseInt(coef),1));
                }

            if (indexSigns.size()==indexX.size()) {//adds the last integer if the last term is an integer term
                coef = p.substring (indexSigns.get(indexSigns.size()-1), p.length());
                coefficients.add(new Fraction(Integer.parseInt(coef),1));
            }
        }

    }
    
    //Displays polynomial in expanded form
    public String displayPolynomial(){ 
        String term = "";
        String Print = "";
        String coef;
        String sign;
        String xTerm = "";
        
        for (int i = 0; i < this.exponents.size(); i++) {
            //Determines the sign to use in each term
            //If the coefficient is negative, there would be a minus sign
            if (this.coefficients.get(i).doubleVal < 0) {
                sign = "-";
            }
            //If the coefficient is positive
            else if (this.coefficients.get(i).doubleVal > 0) {
                if (i==0) sign = "";//There will be no sign for the first term
          

                else{
                    //int check = 0;//checks for cases of empty terms (i.e. both exponent and coefficient are zero)
                    //for (int j = 0; j < this.coefficients.size(); j++) {
                        if (coefficients.get(i).doubleVal==0 && exponents.get(i)==0) sign = "";//check++;
                    //}
                    //if (check == coefficients.size()-1) sign = "";
                    else sign = "+";
                }//There will be a plus sign in front of the rest of the terms
            }
            else sign="";
            //If the coefficient is 0, indicating that there is no term, there will be no sign

            //Determines the coefficient to print
            //If the coefficient is 1 or -1
            if (this.coefficients.get(i).doubleVal == 1 || this.coefficients.get(i).doubleVal == -1){
                //If the exponent is 0, indicating that it is an integer term, the coefficient displayed will be 1
                if (this.exponents.get(i)==0){
                    coef = "1";
                }
                //If the there is an x term, no coefficient will be displayed in front of the x
                else {
                    coef = "";
                }
            }
            //Else if the coefficient is a fraction
            else if (this.coefficients.get(i).numerator%this.coefficients.get(i).denominator != 0){
                coef = "("+this.coefficients.get(i).displayFraction()+")";
            }
            //Else if the coefficient is a non-zero integer
            else if (this.coefficients.get(i).numerator%this.coefficients.get(i).denominator == 0){
                coef = String.valueOf(Math.abs((int)this.coefficients.get(i).doubleVal));
            }
            //Else if the coefficient is 0, indicating that there is no term, there will be no coefficient 
            else coef="";
            
            //Determines the x term to print
            if (this.exponents.get(i) > 1) {
                xTerm = "x^" + String.valueOf(this.exponents.get(i));
            }
            else if (this.exponents.get(i) == 1) {
                xTerm = "x";
            }
            else if (this.exponents.get(i) == 0){
                xTerm = "";
            }
            
            //the term in the polynomial is constructed by adding together the sign, coefficient and the x term
            term = sign + coef + xTerm;
            //Adds the term to the whole polynomial
            Print += term;

        }
        if (Print.equals("")) return "0";
        else return Print;
    }
    
    
    public double calcY(double x){ //Calculate the y value of a polynomial function at a given x value
        double y = 0;
        double term;
        for (int k = 0; k < coefficients.size(); k++){ //Goes through each term of the polynomial funciton one by one
            term = coefficients.get(k).doubleVal*(Math.pow(x, exponents.get(k)));
            y = y + term;
        }
        return y;
    }
    
    public void plotPoints(int width, int xMin, double increX){ //Append y-coordinates calculated by calcY()of points on function into the yval[] array
        yval = new double[width];
        for (int i = 0; i < width; i++){
            yval[i] = calcY(xMin + i*increX);
        }
    }
    
    //Calculate the indefinite integral of the function
    public Polynomial findIndefiniteIntegral(){
        ArrayList<Fraction> coef = new ArrayList();
        ArrayList<Integer> expo = new ArrayList();
        
        for (int i = 0; i < exponents.size(); i++) {
            expo.add(exponents.get(i)+1);
        }
        
        for (int i = 0; i < coefficients.size(); i++) {
            int currentCoe = (int)coefficients.get(i).doubleVal;
            int currentExpo = expo.get(i);
            Fraction addCoef = new Fraction(currentCoe, currentExpo);
            coef.add(addCoef.reduceFraction());
        }
        
        Polynomial integralFunction = new Polynomial(coef, expo);
        
        return integralFunction;
    }
    
    //Use the indefinite integral function to calculate the definite integral over a given interval 
    public double calcDefiniteIntegral(double xMin, double xMax){
        Polynomial integralFunction = findIndefiniteIntegral();
        double fAtxMin = integralFunction.calcY(xMin);
        double fAtxMax = integralFunction.calcY(xMax);
        return fAtxMax - fAtxMin;
    }
}
