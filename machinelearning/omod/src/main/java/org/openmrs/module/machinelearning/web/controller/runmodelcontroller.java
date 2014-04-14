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

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;



//import weka.classifiers.Classifier;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Instances;
import weka.classifiers.bayes.NaiveBayes;

import org.openmrs.module.machinelearning.mlcode.*;


/**
 * The main controller.
 */
@Controller
public class  runmodelcontroller {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/machinelearning/runmodel", method = RequestMethod.GET)
	public void runmodel(ModelMap model) 
	{	
		String path = this.getClass().getClassLoader().getResource("").getPath();
		String fullqualpath = path.split("target")[0];
		
		String modelDir = path;
		String flatFile =  fullqualpath+"structure_TESTOUT.csv";
		
		
		ModelTrainer m = new ModelTrainer();	
		Instances data = m.prepData(flatFile,modelDir);
		
		NaiveBayes nb = new NaiveBayes();
		try {
			nb.buildClassifier(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			weka.core.SerializationHelper.write(modelDir + "/cls.ser", nb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		model.addAttribute("user", Context.getAuthenticatedUser());
		
		
	}
}
