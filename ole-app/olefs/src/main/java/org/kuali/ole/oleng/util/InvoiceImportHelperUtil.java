package org.kuali.ole.oleng.util;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLETranscationalRecordGenerator;
import org.kuali.ole.converter.OLEINVConverter;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.pojo.edi.INVOrder;
import org.kuali.ole.pojo.edi.INVOrders;
import org.kuali.ole.vnd.businessobject.OleCurrencyType;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 1/27/2016.
 */
public class InvoiceImportHelperUtil extends BusinessObjectServiceHelperUtil {

    private OLEINVConverter oleInvConverter;
    private OLETranscationalRecordGenerator oleTranscationalRecordGenerator;

    public String getCurrencyTypeIdByCurrencyType(String currencyType){
        Map<String,String> currencyTypeMap = new HashMap<>();
        currencyTypeMap.put(org.kuali.ole.OLEConstants.OLEBatchProcess.CURRENCY_TYPE,currencyType);
        List<OleCurrencyType> currencyTypeList = (List) getBusinessObjectService().findMatching(OleCurrencyType.class, currencyTypeMap);
        return currencyTypeList.get(0).getCurrencyTypeId().toString();
    }

    public List<INVOrder> readEDIContent(String rawContent) {
        List<INVOrder> invOrders =new ArrayList<>();
        if (StringUtils.isNotBlank(rawContent)) {
            try {
                String xmlContent = getOleInvConverter().convertToXML(rawContent);
                INVOrders invOrderObject = null;
                if (xmlContent != null) {
                    invOrderObject = getOleTranscationalRecordGenerator().fromInvoiceXml(xmlContent);
                    invOrders = invOrderObject.getInvOrder();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
        return invOrders;

    }

    public OLEINVConverter getOleInvConverter() {
        if(null == oleInvConverter) {
            this.oleInvConverter = new OLEINVConverter();
        }
        return oleInvConverter;
    }

    public void setOleInvConverter(OLEINVConverter oleInvConverter) {
        this.oleInvConverter = oleInvConverter;
    }

    public OLETranscationalRecordGenerator getOleTranscationalRecordGenerator() {
        if(null == oleTranscationalRecordGenerator) {
            oleTranscationalRecordGenerator = new OLETranscationalRecordGenerator();
        }
        return oleTranscationalRecordGenerator;
    }

    public void setOleTranscationalRecordGenerator(OLETranscationalRecordGenerator oleTranscationalRecordGenerator) {
        this.oleTranscationalRecordGenerator = oleTranscationalRecordGenerator;
    }
}
