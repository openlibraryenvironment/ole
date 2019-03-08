package org.kuali.ole.deliver.bo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.deliver.util.ItemFineRate;
import org.kuali.ole.deliver.util.LoanDateTimeUtil;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.service.OleCirculationPolicyServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * The OleLoanDocument is a BO class that defines the loan document fields with getters and setters which
 * is used for interacting the loan data with the persistence layer in OLE.
 */
public class OleDueDateDocument extends PersistableBusinessObjectBase {
    private String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
    private String dueDateId;
    private String loanId;
    private String patronBarcode;
    private String itemBarcode;
    private Timestamp loanDueDate;
    private String loanOperatorId;
    private Date pastDueDate;
    private Timestamp updatedDateTime;

    /**
     * Gets the patronBarcode attribute.
     *
     * @return Returns the patronBarcode
     */
    public String getPatronBarcode() {
        return patronBarcode;
    }

    /**
     * Sets the patronBarcode attribute value.
     *
     * @param patronBarcode The patronBarcode to set.
     */
    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    /**
            * Gets the loanId attribute.
            *
            * @return Returns the loanId
    */
    public String getLoanId() {
        return loanId;
    }

    /**
     * Sets the loanId attribute value.
     *
     * @param loanId The loanId to set.
     */
    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    /**
            * Gets the dueDateId attribute.
            *
            * @return Returns the dueDateId
    */
    public String getDueDateId() {
        return dueDateId;
    }

    /**
     * Sets the dueDateId attribute value.
     *
     * @param dueDateId The loanId to set.
     */
    public void setDueDateId(String dueDateId) {
        this.dueDateId = dueDateId;
    }

       /**
            * Gets the itemBarcode attribute.
            *
            * @return Returns the itemBarcode
    */
    public String getItemBarcode() {
        return itemBarcode;
    }

    /**
     * Sets the itemBarcode attribute value.
     *
     * @param itemBarcode The itemBarcode to set.
     */
    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    /**
            * Gets the loanOperatorId attribute.
            *
            * @return Returns the loanOperatorId
    */
    public String getLoanOperatorIdd() {
        return loanOperatorId;
    }

    /**
     * Sets the loanOperatorId attribute value.
     *
     * @param loanOperatorId The loanOperatorId to set.
     */
    public void setLoanOperatorId(String loanOperatorId) {
        this.loanOperatorId = loanOperatorId;
    }


    /**
     * Gets the loanDueDate attribute.
     *
     * @return Returns the loanDueDate
     */
    public Timestamp getLoanDueDate() {
        return loanDueDate;
    }

    /**
     * Sets the loanDueDate attribute value.
     *
     * @param loanDueDate The loanDueDate to set.
     */
    public void setLoanDueDate(Timestamp loanDueDate) {
        this.loanDueDate = loanDueDate;
    }

    /**
     * Gets the pastDueDate attribute.
     *
     * @return Returns the pastDueDate
     */
    public Date getPastDueDate() {
        return pastDueDate;
    }

    /**
     * Sets the pastDueDate attribute value.
     *
     * @param pastDueDate The pastDueDate to set.
     */
    public void setPastDueDate(Date pastDueDate) {
        this.pastDueDate = pastDueDate;
    }

    /**
     * Gets the updatedDateTime attribute.
     *
     * @return Returns the updatedDateTime
     */
    public Timestamp getUpdatedDateTime() {
        return updatedDateTime;
    }

    /**
     * Sets the updatedDateTime attribute value.
     *
     * @param updatedDateTime The updatedDateTime to set.
     */
    public void setUpdatedDateTime(Timestamp updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

}
