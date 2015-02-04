package org.kuali.ole.systemintegration.rest.circulation;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 3/10/14
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class JAXBContextFactory {
    private static Map<Class, JAXBContext> map = new HashMap<Class, JAXBContext>();

    public synchronized static JAXBContext getJAXBContextForClass(Class clazz) {
        if (!map.containsKey(clazz)) {
            try {
                map.put(clazz, JAXBContext.newInstance(clazz));
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        return map.get(clazz);
    }
}
