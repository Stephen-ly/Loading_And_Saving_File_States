package ca.utoronto.utm.floatingpoint;

public class q2 {
	public static void main(String[] args) {
		q2 p = new q2();
		System.out.println(p.solve711());
	}
	/**
	 * Cycles through multiple two decimal prices to see the first instance of numbers that can produce 
	 * 4 items that's sum sand product are both is 7.11.
	 * @return String containing the prices of each item
	 */
	public String solve711() {
		// we use doubles as it is more precise and rounding errors will not occur, as seen in q1 when we used single point 
		//float numbers the error had occurred. 
		double a,b,c,d;
		//solution is created such that we are forced to have a>=b>=c>=d, which is why each for loop's condition contains an
		//inequality check
		for (a = 1; a <= 711; a++){
			for (b = 1 ; b+a <= 711 && b<=a ; b++){
				for (c = 1; a+b+c <=711 && c<=b ; c++){
					 d=711-(a+b+c);
					 if (d<=c){  // used to check a>=b>=c>=d
						 if(a*b*c*d== 711000000){
							 	a = a/100; //used to return the value into two decimal place prices as /100 is for price in dollars
								b = b/100;
								c = c/100;
								d = d/100;
								return "a: " + a + "b: " + b + "c: " +c + "d: " +d ;
						 }
					}
				}
			}
		}
		return null;
	}
}
