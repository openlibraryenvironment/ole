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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;

import org.apache.log4j.Logger;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernatePersistence;

/*
 * This is a temporary hack until we find a better way to override
 * createContainerEntityManagerFactory(...) below so that we can serialize the
 * configuration and cache the factory.
 */
public class DevHibernatePersistence extends HibernatePersistence {

	private static final Logger LOG = Logger.getLogger(DevHibernatePersistence.class);
	
	private String serializationFilename;

	private boolean useSerialization;

	// Caching the factory does increase speed, but then it uses the first set of jpa properties specified rather than jpa properties per factory
	// private static EntityManagerFactory factory = null;
	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map map) {
		//if (factory != null) {
		//	return factory;
		//}
		EntityManagerFactory factory = null;		
		Ejb3Configuration cfg = null;
		ObjectInputStream in = null;
		try {
			if (useSerialization && new File(serializationFilename).exists()) {
				in = new ObjectInputStream(new FileInputStream(serializationFilename));
				cfg = (Ejb3Configuration) in.readObject();
			} else {
				cfg = configure(info, map);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		
		if (cfg != null) {
			factory = cfg.buildEntityManagerFactory();
		} else {
			LOG.error("Error creating Ejb3Configuration");
		}
		return factory;
	}

	private Ejb3Configuration configure(PersistenceUnitInfo info, Map map) throws IOException {		
		Ejb3Configuration configured = new Ejb3Configuration().configure(info, map);
		if (useSerialization) {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serializationFilename));
			oos.writeObject(configured);
			oos.close();
		}
		return configured;
	}

	public void setSerializationFilename(String serializationFilename) {
		this.serializationFilename = serializationFilename;
	}

	public void setUseSerialization(boolean useSerialization) {
		this.useSerialization = useSerialization;
	}

}
