package org.kuali.ole;

import org.kuali.ole.pojo.OLESruItem;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 4/17/14
 * Time: 1:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESruItemHandler {

    public String generateXmlFromObject(Object object){
        StringWriter stringWriter = new StringWriter();
        try{
        JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        marshaller.marshal(object,stringWriter);
        }catch(Exception e){
           e.printStackTrace();
        }
       return stringWriter.toString();
    }


    public Object getObjectFromXml(String xmlContent,Object requiredObject){
        Object object = null;
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(requiredObject.getClass());
            Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
        object =    unMarshaller.unmarshal(new StringReader(xmlContent));
        }catch(Exception e){
              e.printStackTrace();
        }
        return object;

    }

}
