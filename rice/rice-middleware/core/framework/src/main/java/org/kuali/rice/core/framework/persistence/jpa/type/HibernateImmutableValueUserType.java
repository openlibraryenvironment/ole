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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * This is a description of what this class does - jksmith don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public abstract class HibernateImmutableValueUserType implements UserType {

	/**
	 * As the object is immutable, the immutable value will simply stay in the cache
	 * 
	 * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
	 */
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	/**
	 * As the object is immutable, simply returns the value
	 * 
	 * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
	 */
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	/**
	 * As the object is immutable, returns the value if it is Serializable - otherwise returns null
	 * 
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
	public Serializable disassemble(Object value) throws HibernateException {
		return (value instanceof Serializable) ? (Serializable)value : null;
	}

	/**
	 * As the object is immutable, assumes it has a good equals method on it
	 * 
	 * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	public boolean equals(Object x, Object y) throws HibernateException {
		return (x == y) || (x != null && x.equals(y));
	}

	/**
	 * Returns the hashcode of the passed in value
	 * 
	 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
	 */
	public int hashCode(Object value) throws HibernateException {
		return value.hashCode();
	}

	/**
	 * Immutable types aren't mutable...kind of by definintion
	 * 
	 * @see org.hibernate.usertype.UserType#isMutable()
	 */
	public boolean isMutable() {
		return false;
	}

	/**
	 * Abstract method for children classes to override - this returns the SQL value from the given ResultSet into a Java value
	 * 
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	public abstract Object nullSafeGet(ResultSet rs, String[] names, Object owner)throws HibernateException, SQLException;

	/**
	 * Abstract method for children to override - takes a Java value and changes it to be correctly added to the given PreparedStatement
	 * 
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	public abstract void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException;

	/**
	 * Since the original is immutable, returns the original
	 * 
	 * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	/**
	 * Abstract method for children to override - returns the class of the Java value returned by nullSafeGet
	 * 
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	public abstract Class returnedClass();

	/**
	 * Abstract method for children to override - returns the SQL type set in nullSafeSet
	 * 
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	public abstract int[] sqlTypes();
}
