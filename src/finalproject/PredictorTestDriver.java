package finalproject;

import java.util.*;
import weka.core.*;

import org.json.*;

public class PredictorTestDriver {
	
	public static void main(String[] args){
		
		Predictor p = new Predictor("unused arg");
		JSONObject obj = new JSONObject();
		try {
			obj.put("SEX", "FEMALE");
			obj.put("FREE TEXT","HI HI HI HI");
			obj.put("BLOOD_PRESSURE","73.43");
			obj.put("MOREFREETEXT","free free free free");
			obj.put("ACTUAL_DIAGNOSIS","FINE");
			obj.put("UNCODED_DIAGNOSIS", "I'm not sure.");
			obj.put("AGE", "23");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double[] dlist = p.makeFeatVector(obj);
        for (double d : dlist) {
            System.out.println(d);
        }
        
        System.out.println("Now test whether instance is made properly");
        DataBuilder db = p.getdb();
        ArrayList<Attribute> atts = db.getAttributes();
        Instance i = p.makeInstance(atts, dlist);
        System.out.println(i);
        
		
		
	}
}
