package org.kuali.ole.oleng.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.kuali.ole.Exchange;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.oleng.dao.SelectDAO;
import org.kuali.ole.oleng.dao.impl.SelectDAOImpl;
import org.kuali.ole.oleng.exception.ValidationException;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.ole.vnd.businessobject.OleCurrencyType;
import org.kuali.ole.vnd.businessobject.VendorDetail;

/**
 * Created by SheikS on 3/31/2016.
 */
public class OleNGInvoiceValidationUtil {

    private BatchUtil batchUtil;

    private SelectDAO selectDAO;

    public boolean validateOleInvoiceRecord(OleInvoiceRecord oleInvoiceRecord, Exchange exchange, Integer recordIndex) {
        boolean valid = true;
        valid = validateVendorNumber(oleInvoiceRecord, exchange, recordIndex) && valid;
        valid = validateInvoiceNumber(oleInvoiceRecord, exchange, recordIndex) && valid;
        valid = validateInvoiceDate(oleInvoiceRecord, exchange, recordIndex) && valid;
        valid = validateInvoicePrice(oleInvoiceRecord, exchange, recordIndex) && valid;
        valid = validateItemDescription(oleInvoiceRecord, exchange, recordIndex) && valid;
        valid = validateQuantity(oleInvoiceRecord, exchange, recordIndex) && valid;

        validateAccountNumber(oleInvoiceRecord, exchange, recordIndex);
        validateObjectCode(oleInvoiceRecord, exchange, recordIndex);
        validateFundCode(oleInvoiceRecord, exchange, recordIndex);
        validateCurrencyType(oleInvoiceRecord, exchange, recordIndex);

        return valid;
    }

    private boolean validateInvoiceNumber(OleInvoiceRecord oleInvoiceRecord, Exchange exchange, Integer recordIndex) {
        String invoiceNumber = oleInvoiceRecord.getInvoiceNumber();
        if (StringUtils.isBlank(invoiceNumber)){
            getBatchUtil().addInvoiceFaiureResponseToExchange(
                    new ValidationException("Invoice number cannot be blank or null"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateVendorNumber(OleInvoiceRecord oleInvoiceRecord, Exchange exchange, Integer recordIndex) {
        String vendorNumber = oleInvoiceRecord.getVendorNumber();
        if (StringUtils.isBlank(vendorNumber)){
            getBatchUtil().addInvoiceFaiureResponseToExchange(
                    new ValidationException("Vendor number cannot be blank or null"), recordIndex, exchange);
            return false;
        } else {
            VendorDetail vendorDetail = getSelectDAO().getVendorDetailByVendorNumber(vendorNumber);
            if (null == vendorDetail) {
                getBatchUtil().addInvoiceFaiureResponseToExchange(
                        new ValidationException("Invalid Vendor Number : " + vendorNumber), recordIndex, exchange);
                oleInvoiceRecord.setVendorNumber(null);
                return false;
            }
        }
        return true;
    }

    private boolean validateInvoiceDate(OleInvoiceRecord oleInvoiceRecord, Exchange exchange, Integer recordIndex) {
        if (StringUtils.isBlank(oleInvoiceRecord.getInvoiceDate())){
            getBatchUtil().addInvoiceFaiureResponseToExchange(
                    new ValidationException("Invoice date cannot be blank or null"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateInvoicePrice(OleInvoiceRecord oleInvoiceRecord, Exchange exchange, Integer recordIndex) {
        String listPrice = oleInvoiceRecord.getListPrice();
        if (null == listPrice || !NumberUtils.isNumber(listPrice)){
            getBatchUtil().addInvoiceFaiureResponseToExchange(
                    new ValidationException("List Price cannot be blank or null and it should be numeric"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateItemDescription(OleInvoiceRecord oleInvoiceRecord, Exchange exchange, Integer recordIndex) {
        if (StringUtils.isBlank(oleInvoiceRecord.getItemDescription())){
            getBatchUtil().addInvoiceFaiureResponseToExchange(
                    new ValidationException("Item description cannot be blank or null"), recordIndex, exchange);
            return false;
        }
        return true;
    }

    private boolean validateQuantity(OleInvoiceRecord oleInvoiceRecord, Exchange exchange, Integer recordIndex) {
        String quantity = oleInvoiceRecord.getQuantity();
        if (null == quantity || !NumberUtils.isDigits(quantity)){
            getBatchUtil().addInvoiceFaiureResponseToExchange(
                    new ValidationException("Quantity cannot be blank or null and it should be round number"), recordIndex, exchange);
            return false;
        }
        return true;
    }


    private boolean validateAccountNumber(OleInvoiceRecord oleInvoiceRecord, Exchange exchange, Integer recordIndex) {
        String accountNumber = oleInvoiceRecord.getAccountNumber();
        if (StringUtils.isNotBlank(accountNumber)) {
            Account account = getSelectDAO().getAccountByAccountNumber(accountNumber);
            if (null == account) {
                getBatchUtil().addInvoiceFaiureResponseToExchange(
                        new ValidationException("Invalid Account Number : " + accountNumber), recordIndex, exchange);
                oleInvoiceRecord.setAccountNumber(null);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateFundCode(OleInvoiceRecord oleInvoiceRecord, Exchange exchange, Integer recordIndex) {
        String fundCode = oleInvoiceRecord.getFundCode();
        if (StringUtils.isNotBlank(fundCode)) {
            OleFundCode oleFundCode = getSelectDAO().getOleFundCodeByCode(fundCode);
            if (null == oleFundCode) {
                getBatchUtil().addInvoiceFaiureResponseToExchange(
                        new ValidationException("Invalid Fund Code : " + fundCode), recordIndex, exchange);
                oleInvoiceRecord.setFundCode(null);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateObjectCode(OleInvoiceRecord oleInvoiceRecord, Exchange exchange, Integer recordIndex) {
        String financialObjectCode = oleInvoiceRecord.getObjectCode();
        if (StringUtils.isNotBlank(financialObjectCode)) {
            ObjectCode objectCode = getSelectDAO().getObjectCodeByCode(financialObjectCode);
            if (null == objectCode) {
                getBatchUtil().addInvoiceFaiureResponseToExchange(
                        new ValidationException("Invalid Object Code : " + financialObjectCode), recordIndex, exchange);
                oleInvoiceRecord.setObjectCode(null);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateCurrencyType(OleInvoiceRecord oleInvoiceRecord, Exchange exchange, Integer recordIndex) {
        String currencyType = oleInvoiceRecord.getCurrencyType();
        if (StringUtils.isNotBlank(currencyType)) {
            OleCurrencyType oleCurrencyType = getSelectDAO().getCurrencyType(currencyType);
            if (null == oleCurrencyType) {
                getBatchUtil().addInvoiceFaiureResponseToExchange(
                        new ValidationException("Invalid Currency Type : " + currencyType), recordIndex, exchange);
                oleInvoiceRecord.setObjectCode(null);
            } else {
                return true;
            }
        }
        return false;
    }

    public SelectDAO getSelectDAO() {
        if (null == selectDAO) {
            selectDAO = new SelectDAOImpl();
        }
        return selectDAO;
    }

    public BatchUtil getBatchUtil() {
        if(null == batchUtil) {
            batchUtil = new BatchUtil();
        }
        return batchUtil;
    }
}
