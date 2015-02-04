package org.kuali.ole.deliver.api;

import org.joda.time.DateTime;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.CodedAttributeContract;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.name.EntityNameContract;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/29/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class EntityNameHelper {
    private static final String ID = "1";
    private static final String ENTITY_TYPE_CODE = "PERSON";
    private static final String ENTITY_ID = "190192";
    private static final String TYPE_CODE = "OTH";
    private static final String TYPE_NAME = "Other";
    private static final String TYPE_SORT_CODE = "b";
    private static final boolean TYPE_ACTIVE = true;
    private static final Long TYPE_VERSION_NUMBER = new Long(1);
    private static final String TYPE_OBJECT_ID = String.valueOf(UUID.randomUUID()) ;
    private static final String NAME_PREFIX = "Mr" ;
    private static final String NAME_TITLE = "DVM";
    private static final String FIRST_NAME = "Bob";
    private static final String MIDDLE_NAME = "Mob";
    private static final String LAST_NAME = "Sob" ;
    private static final String NAME_SUFFIX = "Jr";
    private static final String NOTE_MESSAGE = "note message";
    private static final DateTime NAME_CHANGED_DATE = null;
    private static final String COMPOSITE_NAME = LAST_NAME + ", " + FIRST_NAME + " " + MIDDLE_NAME;

    private static final boolean SUPPRESS = false ;
    private static final boolean DEFAULT = true ;
    private static final boolean ACTIVE = true ;
    private static final Long VERSION_NUMBER = new Long(1);
    private static final String OBJECT_ID = String.valueOf(UUID.randomUUID()) ;


    public static EntityName create() {
        return EntityName.Builder.create(new EntityNameContract() {
              @Override
            public String getEntityId() {
                return EntityNameHelper.ID;
            }

            @Override
            public CodedAttributeContract getNameType() {
                return CodedAttribute.Builder.create(new CodedAttributeContract() {
                    @Override
                    public String getName() {
                        return EntityNameHelper.TYPE_NAME;
                    }

                    @Override
                    public String getSortCode() {
                        return EntityNameHelper.TYPE_SORT_CODE;
                    }

                    @Override
                    public String getCode() {
                        return EntityNameHelper.TYPE_CODE;
                    }

                    @Override
                    public String getObjectId() {
                        return EntityNameHelper.TYPE_OBJECT_ID;
                    }

                    @Override
                    public boolean isActive() {
                        return EntityNameHelper.TYPE_ACTIVE;
                    }

                    @Override
                    public Long getVersionNumber() {
                        return EntityNameHelper.TYPE_VERSION_NUMBER;
                    }
                }).build();
            }

            @Override
            public String getFirstName() {
                return EntityNameHelper.FIRST_NAME;
            }

            @Override
            public String getFirstNameUnmasked() {
                return EntityNameHelper.FIRST_NAME;
            }

            @Override
            public String getMiddleName() {
                return EntityNameHelper.MIDDLE_NAME;
            }

            @Override
            public String getMiddleNameUnmasked() {
                return EntityNameHelper.MIDDLE_NAME;
            }

            @Override
            public String getLastName() {
                return EntityNameHelper.LAST_NAME;
            }

            @Override
            public String getLastNameUnmasked() {
                return EntityNameHelper.LAST_NAME;
            }

            @Override
            public String getNamePrefix() {
                return EntityNameHelper.NAME_PREFIX;
            }

            @Override
            public String getNamePrefixUnmasked() {
                return EntityNameHelper.NAME_PREFIX;
            }

            @Override
            public String getNameTitle() {
                return EntityNameHelper.NAME_TITLE;
            }

            @Override
            public String getNameTitleUnmasked() {
                return EntityNameHelper.NAME_TITLE;
            }

            @Override
            public String getNameSuffix() {
                return EntityNameHelper.NAME_SUFFIX;
            }

            @Override
            public String getNameSuffixUnmasked() {
                return EntityNameHelper.NAME_SUFFIX;
            }

            @Override
            public String getCompositeName() {
                return EntityNameHelper.COMPOSITE_NAME ;
            }

            @Override
            public String getCompositeNameUnmasked() {
                return EntityNameHelper.COMPOSITE_NAME;
            }

            @Override
            public String getNoteMessage() {
                return EntityNameHelper.NOTE_MESSAGE;
            }

            @Override
            public DateTime getNameChangedDate() {
                return  EntityNameHelper.NAME_CHANGED_DATE;
            }

            @Override
            public boolean isSuppressName() {
                return EntityNameHelper.SUPPRESS;
            }

            @Override
            public boolean isDefaultValue() {
                return EntityNameHelper.DEFAULT;
            }

            @Override
            public String getObjectId() {
                return EntityNameHelper.OBJECT_ID;
            }

            @Override
            public String getId() {
                return EntityNameHelper.ENTITY_ID;
            }

            @Override
            public boolean isActive() {
                return EntityNameHelper.ACTIVE;
            }

            @Override
            public Long getVersionNumber() {
                return EntityNameHelper.VERSION_NUMBER;
            }
        }).build();

    }
}
