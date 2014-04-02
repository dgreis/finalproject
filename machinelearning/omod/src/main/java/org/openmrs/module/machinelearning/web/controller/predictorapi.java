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

// important - controller does not need to go to view, return response from controller
import org.springframework.web.bind.annotation.ResponseBody;


import org.openmrs.Patient;
import java.util.List;
import java.util.ArrayList;



import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import org.json.*;

import org.openmrs.module.machinelearning.*;

import org.openmrs.module.machinelearning.mlcode.*;




/**
 * The main controller.
 * responsebody directive - prevents control from going to jsp view , returns data instead
 */


@Controller
public class  predictorapi 

{
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/machinelearning/predictorapi", method = RequestMethod.GET)
	@ResponseBody

	public Object predictorapi(ModelMap model) 
	{
	//	System.out.println("--------");
	//	System.out.println();
		
		// String workingDir = System.getProperty("user.dir");
		// System.out.println("Current working directory : " + workingDir);
		 
		Predictor p = new Predictor(this.getClass().getClassLoader().getResource("").getPath());
		
		JSONObject obj = new JSONObject();
		
		try {
			
			obj.put("Petal.Length", "2");
			obj.put("Sepal.Length", "3");
			obj.put("Petal.Width", "5");
			obj.put("Sepal.Width", "7.5");
			
			
			obj.put("Species","Iris-virginica");
			
			
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double[] dlist = p.makeFeatVector(obj);
		
        for (double d : dlist) {
            System.out.println(d);
        }
		
		List<String> patients = new ArrayList<String>();
		
		NaiveBayes cModel = new NaiveBayes();
		String info = cModel.globalInfo();
		
		
		
		patients.add("p1");
		patients.add("p2");
		patients.add("str");
		patients.add(info);

		//return patients;
        
        return dlist;
		

	}
}


