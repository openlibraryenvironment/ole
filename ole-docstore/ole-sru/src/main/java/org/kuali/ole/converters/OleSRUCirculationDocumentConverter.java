package org.kuali.ole.converters;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.bo.explain.OleSRUExplainDatabaseTitle;
import org.kuali.ole.bo.serachRetrieve.OleSRUCirculationDocument;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 9/13/13
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUCirculationDocumentConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
        OleSRUCirculationDocument oleSRUCirculationDocument = (OleSRUCirculationDocument) o;
        if(oleSRUCirculationDocument.getAvailableNow()!=null){
            hierarchicalStreamWriter.startNode("availableNow");
            hierarchicalStreamWriter.addAttribute("value",oleSRUCirculationDocument.getAvailableNow());
            hierarchicalStreamWriter.endNode();
        }
        if(oleSRUCirculationDocument.getAvailabilityDate()!=null){
            hierarchicalStreamWriter.startNode("availabilityDate");
            hierarchicalStreamWriter.setValue(oleSRUCirculationDocument.getAvailabilityDate());
            hierarchicalStreamWriter.endNode();
        }
        if(oleSRUCirculationDocument.getAvailableThru()!=null) {
            hierarchicalStreamWriter.startNode("availableThru");
            hierarchicalStreamWriter.setValue(oleSRUCirculationDocument.getAvailableThru());
            hierarchicalStreamWriter.endNode();
        }
        if(oleSRUCirculationDocument.getRestrictions()!=null){
            hierarchicalStreamWriter.startNode("restrictions");
            hierarchicalStreamWriter.setValue(oleSRUCirculationDocument.getRestrictions());
            hierarchicalStreamWriter.endNode();
        }
        if(oleSRUCirculationDocument.getItemId()!=null){
        hierarchicalStreamWriter.startNode("itemId");
        hierarchicalStreamWriter.setValue(oleSRUCirculationDocument.getItemId());
        hierarchicalStreamWriter.endNode();
        }

        if(oleSRUCirculationDocument.getRenewable()!=null){
        hierarchicalStreamWriter.startNode("renewable");
        hierarchicalStreamWriter.addAttribute("value",oleSRUCirculationDocument.getRenewable());
        hierarchicalStreamWriter.endNode();
        }if(oleSRUCirculationDocument.getOnHold()!=null){
        hierarchicalStreamWriter.startNode("onHold");
        hierarchicalStreamWriter.addAttribute("value",oleSRUCirculationDocument.getOnHold());
        hierarchicalStreamWriter.endNode();
        }
        if(oleSRUCirculationDocument.getEnumAndChron()!=null){
        hierarchicalStreamWriter.startNode("enumAndChron");
        hierarchicalStreamWriter.setValue(oleSRUCirculationDocument.getEnumAndChron());
        hierarchicalStreamWriter.endNode();
        }if(oleSRUCirculationDocument.getMidspine()!=null){
        hierarchicalStreamWriter.startNode("midspine");
        hierarchicalStreamWriter.setValue(oleSRUCirculationDocument.getMidspine());
        hierarchicalStreamWriter.endNode();
        }
        if(oleSRUCirculationDocument.getTemporaryLocation()!=null){
        hierarchicalStreamWriter.startNode("temporaryLocation");
        hierarchicalStreamWriter.setValue(oleSRUCirculationDocument.getTemporaryLocation());
        hierarchicalStreamWriter.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        OleSRUCirculationDocument oleSRUCirculationDocument = new OleSRUCirculationDocument();
      //  oleSRUCirculationDocument.setAvailableNow(hierarchicalStreamReader.getAttribute("value"));
        return oleSRUCirculationDocument;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(OleSRUCirculationDocument.class);
    }

}
