package finalproject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
			writer = new CSVWriter(new FileWriter(OUTFILE), ',');
		    String[] conceptcols = ck.conceptNameMap.values().toArray(new String[ck.conceptNameMap.size()]);
		    String[] firstcols = new String[]{"encounter_id","person_id"};
		    String[] headers = concat(firstcols,conceptcols);		    
			writer.writeNext(headers);
			reader = new CSVReader(new FileReader(INFILE));
			String [] nextLine;
			//reader.readNext();
			while ((nextLine = reader.readNext()) != null) {
				ArrayList<String[]> encounter_group = getNextEncGroup(reader);
				Encounter e = new Encounter(encounter_group, ck);
				String[] firstvals = new String[]{Integer.toString(e.getEncounter_ID()),Integer.toString(e.getPerson_ID())};
				String[] conceptvals = e.flattenEncounter();
				String[] toWrite = concat(firstvals,conceptvals);
				writer.writeNext(toWrite);
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
			if (flusher.size() > 0){
				encounter_group.addAll(flusher);
				flush();
			}
			String [] nextLine;
			try {
				nextLine = r.readNext();
				encounter_group.add(nextLine);
				int curr_enc_id = Integer.parseInt(nextLine[3]);	
				while (groupFull == false){			
					String [] lineAfter;
					lineAfter = r.readNext();
					if (lineAfter != null){
						int next_enc_id = Integer.parseInt(lineAfter[3]);
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	return encounter_group;
	}

	public void getPath(){
		System.out.println(this.getClass().getClassLoader().getResource("").getPath());
	}
	 	
	
	public static void main(String[] args){
		ConceptKeeper ck = new ConceptKeeper("/Users/dgreis/Documents/School/Classes/FP_Aux/concept_test.csv");
		ObsScanner ob = new ObsScanner();		
		ob.flattenObsTable(ck,"/Users/dgreis/Documents/School/Classes/FP_Aux/obs_test.csv","/Users/dgreis/Documents/School/Classes/FP_Aux/testout.csv");
	}

}

