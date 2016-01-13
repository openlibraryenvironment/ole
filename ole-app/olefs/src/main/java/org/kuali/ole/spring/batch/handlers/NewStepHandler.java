package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.lang3.StringUtils;
import org.marc4j.marc.DataField;
import org.marc4j.marc.MarcFactory;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import java.util.StringTokenizer;

/**
 * Created by pvsubrah on 12/22/15.
 */
public class NewStepHandler extends StepHandler {
    /*Eg of source field -  001 or 050 $a$b$c or any thing.
    Eg of destination field - 035 $a$b,040 $c$b or anything.
       */
    @Override
    public void processSteps(Record marcRecord) {
        String value = getBatchProfileDataTransformer().getConstant();
        String destinationFieldString = getBatchProfileDataTransformer().getDestinationField();
        StringTokenizer destinationFieldTokenizer = new StringTokenizer(destinationFieldString,",");

        while(destinationFieldTokenizer.hasMoreTokens()){
            String destination = destinationFieldTokenizer.nextToken();
            String destinationArray[] = destination.split("[' ']");
            String destinationField = destinationArray[0];
            String destinationSubField = (destinationArray.length > 1 ?  destinationArray[1] : "");
            if (StringUtils.isNotBlank(destinationField)) {
                if (!getMarcRecordUtil().isControlField(destinationField)) {
                    getMarcRecordUtil().addDataField(marcRecord, destinationField, destinationSubField, value);
                } else {
                    getMarcRecordUtil().addControlField(marcRecord, destinationField,  value);
                }
            }
        }
    }

    @Override
    public Boolean isInterested(String operation) {
        return operation.equalsIgnoreCase("New");

    }
}
