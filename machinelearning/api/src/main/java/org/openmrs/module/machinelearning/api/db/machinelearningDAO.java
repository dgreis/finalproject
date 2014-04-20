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
package org.openmrs.module.machinelearning.api.db;

import org.openmrs.module.machinelearning.api.machinelearningService;

import java.math.BigInteger;
import java.util.List;

import org.openmrs.Patient;

/**
 *  Database methods for {@link machinelearningService}.
 */
public interface machinelearningDAO {
	
	/*
	 * Add DAO methods here
	 */
	
	/*
	 * returns list of patients in a batch - rohan
	 */
	List<Object[]> getpatienscustom(int batchsize, int i);
	
	BigInteger getobscount();
	
	BigInteger getencountercount();
	List<Object[]> getencounters();
	
	List<Object[]> getflatobs(List<Integer> encounterset);
	
	
}