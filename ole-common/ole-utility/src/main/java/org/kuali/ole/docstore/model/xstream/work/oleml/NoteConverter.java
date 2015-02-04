package org.kuali.ole.docstore.model.xstream.work.oleml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Note;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/28/12
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoteConverter
        implements Converter {

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Note note = (Note) source;
        if (note.getType() != null) {
            writer.addAttribute("type", note.getType());
        }
        if (note.getValue() != null) {
            writer.setValue(note.getValue());
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Note note = new Note();
        note.setType(reader.getAttribute("type"));
        note.setValue(reader.getValue());
        return note;
    }

    @Override
    public boolean canConvert(Class noteClass) {
        return noteClass.equals(Note.class);
    }
}
