package org.openmrs.module.machinelearning.mlcode;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import weka.core.Attribute;



public class DataBuilder {
	
	//TODO: MAKE PRIVATE??
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
		loadDataMap(datModelDir+"/datamap.ser");
		loadReverVarMap(datModelDir+"/revervarmap.ser");
		loadCatVarCodeMap(datModelDir+"/catvarcodemap.ser");
		dictKeeper = new DictKeeper();
		dictKeeper.loadDict(datModelDir+"/dict.ser");
	}
	
	/*public String[] getHeaders( String path){
		//return a string list of headers from the SQL dump file		
		String fileName = path; //name of the file goes here	
		try {
			CSVReader reader = new CSVReader(new FileReader(fileName ));

			String[] header = reader.readNext();//header string is retrieved here
			reader.close();
			return header;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	public String[] getHeaders()
	{
		String[] nftHeaders = reverVarMap.keySet().toArray(new String[reverVarMap.size()]);
		String[] texHeaders = dictKeeper.getDict().keySet().toArray(new String[dictKeeper.getDict().size()]);
		String[] Headers = concat(nftHeaders,texHeaders);
		return Headers;			
	}

	
	
	public  void addToDataMap(int index, String varName, String varType){		
		//TAKES COL INDEX OF NUMERIC OR CATEGORICAL VARIABLE, header value (String), and knowledge of variable type(CATEG,NUMERIC)
		//adds it to dataMap and catVarCodeMap (if necessary) (not reverVarMap)
		//NOTE: inputs here will have to be hardcoded from actually looking at the sql dump.
		dataMap.put(index, new ArrayList<String>(Arrays.asList(varName,varType)));

}
	
	public String[] parseFreeText(String freeText){
		//takes a long String (i.e. a free text field), breaks up on whitespace, lowercases, punctuation and returns List<String>
		String[] words = freeText.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");				
		return words;
		}
		
	
	private void catVarMapUpdater(String VarName, String value){
			if (catVarCodeMap.containsKey(VarName)==false){
				catVarCodeMap.put(VarName, new HashMap<String,Double>());
			}
			if (catVarCodeMap.get(VarName).containsKey(value)){}
			else{
				Collection<Double> codes = catVarCodeMap.get(VarName).values();
				if (codes.size()>0){
					List<Double> list = new ArrayList<Double>( codes );
					Collections.sort(list);
					double max = list.get(list.size()-1);
					double addTo = max + 1.0;
					catVarCodeMap.get(VarName).put(value, addTo);
				}
				else{
					catVarCodeMap.get(VarName).put(value, 0.0);
				}
			}
		//given value of a categorical variable, checks whether it currently exists in catVarCodeMap. If not, adds it and encodes the value.
	}
	
	public void populateMaps(String path){	 //TODO: HANDLE EXTRA LINES AT END OF FILE
		//makes pass through entire file, executes catVarCodeMapUpdater for each categorical variable on each line
		//parses all free text fields using freeTextParser, feeds results to DictKeeper
		//this method will know which fields are numeric/free-text/categorical by looking it up the dataMap
		CSVReader reader;
		
		try {
			reader = new CSVReader(new FileReader(path));
			List<String> parsedText = new ArrayList<String>();
			String [] nextLine;
			String[] headers = reader.readNext();
		    while ((nextLine = reader.readNext()) != null) {
		        //nextLine[] is an array of values from the line
		        //System.out.println(nextLine[0] + nextLine[1] + "etc...");}
		    	int headLen = headers.length;
	    		int nftCounter = 0;
		    	for( int i = 0 ; i < headLen ; i++ ){
		    		if (dataMap.containsKey(i)){
		    			reverVarMap.put(dataMap.get(i).get(0), nftCounter);
		    			nftCounter = nftCounter + 1;
		    			if (dataMap.get(i).get(1) == "CATEG"){
		    				catVarMapUpdater(dataMap.get(i).get(0),nextLine[i]);
		    			}
		    		}
		    		else{
		            	String freeTextField = nextLine[i];
		        		String [] splited = parseFreeText(freeTextField);
		            	for (String s: splited)
		         	        parsedText.add(s);
		     
		    		}	 
		    	}
		    }		    	
		dictKeeper.addToDict(parsedText);
		reader.close();
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally {
			reader = null;
			System.gc();
			
		}
		
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
	
	public ArrayList<Attribute> getAttributes(){	
		Collection<String> keySet = this.reverVarMap.keySet();
		System.out.println("\n\n\n\n\n\n DAVID LOOK HERE \n\n\n\n\n\n\n");
		System.out.println(keySet);
		ArrayList<Attribute> nonTextAtts = new ArrayList<Attribute>();
		Iterator it = keySet.iterator();
	    while (it.hasNext()) {
	        String tok = (String)it.next();
	        System.out.println("tok "+tok);
	        if (catVarCodeMap.containsKey(tok)){
//	        	System.out.println(catVarCodeMap.get(tok));
	        	int ents = catVarCodeMap.get(tok).size();
	        	List my_nominal_values = new ArrayList(ents); 
	        	double label = 0.0;
	        	for( int i = 0 ; i < ents ; i++ )
	            {
	        		String strVal = String.valueOf(label);
	        		my_nominal_values.add(strVal);
	        		label = label + 1.0;
	        		System.out.println(label);
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
	
	public double[] makeFeatVector(String [] csvLine){
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
	int i = 0;
	for (String s : csvLine){  
	//while (it.hasNext()) {
        if (dataMap.containsKey(i) == true){
        	ArrayList<String> ent = dataMap.get(i);
        	String varName = ent.get(0);
        	String varType = ent.get(1);
    			if (varType == "NUMER"){
    	        	String strdub = s;
    	        	double dub = Double.parseDouble(strdub);
    	        	vector[reverVarMap.get(varName)] = dub;
        	     }
    			if (varType == "CATEG"){
    				String strVal = s;
    				System.out.println("Printing strVal");
    				System.out.println(strVal);
    				double code = catVarCodeMap.get(varName).get(strVal);
    				vector[reverVarMap.get(varName)] = code;
    			}    	
        }
        else{
        	String freeTextField = s;
    		String [] splited = parseFreeText(freeTextField);
        	for (String g: splited)
     	        parsedText.add(g);
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
	
	private String[] concat(String[] A, String[] B) {
		   int aLen = A.length;
		   int bLen = B.length;
		   String[] C= new String[aLen+bLen];
		   System.arraycopy(A, 0, C, 0, aLen);
		   System.arraycopy(B, 0, C, aLen, bLen);
		   return C;
		}
	
	private void writeFeatVector(double[] featVec){
		//writes feature vector to a csv file. Should be used to output data.
	}
	
	public void saveDataMap(String path){
		try{
			FileOutputStream fileOut = new FileOutputStream(path+"datamap.ser");
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
	
	public void saveReverVarMap(String path){
		try{
			FileOutputStream fileOut = new FileOutputStream(path+"revervarmap.ser");
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
	
	public void saveCatVarCodeMap(String path){
		try{
			
			FileOutputStream fileOut = new FileOutputStream(path+"catvarcodemap.ser");
		    ObjectOutputStream out = new ObjectOutputStream(fileOut);
		    out.writeObject(this.catVarCodeMap);
		    out.close();
		    fileOut.close();
		    System.out.println("Serialized data is saved in "+path);
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
		 */
		
		String INPUT = "dummydata/iris.csv";
		
		DataBuilder db = new DataBuilder();
		//DUMMYTEST
		/*
		db.addToDataMap(0, "SEX", "CATEG");
		db.addToDataMap(1, "AGE", "NUMER");
		//db.addToDataMap(2, "UNCODED_DIAGNOSIS", "CATEG");
		db.addToDataMap(3, "ACTUAL_DIAGNOSIS", "CATEG");
		//db.addToDataMap("4", "FREETEXT", "CATEG");
		//db.addToDataMap("5", "MOREFREETEXT", "CATEG");
		db.addToDataMap(6, "BLOOD_PRESSURE", "NUMER");*/
		
		//IRISTEST
		/*
		db.addToDataMap(0, "Sepal.Length", "NUMER");
		db.addToDataMap(1, "Sepal.Width", "NUMER");
		db.addToDataMap(2, "Petal.Length", "NUMER");
		db.addToDataMap(3,"Petal.Width","NUMER");
		db.addToDataMap(4,"Species","CATEG");
		*/
		
		db.addToDataMap(0, "6542", "CATEG");
	
			
		
		//STEP TWO: POPULATE MAPS (1st pass through the data)		
		db.populateMaps(INPUT);
	
		//STEP THREE: WRITE DATA (2nd pass through the data)
		CSVReader reader;
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter("dummydata/TESTOUT.csv"), ',');
		    String[] headers = db.getHeaders(); 
			writer.writeNext(headers);
			reader = new CSVReader(new FileReader(INPUT));
			String [] nextLine;
			reader.readNext();
			while ((nextLine = reader.readNext()) != null) {
				double[] toWrite = db.makeFeatVector(nextLine);
				String[] s = new String[toWrite.length];
				for (int i = 0; i < s.length; i++)
				    s[i] = String.valueOf(toWrite[i]);
				writer.writeNext(s);
			}
			writer.close();
			reader.close();	
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		
			
		/**STEP FOUR: SERIALIZE CLASS MEMBERS
		*
		Three different files to be output
		 */
		
		db.saveCatVarCodeMap("");
		db.saveDataMap("");
		db.saveReverVarMap("");
		db.dictKeeper.saveDict("");
	
	

 

	}
}
