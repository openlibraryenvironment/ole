package org.kuali.ole.olekrad.authorization.form;

import org.kuali.ole.OLEPropertyConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.web.form.UifFormBase;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 8/22/13
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEKRADAuthorizationForm extends UifFormBase {
    private String information;
    private String docId;
    private String principalId;
    private String redirectUrl= ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE)+"/portal.do";
    private String error;

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
