package org.kuali.ole.deliver;

import org.kuali.ole.deliver.api.EntityTypeContactInfoHelper;
import org.kuali.ole.deliver.bo.OlePatronLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronLoanDocumentContract;
import org.kuali.ole.deliver.bo.OlePatronLoanDocuments;
import org.kuali.ole.deliver.bo.OlePatronLoanDocumentsContract;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/31/12
 * Time: 12:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePatronLoanDocumentHelper {

    private static final String ITEM_ID = "2222222222";
    private static final Date DUE_DATE =new java.sql.Date((new Timestamp(System.currentTimeMillis())).getTime());
    private static final String LOCATION = "indiana";
    private static final String AUTHOR = "author";
    private static final String TITLE = "the legend";
    private static final String CALLNUMBER = "1234567";
    private static final String MESSAGE_INFO="valid patron";


    private static final String ID="1";
    private static final boolean ACTIVE = true;
    private static final Long VERSION_NUMBER = new Long(1);
    private static final String OBJECT_ID = String.valueOf(UUID.randomUUID());

    private static final EntityTypeContactInfo ENTITY_TYPE_CONTACT = EntityTypeContactInfoHelper.create();


    public static OlePatronLoanDocuments create() {
        return OlePatronLoanDocuments.Builder.create(new OlePatronLoanDocumentsContract() {


            @Override
            public String getId() {
                return ID;
            }

            @Override
            public Long getVersionNumber() {
                return VERSION_NUMBER;
            }



            @Override
            public List<? extends OlePatronLoanDocumentContract> getOlePatronLoanDocuments() {
                return Arrays.asList(OlePatronLoanDocument.Builder.create(new OlePatronLoanDocumentContract() {

                    @Override
                    public String getMessageInfo() {
                        return MESSAGE_INFO;
                    }

                    @Override
                    public String getItemBarcode() {
                        return ITEM_ID;  
                    }

                    @Override
                    public String getAuthor() {
                        return AUTHOR;  
                    }

                    @Override
                    public String getTitle() {
                        return TITLE;  
                    }

                    @Override
                    public Date getDueDate() {
                        return DUE_DATE;  
                    }

                    @Override
                    public String getLocation() {
                        return LOCATION;  
                    }

                    @Override
                    public String getCallNumber() {
                        return CALLNUMBER;  
                    }

                    @Override
                    public String getId() {
                        return ID;
                    }

                    @Override
                    public Long getVersionNumber() {
                        return VERSION_NUMBER;
                    }

                    }).build());
                    }

                }).build();
            }



}
