import java.io.File;

import java.io.IOException;

import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws IOException {
		
		File f = new File("input.txt");
		int counter = 0;
		Scanner s = new Scanner(f);
		int n = s.nextInt();
		System.out.println(n);
		s.nextLine();
		String buf = s.nextLine();
		String[] result;
		Complex.ComplexNumber complexArr[] = new Complex.ComplexNumber[n];
		for (int i = 0; i < n; i++) {
			complexArr[i] = new Complex.ComplexNumber(0, 0);
		}
		System.out.println("after init:");
		printComplexArr(complexArr);

		result = buf.split(",");
		
		for (String str : result) {
			String[] tempNoI = str.split("i");
			String[] tempNums = tempNoI[0].split("\\+");
			double x, y;
			x = Double.parseDouble(tempNums[0]);
			y = Double.parseDouble(tempNums[1]);
			complexArr[counter] = new Complex.ComplexNumber(x, y);
			counter++;
		}
		System.out.println("after getting the numbers:");
		printComplexArr(complexArr);

		complexArr = RecFFT(n, complexArr);
		fixResult ( n, complexArr);
		System.out.println("after calculate:");
		printComplexArr(complexArr);
		s.close();
	}
	
	private static void fixResult (int n, Complex.ComplexNumber[] complexArr) {
		Complex.ComplexNumber half = new Complex.ComplexNumber(0.5,0);
		
		for(int i = 0; i < n; i++) {
			complexArr[i] = complexArr[i].multiply(half);
		}
	}

	
	private static Complex.ComplexNumber[] RecFFT(int n, Complex.ComplexNumber[] complexArr) {
		if (n == 1)
			return new Complex.ComplexNumber[] { complexArr[0] };

		if (Integer.highestOneBit(n) != n) {
			throw new IllegalArgumentException("n is not a power of 2");
		}

		// fft of even terms
		Complex.ComplexNumber[] even = new Complex.ComplexNumber[n / 2];
		for (int k = 0; k < n / 2; k++) {
			even[k] = complexArr[2 * k];
		}
		Complex.ComplexNumber[] q = RecFFT(n / 2, even);

		// fft of odd terms
		Complex.ComplexNumber[] odd = even; // reuse the array
		for (int k = 0; k < n / 2; k++) {
			odd[k] = complexArr[2 * k + 1];
		}
		Complex.ComplexNumber[] r = RecFFT(n / 2, odd);

		// combine
		Complex.ComplexNumber[] y = new Complex.ComplexNumber[n];
		
		for (int k = 0; k < n / 2; k++) {
			double kth = 2 * k * Math.PI / n;
			Complex.ComplexNumber wk = new Complex.ComplexNumber(Math.cos(kth), Math.sin(kth));
			y[k] = q[k].add(r[k].multiply(wk));
			y[k + n / 2] = q[k].minus((r[k].multiply(wk)));
		}
		return y;
	}

	private static void printComplexArr(Complex.ComplexNumber[] complexArr) {
		for (Complex.ComplexNumber complexNumber : complexArr) {
			System.out.println("	" + complexNumber);
		}
	}
	

}
