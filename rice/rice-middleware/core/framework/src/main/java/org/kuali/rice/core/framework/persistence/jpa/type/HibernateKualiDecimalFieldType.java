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
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * This is a description of what this class does - g1zhang don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class HibernateKualiDecimalFieldType extends
HibernateImmutableValueUserType {

	/**
	 * This overridden method ...
	 * 
	 * @see HibernateImmutableValueUserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, Object source)
	throws HibernateException, SQLException {
		BigDecimal objectToConvert = rs.getBigDecimal(names[0]);

		if (objectToConvert != null) {
			return new KualiDecimal(objectToConvert);
		}

		return null;
	}

	/**
	 * This overridden method ...
	 * 
	 * @see HibernateImmutableValueUserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object source, int index)
	throws HibernateException, SQLException {
		
		Object converted =getConverted(source);

		if (converted == null) {
			st.setNull(index, Types.DECIMAL);
		} else {
			st.setBigDecimal(index, (BigDecimal)converted);
		}

	}

	public Object getConverted(Object value){

		Object source = null;

		if (value instanceof KualiDecimal) {
			source = ((KualiDecimal) value).bigDecimalValue();
		} else if (value instanceof BigDecimal) {
			source = value;
		}
		return source;
	}

	/**
	 * Returns String.class
	 * 
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	public Class returnedClass() {
		return BigDecimal.class;
	}

	/**
	 * Returns an array with the SQL VARCHAR type as the single member
	 * 
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	public int[] sqlTypes() {
		return new int[] { Types.DECIMAL };
	}
	
	//for sub types
	public Object getConvertedToKualiDecimal(Object value){
		Object converted = null;
		if (value instanceof BigDecimal) 
			converted = new KualiDecimal((BigDecimal) value);
		return converted;
	}
	public Object getConvertedToBigDecimal(Object value){

		Object converted = null;

		if (value instanceof KualiDecimal) {
			converted = ((KualiDecimal) value).bigDecimalValue();
		}
		return converted;
	}

}
