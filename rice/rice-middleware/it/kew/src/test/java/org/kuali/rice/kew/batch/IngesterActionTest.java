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
package org.kuali.rice.kew.batch;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.junit.Test;
import org.kuali.rice.core.web.impex.IngesterAction;
import org.kuali.rice.core.web.impex.IngesterForm;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.web.MockFormFile;
import org.kuali.rice.kew.test.web.WorkflowServletRequest;
import org.kuali.rice.krad.web.filter.UserLoginFilter;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Tests workflow Struts IngesterAction
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class IngesterActionTest extends KEWTestCase {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(IngesterActionTest.class);

    private static final String TRANSACTION_FAILED_REGEX = "(?i)^Ingestion failed$";
    private static final String SUCCESS_MESSAGE_REGEX_PRE = "(?ism)^Ingested xml doc.*";
    private static final String SUCCESS_MESSAGE_REGEX_POST = ".*";
    private static final String FAILURE_MESSAGE_REGEX_PRE = "(?ism)^((Failed to ingest xml doc)|(Rolled back doc)).*";
    private static final String FAILURE_MESSAGE_REGEX_POST = ".*";

    private static final String escape(String fileName) {
        return fileName.replaceAll("\\.", "\\\\.");
    }

    private static final String getSuccessRegex(String fileName) {
        return SUCCESS_MESSAGE_REGEX_PRE + escape(fileName) + SUCCESS_MESSAGE_REGEX_POST;
    }

    private static final String getFailureRegex(String fileName) {
        return FAILURE_MESSAGE_REGEX_PRE + escape(fileName) + FAILURE_MESSAGE_REGEX_POST;
    }

    private boolean findMessage(List messages, String regex) {
        Pattern p = Pattern.compile(regex);
        Iterator it = messages.iterator();
        LOG.error(regex);
        while (it.hasNext()) {
            String message = (String) it.next();
            LOG.error(message);
            if (p.matcher(message).matches()) {
                return true;
            }
        }
        return false;
    }

    @Test public void testSuccessfulIngestion() throws Exception {
        testIngestion("IngesterActionTest_success.txt", true);
    }

    @Test public void testFailedIngestion() throws Exception {
        testIngestion("IngesterActionTest_failure.txt", false);
    }

    @SuppressWarnings("unchecked")
	private void testIngestion(String config, boolean shouldSucceed) throws Exception {
        IngesterForm form = new IngesterForm();
        Properties filesToIngest = new Properties();
        filesToIngest.load(getClass().getResourceAsStream(config));
        List shouldPass = new LinkedList();
        List shouldFail = new LinkedList();

        // add all test files to form
        Iterator entries = filesToIngest.entrySet().iterator();
        int i = 0;
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String filePath = entry.getKey().toString();
            filePath = filePath.replace("${basedir}", getBaseDir());
            String fileName = new File(filePath).getName();
            if (Boolean.valueOf(entry.getValue().toString()).booleanValue()) {
                shouldPass.add(fileName);
            } else {
                shouldFail.add(fileName);
            }
            FormFile file = new MockFormFile(new File(filePath));
            form.setFile(i, file);
            assertTrue(form.getFiles().size() == i+1);
            i++;
        }

        assertTrue(form.getFiles().size() > 0);

        // invoke action
        IngesterAction action = new IngesterAction();
        ActionMapping mapping = new ActionMapping();
        mapping.addForwardConfig(new ActionForward("view", "/nowhere", false));
        WorkflowServletRequest request = new WorkflowServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setUser("admin");
        // add the user to the session
        new UserLoginFilter().doFilter(request, response, new FilterChain() {
            public void doFilter(ServletRequest req, ServletResponse res) {
            }
        });
        request.setMethod("post");
        try {
        	UserSession userSession = (UserSession)request.getSession().getAttribute(KRADConstants.USER_SESSION_KEY);
        	assertNotNull("UserSession should have been established.", userSession);
        	GlobalVariables.setUserSession(userSession);
        	action.execute(mapping, form, request, response);
        } finally {
        	GlobalVariables.setUserSession(null);
        }

        // test result
        List messages = (List) request.getAttribute("messages");
        assertNotNull(messages);

        Iterator it = shouldFail.iterator();
        while (it.hasNext()) {
            String file = it.next().toString();
            LOG.error("file: " + file);
            LOG.error("file replaced: " + escape(file));
            assertTrue(findMessage(messages, getFailureRegex(file)));
        }


        // test that the global transaction failure message was emitted
        boolean failed = shouldFail.size() > 0;
        if (failed && shouldSucceed) {
            fail("Ingestation failed but should have succeeded");
        } else if (!failed && !shouldSucceed) {
            fail("Ingestation succeeded but should have failed");
        }

        if (failed) {
            assertTrue(findMessage(messages, TRANSACTION_FAILED_REGEX));
        }

        it = shouldPass.iterator();
        while (it.hasNext()) {
            if (failed) {
                assertTrue(findMessage(messages, getFailureRegex(it.next().toString())));
            } else {
                assertTrue(findMessage(messages, getSuccessRegex(it.next().toString())));
            }
        }
    }
}
