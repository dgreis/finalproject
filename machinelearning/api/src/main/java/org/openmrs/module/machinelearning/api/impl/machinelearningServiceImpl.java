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
package org.openmrs.module.machinelearning.api.impl;

import java.math.BigInteger;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.machinelearning.api.machinelearningService;
import org.openmrs.module.machinelearning.api.db.machinelearningDAO;



/**
 * It is a default implementation of {@link machinelearningService}.
 */
public class machinelearningServiceImpl extends BaseOpenmrsService implements machinelearningService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private machinelearningDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(machinelearningDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public machinelearningDAO getDao() {
	    return dao;
    }

	

	@Override
	public BigInteger getobscount() {
		// TODO Auto-generated method stub
		return dao.getobscount();
	}

	@Override
	public  List<Object[]> getpatienscustom(int batchsize, int i) 
	{
		// TODO Auto-generated method stub
		return dao.getpatienscustom(batchsize,i);
		
	//	return null;
	}
	
	public List<Object[]> getencounters()
	{
		return dao.getencounters();
	}

	@Override
	public List<Object[]> getflatobs(List<Integer> encounterset){
		// TODO Auto-generated method stub
		return dao.getflatobs(encounterset);
	}
	
}