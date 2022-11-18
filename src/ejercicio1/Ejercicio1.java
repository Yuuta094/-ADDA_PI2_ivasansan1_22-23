package ejercicio1;

import java.math.BigInteger;

public class Ejercicio1 {
	
	//Viene en la Teoria
	
	public static Double factorialDoubleRec(Integer n) {
		Double r=0.;
		if(n==0) {
			r=1.;
		}else {
			r=n*factorialDoubleRec(n-1);
		}
		return r;
	}

	public static Double factorialDoubleItr(Integer n) {
		Double r=1.;
		while(n!=0) {
			r=r*n;
			n=n-1;
		}
		return r;
	}
	
	public static BigInteger factorialBigIntRec(Integer n) {
		BigInteger r;
		if(n==0) {
			r=BigInteger.ONE;
		}else {
			r=factorialBigIntRec(n-1).multiply(BigInteger.valueOf(n));
		}
		return r;
	}
	
	public static BigInteger factorialBigIntItr(Integer n) {
		BigInteger r= BigInteger.ONE;
		while(n!=0) {
			r=r.multiply(BigInteger.valueOf(n));
			n=n-1;
		}
		return r;
	}
	
}
