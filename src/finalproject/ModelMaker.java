package finalproject;
import net.sf.javaml.classification.*;
import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import net.sf.javaml.tools.data.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

import net.sf.javaml.core.*;
import net.sf.javaml.filter.normalize.NormalizeMidrange;
import net.sf.javaml.classification.bayes.NaiveBayesClassifier;
import net.sf.javaml.classification.evaluation.*;


public class ModelMaker {

	
	public List<String[]> readfile(String path){
	 try{
			CSVReader reader = new CSVReader(new FileReader(path));
			List<String[]> myEntries = reader.readAll();
			reader.close();
			return myEntries;			
		}		
	 catch (FileNotFoundException ex)
	 	{
			ex.printStackTrace();
		} 
	 catch (IOException e) 
	 	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return null;
	 		 
 }
 
 private static void print(Object p){
	 System.out.println(p);
 }
 
 public Dataset importData (String path){
	 try
	 {	 
	 Dataset data = FileHandler.loadDataset(new File(path),4,","); 	// Second param '4' tells Dataset which index corresponds to class label		
	 NormalizeMidrange nmr = new NormalizeMidrange();
	 nmr.filter(data);
	 return data;
	 }
	 catch (Exception e){
		 e.printStackTrace();
	 }
	 return null;
 }
 
 public void serializeClassifier(NaiveBayesClassifier clf){
	 try
	 {
	 FileOutputStream fileStream = new FileOutputStream("clf.ser");
	 ObjectOutputStream os = new ObjectOutputStream(fileStream);
	 print(clf.getClass());
	 os.writeObject(clf);
	 os.close();
	 }
	 catch (FileNotFoundException ex)
	 	{
			ex.printStackTrace();
		}
	 catch (IOException e) 
	 	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 }
 
 public String getPath(){
	 String path = System.getProperty("user.dir");
	 return path;
 }
 
 public static void splitTestTrain(Dataset data){	
	 Random r = new Random();
	 Dataset[] folds = data.folds(10,r);
	 //ModelMaker.print(folds);
	 //for (Dataset set : folds) {
	 //   System.out.println(set.getClass());
	 //}
	 Dataset g = folds[1];
	 print(g.toArray());
 }
 
 public void prettyPrint( Map<Object,PerformanceMeasure> results) {
     StringBuilder sb = new StringBuilder();
     Iterator<Map.Entry<Object,PerformanceMeasure>> iter = results.entrySet().iterator();
     while (iter.hasNext()) {
         Map.Entry<Object, PerformanceMeasure> entry = iter.next();
         sb.append(entry.getKey());
         sb.append(':');
         sb.append('\n');
         sb.append(entry.getValue());
         sb.append('\n');
         //if (iter.hasNext()) {
         //    sb.append(',').append(' ');
         //}
     }
     print( sb.toString());
 }

 

public static void main(String[] args){
	//This is main basic workflow of this class. Implementation can be improved
	String testfile =  "dummydata/iris.data";
	//String testfile =  "/Users/dgreis/Documents/workspace/finalproject/dummydata/iris.data";
	ModelMaker m = new ModelMaker();
	//String path = m.getPath();
	Dataset dat = m.importData(testfile);
	NaiveBayesClassifier nb = new NaiveBayesClassifier(true, true, false);
	CrossValidation cv = new CrossValidation(nb);
	m.prettyPrint((cv.crossValidation(dat)));
	//m.serializeClassifier(nb);
	//System.out.println(path);
	

}


}
