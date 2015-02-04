package org.kuali.ole.alert.keyValue;

import org.kuali.ole.alert.bo.AlertEvent;
import org.kuali.ole.alert.bo.AlertField;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;

/**
 * Created by arunag on 8/27/14.
 */
public class AlertFieldNameValueFinder extends UifKeyValuesFinderBase {



    public List<KeyValue> getKeyValues(ViewModel viewModel) {

/*        MaintenanceDocument maintenanceDocument= ((MaintenanceDocumentForm) viewModel).getDocument();

        AlertDocument alertDocument=(AlertDocument)maintenanceDocument.getDocumentDataObject();
        Class alertDocumentClass = null;
        try {
            alertDocumentClass = Class.forName(alertDocument.getDocumentClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<KeyValue> options = new ArrayList<KeyValue>();
        Field[] fields =alertDocumentClass.getDeclaredFields();
        for(Field field:fields){
           Annotation[] anotations=field.getDeclaredAnnotations();
           for(Annotation anotation:anotations){
               if(anotation instanceof AlertField){
                   options.add(new ConcreteKeyValue(field.getName(), field.getName()));
               }
           }


       }

*//*
        List<ItemRecord> itemRecords = (List<ItemRecord>) getDataObjectService().findMatching(ItemRecord.class, QueryByCriteria.Builder.andAttributes(getHoldingsMap(id)).build()).getResults();

*//*


        return options;*/


        MaintenanceDocument maintenanceDocument= ((MaintenanceDocumentForm) viewModel).getDocument();

        AlertEvent alertEvent=(AlertEvent)maintenanceDocument.getDocumentDataObject();
        Class alertDocumentClass = null;
        try {
            alertDocumentClass = Class.forName(alertEvent.getAlertDocumentClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<KeyValue> options = new ArrayList<KeyValue>();
        Field[] fields =alertDocumentClass.getDeclaredFields();
        for(Field field:fields){
            Annotation[] anotations=field.getDeclaredAnnotations();
            for(Annotation anotation:anotations){
                if(anotation instanceof AlertField){
                    options.add(new ConcreteKeyValue(field.getName(), field.getName()));
                }
            }


        }
        alertEvent.getAlertEventFieldList();

        return options;

    }
}
