package org.kuali.ole.dao;

import org.kuali.ole.model.jpa.*;

/**
 * Created by sheiks on 10/11/16.
 */
public interface OleMemorizeService {

    public CallNumberTypeRecord getCallNumberTypeRecordById(Long id);
    public AuthenticationTypeRecord getAuthenticationTypeRecordById(Integer id);
    public ExtentOfOwnerShipTypeRecord getExtentOfOwnerShipTypeRecordById(Long id);
    public ReceiptStatusRecord getReceiptStatusRecordById(String id);
    public StatisticalSearchRecord getStatisticalSearchRecordById(Long id);
    public AccessLocation getAccessLocationById(Integer id);
    public ItemStatusRecord getItemStatusById(String id);
    public ItemTypeRecord getItemTypeById(String id);
}
