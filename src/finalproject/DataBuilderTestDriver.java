package finalproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class DataBuilderTestDriver {
	
	public static void main(String[] args){
		
		List<String> headers = Arrays.asList("SEX","AGE","UNCODED_DIAGNOSIS","ACTUAL_DIAGNOSIS","FREETEXT","MOREFREETEXT","BLOOD_PRESSURE");
        List<String> rawText = Arrays.asList("MALE","45","Possibly influenza. I'm not sure.","HIV","Free text free text FREE TEXT","Beep Bloop","564.2");
        
		DataBuilder db = new DataBuilder();
		HashMap<String,HashMap<String,Double>> catmap = db.catVarCodeMap;
		LinkedHashMap<Integer,ArrayList<String>>datmap	= db.dataMap;
		DictKeeper dk = db.dictKeeper;
		LinkedHashMap<String,Integer>revmap = db.reverVarMap;
		
		dk.addToDict(Arrays.asList("Possibly","influenza.","I'm","not","sure.","Free","text","free","text","FREE","TEXT","Beep","Bloop"));
		
		datmap.put(0, new ArrayList<String>(Arrays.asList("SEX","CATEG")));
		datmap.put(1, new ArrayList<String>(Arrays.asList("AGE","NUMER")));
		//datmap.put(2, new ArrayList<String>(Arrays.asList("UNCODED_DIAGNOSIS","FREETEXT")));
		datmap.put(3, new ArrayList<String>(Arrays.asList("ACTUAL_DIAGNOSIS","CATEG")));
		//datmap.put(4, new ArrayList<String>(Arrays.asList("FREETEXT","FREETEXT")));
		//datmap.put(5, new ArrayList<String>(Arrays.asList("MOREFREETEXT","FREETEXT")));
		datmap.put(6, new ArrayList<String>(Arrays.asList("BLOOD_PRESSURE","NUMER")));
		
		revmap.put("SEX", 0);
		revmap.put("AGE", 1);
		//revmap.put("UNCODED_DIAGNOSIS", 2);
		revmap.put("ACTUAL_DIAGNOSIS", 2);
		//revmap.put("FREETEXT", 4);
		//revmap.put("MOREFREETEXT", 5);
		revmap.put("BLOOD_PRESSURE", 3);
		
		//properties.put("a", new HashMap<String, Map<String,String>>());
		//properites.get("a").put("b", new HashMap<String,String>());

		catmap.put("SEX", new HashMap<String,Double>());
		catmap.get("SEX").put("MALE", 1.0);
		catmap.get("SEX").put("FEMALE",2.0);
		
		catmap.put("ACTUAL_DIAGNOSIS",new HashMap<String,Double>());
		catmap.get("ACTUAL_DIAGNOSIS").put("DEAD",1.0);
		catmap.get("ACTUAL_DIAGNOSIS").put("FINE",2.0);
		catmap.get("ACTUAL_DIAGNOSIS").put("HIV",3.0);
		
		db.saveCatVarCodeMap();
		db.saveReverVarMap();
		db.saveDataMap();
		dk.saveDict();
		
		//System.out.println(catmap.get("SEX").get("MALE"));
		//System.out.println(rawText.get(0));
		double[] dlist = db.makeFeatVector(rawText);
        for (double d : dlist) {
            System.out.println(d);
        }
		//Iterator it = rawText.iterator();
		//while (it.hasNext()) {
			//String strVal = (String)it.next();
			//System.out.println(strVal);

	//TEST FOR WHETHER PATH CONSTRUCTOR IS WORKING
        DataBuilder db2 = new DataBuilder("meh");
        System.out.println(db2.reverVarMap.size());
	
	}
	
	
		
	}


