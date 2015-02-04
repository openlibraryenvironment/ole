package org.kuali.ole.service;

import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.ole.deliver.bo.PatronBillPayment;
import org.kuali.ole.deliver.bo.OleAddressBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OleProxyPatronDocument;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.ole.docstore.common.document.content.instance.Item;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Bala.km
 * Date: 7/2/12
 * Time: 2:04 PM
 * Service class to provide input to the krms rule engine for loans circulation policy
 */
public class OleCirculationPolicyServiceImpl implements OleCirculationPolicyService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleLocationServiceImpl.class);
    private LoanProcessor loanProcessor;

    private LoanProcessor getLoanProcessor() {
        if (loanProcessor == null) {
            loanProcessor = SpringContext.getBean(LoanProcessor.class);
        }
        return loanProcessor;
    }

    public void setLoanProcessor(LoanProcessor loanProcessor) {
        this.loanProcessor = loanProcessor;
    }

    /**
     *  This method checks valid barcode using barcode.
     *
     * @param barcode
     * @return boolean
     */
    @Override
    public boolean isValidBarcode(String barcode, String pattern) {
        boolean valid = false;
        //"^[0-9]{1}(([0-9]*-[0-9]*)*|([0-9]* [0-9]*)*|[0-9]*)[0-9]{1}$"
        if(pattern!=null && barcode!=null){
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(barcode);
            boolean result = m.matches();

            if (!result) {
                valid = true;
            }
        }
        return valid;
    }

    /**
     *  This method returns MembershipExpireDate using patronBarcode.
     * @param patronBarcode
     * @return Date
     */
    @Override
    public Date getPatronMembershipExpireDate(String patronBarcode) {
        Date expirationDate=null;
        try{
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("barcode", patronBarcode);
            List<OlePatronDocument> olePatronDocument = (List<OlePatronDocument>)  KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,criteria);
            if(olePatronDocument.size()==1) {
                expirationDate = olePatronDocument.get(0).getExpirationDate();
            }
        }catch(Exception e){
            LOG.error(e.getMessage());
        }
        return expirationDate;
    }

    /**
     *  This method returns number of items loaned for a particular patron id.
     * @param olePatronId
     * @return int
     */
    @Override
    public int getNoOfItemsLoaned(String olePatronId,boolean renewalFlag) {
        Map<String, String> criteria = new HashMap<String, String>();
        Collection<OleLoanDocument> oleLoanDocuments=null;
        try{
            criteria.put("patronId", olePatronId);
            oleLoanDocuments = KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class,criteria);
        }catch(Exception e){
            LOG.error(e.getMessage());
        }
        int loanedItems = 0;
        if(oleLoanDocuments!=null){
            loanedItems = renewalFlag ?oleLoanDocuments.size() : oleLoanDocuments.size()+1;
        }
        return loanedItems;
    }

    /**
     *  This method returns loan due date using loan period.
     * @param loanPeriod
     * @return  Timestamp
     */
    @Override
    public Timestamp calculateLoanDueDate(String loanPeriod) {
        String loanPeriodType[]=null;
        Timestamp dueDate = null;
        Calendar calendar = Calendar.getInstance();
        if(loanPeriod != null && loanPeriod.trim().length()>0){
            loanPeriodType =  loanPeriod.split("-");
            int loanPeriodValue =  Integer.parseInt(loanPeriodType[0].toString());
            String loanPeriodTypeValue =  loanPeriodType[1].toString();
            if(loanPeriodTypeValue.equalsIgnoreCase("MINUTE")){
                calendar.add(Calendar.MINUTE, loanPeriodValue);
            } else if(loanPeriodTypeValue.equalsIgnoreCase("HOUR")) {
                calendar.add(Calendar.HOUR, loanPeriodValue);
            } else if(loanPeriodTypeValue.equalsIgnoreCase("WEEK")) {
                calendar.add(Calendar.WEEK_OF_MONTH, loanPeriodValue);
            } else {
                calendar.add(Calendar.DATE, loanPeriodValue);
            }
            dueDate =  new Timestamp(calendar.getTime().getTime());
        }
        return dueDate;
    }
    public List<FeeType> getPatronBillPayment(String patronId){
        List<FeeType> feeTypeList = new ArrayList<FeeType>();
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("patronId",patronId);
        List<PatronBillPayment> patronBillPayments =  (List<PatronBillPayment>)KRADServiceLocator.getBusinessObjectService().findMatching(PatronBillPayment.class,criteria);
        for(PatronBillPayment patronBillPayment : patronBillPayments){
            feeTypeList.addAll(patronBillPayment.getFeeType());
        }
        return feeTypeList;
    }

    @Override
    public HashMap getNumberOfOverdueItemsCheckedOut(String patronId) {
        int count = 0;
        HashMap overdueItems = new HashMap();

        List<OleLoanDocument> overdueLoanedItem = new ArrayList<OleLoanDocument>();
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("patronId", patronId);
        List<OleLoanDocument> oleLoanDocuments= (List<OleLoanDocument>)KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class,criteria);
        oleLoanDocuments = oleLoanDocuments!=null?oleLoanDocuments:new ArrayList<OleLoanDocument>();
        for(OleLoanDocument oleLoanDocument : oleLoanDocuments){
            Integer numberOfOverdueNoticesSent = new Integer(oleLoanDocument.getNumberOfOverdueNoticesSent()!=null?oleLoanDocument.getNumberOfOverdueNoticesSent():"0");
            if(numberOfOverdueNoticesSent>0){
                overdueLoanedItem.add(oleLoanDocument);
                count++;
            }
        }
        overdueItems.put("count",count);
        overdueItems.put("oleLoanDocumentList",overdueLoanedItem);
        return overdueItems;
    }

    @Override
    public HashMap getRecalledOverdueItemsCheckedOut(List<OleLoanDocument> oleLoanDocuments) {
        int count = 0;
        List<Integer> listOfRecalledOverdueDays = new ArrayList<Integer>();
        HashMap recalledOverdueItems = new HashMap();
        List<OleLoanDocument> recallOverdueLoanedItem = new ArrayList<OleLoanDocument>();
        oleLoanDocuments = oleLoanDocuments!=null?oleLoanDocuments:new ArrayList<OleLoanDocument>();
        for(OleLoanDocument oleLoanDocument : oleLoanDocuments){
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("itemId", oleLoanDocument.getItemId());
            List<OleDeliverRequestBo> oleDeliverRequestBoList =(List<OleDeliverRequestBo>)KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class,criteria);
            for(OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBoList){
                if(oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode().contains("recall")){
                    count ++;
                    recallOverdueLoanedItem.add(oleLoanDocument);
                    if(oleLoanDocument!=null && oleLoanDocument.getLoanDueDate()!=null && oleLoanDocument.getLoanDueDate().compareTo(new Date())<=0){
                        listOfRecalledOverdueDays.add(new Integer(getTimeDiff(oleLoanDocument.getLoanDueDate(),new Date())));
                    }
                    break;
                }
            }
        }
        recalledOverdueItems.put("count",count);
        recalledOverdueItems.put("oleLoanDocumentList",recallOverdueLoanedItem);
        recalledOverdueItems.put("listOfRecalledOverdueDays",listOfRecalledOverdueDays);
        return recalledOverdueItems;
    }

    @Override
    public List<Integer> getNumberOfOverdueDays(String patronId) {
        List<Integer> listOfOverdueDays = new ArrayList<Integer>();
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("patronId", patronId);
        OleLoanDocument oleLoanDocument=KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleLoanDocument.class,criteria);
        if(oleLoanDocument!=null && oleLoanDocument.getLoanDueDate()!=null && oleLoanDocument.getLoanDueDate().compareTo(new Date())<=0){
            listOfOverdueDays.add(new Integer(getTimeDiff(oleLoanDocument.getLoanDueDate(),new Date())));
        }
        return listOfOverdueDays;
    }
    public String getTimeDiff(Date dateOne, Date dateTwo) {
        String diff = "";
        long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
        diff = String.format("%d",TimeUnit.MILLISECONDS.toDays(timeDiff),
                -TimeUnit.HOURS.toDays(timeDiff));
        return diff;
    }
    @Override
    public int getNumberOfClaimsReturned(String patronId) {
        int count = 0;
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("patronId", patronId);
        List<OleLoanDocument> oleLoanDocuments= (List<OleLoanDocument>)KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class,criteria);
        oleLoanDocuments = oleLoanDocuments!=null?oleLoanDocuments:new ArrayList<OleLoanDocument>();
        for(OleLoanDocument oleLoanDocument : oleLoanDocuments){
            try {
                String itemXmlContent = getLoanProcessor().getItemXML(oleLoanDocument.getItemUuid());
                Item oleItem = getLoanProcessor().getItemPojo(itemXmlContent);
                if (oleItem.isClaimsReturnedFlag()) {
                    count++;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return count;
    }

    public Integer getHoursDiff(Date dateOne, Date dateTwo) {
        if(dateOne!=null && dateTwo!=null && dateOne.compareTo(dateTwo)<=0){
            String diff = "";
            long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
            diff = String.format("%d",TimeUnit.MILLISECONDS.toHours(timeDiff),
                    -TimeUnit.HOURS.toMinutes(timeDiff));
            return new Integer(diff);
        }
        return 0;
    }

    public List<OlePatronDocument> isProxyPatron(String partonId) throws Exception{
        List realPatron = new ArrayList();
        Map<String, String> criteria = new HashMap<String, String>();
        Map<String, String> proxyCriteria = new HashMap<String, String>();
        criteria.put("proxyPatronId", partonId);
        List<OleProxyPatronDocument> oleProxyPatronDocuments = (List<OleProxyPatronDocument>)KRADServiceLocator.getBusinessObjectService().findMatching(OleProxyPatronDocument.class,criteria);
        if(oleProxyPatronDocuments != null && oleProxyPatronDocuments.size() >0){
            for(int proxyPatron=0;proxyPatron<oleProxyPatronDocuments.size();proxyPatron++){
                proxyCriteria.put("olePatronId", oleProxyPatronDocuments.get(proxyPatron).getOlePatronId());
                List<OlePatronDocument> olePatronDocument = (List<OlePatronDocument>)  KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,proxyCriteria);
                if(olePatronDocument!=null && olePatronDocument.size()>0)
                    realPatron.add(olePatronDocument.get(0));
                // realPatron.add(oleProxyPatronDocuments.get(proxyPatron));
            }
            // return realPatron;
        }
        return realPatron;
    }

    public List<OlePatronDocument> isProxyPatron(OlePatronDocument olePatronDocument) throws Exception{
        List realPatron = new ArrayList();
        List<OleProxyPatronDocument> oleProxyPatronDocuments = olePatronDocument.getOleProxyPatronDocumentList();
        for(OleProxyPatronDocument oleProxyPatronDocument : oleProxyPatronDocuments){
           realPatron.add(oleProxyPatronDocument.getOlePatronDocument());
        }
        return realPatron;
    }

    public boolean isAddressVerified(String patronId) throws Exception{
        int count =0;
        Map<String, String> criteria = new HashMap<String, String>();
        Map<String, String> addressCriteria = new HashMap<String, String>();
        criteria.put("olePatronId", patronId);
        List<OlePatronDocument> olePatronDocument = (List<OlePatronDocument>)  KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,criteria);
        if(olePatronDocument != null && olePatronDocument.size() >0){
            EntityBo entityBo = olePatronDocument.get(0).getEntity();
            if(entityBo.getEntityTypeContactInfos().get(0).getAddresses() != null && entityBo.getEntityTypeContactInfos().get(0).getAddresses().size() >0){
                for(int address=0;address<entityBo.getEntityTypeContactInfos().get(0).getAddresses().size();address ++ ){
                    addressCriteria.put("id",entityBo.getEntityTypeContactInfos().get(0).getAddresses().get(address).getId());
                    List<OleAddressBo> oleAddressBos = (List<OleAddressBo>)  KRADServiceLocator.getBusinessObjectService().findMatching(OleAddressBo.class,addressCriteria);
                    if(oleAddressBos != null && oleAddressBos.size() > 0 && oleAddressBos.get(0).isAddressVerified()){
                        count ++;
                    }
                }
                if(count == entityBo.getEntityTypeContactInfos().get(0).getAddresses().size()){
                    return true;
                }
            }

        }
        return false;
    }


    public boolean isAddressVerified(OlePatronDocument olePatronDocument,String patronId) throws Exception{
        if (olePatronDocument == null){
            return isAddressVerified(patronId);
        }
       // int count =0;
        Map<String, String> addressCriteria = new HashMap<String, String>();
        EntityBo entityBo = olePatronDocument.getEntity();
            if(entityBo.getEntityTypeContactInfos().get(0).getAddresses() != null && entityBo.getEntityTypeContactInfos().get(0).getAddresses().size() >0){
                for(int address=0;address<entityBo.getEntityTypeContactInfos().get(0).getAddresses().size();address ++ ){
                    addressCriteria.put("id",entityBo.getEntityTypeContactInfos().get(0).getAddresses().get(address).getId());
                    List<OleAddressBo> oleAddressBos = (List<OleAddressBo>)  KRADServiceLocator.getBusinessObjectService().findMatching(OleAddressBo.class,addressCriteria);
                    if(oleAddressBos != null && oleAddressBos.size() > 0 && entityBo.getEntityTypeContactInfos().get(0).getAddresses().get(address).isDefaultValue() && oleAddressBos.get(0).isAddressVerified()){
                       return true;
                    }
                    /*if(oleAddressBos != null && oleAddressBos.size() > 0 && oleAddressBos.get(0).isAddressVerified()){
                        count ++;
                    }*/
                }
                /*if(count == entityBo.getEntityTypeContactInfos().get(0).getAddresses().size()){
                    return true;
                }*/
            }
        return false;
    }

    public HashMap getLoanedKeyMap(String patronId,boolean renewalFlag) {
        Long begin = System.currentTimeMillis();
        // Initializing  variables
        OlePatronDocument olePatronDocument;
        int recallCount = 0;
        int overdueCount=0;
        int loanedItems=0;
        List<Integer> listOfRecalledOverdueDays = new ArrayList<Integer>();
        HashMap keyMap = new HashMap();
        List<OleLoanDocument> recallOverdueLoanedItem = new ArrayList<OleLoanDocument>();
        List<OleLoanDocument> overdueLoanedItem = new ArrayList<OleLoanDocument>();

        List<Integer> listOfOverdueDays = new ArrayList<Integer>();
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("patronId", patronId);
        List<OleLoanDocument> oleLoanDocuments= (List<OleLoanDocument>)KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class,criteria);
        oleLoanDocuments = oleLoanDocuments!=null?oleLoanDocuments:new ArrayList<OleLoanDocument>();
        olePatronDocument = oleLoanDocuments!=null &&  oleLoanDocuments.size()>0 ? oleLoanDocuments.get(0).getOlePatron() : null;
        for(OleLoanDocument oleLoanDocument : oleLoanDocuments){
            //checking overdue
            Integer numberOfOverdueNoticesSent = new Integer(oleLoanDocument.getNumberOfOverdueNoticesSent()!=null?oleLoanDocument.getNumberOfOverdueNoticesSent():"0");
            if(numberOfOverdueNoticesSent>0){
                //checking recall if overdue only
                overdueLoanedItem.add(oleLoanDocument);
                criteria.clear();
                criteria.put("itemId", oleLoanDocument.getItemId());
                List<OleDeliverRequestBo> oleDeliverRequestBoList =(List<OleDeliverRequestBo>)KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class,criteria);
                for(OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBoList){
                    if(oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode().contains("recall")){
                        recallCount ++;
                        recallOverdueLoanedItem.add(oleLoanDocument);
                        if(oleLoanDocument!=null && oleLoanDocument.getLoanDueDate()!=null && oleLoanDocument.getLoanDueDate().compareTo(new Date())<=0){
                            listOfRecalledOverdueDays.add(new Integer(getTimeDiff(oleLoanDocument.getLoanDueDate(),new Date())));
                        }
                        break;
                    }
                }
                overdueCount++;
            }

            // no of over
            if(oleLoanDocument!=null && oleLoanDocument.getLoanDueDate()!=null && oleLoanDocument.getLoanDueDate().compareTo(new Date())<=0){
                listOfOverdueDays.add(new Integer(getTimeDiff(oleLoanDocument.getLoanDueDate(),new Date())));
            }
        }
        if(oleLoanDocuments!=null){
            loanedItems = renewalFlag ?oleLoanDocuments.size() : oleLoanDocuments.size()+1;
        }

        HashMap<String,Integer> itemTypeMap = new HashMap<>();
        for(OleLoanDocument oleLoanDocument : oleLoanDocuments){
            if(itemTypeMap.containsKey(oleLoanDocument.getItemType())){
                Integer count = itemTypeMap.get(oleLoanDocument.getItemType());
                count++;
                itemTypeMap.put(oleLoanDocument.getItemType(),count);
            }else{
                itemTypeMap.put(oleLoanDocument.getItemType(),1);
            }
        }
        //total loaned item count
        keyMap.put("loanedItemCount",loanedItems);
        keyMap.put("recallCount", recallCount);
        keyMap.put("oleLoanDocumentList", recallOverdueLoanedItem);
        keyMap.put("listOfRecalledOverdueDays", listOfRecalledOverdueDays);
        //overdue Item
        keyMap.put("overdueCount",overdueCount);
        keyMap.put("oleLoanDocumentList",overdueLoanedItem);
        keyMap.put("listOfOverdueDays",listOfOverdueDays);

        keyMap.put("patronDetails",olePatronDocument);
        keyMap.put("itemTypeMap",itemTypeMap);
        Long end = System.currentTimeMillis();
        Long timeTaken = end-begin;
        LOG.info("The Time Taken for getLoanedKeyMap in Add Item"+timeTaken);
        return keyMap;
    }

}
