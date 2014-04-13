package org.openmrs.module.machinelearning.mlcode;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class ObsScanner {

	//private int num_encounters;
	private ArrayList<String[]> flusher;
	
	public ObsScanner(){
		this.flusher = new ArrayList<String[]>();
	}
	
	
	private void flush(){
		this.flusher = new ArrayList<String[]>();
	}

	
	public void flattenObsTable (ConceptKeeper ck, String INFILE, String OUTFILE) {			
		CSVReader reader;
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter(OUTFILE), ',',CSVWriter.NO_QUOTE_CHARACTER);
		    String[] conceptcols = ck.conceptMap.keySet().toArray(new String[ck.conceptMap.size()]);
		    String[] firstcols = new String[]{"encounter_id","person_id"};
		    String[] headers = concat(firstcols,conceptcols);		    
			writer.writeNext(headers);
			reader = new CSVReader(new FileReader(INFILE));
			String [] nextLine;
			reader.readNext();
			boolean moreEncounters = true;
			//while ((nextLine = reader.readNext()) != null) {
			while (moreEncounters == true){
				ArrayList<String[]> encounter_group = getNextEncGroup(reader);
				if (encounter_group == null){
					moreEncounters = false;
				}
				else{
					Encounter e = new Encounter(encounter_group, ck);
					String[] firstvals = new String[]{Integer.toString(e.getEncounter_ID()),Integer.toString(e.getPerson_ID())};
					String[] conceptvals = e.flattenEncounter();
					String[] toWrite = concat(firstvals,conceptvals);
					writer.writeNext(toWrite);
				}
			}
			reader.close();
			writer.close();		}
		catch (IOException e1) {
			e1.printStackTrace();
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

			
	
	public ArrayList<String[]> getNextEncGroup (CSVReader r) {
			boolean groupFull = false;
			ArrayList<String[]> encounter_group = new ArrayList<String[]>();			
			//String[] nextLine;
			try {
				if (flusher.size() > 0)
				{
					encounter_group.addAll(flusher);
					flush();
				}
				else{
					String [] nextLine = r.readNext();
					
					
			//		System.out.println("david"+Arrays.asList(nextLine));
					
					if (nextLine == null){
						groupFull = true;
					}
					else{
						while (nextLine[3] == "" || nextLine[3].equals("NULL"))
							
						{
							System.out.println("barf"+nextLine[3]);
							nextLine = r.readNext();
						}
						if (encounter_group.size() == 0 ){
							encounter_group.add(nextLine);
						}
					}
				}
				//while (groupFull == false){
				//System.out.println("groupfull never true");									
				//int curr_enc_id = Integer.parseInt(nextLine[3]);
				while (groupFull == false){
					int curr_enc_id = Integer.parseInt(encounter_group.get(0)[3]);
					String [] lineAfter;
					lineAfter = r.readNext();
					if (lineAfter != null){	
						String encID = lineAfter[3];
						if (encID.equals("NULL")==false){
							int next_enc_id = Integer.parseInt(encID);
							if (curr_enc_id == next_enc_id){		
								encounter_group.add(lineAfter);
							}								
							else{
								flusher.add(lineAfter);
								groupFull = true;
							}
						}
						else{
							groupFull = true;
						}
					}
					else{
						groupFull = true;
					}						
				}	
			}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.print("inf");
		if (encounter_group.isEmpty()==true){
			return null;
		}
		else{
			return encounter_group;
		}
	}

	public void getPath(){
		System.out.println(this.getClass().getClassLoader().getResource("").getPath());
	}
	 	
	
	public static void main(String[] args){
		ConceptKeeper ck = new ConceptKeeper();
		ObsScanner ob = new ObsScanner();		
		ob.flattenObsTable(ck,"/Users/dgreis/Documents/School/Classes/FP_Aux/testdatapih/obs_SMALL.csv","/Users/dgreis/Documents/School/Classes/FP_Aux/testdatapih/flattened.csv");
	}

}

