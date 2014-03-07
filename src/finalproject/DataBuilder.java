package finalproject;
import au.com.bytecode.opencsv.CSVReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import org.apache.commons.lang3.mutable.MutableDouble;

import weka.core.Attribute;



public class DataBuilder {
	
	public LinkedHashMap<Integer,ArrayList<String>> dataMap;			//< col index, { var name (i.e. "AGE" ,  var type (i.e."NUMERIC") }
	public LinkedHashMap<String,Integer> reverVarMap;						//< var name , col index > (Reverse dictionary for convenience)
	public HashMap<String,HashMap<String,Double>> catVarCodeMap;			//< var name , < var value, value code >> i.e. ( <"SEX", <"FEMALE", 1.0 >>)
	public DictKeeper dictKeeper;
	
	
	public DataBuilder(){
		dataMap = new LinkedHashMap<Integer,ArrayList<String>>();
		reverVarMap = new LinkedHashMap<String,Integer>();
		catVarCodeMap = new HashMap<String,HashMap<String,Double>>();
		dictKeeper = new DictKeeper();
	}
	
	public DataBuilder(String datModelDir){
		//System.out.println(datModelDir);
		loadDataMap("dummydata/dummodel/datamap.ser");
		loadReverVarMap("dummydata/dummodel/revervarmap.ser");
		loadCatVarCodeMap("dummydata/dummodel/catvarcodemap.ser");
		dictKeeper = new DictKeeper();
		dictKeeper.loadDict("dummydata/dummodel/dict.ser");
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
	
	private void setDataMap(LinkedHashMap<Integer,ArrayList<String>> dm){
		this.dataMap=dm;
	}
	
	private void setReverVarMap (LinkedHashMap<String,Integer> rvm){
		this.reverVarMap = rvm;
	}
	
	private void setCatVarCodeMap (HashMap<String,HashMap<String,Double>> cvcm){
		this.catVarCodeMap = cvcm;
	}
	
	public ArrayList<Attribute> getAttributes(){		//TODO: CHECK IF THESE ARE ORDERED CORRECTLY
		
		Collection<String> keySet = this.reverVarMap.keySet();
		ArrayList<Attribute> nonTextAtts = new ArrayList<Attribute>();
		Iterator it = keySet.iterator();
	    while (it.hasNext()) {
	        String tok = (String)it.next();
	        if (catVarCodeMap.containsKey(tok)){
//	        	System.out.println(catVarCodeMap.get(tok));
	        	int ents = catVarCodeMap.get(tok).size();
	        	List my_nominal_values = new ArrayList(ents); 
	        	double label = 1.0;
	        	for( int i = 0 ; i < ents ; i++ )
	            {
	        		String strVal = String.valueOf(label);
	        		my_nominal_values.add(strVal);
	        		label = label + 1.0;
	        	}
	        	nonTextAtts.add(new Attribute(tok, my_nominal_values));       
	        }
	        else{
	        	nonTextAtts.add(new Attribute(tok));
	        }
	    }
	    ArrayList<Attribute> textAtts = this.dictKeeper.getAttributes();
	    ArrayList<Attribute> attList = new ArrayList<Attribute>();
	    attList.addAll(nonTextAtts);
	    attList.addAll(textAtts);
	    return attList;
	}
	
	public double[] makeFeatVector(List<String> csvLine){
	//Once dataMap is populated, this creates an double array based off the keys of the dataMap/reverMap for all non-free-text variables
	//for each field, this function looks up whether it is a)numeric b)categorical c)free-text
	//if numeric:
		// parse the string into a double, add it to array
	//if categorical:
		//look up code in catVarCodeMap; add it to array
	//if freetext:
		//populated dictkeeper can deal with it
	//After each line is complete, take array for non-free-text variables, append it to output from dictkeeper, return final featvec array 
	double[] vector = new double[this.reverVarMap.size()];
	List<String> parsedText = new ArrayList<String>();
	Iterator it = csvLine.iterator();
    int i = 0;
	while (it.hasNext()) {
        if (dataMap.containsKey(i) == true){
        	ArrayList<String> ent = dataMap.get(i);
        	String varName = ent.get(0);
        	String varType = ent.get(1);
    			if (varType == "NUMER"){
    	        	String strdub = (String)it.next();
    	        	double dub = Double.parseDouble(strdub);
    	        	vector[reverVarMap.get(varName)] = dub;
        	     }
    			if (varType == "CATEG"){
    				String strVal = (String)it.next();
    				System.out.println("Printing strVal");
    				System.out.println(strVal);
    				double code = catVarCodeMap.get(varName).get(strVal);
    				vector[reverVarMap.get(varName)] = code;
    			}    	
        }
        else{
        	String freeTextField = (String)it.next();
        	//List<String> parsedText = freeTextParser(freeTextField);		//TODO: FIX THIS WHEN FREETEXTPARSER IS READY
        	String[] splited = freeTextField.split("\\s+");
    		for (String s: splited)
     	        parsedText.add(s);
        }	   		
    i ++; 
	}
	double[] textVector = dictKeeper.makeTextFeatVec(parsedText);		
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
	
	private void writeFeatVector(double[] featVec){
		//writes feature vector to a csv file. Should be used to output data.
	}
	
	public void saveDataMap(){
		try{
			FileOutputStream fileOut = new FileOutputStream("dummydata/dummodel/datamap.ser");
		    ObjectOutputStream out = new ObjectOutputStream(fileOut);
		    out.writeObject(this.dataMap);
		    out.close();
		    fileOut.close();
		    System.out.println("Serialized data is saved in dummydata/dummodel/datamap.ser");
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	public void saveReverVarMap(){
		try{
			FileOutputStream fileOut = new FileOutputStream("dummydata/dummodel/revervarmap.ser");
		    ObjectOutputStream out = new ObjectOutputStream(fileOut);
		    out.writeObject(this.reverVarMap);
		    out.close();
		    fileOut.close();
		    System.out.println("Serialized data is saved in dummydata/dummodel/revervarmap.ser");
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	public void saveCatVarCodeMap(){
		try{
			FileOutputStream fileOut = new FileOutputStream("dummydata/dummodel/catvarcodemap.ser");
		    ObjectOutputStream out = new ObjectOutputStream(fileOut);
		    out.writeObject(this.catVarCodeMap);
		    out.close();
		    fileOut.close();
		    System.out.println("Serialized data is saved in dummydata/dummodel/catvarcodemap.ser");
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	public void loadDataMap (String path){
	      try
	      {
	         FileInputStream fileIn = new FileInputStream(path);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         LinkedHashMap m = (LinkedHashMap) in.readObject();
	         in.close();
	         fileIn.close();
	         setDataMap(m);
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
	
	public void loadReverVarMap (String path){
	      try
	      {
	         FileInputStream fileIn = new FileInputStream(path);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         LinkedHashMap m = (LinkedHashMap) in.readObject();
	         in.close();
	         fileIn.close();
	         setReverVarMap(m);
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
	
	public void loadCatVarCodeMap (String path){
	      try
	      {
	         FileInputStream fileIn = new FileInputStream(path);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         HashMap m = (HashMap) in.readObject();
	         in.close();
	         fileIn.close();
	         setCatVarCodeMap(m);
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
