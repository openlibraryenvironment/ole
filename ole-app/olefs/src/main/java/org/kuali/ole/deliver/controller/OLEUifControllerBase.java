package org.kuali.ole.deliver.controller;

import org.apache.commons.lang3.StringUtils;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by sheiksalahudeenm on 8/3/15.
 */
public abstract class OLEUifControllerBase extends UifControllerBase {

    protected ModelAndView showLightBoxForUrl(String url, UifFormBase form) {
        form.setLightboxScript("openLightboxUrl('" + url +"');");
        return getUIFModelAndView(form);
    }

    protected ModelAndView showLightBoxForUrlWichCustomScript(String url, UifFormBase form,String customScript) {
        form.setLightboxScript("openLightboxUrl('" + url +"');"+ (StringUtils.isNotBlank(customScript) ? customScript + ";" : ""));
        return getUIFModelAndView(form);
    }

    protected ModelAndView showIFrameDialog(String url, UifFormBase form, String customScript) {
        String script = "var iframeDialog = jq('<div></div>')\n" +
                "               .html('<iframe style=\"border: 0px; \" src=\"" + url +"\" width=\"100%\" height=\"100%\"></iframe>')\n" +
                "               .dialog({\n" +
                "                   autoOpen: false,\n" +
                "                   modal: true,\n" +
                "                   height: 800,\n" +
                "                   width: 700,\n" +
                "                   close: function( event, ui ) {" + customScript + "}\n " +
                "               });\n" +
                "iframeDialog.dialog('open');";
        form.setLightboxScript(script);
        return getUIFModelAndView(form);
    }

    protected ModelAndView closeLightBoxForUrl(UifFormBase form) {
        form.setLightboxScript("jq.fancybox.close();");
        return getUIFModelAndView(form);
    }

    protected ModelAndView executeCustomScriptAfterClosingLightBox(UifFormBase form,String customScript) {
        form.setLightboxScript("closeLightbox();"+ (StringUtils.isNotBlank(customScript) ? customScript + ";" : ""));
        return getUIFModelAndView(form);
    }

    protected ModelAndView executeCustomScriptBeforeClosingLightBox(UifFormBase form,String customScript) {
        form.setLightboxScript((StringUtils.isNotBlank(customScript) ? customScript + ";" : "") + "closeLightbox();");
        return getUIFModelAndView(form);
    }

    protected ModelAndView showDialogAndRunCustomScript(String dialogId,UifFormBase form, String customScript) {
        form.setLightboxScript("openLightboxOnLoad('" + dialogId + "');"+ (StringUtils.isNotBlank(customScript) ? customScript + ";" : ""));
        form.getDialogManager().addDialog(dialogId, form.getMethodToCall());
        if (form.isAjaxRequest()) {
            form.setAjaxReturnType(UifConstants.AjaxReturnTypes.UPDATEDIALOG.getKey());
            form.setUpdateComponentId(dialogId);
        }

        return getUIFModelAndView(form);
    }

    protected ModelAndView showDialogWithOverrideParameters(String dialogId,UifFormBase form, String overrideParameters) {
        form.setLightboxScript("openLightboxOnLoadWithOverrideParameters('" + dialogId + "', " + overrideParameters + ");");
        form.getDialogManager().addDialog(dialogId, form.getMethodToCall());
        if (form.isAjaxRequest()) {
            form.setAjaxReturnType(UifConstants.AjaxReturnTypes.UPDATEDIALOG.getKey());
            form.setUpdateComponentId(dialogId);
        }

        return getUIFModelAndView(form);
    }



    protected ModelAndView showHtmlContentToDialog(String content, UifFormBase form, String customScript, String dialogTitle) {
        content = content.replaceAll(System.lineSeparator(),"");
        content = content.replaceAll("'","&#39;");
        String script = "" +
                "var content = '" + content + "';var iframeDialog = jq('<div></div>')\n" +
                "               .html(content)\n" +
                "               .dialog({\n" +
                "                   autoOpen: false,\n" +
                "                   modal: true,\n" +
                "                   height: 800,\n" +
                "                   width: 700,\n" +
                "                   title: '" + dialogTitle + "'," +
                "                   close: function( event, ui ) {" + customScript + "}\n " +
                "               });\n" +
                "iframeDialog.dialog('open');";
        form.setLightboxScript(script);
        return getUIFModelAndView(form);
    }
}
