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
package org.kuali.rice.kcb.dao;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.kcb.bo.RecipientPreference;
import org.kuali.rice.kcb.test.BusinessObjectTestCase;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This class test basic persistence for the RecipientPreference business object.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RecipientPreferenceDaoTest extends BusinessObjectTestCase {
    RecipientPreference pref1 = new RecipientPreference();
    RecipientPreference pref2 = new RecipientPreference();
    
    private String[] recipientTypes = {"Type 1", "Type 2"};
    private String[] recipientIds = {"unit_test_recip1", "unit_test_recip2"};
    private String[] propertys = {"Property A", "Property B"};
    private String[] values = {"Value A", "Value B"};
    private String[] updatedValues = {"Value C", "Value D"};
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#delete()
     */
    @Test
    @Override
    public void testDelete() {
        testCreate();
        businessObjectDao.delete(pref1);
        businessObjectDao.delete(pref2);
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#retrieve()
     */
    @Test
    @Override
    public void testReadByQuery() {
        testCreate();
        
        HashMap criteria = new HashMap();
        
        criteria.put(RecipientPreference.RECIPIENT_FIELD, recipientIds[0]);
        criteria.put(RecipientPreference.PROPERTY_FIELD, propertys[0]);
        pref1 = (RecipientPreference) businessObjectDao.findByUniqueKey(RecipientPreference.class, criteria);
        
        criteria.clear();
        
        criteria.put(RecipientPreference.RECIPIENT_FIELD, recipientIds[1]);
        criteria.put(RecipientPreference.PROPERTY_FIELD, propertys[1]);
        pref2 = (RecipientPreference) businessObjectDao.findByUniqueKey(RecipientPreference.class, criteria);
        
        assertNotNull(pref1);
        assertEquals(recipientIds[0], pref1.getRecipientId());
        
        assertNotNull(pref2);
        assertEquals(recipientIds[1], pref2.getRecipientId());
    
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#insert()
     */
    @Test
    @Override
    public void testCreate() {
        pref1.setRecipientId(recipientIds[0]);
        pref1.setProperty(propertys[0]);
        pref1.setValue(values[0]);
        
        pref2.setRecipientId(recipientIds[1]);
        pref2.setProperty(propertys[1]);
        pref2.setValue(values[1]);
        
        businessObjectDao.save(pref1);
        businessObjectDao.save(pref2);
    }
    
    /**
     * @see org.kuali.rice.ken.dao.BusinessObjectPersistenceTestCaseBase#update()
     */
    @Test
    @Ignore // until I fix how this test uses test data
    @Override
    public void testUpdate() {
        testCreate();
        pref1.setValue(updatedValues[0]);
        
        pref2.setValue(updatedValues[1]);
    
        businessObjectDao.save(pref1);
        businessObjectDao.save(pref2);
        
        testReadByQuery();
        
        assertEquals(updatedValues[0], pref1.getValue());
        assertEquals(updatedValues[1], pref2.getValue());
    }
}
