package org.kuali.ole.ingest;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.ingest.pojo.OleAddressLine;

/**
 * OlePatronAddressLineConverter is a converter class used to convert object into data and data into object by using
 * hierarchicalStreamWriter and hierarchicalStreamReader respectively
 */
public class OlePatronAddressLineConverter implements Converter {
    /**
     * This method convert the object into data by using hierarchicalStreamWriter.
     * @param obj
     * @param hierarchicalStreamWriter
     * @param marshallingContext
     */
        @Override
        public void marshal(Object obj, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
            OleAddressLine oleAddressLine = (OleAddressLine) obj;
            hierarchicalStreamWriter.setValue(oleAddressLine.getAddressLine());
        }

    /**
     *  This method convert the data into object by using hierarchicalStreamReader.
     * @param hierarchicalStreamReader
     * @param unmarshallingContext
     * @return  oleAddressLine.
     */
        @Override
        public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
            OleAddressLine oleAddressLine = new OleAddressLine();
            oleAddressLine.setAddressLine(hierarchicalStreamReader.getValue());
            return oleAddressLine;
        }

    /**
     *  This method returns True/False.
     *  This method check whether the class is OleAddressLine class or not.
     * @param aClass
     * @return  boolean
     */
        @Override
        public boolean canConvert(Class aClass) {
            return aClass.equals(OleAddressLine.class);
        }
}
