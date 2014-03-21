package finalproject;
import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;






//import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.Instance;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;


public class ModelTrainer {
	//This class takes a csv as input and outputs a serialized trained model.
	//Main method currently loads, trains, serializes model (for testing)

	
	public Instances prepData(String flatFile, String modelDir){
	 try{	 		 
		 CSVReader reader;
		 reader = new CSVReader(new FileReader(flatFile));
		 String [] nextLine;
		 reader.readNext();
		 DataBuilder db = new DataBuilder(modelDir);
		 ArrayList<Attribute> atts = db.getAttributes();
		 Instances data = new Instances("train",atts,0);
		 int Inslength = atts.size();
		 while ((nextLine = reader.readNext()) != null) {
			 Instance ins = new DenseInstance(Inslength);
			 Iterator<Attribute> it = atts.iterator();
			 for (String s : nextLine){
				 ins.setValue(it.next(), Double.parseDouble(s));
			 }
			 if (it.hasNext()){
				 System.out.println("Something is wrong");
			 }
		 }
		 reader.close();
		 if (data.classIndex() == -1)
			   data.setClassIndex(data.numAttributes() - 1);	 
			 return data;
		 }
			 catch (Exception e){
				 e.printStackTrace();
		 }
		 return null;
}
		 

	 
	 // setting class attribute if the data format does not provide this information
	 // For example, the XRFF format saves the class attribute information as well
	 
		
	

 

public static void main(String[] args){
	

	String flatFile =  "dummydata/TESTOUT.csv";
	String modelDir = "dummydata/dummodel";
	
	ModelTrainer m = new ModelTrainer();	
	Instances data = m.prepData(flatFile,modelDir);
	
	NaiveBayes nb = new NaiveBayes();
	try {
		nb.buildClassifier(data);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		weka.core.SerializationHelper.write("dummydata/cls.ser", nb);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}		
}

}
