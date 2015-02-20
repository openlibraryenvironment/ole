package org.kuali.ole.deliver.api;

import org.kuali.rice.kim.api.identity.address.EntityAddressContract;
import org.kuali.rice.kim.api.identity.email.EntityEmailContract;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.entity.EntityContract;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.name.EntityNameContract;
import org.kuali.rice.kim.api.identity.phone.EntityPhoneContract;
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
public class OlePatronDefintionHelper {

    private static final String PATRON_ID = "P1001";
    private static final String BARCODE = "1234";
    private static final Date EXPIRATION_DATE =new java.sql.Date((new Timestamp(System.currentTimeMillis())).getTime());
    private static final Date ACTIVATION_DATE =new java.sql.Date((new Timestamp(System.currentTimeMillis())).getTime());

    private static final boolean GENERAL_BLOCK = true;
    private static final String PATRON_GENERAL_BLOCK_NOTES ="General Block notes";
    private static final boolean DELIVERY_PRIVILEGE = true;
    private static final boolean COURTESY_NOTICE = true;
    private static final boolean PAGING_PRIVILEGE = true;
    private static final boolean ACTIVE = true;
    private static final Long VERSION_NUMBER = new Long(1);
    private static final String OBJECT_ID = String.valueOf(UUID.randomUUID());

    private static final EntityTypeContactInfo ENTITY_TYPE_CONTACT = EntityTypeContactInfoHelper.create();
    private static final Entity ENTITY = EntityHelper.create();
    private static final EntityName ENTITY_NAME = EntityNameHelper.create();

    private static final String BORROWER_ID = "7" ;
    private static final String BORR_CODE = "UGRAD";
    private static final String BORR_NAME = "UnderGrad";
    private static final String BORR_DESC = "UnderGrad";
    private static final Long BORR_VERSION_NUMBER = new Long(1);
    private static final String BORR_OBJECT_ID = String.valueOf(UUID.randomUUID());

    private static final String PATRON_NOTE_ID="P2";
    private static final String PATRON_NOTE_TEXT="Patron Test";
    private static final String PATRON_NOTE_TYPE_ID="2";
    private static final String PATRON_NOTE_TYPE_NAME="Note Test";
    private static final String PATRON_NOTE_TYPE_CODE="NT";
    private static final Long NOTE_VERSION_NUMBER = new Long(1);
    private static final String NOTE_OBJECT_ID = String.valueOf(UUID.randomUUID());

    private static final String PROXY_PATRON_ID="P1002";
    private static final String PROXY_PATRON_DOC_ID = "11";
    private static final Date PROXY_EXPIRATION_DATE =new java.sql.Date((new Timestamp(System.currentTimeMillis())).getTime());
    private static final Date PROXY_ACTIVATION_DATE =new java.sql.Date((new Timestamp(System.currentTimeMillis())).getTime());
    private static final Long PROXY_VERSION_NUMBER = new Long(1);
    private static final OlePatronDefinition OLE_PATRON = OlePatronDefintionHelper.create();
    private static final String PROXY_OBJECT_ID = String.valueOf(UUID.randomUUID());

    private static final String PATRON_ADDRESS_ID = "1";
    private static final boolean PATRON_ADDRESS_VERIFIED = true;
    private static final boolean DELIVER_ADDRESS = true;
    private static final Date PATRON_ADDRESS_VALID_FROM = new java.sql.Date((new Timestamp(System.currentTimeMillis())).getTime());
    private static final Date PATRON_ADDRESS_VALID_TO = new java.sql.Date((new Timestamp(System.currentTimeMillis())).getTime());
    private static final Long PATRON_ADDRESS_VERSION_NUMBER = new Long(1);
    private static final String PATRON_ADDRESS_OBJECT_ID = String.valueOf(UUID.randomUUID());

    private static final String PATRON_ADDRESS_SOURCE_ID = "1";
    private static final String PATRON_ADDRESS_SOURCE_CODE = "REG";
    private static final String PATRON_ADDRESS_SOURCE_NAME = "Registrar";
    private static final String PATRON_ADDRESS_SOURCE_DESC = "Registrar";
    private static final Long PATRON_ADDRESS_SOURCE_VERSION_NUMBER = new Long(1);
    private static final String PATRON_ADDRESS_SOURCE_OBJECT_ID = String.valueOf(UUID.randomUUID());

    private static final String PATRON_SOURCE_ID = "1";
    private static final String PATRON_SOURCE_CODE = "HR";
    private static final String PATRON_SOURCE_NAME = "Human Resource";
    private static final String PATRON_SOURCE_DESC = "Human Resource";
    private static final Long PATRON_SOURCE_VERSION_NUMBER = new Long(1);
    private static final String PATRON_SOURCE_OBJECT_ID = String.valueOf(UUID.randomUUID());

    private static final String PATRON_STATISTICAL_CATEGORY_ID = "1";
    private static final String PATRON_STATISTICAL_CATEGORY_CODE = "REG";
    private static final String PATRON_STATISTICAL_CATEGORY_NAME = "Registrar";
    private static final String PATRON_STATISTICAL_CATEGORY_DESC = "Registrar";
    private static final Long PATRON_STATISTICAL_CATEGORY_VERSION_NUMBER = new Long(1);
    private static final String PATRON_STATISTICAL_CATEGORY_OBJECT_ID = String.valueOf(UUID.randomUUID());

    private static final String PATRON_LOST_BARCODE_ID = "P11";
    private static final String PATRON_LOST_BARCODE = "222222";
    private static final Date  PATRON_LOST_BARCODE_EFF_DATE= new java.sql.Date((new Timestamp(System.currentTimeMillis())).getTime());
    private static final Long PATRON_LOST_BARCODE_VERSION_NUMBER = new Long(1);

    private static final String PATRON_LOCAL_SEQ_ID = "P10";
    private static final String LOCAL_ID = "222222";
    private static final Long PATRON_LOCAL_ID_VERSION_NUMBER = new Long(1);
    private static final String PATRON_LOCAL_ID_OBJECT_ID = String.valueOf(UUID.randomUUID());

    public static OlePatronDefinition create() {
        return OlePatronDefinition.Builder.create(new OlePatronContract() {
            @Override
            public String getOlePatronId() {
                return PATRON_ID;
            }

            @Override
            public String getBarcode() {
                return BARCODE;
            }

            @Override
            public String getBorrowerType() {
                return BORROWER_ID ;
            }

            @Override
            public boolean isGeneralBlock() {
                return GENERAL_BLOCK;
            }

            @Override
            public boolean isPagingPrivilege() {
                return PAGING_PRIVILEGE;
            }

            @Override
            public boolean isCourtesyNotice() {
                return COURTESY_NOTICE;
            }

            @Override
            public boolean isDeliveryPrivilege() {
                return DELIVERY_PRIVILEGE;
            }

            @Override
            public Date getExpirationDate() {
                return EXPIRATION_DATE;
            }

            @Override
            public Date getActivationDate() {
                return ACTIVATION_DATE;
            }

            @Override
            public List<? extends EntityAddressContract> getAddresses() {
                return ENTITY_TYPE_CONTACT.getAddresses();
            }

            @Override
            public List<? extends EntityEmailContract> getEmails() {
                return ENTITY_TYPE_CONTACT.getEmailAddresses();
            }

            @Override
            public EntityNameContract getName() {
                return ENTITY_NAME;
            }

            @Override
            public List<? extends EntityPhoneContract> getPhones() {
                return ENTITY_TYPE_CONTACT.getPhoneNumbers();
            }

            @Override
            public EntityContract getEntity() {
                return ENTITY;
            }

            @Override
            public boolean isActiveIndicator() {
                return ACTIVE;
            }

            @Override
            public List<? extends OlePatronNotesContract> getNotes() {
                return Arrays.asList(OlePatronNotesDefinition.Builder.create(new OlePatronNotesContract() {
                    @Override
                    public String getObjectId() {
                        return NOTE_OBJECT_ID;
                    }

                    @Override
                    public String getPatronNoteId() {
                        return PATRON_NOTE_ID;
                    }

                    @Override
                    public String getOlePatronId() {
                        return PATRON_ID;
                    }

                    @Override
                    public String getPatronNoteText() {
                        return PATRON_NOTE_TEXT;
                    }

                    @Override
                    public OlePatronNoteTypeContract getOlePatronNoteType() {
                        return OlePatronNoteTypeDefinition.Builder.create(new OlePatronNoteTypeContract() {
                            @Override
                            public String getPatronNoteTypeId() {
                                return PATRON_NOTE_TYPE_ID;
                            }

                            @Override
                            public String getPatronNoteTypeCode() {
                                return PATRON_NOTE_TYPE_CODE;
                            }

                            @Override
                            public String getPatronNoteTypeName() {
                                return PATRON_NOTE_TYPE_NAME;
                            }

                            @Override
                            public String getId() {
                                return PATRON_NOTE_TYPE_ID;
                            }

                            @Override
                            public boolean isActive() {
                                return ACTIVE;
                            }

                        }).build();
                    }


                    @Override
                    public String getId() {
                        return PATRON_NOTE_ID;
                    }

                    @Override
                    public Long getVersionNumber() {
                        return NOTE_VERSION_NUMBER;
                    }

                    @Override
                    public boolean isActive() {
                        return ACTIVE;
                    }
                }).build());
            }

            @Override
            public List<? extends OleEntityAddressContract> getOleEntityAddressBo() {
                return Arrays.asList(OleEntityAddressDefinition.Builder.create(new OleEntityAddressContract() {

                    @Override
                    public OleAddressContract getOleAddressBo() {
                        return OleAddressDefinition.Builder.create(new OleAddressContract() {


                            @Override
                            public String getOleAddressId() {
                                return PATRON_ADDRESS_ID;
                            }

                            @Override
                            public boolean isAddressVerified() {
                                return PATRON_ADDRESS_VERIFIED;
                            }

                            @Override
                            public String getOlePatronId() {
                                return PATRON_ID;
                            }

                            @Override
                            public String getId() {
                                return PATRON_ADDRESS_ID;
                            }

                            @Override
                            public Date getAddressValidFrom() {
                                return PATRON_ADDRESS_VALID_FROM;
                            }

                            @Override
                            public Date getAddressValidTo() {
                                return PATRON_ADDRESS_VALID_TO;
                            }

                            @Override
                            public String getAddressSource() {
                                return PATRON_ADDRESS_SOURCE_ID;
                            }
                            @Override
                            public boolean isDeliverAddress(){ return PATRON_ADDRESS_VERIFIED; }

                            @Override
                            public OleAddressSourceContract getAddressSourceBo() {
                                return OleAddressSourceDefinition.Builder.create(new OleAddressSourceContract() {


                                    @Override
                                    public String getId() {
                                        return PATRON_ADDRESS_SOURCE_ID;
                                    }

                                    @Override
                                    public String getOleAddressSourceId() {
                                        return PATRON_ADDRESS_SOURCE_ID;
                                    }

                                    @Override
                                    public String getOleAddressSourceCode() {
                                        return PATRON_ADDRESS_SOURCE_CODE;
                                    }

                                    @Override
                                    public String getOleAddressSourceName() {
                                        return PATRON_ADDRESS_SOURCE_NAME;
                                    }

                                    @Override
                                    public String getOleAddressSourceDesc() {
                                        return PATRON_ADDRESS_SOURCE_DESC;
                                    }

                                    @Override
                                    public boolean isActive() {
                                        return ACTIVE;
                                    }

                                    @Override
                                    public Long getVersionNumber() {
                                        return PATRON_ADDRESS_SOURCE_VERSION_NUMBER;
                                    }
                                }).build();
                            }

                           /* @Override
                            public EntityAddressContract getEntityAddress() {
                                return ENTITY_TYPE_CONTACT.getAddresses().get(0);
                            }
*/
                            @Override
                            public Long getVersionNumber() {
                                return PATRON_ADDRESS_VERSION_NUMBER;
                            }
                        }).build();
                    }
                    @Override
                    public EntityAddressContract getEntityAddressBo() {
                        return ENTITY_TYPE_CONTACT.getAddresses().get(0);
                    }

                }).build());
            }



            @Override
            public List<? extends OlePatronAffiliationContract> getPatronAffiliations() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List<? extends OleProxyPatronContract> getOleProxyPatronDocuments() {
                return Arrays.asList(OleProxyPatronDefinition.Builder.create(new OleProxyPatronContract() {

                    @Override
                    public String getOleProxyPatronDocumentId() {
                        return PROXY_PATRON_DOC_ID;
                    }

                    @Override
                    public String getProxyPatronId() {
                        return PROXY_PATRON_ID;
                    }

                    @Override
                    public String getOlePatronId() {
                        return PATRON_ID;
                    }
/*
                    @Override
                    public OlePatronContract getOlePatronDocument() {
                        return OLE_PATRON;
                    }*/

                    @Override
                    public Date getProxyPatronExpirationDate() {
                        return PROXY_EXPIRATION_DATE;
                    }

                    @Override
                    public Date getProxyPatronActivationDate() {
                        return PROXY_ACTIVATION_DATE;
                    }

                    @Override
                    public boolean isActive() {
                        return ACTIVE;
                    }

                    @Override
                    public String getId() {
                        return PROXY_PATRON_DOC_ID;
                    }

                    @Override
                    public Long getVersionNumber() {
                        return PROXY_VERSION_NUMBER;
                    }
                }).build());
            }

           /* @Override
            public List<? extends OlePatronContract> getOlePatronDocuments() {
                return null;
            }*/

            @Override
            public List<? extends OlePatronLostBarcodeContract> getLostBarcodes() {
                return Arrays.asList(OlePatronLostBarcodeDefinition.Builder.create(new OlePatronLostBarcodeContract() {

                    @Override
                    public String getOlePatronLostBarcodeId() {
                        return PATRON_LOST_BARCODE_ID;
                    }

                    @Override
                    public String getOlePatronId() {
                        return PATRON_ID;
                    }

                    @Override
                    public Date getInvalidOrLostBarcodeEffDate() {
                        return PATRON_LOST_BARCODE_EFF_DATE;
                    }

                    @Override
                    public String getInvalidOrLostBarcodeNumber() {
                        return PATRON_LOST_BARCODE;
                    }

                    @Override
                    public String getObjectId() {
                        return NOTE_OBJECT_ID;
                    }

                    @Override
                    public String getId() {
                        return PATRON_LOST_BARCODE_ID;
                    }

                    @Override
                    public Long getVersionNumber() {
                        return PATRON_LOST_BARCODE_VERSION_NUMBER;
                    }
                }).build());
            }

            @Override
            public String getGeneralBlockNotes() {
                return PATRON_GENERAL_BLOCK_NOTES;
            }

            @Override
            public String getSource() {
                return PATRON_SOURCE_ID;
            }

            @Override
            public String getStatisticalCategory() {
                return PATRON_STATISTICAL_CATEGORY_ID;
            }

            @Override
            public List<? extends OleAddressContract> getOleAddresses() {

                    return Arrays.asList(OleAddressDefinition.Builder.create(new OleAddressContract() {


                        @Override
                        public String getOleAddressId() {
                            return PATRON_ADDRESS_ID;
                        }

                        @Override
                        public boolean isAddressVerified() {
                            return PATRON_ADDRESS_VERIFIED;
                        }

                        @Override
                        public String getOlePatronId() {
                            return PATRON_ID;
                        }

                        @Override
                        public String getId() {
                            return PATRON_ADDRESS_ID;
                        }

                        @Override
                        public Date getAddressValidFrom() {
                            return PATRON_ADDRESS_VALID_FROM;
                        }

                        @Override
                        public Date getAddressValidTo() {
                            return PATRON_ADDRESS_VALID_TO;
                        }

                        @Override
                        public String getAddressSource() {
                            return PATRON_ADDRESS_SOURCE_ID;
                        }

                        @Override
                        public OleAddressSourceContract getAddressSourceBo() {
                            return OleAddressSourceDefinition.Builder.create(new OleAddressSourceContract() {


                                @Override
                                public String getId() {
                                    return PATRON_ADDRESS_SOURCE_ID;
                                }

                                @Override
                                public String getOleAddressSourceId() {
                                    return PATRON_ADDRESS_SOURCE_ID;
                                }

                                @Override
                                public String getOleAddressSourceCode() {
                                    return PATRON_ADDRESS_SOURCE_CODE;
                                }

                                @Override
                                public String getOleAddressSourceName() {
                                    return PATRON_ADDRESS_SOURCE_NAME;
                                }

                                @Override
                                public String getOleAddressSourceDesc() {
                                    return PATRON_ADDRESS_SOURCE_DESC;
                                }

                                @Override
                                public boolean isActive() {
                                    return ACTIVE;
                                }

                                @Override
                                public Long getVersionNumber() {
                                    return PATRON_ADDRESS_SOURCE_VERSION_NUMBER;
                                }
                            }).build();
                        }

                        @Override
                        public boolean isDeliverAddress() {
                            return DELIVER_ADDRESS;
                        }

                       /* @Override
                        public EntityAddressContract getEntityAddress() {
                            return ENTITY_TYPE_CONTACT.getAddresses().get(0);
                        }*/

                        @Override
                        public Long getVersionNumber() {
                            return PATRON_ADDRESS_VERSION_NUMBER;
                        }
                    }).build());
            }

            @Override
            public List<? extends OlePatronLocalIdentificationContract> getOlePatronLocalIds() {
                return Arrays.asList(OlePatronLocalIdentificationDefinition.Builder.create(new OlePatronLocalIdentificationContract() {

                    @Override
                    public String getPatronLocalSeqId() {
                        return PATRON_LOCAL_SEQ_ID;
                    }

                    @Override
                    public String getLocalId() {
                        return LOCAL_ID;
                    }

                    @Override
                    public String getOlePatronId() {
                        return PATRON_ID;
                    }

                    @Override
                    public String getObjectId() {
                        return PATRON_LOCAL_ID_OBJECT_ID;
                    }

                    @Override
                    public String getId() {
                        return PATRON_LOCAL_SEQ_ID;
                    }

                    @Override
                    public Long getVersionNumber() {
                        return PATRON_LOCAL_ID_VERSION_NUMBER;
                    }
                }).build());
            }

            // @Override
            public OleBorrowerTypeContract getOleBorrowerType() {
                return OleBorrowerTypeDefinition.Builder.create(new OleBorrowerTypeContract() {
                    @Override
                    public String getBorrowerTypeId() {
                        return BORROWER_ID;
                    }

                    @Override
                    public String getBorrowerTypeCode() {
                        return BORR_CODE;
                    }

                    @Override
                    public String getBorrowerTypeDescription() {
                        return BORR_DESC;
                    }

                    @Override
                    public String getBorrowerTypeName() {
                        return BORR_NAME;
                    }

                    @Override
                    public String getId() {
                        return BORROWER_ID;
                    }

                    @Override
                    public Long getVersionNumber() {
                        return BORR_VERSION_NUMBER;
                    }
                }).build();
            }

            @Override
            public String getId() {
                return PATRON_ID;
            }

            @Override
            public Long getVersionNumber() {
                return VERSION_NUMBER;
            }
        }) .build();
    }

    public OleSourceContract getSourceBo() {
        return OleSourceDefinition.Builder.create(new OleSourceContract() {

            @Override
            public String getOleSourceId() {
                return PATRON_SOURCE_ID;
            }

            @Override
            public String getOleSourceCode() {
                return PATRON_SOURCE_CODE;
            }

            @Override
            public String getOleSourceName() {
                return PATRON_SOURCE_NAME;
            }

            @Override
            public String getOleSourceDesc() {
                return PATRON_SOURCE_DESC;
            }

            @Override
            public boolean isActive() {
                return ACTIVE;
            }

            @Override
            public String getId() {
                return PATRON_SOURCE_ID;
            }

            @Override
            public Long getVersionNumber() {
                return PATRON_SOURCE_VERSION_NUMBER;
            }
        }).build();
    }

    public OleStatisticalCategoryContract getStatisticalCategoryBo() {
        return OleStatisticalCategoryDefinition.Builder.create(new OleStatisticalCategoryContract() {


            @Override
            public String getOleStatisticalCategoryId() {
                return PATRON_STATISTICAL_CATEGORY_ID;
            }

            @Override
            public String getOleStatisticalCategoryCode() {
                return PATRON_STATISTICAL_CATEGORY_CODE;
            }

            @Override
            public String getOleStatisticalCategoryName() {
                return PATRON_STATISTICAL_CATEGORY_NAME;
            }

            @Override
            public String getOleStatisticalCategoryDesc() {
                return PATRON_STATISTICAL_CATEGORY_DESC;
            }

            @Override
            public boolean isActive() {
                return ACTIVE;
            }

            @Override
            public String getId() {
                return PATRON_STATISTICAL_CATEGORY_ID;
            }

            @Override
            public Long getVersionNumber() {
                return PATRON_STATISTICAL_CATEGORY_VERSION_NUMBER;
            }
        }).build();
    }

}
