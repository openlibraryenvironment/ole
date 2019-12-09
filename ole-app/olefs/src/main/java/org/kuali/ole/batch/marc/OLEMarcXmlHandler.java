package org.kuali.ole.batch.marc;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.marc4j.ErrorHandler;
import org.marc4j.MarcXmlHandler;
import org.marc4j.RecordStack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 9/2/13
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEMarcXmlHandler extends MarcXmlHandler {

    private final static String DATAFIELD = "datafield";
    private final static String SUBFIELD = "subfield";
    private static final String TAG_ATTR = "tag";
    private static final String CONTROLFIELD = "controlfield";
    private static final String LEADER = "leader";
    private static final String OO1 = "001";
    private boolean isRecordId = false;
    private String recordId;
    private String dataField;
    private String subField;
    private ErrorHandler errors;
    private String qName;
    private Attributes atts;
    private boolean elementHasErrors=false;
    private String errMsg;
    private String value;
    Logger LOG = Logger.getLogger(OLEMarcXmlHandler.class);

    /**
     * The code attribute name string
     */
    private static final String CODE_ATTR = "code";

    private RecordStack queue;

    public OLEMarcXmlHandler(RecordStack queue, ErrorHandler errors) {
        super(queue);
        this.queue = queue;
        this.errors = errors;
    }

    public void startElement(String uri, String name, String qName,
                             Attributes atts) throws SAXException {
        try {
            this.qName=qName;
            this.atts = atts;
            elementHasErrors=false;
            if (qName.equalsIgnoreCase(CONTROLFIELD) && atts.getValue(TAG_ATTR).equalsIgnoreCase(OO1)) {
                isRecordId = true;
            } else {
                isRecordId = false;
            }
            if (qName.equalsIgnoreCase(DATAFIELD)) {
                dataField = atts.getValue(TAG_ATTR);
            } else if (qName.equalsIgnoreCase(SUBFIELD)) {
                subField = atts.getValue(CODE_ATTR);
            }
            super.startElement(uri, name, qName, atts);
        } catch (Exception e) {
            elementHasErrors=true;
            errMsg=e.getMessage();
        }
    }

    public void endElement(String uri, String name, String qName)
            throws SAXException {
        try {
            super.endElement(uri, name, qName);
        } catch (Exception e) {
            setError(value);
        }

    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (isRecordId) {
            recordId = new StringBuffer().append(ch, start, length).toString().trim();
            isRecordId = false;
        }
        value = new StringBuffer().append(ch, start, length).toString().trim();
        if(elementHasErrors)
            setError(value);
    }

    public ErrorHandler getErrors() {
        return errors;
    }

    private void setError(String value){
        OLEMarcErrorHandler errorHandler = (OLEMarcErrorHandler)errors;
        if(qName.equalsIgnoreCase(LEADER)){
            errorHandler.getErrorMap().put(qName,value);
        }
        else if(qName.equalsIgnoreCase(CONTROLFIELD) || qName.equalsIgnoreCase(DATAFIELD)){
            errorHandler.getErrorMap().put(qName+" "+TAG_ATTR+"="+atts.getValue(TAG_ATTR),value);
        }
        else if(qName.equalsIgnoreCase(SUBFIELD)){
            errorHandler.getErrorMap().put(qName+" "+CODE_ATTR+"="+atts.getValue(CODE_ATTR),value);
        }
        LOG.info("Error while coverting marcxml to xml for element:: "+errorHandler.getErrorMap().get(qName));
    }

    public void endDocument() throws SAXException {
        super.endDocument();
        OLEMarcErrorHandler errorHandler = (OLEMarcErrorHandler)errors;
        if(!errorHandler.getErrorMap().isEmpty()){
            errors.addError(recordId,dataField,subField,ErrorHandler.MAJOR_ERROR,"Error occurred while converting marcxml to mrc");
        }
    }
}