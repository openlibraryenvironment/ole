package org.kuali.ole.deliver.inquiry;

import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.ole.deliver.bo.PatronBillHelperService;
import org.kuali.ole.deliver.bo.PatronBillPayment;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.krad.inquiry.InquirableImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 4/3/14
 * Time: 8:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePatronBillInquirableImpl extends InquirableImpl {

    @Override
    public Object retrieveDataObject(Map<String, String> parameters) {
        PatronBillPayment patronBillPayment=(PatronBillPayment)super.retrieveDataObject(parameters);
        PatronBillHelperService patronBillHelperService=new PatronBillHelperService();
        if (patronBillPayment.getFeeType() != null) {
            patronBillHelperService.getUpdateItemDetailsToFeeTypeList(patronBillPayment.getFeeType());
        }
        if(patronBillPayment.getPatronId()!=null){
            Map<String,String> map=new HashMap<String,String>();
            map.put("entityId",patronBillPayment.getPatronId());
            EntityNameBo entityNameBo=(EntityNameBo) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(EntityNameBo.class,map);
            if(entityNameBo!=null){
               patronBillPayment.setFirstName(entityNameBo.getFirstName());
               patronBillPayment.setLastName(entityNameBo.getLastName());
            }
        }

        return patronBillPayment;
    }
}
