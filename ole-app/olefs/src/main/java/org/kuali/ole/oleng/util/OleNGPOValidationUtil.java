package org.kuali.ole.oleng.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.kuali.ole.Exchange;
import org.kuali.ole.oleng.exception.ValidationException;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.spring.batch.BatchUtil;

/**
 * Created by SheikS on 3/30/2016.
 */
public class OleNGPOValidationUtil {
    private BatchUtil batchUtil;

    public boolean validateOleOrderRecord(OleOrderRecord oleOrderRecord, Exchange exchange, Integer recordIndex) {
        boolean valid = true;
        OleTxRecord oleTxRecord = oleOrderRecord.getOleTxRecord();
        valid = validateVendorNumber(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateAccountNumber(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateBuildingCode(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateBuildingRoomNumber(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateQuantity(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateOrderType(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateItemChartCode(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateObjectCode(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateCostSource(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateMethodOfPOTransmission(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateDeliveryCampusCode(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateNumberOfParts(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateItemStatus(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateOrganizationCode(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateChartCode(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateDefaultLocation(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateListPrice(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateFundingSource(oleTxRecord, exchange, recordIndex) && valid;
        return valid;
    }

    private boolean validateVendorNumber(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        if (StringUtils.isBlank(oleTxRecord.getVendorNumber())){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Vendor number cannot be blank or null"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateAccountNumber(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        if (StringUtils.isBlank(oleTxRecord.getAccountNumber())){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Account number cannot be blank or null"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateBuildingCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        if (StringUtils.isBlank(oleTxRecord.getBuildingCode())){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Building Code cannot be blank or null"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateBuildingRoomNumber(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        if (StringUtils.isBlank(oleTxRecord.getDeliveryBuildingRoomNumber())){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Delivery building room number cannot be blank or null"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateQuantity(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String quantity = oleTxRecord.getQuantity();
        if (null == quantity || !NumberUtils.isDigits(quantity)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Delivery building room number cannot be blank or null and it should be round number."), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateOrderType(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String orderType = oleTxRecord.getOrderType();
        if (StringUtils.isBlank(orderType)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Order type cannot be blank or null."), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateItemChartCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String itemChartCode = oleTxRecord.getItemChartCode();
        if (StringUtils.isBlank(itemChartCode)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Item Chart Code cannot be blank or null."), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateObjectCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String objectCode = oleTxRecord.getObjectCode();
        if (StringUtils.isBlank(objectCode)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Object Code cannot be blank or null."), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateCostSource(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String costSource = oleTxRecord.getCostSource();
        if (StringUtils.isBlank(costSource)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Cost Source cannot be blank or null."), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateMethodOfPOTransmission(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String methodOfPOTransmission = oleTxRecord.getMethodOfPOTransmission();
        if (StringUtils.isBlank(methodOfPOTransmission)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Method of PO Transmission cannot be blank or null."), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateDeliveryCampusCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String deliveryCampusCode = oleTxRecord.getDeliveryCampusCode();
        if (StringUtils.isBlank(deliveryCampusCode)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Delivery Campus Code cannot be blank or null."), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateNumberOfParts(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String itemNoOfParts = oleTxRecord.getItemNoOfParts();
        if (null == itemNoOfParts || !NumberUtils.isDigits(itemNoOfParts)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Number of Parts cannot be blank or null and it should be round number"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateItemStatus(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String itemStatus = oleTxRecord.getItemStatus();
        if (StringUtils.isBlank(itemStatus)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Item Status cannot be blank or null"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateOrganizationCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String orgCode = oleTxRecord.getOrgCode();
        if (StringUtils.isBlank(orgCode)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Organization code cannot be blank or null."), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateChartCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String chartCode = oleTxRecord.getChartCode();
        if (StringUtils.isBlank(chartCode)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Chart code cannot be blank or null"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateDefaultLocation(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String chartCode = oleTxRecord.getChartCode();
        if (StringUtils.isBlank(chartCode)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Default Location cannot be blank or null"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateListPrice(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String listPrice = oleTxRecord.getListPrice();
        if (StringUtils.isBlank(listPrice)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("List price cannot be blank or null"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateFundingSource(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String fundingSource = oleTxRecord.getFundingSource();
        if (StringUtils.isBlank(fundingSource)){
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Funding Source cannot be blank or null"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    public BatchUtil getBatchUtil() {
        if(null == batchUtil) {
            batchUtil = new BatchUtil();
        }
        return batchUtil;
    }
}
