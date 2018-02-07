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
import org.hibernate.type.StandardBasicTypes;
import org.kuali.rice.core.api.util.type.KualiInteger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 *  KualiInteger Type converter 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class HibernateKualiIntegerFieldType extends	HibernateImmutableValueUserType {

	/**
	 * This overridden method ...
	 * 
	 * @see HibernateImmutableValueUserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
			throws HibernateException, SQLException {
		
        Object source = StandardBasicTypes.LONG.nullSafeGet(rs, names[0]);
		Object converted = null;
        
        if ((Long)source instanceof Long) {
            converted = new KualiInteger(((Long) source).longValue());
        }
        return converted;
    }

	/**
	 * This overridden method ...
	 * 
	 * @see HibernateImmutableValueUserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object source, int index)
			throws HibernateException, SQLException {
		
        Object converted = source;

        if (source instanceof KualiInteger) {
            converted = Long.valueOf(((KualiInteger)source).longValue());
        }
        
        if (converted == null) {
        	st.setNull(index, Types.BIGINT); 
        } else {
        	st.setLong(index, ((Long)converted).longValue()); 
        }
	}

	/**
	 * Returns String.class
	 * 
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	public Class returnedClass() {
		return Long.class;
	}

	/**
	 * Returns an array with the SQL VARCHAR type as the single member
	 * 
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	public int[] sqlTypes() {
		return new int[] { Types.BIGINT};
	}

}
