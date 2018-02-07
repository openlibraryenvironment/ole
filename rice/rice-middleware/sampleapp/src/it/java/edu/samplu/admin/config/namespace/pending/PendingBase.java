package edu.samplu.admin.config.namespace.pending;

import edu.samplu.admin.test.AdminTmplMthdSTNavBase;
import edu.samplu.common.ITUtil;
import org.apache.commons.lang.RandomStringUtils;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public abstract class PendingBase extends AdminTmplMthdSTNavBase {
    /**
     * methodToCall.insertAdHocRoutePerson
     */
    protected static final String ADD_PERSON_ELEMENT_NAME = "methodToCall.insertAdHocRoutePerson";

    /**
     * newAdHocRoutePerson.id
     */
    protected static final String ADD_PERSON_TEXT_ELEMENT_NAME = "newAdHocRoutePerson.id";

    /**
     * methodToCall.toggleTab.tabAdHocRecipients
     */
    protected static final String DOCUMENT_AD_HOC_RECIPIENTS_TOGGLE = "methodToCall.toggleTab.tabAdHocRecipients";
    /**
     * document.documentHeader.documentDescription
     */
    protected static final String DOCUMENT_DESCRIPTION_NAME = "document.documentHeader.documentDescription";

    /**
     * document.newMaintainableObject.code
     */
    protected static final String DOCUMENT_CODE_NAME = "document.newMaintainableObject.code";

    /**
     * document.newMaintainableObject.name
     */
    protected static final String DOCUMENT_NAME = "document.newMaintainableObject.name";

    /**
     * document.newMaintainableObject.applicationId
     */
    protected static final String DOCUMENT_APPLICATIONID_NAME = "document.newMaintainableObject.applicationId";

    /**
     * methodToCall.performLookup.(!!org.kuali.rice.kim.impl.group.GroupBo!!).(((namespaceCode:newAdHocRouteWorkgroup.recipientNamespaceCode,name:newAdHocRouteWorkgroup.recipientName))).((`newAdHocRouteWorkgroup.recipientNamespaceCode:namespaceCode,newAdHocRouteWorkgroup.recipientName:name`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;;::::).anchor
     */
    protected static final String NAME_LOOKUP_COMPONENT_NAME ="methodToCall.performLookup.(!!org.kuali.rice.kim.impl.group.GroupBo!!).(((namespaceCode:newAdHocRouteWorkgroup.recipientNamespaceCode,name:newAdHocRouteWorkgroup.recipientName))).((`newAdHocRouteWorkgroup.recipientNamespaceCode:namespaceCode,newAdHocRouteWorkgroup.recipientName:name`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;;::::).anchor";

    /**
     * newAdHocRoutePerson.actionRequested
     */
    protected static final String NEW_AD_HOC_ROUTE_PERSON_ACTION_REQUESTED = "newAdHocRoutePerson.actionRequested";

    /**
     * methodToCall.route
     */
    protected static final String SUBMIT_NAME = "methodToCall.route";

    protected void assertSuperGroup(String docId) throws InterruptedException {
        // second checkbox (APPROVED)
        waitAndClick("#tab-SuperUserAction-div > div:nth-child(2) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(1) > input:nth-child(1)");
        waitAndTypeByName("superUserAnnotation","test suaction");
        waitAndClickByName("methodToCall.takeSuperUserActions");
        if(!isTextPresent("Group1")) {
            assertTextPresent("superuser approved in Document " + docId);
        } else {
            fail("Super User Approve functionality may not be working.");
        }
    }
        /**
        * {@inheritDoc}
        * Namespace
        * @return
        */
    @Override
    protected String getLinkLocator() {
        return "Namespace";
    }

    protected void fillInAddAdHocPersons(String[][] personsActions) throws Exception {
        waitAndClickByName(DOCUMENT_AD_HOC_RECIPIENTS_TOGGLE);

        for (String[] personAction: personsActions) {
            waitAndTypeByName(ADD_PERSON_TEXT_ELEMENT_NAME, personAction[0]);
            if (personAction.length > 1) {
                selectByName(NEW_AD_HOC_ROUTE_PERSON_ACTION_REQUESTED, personAction[1]);
            }
            waitAndClickByName(ADD_PERSON_ELEMENT_NAME);
            Thread.sleep(1000);
        }
    }

    protected void fillInAdHocGroups(String[][] groupsActions) throws Exception {
        waitAndClickByName(DOCUMENT_AD_HOC_RECIPIENTS_TOGGLE);
        for (String[] groupAction: groupsActions) {
            waitAndClickByName(NAME_LOOKUP_COMPONENT_NAME);
            waitAndTypeByName("name", groupAction[0]);
            waitAndClickByXpath(SEARCH_XPATH);
            waitAndClickByLinkText("return value");
            if (groupAction.length > 1) {
                selectByName("newAdHocRouteWorkgroup.actionRequested", groupAction[1]);
            }
            waitAndClickByName("methodToCall.insertAdHocRouteWorkgroup");
            Thread.sleep(1000);
        }
    }

    protected void fillInNamespaceOverview(String descBase, String codeNameBase, String nameBase, String appId) throws Exception {
        selectFrameIframePortlet();
        waitAndCreateNew();
        waitAndTypeByName(DOCUMENT_DESCRIPTION_NAME, descBase + " " + ITUtil.createUniqueDtsPlusTwoRandomCharsNot9Digits());
        String randomFour = RandomStringUtils.randomAlphabetic(4).toLowerCase();
        waitAndTypeByName(DOCUMENT_CODE_NAME, codeNameBase + randomFour);
        waitAndTypeByName(DOCUMENT_NAME, nameBase + randomFour);
        waitAndTypeByName(DOCUMENT_APPLICATIONID_NAME, appId);
    }

    protected String submitAndLookupDoc() throws InterruptedException {
        waitAndClickByName(SUBMIT_NAME);
        String docId= waitForDocId();
        switchToWindow("Kuali Portal Index");
        waitAndClickDocSearch();
        selectFrameIframePortlet();
        waitAndTypeByName("documentId",docId);
        waitAndClickByXpath(SEARCH_XPATH);
        if(!isTextPresent("No values match this search.")) {
            waitAndClickByLinkText(docId);
            switchToWindow("Kuali :: Namespace");
        } else {
            fail("Previous Document may have routing problem so the latest Document is not able to submit correctly.");
        }
        return docId;
    }

    protected void assertSuperPerson(String user, String action, String docId) throws InterruptedException {
        waitAndClickByName("selectedActionRequests");
        waitAndTypeByName("superUserAnnotation","test suaction");
        waitAndClickByName("methodToCall.takeSuperUserActions");

        Thread.sleep(1000);
        if(!isTextPresent(user)) {
            assertTextPresent("superuser " + action + " in Document " + docId);
        } else {
            fail("Super User Approve functionality may not be working.");
        }
    }
}
