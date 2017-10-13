/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.Assert;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.fail;


/**
 * Defines utilities for unit testing
 */
public final class TestUtilities {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(TestUtilities.class);

    private static final String TEST_TABLE_NAME = "EN_UNITTEST_T";
    private static Thread exceptionThreader;

	private TestUtilities() {
		throw new UnsupportedOperationException("do not call");
	}

    public static InputStream loadResource(Class packageClass, String resourceName) {
    	return packageClass.getResourceAsStream(resourceName);
    }

    public static TransactionTemplate getTransactionTemplate() {
		return (TransactionTemplate)
					GlobalResourceLoader.getService(KEWServiceLocator.TRANSACTION_TEMPLATE);
	}

    public static void verifyTestEnvironment(DataSource dataSource) {
        if (dataSource == null) {
            Assert.fail("Could not locate the data source.");
        }
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.execute(new ConnectionCallback() {
            public Object doInConnection(Connection connection) throws SQLException {
                ResultSet resultSet = connection.getMetaData().getTables(null, null, TEST_TABLE_NAME, null);
                if (!resultSet.next()) {
                    LOG.error("No table named '"+TEST_TABLE_NAME+"' was found in the configured database.  " +
                            "You are attempting to run tests against a non-test database!!!");
                    LOG.error("The test environment will not start up properly!!!");
                    Assert.fail("No table named '"+TEST_TABLE_NAME+"' was found in the configured database.  " +
                    		"You are attempting to run tests against a non-test database!!!");
                }
                return null;
            }
        });
    }

    public static void clearTables(final PlatformTransactionManager transactionManager, final DataSource dataSource, final String edenSchemaName, final List<String> dontClear) {
        LOG.info("Clearing tables for schema " + edenSchemaName);
        if (dataSource == null) {
            Assert.fail("Null data source given");
        }
        if (edenSchemaName == null || edenSchemaName.equals("")) {
            Assert.fail("Empty eden schema name given");
        }
        new TransactionTemplate(transactionManager).execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                verifyTestEnvironment(dataSource);
                JdbcTemplate template = new JdbcTemplate(dataSource);
                return template.execute(new StatementCallback() {
                    public Object doInStatement(Statement statement) throws SQLException {
                        List<String> reEnableConstraints = new ArrayList<String>();
                    	ResultSet resultSet = statement.getConnection().getMetaData().getTables(null, edenSchemaName, null, new String[] { "TABLE" });
                        while (resultSet.next()) {
                            String tableName = resultSet.getString("TABLE_NAME");
                            if (tableName.startsWith("EN_") && !dontClear.contains(tableName)) {
                            	ResultSet keyResultSet = statement.getConnection().getMetaData().getExportedKeys(null, edenSchemaName, tableName);
                            	while (keyResultSet.next()) {
                            		String fkName = keyResultSet.getString("FK_NAME");
                            		String fkTableName = keyResultSet.getString("FKTABLE_NAME");
                            		statement.addBatch("ALTER TABLE "+fkTableName+" DISABLE CONSTRAINT "+fkName);
                            		reEnableConstraints.add("ALTER TABLE "+fkTableName+" ENABLE CONSTRAINT "+fkName);
                            	}
                            	keyResultSet.close();
                            	statement.addBatch("DELETE FROM "+tableName);
                            }
                        }
                        for (String constraint : reEnableConstraints) {
                    		statement.addBatch(constraint);
                    	}
                        statement.executeBatch();
                        resultSet.close();
                        return null;
                    }
                });
            }
        });
        LOG.info("Tables successfully cleared for schema " + edenSchemaName);
    }

    public static Set<String> createNodeInstanceNameSet(Collection nodeInstances) {
    	Set<String> nameSet = new HashSet<String>();
    	for (Iterator iterator = nodeInstances.iterator(); iterator.hasNext(); ) {
			RouteNodeInstance nodeInstance = (RouteNodeInstance) iterator.next();
			nameSet.add(nodeInstance.getName());
		}
    	return nameSet;
    }

    /**
     * Checks that the document is at a node with the given name.  This does not check that the document is at
     * the given node and only the given node, the document can be at other nodes as well and this assertion
     * will still pass.
     */
    public static void assertAtNode(String message, WorkflowDocument document, String nodeNameToAssert) {
		Set<String> nodeNames = document.getNodeNames();
		for (String nodeName : nodeNames) {
			if (nodeNameToAssert.equals(nodeName)) {
				return;
			}
		}
		fail((org.apache.commons.lang.StringUtils.isEmpty(message) ? "" : message + ": ") + "Was [" + StringUtils.join(nodeNames, ", ") + "], Expected " + nodeNameToAssert);
	}

    public static void assertAtNode(WorkflowDocument document, String nodeName) throws WorkflowException {
    	assertAtNode("", document, nodeName);
    }
    
    /**
     * Checks that the document is at a node with the given name.  This does not check that the document is at
     * the given node and only the given node, the document can be at other nodes as well and this assertion
     * will still pass.
     */
    public static void assertAtNodeNew(String message, org.kuali.rice.kew.api.WorkflowDocument document, String nodeName) throws WorkflowException {
		Set<String> nodeNames = document.getNodeNames();
		if (!nodeNames.contains(nodeName)) {
			fail((org.apache.commons.lang.StringUtils.isEmpty(message) ? "" : message + ": ") + "Was [" + StringUtils.join(nodeNames, ", ") + "], Expected " + nodeName);
		}
	}
    
    public static void assertAtNodeNew(org.kuali.rice.kew.api.WorkflowDocument document, String nodeName) throws WorkflowException {
    	assertAtNodeNew("", document, nodeName);
    }

    /**
     * Asserts that the given document id is in the given user's action list.
     */
    public static void assertInActionList(String principalId, String documentId) {
    	Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
    	Assert.assertNotNull("Given principal id was invalid: " + principalId, principal);
    	Collection<ActionItem> actionList = KEWServiceLocator.getActionListService().findByPrincipalId(principalId);
    	for (Iterator iterator = actionList.iterator(); iterator.hasNext();) {
			ActionItem actionItem = (ActionItem) iterator.next();
			if (actionItem.getDocumentId().equals(documentId)) {
				return;
			}
		}
    	Assert.fail("Could not locate an action item in the user's action list for the given document id.");
    }

    /**
     * Asserts that the given document id is NOT in the given user's action list.
     */
    public static void assertNotInActionList(String principalId, String documentId) {
    	Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
    	Assert.assertNotNull("Given principal id was invalid: " + principalId, principal);
    	Collection actionList = KEWServiceLocator.getActionListService().findByPrincipalId(principalId);
    	for (Iterator iterator = actionList.iterator(); iterator.hasNext();) {
			ActionItem actionItem = (ActionItem) iterator.next();
			if (actionItem.getDocumentId().equals(documentId)) {
				Assert.fail("Found an action item in the user's acton list for the given document id.");
			}
		}
    }

    public static void assertNumberOfPendingRequests(String documentId, int numberOfPendingRequests) {
    	List actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(documentId);
    	Assert.assertEquals("Wrong number of pending requests for document: " + documentId, numberOfPendingRequests, actionRequests.size());
    }

    /**
     * Asserts that the user with the given network id has a pending request on the given document
     */
    public static void assertUserHasPendingRequest(String documentId, String principalName) throws WorkflowException {
    	String principalId = KEWServiceLocator.getIdentityHelperService().getIdForPrincipalName(principalName);
    	List actionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(documentId);
    	boolean foundRequest = false;
    	for (Iterator iterator = actionRequests.iterator(); iterator.hasNext();) {
			ActionRequestValue actionRequest = (ActionRequestValue) iterator.next();
			if (actionRequest.isUserRequest() && actionRequest.getPrincipalId().equals(principalId)) {
				foundRequest = true;
				break;
			} else if (actionRequest.isGroupRequest() && 
			        KimApiServiceLocator.getGroupService().isMemberOfGroup(principalId, actionRequest.getGroup().getId())) {
				foundRequest = true;
				break;
			}
		}
    	Assert.assertTrue("Could not locate pending request for the given user: " + principalId, foundRequest);
    }

    /**
     * Asserts that the specified users do or do not have outstanding approvals
     * @param docId the id of the document
     * @param users the list of users
     * @param shouldHaveApproval whether they should have an approval outstanding
     * @throws WorkflowException
     */
    public static void assertApprovals(String docId, String[] users, boolean shouldHaveApproval) throws WorkflowException {
        List<String> failedUsers = new ArrayList<String>();
        IdentityService ims = KimApiServiceLocator.getIdentityService();
        for (String user: users) {
            WorkflowDocument doc = WorkflowDocumentFactory.loadDocument(ims.getPrincipalByPrincipalName(user).getPrincipalId(), docId);
            boolean appRqsted = doc.isApprovalRequested();
            if (shouldHaveApproval != appRqsted) {
                failedUsers.add(user);
            }
            LOG.info("User " + user + (appRqsted ? " HAS " : " HAS NO ") + "approval request");
        }
        for (String user: failedUsers) {
            LOG.error("User " + user + (shouldHaveApproval ? " should have " : " should NOT have ") + " approval");
        }
        if (failedUsers.size() > 0) {
            Assert.fail("Outstanding approvals are incorrect");
        }
    }
    
    public static WorkflowDocument switchByPrincipalName(String principalName, WorkflowDocument document) throws WorkflowException {
    	return switchPrincipalId(KEWServiceLocator.getIdentityHelperService().getIdForPrincipalName(principalName), document);
    }
    
    public static WorkflowDocument switchPrincipalId(String principalId, WorkflowDocument document) throws WorkflowException {
    	return WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
    }

    public static void logActionRequests(String docId) {
        List<ActionRequestValue> actionRequests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(docId);
        LOG.info("Current action requests:");
        for (ActionRequestValue ar: actionRequests) {
            LOG.info(ar);
        }
    }

    public static JdbcTemplate getJdbcTemplate() {
    	JdbcTemplate jdbcTemplate = new JdbcTemplate(KEWServiceLocator.getDataSource());
    	jdbcTemplate.afterPropertiesSet();
    	return jdbcTemplate;
    }
    
    /**
     * Waits "indefinately" for the exception routing thread to terminate.
     *
     * This actually doesn't wait forever but puts an upper bound of 5 minutes
     * on the time to wait for the exception routing thread to complete.  If a
     * document cannot go into exception routing within 5 minutes  then we got
     * problems.
     */
    public static void waitForExceptionRouting() {
    	waitForExceptionRouting(5*60*1000);
    }

    public static void waitForExceptionRouting(long milliseconds) {
    	if (getExceptionThreader() == null) {
    		return;
    	}
    	try {
    		getExceptionThreader().join(milliseconds);
    	} catch (InterruptedException e) {
    		Assert.fail("This thread was interuppted while waiting for exception routing.");
    	}
    	if (getExceptionThreader().isAlive()) {
    		Assert.fail("Document was not put into exception routing within the specified amount of time " + milliseconds);
    	}
    }

    public static Thread getExceptionThreader() {
        return exceptionThreader;
    }

    public static void setExceptionThreader(Thread exceptionThreader) {
        TestUtilities.exceptionThreader = exceptionThreader;
    }

    private static final String DEFAULT_TEST_PLATFORM = "oracle";
	private static final String BUILD_PROPERTIES = "build.properties";
	private static final String TEST_PLATFORM = "test.platform";

    /**
     * Attempts to derive the database "platform" to use for unit tests by
     * inspected Ant build.properties files in typical locations.
     * @return the test platform if so defined in Ant build.properties file(s), or the DEFAULT_TEST_PLATFORM otherwise
     * @see #DEFAULT_TEST_PLATFORM
     * @throws IOException if anything goes awry
     */
	public static String getTestPlatform() throws IOException {
        // check the user's build.properties in user's home
		File userBuildProperties = new File(SystemUtils.USER_HOME + "/" + BUILD_PROPERTIES);
		if (userBuildProperties.isFile()) {
			Properties properties = loadProperties(userBuildProperties);
			if (properties.containsKey(TEST_PLATFORM)) {
				return properties.getProperty(TEST_PLATFORM).toLowerCase();
			}
		}
		// check the "local" build.properties in the current directory
        File localBuildProperties = new File(BUILD_PROPERTIES);
        if (localBuildProperties.isFile()) {
            Properties properties = loadProperties(localBuildProperties);
            if (properties.containsKey(TEST_PLATFORM)) {
                return properties.getProperty(TEST_PLATFORM).toLowerCase();
            }
        }
		return DEFAULT_TEST_PLATFORM.toLowerCase();
	}

    /**
     * Loads a file into a Properties object
     * @param file the file
     * @return a Properties object
     */
    private static Properties loadProperties(File file) throws IOException {
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(file);
        try {
            properties.load(fis);
        } finally {
            fis.close();
        }
        return properties;
    }

	public static File createTempDir() throws Exception {
		File tmpFile = File.createTempFile("wfUnitTest", "");
		Assert.assertTrue(tmpFile.delete());
		File tmpDir = new File(new File(SystemUtils.JAVA_IO_TMPDIR), tmpFile.getName());
		Assert.assertTrue(tmpDir.mkdir());
		tmpDir.deleteOnExit();
		return tmpDir;
}
	
	public static File getPluginsDirectory() {
        String directory = ConfigContext.getCurrentContextConfig().getProperty(Config.PLUGIN_DIR);
        if (StringUtils.isNotBlank(directory)) {
            return new File(directory);
        }
		return new File("./work/unit-test/plugins");
	}

	public static void initializePluginDirectories() throws Exception {
		File pluginDir = getPluginsDirectory();
		if (pluginDir.exists()) {
			FileUtils.forceDelete(pluginDir);
		}
		FileUtils.forceMkdir(pluginDir);
		FileUtils.forceDeleteOnExit(pluginDir);
	}

	public static void cleanupPluginDirectories() throws Exception {
		FileUtils.deleteDirectory(getPluginsDirectory());
	}

	/**
     * This method searches for an exception of the specified type in the exception stack
     * @param topLevelException the exception whose stack to traverse
     * @param exceptionClass the exception class to look for
     * @return the first instance of an exception of the specified class if found, or null otherwise
     */
    public static <T extends Throwable> T findExceptionInStack(Throwable topLevelException, Class<T> exceptionClass) {
        Throwable t = topLevelException;
        while (t != null) {
            if (exceptionClass.isAssignableFrom(t.getClass())) return (T) t;
            t = t.getCause();
        }
        return null;
    }
}
