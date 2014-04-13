/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.machinelearning.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.openmrs.Patient;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.Concept;
import org.openmrs.Encounter;

import java.util.List;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


// custom built service for the module - rohan





import org.openmrs.module.machinelearning.api.machinelearningService;


/**
 * The main controller.
 */
@Controller


public class  createipcontroller {
	
	
private void createfilepatients(List<Patient> patients) 
	{
		try
		{
		    FileWriter writer = new FileWriter("patients.csv");
		    
		    writer.append("person_id");
		    writer.append(',');
		    writer.append("birthdate");
		    writer.append(',');
		    writer.append("gender");
		    writer.append('\n');

	 		for (Patient p : patients) 
	 		{	
	 		
	 		writer.append(Integer.toString(p.getPersonId()));
	 		writer.append(",");
	 		writer.append(Integer.toString(p.getAge()));
	 		writer.append(",");
	 		writer.append(p.getGender());
	 
	        writer.append('\n');	
	        
			}

		    //generate whatever data you want
	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	 
		
	}

private void createfileobs(List<Obs> someObsList)


	{
	try
	{
	    FileWriter writer = new FileWriter("obs.csv");
	    
 		writer.append("obs_id");
	    writer.append(',');
	    writer.append("person_id");
	    writer.append(',');
	    writer.append("concept_id");
	    writer.append(',');
	    writer.append("comments");
	    writer.append('\n');

 		for (Obs obs : someObsList) 
 		{	
 		
 		writer.append(Integer.toString(obs.getObsId()));
 		writer.append(",");
 		writer.append(Integer.toString(obs.getPersonId()));
 		writer.append(",");
 		
 		Integer.toString(obs.getConcept().getConceptId());
 		writer.append(obs.getConcept().toString());
 		
 		writer.append(",");
 		writer.append(obs.getComment());
        writer.append('\n');	
        
		}

	    //generate whatever data you want
 
	    writer.flush();
	    writer.close();
	}
	catch(IOException e)
	{
	     e.printStackTrace();
	} 
 


	}
	
private void createfileconcepts(List<Concept> concepts) 
{
	try
	{
	    FileWriter writer = new FileWriter("concepts.csv");
	    
	    writer.append("concept_id");
	    writer.append(',');
	    writer.append("name");
	    writer.append(',');
	    writer.append("description");
	    writer.append('\n');

 		for (Concept c : concepts) 
 		{	
 		
 		writer.append(Integer.toString(c.getConceptId()));
 		writer.append(",");
 		
 		writer.append(c.getConceptClass().getName());
 		writer.append(",");
 		
 		if(c.getDescription() != null){
 			writer.append(c.getDescription().getDescription());	
 		}
 		
 		
        writer.append('\n');	
        
		}

	    //generate whatever data you want
 
	    writer.flush();
	    writer.close();
	}
	catch(IOException e)
	{
	     e.printStackTrace();
	} 
 


	
}

private void createfilenotes(){
	
}

private void printoutput(List<Object[]> pr,CSVWriter writer) throws IOException{
	String[] singlerow = new String[pr.get(0).length];
	
	int i; // will go from zero to column length
	for(Object[] p:pr)
	{
		i=0; //reset counter
		for(Object column:p)
		{
			if(column == null){
				singlerow[i] = "NULL";
			}
			else
			{
				singlerow[i] = column.toString();	
			}
			
			i++;
			System.out.print(column+"\t");
		}
		writer.writeNext(singlerow);
		System.out.println();
	}
	// flush output to disc
	writer.flush();
	
	
	System.out.println("batch process");
}


protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/machinelearning/createip", method = RequestMethod.GET)
	public void createip(ModelMap model) 
	{
	
		// dump obs table in batches of 50
		try {
			
			
			CSVWriter writer = new CSVWriter(new FileWriter("obs_patients.csv"), ',',CSVWriter.NO_QUOTE_CHARACTER);
			
			System.out.println("----------current location---------123142141213");
			System.out.println(this.getClass().getClassLoader().getResource("").getPath());
		
		
		
		BigInteger count = Context.getService(machinelearningService.class).getobscount();
		
		double cnt = count.doubleValue();
		
		long cntlong = (long) cnt;
		
		
		//int cnt = count.intValue();
		System.out.println("Count:"+count.toString());
		
		
		
		cnt = 1000L;
		
		int batchsize = 20;
		List<Object[]> batchoutput;
		
		int flag = 0;
		int currentcnt = 0;
		
		while(currentcnt < cnt)
		{
			if(flag==0)
			{
				batchoutput = Context.getService(machinelearningService.class).getpatienscustom(0,batchsize);
				flag = 1;
				currentcnt = 20;
			}
			else{
				currentcnt = currentcnt + 20;
				batchoutput = Context.getService(machinelearningService.class).getpatienscustom(currentcnt,batchsize);
				
			}
			printoutput(batchoutput,writer);
			
			
		}
		
		
		writer.close();
		
		
		System.out.println("custom service result");
		
		
		
		//sessionFactory.getCurrentSession()
		model.put("me", Context.getAuthenticatedUser());
				
		
		//List<Patient> patients = Context.getPatientService().getAllPatients();
		System.out.println("before query");
	
		
		// link to api : http://resources.openmrs.org/doc/org/openmrs/api/ObsService.html
		// obs output, find for these patients only
		/*
		List<Obs> someObsList = Context.getObsService().getObservations(people, null, null, null, null, null, null, null, null, null, null, true);
		createfileobs(someObsList);
  		*/
		
		/*
		// creating concepts
		List<Concept> concepts = Context.getConceptService().getAllConcepts();
		createfileconcepts(concepts);
		*/
		
		List<Patient> temp = new ArrayList<Patient>();
		
		
		model.addAttribute("favorites",temp);
		
		
		// pass control to createip.jsp file passing the two values
		
        
 		//model.addAttribute("obs",someObsList);

		}
		catch(Exception e){
			e.printStackTrace();
			
		}

	}

	

}
