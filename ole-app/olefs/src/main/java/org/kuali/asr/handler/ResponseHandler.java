package org.kuali.asr.handler;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 12/9/13
 * Time: 6:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResponseHandler {

    private static final Logger LOG = Logger.getLogger(ResponseHandler.class);
    public String  marshalObjectToXml(Object object){
        String response=null;
        try{
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(object.getClass());
             Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(object, writer);
            response = writer.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }


  public String marshalObjectToJson(Object object){
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonContent ="";
      try {
          jsonContent = objectMapper.writeValueAsString(object);
          System.out.println(objectMapper.writeValueAsString(object));

      } catch (Exception e) {
          e.printStackTrace();
      }
      return jsonContent;

  }

}
