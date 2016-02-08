package org.kuali.ole.deliver.bo;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;

import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/18/12
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class ItemBillHelperService {

    private static final Logger LOG = Logger.getLogger(org.kuali.ole.deliver.bo.ItemBillHelperService.class);
    private BusinessObjectService businessObjectService;
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    /**
     * Gets the businessObjectService attribute.
     *
     * @return Returns the businessObjectService
     */
    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    /**
     * This method will retrieve  FeeType object and iterate it  and set to item bill for differencating paid and unpaid bills
     *
     * @return Returns the feeTypeList
     */

    public List<FeeType> getItemBillDetails(String itemBarcode) {
        LOG.debug("Initialized getItemBillDetails Method");
        Map itemBarcodeMap = new HashMap();
        itemBarcodeMap.put("itemBarcode", itemBarcode);
        List<FeeType> feeTypeList = (List<FeeType>) KRADServiceLocator.getBusinessObjectService().findMatching(FeeType.class, itemBarcodeMap);
        if (feeTypeList == null) {
            return null;
        }
        return sortById(feeTypeList);
    }

    public List<FeeType> sortById(List<FeeType> feeTypes) {
        Collections.sort(feeTypes, new Comparator<FeeType>() {
            public int compare(FeeType feeType1, FeeType feeType2) {
                return Integer.parseInt(feeType1.getBillNumber()) > Integer.parseInt(feeType2.getBillNumber()) ? 1 : -1;
            }
        });
        List<FeeType> outFeeTypes = new ArrayList<FeeType>();
        List<FeeType> partFeeTypes = new ArrayList<FeeType>();
        List<FeeType> restFeeTypes = new ArrayList<FeeType>();
        List<FeeType> categorizedFeeTypes = new ArrayList<FeeType>();

        for (FeeType feeType : feeTypes) {
            if (feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_STATUS_OUTSTANDING_CODE)) {
                outFeeTypes.add(feeType);
            } else if (feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_STATUS_PART_CODE)) {
                partFeeTypes.add(feeType);
            } else {
                restFeeTypes.add(feeType);
            }
        }
        categorizedFeeTypes.addAll(outFeeTypes);
        categorizedFeeTypes.addAll(partFeeTypes);
        categorizedFeeTypes.addAll(restFeeTypes);

        return categorizedFeeTypes;
    }


    public FeeType getFeeTypeDetails(FeeType feeType) {
        LoanProcessor loanProcessor = new LoanProcessor();
        if (feeType != null && feeType.getItemUuid() != null) {
            try {
                org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(feeType.getItemUuid());
                ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
                Item itemContent = itemOlemlRecordProcessor.fromXML(item.getContent());
                OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(item.getHolding().getContent());
                feeType.setItemUuid(item.getId());

                feeType.setItemTitle(item.getHolding().getBib().getTitle());
                feeType.setItemAuthor(item.getHolding().getBib().getAuthor());
               /* if(itemContent.getCallNumber()!=null && !StringUtils.isEmpty(itemContent.getCallNumber().getNumber())){
                    feeType.setItemCallNumber(loanProcessor.getItemCallNumber(itemContent.getCallNumber()));
                }else {
                    feeType.setItemCallNumber(loanProcessor.getItemCallNumber(oleHoldings.getCallNumber()));
                }*/
                feeType.setItemCallNumber(loanProcessor.getItemCallNumber(itemContent.getCallNumber(),oleHoldings.getCallNumber()));
                feeType.setItemCopyNumber(itemContent.getCopyNumber());
                feeType.setItemChronology(itemContent.getChronology());
                feeType.setItemEnumeration(itemContent.getEnumeration());
                if(itemContent.getTemporaryItemType()!=null && itemContent.getTemporaryItemType().getCodeValue()!=null){
                    feeType.setItemType(itemContent.getTemporaryItemType().getCodeValue());
                }else{
                    feeType.setItemType(itemContent.getItemType().getCodeValue());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return feeType;
    }
}