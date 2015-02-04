package org.kuali.ole.select.document.web;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kns.web.struts.action.KualiAction;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 12/10/13
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEReqPOAskQuestionAction extends KualiAction {


    @Override
    protected void checkAuthorization(ActionForm form, String methodToCall)
            throws AuthorizationException {
        // no authorization required
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // deal with the fact that some requests might be reposts from errors on the reason field
        processErrorMessages(request);

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }


    public ActionForward processAnswer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEReqPOAskQuestionForm oleReqPOAskQuestionForm = (OLEReqPOAskQuestionForm) form;

        Properties parameters = new Properties();

        parameters.put(KRADConstants.DOC_FORM_KEY, oleReqPOAskQuestionForm.getFormKey());
        parameters.put(KRADConstants.QUESTION_CLICKED_BUTTON, getSelectedButton(request));
        parameters.put(KRADConstants.METHOD_TO_CALL_ATTRIBUTE, oleReqPOAskQuestionForm.getCaller());
        parameters.put(KRADConstants.REFRESH_CALLER, KRADConstants.QUESTION_REFRESH);
        parameters.put(KRADConstants.QUESTION_INST_ATTRIBUTE_NAME, oleReqPOAskQuestionForm.getQuestionIndex());
        if(oleReqPOAskQuestionForm.getDocNum() != null){
            parameters.put(KRADConstants.DOC_NUM, oleReqPOAskQuestionForm.getDocNum());
        }

        if (StringUtils.isNotBlank(oleReqPOAskQuestionForm.getQuestionAnchor())) {
            parameters.put(KRADConstants.ANCHOR, oleReqPOAskQuestionForm.getQuestionAnchor());
        }

        String context = oleReqPOAskQuestionForm.getContext();
        if (StringUtils.isNotBlank(context)) {
            parameters.put(KRADConstants.QUESTION_CONTEXT, context);
        }

        if (StringUtils.isNotBlank(oleReqPOAskQuestionForm.getCancellationReason()) || StringUtils.isNotBlank(oleReqPOAskQuestionForm.getReason())) {
            String reason = oleReqPOAskQuestionForm.getCancellationReason()+ OLEConstants.BLANK_SPACE + "/" + OLEConstants.BLANK_SPACE + oleReqPOAskQuestionForm.getReason();
            parameters.put(KRADConstants.QUESTION_REASON_ATTRIBUTE_NAME, reason);
        }

        if (StringUtils.isNotBlank(oleReqPOAskQuestionForm.getMethodToCallPath())) {
            // For header tab navigation. Leaving it blank will just kick user back to page.
            parameters.put(oleReqPOAskQuestionForm.getMethodToCallPath(), "present");
        }

        String returnUrl = UrlFactory.parameterizeUrl( oleReqPOAskQuestionForm.getBackLocation(), parameters);

        return new ActionForward(returnUrl, true);
    }


/**
     * Parses the method to call attribute to pick off the button number that was pressed.
     *
     * @param request
     * @return int
     */

    private String getSelectedButton(HttpServletRequest request) {
        String selectedButton = "-1";
        String parameterName = (String) request.getAttribute(KRADConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName)) {
            selectedButton = StringUtils.substringBetween(parameterName, ".button", ".");
        }

        return selectedButton;
    }


    private void processErrorMessages(HttpServletRequest request) {
        String errorKey = request.getParameter(KRADConstants.QUESTION_ERROR_KEY);
        String errorPropertyName = request.getParameter(KRADConstants.QUESTION_ERROR_PROPERTY_NAME);
        String errorParameter = request.getParameter(KRADConstants.QUESTION_ERROR_PARAMETER);

        if (StringUtils.isNotBlank(errorKey)) {
            if (StringUtils.isBlank(errorPropertyName)) {
                throw new IllegalStateException("Both the errorKey and the errorPropertyName must be filled in, " + "in order for errors to be displayed by the question component.  Currently, " + "only the errorKey has a value specified.");
            }
            else {
                if (StringUtils.isBlank(errorParameter)) {
                    GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(errorPropertyName, errorKey);
                }
                else {
                    GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(errorPropertyName, errorKey, errorParameter);
                }
            }
        }
    }
}


