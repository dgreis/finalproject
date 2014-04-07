package finalproject;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import au.com.bytecode.opencsv.CSVReader;

public class ConceptKeeper {
	
	public LinkedHashMap<Integer,String> conceptNameMap;
	public HashMap<Integer,Integer> conceptOrderMap;
	private final HashMap<String,Boolean> classMap = new HashMap<String,Boolean>();
	
	
	
	public ConceptKeeper(String path){
		this.classMap.put("1", true);
		this.classMap.put("4", true);
		
		this.conceptNameMap = new LinkedHashMap<Integer,String>();
		this.conceptOrderMap = new HashMap<Integer,Integer>(); 
		populateMaps(path);
		
	}
	
	private void populateMaps(String filepath){
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(filepath));
			String [] nextLine;
			reader.readNext();
			while ((nextLine = reader.readNext()) != null) {
				if (classMap.containsKey(nextLine[6])){
					int concept_id = Integer.parseInt(nextLine[0]);
					conceptNameMap.put(concept_id,nextLine[2]);
					int dictLength = conceptOrderMap.size();
					conceptOrderMap.put(concept_id,dictLength);
				}					
			}
			reader.close();	
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}
	

	
	public static void main(String[] args){
		
	}

}
