package org.kuali.ole.docstore.model.xstream.work.bib.marc;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/30/12
 * Time: 7:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkBibMarcRecordConverter
        implements Converter {

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        WorkBibMarcRecord workBibMarcRecord = (WorkBibMarcRecord) source;
        if (workBibMarcRecord != null) {
            writer.startNode("collection");
            writer.startNode("record");
            if (workBibMarcRecord.getLeader() != null && workBibMarcRecord.getLeader().length() > 0) {
                writer.startNode("leader");
                writer.setValue(workBibMarcRecord.getLeader());
                writer.endNode();
            }
            List<ControlField> controlFieldList = workBibMarcRecord.getControlFields();

            for (ControlField controlField : controlFieldList) {
                writer.startNode("controlfield");
                context.convertAnother(controlField, new ControlFieldConverter());
                writer.endNode();
            }
            List<DataField> dataFieldList = workBibMarcRecord.getDataFields();
            for (DataField dataField : dataFieldList) {
                writer.startNode("datafield");
                context.convertAnother(dataField, new DataFieldConverter());
                writer.endNode();
            }
            writer.endNode();
            writer.endNode();

        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        WorkBibMarcRecord workBibMarcRecord = new WorkBibMarcRecord();
        reader.moveDown();
        reader.moveDown();
        workBibMarcRecord.setLeader(reader.getValue());
        System.out.println(reader.hasMoreChildren());
        reader.moveUp();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            if (reader.getNodeName().equals("controlfield")) {
                workBibMarcRecord.addControlFields(
                        (ControlField) context.convertAnother(reader, ControlField.class, new ControlFieldConverter()));
            } else if (reader.getNodeName().equals("datafield")) {
                workBibMarcRecord.addDataFields(
                        (DataField) context.convertAnother(reader, DataField.class, new DataFieldConverter()));
            }
            reader.moveUp();
        }
        reader.moveUp();
        return workBibMarcRecord;
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(WorkBibMarcRecord.class);
    }
}
