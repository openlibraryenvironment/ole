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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arunag on 8/27/14.
 */
public class AlertFieldValueFinder extends UifKeyValuesFinderBase {



    public List<KeyValue> getKeyValues(ViewModel viewModel) {

/*        MaintenanceDocument maintenanceDocument= ((MaintenanceDocumentForm) viewModel).getDocument();
        AlertDocument alertDocument=(AlertDocument)maintenanceDocument.getDocumentDataObject();
        List<KeyValue> options = new ArrayList<KeyValue>();
        Class alertDocumentClass = null;
        try {
            alertDocumentClass = Class.forName(alertDocument.getDocumentClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Field[] declaredFields =alertDocumentClass.getDeclaredFields();
        for(Field field:declaredFields){
            Annotation[] anotations=field.getDeclaredAnnotations();
            for(Annotation anotation:anotations){
                if(anotation instanceof AlertField){
                    String sdd=field.getType().toString().replace(".", "-");
                    String[] value = sdd.split("-");
                    String lastOne = value[value.length-1];
                    options.add(new ConcreteKeyValue(lastOne, lastOne));
                }
            }
        }




        return options;*/

        MaintenanceDocument maintenanceDocument= ((MaintenanceDocumentForm) viewModel).getDocument();
        AlertEvent alertEvent=(AlertEvent)maintenanceDocument.getDocumentDataObject();
        List<KeyValue> options = new ArrayList<KeyValue>();
        Class alertDocumentClass = null;
        try {
            alertDocumentClass = Class.forName(alertEvent.getAlertDocumentClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Field[] declaredFields =alertDocumentClass.getDeclaredFields();
        for(Field field:declaredFields){
            Annotation[] annotations=field.getDeclaredAnnotations();
            for(Annotation annotation:annotations){
                if(annotation instanceof AlertField){
                    String sdd=field.getType().toString().replace(".", "-");
                    String[] value = sdd.split("-");
                    String lastOne = value[value.length-1];
                    options.add(new ConcreteKeyValue(lastOne, lastOne));
                }
            }
        }




        return options;
    }
}
