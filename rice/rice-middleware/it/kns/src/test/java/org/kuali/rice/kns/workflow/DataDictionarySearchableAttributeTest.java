/**
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kns.workflow;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.docsearch.service.DocumentSearchService;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.datadictionary.exception.UnknownDocumentTypeException;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.test.document.AccountWithDDAttributesDocument;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.kns.workflow.attribute.DataDictionarySearchableAttribute;
import org.kuali.rice.krad.test.KRADTestCase;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * DataDictionarySearchableAttributeTest performs various DataDictionarySearchableAttribute-related tests on the doc search, including verification of proper wildcard functionality
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataDictionarySearchableAttributeTest extends KRADTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GlobalVariables.setUserSession(new UserSession("quickstart"));
    }
    
    private final static String ACCOUNT_WITH_DD_ATTRIBUTES_DOCUMENT_NAME = "AccountWithDDAttributes";
    
    enum DOCUMENT_FIXTURE {
    	NORMAL_DOCUMENT("Testing NORMAL_DOCUMENT", new Integer(1234567890), "John Doe", new KualiDecimal(501.77), createDate(2009, Calendar.OCTOBER, 15), createTimestamp(2009, Calendar.NOVEMBER, 1, 0, 0, 0), "SecondState", true),
    	ZERO_NUMBER_DOCUMENT("Testing ZERO_NUMBER_DOCUMENT", new Integer(0), "Jane Doe", new KualiDecimal(-100), createDate(2009, Calendar.OCTOBER, 16), createTimestamp(2015, Calendar.NOVEMBER, 2, 0, 0, 0), "FirstState", true),
    	FALSE_AWAKE_DOCUMENT("Testing FALSE_AWAKE_DOCUMENT", new Integer(987654321), "John D'oh", new KualiDecimal(0.0), createDate(2006, Calendar.OCTOBER, 17), createTimestamp(1900, Calendar.NOVEMBER, 3, 0, 0, 0), "FourthState", false),
    	ODD_NAME_DOCUMENT("Testing ODD_NAME_DOCUMENT", new Integer(88), "_", new KualiDecimal(10000051.0), createDate(2009, Calendar.OCTOBER, 18), createTimestamp(2009, Calendar.NOVEMBER, 4, 0, 0, 0), "FourthState", true),
    	ODD_TIMESTAMP_DOCUMENT("Testing ODD_TIMESTAMP_DOCUMENT", new Integer(9000), "Shane Kloe", new KualiDecimal(4.54), createDate(2012, Calendar.OCTOBER, 19), createTimestamp(2007, Calendar.NOVEMBER, 5, 12, 4, 38), "ThirdState", false),
    	ANOTHER_ODD_NAME_DOCUMENT("Testing ANOTHER_ODD_NAME_DOCUMENT", new Integer(1234567889), "---", new KualiDecimal(501), createDate(2009, Calendar.APRIL, 20), createTimestamp(2009, Calendar.NOVEMBER, 6, 12, 59, 59), "ThirdState", true),
    	INVALID_STATE_DOCUMENT("Testing INVALID_STATE_DOCUMENT", new Integer(99999), "AAAAAAAAA", new KualiDecimal(2.22), createDate(2009, Calendar.OCTOBER, 21), createTimestamp(2009, Calendar.NOVEMBER, 7, 0, 0, 1), "SeventhState", true),
    	WILDCARD_NAME_DOCUMENT("Testing WILDCARD_NAME_DOCUMENT", new Integer(1), "Sh*ne><K!=e?", new KualiDecimal(771.05), createDate(2054, Calendar.OCTOBER, 22), createTimestamp(2008, Calendar.NOVEMBER, 8, 12, 0, 0), "FirstState", true);
    	
    	private String accountDocumentDescription;
    	private Integer accountNumber;
    	private String accountOwner;
    	private KualiDecimal accountBalance;
    	private Date accountOpenDate;
    	private Timestamp accountUpdateDateTime;
    	private String accountState;
    	private boolean accountAwake;
    	
    	private DOCUMENT_FIXTURE(String accountDocumentDescription, Integer accountNumber, String accountOwner, KualiDecimal accountBalance, Date accountOpenDate, Timestamp accountUpdateDateTime, String accountState, boolean accountAwake) {
    		this.accountDocumentDescription = accountDocumentDescription;
    		this.accountNumber = accountNumber;
    		this.accountOwner = accountOwner;
    		this.accountBalance = accountBalance;
    		this.accountOpenDate = accountOpenDate;
    		this.accountUpdateDateTime = accountUpdateDateTime;
    		this.accountState = accountState;
    		this.accountAwake = accountAwake;
    	}
    	
    	public AccountWithDDAttributesDocument getDocument(DocumentService docService) throws WorkflowException {
    		AccountWithDDAttributesDocument acctDoc = (AccountWithDDAttributesDocument) docService.getNewDocument(ACCOUNT_WITH_DD_ATTRIBUTES_DOCUMENT_NAME);
    		acctDoc.getDocumentHeader().setDocumentDescription(this.accountDocumentDescription);
    		acctDoc.setAccountNumber(this.accountNumber);
    		acctDoc.setAccountOwner(this.accountOwner);
    		acctDoc.setAccountBalance(this.accountBalance);
    		acctDoc.setAccountOpenDate(this.accountOpenDate);
    		acctDoc.setAccountUpdateDateTime(this.accountUpdateDateTime);
    		acctDoc.setAccountState(this.accountState);
    		acctDoc.setAccountAwake(this.accountAwake);
    		
    		return acctDoc;
    	}
    }
	
	/**
	 * Tests the use of multi-select and wildcard searches to ensure that they function correctly for DD searchable attributes on the doc search.
	 */
    @Test
	public void testWildcardsAndMultiSelectsOnDDSearchableAttributes() throws Exception {
		DocumentService docService = KRADServiceLocatorWeb.getDocumentService();
		//docSearchService = KEWServiceLocator.getDocumentSearchService();
		DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName("AccountWithDDAttributes");
        String principalName = "quickstart";
        String principalId = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(principalName).getPrincipalId();
		
        // Route some test documents.
		docService.routeDocument(DOCUMENT_FIXTURE.NORMAL_DOCUMENT.getDocument(docService), "Routing NORMAL_DOCUMENT", null);
		docService.routeDocument(DOCUMENT_FIXTURE.ZERO_NUMBER_DOCUMENT.getDocument(docService), "Routing ZERO_NUMBER_DOCUMENT", null);
		docService.routeDocument(DOCUMENT_FIXTURE.FALSE_AWAKE_DOCUMENT.getDocument(docService), "Routing FALSE_AWAKE_DOCUMENT", null);
		docService.routeDocument(DOCUMENT_FIXTURE.ODD_NAME_DOCUMENT.getDocument(docService), "Routing ODD_NAME_DOCUMENT", null);
		docService.routeDocument(DOCUMENT_FIXTURE.ODD_TIMESTAMP_DOCUMENT.getDocument(docService), "Routing ODD_TIMESTAMP_DOCUMENT", null);
		docService.routeDocument(DOCUMENT_FIXTURE.ANOTHER_ODD_NAME_DOCUMENT.getDocument(docService), "Routing ANOTHER_ODD_NAME_DOCUMENT", null);
		docService.routeDocument(DOCUMENT_FIXTURE.INVALID_STATE_DOCUMENT.getDocument(docService), "Routing INVALID_STATE_DOCUMENT", null);
		docService.routeDocument(DOCUMENT_FIXTURE.WILDCARD_NAME_DOCUMENT.getDocument(docService), "Routing WILDCARD_NAME_DOCUMENT", null);

		// Ensure that DD searchable attribute integer fields function correctly when searched on.
		// Note that negative numbers are disallowed by the NumericValidationPattern that validates this field.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountNumber",
				new String[] {"!1234567890", "9???9", ">1", "987654321|1234567889", "<100", ">=99999", "<=-42", ">9000|<=1", "<1|>=1234567890",
						">1234567889&&<1234567890", ">=88&&<=99999", "0|>10&&<10000", "9000..1000000", "0..100|>1234567889", "1..10000&&>50", "250..50"},
				new int[]    {7            , -1     , 6   , 2                     , 3     , 4        , -1     , 6          , 2,
						0                         , 3              , 3              , 2              , 4                   , 2              , 0});
		
		// Ensure that DD searchable attribute string fields function correctly when searched on.
		// Note that DD searchable attributes cannot treat wildcards literally, so the "Sh*ne><K!=e" case below yields very different results.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountOwner",
				new String[] {"!John Doe", "!John*", "!John Doe&&!Shane Kloe", "!Jane ???", "!Jane Doe!John Doe", "_", "_|---", "Sh*ne><K!=e",
						">Jane Doe", "<Shane Kloe", ">=Johnny", "<=John D'oh", ">John Doe|<---", ">=AAAAAAAAA&&<=Jane Doe", ">---&&!John D'oh",
						"<Shane Kloe&&!John*", "*oe", "???? Doe", "Jane Doe..John Doe", "AAAAAAAAA..Shane Kloe&&!John Doe", "John D'oh|---..Jane Doe"},
				new int[]    {7          , 6       , 6                       , 7          , 6                   , 1  , 2      , 8,
						5          , 6            , 3         , 4            , 3               , 2                        , 6,
						4                    , 3    , 2         , 3                   , 5                                 , 4});
		
		// Ensure that DD searchable attribute float fields function correctly when searched on. Also ensure that the CurrencyFormatter is working.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountBalance",
				new String[] {"501.??", "*.54" , "!2.22", "10000051.0|771.05", "<0.0", ">501", "<=4.54", ">=-99.99", ">4.54|<=-1", ">=0&&<501.77", "!",
						"<=0|>=10000051", ">501&&<501.77", "-100|>771.05", "2.22..501", "-100..4.54&&<=0", "2.22|501.77..10000051.0", "Zero",
						"-$100", "<(501)&&>=($2.22)", "$4.54|<(1)", "($0.00)..$771.05", ">=$(500)", ")501(", "4.54$", "$501..0"},
				new int[]    {-1      , -1     , 7      , 2                  , 1     , 3     , 4       , 7         , 5           , 4             , -1,
						3               , 0              , 2             , 3          , 2                , 4                        , -1,
						1      , 2                  , 3           , 6                 , -1        , -1     , -1     , 0});
		
		// Ensure that DD searchable attribute date fields function correctly when searched on.
		// Note that dates with non-two-digit years outside the range of 1000 to 9999 will now fail validation.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountOpenDate",
				new String[] {"!10/15/2009", "Unknown", "10/15/2009|10/21/2009", "10/22/????", "*/*/05", ">10/17/06", "<=12-31-09&&>=10/16/2009",
						">101809&&<102012", ">=10/22/2054|<10/16/2009", ">2-29-12|<=10/21/09", "<2009", ">=10/19/2012|04/20/09", ">2/29/09", "2009..2008",
						"10/15/2009..10/21/2009", "1/1/2009..10/20/2009|10/22/2054", "<=06/32/03", ">2008&&<2011|10/17/06", 
						"<02/26/10500", ">05-07-333", ">=03/26/1001", "<=11-11-9900"},
				new int[]    {-1           , -1       , 2                      , -1          , -1      , 7          , 3,
						2                 , 4                         , 8                    , 1      , 3                      , -1        , -1,
						4                       , 5                                , -1          , 6                      ,
						-1            , -1          , 8             , 8});
		
		// Ensure that DD searchable attribute multi-select fields function correctly when searched on.
		// Currently, an exception is *not* thrown if the value given is not among the selectable values.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountStateMultiselect",
				new String[][] {{"FirstState"}, {"SecondState"}, {"ThirdState"}, {"FourthState"}, {"FirstState","ThirdState"},
						{"SecondState","FourthState"}, {"ThirdState","SecondState"}, {"FourthState","FirstState","SecondState"}, {"SeventhState"},
						{"ThirdState","FirstState","SecondState","FourthState"}},
				new int[]      {2             , 1              , 2             , 2              , 4,
						3                            , 3                           , 5                                         , 1,
						7});
		
		// Ensure that DD searchable attribute boolean fields function correctly when searched on.
		// TODO: Add the commented-out boolean search expressions back in once KULRICE-3698 is complete.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountAwake", new String[] {"Y", "N"}, new int[] {6, 2});
		/*assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountAwake",
				new String[] {"Y", "N", "Z", "Neither", "n", "y", "true", "FALSE", "fAlSe", "TrUe", "NO", "Yes", "f", "F", "T", "t", "2", "0", "1",
						"Active", "INACTIVE", "On", "Off", "ON", "off", "EnAbLeD", "enabled", "dIsAbLeD", "DISABLED"},
				new int[]    {6  , 2  , -1 , -1       , 2  , 6  , 6     , 2      , 2      , 6     , 2   , 6    , 2  , 2  , 6  , 6  , -1 , 2  , 6  ,
						-1      , -1        , 6   , 2    , 6   , 2    , 6        , 6        , 2         , 2});*/
		
		// Ensure that DD searchable attribute timestamp fields function correctly when searched on.
		// Note that timestamps with non-two-digit years outside the range of 1000 to 9999 will now fail validation.
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountUpdateDateTime",
				new String[] {"!11/01/2009 00:00:00",  "11/02/2015 00:00:00|11/06/2009 12:59:59", "11/??/2009 ??:??:??", ">110609 12:59:59",
						"<=2009 1:2:3", ">=11/06/09 12:59:59", "<11/8/2008 12:00 PM", "Blank",
						"11/3/1900 00:00:00|>11-7-09 00:00:01", "02/29/2008 07:00:00..11/04/2009 00:00:00",
						"11/1/09 00:00:00..11/06/09 12:59:59|11/03/1900 00:00:00", "2009..2008", "2000..2009&&>=110507 12:4:38",
						"<=11/08/2008 12:00 AM", ">=01-01-1000 00:00:00", ">12/31/999 23:59:59", "<01-01-10000 00:00:00", "<=12/31/9999 23:59:59"},
				new int[]    {-1                    , 2                                        , -1                   , 2,
						3             , 3                    , 2                    , -1,
						2                                     , 3,
						4                                                        , -1          , 2,
						2                      , 8                      , -1                   , -1                     , 8});
	}
    
    /**
     * Creates a date quickly
     * 
     * @param year the year of the date
     * @param month the month of the date
     * @param day the day of the date
     * @return a new java.sql.Date initialized to the precise date given
     */
    private static Date createDate(int year, int month, int day) {
    	Calendar date = Calendar.getInstance();
		date.set(year, month, day, 0, 0, 0);
		return new java.sql.Date(date.getTimeInMillis());
    }
    
    /**
     * Utility method to create a timestamp quickly
     * 
     * @param year the year of the timestamp
     * @param month the month of the timestamp
     * @param day the day of the timestamp
     * @param hour the hour of the timestamp
     * @param minute the minute of the timestamp
     * @param second the second of the timestamp
     * @return a new java.sql.Timestamp initialized to the precise time given
     */
    private static Timestamp createTimestamp(int year, int month, int day, int hour, int minute, int second) {
    	Calendar date = Calendar.getInstance();
    	date.set(year, month, day, hour, minute, second);
    	return new java.sql.Timestamp(date.getTimeInMillis());
    }

    /**
     * A convenience method for testing wildcards on data dictionary searchable attributes
     *
     * @param docType The document type containing the attributes.
     * @param principalId The ID of the user performing the search.
     * @param fieldName The name of the field on the test document.
     * @param searchValues The search expressions to test. Has to be a String array (for regular fields) or a String[] array (for multi-select fields).
     * @param resultSizes The number of expected documents to be returned by the search; use -1 to indicate that an error should have occurred.
     * @throws Exception
     */
    private void assertDDSearchableAttributeWildcardsWork(DocumentType docType, String principalId, String fieldName, Object[] searchValues,
    		int[] resultSizes) throws Exception {
    	if (!(searchValues instanceof String[]) && !(searchValues instanceof String[][])) {
    		throw new IllegalArgumentException("'searchValues' parameter has to be either a String[] or a String[][]");
    	}
    	DocumentSearchCriteria.Builder criteria = null;
        DocumentSearchResults results = null;
        DocumentSearchService docSearchService = KEWServiceLocator.getDocumentSearchService();
        for (int i = 0; i < resultSizes.length; i++) {
            if (searchValues[i].toString().equalsIgnoreCase("Zero")) {
                int num = 1 + 1;
            }
        	criteria = DocumentSearchCriteria.Builder.create();
        	criteria.setDocumentTypeName(docType.getName());
            if (searchValues instanceof String[][]) {
                String[] innerArray = (String[]) searchValues[i];
                for (int j=0; j<innerArray.length; j++) {
                    criteria.addDocumentAttributeValue(fieldName, innerArray[j]);
                }
            } else {
                criteria.addDocumentAttributeValue(fieldName, searchValues[i].toString());
            }

        	try {
        		results = docSearchService.lookupDocuments(principalId, criteria.build());
        		if (resultSizes[i] < 0) {
        			Assert.fail(fieldName + "'s search at loop index " + i + " should have thrown an exception");
        		}
        		if(resultSizes[i] != results.getSearchResults().size()){
        			assertEquals(fieldName + "'s search results at loop index " + i + " returned the wrong number of documents.", resultSizes[i], results.getSearchResults().size());
        		}
        	} catch (Exception ex) {
        		if (resultSizes[i] >= 0) {
                    LOG.error("exception", ex);
        			Assert.fail(fieldName
                            + "'s search at loop index "
                            + i
                            + " for search value '"
                            + searchValues[i]
                            + "' should not have thrown an exception");
        		}
        	}
        	GlobalVariables.clear();
        }
    }
    
    /**
     * Validates that the search inputs does not cause a class cast exception
     */
    @Test
    public void testValidateUserSearchInputsNoCast() throws Exception {
    	DataDictionarySearchableAttribute searchableAttribute = new DataDictionarySearchableAttribute();
    	final DocumentService documentService = KRADServiceLocatorWeb.getDocumentService();

        try {
            AccountWithDDAttributesDocument document = DOCUMENT_FIXTURE.NORMAL_DOCUMENT.getDocument(documentService);
            documentService.saveDocument(document);
            final String documentNumber = document.getDocumentNumber();
        } catch (UnknownDocumentTypeException udte) {
            fail("CI failure - https://jira.kuali.org/browse/KULRICE-9289 " + udte.getMessage() + ExceptionUtils.getStackTrace(udte));
        }

    	Exception caughtException;
    	List foundErrors;
    	
    	caughtException = null;
    	foundErrors = new ArrayList();
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentTypeName(ACCOUNT_WITH_DD_ATTRIBUTES_DOCUMENT_NAME);
    	Map<String, List<String>> simpleParamMap = new HashMap<String, List<String>>();
    	simpleParamMap.put("accountState", Collections.singletonList("FirstState"));
        criteria.setDocumentAttributeValues(simpleParamMap);
    	try {
    		foundErrors = searchableAttribute.validateDocumentAttributeCriteria(null, criteria.build());
    	} catch (RuntimeException re) {
    		caughtException = re;
    	}
    	Assert.assertNull("Found Exception " + caughtException, caughtException);
    	Assert.assertTrue("There were errors: " + foundErrors, (foundErrors == null || foundErrors.isEmpty()));
    	
    	caughtException = null;
    	foundErrors = new ArrayList();
    	Map<String, List<String>>  listParamMap = new HashMap<String, List<String>>();
    	List<String> paramValues = new ArrayList<String>();
    	paramValues.add("FirstState");
    	paramValues.add("SecondState");
    	listParamMap.put("accountState", paramValues);
        criteria.setDocumentAttributeValues(listParamMap);
    	try {
    		foundErrors = searchableAttribute.validateDocumentAttributeCriteria(null, criteria.build());
    	} catch (RuntimeException re) {
    		caughtException = re;
    	}
    	Assert.assertNull("Found Exception " + caughtException, caughtException);
    	Assert.assertTrue("There were errors: " + foundErrors, (foundErrors == null || foundErrors.isEmpty()));
    }

    /**
     * Tests handling resolution of error messages
     */
    @Test
    public void testErrorMessageResolution() throws Exception {
        final DataDictionarySearchableAttribute searchableAttribute = new DataDictionarySearchableAttribute();
        final DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        /*criteria.setDocumentTypeName(ACCOUNT_WITH_DD_ATTRIBUTES_DOCUMENT_NAME);
        Map<String, List<String>> simpleParamMap = new HashMap<String, List<String>>();
        simpleParamMap.put("accountState", Collections.singletonList("FirstState"));
        criteria.setDocumentAttributeValues(simpleParamMap);*/
        List<RemotableAttributeError> errors = GlobalVariables.doInNewGlobalVariables(new Callable<List<RemotableAttributeError>>() {
            public List<RemotableAttributeError> call() {
                GlobalVariables.getMessageMap().putError("fake.property", "error.custom", "the error message");
                return searchableAttribute.validateDocumentAttributeCriteria(null, criteria.build());
            }
        });
        Assert.assertEquals(1, errors.size());
        assertEquals("the error message", errors.get(0).getMessage());
    }

    /**
     * Test multiple value searches in the context of whole document search context
     */
    @Test
    public void testMultiSelectIntegration() throws Exception {
    	final DocumentService docService = KRADServiceLocatorWeb.getDocumentService();
		//docSearchService = KEWServiceLocator.getDocumentSearchService();
		DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName("AccountWithDDAttributes");
        String principalName = "quickstart";
        String principalId = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(principalName).getPrincipalId();
		
        // Route some test documents.
		docService.routeDocument(DOCUMENT_FIXTURE.NORMAL_DOCUMENT.getDocument(docService), "Routing NORMAL_DOCUMENT", null);
		
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountStateMultiselect",
				new String[][] {{"FirstState"}, {"SecondState"}, {"ThirdState"}, {"FourthState"}, {"FirstState", "SecondState"}, {"FirstState","ThirdState"}, {"FirstState", "FourthState"}, {"SecondState", "ThirdState"}, {"SecondState", "FourthState"}, {"ThirdState", "FourthState"}, {"FirstState", "SecondState", "ThirdState"}, {"FirstState", "ThirdState", "FourthState"}, {"SecondState", "ThirdState", "FourthState"}, {"FirstState","SecondState", "ThirdState", "FourthState"}},
				new int[] { 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1 });
		
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountOpenDate",
				new String[][] {{"10/15/2009"}, {"10/15/2009","10/17/2009"}, {"10/14/2009","10/16/2009"}},
				new int[] { 1, 1, 0 });
		
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountBalance",
				new String[][] {{"501.77"},{"501.77", "63.54"},{"501.78","501.74"}, {"502.00"}, {"0.00"} },
				new int[] { 1, 1, 0, 0, 0 });
		
		assertDDSearchableAttributeWildcardsWork(docType, principalId, "accountNumber",
				new String[][] {{"1234567890"},{"1234567890", "9876543210"},{"9876543210","77774"}, {"88881"}, {"0"} },
				new int[] { 1, 1, 0, 0, 0 });
    }
}
