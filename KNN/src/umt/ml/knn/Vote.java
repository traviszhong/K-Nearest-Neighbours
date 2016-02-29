package umt.ml.knn;
/**
 * The vote class, contains the distance of a sample from training set to the test sample and the class label
 * of the sample from training dataset 
 * @author Zhong Ziyue
 *
 * @email zhongzy@strongit.com.cn
 * 
 * Feb 6, 2016
 */
public class Vote {
	private double distance;
	private int classVote;
	public Vote(double d,int c){
		this.distance=d;
		this.classVote=c;
	}
	public double getDistance() {
		return distance;
	}
	public int getClassVote() {
		return classVote;
	}
}
