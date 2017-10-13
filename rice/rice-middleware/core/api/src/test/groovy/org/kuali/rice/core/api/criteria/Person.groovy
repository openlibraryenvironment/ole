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
package org.kuali.rice.core.api.criteria;


import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAnyElement
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.apache.commons.lang.builder.ToStringBuilder
import org.w3c.dom.Element

/**
 * A simple class used in Criteria testing. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = "person")
@XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.NONE)
@XmlType(name = "PersonType", propOrder = [
        "name",
        "displayName",
        "birthDate",
        "_futureElements"
])
class Person {

	@XmlElement(name = "name", required = true)
	private final Name name;
	
	@XmlElement(name = "displayName", required = false)
	private final String displayName;
	
	@XmlElement(name = "birthDate", required = false)
	private final Date birthDate;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    @SuppressWarnings("unused")
    private Person() {
    	this.name = null;
    	this.displayName = null;
    	this.birthDate = null;
    }
    
	Person(Name name, String displayName, Date birthDate) {
		this.name = name;
		this.displayName = displayName;
		this.birthDate = birthDate;
	}
	
	Name getName() {
		return this.name;
	}

	String getDisplayName() {
		return this.displayName;
	}

	Date getBirthDate() {
		return this.birthDate;
	}
	
	@Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, [ "_futureElements" ]);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(obj, this, [ "_futureElements" ]);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
	
	@XmlRootElement(name = "name")
	@XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.NONE)
	@XmlType(name = "NameType", propOrder = [
	        "first",
	        "last",
	        "_futureElements"
	])
	static final class Name {
		
		@XmlElement(name = "first", required = true)
		private final String first;
		
		@XmlElement(name = "last", required = true)
		private final String last;
		
		@SuppressWarnings("unused")
	    @XmlAnyElement
	    private final Collection<Element> _futureElements = null;
		
		@SuppressWarnings("unused")
		private Name() {
			this.first = null;
			this.last = null;
		}
		
		Name(String first, String last) {
			this.first = first;
			this.last = last;
		}

		public String getFirst() {
			return this.first;
		}

		public String getLast() {
			return this.last;
		}
		
		@Override
	    public int hashCode() {
	        return HashCodeBuilder.reflectionHashCode(this, [ "_futureElements" ]);
	    }

	    @Override
	    public boolean equals(Object obj) {
	        return EqualsBuilder.reflectionEquals(obj, this, [ "_futureElements" ]);
	    }

	    @Override
	    public String toString() {
	        return ToStringBuilder.reflectionToString(this);
	    }
		
	}
	
}
