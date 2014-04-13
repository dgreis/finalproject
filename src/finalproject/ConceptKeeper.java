package sqlninja;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

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
	
	private void populateMaps(String targetdir){
		CSVReader reader;
		try {
			merge(targetdir+"/concept.csv",targetdir+"/concept_name.csv","concept_id","concept_id",targetdir+"/TESTOUT.csv");
			reader = new CSVReader(new FileReader(targetdir + "/TESTOUT.csv"));
			String [] nextLine;
			reader.readNext();
			while ((nextLine = reader.readNext()) != null) {
				if (classMap.containsKey(nextLine[6])){
					int concept_id = Integer.parseInt(nextLine[0]);
					conceptNameMap.put(concept_id,nextLine[18]);
					int dictLength = conceptOrderMap.size();
					conceptOrderMap.put(concept_id,dictLength);
				}					
			}
			reader.close();	
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}
	
	private void merge(String file1,String file2,String jc1,String jc2,String outName)
	{
		try
		{

					
			CSVReader reader1 = new CSVReader(new FileReader(file1));
			String[] columnheaders1 = reader1.readNext();
			List s1 = Arrays.asList(columnheaders1);
			int position1 = s1.indexOf(jc1);
				
			
			CSVReader reader2 = new CSVReader(new FileReader(file2));
			String[] columnheaders2 = reader2.readNext();
			List s2 = Arrays.asList(columnheaders2);
			int position2 = s2.indexOf(jc2);
			
			CSVWriter writer;
			writer = new CSVWriter(new FileWriter(outName), ',',CSVWriter.NO_QUOTE_CHARACTER);
			String[] headers = concat(columnheaders1,columnheaders2);
			writer.writeNext(headers);
			
			String[] nextLine1;
			String[] nextLine2;
			
			String key1,key2;
			List l1,l2;
			
			
			HashMap<String,String[]> map = new HashMap<String,String[]>();
		//	print("first key");
			
			// make file 2 persist
			while ((nextLine2 = reader2.readNext()) != null)
			{
				
			//	print(nextLine2[position2]);
				map.put(nextLine2[position2], nextLine2);
				
			}
			
			
	//		print(map.toString());
			//print("second key");
			while ((nextLine1 = reader1.readNext()) != null) 
			{
				
				key1 = nextLine1[position1];
				
			//	print(key1);
				
				if(map.containsKey(key1))
				{
					//print("match");
					String[] b = map.get(key1);
					String[] a = nextLine1;
										
					String[] c = concat(a,b);
					writer.writeNext(c);
					//print(Arrays.toString(map.get(key1))+Arrays.toString(nextLine1));
				}
						
				}
			writer.close();
			reader1.close();
			reader2.close();
				
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}	
	}
	
	private String[] concat(String[] A, String[] B) {
		   int aLen = A.length;
		   int bLen = B.length;
		   String[] C= new String[aLen+bLen];
		   System.arraycopy(A, 0, C, 0, aLen);
		   System.arraycopy(B, 0, C, aLen, bLen);
		   return C;
		}
	
	private static void print(Object s)
	{
		System.out.println(s);
	}
	
	public static void main(String[] args){
		//ConceptKeeper ck = new ConceptKeeper("");
		//ck.merge("/Users/dgreis/Documents/School/Classes/FP_Aux/testdatapih/concept.csv", "/Users/dgreis/Documents/School/Classes/FP_Aux/testdatapih/concept_name.csv", "concept_id", "concept_id");
		//System.out.println("hi");
	}

}
