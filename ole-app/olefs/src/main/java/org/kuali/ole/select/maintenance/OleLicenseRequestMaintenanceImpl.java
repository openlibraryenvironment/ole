package org.kuali.ole.select.maintenance;

import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.License;
import org.kuali.ole.select.bo.OleLicenseRequestBo;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.krad.maintenance.MaintainableImpl;

/**
 * Created with IntelliJ IDEA.
 * User: Juliya Monica.S
 * Date: 3/17/13
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLicenseRequestMaintenanceImpl extends MaintainableImpl {

    private DocstoreClientLocator docstoreClientLocator;
    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return  SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }


    /* This method is used to view the E-Resource document
    * @param oleLicenseRequestBo
    * @return  redirectUrl
            */
    public String getUrl(OleLicenseRequestBo oleLicenseRequestBo) {
        String oleurl = ConfigContext.getCurrentContextConfig().getProperty("ole.url");
        //String olePortal = oleurl.substring(0,oleurl.indexOf("portal.jsp"));
        //String requisitionDocNumber = request.getParameter(OLEConstants.OleLicenseRequest.REQUISITION_DOC_NUM);
        String redirectUrl = "";
        String eResourceDocNumber = "";
        if (oleLicenseRequestBo != null) {
            eResourceDocNumber = oleLicenseRequestBo.geteResourceDocNumber();
            redirectUrl = oleurl + "/" + KewApiConstants.Namespaces.MODULE_NAME + "/" +
                    KewApiConstants.DOC_HANDLER_REDIRECT_PAGE + "?" + KewApiConstants.COMMAND_PARAMETER + "=" +
                    KewApiConstants.DOCSEARCH_COMMAND + "&" + KewApiConstants.DOCUMENT_ID_PARAMETER + "="
                    + eResourceDocNumber;
        }
        return redirectUrl;
    }

    /* This method is used to view the agreement linked to the license request
    * @param oleLicenseRequestBo
    * @return  redirectUrl
            */
    public String viewAgreement(OleLicenseRequestBo oleLicenseRequestBo) {
        String url = ConfigContext.getCurrentContextConfig().getProperty("ole.docstore.Documentrest.url");
        String redirectUrl = "";
        if (oleLicenseRequestBo != null && oleLicenseRequestBo.getAgreementId() != null) {
//            redirectUrl = url + "?docAction=checkOut&uuid=" + oleLicenseRequestBo.getAgreementId();
            redirectUrl = url + "license/" + oleLicenseRequestBo.getAgreementId();
        }
        return redirectUrl;
    }

    /* This method is used to get E-Resource Document Number
    * @param oleLicenseRequestBo
    * @return  eResourceDocNumber
    */
    public String getDocNum(OleLicenseRequestBo oleLicenseRequestBo) {
        String oleurl = ConfigContext.getCurrentContextConfig().getProperty("ole.url");
        String eResourceDocNumber = "";
        if (oleLicenseRequestBo != null) {
            eResourceDocNumber = oleLicenseRequestBo.geteResourceDocNumber();
        }
        return eResourceDocNumber;
    }

}
