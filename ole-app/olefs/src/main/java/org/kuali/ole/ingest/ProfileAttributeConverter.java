package org.kuali.ole.ingest;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.ingest.pojo.ProfileAttributeBo;

/**
 * ProfileAttributeConverter converts the data into profileAttributeBo Object
 */
public class ProfileAttributeConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {

    }

    /**
     *   This method converts the data into profileAttributeBo Object.
     * @param hierarchicalStreamReader
     * @param unmarshallingContext
     * @return  profileAttributeBo
     */
    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        ProfileAttributeBo profileAttributeBo = new ProfileAttributeBo();
        profileAttributeBo.setAttributeName(hierarchicalStreamReader.getAttribute("name"));
        profileAttributeBo.setSystemValue(hierarchicalStreamReader.getAttribute("system"));
        profileAttributeBo.setAttributeValue(hierarchicalStreamReader.getValue());
        return profileAttributeBo;
    }

    /**
     *  This method returns True/False.
     *  This method checks whether the class is ProfileAttributeBo class or not.
     * @param aClass
     * @return  boolean
     */
    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(ProfileAttributeBo.class);
    }
}
