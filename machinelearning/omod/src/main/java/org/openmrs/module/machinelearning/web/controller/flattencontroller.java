

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
import org.openmrs.module.machinelearning.mlcode.ConceptKeeper;
import org.openmrs.module.machinelearning.mlcode.ObsScanner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.openmrs.module.machinelearning.mlcode.ObsScanner;
import org.openmrs.module.machinelearning.mlcode.ConceptKeeper;
import org.openmrs.module.machinelearning.mlcode.Encounter;


/**
 * The main controller.
 */
@Controller
public class  flattencontroller {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/machinelearning/flatten", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		
		System.out.println("----------current location---------123142141213");
		String path = this.getClass().getClassLoader().getResource("").getPath();
		String fullqualpath = path.split("target")[0];
		
		
		
		ConceptKeeper ck = new ConceptKeeper();
		ObsScanner ob = new ObsScanner();
		
		ob.flattenObsTable(ck,fullqualpath+"obs_patients.csv","flatfile.csv");
		
		model.addAttribute("user", Context.getAuthenticatedUser());
	}
}
