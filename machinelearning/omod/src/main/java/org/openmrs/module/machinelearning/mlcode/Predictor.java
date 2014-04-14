package org.openmrs.module.machinelearning.mlcode;

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
	public Instance makeInstance( ArrayList<Attribute> atts, double[] featvec){
		
		Instance iExample = new DenseInstance(atts.size());
		for( int i = 0 ; i < atts.size() ; i++ )
        {
			iExample.setValue(i, featvec[i]);
    	}	
		//iExample.setValue((Attribute)fvWekaAttributes.elementAt(4), "Iris-setosa");
		//iExample.setMissing(4);
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
				//System.out.println("\n\nkey:"+key+"\n\n\nDAVID:\n"+db.reverVarMap.entrySet().toString());
				
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
	            	String[] splited = db.parseFreeText(val);
	        		for (String s: splited)
	         	        parsedText.add(s);
	            }

	    	           
			} catch (JSONException e) {
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
		double[] featVec = makeFeatVector(json);
		Instance i = makeInstance(atts, featVec);		
		insts.add(i);
		try {
			double predic = cls.classifyInstance(insts.instance(0));
			System.out.println(predic + " -> " + insts.classAttribute().value((int) predic));
			return predic;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -999.99;	//This return value means something went wrong.
	}
		
	public static void main(String[] args){
	}
	
}
