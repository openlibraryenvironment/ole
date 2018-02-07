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
package org.kuali.rice.ken.dao;

import org.junit.Test;
import org.kuali.rice.ken.bo.NotificationProducerBo;
import org.kuali.rice.ken.test.TestConstants;
import org.springframework.dao.DataAccessException;

import java.util.*;

import static org.junit.Assert.*;

/**
 * This class tests the various methods offered up by the BusinessObjectDao.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BusinessObjectDaoTest extends BusinessObjectDaoTestCaseBase {
    private static final Map<Long, NotificationProducerBo> producers = new HashMap<Long, NotificationProducerBo>();
    static {
        producers.put(TestConstants.PRODUCER_1.getId(), TestConstants.PRODUCER_1);
        producers.put(TestConstants.PRODUCER_2.getId(), TestConstants.PRODUCER_2);
    }
    private static final NotificationProducerBo[] producerOrder = new NotificationProducerBo[] { TestConstants.PRODUCER_1, TestConstants.PRODUCER_2 };

    @Test
    public void testFindByPrimaryKey() {
	Map primaryKeys = new HashMap();
	primaryKeys.put("id", TestConstants.PRODUCER_2.getId());
	NotificationProducerBo
            notificationProducer = (NotificationProducerBo)businessObjectDao.findByPrimaryKey(NotificationProducerBo.class, primaryKeys);
   	assertEquals(TestConstants.PRODUCER_2.getId().longValue(), notificationProducer.getId().longValue());
	assertEquals(TestConstants.PRODUCER_2.getName(), notificationProducer.getName());
	assertEquals(TestConstants.PRODUCER_2.getDescription(), notificationProducer.getDescription());
	assertEquals(TestConstants.PRODUCER_2.getContactInfo(), notificationProducer.getContactInfo());
    }
    
    @Test
    public void testFindByUniqueKey() {
	Map uniqueKeys = new HashMap();
	uniqueKeys.put("name", TestConstants.PRODUCER_2.getName());
	NotificationProducerBo
            notificationProducer = (NotificationProducerBo)businessObjectDao.findByPrimaryKey(NotificationProducerBo.class, uniqueKeys);
	assertEquals(TestConstants.PRODUCER_2.getId().longValue(), notificationProducer.getId().longValue());
	assertEquals(TestConstants.PRODUCER_2.getName(), notificationProducer.getName());
	assertEquals(TestConstants.PRODUCER_2.getDescription(), notificationProducer.getDescription());
	assertEquals(TestConstants.PRODUCER_2.getContactInfo(), notificationProducer.getContactInfo());
    }

    @Test
    public void testFindAll() {
	Collection notificationProducers = businessObjectDao.findAll(NotificationProducerBo.class);
	assertEquals(5, notificationProducers.size());
    }

    @Test
    public void testFindAllOrderBy() {
	// test ascending order
	Collection notificationProducers = businessObjectDao.findAllOrderBy(NotificationProducerBo.class, "id", true);
	assertEquals(TestConstants.NUM_TEST_PRODUCERS, notificationProducers.size());

	// test descending order
	notificationProducers = businessObjectDao.findAllOrderBy(NotificationProducerBo.class, "id", false);
        assertEquals(TestConstants.NUM_TEST_PRODUCERS, notificationProducers.size());
    }

    @Test
    public void testFindMatching() {
	Map fieldValues = new HashMap();
	fieldValues.put("contactInfo", TestConstants.PRODUCER_1.getContactInfo());
	Collection notificationProducers = businessObjectDao.findMatching(NotificationProducerBo.class, fieldValues);
	assertEquals(2, notificationProducers.size());
	Iterator it = notificationProducers.iterator();
	while (it.hasNext()) {
	    NotificationProducerBo producer = (NotificationProducerBo)it.next();
            NotificationProducerBo expected = producers.get(producer.getId());
	    assertEquals(expected.getName(), producer.getName());
            assertEquals(expected.getDescription(), producer.getDescription());
            assertEquals(expected.getContactInfo(), producer.getContactInfo());
	}
    }

    @Test
    public void testCountMatchingClassMap() {
	Map fieldValues = new HashMap();
	fieldValues.put("contactInfo", TestConstants.CONTACT_INFO_2_PRODUCERS);
	assertEquals(2, businessObjectDao.countMatching(NotificationProducerBo.class, fieldValues));
	
	fieldValues = new HashMap();
	fieldValues.put("contactInfo", TestConstants.CONTACT_INFO_1_PRODUCER);
	assertEquals(1, businessObjectDao.countMatching(NotificationProducerBo.class, fieldValues));

	fieldValues = new HashMap();
	fieldValues.put("id", TestConstants.PRODUCER_2.getId());
	assertEquals(1, businessObjectDao.countMatching(NotificationProducerBo.class, fieldValues));

	fieldValues = new HashMap();
	fieldValues.put("description", "xxxx");
	assertEquals(0, businessObjectDao.countMatching(NotificationProducerBo.class, fieldValues));
    }

    @Test
    public void testCountMatchingClassMapMap() {
        // 2 producers with the same contact info
        // exclude 1 of them
        // 1 left
	Map positiveFieldValues = new HashMap();
	positiveFieldValues.put("contactInfo", TestConstants.PRODUCER_1.getContactInfo());
	Map negativeFieldValues = new HashMap();
	negativeFieldValues.put("id", TestConstants.PRODUCER_2.getId());
	assertEquals(1, businessObjectDao.countMatching(NotificationProducerBo.class, positiveFieldValues, negativeFieldValues));

        // 1 matching producer, exclude it, 0 left
	positiveFieldValues = new HashMap();
	positiveFieldValues.put("contactInfo", TestConstants.PRODUCER_3.getContactInfo());
	negativeFieldValues = new HashMap();
	negativeFieldValues.put("id", TestConstants.PRODUCER_3.getId());
	assertEquals(0, businessObjectDao.countMatching(NotificationProducerBo.class, positiveFieldValues, negativeFieldValues));

        // 1 matching producer, exclude a non-match, 1 left 
	positiveFieldValues = new HashMap();
	positiveFieldValues.put("contactInfo", TestConstants.PRODUCER_3.getContactInfo());
	negativeFieldValues = new HashMap();
	negativeFieldValues.put("id", TestConstants.PRODUCER_2.getId());
	assertEquals(1, businessObjectDao.countMatching(NotificationProducerBo.class, positiveFieldValues, negativeFieldValues));
    }

    @Test
    public void testFindMatchingOrderBy() {
	Map fieldValues = new HashMap();
	fieldValues.put("contactInfo", TestConstants.CONTACT_INFO_2_PRODUCERS);

	// test ascending order
	Collection notificationProducers = businessObjectDao.findMatchingOrderBy(NotificationProducerBo.class, fieldValues, "id", true);
	assertEquals(2, notificationProducers.size());
	Iterator it = notificationProducers.iterator();
	int i=1;
	while (it.hasNext()) {
	    NotificationProducerBo producer = (NotificationProducerBo)it.next();
            NotificationProducerBo expected = producerOrder[(i - 1)];
	    long id = producer.getId().longValue();
	    assertEquals(expected.getId().longValue(), id);
	    assertEquals(expected.getName(), producer.getName());
            assertEquals(expected.getDescription(), producer.getDescription());
            assertEquals(expected.getContactInfo(), producer.getContactInfo());
            i++;
	}

	// test descending order
	notificationProducers = businessObjectDao.findMatchingOrderBy(NotificationProducerBo.class, fieldValues, "id", false);
	assertEquals(2, notificationProducers.size());
	it = notificationProducers.iterator();
	i=2;
	while (it.hasNext()) {
	    NotificationProducerBo producer = (NotificationProducerBo)it.next();
            NotificationProducerBo expected = producerOrder[(i - 1)];
	    long id = producer.getId().longValue();
	    assertEquals(expected.getId().longValue(), id);
	    assertEquals(expected.getName(), producer.getName());
            assertEquals(expected.getDescription(), producer.getDescription());
            assertEquals(expected.getContactInfo(), producer.getContactInfo());
            i--;
	}
    }

    @Test
    public void testSaveObject() {
	NotificationProducerBo notificationProducer = new NotificationProducerBo();
	notificationProducer.setName("TestNotificationProducer");
	notificationProducer.setDescription("Notification Producer for Unit Tests");
	notificationProducer.setContactInfo("bh79@cornell.edu");

	assertNull(notificationProducer.getId());
	businessObjectDao.save(notificationProducer);
	assertNotNull(notificationProducer.getId());
	
	Map primaryKeys = new HashMap();
	primaryKeys.put("id", notificationProducer.getId());
	NotificationProducerBo saved = (NotificationProducerBo)businessObjectDao.findByPrimaryKey(NotificationProducerBo.class, primaryKeys);
	assertEquals(notificationProducer.getId().longValue(), saved.getId().longValue());
	assertEquals(notificationProducer.getName(), saved.getName());
	assertEquals(notificationProducer.getDescription(), saved.getDescription());
	assertEquals(notificationProducer.getContactInfo(), saved.getContactInfo());
    }

    @Test
    public void testSaveList() {
	List notificationProducers = new ArrayList(2);
	
	NotificationProducerBo notificationProducer = new NotificationProducerBo();
	notificationProducer.setName("TestNotificationProducer");
	notificationProducer.setDescription("Notification Producer for Unit Tests");
	notificationProducer.setContactInfo("bh79@cornell.edu");

	NotificationProducerBo notificationProducer2 = new NotificationProducerBo();
	notificationProducer2.setName("TestNotificationProducer2");
	notificationProducer2.setDescription("Notification Producer for Unit Tests 2");
	notificationProducer2.setContactInfo("bh79@cornell.edu");

	notificationProducers.add(notificationProducer);
	notificationProducers.add(notificationProducer2);
	
	assertNull(notificationProducer.getId());
	assertNull(notificationProducer.getId());
	businessObjectDao.save(notificationProducers);
	assertNotNull(notificationProducer.getId());
	assertNotNull(notificationProducer2.getId());
	
	Map primaryKeys = new HashMap();
	primaryKeys.put("id", notificationProducer.getId());
	NotificationProducerBo saved = (NotificationProducerBo)businessObjectDao.findByPrimaryKey(NotificationProducerBo.class, primaryKeys);
	assertEquals(notificationProducer.getId().longValue(), saved.getId().longValue());
	assertEquals(notificationProducer.getName(), saved.getName());
	assertEquals(notificationProducer.getDescription(), saved.getDescription());
	assertEquals(notificationProducer.getContactInfo(), saved.getContactInfo());

	primaryKeys = new HashMap();
	primaryKeys.put("id", notificationProducer2.getId());
	saved = (NotificationProducerBo)businessObjectDao.findByPrimaryKey(NotificationProducerBo.class, primaryKeys);
	assertEquals(notificationProducer2.getId().longValue(), saved.getId().longValue());
	assertEquals(notificationProducer2.getName(), saved.getName());
	assertEquals(notificationProducer2.getDescription(), saved.getDescription());
	assertEquals(notificationProducer2.getContactInfo(), saved.getContactInfo());
    }

    @Test
    public void testSaveListViolateConstraint() {
        String exceptionDescription = null;
        try {
            List notificationProducers = new ArrayList(2);

            NotificationProducerBo notificationProducer = new NotificationProducerBo();
            notificationProducer.setName("TestNotificationProducer");
            notificationProducer.setDescription("Notification Producer for Unit Tests");
            notificationProducer.setContactInfo("bh79@cornell.edu");

            NotificationProducerBo notificationProducer2 = new NotificationProducerBo();
            notificationProducer2.setName("TestNotificationProducer");
            notificationProducer2.setDescription("Notification Producer for Unit Tests");
            notificationProducer2.setContactInfo("bh79@cornell.edu");

            notificationProducers.add(notificationProducer);
            notificationProducers.add(notificationProducer2);

            assertNull(notificationProducer.getId());
            assertNull(notificationProducer.getId());
            businessObjectDao.save(notificationProducers);
            
            fail("No exception was thrown; expected constraint violation");
        } catch (DataAccessException e) {
        }

    }

    @Test
    public void testDeleteObject() {
	Map primaryKeys = new HashMap();
	primaryKeys.put("id", TestConstants.PRODUCER_2.getId());
	NotificationProducerBo
            notificationProducer = (NotificationProducerBo)businessObjectDao.findByPrimaryKey(NotificationProducerBo.class, primaryKeys);
	businessObjectDao.delete(notificationProducer);

	NotificationProducerBo saved = (NotificationProducerBo)businessObjectDao.findByPrimaryKey(NotificationProducerBo.class, primaryKeys);
	assertNull(saved);

	Map fieldValues = new HashMap();
	fieldValues.put("contactInfo", TestConstants.CONTACT_INFO_2_PRODUCERS);
	assertEquals(1, businessObjectDao.countMatching(NotificationProducerBo.class, fieldValues));
    }

    @Test
    public void testDeleteListOfObject() {
	Map fieldValues = new HashMap();
	fieldValues.put("contactInfo", TestConstants.CONTACT_INFO_2_PRODUCERS);
	assertEquals(2, businessObjectDao.countMatching(NotificationProducerBo.class, fieldValues));

	Collection notificationProducers = businessObjectDao.findMatching(NotificationProducerBo.class, fieldValues);
	ArrayList listToDelete = new ArrayList(notificationProducers);
	businessObjectDao.delete(listToDelete);

	assertEquals(0, businessObjectDao.countMatching(NotificationProducerBo.class, fieldValues));
    }

    @Test
    public void testDeleteMatching() {
	Map fieldValues = new HashMap();
	fieldValues.put("contactInfo", TestConstants.CONTACT_INFO_2_PRODUCERS);
	assertEquals(2, businessObjectDao.countMatching(NotificationProducerBo.class, fieldValues));
	
	businessObjectDao.deleteMatching(NotificationProducerBo.class, fieldValues);

	assertEquals(0, businessObjectDao.countMatching(NotificationProducerBo.class, fieldValues));
    }

    @Test
    public void testRetrieve() {
	NotificationProducerBo template = new NotificationProducerBo();
	template.setId(TestConstants.PRODUCER_2.getId());

	NotificationProducerBo notificationProducer = (NotificationProducerBo) businessObjectDao.retrieve(template);
	assertEquals(TestConstants.PRODUCER_2.getId().longValue(), notificationProducer.getId().longValue());
	assertEquals(TestConstants.PRODUCER_2.getName(), notificationProducer.getName());
	assertEquals(TestConstants.PRODUCER_2.getDescription(), notificationProducer.getDescription());
	assertEquals(TestConstants.PRODUCER_2.getContactInfo(), notificationProducer.getContactInfo());
    }

    @Test
    public void testFindMatchingByExample() {
	NotificationProducerBo template = new NotificationProducerBo();
	template.setName(TestConstants.PRODUCER_1.getName());
	
	Collection producers = businessObjectDao.findMatchingByExample(template);
	
	assertEquals(producers.size(), 1);
    }
}
