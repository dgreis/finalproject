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


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.machinelearning.mlcode.DataBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


/**
 * The main controller.
 */
@Controller
public class  structuredata {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/machinelearning/structuredata", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		/*
		 **STEP ONE: INITIALIZE dataMaps
		 */
		DataBuilder db;
		CSVReader reader;
		CSVWriter writer;
		
		try {		
			String path = this.getClass().getClassLoader().getResource("").getPath();
			String fullqualpath = path.split("target")[0];
			
			String INPUT = fullqualpath+"obs_patients.csv";
			db = new DataBuilder();
			
	
			db.addToDataMap(0,"6542","CATEG");
			//db.addToDataMap(3,"6669","CATEG");
				
			//STEP TWO: POPULATE MAPS (1st pass through the data)		
			db.populateMaps(INPUT);
		
			//STEP THREE: WRITE DATA (2nd pass through the data)
			

			writer = new CSVWriter(new FileWriter("structure_TESTOUT.csv"), ',', CSVWriter.NO_QUOTE_CHARACTER);
		    String[] headers = db.getHeaders(); 
			writer.writeNext(headers);
			reader = new CSVReader(new FileReader(INPUT));
			String [] nextLine;
			reader.readNext();
			while ((nextLine = reader.readNext()) != null) 
			{
				double[] toWrite = db.makeFeatVector(nextLine);
				String[] s = new String[toWrite.length];
				for (int i = 0; i < s.length; i++)
				    s[i] = String.valueOf(toWrite[i]);
				writer.writeNext(s);
			}
			writer.close();
			reader.close();	
		
			


		
			
		/**STEP FOUR: SERIALIZE CLASS MEMBERS
		*
		Three different files to be output
		 */
		db.saveCatVarCodeMap(path);
		db.saveDataMap(path);
		db.saveReverVarMap(path);
		db.dictKeeper.saveDict(path);
	
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}

		finally 
		{
			// nullify all objects that are created
			db = null;
			reader = null;
			writer = null;
			// garbage collection
			System.gc();
			
		}
		
		model.addAttribute("user", Context.getAuthenticatedUser());
	}
}
