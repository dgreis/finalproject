package finalproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DataBuilderTestDriver {
	
	public static void main(String[] args){
		
		List<String> headers = Arrays.asList("SEX","AGE","UNCODED_DIAGNOSIS","ACTUAL_DIAGNOSIS","FREETEXT","MOREFREETEXT","BLOOD_PRESSURE");
        List<String> rawText = Arrays.asList("Male","45","Possibly influenza. I'm not sure.","HIV","Free text free text FREE TEXT","Beep Bloop","564.2");
        
		DataBuilder db = new DataBuilder();
		HashMap<String,HashMap<String,Double>> catmap = db.catVarCodeMap;
		LinkedHashMap<Integer,ArrayList<String>>datmap	= db.dataMap;
		DictKeeper dk = db.dictKeeper;
		HashMap<String,Integer>revmap = db.reverVarMap;
		
		datmap.put(0, new ArrayList<String>(Arrays.asList("SEX","CATEG")));
		datmap.put(1, new ArrayList<String>(Arrays.asList("AGE","NUMER")));
		datmap.put(2, new ArrayList<String>(Arrays.asList("UNCODED_DIAGNOSIS","FREETEXT")));
		datmap.put(3, new ArrayList<String>(Arrays.asList("ACTUAL_DIAGNOSIS","CATEG")));
		
	}

}
