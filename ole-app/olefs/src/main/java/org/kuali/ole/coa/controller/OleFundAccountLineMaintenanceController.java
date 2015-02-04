package org.kuali.ole.coa.controller;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.coa.businessobject.*;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 9/30/14
 * Time: 7:26 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/addAccountingLineValidator")
public class OleFundAccountLineMaintenanceController extends MaintenanceDocumentController {

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addAccountingLine")
    public ModelAndView addAccountingLine(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        /*BindingInfo addLineBindingInfo = (BindingInfo) form.getViewPostMetadata().getComponentPostData(
                selectedCollectionId, UifConstants.PostMetadata.ADD_LINE_BINDING_INFO);*/
        CollectionGroup collectionGroup = form.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object accountingLineObject = ObjectPropertyUtils.getPropertyValue(maintenanceForm, addLinePath);
        OleFundCodeAccountingLine oleFundCodeAccountingLine = (OleFundCodeAccountingLine) accountingLineObject;
        boolean error = false;
        //blank validation
        if (StringUtils.isBlank(oleFundCodeAccountingLine.getChartCode()) || StringUtils.isBlank(oleFundCodeAccountingLine.getAccountNumber()) || StringUtils.isBlank(oleFundCodeAccountingLine.getObjectCode()) || ObjectUtils.isNull(oleFundCodeAccountingLine.getPercentage())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_MANDATORY_FIELDS);
            return getUIFModelAndView(form);
        }
        //chart code validation
        Chart chart = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(Chart.class, oleFundCodeAccountingLine.getChartCode());
        if (chart == null) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_CHART_CODE);
            error = true;
        }
        //Account no validation
        Map accNoMap = new HashMap();
        accNoMap.put(OLEConstants.ACCOUNT_NUMBER, oleFundCodeAccountingLine.getAccountNumber());
        Account account = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(Account.class, accNoMap);
        if (account == null) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_ACCOUNT_NUM);
            error = true;
        } else {
            accNoMap = new HashMap();
            accNoMap.put(OLEConstants.ACCOUNT_NUMBER, oleFundCodeAccountingLine.getAccountNumber());
            accNoMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, oleFundCodeAccountingLine.getChartCode());
            List<Account> accountList = (List<Account>) KRADServiceLocator.getBusinessObjectService().findMatching(Account.class, accNoMap);
            if (accountList.size() == 0) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_COMBINATION_ACCOUNT_NUM);
                error = true;
            }
        }
        //Object Code validation
        UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
        Integer fiscalYear = universityDateService.getCurrentUniversityDate().getUniversityFiscalYear();
        Map objectCodeMap = new HashMap();
        objectCodeMap.put(OLEConstants.OLEBatchProcess.OBJECT_CODE, oleFundCodeAccountingLine.getObjectCode());
        objectCodeMap.put(org.kuali.ole.sys.OLEConstants.FISCAL_YEAR, fiscalYear);
        List<ObjectCode> objectCodeList = (List<ObjectCode>) KRADServiceLocator.getBusinessObjectService().findMatching(ObjectCode.class, objectCodeMap);
        if (objectCodeList.size() == 0) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_OBJECT_CODE);
            error = true;
        }
        //Sub-Account no validation
        String subAccNo = oleFundCodeAccountingLine.getSubAccount();
        if (StringUtils.isNotBlank(subAccNo)) {
            Map subAccNoMap = new HashMap();
            subAccNoMap.put(OLEConstants.SUB_ACCOUNT_NUMBER, subAccNo);
            SubAccount subAccount = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(SubAccount.class, subAccNoMap);
            if (subAccount == null) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_SUB_ACCOUNT_NUM);
                error = true;
            } else {
                subAccNoMap = new HashMap();
                subAccNoMap.put(OLEConstants.SUB_ACCOUNT_NUMBER, subAccNo);
                subAccNoMap.put(OLEConstants.ACCOUNT_NUMBER, oleFundCodeAccountingLine.getAccountNumber());
                subAccNoMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, oleFundCodeAccountingLine.getChartCode());
                List<SubAccount> subAccountList = (List<SubAccount>) KRADServiceLocator.getBusinessObjectService().findMatching(SubAccount.class, subAccNoMap);
                if (subAccountList.size() == 0) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_COMBINATION_SUB_ACCOUNT_NUM);
                    error = true;
                }
            }
        }
        //Sub Object Code validation
        String subObjectCode = oleFundCodeAccountingLine.getSubObject();
        if (StringUtils.isNotBlank(subObjectCode)) {
            Map subObjectCodeMap = new HashMap();
            subObjectCodeMap.put(OLEConstants.FINANCIAL_SUB_OBJECT_CODE, subObjectCode);
            SubObjectCode subObject = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(SubObjectCode.class, subObjectCodeMap);
            if (subObject == null) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_SUB_OBJECT_CODE);
                error = true;
            } else {
                subObjectCodeMap = new HashMap();
                subObjectCodeMap.put(OLEConstants.FINANCIAL_SUB_OBJECT_CODE, subObjectCode);
                subObjectCodeMap.put(OLEConstants.ACCOUNT_NUMBER, oleFundCodeAccountingLine.getAccountNumber());
                subObjectCodeMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, oleFundCodeAccountingLine.getChartCode());
                subObjectCodeMap.put(OLEConstants.OLEBatchProcess.OBJECT_CODE, oleFundCodeAccountingLine.getObjectCode());
                subObjectCodeMap.put(org.kuali.ole.sys.OLEConstants.FISCAL_YEAR, fiscalYear);
                List<SubObjectCode> subObjectCodeList = (List<SubObjectCode>) KRADServiceLocator.getBusinessObjectService().findMatching(SubObjectCode.class, subObjectCodeMap);
                if (subObjectCodeList.size() == 0) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_COMBINATION_SUB_OBJECT_CODE);
                    error = true;
                }
            }
        }
        //Project Code validation
        String projectCode = oleFundCodeAccountingLine.getProject();
        if (StringUtils.isNotBlank(projectCode)) {
            Map projectCodeMap = new HashMap();
            projectCodeMap.put(OLEConstants.CODE, projectCode);
            List<ProjectCode> projectCodeList = (List<ProjectCode>) KRADServiceLocator.getBusinessObjectService().findMatching(ProjectCode.class, projectCodeMap);
            if (projectCodeList.size() == 0) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_FUND_CODE_ACCOUNTING_LINE, OLEConstants.OLEEResourceRecord.ERROR_INVALID_PROJECT_CODE);
                error = true;
            }
        }
        if (!error) {
            ModelAndView modelAndView = super.addLine(form,result,request,response);
            return modelAndView;
        } else {
            return getUIFModelAndView(form);
        }
    }

}
