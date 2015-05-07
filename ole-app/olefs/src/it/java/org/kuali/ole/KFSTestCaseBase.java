package org.kuali.ole;

import org.junit.After;
import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.ole.sys.*;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.KualiMaintainableImpl;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.kns.rules.MaintenanceDocumentRule;
import org.kuali.rice.kns.service.DictionaryValidationService;
import org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 4/27/15
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class KFSTestCaseBase extends OLETestCaseBase {

    private static final Map<String, Level> changedLogLevels = new HashMap<String, Level>();

    @After
    public void tearDown() throws Exception {
        resetLogLevels();
    }

    protected void resetLogLevels() {
        for (Iterator i = changedLogLevels.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();

            String loggerName = (String) e.getKey();
            Level originalLevel = (Level) e.getValue();

            java.util.logging.Logger.getLogger(loggerName).setLevel(originalLevel);
        }
        changedLogLevels.clear();
    }


    protected void changeCurrentUser(UserNameFixture sessionUser) throws Exception {
        Person p = sessionUser.getPerson();
        GlobalVariables.setUserSession(new UserSession(p.getPrincipalName()));
    }

    protected void changeCurrentUser(Person p) throws Exception {
        GlobalVariables.setUserSession(new UserSession(p.getPrincipalName()));
    }

    protected MaintenanceDocument newMaintDoc(PersistableBusinessObject oldBo, PersistableBusinessObject newBo) {

        // disallow null value for newBo
        if (null == newBo) {
            throw new IllegalArgumentException("Invalid value (null) for newBo.  " + "This must always be a valid, populated BusinessObject instance.");
        }

        // get a new MaintenanceDocument from Spring
        MaintenanceDocument document = null;
        try {
            document = (MaintenanceDocument) SpringContext.getBean(DocumentService.class).getNewDocument(SpringContext.getBean(MaintenanceDocumentDictionaryService.class).getDocumentTypeName(newBo.getClass()));
        }
        catch (WorkflowException e) {
            throw new RuntimeException(e);
        }

        // add all the pieces
        document.getDocumentHeader().setDocumentDescription("test");
        if (null == oldBo) {
            document.setOldMaintainableObject(new KualiMaintainableImpl());
        }
        else {
            document.setOldMaintainableObject(new KualiMaintainableImpl(oldBo));
            document.getOldMaintainableObject().setBoClass(oldBo.getClass());
        }
        document.setNewMaintainableObject(new KualiMaintainableImpl(newBo));
        document.getNewMaintainableObject().setBoClass(newBo.getClass());
        return document;
    }


    protected MaintenanceDocument newMaintDoc(PersistableBusinessObject newBo) {
        return newMaintDoc(null, newBo);
    }

    protected MaintenanceDocumentRule setupMaintDocRule(MaintenanceDocument maintDoc, Class ruleClass) {

        MaintenanceDocumentRule rule;
        try {
            rule = (MaintenanceDocumentRule) ruleClass.newInstance();
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        rule.setupBaseConvenienceObjects(maintDoc);

        // confirm that we're starting with no errors
        assertEquals(0, GlobalVariables.getMessageMap().getNumberOfPropertiesWithErrors());

        return rule;
    }

    protected void testDefaultExistenceCheck(PersistableBusinessObject bo, String fieldName, boolean shouldFail) {

        // init the error path
        GlobalVariables.getMessageMap().addToErrorPath("document.newMaintainableObject");

        // run the dataDictionary validation
        SpringContext.getBean(DictionaryValidationService.class).validateDefaultExistenceChecks(bo);

        // clear the error path
        GlobalVariables.getMessageMap().removeFromErrorPath("document.newMaintainableObject");

        // assert that the existence of the error is what is expected
        assertFieldErrorExistence(fieldName, org.kuali.ole.sys.OLEKeyConstants.ERROR_EXISTENCE, shouldFail);

    }

    protected void assertFieldErrorExistence(String fieldName, String errorKey, boolean expectedResult) {
        boolean result = doesFieldErrorExist(fieldName, errorKey);
        assertEquals("Existence check for Error on fieldName/errorKey: " + fieldName + "/" + errorKey + ". " + GlobalVariables.getMessageMap(), expectedResult, result);
    }

    protected boolean doesFieldErrorExist(String fieldName, String errorKey) {
        return GlobalVariables.getMessageMap().fieldHasMessage(MaintenanceDocumentRuleBase.MAINTAINABLE_ERROR_PREFIX + fieldName, errorKey);
    }

    protected void assertFieldErrorDoesNotExist(String fieldName, String errorKey) {
        boolean result = doesFieldErrorExist(fieldName, errorKey);
        assertTrue("FieldName (" + fieldName + ") should NOT contain errorKey: " + errorKey, !result);
    }

    protected void assertFieldErrorExists(String fieldName, String errorKey) {
        boolean result = GlobalVariables.getMessageMap().fieldHasMessage(MaintenanceDocumentRuleBase.MAINTAINABLE_ERROR_PREFIX + fieldName, errorKey);
        if ( !result ) {
            org.apache.log4j.Logger.getLogger(getClass()).info("Messages in MessageMap: " + GlobalVariables.getMessageMap());
        }
        assertTrue("FieldName (" + fieldName + ") should contain errorKey: " + errorKey, result);
    }

    protected MaintenanceDocumentRule setupMaintDocRule(PersistableBusinessObject newBo, Class ruleClass) {
        MaintenanceDocument maintDoc = newMaintDoc(newBo);
        return setupMaintDocRule(maintDoc, ruleClass);
    }

    protected void assertGlobalErrorExists(String errorKey) {
        boolean result = GlobalVariables.getMessageMap().fieldHasMessage(org.kuali.ole.sys.OLEConstants.DOCUMENT_ERRORS, errorKey);
        assertTrue("Document should contain errorKey: " + errorKey, result);
    }

}
