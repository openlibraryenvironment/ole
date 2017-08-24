package org.kuali.ole.describe.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.DocumentSelectionTree;
import org.kuali.ole.describe.bo.DocumentTreeNode;
import org.kuali.ole.describe.form.AnalyticsForm;
import org.kuali.ole.describe.form.BoundwithForm;
import org.kuali.ole.describe.rule.OLEAnalyticsRule;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 3/3/14
 * Time: 8:32 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/analyticsController")
public class AnalyticsController extends BoundwithController {

    private static final Logger LOG = Logger.getLogger(AnalyticsController.class);

    private OLEAnalyticsRule oleAnalyticsRule;

    public OLEAnalyticsRule getOleAnalyticsRule() {
        if (oleAnalyticsRule == null) {
            oleAnalyticsRule = new OLEAnalyticsRule();
        }
        return oleAnalyticsRule;
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new AnalyticsForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the AnalyticsForm start method");
        if (request.getSession().getAttribute("LeftList") != null && request.getSession().getAttribute("RightList") != null) {
            request.getSession().removeAttribute("LeftList");
            request.getSession().removeAttribute("RightList");
        }
        return super.start(form, result, request, response);
    }

    /**
     * This method creates the analytic relation if the uses clicks on Create Analytics relation button.
     *
     * @param form
     * @param result
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=createAnalyticsRelation")
    public ModelAndView createAnalyticsRelation(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response) throws Exception {

        AnalyticsForm analyticsForm = (AnalyticsForm) form;
        Boolean isInputValid = getOleAnalyticsRule().validateInputForAnalytics(analyticsForm, OLEConstants.CREATE_ANALYTICS);
        if (!isInputValid) {
            return getUIFModelAndView(analyticsForm);
        }

        Tree<DocumentTreeNode, String> seriesTree = analyticsForm.getLeftTree();
        Node<DocumentTreeNode, String> seriesTreeRootElement = seriesTree.getRootElement();
        getOleAnalyticsRule().selectCheckedNodesForSeriesTree(analyticsForm, seriesTreeRootElement);

        Tree<DocumentTreeNode, String> analyticsTree = analyticsForm.getRightTree();
        Node<DocumentTreeNode, String> analyticsTreeRootElement = analyticsTree.getRootElement();
        getOleAnalyticsRule().selectCheckedNodesForAnalyticTree(analyticsForm, analyticsTreeRootElement);

        analyticsForm.getDocumentTreeNode().setReturnCheck(true);
        addAnalyticRelation(analyticsForm);
        return super.navigate(analyticsForm, result, request, response);
    }

    private void addAnalyticRelation(AnalyticsForm analyticsForm) {
        try {
            getDocstoreClientLocator().getDocstoreClient().createAnalyticsRelation(analyticsForm.getSelectedHoldingsFromTree1().get(0), analyticsForm.getSelectedItemsFromTree2());
            analyticsForm.setShowBoundwithTree(true);
            List<String> uuidList = new ArrayList<String>();
            uuidList = analyticsForm.getSelectedBibs();
            uuidList.add(analyticsForm.getTree1BibId());
            DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
            Node<DocumentTreeNode, String> rootNode = documentSelectionTree.add(uuidList, DocType.BIB.getDescription(), true);
            analyticsForm.getBoundwithTree().setRootElement(rootNode);
            GlobalVariables.getMessageMap().putInfoForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.INFO_ANALYTICS_CREATE_SUCCESS);
            analyticsForm.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INFO_ANALYTICS_CREATE_SUCCESS));
            analyticsForm.setBoundwithTreeLabelText("select");
        } catch (DocstoreException e) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, e.getErrorCode());
            analyticsForm.setMessage(e.getErrorCode());
        } catch (Exception e) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_ANALYTICS_CREATE_FAILED, e.getMessage());
            analyticsForm.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ERROR_ANALYTICS_CREATE_FAILED)+e.getMessage());
        }
        analyticsForm.setSelectedHoldings(null);
    }

    /**
     * This method displays Analytics Summary if the uses clicks on Series or Analytic Link.
     *
     * @param form
     * @param result
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=showAnalyticsSummary")
    public ModelAndView showAnalyticsSummary(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        AnalyticsForm analyticsForm = (AnalyticsForm) form;
        String docType = request.getParameter("docType");
        String holdingsId = request.getParameter("holdingsId");
        String itemId = request.getParameter("itemId");
        if (StringUtils.isNotBlank(holdingsId) && StringUtils.isBlank(itemId) && docType.equalsIgnoreCase(DocType.HOLDINGS.getDescription())) {
            getOleAnalyticsRule().getAnalyticsSummaryByHoldingsId(analyticsForm, holdingsId);
        } else if (StringUtils.isBlank(holdingsId) && StringUtils.isNotBlank(itemId) && docType.equalsIgnoreCase(DocType.ITEM.getDescription())) {
            getOleAnalyticsRule().getAnalyticsSummaryByItemId(analyticsForm, itemId);
        }
        return super.navigate(analyticsForm, result, request, response);
    }

    /**
     * This method breaks the analytic relation if the uses clicks on Break Analytics relation button.
     *
     * @param form
     * @param result
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=breakAnalyticsRelation")
    public ModelAndView breakAnalyticsRelation(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        AnalyticsForm analyticsForm = (AnalyticsForm) form;

        Boolean isInputValid = getOleAnalyticsRule().validateInputForAnalytics(analyticsForm, OLEConstants.BREAK_ANALYTICS);
        if (!isInputValid) {
            return getUIFModelAndView(analyticsForm);
        }

        String seriesHoldingsId = null;
        Tree<DocumentTreeNode, String> seriesTree = analyticsForm.getLeftTree();
        Node<DocumentTreeNode, String> seriesTreeRootElement = seriesTree.getRootElement();
        //selectCheckedNodesForSeriesTree(analyticsForm, seriesTreeRootElement);
        List<Node<DocumentTreeNode, String>> seriesBibNode = null;
        List<Node<DocumentTreeNode, String>> seriesHoldingsNode = null;
        if (seriesTreeRootElement != null && !CollectionUtils.isEmpty(seriesTreeRootElement.getChildren())) {
            seriesBibNode = seriesTreeRootElement.getChildren();
            if (null != seriesBibNode.get(0) && !CollectionUtils.isEmpty(seriesBibNode.get(0).getChildren())) {
                seriesHoldingsNode = seriesBibNode.get(0).getChildren();
                for(int i=0;seriesHoldingsNode.size()>i;i++){
                    if (null != seriesHoldingsNode.get(i) && null != seriesHoldingsNode.get(i).getNodeType()&&seriesHoldingsNode.get(i).getNodeLabel().contains(">SH<")) {
                        seriesHoldingsId = seriesHoldingsNode.get(i).getNodeType();
                        break;
                    }
                }
            }
        }
        Tree<DocumentTreeNode, String> analyticsTree = analyticsForm.getRightTree();
        Node<DocumentTreeNode, String> analyticsTreeRootElement = analyticsTree.getRootElement();
        getOleAnalyticsRule().selectCheckedNodesForAnalyticTree(analyticsForm, analyticsTreeRootElement);

        analyticsForm.getDocumentTreeNode().setReturnCheck(true);
        try {
            getDocstoreClientLocator().getDocstoreClient().breakAnalyticsRelation(seriesHoldingsId, analyticsForm.getSelectedItemsFromTree2());
            GlobalVariables.getMessageMap().putInfo(OLEConstants.ANALYTICS_SUMMARY_VIEW_PAGE, OLEConstants.INFO_ANALYTICS_BREAK_SUCCESS);
        } catch (DocstoreException e) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, e.getErrorCode());
        } catch (Exception e) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_ANALYTICS_BREAK_FAILED, e.getMessage());
        }
        return getUIFModelAndView(analyticsForm);
    }

    /**
     * This method refreshes the Series Tree and Analytic Tree if the uses clicks on Refresh Trees button.
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=refreshTrees")
    public ModelAndView refreshTrees(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        AnalyticsForm analyticsForm = (AnalyticsForm) form;
        LOG.info("refreshTrees Method");
        Map<String, String> uuidLeftList = (Map<String, String>) request.getSession().getAttribute("LeftList");
        if (!CollectionUtils.isEmpty(uuidLeftList)) {
            List<String> uuidList = new ArrayList<>(uuidLeftList.values());
            if (!CollectionUtils.isEmpty(uuidList)) {
                BibTree bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(uuidList.get(0));
                for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                    getOleAnalyticsRule().getAnalyticsSummaryByHoldingsId(analyticsForm, holdingsTree.getHoldings().getId());
                }
            }
        }
        return navigate(analyticsForm, result, request, response);
    }

}
