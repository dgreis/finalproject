package org.openmrs.module.machinelearning.mlcode;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.IOException;
import java.util.List;

//import weka.classifiers.Classifier;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Instances;
import weka.classifiers.bayes.NaiveBayes;


public class ModelTrainer {
	//This class takes a csv as input and outputs a serialized trained model.
	//Main method currently loads, trains, serializes model (for testing)

	//CSV Reader Method (Unused 2/25/14)
	public List<String[]> readCSV(String path){
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
	
	//Weka method to read csv files
	public Instances readData( String path){
	 try{
	 DataSource source = new DataSource(path);
	 Instances data = source.getDataSet();
	 // setting class attribute if the data format does not provide this information
	 // For example, the XRFF format saves the class attribute information as well
	 if (data.classIndex() == -1)
	   data.setClassIndex(data.numAttributes() - 1);	 
	 return data;
	 }
	 catch (Exception e){
		 e.printStackTrace();
	 }
	 return null;
	}
		
	 //Utility print method
	 public static void print(Object p){
		 System.out.println(p);
	 }
 
	 //Utility method to get system path (Unused 2/25/14)
	 public String getPath(){
		 String path = System.getProperty("user.dir");
		 return path;
	 }
	 
	 /*Utility method to pretty print dictionaries (Unused 2/25/14)
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
	 }*/

 

public static void main(String[] args){

	//WEKA CODE (This logic should be exported out of the main method)

	String testfile =  "dummydata/iris.csv";
	ModelTrainer m = new ModelTrainer();
	Instances data = m.readData(testfile);
	NaiveBayes nb = new NaiveBayes();
	try {
		nb.buildClassifier(data);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("print nb");
	print(nb);
	try {
		weka.core.SerializationHelper.write("dummydata/cls.ser", nb);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}		
}

}
