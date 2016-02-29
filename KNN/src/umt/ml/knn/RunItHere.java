package umt.ml.knn;

import java.io.File;
import java.io.IOException;

public class RunItHere {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double res=0;
		try {
			Test t=new Test("src/testFruit.csv");
			res=t.testAccuracy(100);//we can tune the k here,k=0 is the weighted method
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(res);
	}

}
