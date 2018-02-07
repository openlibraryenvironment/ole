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

import java.security.GeneralSecurityException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.encryption.EncryptionService;

/**
 * This class calls core service to hash values going to the database
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class HibernateKualiHashType extends HibernateImmutableValueUserType implements UserType {
	/**
	 * Retrieves a value from the given ResultSet and decrypts it
	 * 
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {

		String value = rs.getString(names[0]);
		String converted = null;
		
		if ( value == null ) {
			return "";
		}
		return value + EncryptionService.HASH_POST_PREFIX;
	}

	/**
	 * sets the hash value on the PreparedStatement
	 * 
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	public void nullSafeSet(PreparedStatement st, Object value,  int index) throws HibernateException, SQLException {
		
		Object converted = value;
		if ( converted != null ) {
			// don't convert if already a hashed value
			if ( converted.toString().endsWith( EncryptionService.HASH_POST_PREFIX ) ) {
				converted = StringUtils.stripEnd( converted.toString(), EncryptionService.HASH_POST_PREFIX );
			} else {
				try {
					converted = CoreApiServiceLocator.getEncryptionService().hash(converted);
				} catch (GeneralSecurityException e) {
					throw new RuntimeException("Unable to hash value to db: " + e.getMessage());
				}
			}
		}

		if (converted == null) {
			st.setNull(index, Types.VARCHAR);
		} else {
			st.setString(index, (String)converted);
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
