package finalproject;

import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.DenseInstance;
import weka.core.Attribute;
import weka.core.FastVector;

@SuppressWarnings("deprecation")
public class Predictor {
	//This class reads a saved (serialized) model and makes predictions when it is fed a feature vector as double[]
	//TODO: implement a canonical dictionary to make sure double[] is always in same order.

	private Classifier cls;
	
	public Predictor(String savmodel){
		try {
			cls = (Classifier) weka.core.SerializationHelper.read(savmodel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	//Create Instance from vector of doubles
	private Instance makeInstance( FastVector<Attribute> fvWekaAttributes, double[] featvec){
		Instance iExample = new DenseInstance(5); 
		iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), featvec[0]);       
		iExample.setValue((Attribute)fvWekaAttributes.elementAt(1), featvec[1]);       
		iExample.setValue((Attribute)fvWekaAttributes.elementAt(2), featvec[2]); 
		iExample.setValue((Attribute)fvWekaAttributes.elementAt(3), featvec[3]); 	
		iExample.setValue((Attribute)fvWekaAttributes.elementAt(4), "Iris-setosa");
		iExample.setMissing(4);
		return iExample;
	}
	
	@SuppressWarnings("unchecked")
	private FastVector<Attribute> dummyAttMaker(){
		 Attribute Attribute1 = new Attribute("Sepal.Length");
		 Attribute Attribute2 = new Attribute("Sepal.Width");
		 Attribute Attribute3 = new Attribute("Petal.Length");
		 Attribute Attribute4 = new Attribute("Petal.Width");
		 
		 @SuppressWarnings("rawtypes")
		FastVector fvClassVal = new FastVector(3);
		 fvClassVal.addElement("Iris-setosa");
		 fvClassVal.addElement("Iris-versicolor");
		 fvClassVal.addElement("Iris-virginica");
		 Attribute ClassAttribute = new Attribute("Speciies", fvClassVal);
	
		 @SuppressWarnings("rawtypes")
		FastVector fvWekaAttributes = new FastVector(5);
		 fvWekaAttributes.addElement(Attribute1);    
		 fvWekaAttributes.addElement(Attribute2);    
		 fvWekaAttributes.addElement(Attribute3);
		 fvWekaAttributes.addElement(Attribute4);
		 fvWekaAttributes.addElement(ClassAttribute);
		 
		 return fvWekaAttributes;
	}
	
	public double predict(double[] featvec){
		FastVector<Attribute> atts = dummyAttMaker();
		Instances insts = new Instances("toClas",atts,0);
		insts.setClassIndex(insts.numAttributes() - 1);
		Instance i = makeInstance(atts, featvec);
		insts.add(i);
		try {
			double predic = cls.classifyInstance(insts.instance(0));
			System.out.println(predic + " -> " + insts.classAttribute().value((int) predic));
			return predic;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -999.99;	//This return value means something went wrong.
	}
		
	public static void main(String[] args){
		Predictor p = new Predictor("dummydata/cls.ser");
		double[] f = {2.4,5.5,3.0,5.5};
		double pred = p.predict(f);
		//System.out.println(pred);
	}
	
}
