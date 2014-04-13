package sqlninja;

import java.util.*;

public class Encounter {

	private int id;
	private int personid;
	private ArrayList<String[]> elines;
	private ConceptKeeper ck;
	private HashMap<Integer,HashSet<Integer>> conceptValueMap;
	private HashMap<Integer,ArrayList<Integer>> conceptIndexMap;
	
	
	public Encounter(ArrayList<String[]> encounter_group, ConceptKeeper ck_in){
		this.elines = encounter_group;
		this.id = Integer.parseInt(elines.get(0)[3]);
		this.personid = Integer.parseInt(elines.get(0)[1]);
		this.ck = ck_in;
		this.conceptValueMap = new HashMap<Integer,HashSet<Integer>>();
		this.conceptIndexMap = new HashMap<Integer,ArrayList<Integer>>();
		populateMaps();
	}
	
	public int getEncounter_ID(){
		return this.id;
	}
	
	public int getPerson_ID(){
		return this.personid;
	}
	
	public String[] flattenEncounter() {
		int arraySize = ck.conceptOrderMap.size();
		String[] returnVector = new String[arraySize];
		for (int k : ck.conceptOrderMap.keySet()){
			if (conceptValueMap.containsKey(k)){
				int col_val = conceptValueMap.get(k).toArray(new Integer[1])[0];			
				if (col_val == 15){
					String s = aggregate(k,new mean());
					returnVector[ck.conceptOrderMap.get(k)] = s;
				}
				if (col_val == 17){
					String s = aggregate(k,new append());
					returnVector[ck.conceptOrderMap.get(k)] = s;
				}
			}
		}
		return returnVector;
	}
	
	private void populateMaps(){
		scanObs(elines);
		//validateValueFields();
	}
	
	private void scanObs(ArrayList<String[]> obs){
		int i = 0;
		for (String[] o : obs){
			int concept_id = Integer.parseInt(o[2]);
			updateValueFields(concept_id,o);
			if (conceptIndexMap.containsKey(concept_id)){
				conceptIndexMap.get(concept_id).add(i);
			}
			else{
				conceptIndexMap.put(concept_id, new ArrayList<Integer>());
				conceptIndexMap.get(concept_id).add(i);
			}
			i++;			
		}
	}
	
	private void updateValueFields(int id, String[] ob){
		for(int i=10; i<18; i++){
			if (conceptValueMap.containsKey(id)){
				if ( ! (ob[i].equals("NULL")) ){
					conceptValueMap.get(id).add(i);
				}
				else{
					continue;
				}
			}
			else{
				conceptValueMap.put(id, new HashSet<Integer>());
				if ( ! (ob[i].equals("NULL")) ){
					conceptValueMap.get(id).add(i);
				}
			}
		}
	}
/*	
	private void validateValueFields(){
	    for (int k : conceptValueMap.keySet()){
	    	int size = conceptValueMap.get(k).size();
	    	if (size > 1){
	    		System.out.println("Problem:: Encounter: "+id+" Concept_ID: "+k+" Concept Name: "+ ck.conceptNameMap.get(k));
	    		System.exit(0);
	    	}
	    }
	}
*/	
	
	private String aggregate(Integer concept_id, Command command){
		ArrayList<Integer> indices = conceptIndexMap.get(concept_id);
		String result = callCommand(command, indices); 		
		return result;
	}
	
	
	public interface Command 
    {
        public String execute(ArrayList<Integer> indices);
    }

    public class mean implements Command
    {
        public String execute(ArrayList<Integer> indices) 
        {
           double sum = 0.0;
           double len = indices.size();
           for (int d : indices){
        	   sum = sum + Double.parseDouble(elines.get(d)[15]);        	   
           }
           double avg = sum/len;
           return Double.toString(avg);
        }    
    }
    
    public class append implements Command
    {
    	public String execute(ArrayList<Integer> indices)
    	{
    	ArrayList<String> stringList = new ArrayList<String>();
    	for (int d : indices){
    		stringList.add(elines.get(d)[17]);
    	}
    	StringBuilder builder = new StringBuilder();
    	for(String s : stringList) {
    	    builder.append(s);
    	    builder.append(" ");
    	}
    	return builder.toString();	
    	}
    }

    public static String callCommand(Command command, ArrayList<Integer> indices) 
    {
        return command.execute(indices);

    }
    

	
	
}
