package org.kuali.ole.dao.impl;

import org.kuali.ole.dao.OleMemorizeService;
import org.kuali.ole.model.jpa.*;
import org.kuali.ole.repo.*;
import org.kuali.ole.repo.jpa.ItemStatusRecordRepository;
import org.kuali.ole.repo.jpa.ItemTypeRecordRepository;
import org.kuali.ole.spring.cache.Memoize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sheiks on 10/11/16.
 */
@Component
public class OleMemorizeServiceImpl implements OleMemorizeService {

    @Autowired
    CallNumberTypeRecordRepository callNumberTypeRecordRepository;

    @Autowired
    AccessLocationRepository accessLocationRepository;

    @Autowired
    AuthenticationTypeRecordRepository authenticationTypeRecordRepository;

    @Autowired
    ExtentOfOwnerShipTypeRecordRepository extentOfOwnerShipTypeRecordRepository;

    @Autowired
    StatisticalSearchRecordRepository statisticalSearchRecordRepository;

    @Autowired
    ReceiptStatusRecordRepository receiptStatusRecordRepository;

    @Autowired
    ItemStatusRecordRepository itemStatusRecordRepository;

    @Autowired
    ItemTypeRecordRepository itemTypeRecordRepository;

    @Memoize
    public CallNumberTypeRecord getCallNumberTypeRecordById(Long id) {
        if(null == id) {
            return null;
        }
        return callNumberTypeRecordRepository.findOne(id);
    }

    @Memoize
    public AuthenticationTypeRecord getAuthenticationTypeRecordById(Integer id) {
        if(null == id) {
            return null;
        }
        return authenticationTypeRecordRepository.findOne(id);
    }

    @Memoize
    public ExtentOfOwnerShipTypeRecord getExtentOfOwnerShipTypeRecordById(Long id) {
        if(null == id) {
            return null;
        }
        return extentOfOwnerShipTypeRecordRepository.findOne(id);
    }

    @Memoize
    public ReceiptStatusRecord getReceiptStatusRecordById(String id) {
        if(null == id) {
            return null;
        }
        return receiptStatusRecordRepository.findOne(id);
    }

    @Memoize
    public StatisticalSearchRecord getStatisticalSearchRecordById(Long id) {
        if(null == id) {
            return null;
        }
        return statisticalSearchRecordRepository.findOne(id);
    }

    @Memoize
    public AccessLocation getAccessLocationById(Integer id) {
        if(null == id) {
            return null;
        }
        return accessLocationRepository.findOne(id);
    }

    @Memoize
    public ItemTypeRecord getItemTypeById(String id) {
        if(null == id) {
            return null;
        }
        return itemTypeRecordRepository.findOne(id);
    }

    @Memoize
    public ItemStatusRecord getItemStatusById(String id) {
        if(null == id) {
            return null;
        }
        return itemStatusRecordRepository.findOne(id);
    }
}
