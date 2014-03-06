package finalproject;

import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.DenseInstance;
import weka.core.Attribute;
import weka.core.FastVector;

@SuppressWarnings("deprecation")
public class Predictor {
	//This class reads a saved (serialized) model and makes predictions when it is fed a feature vector as double[]

	private Classifier cls;
	private DataBuilder db;
	
	/*public Predictor(){
		try {
			cls = (Classifier) weka.core.SerializationHelper.read(savmodel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public Predictor(String modelFolder){
		db = new DataBuilder(modelFolder);		
	}
	
	public DataBuilder getdb(){
		return this.db;
	}
	
	//Create Instance from vector of doubles
	private Instance makeInstance( ArrayList<Attribute> atts, double[] featvec){
		
		Instance iExample = new DenseInstance(atts.size());
		int i = 0;
		Iterator it = atts.iterator();
	    while (it.hasNext()) {
	        String tok = (String)it.next();
	        //String key = pairs.getKey();
	        myList.add(new Attribute(tok));
	    }
		iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), featvec[0]);       
		iExample.setValue((Attribute)fvWekaAttributes.elementAt(1), featvec[1]);       
		iExample.setValue((Attribute)fvWekaAttributes.elementAt(2), featvec[2]); 
		iExample.setValue((Attribute)fvWekaAttributes.elementAt(3), featvec[3]); 	
		iExample.setValue((Attribute)fvWekaAttributes.elementAt(4), "Iris-setosa");
		iExample.setMissing(4);
		return iExample;
	}
	
	public double[] makeFeatVector(JSONObject json){
		//method for Predictor.
		//looks up key
		//if not in revMap
			//let dictionary handle value
		//if in revMap
			//insert in array
		//at end:
			//detach values of dictionary
		//System.out.println(db.reverVarMap);
		double[] vector = new double[db.reverVarMap.size()];
		List<String> parsedText = new ArrayList<String>();
		Iterator keys = json.keys();
		while( keys.hasNext() ){
            String key = (String)keys.next();
            String val;
			try {
				val = json.getString(key);
				if (db.reverVarMap.containsKey(key)){							//Is it free-text or not free-text
	            	if (db.catVarCodeMap.containsKey(key)){						//Is it categorical or numeric?
	            		double code = db.catVarCodeMap.get(key).get(val);
	    				vector[db.reverVarMap.get(key)] = code;
	            	}
	            	else{
	    	        	double dub = Double.parseDouble(val);
	    	        	vector[db.reverVarMap.get(key)] = dub;
	            	}
				}
	            else{
	            	String[] splited = val.split("\\s+");			//TODO: FIX WHEN PARSETEXT IS READY
	        		for (String s: splited)
	         	        parsedText.add(s);
	            }

	    	           
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}            
            		
            }
		double[] textVector = db.dictKeeper.makeTextFeatVec(parsedText);		
		return concat(vector,textVector);
	}

	private double[] concat(double[] A, double[] B) {
		   int aLen = A.length;
		   int bLen = B.length;
		   double[] C= new double[aLen+bLen];
		   System.arraycopy(A, 0, C, 0, aLen);
		   System.arraycopy(B, 0, C, aLen, bLen);
		   return C;
		}
	
	
	public double predict(JSONObject json){
		/*
		 * This class can receive a json-like input. This can make sense of it using the deserialized data model
		 * 
		 */
		ArrayList<Attribute> atts = db.getAttributes();	
		Instances insts = new Instances("toClas",atts,0);
		insts.setClassIndex(insts.numAttributes() - 1);
		//TODO: transform json into a featvec
		double[] featVec = makeFeatVec(json);
		Instance i = makeInstance(atts, featVec);
		
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
