package ca.utoronto.utm.floatingpoint;

public class q1 {
	public static void main(String[] args) {
		q1 p = new q1();
		System.out.println(p.solve711());
	}
	
	/**
	 * The issue with the program is that when ever any arithmetic occurs there is the very high possibility of a floating point
	 * rounding error. When float numbers have arithmetic performed, the representation from binary to decimal form cause the
	 * loss in precision due to rounding.There is an attempt to put near infinite number into 32 bits for the float representation.
	 * Thus, when arithmetic operations are performed rounding errors will not produce the desired result. For example the 
	 * product of 0.3f  and 0.2f will produce not the desired result of 0.03 but 0.030000001 due to the rounding error. When looking 
	 * at the code the if statement is one of the few issues the round error is present, as the multiplication and addition would never 
	 * equal to each other due to the very same rounding error.
	 * @return
	 */
	public String solve711() {
		float a, b, c, d;
		for (a = 0.00f; a < 7.11f; a = a + .01f) {
			for (b = 0.00f; b < 7.11f; b = b + .01f) {
				for (c = 0.00f; c < 7.11f; c = c + .01f) {
					for (d = 0.00f; d < 7.11f; d = d + .01f) {
						if (a * b * c * d == 7.11f && a + b + c + d == 7.11f) {
							return (a + " " + b + " " + c + " " + d);
						}
					}
				}
			}
		}
		return "";
	}
}
