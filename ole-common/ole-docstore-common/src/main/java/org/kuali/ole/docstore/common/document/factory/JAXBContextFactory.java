package org.kuali.ole.docstore.common.document.factory;

import org.kuali.ole.docstore.common.document.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rajeshbabuk on 9/13/14.
 */
public class JAXBContextFactory {

    private JAXBContextFactory() {
    }

    private static JAXBContextFactory jaxbContextFactory;

    // To transform string to xml and to use it in jaxb unmarshalling
    private static XMLInputFactory xmlInputFactory;

    private static final Map<String, JAXBContext> jaxbContextConcurrentHashMap = new ConcurrentHashMap<>();
    private static final Map<String, Marshaller> marshallerConcurrentHashMap = new ConcurrentHashMap<>();
    private static final Map<String, Unmarshaller> unmarshallerConcurrentHashMap = new ConcurrentHashMap<>();

    /**
     * This method creates one instance for the application. Returns JAXBContextFactory.
     * @return
     */
    public static synchronized JAXBContextFactory getInstance() {
        if (jaxbContextFactory == null) {
            jaxbContextFactory = new JAXBContextFactory();
        }
        return jaxbContextFactory;
    }

    /**
     * This method returns XMLInputFactory.
     * @return
     */
    public XMLInputFactory getXmlInputFactory() {
        if (xmlInputFactory == null) {
            xmlInputFactory = XMLInputFactory.newFactory();
        }
        return xmlInputFactory;
    }

    /**
     * Returns an existing Marshaller if one for the particular namespace exists,
     * else it creates an instance adds it to a internal map.
     * @param classContextPath the context path
     * @throws JAXBException exception in creating context
     * @return a created JAXBContext
     */
    public Marshaller getMarshaller(Class classContextPath) throws JAXBException {
        JAXBContext jaxbContext = jaxbContextConcurrentHashMap.get(classContextPath.getName());
        Marshaller marshaller = null;
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(classContextPath);
            jaxbContextConcurrentHashMap.put(classContextPath.getName(), jaxbContext);
            marshaller = jaxbContext.createMarshaller();
            marshallerConcurrentHashMap.put(classContextPath.getName(), marshaller);
        } else {
            marshaller = marshallerConcurrentHashMap.get(classContextPath.getName());
            if (marshaller == null) {
                marshaller = jaxbContext.createMarshaller();
                marshallerConcurrentHashMap.put(classContextPath.getName(), marshaller);
            }
        }
        return marshaller;
    }

    /**
     * Returns an existing UnMarshaller if one for the particular namespace exists,
     * else it creates an instance adds it to a internal map.
     * @param classContextPath the context path
     * @throws JAXBException exception in creating context
     * @return a created JAXBContext
     */
    public Unmarshaller getUnMarshaller(Class classContextPath) throws JAXBException {
        JAXBContext jaxbContext = jaxbContextConcurrentHashMap.get(classContextPath.getName());
        Unmarshaller unmarshaller = null;
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(classContextPath);
            jaxbContextConcurrentHashMap.put(classContextPath.getName(), jaxbContext);
            unmarshaller = jaxbContext.createUnmarshaller();
            unmarshallerConcurrentHashMap.put(classContextPath.getName(), unmarshaller);
        } else {
            unmarshaller = unmarshallerConcurrentHashMap.get(classContextPath.getName());
            if (unmarshaller == null) {
                unmarshaller = jaxbContext.createUnmarshaller();
                unmarshallerConcurrentHashMap.put(classContextPath.getName(), unmarshaller);
            }
        }
        return unmarshaller;
    }

}
