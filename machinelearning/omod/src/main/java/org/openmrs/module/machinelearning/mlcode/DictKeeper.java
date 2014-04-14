package org.openmrs.module.machinelearning.mlcode;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import org.apache.commons.lang3.mutable.MutableDouble;

import weka.core.Attribute;
import weka.core.FastVector;

public class DictKeeper {
	
	private LinkedHashMap<String, MutableDouble> dict;

	public DictKeeper(){
	}
	
	public DictKeeper(String path){
		loadDict(path);
	}
	
	
	private final void setDict(LinkedHashMap<String, MutableDouble> dict) {
		this.dict = dict;
	}
	

	public final void testSetDict(LinkedHashMap<String, MutableDouble> dict){
		this.dict = dict;
	}
	
	public LinkedHashMap<String, MutableDouble> getDict() {
		return dict;
	}
	
	public void saveDict(String path){
		try {
		LinkedHashMap<String, MutableDouble> map = this.dict;
		if (map == null){
			System.out.println("MapKeeper must be associated with a map before it can serialize the map. Ending Program...");
			System.exit(0);
		}
		//iterating over keys only
		for (String key : map.keySet()) {
			map.put(key, new MutableDouble(0.0) );
		}
		FileOutputStream fileOut = new FileOutputStream(path+"dict.ser");
	    ObjectOutputStream out = new ObjectOutputStream(fileOut);
	    out.writeObject(map);
	    out.close();
	    fileOut.close();
	    System.out.println("Serialized data is saved in dummydata/dummodel/dict.ser");
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	public void loadDict (String path){
	      try
	      {
	         FileInputStream fileIn = new FileInputStream(path);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         LinkedHashMap m = (LinkedHashMap) in.readObject();
	         in.close();
	         fileIn.close();
	         setDict(m);
	       }
	      catch(IOException i)
	       {
	         i.printStackTrace();
	       }catch(ClassNotFoundException c)
	       {
	         System.out.println("Employee class not found");
	         c.printStackTrace();
	      }	
	}
	
	public ArrayList<Attribute> getAttributes(){
		
		Collection<String> keySet = this.dict.keySet();
		ArrayList<Attribute> myList = new ArrayList<Attribute>();
		Iterator it = keySet.iterator();
	    while (it.hasNext()) {
	        String tok = (String)it.next();
	        //String key = pairs.getKey();
	        myList.add(new Attribute(tok));
	    }
	    return myList;	
	}

	
	public double[] makeTextFeatVec(List<String> parsedText){
		if (this.dict == null)
			System.out.println("MapKeeper must be associated with a map before it can create a text feature vector. Ending Program...");
		//System.out.println("this.dict at beginning");
		//System.out.println(this.dict);
		//LinkedHashMap<String, MutableDouble> freshMap = deserializeMap(this.dictLoc, false);
		LinkedHashMap<String,MutableDouble> freshDict = new LinkedHashMap<String,MutableDouble>();
		Iterator it = this.dict.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String,MutableDouble> pairs = (Map.Entry<String,MutableDouble>)it.next();
		        //String key = pairs.getKey();		        
		        freshDict.put(pairs.getKey(), new MutableDouble(0.0));
		    }
        //System.out.println("Ref Comp Test");
        //System.out.println(this.dict == freshDict);
        //System.out.println("This is freshMap before");
        //System.out.println(freshDict);
		for (String tok : parsedText){
			MutableDouble count = freshDict.get(tok);
			if (count == null) {System.out.println("New Token Observed");}
			else {
			    count.increment();
			}			
		}
		//System.out.println("now freshMap:");
		//System.out.println(freshDict);
		Collection vals = freshDict.values();
		//System.out.println(this.getDict().values());
		double[] dest = new double[vals.size()];
		Iterator<MutableDouble> fooIter = vals.iterator();
	    int i = 0;
		while (fooIter.hasNext()){
	        dest[i] = fooIter.next().doubleValue();
	        i++;
	      }
		//System.out.println("Now at end, this is this.dict");
		//System.out.println(this.dict);
	    return dest;	
	}
	
	public void addToDict(List<String> parsedText){
		if (this.dict == null){
			LinkedHashMap<String,MutableDouble> lhm = new LinkedHashMap<String,MutableDouble>();
			setDict(lhm);
		}
		for (String tok : parsedText){
			MutableDouble count = this.dict.get(tok);
			if (count == null) {    
				this.dict.put(tok, new MutableDouble(1.0));
			}
			else {
			    count.increment();
			}			
		}
	}
	
	public static void main(String[] args){
		
	}


}
