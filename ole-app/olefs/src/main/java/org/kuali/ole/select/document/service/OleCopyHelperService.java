package org.kuali.ole.select.document.service;

import org.kuali.ole.docstore.common.document.ids.BibId;
import org.kuali.ole.select.businessobject.*;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 11/13/13
 * Time: 1:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleCopyHelperService {

    public HashMap<String, List<OleCopy>> getCopyListBasedOnLocation(List<OleCopy> copyList, String bibId);

    public HashMap<String, List<OleCopy>> getCopyListBasedOnCopyNumber(List<OleCopy> copyList,Integer partNumber);

    public List<OleCopies> setCopiesToLineItem(List<OleCopy> copyList, KualiInteger noOfParts, String bibId);

    public List<OleCopy> setCopyValues(OleRequisitionCopies itemCopy, String bibId, List<String> volChar);

    public boolean checkCopyEntry(KualiDecimal noOfCopies, String location, Integer itemCount, KualiDecimal noOfCopiesOrdered,
                                  KualiInteger noOfPartsOrdered, List<OleCopies> copiesList, String volumeNumber, boolean isRoute);

    public void updateRequisitionAndPOItems(OlePurchaseOrderItem olePurchaseOrderItem,
                                            OleLineItemReceivingItem oleLineItemReceivingItem, OleCorrectionReceivingItem oleCorrectionReceivingItem, boolean isReceiving);

    public List<OleCopy> setCopyValuesForList(List<OleCopies> itemCopies, String bibId, BibId bibTree, String oleERSIdentifier);

    public boolean checkForTotalCopiesGreaterThanQuantityAtSubmit(List<OleCopies> copyList, KualiDecimal noOfCopiesOrdered);
}
