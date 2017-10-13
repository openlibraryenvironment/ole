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
package org.kuali.rice.kew.preferences;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import org.junit.Test;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.api.preferences.PreferencesService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.useroptions.UserOptions;
import org.kuali.rice.kew.useroptions.UserOptionsService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class PreferencesServiceTest extends KEWTestCase {

    /**
     * Test that the preferences are saved by default when going through the preferences service.  This
     * means that the preferences service will persist any user option that was not in the db when it went
     * to fetch that preferences.
     */
	@Test public void testPreferencesDefaultSave() throws Exception {
       //verify that user doesn't have any preferences in the db.

       final UserOptionsService userOptionsService = KEWServiceLocator.getUserOptionsService();
       Principal principal = KEWServiceLocator.getIdentityHelperService().getPrincipalByPrincipalName("rkirkend");
       Collection userOptions = userOptionsService.findByWorkflowUser(principal.getPrincipalId());
       assertTrue("UserOptions should be empty", userOptions.isEmpty());

       PreferencesService preferencesService = KewApiServiceLocator.getPreferencesService();
       Preferences preferences = preferencesService.getPreferences(principal.getPrincipalId());
       assertTrue("Preferences should require a save.", preferences.isRequiresSave());

       userOptions = userOptionsService.findByWorkflowUser(principal.getPrincipalId());
       assertTrue("UserOptions should not empty", userOptions.isEmpty());

       preferencesService.savePreferences(principal.getPrincipalId(), preferences);
       userOptions = userOptionsService.findByWorkflowUser(principal.getPrincipalId());
       assertTrue("UserOptions should not be empty", !userOptions.isEmpty());

       preferences = preferencesService.getPreferences(principal.getPrincipalId());
       assertFalse("Preferences should NOT require a save.", preferences.isRequiresSave());

       // now delete one of the options
       final UserOptions refreshRateOption = userOptionsService.findByOptionId("REFRESH_RATE", principal.getPrincipalId());
       assertNotNull("REFRESH_RATE option should exist.", refreshRateOption);
       TransactionTemplate template = new TransactionTemplate(KEWServiceLocator.getPlatformTransactionManager());
       template.execute(new TransactionCallback() {
           public Object doInTransaction(TransactionStatus status) {
               userOptionsService.deleteUserOptions(refreshRateOption);
               return null;
           }
       });
       assertNull("REFRESH_RATE option should no longer exist.", userOptionsService.findByOptionId("REFRESH_RATE", principal.getPrincipalId()));

       preferences = preferencesService.getPreferences(principal.getPrincipalId());
       assertTrue("Preferences should now require a save again.", preferences.isRequiresSave());

       // save refresh rate again
       template.execute(new TransactionCallback() {
           public Object doInTransaction(TransactionStatus status) {
               userOptionsService.save(refreshRateOption);
               return null;
           }
       });
       preferences = preferencesService.getPreferences(principal.getPrincipalId());
       assertFalse("Preferences should no longer require a save.", preferences.isRequiresSave());
    }

    @Test
    public void testPreferencesMarshallingWithInvalidJson() {
        final UserOptionsService userOptionsService = KEWServiceLocator.getUserOptionsService();
        Principal principal = KEWServiceLocator.getIdentityHelperService().getPrincipalByPrincipalName("ewestfal");
        Collection<UserOptions> userOptions = userOptionsService.findByWorkflowUser(principal.getPrincipalId());
        assertTrue("UserOptions should be empty", userOptions.isEmpty());

        PreferencesService preferencesService = KewApiServiceLocator.getPreferencesService();
        Preferences preferences = preferencesService.getPreferences(principal.getPrincipalId());
        assertTrue("Preferences should require a save.", preferences.isRequiresSave());
        preferencesService.savePreferences(principal.getPrincipalId(), preferences);
        userOptions = userOptionsService.findByWorkflowUser(principal.getPrincipalId());

        UserOptions docSearchOrder = new UserOptions();
        docSearchOrder.setOptionId("DocSearch.LastSearch.Order");
        docSearchOrder.setWorkflowId(principal.getPrincipalId());
        docSearchOrder.setOptionVal("DocSearch.LastSearch.Holding0");

        UserOptions badJsonOption = new UserOptions();
        badJsonOption.setOptionId("DocSearch.LastSearch.Holding0");
        badJsonOption.setWorkflowId(principal.getPrincipalId());

        //this be invalid
        badJsonOption.setOptionVal("{isAdvancedSearch\":\"NO\",\"dateCreatedFrom\":1339168393063,\"documentStatuses\":[],\"documentStatusCategories\":[],\"documentAttributeValues\":{},\"additionalDocumentTypeNames\":[]}");

        userOptionsService.save(docSearchOrder);
        userOptionsService.save(badJsonOption);


        userOptions = userOptionsService.findByWorkflowUser(principal.getPrincipalId());
        assertTrue("UserOptions should not empty", !userOptions.isEmpty());

        //UserOptions previousDocSearch0 = userOptionsService.findByOptionId("DocSearch.LastSearch.Holding0", principal.getPrincipalId());
        preferences = preferencesService.getPreferences(principal.getPrincipalId());
        Preferences.Builder pBuilder = Preferences.Builder.create(preferences);
        Map<String, String> docTypeNotification = new HashMap<String, String>();

        docTypeNotification.put("hello", "world");
        docTypeNotification.put("does_this_thing_have_a", "bookstore example");
        pBuilder.setDocumentTypeNotificationPreferences(docTypeNotification);

        String marshaledXml = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Preferences.class);
            Marshaller marshaller = jaxbContext.createMarshaller();

            StringWriter stringWriter = new StringWriter();

            marshaller.marshal(pBuilder.build(), stringWriter);

            marshaledXml = stringWriter.toString();

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Preferences actual = (Preferences)unmarshaller.unmarshal(new StringReader(marshaledXml));
            //Object expected = unmarshaller.unmarshal(new StringReader(XML))
            Assert.assertEquals(pBuilder.build(), actual);

        } catch (Throwable e) {
            e.printStackTrace();
        }


    }


	/**
     * Tests default saving concurrently which can cause a race condition on startup
     * that leads to constraint violations
     */
    @Test public void testPreferencesConcurrentDefaultSave() throws Throwable {
       //verify that user doesn't have any preferences in the db.
       final UserOptionsService userOptionsService = KEWServiceLocator.getUserOptionsService();
       final Principal principal = KEWServiceLocator.getIdentityHelperService().getPrincipalByPrincipalName("rkirkend");
       Collection userOptions = userOptionsService.findByWorkflowUser(principal.getPrincipalId());
       assertTrue("UserOptions should be empty", userOptions.isEmpty());

       final PreferencesService preferencesService = KewApiServiceLocator.getPreferencesService();
       Runnable getPrefRunnable = new Runnable() {
           public void run() {
               Preferences preferences = preferencesService.getPreferences(principal.getPrincipalId());
               assertTrue("Preferences should require a save.", preferences.isRequiresSave());
               Collection updatedOptions = userOptionsService.findByWorkflowUser(principal.getPrincipalId());
               assertTrue("UserOptions should be empty", updatedOptions.isEmpty());
           }
       };
       final List<Throwable> errors = new ArrayList<Throwable>();
       Thread.UncaughtExceptionHandler ueh = new Thread.UncaughtExceptionHandler() {
           public void uncaughtException(Thread thread, Throwable error) {
               errors.add(error);
           }
       };

       // 3 threads should do
       Thread t1 = new Thread(getPrefRunnable);
       Thread t2 = new Thread(getPrefRunnable);
       Thread t3 = new Thread(getPrefRunnable);
       t1.setUncaughtExceptionHandler(ueh);
       t2.setUncaughtExceptionHandler(ueh);
       t3.setUncaughtExceptionHandler(ueh);
       t1.start();
       t2.start();
       t3.start();
       t1.join();
       t2.join();
       t3.join();

       if (errors.size() > 0) {
           throw errors.iterator().next();
       }

       Preferences preferences = preferencesService.getPreferences(principal.getPrincipalId());
       assertTrue("Preferences should require a save.", preferences.isRequiresSave());
       Collection updatedOptions = userOptionsService.findByWorkflowUser(principal.getPrincipalId());
       assertTrue("UserOptions should be empty", updatedOptions.isEmpty());
       preferencesService.savePreferences(principal.getPrincipalId(), preferences);
       updatedOptions = userOptionsService.findByWorkflowUser(principal.getPrincipalId());
       assertTrue("UserOptions should not be empty", !updatedOptions.isEmpty());
       t1.stop();
    }
}
