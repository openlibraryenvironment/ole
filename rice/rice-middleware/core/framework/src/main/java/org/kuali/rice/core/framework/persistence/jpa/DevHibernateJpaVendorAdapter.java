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
package org.kuali.rice.core.framework.persistence.jpa;

import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.spi.PersistenceProvider;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.InformixDialect;
import org.hibernate.dialect.Oracle9iDialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.dialect.SybaseASE15Dialect;
import org.hibernate.ejb.HibernateEntityManager;
import org.kuali.rice.core.framework.persistence.jpa.dialect.MySQLDialect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

/**
 * A Hibernate JPA adapter to expose the Kuali DEV DevHibernatePersistence.  The DEV
 * mode enables extra caching of the EntityManagerFactory and object serialization
 * of the Ejb3Configuration so that Hibernate startup can be faster in development.
 * 
 * Note: This implementation should not be used in test or production environments
 * unless further testing determines is it appropriate.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DevHibernateJpaVendorAdapter extends AbstractJpaVendorAdapter implements InitializingBean {

	private final PersistenceProvider persistenceProvider = new DevHibernatePersistence();

	private final JpaDialect jpaDialect = new HibernateJpaDialect();

	private String serializationFilename;
	private boolean useSerialization;
	
	public void afterPropertiesSet() throws Exception {
		((DevHibernatePersistence)persistenceProvider).setSerializationFilename(serializationFilename);
		((DevHibernatePersistence)persistenceProvider).setUseSerialization(useSerialization);
	}
	
	public PersistenceProvider getPersistenceProvider() {
		return this.persistenceProvider;
	}

	public Map getJpaPropertyMap() {
		Properties jpaProperties = new Properties();

		if (getDatabasePlatform() != null) {
			jpaProperties.setProperty(Environment.DIALECT, getDatabasePlatform());
		}
		else if (getDatabase() != null) {
			Class databaseDialectClass = determineDatabaseDialectClass(getDatabase());
			if (databaseDialectClass != null) {
				jpaProperties.setProperty(Environment.DIALECT, databaseDialectClass.getName());
			}
		}

		if (isGenerateDdl()) {
			jpaProperties.setProperty(Environment.HBM2DDL_AUTO, "update");
		}
		if (isShowSql()) {
			jpaProperties.setProperty(Environment.SHOW_SQL, "true");
		}

		return jpaProperties;
	}

	/**
	 * Determine the Hibernate database dialect class for the given target database.
	 * @param database the target database
	 * @return the Hibernate database dialect class, or <code>null<code> if none found
	 */
	protected Class determineDatabaseDialectClass(Database database) {
		switch (database) {
			case DB2: return DB2Dialect.class;
			case HSQL: return HSQLDialect.class;
			case INFORMIX: return InformixDialect.class;
			case MYSQL: return MySQLDialect.class;
			case ORACLE: return Oracle9iDialect.class;
			case POSTGRESQL: return PostgreSQLDialect.class;
			case SQL_SERVER: return SQLServerDialect.class;
			case SYBASE: return SybaseASE15Dialect.class;
			default: return null;
		}
	}

	public Class<? extends EntityManager> getEntityManagerInterface() {
		return HibernateEntityManager.class;
	}

	public JpaDialect getJpaDialect() {
		return this.jpaDialect;
	}

	public void setSerializationFilename(String serializationFilename) {
		this.serializationFilename = serializationFilename;
	}

	public void setUseSerialization(boolean useSerialization) {
		this.useSerialization = useSerialization;
	}

}
