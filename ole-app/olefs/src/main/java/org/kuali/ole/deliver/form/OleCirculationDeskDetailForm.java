package org.kuali.ole.deliver.form;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDeskDetail;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/16/12
 * Time: 9:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleCirculationDeskDetailForm extends UifFormBase {

    private String operatorId;
    private String roleName = OLEConstants.OleCirculationDeskDetail.OPERATOR_ROLE_NAME;
    private String roleNamespaceCode = OLEConstants.OleCirculationDeskDetail.OPERATOR_ROLE_NAMESPACE;
    private String message;

    public String getErrorAuthrisedUserMessage() {
        return errorAuthrisedUserMessage;
    }

    public void setErrorAuthrisedUserMessage(String errorAuthrisedUserMessage) {
        this.errorAuthrisedUserMessage = errorAuthrisedUserMessage;
    }

    private String errorAuthrisedUserMessage;


    private List<OleCirculationDeskDetail> oleCirculationDetailsCreateList;

    /**
     * Gets the oleCirculationDetailsCreateList attribute
     *
     * @return oleCirculationDetailsCreateList
     */
    public List<OleCirculationDeskDetail> getOleCirculationDetailsCreateList() {
        return oleCirculationDetailsCreateList;
    }

    /**
     * Sets the oleCirculationDetailsCreateList attribute value.
     *
     * @param oleCirculationDetailsCreateList
     *         The oleCirculationDetailsCreateList to set.
     */
    public void setOleCirculationDetailsCreateList(List<OleCirculationDeskDetail> oleCirculationDetailsCreateList) {
        this.oleCirculationDetailsCreateList = oleCirculationDetailsCreateList;
    }

    /**
     * Gets the operatorId attribute.
     *
     * @return Returns the operatorId
     */
    public String getOperatorId() {
        return operatorId;
    }

    /**
     * Sets the operatorId attribute value.
     *
     * @param operatorId The operatorId to set.
     */
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * Gets the roleNamespaceCode attribute.
     *
     * @return Returns the roleNamespaceCode
     */
    public String getRoleNamespaceCode() {
        return roleNamespaceCode;
    }

    /**
     * Sets the roleNamespaceCode attribute value.
     *
     * @param roleNamespaceCode The roleNamespaceCode to set.
     */
    public void setRoleNamespaceCode(String roleNamespaceCode) {
        this.roleNamespaceCode = roleNamespaceCode;
    }

    /**
     * Gets the roleName attribute.
     *
     * @return Returns the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Sets the roleName attribute value.
     *
     * @param roleName The roleName to set.
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * Gets the message attribute.
     *
     * @return Returns the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message attribute value.
     *
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
