package org.kuali.ole.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.kuali.ole.bo.serachRetrieve.OleSRUCirculationDocument;
import org.kuali.ole.bo.serachRetrieve.OleSRUInstanceDocument;
import org.kuali.ole.bo.serachRetrieve.OleSRUInstanceVolume;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 1/13/15
 * Time: 7:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUInstanceDocumentConverter implements Converter {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleSRUInstanceDocumentConverter.class);

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        OleSRUInstanceDocument oleSRUInstanceDocument=(OleSRUInstanceDocument)source;
        convertToEscape(oleSRUInstanceDocument);
        if (CollectionUtils.isNotEmpty(oleSRUInstanceDocument.getVolumes())) {
            for (OleSRUInstanceVolume oleSRUInstanceVolume : oleSRUInstanceDocument.getVolumes()) {
                convertToEscape(oleSRUInstanceVolume);
            }
        }
        if (CollectionUtils.isNotEmpty(oleSRUInstanceDocument.getCirculations())) {
            for (OleSRUCirculationDocument oleSRUCirculationDocument : oleSRUInstanceDocument.getCirculations()) {
                convertToEscape(oleSRUCirculationDocument);
            }
        }


    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        OleSRUInstanceDocument oleSRUInstanceDocument=new OleSRUInstanceDocument();
        return oleSRUInstanceDocument;
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(OleSRUInstanceDocument.class);
    }

    public boolean isSetter(Method method) {
        return Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(String.class) &&
                method.getParameterTypes().length == 1 &&
                method.getName().matches("^set[A-Z].*");
    }

     public ArrayList<Method> convertToEscape(Object obj) {
        Class clazz=obj.getClass();
        ArrayList<Method> list = new ArrayList<Method>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (isSetter(method)){
                try {
                    String value=(String)method.getDefaultValue();
                    value= StringEscapeUtils.unescapeXml(value);
                    method.invoke(obj,value);
                }  catch (Exception e){
                     LOG.error("Error :while converting to unescapeXml "+e);
                }
            }

        }

        return list;
    }
}
