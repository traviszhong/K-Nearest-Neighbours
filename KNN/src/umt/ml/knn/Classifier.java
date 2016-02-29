package umt.ml.knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.Queue;
/**
 * 
 * @author Zhong Ziyue
 *
 * @email zhongzy@strongit.com.cn
 * 
 * Feb 6, 2016
 */
public class Classifier {
	private double[][] trainingData;
	private int[] trainingClass;
	private String dataPath;
	private Map<String,Integer> classMap;
	private Map<Integer,String> mapToClass;
	private double[] max;
	private double[] min;
	/**
	 * Constructor
	 * @param path: The path of csv file
	 */
	public Classifier(String path){
		this.dataPath=path;
		trainingData=new double[1000][4];
		trainingClass=new int[1000];
		this.classMap=new HashMap();
		this.mapToClass=new HashMap();
		max=new double[4];
		min=new double[4];
		Arrays.fill(max,Double.MIN_VALUE);
		Arrays.fill(min,Double.MAX_VALUE);
		classMap.put("apple",0);
		classMap.put("orange",1);
		classMap.put("lemon",2);
		classMap.put("peach", 3);
		mapToClass.put(0,"apple");
		mapToClass.put(1,"orange");
		mapToClass.put(2,"lemon");
		mapToClass.put(3,"peach");
	}
	/**
	 * This function helps with reading in the fruit data csv file
	 * Will also get the max and min value for each dimension while loading the data
	 * @throws IOException: file I/O exceptions
	 */
	public void loadData() throws IOException{
		File csvData= new File(this.dataPath);
		BufferedReader br=new BufferedReader(new FileReader(csvData));
		String line="";
		int index=-1;
		while((line=br.readLine())!=null){
			if(index==-1){
				index++;continue;
			}
			StringTokenizer st=new StringTokenizer(line,",");
			for(int i=0;i<=3;i++){
				trainingData[index][i]=Double.valueOf(st.nextToken());
				max[i]=Math.max(max[i],trainingData[index][i]);
				min[i]=Math.min(min[i],trainingData[index][i]);
			}
			trainingClass[index]=classMap.get(st.nextToken());
			index++;
		}
	}
	/**
	 * Normalize the data
	 */
	public void dataNormalize(){
		for(double[] sample:trainingData){
			for(int i=0;i<=3;i++) 
				sample[i]=(sample[i]-min[i])/(max[i]-min[i]);
		}
	}
	/**
	 * 
	 * @param k: The number of neighbours, when k=0 will use the function classify by weight
	 * @param attr1 :These are attributes of fruit data
	 * @param attr2
	 * @param attr3
	 * @param attr4
	 * @return: the class of test sample
	 */
	public String classify(int k,double attr1,double attr2,double attr3,double attr4){
		attr1=(attr1-min[0])/(max[0]-min[0]);
		attr2=(attr2-min[1])/(max[1]-min[1]);
		attr3=(attr3-min[2])/(max[2]-min[2]);
		attr4=(attr4-min[3])/(max[3]-min[3]);
		if(k==0) return classifyByWeight(attr1,attr2,attr3,attr4);
		else return classifyByKNN(k,attr1,attr2,attr3,attr4);
	}
	/**
	 * This function do the weighted classification method
	 * @param attr1
	 * @param attr2
	 * @param attr3
	 * @param attr4
	 * @return: the result of classification by using weighted method
	 */
	public String classifyByWeight(double attr1,double attr2,double attr3,double attr4){
		double[] voteTable=new double[4];
		for(int i=0;i<trainingData.length;i++){
			double sum=0;
			sum+=(attr1-trainingData[i][0])*(attr1-trainingData[i][0]);
			sum+=(attr2-trainingData[i][1])*(attr2-trainingData[i][1]);
			sum+=(attr3-trainingData[i][2])*(attr3-trainingData[i][2]);
			sum+=(attr4-trainingData[i][3])*(attr4-trainingData[i][3]);
			double distance=Math.sqrt(sum);
			voteTable[this.trainingClass[i]]+=1/distance;
		}
		int result=0;
		double maxCnt=0;
		for(int i=0;i<=3;i++){
			if(maxCnt<voteTable[i]){
				maxCnt=voteTable[i];
				result=i;
			}
		}
		return this.mapToClass.get(result);
	}
	/**
	 * 
	 * @param k: number of neighbours to vote
	 * @param attr1
	 * @param attr2
	 * @param attr3
	 * @param attr4
	 * @return: result of classification by using knn
	 */
	public String classifyByKNN(int k,double attr1,double attr2,double attr3,double attr4){
		//This is a max heap, I overload the compare method, so that it can compare Vote class
		Queue<Vote> maxHeap=new PriorityQueue(k,new Comparator<Vote>(){
			public int compare(Vote v1,Vote v2){
				return Double.compare(v2.getDistance(), v1.getDistance());
			}
		});
		for(int i=0;i<trainingData.length;i++){
			double sum=0;
			//calculate the distance
			sum+=(attr1-trainingData[i][0])*(attr1-trainingData[i][0]);
			sum+=(attr2-trainingData[i][1])*(attr2-trainingData[i][1]);
			sum+=(attr3-trainingData[i][2])*(attr3-trainingData[i][2]);
			sum+=(attr4-trainingData[i][3])*(attr4-trainingData[i][3]);
			double distance=Math.sqrt(sum);
			Vote cur=new Vote(distance,trainingClass[i]);
			maxHeap.add(cur);//put a vote into the heap
			//check the size of the heap,if it reach the maximum size, then get rid of the top of the heap
			if(maxHeap.size()>k) maxHeap.poll();
		}
		int[] voteTable=new int[4];
		while(!maxHeap.isEmpty()) voteTable[maxHeap.poll().getClassVote()]++;
		int result=0;
		int maxCnt=0;
		//vote
		for(int i=0;i<=3;i++){
			if(maxCnt<voteTable[i]){
				maxCnt=voteTable[i];
				result=i;
			}
		}
		return this.mapToClass.get(result);
	}
	
}
