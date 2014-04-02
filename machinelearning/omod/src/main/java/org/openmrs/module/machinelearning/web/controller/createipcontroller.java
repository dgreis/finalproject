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

import java.io.FileWriter;
import java.io.IOException;


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
protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/machinelearning/createip", method = RequestMethod.GET)
	public void createip(ModelMap model) 
	{
		
		model.put("me", Context.getAuthenticatedUser());
		
		// obs output
		List<Obs> someObsList = Context.getObsService().getObservations(null, null, null, null, null, null, null, null, null, null, null, true);;
		createfileobs(someObsList);
  			
		// patients output
		
		List<Patient> patients = Context.getPatientService().getAllPatients();
		createfilepatients(patients);
		
		// creating concepts
		List<Concept> concepts = Context.getConceptService().getAllConcepts();
		createfileconcepts(concepts);
		
		
		
		model.addAttribute("favorites",patients);
		
		
		// pass control to createip.jsp file passing the two values
		
        
 		//model.addAttribute("obs",someObsList);

 

	}

	

}
