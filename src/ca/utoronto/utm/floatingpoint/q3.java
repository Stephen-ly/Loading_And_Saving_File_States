package ca.utoronto.utm.floatingpoint;


public class q3 {
	/**
	 * 
	 * a) -6.8 as an IEEE754 single is 1 10000001 10110011001100110011010
	 * We achieve this by looking at each individual component of the piece, the sign, exponent, and mantissa. First, since the
	 * number -6.8 is negative the sign will be 1 which will be the first binary digit. The last 23 digits originate from the binary 
	 * representation of 6.8. The number is 110 and the decimal value of 0.8 is rounded to .11001100 repeating infinite times. Thus, 
	 * we have 110.11001100. Finally, we move the decimal position over 2 places such that we get 1.10110011001100110011001 for the 
	 * mantissa 23 bits, but we must remove the 1. at the front as it will always begin with 1. the mantissa becomes
	 * 10110011001100110011001. Now the exponent section is 127 + the number of decimal places moved, in this case its 2, thus 
	 * exponent is 129, which binary representation is 10000001. But for rounding errors, we need to change the last two binary 
	 * digits to 10.
	 *  
	 * 
	 * 
	 * b) 23.1 as an IEEE754 single is 0 10000011 01110001100110011001101
	 * In order to convert the decimal number into the IEEE754 single representation, we individual component of the piece, 
	 * the sign, exponent, and mantissa must be solved. Since 23.1 is a positive number, the first binary digit is 0. The 
	 * mantissa consists of 23 bit conversion of the decimal number, in which the whole decimal number of 23 is represented as 10111.
	 * The decimal portion is repetitive and thus rounding must occur as 0001100110011. The final result of 10111.0001100110011 
	 * represents the decimal number of 23.1, but we need to move the decimal place 4 times for the IEEE754 form, in which
	 * 1.01110001100110011 is the representation. The 1 in front can be removed as all will start with 1, and thus the 23 bit 
	 * representation of the mantissa for 23.1. The exponent is 127 + 4 decimal place movements is 131 for the exponent is 10000011. 
	 * And adjusted for rounding at the end.
	 */
}
