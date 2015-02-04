package org.kuali.ole.docstore.discovery;

import groovy.json.JsonBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.base.BaseTestCase;
import org.kuali.ole.docstore.common.document.content.instance.Instance;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.discovery.circulation.XmlContentHandler;
import org.kuali.ole.docstore.discovery.circulation.json.CircInstance;
import org.kuali.ole.docstore.discovery.circulation.json.InstanceRecord;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/22/13
 * Time: 10:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreCricInfoRetrieveService_UT extends BaseTestCase {

    @Test
    public void testJSONStringForSimpleCollection() throws Exception {
        Person person = new Person();
        person.setName("Mock.A");
        ArrayList<String> cars = new ArrayList<String>();
        cars.add("cadiallac");
        cars.add("chevy");
        person.setCars(cars);

        JsonBuilder jsonBuilder = new JsonBuilder(person);
        String jsonString = jsonBuilder.toPrettyString();
        System.out.println(jsonString);

    }

    @Test
    public void generateJSONForCircInstance() throws Exception {
        String responseXML = FileUtils.readFileToString(new File(getClass().getResource("instancecollection-response.xml").toURI()));
        XmlContentHandler xmlContentHandler = new XmlContentHandler();
        Object unmarshalledObject = xmlContentHandler.unmarshalXMLContent(InstanceCollection.class, responseXML);
        CircInstance circInstance = new CircInstance();
        List<Instance> instanceList = ((InstanceCollection) unmarshalledObject).getInstance();
        for (Iterator<Instance> iterator = instanceList.iterator(); iterator.hasNext(); ) {
            Instance instance = iterator.next();
            ArrayList<org.kuali.ole.docstore.discovery.circulation.json.InstanceCollection> instanceCollection = new ArrayList<org.kuali.ole.docstore.discovery.circulation.json.InstanceCollection>();
            org.kuali.ole.docstore.discovery.circulation.json.InstanceCollection col1 = new org.kuali.ole.docstore.discovery.circulation.json.InstanceCollection();
            InstanceRecord instanceRecord = new InstanceRecord();
            instanceRecord.setExtension(instance.getExtension());
            instanceRecord.setResourceIdentifier(instance.getResourceIdentifier());
            instanceRecord.setSourceHoldings(instance.getSourceHoldings());
            instanceRecord.setItems(instance.getItems().getItem());
            instanceRecord.setFormerResourceIdentifier(instance.getFormerResourceIdentifier());
            instanceRecord.setInstanceIdentifier(instance.getInstanceIdentifier());
            instanceCollection.add(col1);
            circInstance.setInstanceCollection(instanceCollection);
        }
        String json = xmlContentHandler.marshalToJSON(circInstance);
        System.out.println(json);
    }

    private class Person {
        private String name;
        private List<String> cars;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setCars(List<String> cars) {
            this.cars = cars;
        }
    }
}
