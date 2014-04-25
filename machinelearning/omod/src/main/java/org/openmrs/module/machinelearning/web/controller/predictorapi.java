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


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// important - controller does not need to go to view, return response from controller
import org.springframework.web.bind.annotation.ResponseBody;
import org.openmrs.Concept;
import org.openmrs.Patient;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.File;







import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;

import org.json.*;
import org.openmrs.module.machinelearning.*;
import org.openmrs.module.machinelearning.mlcode.*;

import java.util.HashMap;

import org.openmrs.module.machinelearning.api.machinelearningService;



/**
 * The main controller.
 * responsebody directive - prevents control from going to jsp view , returns data instead
 */


@Controller
public class  predictorapi 

{
	private Object extractargs(String params)
	{
		
		HashMap<String,String> h = new HashMap<String,String>();
		
		System.out.println("params"+params);
		String query = params.split("\\?")[1];
		System.out.println("split"+query);
		
		
		String[] values = query.split("\\&");
		System.out.println("values"+values);
		
		
		String[] keyval;
		
		for(String x:values)
		{
			keyval = x.split("=");
			if(keyval.equals("patientId")){
				h.put(keyval[0], keyval[1]);	
			}
			
		}	
			
		
		return h;
	}
	
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/machinelearning/predictorapi", method = RequestMethod.GET)
	@ResponseBody

	public Object predictorapi(HttpServletRequest request, HttpSession httpSession,ModelMap model) 
	{
		
		
		
		List<String> xyz = new ArrayList<String>();
		
		// get parameteres
		String param = request.getParameter("class");	
		String url = request.getParameter("url");	
		
		HashMap<String,String> keyval = (HashMap) extractargs(url);
		String temp;
		
		for(String key:keyval.keySet())
		{
			temp = keyval.get(key);
			xyz.add(key+"and"+temp);
			System.out.println(key+"and"+temp);
		}
		
		File f = new File(this.getClass().getClassLoader().getResource("").getPath()+"/cls.ser");
		int obsolete = 0;
		//long predictorAge = new Date().getTime() - f.lastModified();
		//if ( predictorAge <= 2)
		//	obsolete = 1;

		double predictorAgeMS = (new Date().getTime() - f.lastModified());
		double predictorAge = predictorAgeMS/(1000*60*60*24);
		if ( predictorAge >= 0.006) 		//flips on 'obsolete' switch if classifier is more than 10
			obsolete = 1;					// minutes old.
		
		
		Predictor p = new Predictor(this.getClass().getClassLoader().getResource("").getPath());
		JSONObject obj = new JSONObject();
		
		try 
		{
			
			
			obj.put("6542","6704");
			obj.put("6543_voided",param);
						
			
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//double[] dlist = p.makeFeatVector(obj);

		List<String> dlist = new ArrayList<String>();
		// call predict function
		
		String output = p.predict(obj);

		Concept concept = Context.getConceptService().getConcept(Integer.parseInt(output));
		dlist.add(concept.getName().getName());
		
		dlist.add(output);
		dlist.add(Integer.toString(obsolete));
		System.out.println(output);
		
	//	String outputstr = Double.toString(output);
		
		
		// add prediction to 
		//xyz.add(outputstr);
			
		List<String> patients = new ArrayList<String>();
		
		NaiveBayes cModel = new NaiveBayes();
		String info = cModel.globalInfo();
		
		patients.add("p1");
		patients.add("p2");
		patients.add("str");
		patients.add(info);

		
		//return patients;
		/* 
		for(double d:dlist){
			xyz.add(Double.toString(d));
		}*/
        
        return dlist;	
	}
}


