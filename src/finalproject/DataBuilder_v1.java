package finalproject;
import au.com.bytecode.opencsv.CSVReader;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class DataBuilder {
	
	private LinkedHashMap<Integer,ArrayList<String>> dataMap;			//< col index, { var name (i.e. "AGE" ,  var type (i.e."NUMERIC") }
	private HashMap<String,Integer> reverVarMap;						//< var name , col index > (Reverse dictionary for convenience)
	private HashMap<String,HashMap<String,Double>> catVarCodeMap;			//< var name , < var value, value code >> i.e. ( <"SEX", <"FEMALE", 1.0 >>)
	private DictKeeper dictKeeper;
	
	
	public DataBuilder(){
		dataMap = new LinkedHashMap<Integer,ArrayList<String>>();
		reverVarMap = new HashMap<String,Integer>();
		catVarCodeMap = new HashMap<String,HashMap<String,Double>>();
		DictKeeper dictKeeper = new DictKeeper();
	}
	
	public List<String> getHeaders( String path){
		//return a string list of headers from the SQL dump file
		
		String fileName = "data.csv"; //name of the file goes here
		CSVReader reader = new CSVReader(new FileReader(fileName ));
		
		String[] header = reader.readNext();//header string is retrieved here
		
		return header;
	}
	
	public void addToDataMap(){		
		//TAKES COL INDEX OF NUMERIC OR CATEGORICAL VARIABLE, header value (String), and knowledge of variable type(CATEG,NUMERIC)
		//adds it to dataMap and catVarCodeMap (if necessary) (not reverVarMap)
		//NOTE: inputs here will have to be hardcoded from actually looking at the sql dump.
		Map<String, String> dataMap = new HashMap<Integer, String, String>();
		Map<String, String> catVarCodeMap = new HashMap<Integer, String, String>();
		
		dataMap.put("0", "Sex", "CATEG");
		dataMap.put("1", "Age", "NUMER");
		//dataMap.put("2", "Uncoded_diag", "CATEG");
		dataMap.put("3", "Actual_diag", "CATEG");
		//dataMap.put("4", "Free_text", "CATEG");
		//dataMap.put("5", "More_Free_text", "CATEG");
		dataMap.put("6", "Blood_pressure", "NUMER");
		
		catVarCodeMap.put("0", "Sex", "CATEG");
		catVarCodeMap.put("3", "Actual_diag", "CATEG");
		
		return dataMap, catVarCodeMap;
	}
	
	private List<String> freeTextParser(String freeText){
	//takes a long String (i.e. a free text field), breaks up on whitespace, lowercases, punctuation and returns List<String>
		
	String[] freeText=freeText.replaceAll("[^a-zA-Z ]", "").toLowerCase(); // change the string into lower case and remove everything which is not a text
	splited = freeText.split("\\s+"); //the string is split by whitespace
	
	return splited;
	}
	
	private void categVarMapUpdater(){	
		//given value of a categorical variable, checks whether it currently exists in catVarCodeMap. If not, adds it and encodes the value.
		
		// I am not sure how the variables to check here will be accessed :(
	}
	
	private void populateMaps(String path){
		//makes pass through entire file, executes catVarCodeMapUpdater for each categorical variable on each line
		//parses all free text fields using freeTextParser, feeds results to DictKeeper
		//this method will know which fields are numeric/free-text/categorical by looking it up the dataMap
	}
	
	private double[] makeFeatVector(List<String> csvLine){
	//Once dataMap is populated, this creates an double array based off the keys of the dataMap/reverMap for all non-free-text variables
	//for each field, this function looks up whether it is a)numeric b)categorical c)free-text
	//if numeric:
		// parse the string into a double, add it to array
	//if categorical:
		//look up code in catVarCodeMap; add it to array
	//if freetext:
		//populated dictkeeper can deal with it
	//After each line is complete, take array for non-free-text variables, append it to output from dictkeeper, return final featvec array 
	
	return null;
}
	
	private void writeFeatVector(double[] featVec){
		//writes feature vector to a csv file. Should be used to output data.
	}
	
	//TODO: ALSO, methods to serialize dataMap, reverVarMap, catVarCodeMap
	

	public static void main(String[] args){
		/*
		 **STEP ONE: INITIALIZE dataMaps
		 * 
		List = getHeaders(from sql_dump)
		NEW: LIST of column indices that have Categorical or Numeric values
		for each ind in list of column indices:
			run addToDataMap(use ind, element from getHeaders list, and manual variable type defn)
			
		
		**STEP TWO: POPULATE MAPS (1st pass through the data)
		*
		populateMaps(string sql_dump)
	
		**STEP THREE: WRITE DATA (2nd pass through the data)
		*
		for line in sql_dump:
			makeFeatVec(line)
			writeFeatVec(outfile)
			
		**STEP FOUR: SERIALIZE CLASS MEMBERS
		*
		Three different files to be output
		 */
	
	

 

	}
}
