package org.kuali.ole.docstore.common.document.content.bib.marc.xstream;


import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 10/5/11
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataFieldConverter
        implements Converter {
    public static SubFieldConverter subFieldConverter = new SubFieldConverter();

    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
                        MarshallingContext marshallingContext) {
        DataField marcDataField = (DataField) o;
        hierarchicalStreamWriter.addAttribute("tag", marcDataField.getTag());
        hierarchicalStreamWriter.addAttribute("ind1", StringUtils.isEmpty(marcDataField.getInd1()) ? " " : marcDataField.getInd1());
        hierarchicalStreamWriter.addAttribute("ind2", StringUtils.isEmpty(marcDataField.getInd2()) ? " " : marcDataField.getInd2());
        List<SubField> subFields = ((DataField) o).getSubFields();
        for (SubField subField : subFields) {
            hierarchicalStreamWriter.startNode("subfield");
            marshallingContext.convertAnother(subField, subFieldConverter);
            hierarchicalStreamWriter.endNode();
        }

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {
        // System.out.println("datafield unmarshal");
        DataField marcDataField = new DataField();
        marcDataField.setTag(hierarchicalStreamReader.getAttribute("tag"));
        marcDataField.setInd1(hierarchicalStreamReader.getAttribute("ind1"));
        marcDataField.setInd2(hierarchicalStreamReader.getAttribute("ind2"));
        while (hierarchicalStreamReader.hasMoreChildren()) {
            hierarchicalStreamReader.moveDown();
            marcDataField.addSubField((SubField) unmarshallingContext
                    .convertAnother(hierarchicalStreamReader, SubField.class, subFieldConverter));
            hierarchicalStreamReader.moveUp();
        }
        return marcDataField;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(DataField.class);
    }
}
