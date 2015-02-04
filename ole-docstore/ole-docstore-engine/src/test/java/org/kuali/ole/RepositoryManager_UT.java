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
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.jcr.Session;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: peris
 * Date: 5/9/11
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class RepositoryManager_UT
        extends BaseTestCase {

    @Before
    public void setUp() {
        System.getProperties().put("app.environment", "local");
    }

    @After
    public void tearDown() throws Exception {
        System.getProperties().remove("app.environment");
        RepositoryManager.getRepositoryManager().shutdown();
    }


    @Test
    public void testGetRepositoryManager() throws Exception {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        assertNotNull(repositoryManager);
    }

    @Test
    public void testGetSession() throws Exception {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession();
        repositoryManager.logout(session);
        assertNotNull(session);
    }

    @Test
    public void testGetSessionWithUserName() throws Exception {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession("mockUser", "test");
        repositoryManager.logout(session);
        assertNotNull(session);
    }

    @Test
    public void testGetMultipleSessions() throws Exception {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession();
        Session session1 = repositoryManager.getSession();
        assertNotNull(session);
        repositoryManager.logout(session);
        repositoryManager.logout(session1);
        assertTrue(!session.isLive());
        assertTrue(!session1.isLive());
    }

    @Test
    public void testGetMultipleSessionsWithMultipleUsers() throws Exception {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession("mockUser", "test");
        Session session1 = repositoryManager.getSession("mockUser1", "test");
        assertNotNull(session);
        repositoryManager.logout(session);
        repositoryManager.logout(session1);
        assertTrue(!session.isLive());
        assertTrue(!session1.isLive());
    }

    @Test
    public void testLogout() throws Exception {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession();
        repositoryManager.logout(session);
        assertTrue(!session.isLive());
    }

    @Test
    public void testGetTransientRepository() throws Exception {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        TransientRepository transientRepository = repositoryManager.getTransientRepository();
        assertNotNull(transientRepository);
    }

    @Test
    public void testShutdownRepository() throws Exception {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        repositoryManager.shutdownRepository();
    }
}
