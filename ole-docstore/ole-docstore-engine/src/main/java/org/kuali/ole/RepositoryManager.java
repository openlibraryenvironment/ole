/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole;

import org.apache.jackrabbit.core.TransientRepository;
import org.kuali.ole.logger.MetricsLogger;
import org.kuali.ole.pojo.OleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Peri Subrahmanya
 * Date: 3/30/11
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class RepositoryManager {
    private static final Logger LOG = LoggerFactory.getLogger(RepositoryManager.class);

    private TransientRepository transientRepository;
    private static RepositoryManager repositoryManager;
    private MetricsLogger metricsLogger;
    private List<Session> sessionPool = new ArrayList<Session>();
    boolean customNodeTypesInitialllized = false;

    private RepositoryManager() throws OleException {

    }

    public static RepositoryManager getRepositoryManager() throws OleException {
        if (null == repositoryManager) {
            repositoryManager = new RepositoryManager();
        }
        return repositoryManager;
    }


    public synchronized Session getSession() throws OleException {
        return getSession(null, null);
    }

    public synchronized Session getSession(String userName, String actionSource) throws OleException {
        if (!customNodeTypesInitialllized) {
            init();
        }
        Session session = null;
        getMetricsLogger().startRecording();

        try {
            LOG.debug("Environment: " + System.getProperty("app.environment"));

            session = getTransientRepository()
                    .login(new SimpleCredentials(null == userName ? "username" : userName, "password".toCharArray()));
            LOG.info("session successfully created with id: " + session.getUserID() + " for the following action " + (
                    (actionSource == null) ? "default" : actionSource));
        } catch (RepositoryException e) {
            return throwException(e);
        } catch (IOException e) {
            throwException(e);
        } catch (Exception e) {
            throwException(e);
        }
        metricsLogger.endRecording();
        metricsLogger.printTimes("time taken to initiate a session was: ");
        return session;
    }

    private MetricsLogger getMetricsLogger() {
        if (null == metricsLogger) {
            metricsLogger = new MetricsLogger(this.getClass().getName());
        }
        return metricsLogger;
    }

    private Session throwException(Exception e) throws OleException {
        LOG.error("Unable to get the session from the repository", e);
        throw new OleException("Unable to get the session from the repository", e);
    }

    private void registerNodeTypes() throws OleException {
        try {
            Session session = getSession("repositoryManager", "registerNodeTypes");
            new CustomNodeRegistrar().registerCustomNodeTypes(session);
            sessionPool.add(session);
        } catch (OleException e) {
            LOG.error("Error registering node types", e);
        }
    }

    public synchronized void logout(Session session) {
        if (null != session && session.isLive()) {
            if (!sessionPool.contains(session)) {
                session.logout();
                LOG.info("session successfully closed for user: " + session.getUserID());
            }
        }
        LOG.info("Num sessions in the pool: " + sessionPool.size());
    }

    public TransientRepository getTransientRepository() throws IOException {
        if (null == transientRepository) {
            transientRepository = new TransientRepository();
        }
        return transientRepository;
    }

    public void shutdown() {
        if (null != transientRepository) {
            transientRepository.shutdown();
        }
    }


    public void init() throws OleException {
        customNodeTypesInitialllized = true;
        registerNodeTypes();
        setupInternalModel();
    }

    private void setupInternalModel() throws OleException {
        try {
            new DocumentStoreModelInitiallizer().init();
        } catch (Exception e) {
            throw new OleException(e.getMessage());
        }
    }

    public void shutdownRepository() {
        transientRepository.shutdown();
        LOG.info("Repository has been down");
    }

    public static void setRepositoryManager(RepositoryManager repositoryManager) {
        RepositoryManager.repositoryManager = repositoryManager;
    }
}
