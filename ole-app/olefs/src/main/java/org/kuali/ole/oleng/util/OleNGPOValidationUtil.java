package org.kuali.ole.oleng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.kuali.ole.Exchange;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.coa.businessobject.*;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.oleng.dao.SelectDAO;
import org.kuali.ole.oleng.dao.impl.SelectDAOImpl;
import org.kuali.ole.oleng.exception.ValidationException;
import org.kuali.ole.oleng.service.OleNGMemorizeService;
import org.kuali.ole.oleng.service.impl.OleNGMemorizeServiceImpl;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.businessobject.OLERequestorPatronDocument;
import org.kuali.ole.select.businessobject.OleFormatType;
import org.kuali.ole.select.businessobject.OleRequestSourceType;
import org.kuali.ole.select.document.service.OleDocstoreHelperService;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.ole.sys.businessobject.Building;
import org.kuali.ole.sys.businessobject.Room;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.ItemUtil;
import org.kuali.ole.vnd.businessobject.*;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;

import java.util.List;

/**
 * Created by SheikS on 3/30/2016.
 */
public class OleNGPOValidationUtil {
    private BatchUtil batchUtil;
    private OleNGMemorizeService oleNGMemorizeService;

    private SelectDAO selectDAO;
    private ItemUtil itemUtil;

    public boolean validateOleOrderRecord(OleOrderRecord oleOrderRecord, Exchange exchange, Integer recordIndex) {
        boolean valid = true;
        OleTxRecord oleTxRecord = oleOrderRecord.getOleTxRecord();

        valid = validateVendorDetails(oleTxRecord, exchange, recordIndex) && valid;

        valid = validateBuildingCode(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateDeliveryCampusCode(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateBuildingRoomNumber(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateDeliveryDetails(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateDeliveryDetails(oleTxRecord, exchange, recordIndex) && valid;

        valid = validateAccountingLine(oleTxRecord, exchange, recordIndex) && valid;

        valid = validateChartCode(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateOrganizationCode(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateChartCodeAndOrgCodeCombination(oleTxRecord, exchange, recordIndex) && valid;

        valid = validateQuantity(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateOrderType(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateCostSource(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateMethodOfPOTransmission(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateNumberOfParts(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateItemStatus(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateDefaultLocation(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateListPrice(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateFundingSource(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateDonorCode(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateSingleCopyNumber(oleTxRecord, exchange, recordIndex) && valid;
        valid = validateFormatType(oleTxRecord,exchange,recordIndex) && valid;

        valid = validateBibRecord(oleOrderRecord, exchange, recordIndex) && valid;

        validateVendorCustomerNumber(oleTxRecord, exchange, recordIndex);
        validateRecurringPaymentType(oleTxRecord, exchange, recordIndex);
        validateRequestSource(oleTxRecord, exchange, recordIndex);
        validateRequestorName(oleTxRecord, exchange, recordIndex);
        validateVendorChoice(oleTxRecord, exchange, recordIndex);
        validateAssignToUser(oleTxRecord, exchange, recordIndex);
        validateContractManager(oleTxRecord, exchange, recordIndex);
        return valid;
    }

    private boolean validateVendorDetails(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String vendorNumber = oleTxRecord.getVendorNumber();
        String vendorAliasName = oleTxRecord.getVendorAliasName();
        if (StringUtils.isBlank(vendorNumber) && StringUtils.isBlank(vendorAliasName)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Vendor number / Vendor Alias Name cannot be blank or null"), recordIndex, exchange);
            return false;
        } else {
            boolean isValidVendorNumber = validateVendorNumber(oleTxRecord, exchange, recordIndex);
            boolean isValidVendorAliasName = validateVendorAliasName(oleTxRecord, exchange, recordIndex);
            return isValidVendorNumber || isValidVendorAliasName;
        }
    }

    private boolean validateVendorNumber(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String vendorNumber = oleTxRecord.getVendorNumber();
        if (StringUtils.isNotBlank(vendorNumber)) {
            VendorDetail vendorDetail = getSelectDAO().getVendorDetailByVendorNumber(vendorNumber);
            if (null == vendorDetail) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Vendor Number : " + vendorNumber), recordIndex, exchange);
                oleTxRecord.setVendorNumber(null);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateVendorAliasName(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String vendorAliasName = oleTxRecord.getVendorAliasName();
        if (StringUtils.isNotBlank(vendorAliasName)) {
            VendorAlias vendorAlias = getSelectDAO().getVendorAliasByName(vendorAliasName);
            if (null == vendorAlias) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Vendor Alias Name : " + vendorAliasName), recordIndex, exchange);
                oleTxRecord.setVendorAliasName(null);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateBuildingCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String buildingCode = oleTxRecord.getBuildingCode();
        if (StringUtils.isBlank(buildingCode)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Building Code cannot be blank or null"), recordIndex, exchange);
        } else {
            Building building = getSelectDAO().getBuildingByBuildingCode(buildingCode);
            if (null == building) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Building Code : " + buildingCode), recordIndex, exchange);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateDeliveryCampusCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String deliveryCampusCode = oleTxRecord.getDeliveryCampusCode();
        if (StringUtils.isBlank(deliveryCampusCode)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Delivery Campus Code cannot be blank or null."), recordIndex, exchange);
        } else {
            Building building = getSelectDAO().getBuildingByCampusCode(deliveryCampusCode);
            if (null == building) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Delivery Campus Code"), recordIndex, exchange);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateBuildingRoomNumber(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String deliveryBuildingRoomNumber = oleTxRecord.getDeliveryBuildingRoomNumber();
        if (StringUtils.isBlank(deliveryBuildingRoomNumber)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Delivery building room number cannot be blank or null"), recordIndex, exchange);
        } else {
            Room room = getSelectDAO().getRoomByBuildingRoomNumber(deliveryBuildingRoomNumber);
            if (null == room) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Delivery Building Room Number : " + deliveryBuildingRoomNumber), recordIndex, exchange);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateDeliveryDetails(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String buildingCode = oleTxRecord.getBuildingCode();
        String campusCode = oleTxRecord.getDeliveryCampusCode();
        String buildingRoomNumber = oleTxRecord.getDeliveryBuildingRoomNumber();
        if (StringUtils.isNotBlank(buildingCode) && StringUtils.isNotBlank(campusCode) && StringUtils.isNotBlank(buildingRoomNumber)) {
            Room room = getSelectDAO().getRoom(buildingCode, campusCode, buildingRoomNumber);
            if (null == room) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid combination of Building Code, Delivery Campus Code and Building Room Number. : " + buildingCode + " , " + campusCode + " , " + buildingRoomNumber), recordIndex, exchange);
            } else {
                return true;
            }
        }
        return false;

    }

    private boolean validateAccountingLine(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String fundCode = oleTxRecord.getFundCode();
        String itemChartCode = oleTxRecord.getItemChartCode();
        String accountNumber = oleTxRecord.getAccountNumber();
        String financialObjectCode = oleTxRecord.getObjectCode();

        if (StringUtils.isBlank(fundCode)) {
            StringBuffer fieldsRequired = new StringBuffer();
            fieldsRequired.append("(");
            if (StringUtils.isBlank(itemChartCode)) {
                fieldsRequired.append(OLEConstants.REQUIRED_ITEM_CHART_CD);
                fieldsRequired.append(", ");
            }
            if (StringUtils.isBlank(accountNumber)) {
                fieldsRequired.append(OLEConstants.REQUIRED_ACCOUNT_NUMBER);
                fieldsRequired.append(", ");
            }
            if (StringUtils.isBlank(financialObjectCode)) {
                fieldsRequired.append(OLEConstants.REQUIRED_OBJECT_CODE);
                fieldsRequired.append(", ");
            }

            if (fieldsRequired.length() > 1) {
                fieldsRequired.deleteCharAt(fieldsRequired.length() - 2);
                fieldsRequired.append(") / ");
                fieldsRequired.append(OLEConstants.REQUIRED_FUND_CODE);
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException(fieldsRequired.toString() + " cannot be blank or null."), recordIndex, exchange);
                return false;
            }
        }
        boolean isValidFundCode = validateFundCode(oleTxRecord, exchange, recordIndex);
        boolean isValidItemChartCode = validateItemChartCode(oleTxRecord, exchange, recordIndex);
        boolean isValidAccountNumber = validateAccountNumber(oleTxRecord, exchange, recordIndex);
        boolean isValidObjectCode = validateObjectCode(oleTxRecord, exchange, recordIndex);
        if (!(isValidFundCode || (isValidItemChartCode && isValidAccountNumber && isValidObjectCode))) {
            return false;
        }

        if (StringUtils.isBlank(oleTxRecord.getFundCode()) && StringUtils.isNotBlank(oleTxRecord.getItemChartCode()) && StringUtils.isNotBlank(oleTxRecord.getAccountNumber()) && StringUtils.isNotBlank(oleTxRecord.getObjectCode())) {
            Account account = getSelectDAO().getAccountByAccountNumberAndChartCode(accountNumber, itemChartCode);
            if (null == account) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Combination of Account Number and Item chart code : " + accountNumber + " , " + itemChartCode), recordIndex, exchange);
                return false;
            } else {
                ObjectCode objectCode = getSelectDAO().getObjectCodeByChartCodeAndObjectCode(itemChartCode, financialObjectCode);
                if (null == objectCode) {
                    getBatchUtil().addOrderFaiureResponseToExchange(
                            new ValidationException("Invalid Combination of Object code and Item chart code : " + financialObjectCode + " , " + itemChartCode), recordIndex, exchange);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateFundCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String fundCode = oleTxRecord.getFundCode();
        if (StringUtils.isNotBlank(fundCode)) {
            OleFundCode oleFundCode = getSelectDAO().getOleFundCodeByCode(fundCode);
            if (null == oleFundCode) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Fund Code : " + fundCode), recordIndex, exchange);
                oleTxRecord.setFundCode(null);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateItemChartCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String itemChartCode = oleTxRecord.getItemChartCode();
        if (StringUtils.isNotBlank(itemChartCode)) {
            Chart chart = getSelectDAO().getChartByChartCode(itemChartCode);
            if (null == chart) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Chart Code : " + itemChartCode), recordIndex, exchange);
                oleTxRecord.setItemChartCode(null);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateAccountNumber(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String accountNumber = oleTxRecord.getAccountNumber().trim();
        if (StringUtils.isNotBlank(accountNumber)) {
            Account account = getSelectDAO().getAccountByAccountNumber(accountNumber);
            if (null == account) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Account Number : " + accountNumber), recordIndex, exchange);
                oleTxRecord.setAccountNumber(null);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateObjectCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String financialObjectCode = oleTxRecord.getObjectCode();
        if (StringUtils.isNotBlank(financialObjectCode)) {
            ObjectCode objectCode = getSelectDAO().getObjectCodeByCode(financialObjectCode);
            if (null == objectCode) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Object Code : " + financialObjectCode), recordIndex, exchange);
                oleTxRecord.setObjectCode(null);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateChartCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String chartCode = oleTxRecord.getChartCode();
        if (StringUtils.isBlank(chartCode)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Chart code cannot be blank or null"), recordIndex, exchange);
        } else {
            Organization organization = getSelectDAO().getOrganizationByChartCode(chartCode);
            if (null == organization) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Chart Code : " + chartCode), recordIndex, exchange);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateOrganizationCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String orgCode = oleTxRecord.getOrgCode();
        if (StringUtils.isBlank(orgCode)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Organization code cannot be blank or null."), recordIndex, exchange);
        } else {
            Organization organization = getSelectDAO().getOrganizationByOrgCode(orgCode);
            if (null == organization) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Organization Code : " + orgCode), recordIndex, exchange);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateChartCodeAndOrgCodeCombination(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String chartCode = oleTxRecord.getChartCode();
        String orgCode = oleTxRecord.getOrgCode();

        if (StringUtils.isNotBlank(chartCode) && StringUtils.isNotBlank(orgCode)) {
            Organization organization = getSelectDAO().getOrganizationByChartAndOrgCode(chartCode, orgCode);
            if (null == organization) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid combination of Chart Code and Org Code. : " + chartCode + " , " + orgCode), recordIndex, exchange);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateQuantity(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String quantity = oleTxRecord.getQuantity();
        if (StringUtils.isBlank(quantity)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Quantity cannot be blank or null."), recordIndex, exchange);
        } else if (!NumberUtils.isDigits(quantity)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Invalid Quantity"), recordIndex, exchange);
        } else {
            return true;
        }
        return false;
    }

    private boolean validateOrderType(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String orderType = oleTxRecord.getOrderType();
        if (StringUtils.isBlank(orderType)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Order type cannot be blank or null."), recordIndex, exchange);
        } else {
            PurchaseOrderType purchaseOrderType = getSelectDAO().getPurchaseOrderTypeByOrderType(orderType);
            if (null == purchaseOrderType) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Order Type : " + orderType), recordIndex, exchange);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateCostSource(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String costSource = oleTxRecord.getCostSource();
        if (StringUtils.isBlank(costSource)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Cost Source cannot be blank or null."), recordIndex, exchange);
        } else {
            PurchaseOrderCostSource purchaseOrderCostSource = getSelectDAO().getPurchaseOrderCostSourceByCode(costSource);
            if (null == purchaseOrderCostSource) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Cost Source : " + costSource), recordIndex, exchange);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateMethodOfPOTransmission(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String methodOfPOTransmission = oleTxRecord.getMethodOfPOTransmission();
        if (StringUtils.isBlank(methodOfPOTransmission)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Method of PO Transmission cannot be blank or null."), recordIndex, exchange);
        } else {
            PurchaseOrderTransmissionMethod purchaseOrderTransmissionMethod = getSelectDAO().getPurchaseOrderTransmissionMethodByDesc(methodOfPOTransmission);
            if (null == purchaseOrderTransmissionMethod) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Method of PO Transmission : " + methodOfPOTransmission), recordIndex, exchange);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean validateNumberOfParts(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String itemNoOfParts = oleTxRecord.getItemNoOfParts();
        if (StringUtils.isBlank(itemNoOfParts)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Number of Parts cannot be blank or null"), recordIndex, exchange);
        } else if (!NumberUtils.isDigits(itemNoOfParts)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Invalid Number of Parts : " + itemNoOfParts), recordIndex, exchange);
        } else {
            return true;
        }
        return false;
    }

    private boolean validateDonorCode(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        List<String> oleDonors = oleTxRecord.getOleDonors();
        if (CollectionUtils.isNotEmpty(oleDonors)) {
            for (String donorCode : oleDonors) {
                OLEDonor oleDonor = getOleNGMemorizeService().getDonorCode(donorCode);
                if (null == oleDonor) {
                    getBatchUtil().addOrderFaiureResponseToExchange(
                            new ValidationException("Invalid donor Code : " + donorCode), recordIndex, exchange);
                    return false;
                }
            }
        }
        return true;
    }


    private boolean validateSingleCopyNumber(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String singleCopyNumber = oleTxRecord.getSingleCopyNumber();
        if (StringUtils.isNotBlank(singleCopyNumber)) {
            if(!NumberUtils.isDigits(singleCopyNumber)) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Single Copy Number Value : " + singleCopyNumber), recordIndex, exchange);
                return false;
            }
        }
        return true;
    }

    private boolean validateFormatType(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex){
        String formatTypeName = oleTxRecord.getFormatTypeId();
        if(StringUtils.isNotBlank(formatTypeName)){
            OleFormatType formatType = selectDAO.getFormatTypeByName(formatTypeName);
            if(formatType == null){
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Format Type : " + formatTypeName), recordIndex, exchange);
                oleTxRecord.setFormatTypeId(null);
                return false;
            }else{
                oleTxRecord.setFormatTypeId(formatType.getFormatTypeId().toString());
                return true;
            }
        }
        return true;
    }


    private void validateVendorCustomerNumber(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String vendorInfoCustomer = oleTxRecord.getVendorInfoCustomer();
        if (StringUtils.isNotBlank(vendorInfoCustomer)) {
            VendorCustomerNumber vendorCustomerNumber = getSelectDAO().getVendorCustomerNumberByNumber(vendorInfoCustomer);
            if (null == vendorCustomerNumber) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Acquisition Unit\'s Vendor account / Vendor Info Customer # : " + vendorInfoCustomer), recordIndex, exchange);
                oleTxRecord.setVendorInfoCustomer(null);
            }
        }
    }

    private boolean validateItemStatus(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String itemStatus = oleTxRecord.getItemStatus();
        if (StringUtils.isNotBlank(itemStatus)) {
            ItemStatusRecord itemStatusRecord = getOleNGMemorizeService().fetchItemStatusByName(itemStatus);
            if (null == itemStatusRecord) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Item Status : " + itemStatus), recordIndex, exchange);
                oleTxRecord.setItemStatus(null);
            } else {
                oleTxRecord.setItemStatus(itemStatusRecord.getCode());
                return true;
            }
        }
        return false;
    }


    private boolean validateDefaultLocation(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String defaultLocation = oleTxRecord.getDefaultLocation();
        if (StringUtils.isBlank(defaultLocation)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Default Location is invalid or null"), recordIndex, exchange);
        } else {
            boolean validLocation = SpringContext.getBean(OleDocstoreHelperService.class).isValidLocation(defaultLocation);
            if (validLocation) {
                return true;
            } else {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Default Location : " + defaultLocation), recordIndex, exchange);
            }
        }
        return false;
    }

    private boolean validateListPrice(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String listPrice = oleTxRecord.getListPrice();
        if (StringUtils.isBlank(listPrice)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("List price cannot be blank or null"), recordIndex, exchange);
        } else if (!NumberUtils.isNumber(listPrice)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Invalid List price : " + listPrice), recordIndex, exchange);
        } else {
            return true;
        }
        return false;
    }

    private boolean validateFundingSource(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String fundingSource = oleTxRecord.getFundingSource();
        if (StringUtils.isBlank(fundingSource)) {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Funding Source cannot be blank or null"), recordIndex, exchange);
        } else {
            FundingSource source = getSelectDAO().getFundingSourceByCode(fundingSource);
            if (null == source) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Funding Source : " + fundingSource), recordIndex, exchange);
            } else {
                return true;
            }
        }
        return false;
    }

    private void validateRecurringPaymentType(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String recurringPaymentTypeCode = oleTxRecord.getRecurringPaymentType();
        if (StringUtils.isNotBlank(recurringPaymentTypeCode)) {
            RecurringPaymentType recurringPaymentType = getSelectDAO().getRecurringPaymentTypeByCode(recurringPaymentTypeCode);
            if (null == recurringPaymentType) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Recurring Payment Type : " + recurringPaymentTypeCode), recordIndex, exchange);
                oleTxRecord.setRecurringPaymentType(null);
            }
        }
    }

    private void validateRequestSource(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String requestSourceType = oleTxRecord.getRequestSourceType();
        if (StringUtils.isNotBlank(requestSourceType)) {
            OleRequestSourceType oleRequestSourceType = getSelectDAO().getRequestSourceTypeByType(requestSourceType);
            if (null == oleRequestSourceType) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Request Source : " + requestSourceType), recordIndex, exchange);
                oleTxRecord.setRequestSourceType(null);
            }
        }
    }

    private void validateRequestorName(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String requestorName = oleTxRecord.getRequestorName();
        if (StringUtils.isNotBlank(requestorName)) {
            OLERequestorPatronDocument oleRequestorPatronDocument = getSelectDAO().getRequestorPatronDocumentByName(requestorName);
            if (null == oleRequestorPatronDocument) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Requestor Name : " + requestorName), recordIndex, exchange);
                oleTxRecord.setRequestorName(null);
            }
        }
    }

    private void validateVendorChoice(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String vendorChoice = oleTxRecord.getVendorChoice();
        if (StringUtils.isNotBlank(vendorChoice)) {
            PurchaseOrderVendorChoice purchaseOrderVendorChoice = getSelectDAO().getPurchaseOrderVendorChoiceByCode(vendorChoice);
            if (null == purchaseOrderVendorChoice) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Vendor Choice : " + vendorChoice), recordIndex, exchange);
                oleTxRecord.setVendorChoice(null);
            }
        }
    }

    private void validateAssignToUser(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String assignToUser = oleTxRecord.getAssignToUser();
        if (StringUtils.isNotBlank(assignToUser)) {
            Person assignedUser = SpringContext.getBean(PersonService.class).getPersonByPrincipalName(assignToUser);
            if (null == assignedUser) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Assign To User : " + assignToUser), recordIndex, exchange);
                oleTxRecord.setAssignToUser(null);
            }
        }
    }

    private void validateContractManager(OleTxRecord oleTxRecord, Exchange exchange, Integer recordIndex) {
        String contractManager = oleTxRecord.getContractManager();
        if (StringUtils.isNotBlank(contractManager)) {
            ContractManager manager = getSelectDAO().getContractManagerByName(contractManager);
            if (null == manager) {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Invalid Contract Manager : " + contractManager), recordIndex, exchange);
                oleTxRecord.setContractManager(null);
            }
        }
    }

    private boolean validateBibRecord(OleOrderRecord oleOrderRecord, Exchange exchange, Integer recordIndex) {
        OleBibRecord oleBibRecord = oleOrderRecord.getOleBibRecord();
        if(null != oleBibRecord.getBib()) {
            if(StringUtils.isNotBlank(oleBibRecord.getBib().getTitle())) {
                return true;
            } else {
                getBatchUtil().addOrderFaiureResponseToExchange(
                        new ValidationException("Bib Title is null or empty for bibId : " + oleBibRecord.getBibUUID()), recordIndex, exchange);
            }
        } else {
            getBatchUtil().addOrderFaiureResponseToExchange(
                    new ValidationException("Bib Record is null for bibId : " + oleBibRecord.getBibUUID()), recordIndex, exchange);
        }
        return false;
    }

    public BatchUtil getBatchUtil() {
        if (null == batchUtil) {
            batchUtil = new BatchUtil();
        }
        return batchUtil;
    }

    public SelectDAO getSelectDAO() {
        if (null == selectDAO) {
            selectDAO = new SelectDAOImpl();
        }
        return selectDAO;
    }

    public void setSelectDAO(SelectDAO selectDAO) {
        this.selectDAO = selectDAO;
    }

    public ItemUtil getItemUtil() {
        if(null == itemUtil) {
            itemUtil = new ItemUtil();
        }
        return itemUtil;
    }

    public void setItemUtil(ItemUtil itemUtil) {
        this.itemUtil = itemUtil;
    }

    public OleNGMemorizeService getOleNGMemorizeService() {
        if(null == oleNGMemorizeService) {
            oleNGMemorizeService = new OleNGMemorizeServiceImpl();
        }
        return oleNGMemorizeService;
    }

    public void setOleNGMemorizeService(OleNGMemorizeService oleNGMemorizeService) {
        this.oleNGMemorizeService = oleNGMemorizeService;
    }
}
