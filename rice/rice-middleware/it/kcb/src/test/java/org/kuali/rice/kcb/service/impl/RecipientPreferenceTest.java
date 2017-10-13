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
package org.kuali.rice.kcb.service.impl;

import org.junit.Test;
import org.kuali.rice.kcb.bo.RecipientPreference;
import org.kuali.rice.kcb.service.GlobalKCBServiceLocator;
import org.kuali.rice.kcb.service.RecipientPreferenceService;
import org.kuali.rice.kcb.test.BusinessObjectTestCase;
import org.kuali.rice.kcb.test.KCBTestData;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests recipient preferences
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RecipientPreferenceTest extends BusinessObjectTestCase {
    private RecipientPreference PREF;
    private RecipientPreferenceService prefsvc;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        prefsvc = GlobalKCBServiceLocator.getInstance().getRecipientPreferenceService();

        PREF = new RecipientPreference();
        PREF.setRecipientId("user1");
        PREF.setProperty("property1");
        PREF.setValue("value1");

        prefsvc.saveRecipientPreference(PREF);
    }

    @Test
    @Override
    public void testCreate() {
        RecipientPreference p2 = new RecipientPreference();
        p2.setRecipientId("user1");
        p2.setProperty("property2");
        p2.setValue("value2");

        prefsvc.saveRecipientPreference(p2);
        assertNotNull(p2.getId());

        Map<String, String> p = prefsvc.getRecipientPreferences("user1");
        assertNotNull(p);
        assertEquals(2, p.size());

        assertTrue(p.containsKey("property1"));
        assertTrue(p.containsKey("property2"));
    }

    @Test
    @Override
    public void testDelete() {
        prefsvc.deleteRecipientPreference(PREF);

        assertNull(prefsvc.getRecipientPreference(PREF.getRecipientId(), PREF.getProperty()));
        assertEquals(0, prefsvc.getRecipientPreferences(PREF.getRecipientId()).size());
    }

    @Test(expected = DataIntegrityViolationException.class)
    @Override
    public void testDuplicateCreate() {
        final RecipientPreference p = new RecipientPreference();
        p.setId(KCBTestData.FAKE_ID);
        p.setRecipientId(PREF.getRecipientId());
        p.setProperty(PREF.getProperty());
        p.setValue(PREF.getValue());

        prefsvc.saveRecipientPreference(p);
    }

    @Test(expected = DataIntegrityViolationException.class)
    @Override
    public void testInvalidCreate() {
        final RecipientPreference p = new RecipientPreference();
        prefsvc.saveRecipientPreference(p);
    }

    @Test(expected = DataAccessException.class)
    @Override
    public void testInvalidDelete() {
        final RecipientPreference p = new RecipientPreference();
        p.setId(KCBTestData.INVALID_ID);
        // OJB yields an org.springmodules.orm.ojb.OjbOperationException/OptimisticLockException and claims the object
        // may have been deleted by somebody else
        prefsvc.deleteRecipientPreference(p);
    }

    @Test
    @Override
    public void testInvalidRead() {
        RecipientPreference p = prefsvc.getRecipientPreference("nobody", "nuthin'");
        assertNull(p);
    }

    @Test(expected = DataAccessException.class)
    @Override
    public void testInvalidUpdate() {
        RecipientPreference sample = new RecipientPreference();
        sample.setRecipientId("user1");
        sample.setProperty("uniqueproperty");
        sample.setValue("value1");
        prefsvc.saveRecipientPreference(sample);

        // violates not null property constraint
        final RecipientPreference p1 = prefsvc.getRecipientPreference(PREF.getRecipientId(), PREF.getProperty());
        p1.setProperty(null);
        prefsvc.saveRecipientPreference(p1);
    }

    @Test(expected = DataAccessException.class)
    public void testInvalidUpdateUniqueConstraint() {
        RecipientPreference sample = new RecipientPreference();
        sample.setRecipientId("user1");
        sample.setProperty("uniqueproperty");
        sample.setValue("value1");
        prefsvc.saveRecipientPreference(sample);

        final RecipientPreference p2 = prefsvc.getRecipientPreference(PREF.getRecipientId(), PREF.getProperty());
        p2.setProperty("uniqueproperty");
        prefsvc.saveRecipientPreference(p2);
    }


    @Test
    @Override
    public void testReadByQuery() {
        RecipientPreference p = prefsvc.getRecipientPreference(PREF.getRecipientId(), PREF.getProperty());
        assertNotNull(p);
        assertEquals(PREF, p);
    }

    @Test
    @Override
    public void testUpdate() {
        RecipientPreference p = prefsvc.getRecipientPreference(PREF.getRecipientId(), PREF.getProperty());
        p.setValue("different value");

        prefsvc.saveRecipientPreference(p);

        RecipientPreference p2 = prefsvc.getRecipientPreference(PREF.getRecipientId(), PREF.getProperty());
        assertEquals(p, p2);
    }
}
