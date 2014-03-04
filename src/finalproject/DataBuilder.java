package finalproject;
import au.com.bytecode.opencsv.CSVReader;
import java.util.*;



public class DataBuilder {
	
	private LinkedHashMap<Integer,ArrayList<String>> dataMap;			//< col index, { var name (i.e. "AGE" ,  var type (i.e."NUMERIC") }
	private HashMap<String,Integer> reverVarMap;						//< var name , col index > (Reverse dictionary for convenience)
	private HashMap<String,HashMap<String,Double>> catVarCodeMap;			//< var name , < var value, value code >> i.e. ( <"SEX", <"FEMALE", 1.0 >>)
	private DictKeeper dictkeeper;
	
	
	public DataBuilder(){
		//blank versions of all class members should be initialized here.
	}
	
	public List<String> getHeaders( String path){
		//return a string list of headers from the SQL dump file
		return null;
	}
	
	public void addToDataMap(){		
		//takes an incrementer, header value (String), and knowledge of variable type(CATEG,NUMERIC,FREETEXT)
		//adds it to dataMap, reverVarMap and catVarCodeMap
		//NOTE: inputs here will have to be hardcoded from actually looking at the sql dump.
	}
	
	private List<String> freeTextParser(String freeText){
	//takes a long String (i.e. a free text field), breaks up on whitespace, lowercases, and returns List<String>
	return null;
	}
	
	private void categVarMapUpdater(){	
		//given value of a categorical variable, checks whether it currently exists in catVarCodeMap. If not, adds it and encodes the value.
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
		make i = 0
		for each header:
			run addToDataMap(use i, element from getHeaders list, and manual variable type defn)
			i++
		
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
