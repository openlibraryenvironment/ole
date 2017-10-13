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

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.kuali.rice.core.api.CoreApiServiceLocator;

import java.security.GeneralSecurityException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Hibernate UserType to encrypt and decript data on its way to the database 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiDecimalIntegerPercentFieldType extends HibernateImmutableValueUserType implements UserType {
	/**
	 * Retrieves a value from the given ResultSet and decrypts it
	 * 
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		String value = rs.getString(names[0]);
		String converted = null;

		if (value != null) {
	        try {
                if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
	                converted = CoreApiServiceLocator.getEncryptionService().decrypt(value);
                }
	        }
	        catch (GeneralSecurityException gse) {
	            throw new RuntimeException("Unable to decrypt value from db: " + gse.getMessage());
	        }
	        
	        if (converted == null) {
				converted = value;
			}
		}

        return converted;
	}

	/**
	 * Encrypts the value if possible and then sets that on the PreparedStatement
	 * 
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		String converted = null;

		if (value != null) {
	        try {
                if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
	                converted = CoreApiServiceLocator.getEncryptionService().encrypt(value);
                }
	        }
	        catch (GeneralSecurityException gse) {
	            throw new RuntimeException("Unable to encrypt value to db: " + gse.getMessage());
	        }
		}
        
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
