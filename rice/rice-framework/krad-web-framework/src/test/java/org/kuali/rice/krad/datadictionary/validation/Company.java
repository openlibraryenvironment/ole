/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.datadictionary.validation;

import java.util.List;

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class Company {

	private String id;
	private String name;
	private List<Address> locations;

	private Employee mainContact;
	private Address mainAddress;
	private List<Employee> employees;
	private List<String> slogans;


    public Company(){
	}


	/**
	 * @return the id
	 */

	public String getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}



	public Company(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the addresses
	 */
	public List<Address> getLocations() {
		return this.locations;
	}
	/**
	 * @param addresses the addresses to set
	 */
	public void setLocations(List<Address> addresses) {
		this.locations = addresses;
	}

	/**
	 * @return the address
	 */
	public Address getMainAddress() {
		return this.mainAddress;
	}

	/**
	 * @param address the address to set
	 */
	public void setMainAddress(Address address) {
		this.mainAddress = address;
	}

	/**
	 * @return the employee
	 */
	public List<Employee> getEmployees() {
		return this.employees;
	}

	/**
	 * @param employees the employee to set
	 */
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}


	/**
	 * @return the mainContact
	 */
	public Employee getMainContact() {
		return this.mainContact;
	}


	/**
	 * @param mainContact the mainContact to set
	 */
	public void setMainContact(Employee mainContact) {
		this.mainContact = mainContact;
	}


    /**
     * @return the slogans
     */
    public List<String> getSlogans() {
        return this.slogans;
    }


    /**
     * @param slogans the slogans to set
     */
    public void setSlogans(List<String> slogans) {
        this.slogans = slogans;
    }

}
