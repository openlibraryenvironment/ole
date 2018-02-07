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
package org.kuali.rice.kew.docsearch.service.impl

import org.apache.commons.lang.RandomStringUtils
import org.apache.commons.lang.SerializationUtils
import org.apache.commons.lang.time.StopWatch
import org.joda.time.DateTime
import org.junit.Test
import org.kuali.rice.kew.api.document.DocumentStatus
import org.kuali.rice.kew.api.document.DocumentStatusCategory
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria
import org.kuali.rice.kew.api.document.search.RouteNodeLookupLogic
import org.kuali.rice.kew.docsearch.DocumentSearchInternalUtils

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller

import static org.junit.Assert.assertEquals

/**
 * Tests DocumentSearchCriteria marshalling and performance
 */
class DocumentSearchCriteriaTest {

    @Test
    public void test_Xml_Marshal_Unmarshal() {
        // DateTimeAdapter ensures DateTimes are marshalled appropriately
        DocumentSearchCriteria c = createWithoutDocAttribs("name")
        assertEquals(c, unmarshalJAXB(marshalJAXB(c)))

        // test w/ full object including attributes
        c = create("name");
        assertEquals(c, unmarshalJAXB(marshalJAXB(c)));
    }

    @Test
    void testJSONMarshalling() {
        DocumentSearchCriteria c = create("name")
        def s = DocumentSearchInternalUtils.marshalDocumentSearchCriteria(c)
        def d = DocumentSearchInternalUtils.unmarshalDocumentSearchCriteria(s)
        assertEquals(c, d)
    }

    @Test
    void testPerformance() {
        def strats = [
            jaxb: [
                marshal: { marshalJAXB(it) },
                unmarshal: { unmarshalJAXB(it) }
            ],
            serialization: [
                marshal: { SerializationUtils.serialize(it) },
                unmarshal: { SerializationUtils.deserialize(it) }
            ],
            json: [
                marshal: {
                    DocumentSearchInternalUtils.marshalDocumentSearchCriteria(it)
                },
                unmarshal: {
                    DocumentSearchInternalUtils.unmarshalDocumentSearchCriteria(it)
                }
            ]
        ]

        for (key in strats.keySet()) {
            println("Strategy: " + key)
            def strat = strats[key]

            def sw = new StopWatch()
            sw.start()
            def criterias = []
            for (i in 1..1000) {
              criterias << create(RandomStringUtils.randomAlphanumeric(100))
            }
            println("Build time: " + sw + " " + (sw.getTime() / 1000) + "ms/object")
            sw.reset()
            sw.start()
            def marshalled = []
            for (c in criterias) {
                marshalled << strat['marshal'].call(c)
            }
            //println marshalled[0]
            println("Marshal time: " + sw + " " + (sw.getTime() / 1000) + "ms/object")
            sw.reset()
            criterias.clear()
            sw.start()
            for (string in marshalled) {
                criterias << strat['unmarshal'].call(string)
            }
            println("Unmarshal time: " + sw + " " + (sw.getTime() / 1000) + "ms/object")
        }
    }

    protected static String marshalJAXB(DocumentSearchCriteria criteria) {
        StringWriter marshalledCriteriaWriter = new StringWriter()
        JAXBContext jaxbContext = JAXBContext.newInstance(DocumentSearchCriteria.class)
        Marshaller marshaller = jaxbContext.createMarshaller()
        marshaller.marshal(criteria, marshalledCriteriaWriter)
        marshalledCriteriaWriter.toString()
    }

    protected static DocumentSearchCriteria unmarshalJAXB(String s) {
        JAXBContext jaxbContext = JAXBContext.newInstance(DocumentSearchCriteria.class)
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller()
        return unmarshaller.unmarshal(new StringReader(s))
    }

    protected static DocumentSearchCriteria.Builder createBare(String saveName = null) {
        def builder = DocumentSearchCriteria.Builder.create()
        builder.applicationDocumentId = RandomStringUtils.randomAlphanumeric(5)
        builder.applicationDocumentStatus = RandomStringUtils.randomAlphanumeric(5)
        builder.approverPrincipalName = RandomStringUtils.randomAlphanumeric(5)
        builder.approverPrincipalId = RandomStringUtils.randomAlphanumeric(5)
        builder.documentId = RandomStringUtils.randomAlphanumeric(5)
        builder.documentTypeName = RandomStringUtils.randomAlphanumeric(5)
        builder.documentStatusCategories = Arrays.asList([ DocumentStatusCategory.PENDING, DocumentStatusCategory.SUCCESSFUL ] as DocumentStatusCategory[])
        builder.documentStatuses = Arrays.asList([ DocumentStatus.ENROUTE, DocumentStatus.INITIATED, DocumentStatus.SAVED ] as DocumentStatus[])
        builder.initiatorPrincipalName = RandomStringUtils.randomAlphanumeric(10)
        builder.initiatorPrincipalId = RandomStringUtils.randomAlphanumeric(10)
        builder.maxResults = 500
        builder.routeNodeName = RandomStringUtils.randomAlphanumeric(5)
        builder.saveName = saveName
        builder.startAtIndex = 1
        builder.title = RandomStringUtils.randomAlphanumeric(10)
        builder.groupViewerId = RandomStringUtils.randomAlphanumeric(5)
        builder.viewerPrincipalName = RandomStringUtils.randomAlphanumeric(10)
        builder.viewerPrincipalId = RandomStringUtils.randomAlphanumeric(10)
        builder.routeNodeLookupLogic = RouteNodeLookupLogic.EXACTLY
        return builder
    }

    protected static void addDocAttribs(DocumentSearchCriteria.Builder builder) {
        // TODO: FIXME: MultiValuedStringMapAdapter unmarshal not implemented
        Map<String, List<String>> attrs = new HashMap<String, List<String>>()
        for (i in 1..5) {
            def list = new ArrayList(5)
            for (j in 1..5) {
                list.add(RandomStringUtils.randomAlphanumeric(5))
            }
            attrs.put(RandomStringUtils.randomAlphanumeric(5), list)
        }
        builder.documentAttributeValues = attrs
    }

    protected static void addDates(DocumentSearchCriteria.Builder builder) {
        builder.dateApplicationDocumentStatusChangedFrom = new DateTime()
        builder.dateApplicationDocumentStatusChangedTo = new DateTime()
        builder.dateApprovedFrom = new DateTime()
        builder.dateApprovedTo = new DateTime()
        builder.dateCreatedFrom = new DateTime()
        builder.dateCreatedTo = new DateTime()
        builder.dateFinalizedFrom = new DateTime()
        builder.dateFinalizedTo = new DateTime()
        builder.dateLastModifiedFrom = new DateTime()
        builder.dateLastModifiedTo = new DateTime()
    }

    protected static DocumentSearchCriteria createWithoutDocAttribs(String saveName = null) {
        def builder = createBare(saveName)
        addDates(builder)
        return builder.build()
    }

    protected static DocumentSearchCriteria createWithoutDates(String saveName = null) {
        def builder = createBare(saveName)
        addDocAttribs(builder)
        return builder.build()
    }

    protected static DocumentSearchCriteria create(String saveName = null) {
        def builder = createBare(saveName)
        addDates(builder)
        addDocAttribs(builder)
        return builder.build()
    }
}
