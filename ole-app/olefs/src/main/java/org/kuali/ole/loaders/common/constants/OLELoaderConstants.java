package org.kuali.ole.loaders.common.constants;

import org.kuali.ole.OLEPropertyConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;

/**
 * Created by sheiksalahudeenm on 2/4/15.
 */
public class OLELoaderConstants {

    public static final String API_ROOT = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE) + "/api";
    public static final String LOCATION_URI = API_ROOT + "/location";
    public static final String LOCATION_LEVEL_URI = API_ROOT + "/locationLevel";
    public static final String SLASH = "/";

    public static final class OLEloaderCode {
        public static final String LOCATION_NOT_EXIST = "001";
        public static final String LOCATION_LEVEL_NOT_EXIST = "002";
        public static final String PARENT_LOCATION_NOT_EXIST = "003";
        public static final String LOCATION_SUCCESS = "004";
        public static final String LOCATION_FAILED = "005";
        public static final String LOCATION_INVALID_CONTENT = "006";
        public static final String PARENT_LOCATION_LEVEL_NOT_EXIST = "007";
        public static final String LOCATION_LEVEL_SUCCESS = "008";
        public static final String LOCATION_LEVEL_FAILED = "009";
        public static final String SCHELVING_SCHEME_NOT_EXIST = "010";
        public static final String SCHELVING_SCHEME_SUCCESS = "011";
        public static final String SCHELVING_SCHEME_FAILED = "012";
        public static final String SCHELVING_SCHEME_INVALID_CONTENT = "013";


        public static final String PATRON_NOT_EXIST = "014";
        public static final String PATRON_SUCCESS = "015";
        public static final String PATRON_FAILED = "016";
        public static final String PATRON_INVALID_CONTENT = "017";

        public static final String BORROWER_TYPE_NOT_EXIST = "018";
        public static final String BORROWER_TYPE_SUCCESS = "019";
        public static final String BORROWER_TYPE_FAILED = "020";
        public static final String BORROWER_TYPE_INVALID_CONTENT = "021";

        public static final String ITEM_TYPE_NOT_EXIST = "022";
        public static final String ITEM_TYPE_SUCCESS = "023";
        public static final String ITEM_TYPE_FAILED = "024";
        public static final String ITEM_TYPE_INVALID_CONTENT = "025";


        public static final String BAD_REQUEST = "400";
    }

    public static final class OLEloaderStatus{
        public static final int LOCATION_NOT_EXIST = 404;
        public static final int LOCATION_LEVEL_NOT_EXIST = 404;
        public static final int PARENT_LOCATION_NOT_EXIST = 404;
        public static final int LOCATION_SUCCESS = 200;
        public static final int LOCATION_FAILED = 500;
        public static final int LOCATION_INVALID_CONTENT = 500;
        public static final int PARENT_LOCATION_LEVEL_NOT_EXIST = 200;
        public static final int LOCATION_LEVEL_SUCCESS = 200;
        public static final int LOCATION_LEVEL_FAILED = 500;
        public static final int SCHELVING_SCHEME_NOT_EXIST = 200;
        public static final int SCHELVING_SCHEME_SUCCESS = 200;
        public static final int SCHELVING_SCHEME_FAILED = 500;
        public static final int SCHELVING_SCHEME_INVALID_CONTENT = 500;

        public static final int PATRON_NOT_EXIST = 200;
        public static final int PATRON_SUCCESS = 200;
        public static final int PATRON_FAILED = 500;
        public static final int PATRON_INVALID_CONTENT = 500;

        public static final int BORROWER_TYPE_NOT_EXIST = 200;
        public static final int BORROWER_TYPE_SUCCESS = 200;
        public static final int BORROWER_TYPE_FAILED = 500;
        public static final int BORROWER_TYPE_INVALID_CONTENT = 500;

        public static final int ITEM_TYPE_NOT_EXIST = 200;
        public static final int ITEM_TYPE_SUCCESS = 200;
        public static final int ITEM_TYPE_FAILED = 500;
        public static final int ITEM_TYPE_INVALID_CONTENT = 500;

        public static final int BAD_REQUEST = 400;
    }

    public static final class OLEloaderMessage {
        public static final String LOCATION_NOT_EXIST = "Location does not exist";
        public static final String LOCATION_LEVEL_NOT_EXIST = "Location level does not exist.";
        public static final String PARENT_LOCATION_NOT_EXIST = "Parent Location does not exist.";
        public static final String LOCATION_SUCCESS= "Successfully Updated the location.";
        public static final String LOCATION_FAILED = "Location update failed.";
        public static final String LOCATION_INVALID_CONTENT = "Invalid Input Content for location import";
        public static final String PARENT_LOCATION_LEVEL_NOT_EXIST = "Parent Location Level does not exist.";
        public static final String LOCATION_LEVEL_SUCCESS = "Successfully Updated the location level";
        public static final String LOCATION_LEVEL_FAILED = "Location Level update failed.";
        public static final String SCHELVING_SCHEME_NOT_EXIST = "Schelving Scheme does not exist";
        public static final String SCHELVING_SCHEME_SUCCESS = "Successfully Updated the schelving scheme.";
        public static final String SCHELVING_SCHEME_FAILED = "Schelving scheme update failed.";
        public static final String SCHELVING_SCHEME_INVALID_CONTENT = "Invalid input content for schelving scheme import";

        public static final String PATRON_NOT_EXIST = "Patron does not exits.";
        public static final String PATRON_SUCCESS = "Successfully Updated the patron.";
        public static final String PATRON_FAILED = "Patron update failed.";
        public static final String PATRON_INVALID_CONTENT = "Invalid input content for Patron import";

        public static final String BORROWER_TYPE_NOT_EXIST = "Borrower type does not exits.";
        public static final String BORROWER_TYPE_SUCCESS = "Successfully Updated the Borrower type.";
        public static final String BORROWER_TYPE_FAILED = "Borrower type update failed.";
        public static final String BORROWER_TYPE_INVALID_CONTENT = "Invalid input content for Borrower type import";

        public static final String ITEM_TYPE_NOT_EXIST = "Item type does not exits.";
        public static final String ITEM_TYPE_SUCCESS = "Successfully Updated the Item type.";
        public static final String ITEM_TYPE_FAILED = "Item type update failed.";
        public static final String ITEM_TYPE_INVALID_CONTENT = "Invalid input content for Item type import";

        public static final String BAD_REQUEST = "Bad Request";
    }

    public static final class OLELoaderContext {

        public static String LOCATION = "http;//ole.kuali.org/standards/api/location.jsonld";
        public static String LOCATION_LEVEL = "http;//ole.kuali.org/standards/api/locationLevel.jsonld";
        public static String ITEM_TYPE = "http;//ole.kuali.org/standards/api/itemType.jsonld";
        public static String ITEM_STATUS = "http;//ole.kuali.org/standards/api/itemAvailabilityStatus.jsonld";
        public static String SCHELVING_SCHEME = "http;//ole.kuali.org/standards/api/callNumberType.jsonld";
        public static String STATISTICAL_SEARCH = "http;//ole.kuali.org/standards/api/statSearchCode.jsonld";
       /* public static String BIBLIOGRAPHIC_RECORD_STATUS = "http;//ole.kuali.org/standards/api/location.jsonld";*/
        public static String BORROWER_TYPE = "http;//ole.kuali.org/standards/api/borrowerType.jsonld";
        public static String PATRON = "http;//ole.kuali.org/standards/api/patron.jsonld";
        public static String CIRCULATION_DESK = "http;//ole.kuali.org/standards/api/circulationDesk.jsonld";
    }


}
