package org.openmrs.module.machinelearning.mlcode;

import java.util.*;

import org.apache.commons.lang3.mutable.MutableDouble;


public class DictKeeperTestDriver {

	public static void main(String[] args){
	
		DictKeeper mk = new DictKeeper();
		//Set dummy map for test purposes
		//LinkedHashMap<String,MutableDouble> m = new LinkedHashMap<String, MutableDouble>();
		//m.put("Kathy", new MutableDouble(42.0));
		//m.put("Bert",  new MutableDouble(343.0));
		//m.put("Skyler",new MutableDouble(420.0));
		//mk.testSetDict(m);
		//mk.serializeMap();
		mk.loadDict("dummydata/dict.ser");
		List<String> messages = Arrays.asList("Bert", "Bert", "Bert", "Kathy", "Skyler","Skyler");
		double[] dlist = mk.makeTextFeatVec(messages);
        for (double d : dlist) {
            System.out.println(d);
        }
        //System.out.println("Is this the line?");
        //System.out.println(mk.getDict());
        
        DictKeeper dk2 = new DictKeeper();
        List<String> othermessages = Arrays.asList("Ernie","Ernie","Bert","Bert","Kathy");
        List<List<String>> twoMes = Arrays.asList(messages,othermessages);
        for (List<String> arr : twoMes){
        	dk2.addToDict(arr);
        }
        System.out.println("this.dict after 2nd test");
        System.out.println(dk2.getDict());
        
        System.out.println();
        System.out.println("Now create a feature vector");
		double[] fv = dk2.makeTextFeatVec(othermessages);
        for (double d : fv) {
            System.out.println(d);
        }
        dk2.saveDict();
        
        System.out.println("Make new DictKeeper, load serialized dict, and make a feature vector according to the loaded dict");
        DictKeeper dk3 = new DictKeeper();
        dk3.loadDict("dummydata/dict.ser");
        List<String> finalmsgs = Arrays.asList("Skyler","Skyler","Skyler","Skyler","Skyler","Ted");
        double[] nfv = dk3.makeTextFeatVec(finalmsgs);
        for (double d : nfv) {
            System.out.println(d);
        }
        
        System.out.println("Now confirm attributes can be gotten");
        System.out.println(dk3.getAttributes());
        

        
	}
}
