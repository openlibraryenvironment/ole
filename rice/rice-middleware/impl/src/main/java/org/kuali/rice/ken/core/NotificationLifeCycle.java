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
package org.kuali.rice.ken.core;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.lifecycle.LifecycleBean;
import org.kuali.rice.core.api.lifecycle.LifecycleBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Eager-initializing singleton bean that performs some notification startup operations
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationLifeCycle extends LifecycleBean implements BeanFactoryAware {
    private static final Logger LOG = Logger.getLogger(NotificationLifeCycle.class);

    //private String ojbPlatform;
    private BeanFactory theFactory;
    private PlatformTransactionManager txMgr;
    private DataSource dataSource;

    /**
     * This method sets the OJB platform.
     * @param platform
     */
    /*
    public void setOjbPlatform(String platform) {
        this.ojbPlatform = platform;
    }*/

    /**
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory theFactory) throws BeansException {
	 this.theFactory = theFactory;
    }

    public void setTransactionManager(PlatformTransactionManager txMgr) {
        this.txMgr = txMgr;
    }
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Helper method for creating a TransactionTemplate initialized to create
     * a new transaction
     * @return a TransactionTemplate initialized to create a new transaction
     */
    protected TransactionTemplate createNewTransaction() {
        TransactionTemplate tt = new TransactionTemplate(txMgr);
        tt.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
        return tt;
    }

    /**
     * @see org.kuali.rice.ken.core.BaseLifecycle#start()
     */
    public void start() throws Exception {
        /*if (ojbPlatform == null) {
            throw new BeanInitializationException("No platform was configured, please configure the datasource.ojb.platform property.");
        }*/

        GlobalNotificationServiceLocator.init(theFactory);
        /*
        createNewTransaction().execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus txStatus) {
                JdbcTemplate t = new JdbcTemplate(dataSource);
                Boolean dataLoaded = (Boolean) t.execute(new StatementCallback() {
                    public Object doInStatement(Statement stmt) throws SQLException, DataAccessException {
                        ResultSet rs = stmt.executeQuery("select * from APP_META_T where NAME = 'ken.bootstrap.loaded' and VALUE = 'true'");
                        try {
                            return rs.next();
                        } finally {
                            rs.close();
                        }
                    }
                });
                if (!dataLoaded.booleanValue()) {
                    loadXmlFile("classpath:data/NotificationData.xml");
                    
                    t.execute(new StatementCallback() {
                        public Object doInStatement(Statement stmt) throws SQLException, DataAccessException {
                            ResultSet rs = stmt.executeQuery("update APP_META_T where NAME = 'ken.bootstrap.loaded' and VALUE = 'true'");
                            try {
                                return rs.next();
                            } finally {
                                rs.close();
                            }
                        }
                    });
                }
            }
        });
        */
        
        //LOG.info("Setting OJB platform to: " + ojbPlatform);
        //PersistenceBrokerFactory.defaultPersistenceBroker().serviceConnectionManager().getConnectionDescriptor().setDbms(ojbPlatform);
        super.start();
    }

    // yanked from KEWXmlDataLoaderLifecycle
    /*
    protected void loadXmlFile(String fileName) throws Exception {
        Resource resource = new DefaultResourceLoader().getResource(fileName);
        InputStream xmlFile = resource.getInputStream();
        if (xmlFile == null) {
                throw new ConfigurationException("Didn't find file " + fileName);
        }
        List<XmlDocCollection> xmlFiles = new ArrayList<XmlDocCollection>();
        XmlDocCollection docCollection = getFileXmlDocCollection(xmlFile, "UnitTestTemp");
        xmlFiles.add(docCollection);
        KEWServiceLocator.getXmlIngesterService().ingest(xmlFiles);
        for (Iterator iterator = docCollection.getXmlDocs().iterator(); iterator.hasNext();) {
                XmlDoc doc = (XmlDoc) iterator.next();
                if (!doc.isProcessed()) {
                        throw new RuntimeException("Failed to ingest xml doc: " + doc.getName());
                }
        }
    }

    protected FileXmlDocCollection getFileXmlDocCollection(InputStream xmlFile, String tempFileName) throws IOException {
        if (xmlFile == null) {
            throw new RuntimeException("Didn't find the xml file " + tempFileName);
        }
        File temp = File.createTempFile(tempFileName, ".xml");
        FileOutputStream fos = new FileOutputStream(temp);
        int data = -1;
        while ((data = xmlFile.read()) != -1) {
            fos.write(data);
        }
        fos.close();
        return new FileXmlDocCollection(temp);
    }*/

    /**
     * @see org.kuali.rice.ken.core.BaseLifecycle#stop()
     */
    public void stop() throws Exception {
        GlobalNotificationServiceLocator.destroy();
        super.stop();
    }
}
