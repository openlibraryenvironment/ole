package org.kuali.ole.deliver.api;

import org.joda.time.DateTime;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.CodedAttributeContract;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.kim.api.identity.address.EntityAddressContract;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.api.identity.email.EntityEmailContract;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.kuali.rice.kim.api.identity.phone.EntityPhoneContract;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoContract;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/29/12
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class EntityTypeContactInfoHelper {
    private static final String ENTITY_TYPE_CODE = "PERSON";
    private static final String ENTITY_TYPE_NAME = "PERSON";
    private static final String ENTITY_ID = "190192";
    private static final String TYPE_SORT_CODE = "0";
    private static final boolean TYPE_ACTIVE = true ;
    private static final Long TYPE_VERSION_NUMBER = new Long(1) ;
    private static final String TYPE_OBJECT_ID = String.valueOf(UUID.randomUUID());
    private static final boolean DEFAULT = true ;

    private static final String TYPE_CODE = "HM" ;
    private static final String TYPE_NAME = "Home";



    // ADDRESS stuff
    private static final String ADDR_ID = "1";
    private static final Long ADDR_TYPE_VERSION_NUMBER = new Long(1);
    private static final String ADDR_TYPE_OBJECT_ID = String.valueOf(UUID.randomUUID());
    private static final String ATTENTION_LINE= "Attn Line";
    private static final String ADDR_LINE1 = "Line 1";
    private static final String ADDR_LINE2 = "Line 2";
    private static final String ADDR_LINE3 = "Line 3";
    private static final String ADDR_CITY = "Super Sweet City";
    private static final String ADDR_STATE_PROVINCE_CODE = "CA";
    private static final String ADDR_POSTAL_CODE = "55555" ;
    private static final String ADDR_COUNTRY_CODE = "US" ;
    private static final boolean ADDR_SUPPRESS = false ;
    private static final boolean ADDR_DEFAULT = true;
    private static final boolean ADDR_ACTIVE = true;
    private static final String ADDR_FMT = "address format" ;
    private static final DateTime MODIFIED_DATE = new DateTime();
    private static final DateTime VALIDATED_DATE = new DateTime() ;
    private static final boolean VALIDATED = true ;
    private static final String NOTE_MESSAGE = "note message" ;
    private static final Long ADDR_VERSION_NUMBER = new Long(1);
    private static final String ADDR_OBJECT_ID = String.valueOf(UUID.randomUUID());


    //PHONE stuff
    private static final String PHONE_ID = "1";
    private static final String PHONE_NUMBER = "439-0116";
    private static final String PHONE_COUNTRY_CODE = "1";
    private static final String PHONE_EXTENSION_NUMBER ="12" ;
    private static final boolean PHONE_SUPPRESS = false;
    private static final boolean PHONE_ACTIVE = true ;
    private static final Long PHONE_VERSION_NUMBER = new Long(1);
    private static final boolean PHONE_DEFAULT = true;
    private static final String PHONE_OBJECT_ID = String.valueOf(UUID.randomUUID());
    private static final Long PHONE_TYPE_VERSION_NUMBER = new Long(1) ;
    private static final String PHONE_TYPE_OBJECT_ID = String.valueOf(UUID.randomUUID());

    //email data
    private static final String EMAIL_ID = "1";
    private static final Long EMAIL_TYPE_VERSION_NUMBER = new Long(1) ;
    private static final String EMAIL_TYPE_OBJECT_ID = String.valueOf(UUID.randomUUID());
    private static final String EMAIL_ADDRESS = "test@kuali.org";
    private static final boolean EMAIL_SUPPRESS = false ;
    private static final boolean EMAIL_DEFAULT = true;
    private static final boolean EMAIL_ACTIVE = true;
    private static final Long EMAIL_VERSION_NUMBER = new Long(1);
    private static final String EMAIL_OBJECT_ID = String.valueOf(UUID.randomUUID());


    private static final Long VERSION_NUMBER = new Long(1);
    private static final String OBJECT_ID = String.valueOf(UUID.randomUUID());
    private static final boolean ACTIVE = true;

    public static EntityTypeContactInfo create () {
        final EntityAddress addr = EntityAddress.Builder.create(new EntityAddressContract() {
            @Override
            public String getEntityId() {
                return EntityTypeContactInfoHelper.ENTITY_ID;
            }

            @Override
            public String getEntityTypeCode() {
                return EntityTypeContactInfoHelper.ENTITY_TYPE_CODE;
            }

            @Override
            public CodedAttributeContract getAddressType() {
                return CodedAttribute.Builder.create(new CodedAttributeContract() {
                    @Override
                    public String getName() {
                        return EntityTypeContactInfoHelper.TYPE_NAME;
                    }

                    @Override
                    public String getSortCode() {
                        return EntityTypeContactInfoHelper.TYPE_SORT_CODE;
                    }

                    @Override
                    public String getCode() {
                        return EntityTypeContactInfoHelper.TYPE_CODE;
                    }

                    @Override
                    public String getObjectId() {
                        return EntityTypeContactInfoHelper.ADDR_TYPE_OBJECT_ID;
                    }

                    @Override
                    public boolean isActive() {
                        return EntityTypeContactInfoHelper.TYPE_ACTIVE;
                    }

                    @Override
                    public Long getVersionNumber() {
                        return EntityTypeContactInfoHelper.ADDR_TYPE_VERSION_NUMBER;
                    }
                }).build();
            }

            @Override
            public String getAttentionLine() {
                return EntityTypeContactInfoHelper.ATTENTION_LINE;
            }

            @Override
            public String getLine1() {
                return EntityTypeContactInfoHelper.ADDR_LINE1;
            }

            @Override
            public String getLine2() {
                return EntityTypeContactInfoHelper.ADDR_LINE2;
            }

            @Override
            public String getLine3() {
                return EntityTypeContactInfoHelper.ADDR_LINE3;
            }

            @Override
            public String getCity() {
                return EntityTypeContactInfoHelper.ADDR_CITY;
            }

            @Override
            public String getStateProvinceCode() {
                return EntityTypeContactInfoHelper.ADDR_STATE_PROVINCE_CODE;
            }

            @Override
            public String getPostalCode() {
                return EntityTypeContactInfoHelper.ADDR_POSTAL_CODE;
            }

            @Override
            public String getCountryCode() {
                return EntityTypeContactInfoHelper.ADDR_COUNTRY_CODE;
            }

            @Override
            public String getAttentionLineUnmasked() {
                return EntityTypeContactInfoHelper.ATTENTION_LINE;
            }

            @Override
            public String getLine1Unmasked() {
                return EntityTypeContactInfoHelper.ADDR_LINE1 ;
            }

            @Override
            public String getLine2Unmasked() {
                return EntityTypeContactInfoHelper.ADDR_LINE2;
            }

            @Override
            public String getLine3Unmasked() {
                return EntityTypeContactInfoHelper.ADDR_LINE3;
            }

            @Override
            public String getCityUnmasked() {
                return EntityTypeContactInfoHelper.ADDR_CITY;
            }

            @Override
            public String getStateProvinceCodeUnmasked() {
                return EntityTypeContactInfoHelper.ADDR_STATE_PROVINCE_CODE;
            }

            @Override
            public String getPostalCodeUnmasked() {
                return EntityTypeContactInfoHelper.ADDR_POSTAL_CODE;
            }

            @Override
            public String getCountryCodeUnmasked() {
                return EntityTypeContactInfoHelper.ADDR_COUNTRY_CODE;
            }

            @Override
            public String getAddressFormat() {
                return EntityTypeContactInfoHelper.ADDR_FMT;
            }

            @Override
            public DateTime getModifiedDate() {
                return EntityTypeContactInfoHelper.MODIFIED_DATE ;
            }

            @Override
            public DateTime getValidatedDate() {
                return EntityTypeContactInfoHelper.VALIDATED_DATE ;
            }

            @Override
            public boolean isValidated() {
                return EntityTypeContactInfoHelper.VALIDATED;
            }

            @Override
            public String getNoteMessage() {
                return EntityTypeContactInfoHelper.NOTE_MESSAGE;
            }

            @Override
            public boolean isSuppressAddress() {
                return EntityTypeContactInfoHelper.ADDR_SUPPRESS;
            }

            @Override
            public boolean isDefaultValue() {
                return EntityTypeContactInfoHelper.ADDR_DEFAULT;
            }

            @Override
            public String getObjectId() {
                return EntityTypeContactInfoHelper.ADDR_OBJECT_ID;
            }

            @Override
            public String getId() {
                return EntityTypeContactInfoHelper.ADDR_ID ;
            }

            @Override
            public boolean isActive() {
                return EntityTypeContactInfoHelper.ADDR_ACTIVE;
            }

            @Override
            public Long getVersionNumber() {
                return EntityTypeContactInfoHelper.ADDR_VERSION_NUMBER;
            }
        } ).build();
        final EntityEmail email = EntityEmail.Builder.create(new EntityEmailContract() {
                    @Override
                    public String getEntityId() {
                        return EntityTypeContactInfoHelper.ENTITY_ID;
                    }

                    @Override
                    public String getEntityTypeCode() {
                        return EntityTypeContactInfoHelper.ENTITY_TYPE_CODE;
                    }

                    @Override
                    public CodedAttributeContract getEmailType() {
                        return CodedAttribute.Builder.create(new CodedAttributeContract() {
                            @Override
                            public String getName() {
                                return EntityTypeContactInfoHelper.TYPE_NAME;
                            }

                            @Override
                            public String getSortCode() {
                                return EntityTypeContactInfoHelper.TYPE_SORT_CODE;
                            }

                            @Override
                            public String getCode() {
                                return EntityTypeContactInfoHelper.TYPE_CODE;
                            }

                            @Override
                            public String getObjectId() {
                                return EntityTypeContactInfoHelper.EMAIL_TYPE_OBJECT_ID;
                            }

                            @Override
                            public boolean isActive() {
                                return EntityTypeContactInfoHelper.TYPE_ACTIVE;
                            }

                            @Override
                            public Long getVersionNumber() {
                                return EntityTypeContactInfoHelper.EMAIL_TYPE_VERSION_NUMBER;
                            }
                        }).build();
                    }

                    @Override
                    public String getEmailAddress() {
                        return EntityTypeContactInfoHelper.EMAIL_ADDRESS;
                    }

                    @Override
                    public String getEmailAddressUnmasked() {
                        return EntityTypeContactInfoHelper.EMAIL_ADDRESS;
                    }

                    @Override
                    public boolean isSuppressEmail() {
                        return EntityTypeContactInfoHelper.EMAIL_SUPPRESS;
                    }

                    @Override
                    public boolean isDefaultValue() {
                        return EntityTypeContactInfoHelper.EMAIL_DEFAULT;
                    }

                    @Override
                    public String getObjectId() {
                        return EntityTypeContactInfoHelper.EMAIL_OBJECT_ID;
                    }

                    @Override
                    public String getId() {
                        return EntityTypeContactInfoHelper.EMAIL_ID;
                    }

                    @Override
                    public boolean isActive() {
                        return EntityTypeContactInfoHelper.EMAIL_ACTIVE;
                    }

                    @Override
                    public Long getVersionNumber() {
                        return EntityTypeContactInfoHelper.EMAIL_VERSION_NUMBER;
                    }
                }).build();
            final EntityPhone phone = EntityPhone.Builder.create(new EntityPhoneContract() {
                    @Override
                    public String getEntityId() {
                        return EntityTypeContactInfoHelper.ENTITY_ID;
                    }

                    @Override
                    public String getEntityTypeCode() {
                        return EntityTypeContactInfoHelper.ENTITY_TYPE_CODE;
                    }

                    @Override
                    public CodedAttributeContract getPhoneType() {
                        return CodedAttribute.Builder.create(new CodedAttributeContract() {
                            @Override
                            public String getName() {
                                return EntityTypeContactInfoHelper.TYPE_NAME;
                            }

                            @Override
                            public String getSortCode() {
                                return EntityTypeContactInfoHelper.TYPE_SORT_CODE;
                            }

                            @Override
                            public String getCode() {
                                return EntityTypeContactInfoHelper.TYPE_CODE;
                            }

                            @Override
                            public String getObjectId() {
                                return EntityTypeContactInfoHelper.PHONE_TYPE_OBJECT_ID;
                            }

                            @Override
                            public boolean isActive() {
                                return EntityTypeContactInfoHelper.TYPE_ACTIVE;
                            }

                            @Override
                            public Long getVersionNumber() {
                                return EntityTypeContactInfoHelper.PHONE_TYPE_VERSION_NUMBER;
                            }
                        }).build();
                    }

                    @Override
                    public String getPhoneNumber() {
                        return EntityTypeContactInfoHelper.PHONE_NUMBER;
                    }

                    @Override
                    public String getExtensionNumber() {
                        return EntityTypeContactInfoHelper.PHONE_EXTENSION_NUMBER;
                    }

                    @Override
                    public String getCountryCode() {
                        return EntityTypeContactInfoHelper.PHONE_COUNTRY_CODE;
                    }

                    @Override
                    public String getPhoneNumberUnmasked() {
                        return EntityTypeContactInfoHelper.PHONE_NUMBER;
                    }

                    @Override
                    public String getExtensionNumberUnmasked() {
                        return EntityTypeContactInfoHelper.PHONE_EXTENSION_NUMBER;
                    }

                    @Override
                    public String getCountryCodeUnmasked() {
                        return EntityTypeContactInfoHelper.PHONE_COUNTRY_CODE;
                    }

                    @Override
                    public String getFormattedPhoneNumber() {
                        return EntityTypeContactInfoHelper.PHONE_NUMBER + " x" + EntityTypeContactInfoHelper.PHONE_EXTENSION_NUMBER;
                    }

                    @Override
                    public String getFormattedPhoneNumberUnmasked() {
                        return EntityTypeContactInfoHelper.PHONE_NUMBER + " x" + EntityTypeContactInfoHelper.PHONE_EXTENSION_NUMBER;
                    }

                    @Override
                    public boolean isSuppressPhone() {
                        return EntityTypeContactInfoHelper.PHONE_SUPPRESS;
                    }

                    @Override
                    public boolean isDefaultValue() {
                        return EntityTypeContactInfoHelper.PHONE_DEFAULT;
                    }

                    @Override
                    public String getObjectId() {
                        return EntityTypeContactInfoHelper.PHONE_OBJECT_ID;
                    }

                    @Override
                    public String getId() {
                        return EntityTypeContactInfoHelper.PHONE_ID;
                    }

                    @Override
                    public boolean isActive() {
                        return EntityTypeContactInfoHelper.PHONE_ACTIVE;
                    }

                    @Override
                    public Long getVersionNumber() {
                        return EntityTypeContactInfoHelper.PHONE_VERSION_NUMBER;
                    }
                }).build();
        return EntityTypeContactInfo.Builder.create(new EntityTypeContactInfoContract() {
            @Override
            public String getEntityId() {
                return EntityTypeContactInfoHelper.ENTITY_ID;
            }

            @Override
            public String getEntityTypeCode() {
                return EntityTypeContactInfoHelper.ENTITY_TYPE_CODE;
            }

            @Override
            public CodedAttributeContract getEntityType() {
                return CodedAttribute.Builder.create(new CodedAttributeContract() {
                    @Override
                    public String getName() {
                        return EntityTypeContactInfoHelper.ENTITY_TYPE_NAME;
                    }

                    @Override
                    public String getSortCode() {
                        return EntityTypeContactInfoHelper.TYPE_SORT_CODE;
                    }

                    @Override
                    public String getCode() {
                        return EntityTypeContactInfoHelper.ENTITY_TYPE_CODE;
                    }

                    @Override
                    public String getObjectId() {
                        return EntityTypeContactInfoHelper.TYPE_OBJECT_ID;
                    }

                    @Override
                    public boolean isActive() {
                        return EntityTypeContactInfoHelper.TYPE_ACTIVE;
                    }

                    @Override
                    public Long getVersionNumber() {
                        return EntityTypeContactInfoHelper.TYPE_VERSION_NUMBER;
                    }
                }).build();
            }

            @Override
            public List<? extends EntityAddressContract> getAddresses() {
                return Arrays.asList(addr);
            }

            @Override
            public List<? extends EntityEmailContract> getEmailAddresses() {
                return Arrays.asList(email);
            }

            @Override
            public List<? extends EntityPhoneContract> getPhoneNumbers() {
                return Arrays.asList(phone);
            }

            @Override
            public EntityAddressContract getDefaultAddress() {
                return addr;
            }

            @Override
            public EntityEmailContract getDefaultEmailAddress() {
                return email;
            }

            @Override
            public EntityPhoneContract getDefaultPhoneNumber() {
                return phone;
            }

            @Override
            public String getObjectId() {
                return EntityTypeContactInfoHelper.OBJECT_ID;
            }

            @Override
            public boolean isActive() {
                return EntityTypeContactInfoHelper.ACTIVE;
            }

            @Override
            public Long getVersionNumber() {
                return EntityTypeContactInfoHelper.VERSION_NUMBER;
            }
        }).build();

    }

}
