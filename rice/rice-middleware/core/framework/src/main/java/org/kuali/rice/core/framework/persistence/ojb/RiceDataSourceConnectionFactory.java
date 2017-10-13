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
package org.kuali.rice.core.framework.persistence.ojb;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ojb.broker.accesslayer.ConnectionFactoryNotPooledImpl;
import org.apache.ojb.broker.accesslayer.LookupException;
import org.apache.ojb.broker.metadata.JdbcConnectionDescriptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

public class RiceDataSourceConnectionFactory extends ConnectionFactoryNotPooledImpl {

	/**
	 * BeanFactories to retrieve DataSource beans from.
	 */
	private static List<BeanFactory> beanFactories = new ArrayList<BeanFactory>();

	public static void addBeanFactory(BeanFactory beanFactory) {
		beanFactories.add(beanFactory);
	}

	/**
	 * Map that holds already retrieved DataSources,
	 * with JCD alias Strings as keys and DataSources as values.
	 */
	private Map<String, DataSource> dataSources = new HashMap<String, DataSource>();

	public RiceDataSourceConnectionFactory() {
		if (beanFactories.isEmpty()) {
			throw new IllegalStateException("No BeanFactories found for configuration - must specify RiceOjbConfigurer as a Spring bean.");
		}
	}

	public Connection lookupConnection(JdbcConnectionDescriptor jcd) throws LookupException {
		try {
			DataSource dataSource = null;
			synchronized (this.dataSources) {
				dataSource = this.dataSources.get(jcd.getJcdAlias());
				if (dataSource == null) {
					dataSource = getDataSource(jcd.getJcdAlias());
					this.dataSources.put(jcd.getJcdAlias(), dataSource);
				}
			}
			return dataSource.getConnection();
		}
		catch (Exception ex) {
			throw new LookupException("Could not obtain connection from data source", ex);
		}
	}

	/**
	 * Return the DataSource to use for the given JCD alias.
	 * <p>This implementation fetches looks for a bean with the
	 * JCD alias name in the provided Spring BeanFactory.
	 * @param jcdAlias the JCD alias to retrieve a DataSource for
	 * @return the DataSource to use
	 */

	protected DataSource getDataSource(String jcdAlias) throws LookupException {
		DataSource dataSource = null;
		for (BeanFactory beanFactory : beanFactories) {
			if (beanFactory.containsBean(jcdAlias)) {
				dataSource = (DataSource) beanFactory.getBean(jcdAlias, DataSource.class);
				break;
			}
		}
		if (dataSource == null) {
			throw new LookupException("Could not lookup datasource with alias " + jcdAlias);
		} else if (dataSource instanceof TransactionAwareDataSourceProxy) {
			return dataSource;
		} else {
			return new TransactionAwareDataSourceProxy(dataSource);
		}
	}

}
