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
package org.kuali.rice.core.framework.persistence.jpa.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;


/**
 *  This class converts the "A" or "I" value from the database into a true or false in Java. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class HibernateKualiCharBooleanAIType extends HibernateImmutableValueUserType implements UserType
{
	/**
	 * Retrieves a value from the given ResultSet
	 * 
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		
		String value = (String) StandardBasicTypes.STRING.nullSafeGet(rs, names[0]);
		Boolean converted = null;
		if (value != null) {
			try {
				if(value.equalsIgnoreCase("A"))
					converted = Boolean.TRUE;
				if(value.equalsIgnoreCase("I"))
					converted = Boolean.FALSE;
			}
			catch (Exception e) {
				throw new RuntimeException("Unable to get status value from db: " + e.getMessage());
			}

			if (converted == null) {
				throw new RuntimeException("Unable to get status value from db: ");
			}
		}
		return converted;
	}

	/**
	 * 
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		
		String converted = null;

		if (value != null) {
			try {
				converted = ((Boolean)value).booleanValue()? "A":"I";
			}
			catch (Exception e) {
				throw new RuntimeException("Unable to set status value to db: " + e.getMessage());
			}
		}

		//Hibernate.STRING.nullSafeSet(st, converted, index);
		if (converted == null) {
			st.setNull(index, Types.VARCHAR); 
		} else {
			st.setString(index, converted);
		}
	}

	/**
	 * Returns String.class
	 * 
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	public Class returnedClass() {
		return String.class;
	}

	/**
	 * Returns an array with the SQL VARCHAR type as the single member
	 * 
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	public int[] sqlTypes() {
		return new int[] { Types.VARCHAR };
	}
}

