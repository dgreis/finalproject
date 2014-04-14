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
		
		String path = this.getClass().getClassLoader().getResource("").getPath();
		String fullqualpath = path.split("target")[0];
		
		String INPUT = fullqualpath+"flatfile.csv";
		DataBuilder db = new DataBuilder();
		
		
		//DUMMYTEST
		/*
		db.addToDataMap(0, "SEX", "CATEG");
		db.addToDataMap(1, "AGE", "NUMER");
		//db.addToDataMap(2, "UNCODED_DIAGNOSIS", "CATEG");
		db.addToDataMap(3, "ACTUAL_DIAGNOSIS", "CATEG");
		//db.addToDataMap("4", "FREETEXT", "CATEG");
		//db.addToDataMap("5", "MOREFREETEXT", "CATEG");
		db.addToDataMap(6, "BLOOD_PRESSURE", "NUMER");*/
		
		//IRISTEST
		/*
		db.addToDataMap(0, "Sepal.Length", "NUMER");
		db.addToDataMap(1, "Sepal.Width", "NUMER");
		db.addToDataMap(2, "Petal.Length", "NUMER");
		db.addToDataMap(3,"Petal.Width","NUMER");
		db.addToDataMap(4,"Species","CATEG");
		*/
		
		//db.addToDataMap(0,"encounter_id","CATEG");
		//db.addToDataMap(0,"person_id","CATEG");
		db.addToDataMap(2,"6670","FREE_TEXT");
		db.addToDataMap(3,"6543","FREE_TEXT");
		db.addToDataMap(4,"6542","CATEG");
		db.addToDataMap(5,"6669","CATEG");
			
		//STEP TWO: POPULATE MAPS (1st pass through the data)		
		db.populateMaps(INPUT);
	
		//STEP THREE: WRITE DATA (2nd pass through the data)
		CSVReader reader;
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter("structure_TESTOUT.csv"), ',', CSVWriter.NO_QUOTE_CHARACTER);
		    String[] headers = db.getHeaders(); 
			writer.writeNext(headers);
			reader = new CSVReader(new FileReader(INPUT));
			String [] nextLine;
			reader.readNext();
			while ((nextLine = reader.readNext()) != null) {
				double[] toWrite = db.makeFeatVector(nextLine);
				String[] s = new String[toWrite.length];
				for (int i = 0; i < s.length; i++)
				    s[i] = String.valueOf(toWrite[i]);
				writer.writeNext(s);
			}
			writer.close();
			reader.close();	
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		
			
		/**STEP FOUR: SERIALIZE CLASS MEMBERS
		*
		Three different files to be output
		 */
		db.saveCatVarCodeMap(path);
		db.saveDataMap(path);
		db.saveReverVarMap(path);
		db.dictKeeper.saveDict(path);
	
	
		
		
		model.addAttribute("user", Context.getAuthenticatedUser());
	}
}
