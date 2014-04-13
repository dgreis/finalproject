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
package org.openmrs.module.machinelearning.api.db.hibernate;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.openmrs.Patient;
import org.openmrs.Person;

import java.util.ArrayList;

import org.openmrs.api.context.Context;
import org.openmrs.module.machinelearning.api.db.machinelearningDAO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.openmrs.api.PersonService;

/**
 * It is a default implementation of  {@link machinelearningDAO}.
 */
public class HibernatemachinelearningDAO implements machinelearningDAO {
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	/**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
	    this.sessionFactory = sessionFactory;
    }
    
	/**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
	    return sessionFactory;
    }

    // added custom method - rohan
	@Override
	public List<Object[]> getpatienscustom(int batchsize,int i) {
		// TODO Auto-generated method stub
		
		String query = "select * from obs,person where obs.person_id=person.person_id and encounter_id is not null order by encounter_id limit "+Integer.toString(batchsize)+","+Integer.toString(i)+";";
		
		List<Object[]> batch = sessionFactory.getCurrentSession().createSQLQuery(query).list(); 
		//List<Object> batch = new ArrayList<Person>();
		//PersonService ps = Context.getPersonService();
		return batch;
		//return null;
	}

	@Override
	public BigInteger getobscount() {
		List<BigInteger> countx = sessionFactory.getCurrentSession().createSQLQuery("select count(1) from obs;").list();
		return countx.get(0);
		
	}
}