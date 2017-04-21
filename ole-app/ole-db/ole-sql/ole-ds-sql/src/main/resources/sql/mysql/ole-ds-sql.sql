
# -----------------------------------------------------------------------
# OLE_DS_ACCESS_LOCATION_CODE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ACCESS_LOCATION_CODE_T
/

CREATE TABLE OLE_DS_ACCESS_LOCATION_CODE_T
(
      ACCESS_LOCATION_CODE_ID INTEGER
        , CODE VARCHAR(100)
        , NAME VARCHAR(500)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_ACCESS_LOCATION_CODEP1 PRIMARY KEY(ACCESS_LOCATION_CODE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_AUTHENTICATION_TYPE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_AUTHENTICATION_TYPE_T
/

CREATE TABLE OLE_DS_AUTHENTICATION_TYPE_T
(
      AUTHENTICATION_TYPE_ID INTEGER
        , CODE VARCHAR(100)
        , NAME VARCHAR(500)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_AUTHENTICATION_TYPE_P1 PRIMARY KEY(AUTHENTICATION_TYPE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_BIB_HOLDINGS_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_BIB_HOLDINGS_T
/

CREATE TABLE OLE_DS_BIB_HOLDINGS_T
(
      BIB_HOLDINGS_ID INTEGER
        , HOLDINGS_ID INTEGER NOT NULL
        , BIB_ID INTEGER NOT NULL
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_BIB_HOLDINGS_TP1 PRIMARY KEY(BIB_HOLDINGS_ID)





    
                                                                                                                                    
                                    
, INDEX OLE_DS_BIB_HOLDINGS_TI1 (BIB_ID )
    
                                                                                                                                    
                                    
, INDEX OLE_DS_BIB_HOLDINGS_TI2 (HOLDINGS_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_BIB_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_BIB_T
/

CREATE TABLE OLE_DS_BIB_T
(
      BIB_ID INTEGER
        , FORMER_ID VARCHAR(45)
        , FAST_ADD CHAR(1)
        , STAFF_ONLY CHAR(1)
        , CREATED_BY VARCHAR(40)
        , DATE_CREATED DATETIME
        , UPDATED_BY VARCHAR(40)
        , DATE_UPDATED DATETIME
        , STATUS VARCHAR(20)
        , STATUS_UPDATED_BY VARCHAR(40)
        , STATUS_UPDATED_DATE DATETIME
        , UNIQUE_ID_PREFIX VARCHAR(10)
        , CONTENT LONGTEXT
    
    , CONSTRAINT OLE_DS_BIB_TP1 PRIMARY KEY(BIB_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_BIB_INFO_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_BIB_INFO_T
/

CREATE TABLE OLE_DS_BIB_INFO_T
(
      BIB_ID_STR VARCHAR(20)
        , BIB_ID INTEGER(20)
        , TITLE VARCHAR(4000)
        , AUTHOR VARCHAR(4000)
        , PUBLISHER VARCHAR(4000)
        , ISXN VARCHAR(100)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_BIB_INFO_TP1 PRIMARY KEY(BIB_ID_STR)





    
                                                                                                                                                                                                
                                    
, INDEX OLE_DS_BIB_INFO_TI1 (BIB_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_HOLDINGS_COVERAGE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HOLDINGS_COVERAGE_T
/

CREATE TABLE OLE_DS_HOLDINGS_COVERAGE_T
(
      HOLDINGS_COVERAGE_ID INTEGER
        , HOLDINGS_ID INTEGER
        , COVERAGE_START_DATE VARCHAR(100)
        , COVERAGE_START_VOLUME VARCHAR(100)
        , COVERAGE_START_ISSUE VARCHAR(100)
        , COVERAGE_END_DATE VARCHAR(100)
        , COVERAGE_END_VOLUME VARCHAR(100)
        , COVERAGE_END_ISSUE VARCHAR(100)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_HOLDINGS_COVERAGE_TP1 PRIMARY KEY(HOLDINGS_COVERAGE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_EXT_OWNERSHIP_NOTE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_EXT_OWNERSHIP_NOTE_T
/

CREATE TABLE OLE_DS_EXT_OWNERSHIP_NOTE_T
(
      EXT_OWNERSHIP_NOTE_ID INTEGER
        , TYPE VARCHAR(20)
        , NOTE VARCHAR(4000)
        , EXT_OWNERSHIP_ID INTEGER NOT NULL
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_EXT_OWNERSHIP_NOTE_TP1 PRIMARY KEY(EXT_OWNERSHIP_NOTE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_EXT_OWNERSHIP_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_EXT_OWNERSHIP_T
/

CREATE TABLE OLE_DS_EXT_OWNERSHIP_T
(
      EXT_OWNERSHIP_ID INTEGER
        , HOLDINGS_ID INTEGER
        , EXT_OWNERSHIP_TYPE_ID INTEGER
        , ORD INTEGER
        , TEXT VARCHAR(4000)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_EXT_OWNERSHIP_TP1 PRIMARY KEY(EXT_OWNERSHIP_ID)





    
                                                                                                                                                                            
                                    
, INDEX OLE_DS_EXT_OWNERSHIP_TI1 (HOLDINGS_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_ITM_FORMER_IDENTIFIER_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ITM_FORMER_IDENTIFIER_T
/

CREATE TABLE OLE_DS_ITM_FORMER_IDENTIFIER_T
(
      ITEM_FORMER_IDENTIFIER_ID INTEGER
        , ITEM_ID INTEGER
        , TYPE VARCHAR(100)
        , VALUE VARCHAR(500)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_ITM_FORMER_IDENTIFIEP1 PRIMARY KEY(ITEM_FORMER_IDENTIFIER_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_HIGH_DENSITY_STORAGE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HIGH_DENSITY_STORAGE_T
/

CREATE TABLE OLE_DS_HIGH_DENSITY_STORAGE_T
(
      HIGH_DENSITY_STORAGE_ID INTEGER
        , HIGH_DENSITY_ROW VARCHAR(30)
        , HIGH_DENSITY_MODULE VARCHAR(30)
        , HIGH_DENSITY_SHELF VARCHAR(30)
        , HIGH_DENSITY_TRAY VARCHAR(30)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_HIGH_DENSITY_STORAGEP1 PRIMARY KEY(HIGH_DENSITY_STORAGE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_ACCESS_LOCATION_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ACCESS_LOCATION_T
/

CREATE TABLE OLE_DS_ACCESS_LOCATION_T
(
      ACCESS_LOCATION_ID INTEGER
        , HOLDINGS_ID INTEGER
        , ACCESS_LOCATION_CODE_ID INTEGER
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_ACCESS_LOCATION_TP1 PRIMARY KEY(ACCESS_LOCATION_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_HOLDINGS_URI_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HOLDINGS_URI_T
/

CREATE TABLE OLE_DS_HOLDINGS_URI_T
(
      HOLDINGS_URI_ID INTEGER
        , HOLDINGS_ID INTEGER NOT NULL
        , URI VARCHAR(400)
        , TEXT VARCHAR(400)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_HOLDINGS_URI_TP1 PRIMARY KEY(HOLDINGS_URI_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_HOLDINGS_DONOR_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HOLDINGS_DONOR_T
/

CREATE TABLE OLE_DS_HOLDINGS_DONOR_T
(
      HOLDINGS_DONOR_ID INTEGER
        , HOLDINGS_ID INTEGER
        , DONOR_CODE VARCHAR(40)
        , DONOR_DISPLAY_NOTE VARCHAR(4000)
        , DONOR_NOTE VARCHAR(4000)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_HOLDINGS_DONOR_TP1 PRIMARY KEY(HOLDINGS_DONOR_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_HOLDINGS_NOTE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HOLDINGS_NOTE_T
/

CREATE TABLE OLE_DS_HOLDINGS_NOTE_T
(
      HOLDINGS_NOTE_ID INTEGER
        , HOLDINGS_ID INTEGER NOT NULL
        , TYPE VARCHAR(100)
        , NOTE VARCHAR(4000)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_HOLDINGS_NOTE_TP1 PRIMARY KEY(HOLDINGS_NOTE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_HOLDINGS_STAT_SEARCH_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HOLDINGS_STAT_SEARCH_T
/

CREATE TABLE OLE_DS_HOLDINGS_STAT_SEARCH_T
(
      HOLDINGS_STAT_SEARCH_ID INTEGER
        , HOLDINGS_ID INTEGER
        , STAT_SEARCH_CODE_ID INTEGER
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_HOLDINGS_STAT_SEARCHP1 PRIMARY KEY(HOLDINGS_STAT_SEARCH_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_ITEM_HOLDINGS_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ITEM_HOLDINGS_T
/

CREATE TABLE OLE_DS_ITEM_HOLDINGS_T
(
      ITEM_HOLDINGS_ID INTEGER
        , HOLDINGS_ID INTEGER NOT NULL
        , ITEM_ID INTEGER NOT NULL
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_ITEM_HOLDINGS_TP1 PRIMARY KEY(ITEM_HOLDINGS_ID)

    , CONSTRAINT OLE_DS_ITEM_HOLDINGS_TC1 UNIQUE (HOLDINGS_ID, ITEM_ID)




    
                                                                                                                                    
                                    
, INDEX OLE_DS_ITEM_HOLDINGS_TI1 (HOLDINGS_ID )
    
                                                                                                                                    
                                    
, INDEX OLE_DS_ITEM_HOLDINGS_TI2 (ITEM_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_HOLDINGS_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HOLDINGS_T
/

CREATE TABLE OLE_DS_HOLDINGS_T
(
      HOLDINGS_ID INTEGER
        , BIB_ID INTEGER NOT NULL
        , HOLDINGS_TYPE VARCHAR(10)
        , FORMER_HOLDINGS_ID VARCHAR(45)
        , STAFF_ONLY CHAR(1)
        , LOCATION_ID INTEGER
        , LOCATION VARCHAR(300)
        , LOCATION_LEVEL VARCHAR(300)
        , CALL_NUMBER_TYPE_ID INTEGER
        , CALL_NUMBER_PREFIX VARCHAR(100)
        , CALL_NUMBER VARCHAR(300)
        , SHELVING_ORDER VARCHAR(300)
        , RECEIPT_STATUS_ID INTEGER
        , COPY_NUMBER VARCHAR(20)
        , PUBLISHER VARCHAR(200)
        , ACCESS_STATUS VARCHAR(40)
        , ACCESS_STATUS_DATE_UPDATED DATETIME
        , SUBSCRIPTION_STATUS VARCHAR(40)
        , INITIAL_SBRCPTN_START_DT DATETIME
        , CURRENT_SBRCPTN_START_DT DATETIME
        , CURRENT_SBRCPTN_END_DT DATETIME
        , CANCELLATION_CANDIDATE CHAR(1)
        , CANCELLATION_DECISION_DT DATETIME
        , CANCELLATION_EFFECTIVE_DT DATETIME
        , CANCELLATION_REASON VARCHAR(40)
        , PLATFORM VARCHAR(200)
        , IMPRINT VARCHAR(200)
        , LOCAL_PERSISTENT_URI VARCHAR(400)
        , ALLOW_ILL CHAR(1)
        , AUTHENTICATION_TYPE_ID INTEGER
        , PROXIED_RESOURCE VARCHAR(10)
        , NUMBER_SIMULT_USERS INTEGER(11)
        , E_RESOURCE_ID VARCHAR(40)
        , ADMIN_URL VARCHAR(400)
        , ADMIN_USERNAME VARCHAR(100)
        , ADMIN_PASSWORD VARCHAR(100)
        , ACCESS_USERNAME VARCHAR(100)
        , ACCESS_PASSWORD VARCHAR(100)
        , MATERIALS_SPECIFIED VARCHAR(200)
        , FIRST_INDICATOR VARCHAR(20)
        , SECOND_INDICATOR VARCHAR(20)
        , CREATED_BY VARCHAR(40)
        , DATE_CREATED DATETIME
        , UPDATED_BY VARCHAR(40)
        , DATE_UPDATED DATETIME
        , UNIQUE_ID_PREFIX VARCHAR(10)
        , GOKB_IDENTIFIER INTEGER
        , SOURCE_HOLDINGS_CONTENT LONGTEXT
    
    , CONSTRAINT OLE_DS_HOLDINGS_TP1 PRIMARY KEY(HOLDINGS_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_ITEM_DONOR_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ITEM_DONOR_T
/

CREATE TABLE OLE_DS_ITEM_DONOR_T
(
      ITEM_DONOR_ID INTEGER
        , ITEM_ID INTEGER
        , DONOR_CODE VARCHAR(10)
        , DONOR_DISPLAY_NOTE VARCHAR(4000)
        , DONOR_NOTE VARCHAR(4000)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_ITEM_DONOR_TP1 PRIMARY KEY(ITEM_DONOR_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_ITEM_NOTE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ITEM_NOTE_T
/

CREATE TABLE OLE_DS_ITEM_NOTE_T
(
      ITEM_NOTE_ID INTEGER
        , ITEM_ID INTEGER
        , TYPE VARCHAR(50)
        , NOTE VARCHAR(4000)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_ITEM_NOTE_TP1 PRIMARY KEY(ITEM_NOTE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_ITEM_STAT_SEARCH_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ITEM_STAT_SEARCH_T
/

CREATE TABLE OLE_DS_ITEM_STAT_SEARCH_T
(
      ITEM_STAT_SEARCH_ID INTEGER
        , ITEM_ID INTEGER
        , STAT_SEARCH_CODE_ID INTEGER
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_ITEM_STAT_SEARCH_TP1 PRIMARY KEY(ITEM_STAT_SEARCH_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_ITEM_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ITEM_T
/

CREATE TABLE OLE_DS_ITEM_T
(
      ITEM_ID INTEGER
        , HOLDINGS_ID INTEGER NOT NULL
        , FAST_ADD CHAR(1)
        , STAFF_ONLY CHAR(1)
        , BARCODE VARCHAR(30)
        , URI VARCHAR(400)
        , ITEM_TYPE_ID INTEGER
        , TEMP_ITEM_TYPE_ID INTEGER
        , ITEM_STATUS_ID INTEGER
        , ITEM_STATUS_DATE_UPDATED DATETIME
        , LOCATION_ID INTEGER
        , LOCATION VARCHAR(600)
        , LOCATION_LEVEL VARCHAR(600)
        , CALL_NUMBER_TYPE_ID INTEGER
        , CALL_NUMBER_PREFIX VARCHAR(40)
        , CALL_NUMBER VARCHAR(100)
        , SHELVING_ORDER VARCHAR(300)
        , ENUMERATION VARCHAR(100)
        , VOLUME_NUMBER VARCHAR(100)
        , CHRONOLOGY VARCHAR(100)
        , COPY_NUMBER VARCHAR(20)
        , NUM_PIECES VARCHAR(10)
        , PURCHASE_ORDER_LINE_ITEM_ID VARCHAR(45)
        , VENDOR_LINE_ITEM_ID VARCHAR(45)
        , FUND VARCHAR(100)
        , PRICE DECIMAL
        , CLAIMS_RETURNED CHAR(1)
        , CLAIMS_RETURNED_DATE_CREATED DATETIME
        , CLAIMS_RETURNED_NOTE VARCHAR(400)
        , CURRENT_BORROWER VARCHAR(30)
        , PROXY_BORROWER VARCHAR(30)
        , DUE_DATE_TIME DATETIME
        , CHECK_OUT_DATE_TIME DATETIME
        , CHECK_IN_NOTE VARCHAR(400)
        , ITEM_DAMAGED_STATUS CHAR(1)
        , ITEM_DAMAGED_NOTE VARCHAR(400)
        , MISSING_PIECES CHAR(1)
        , MISSING_PIECES_EFFECTIVE_DATE DATETIME
        , MISSING_PIECES_COUNT INTEGER
        , MISSING_PIECES_NOTE VARCHAR(400)
        , BARCODE_ARSL VARCHAR(200)
        , HIGH_DENSITY_STORAGE_ID INTEGER
        , NUM_OF_RENEW INTEGER
        , CREATED_BY VARCHAR(40)
        , DATE_CREATED DATETIME
        , UPDATED_BY VARCHAR(40)
        , DATE_UPDATED DATETIME
        , UNIQUE_ID_PREFIX VARCHAR(10)
        , ORG_DUE_DATE_TIME DATETIME
        , DESC_OF_PIECES VARCHAR(400)
    
    , CONSTRAINT OLE_DS_ITEM_TP1 PRIMARY KEY(ITEM_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_DS_ITEM_TI1 (BARCODE )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
                                    
, INDEX OLE_DS_ITEM_TI2 (HOLDINGS_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_LOC_CHECKIN_COUNT_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_LOC_CHECKIN_COUNT_T
/

CREATE TABLE OLE_DS_LOC_CHECKIN_COUNT_T
(
      CHECK_IN_LOCATION_ID INTEGER
        , ITEM_ID INTEGER
        , LOCATION_NAME VARCHAR(200)
        , LOCATION_COUNT INTEGER(20)
        , LOCATION_IN_HOUSE_COUNT INTEGER(20)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_LOC_CHECKIN_COUNT_TP1 PRIMARY KEY(CHECK_IN_LOCATION_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_PERPETUAL_ACCESS_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_PERPETUAL_ACCESS_T
/

CREATE TABLE OLE_DS_PERPETUAL_ACCESS_T
(
      HOLDINGS_PERPETUAL_ACCESS_ID INTEGER
        , HOLDINGS_ID INTEGER
        , PERPETUAL_ACCESS_START_DATE VARCHAR(100)
        , PERPETUAL_ACCESS_START_VOLUME VARCHAR(100)
        , PERPETUAL_ACCESS_START_ISSUE VARCHAR(100)
        , PERPETUAL_ACCESS_END_DATE VARCHAR(100)
        , PERPETUAL_ACCESS_END_VOLUME VARCHAR(100)
        , PERPETUAL_ACCESS_END_ISSUE VARCHAR(100)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_PERPETUAL_ACCESS_TP1 PRIMARY KEY(HOLDINGS_PERPETUAL_ACCESS_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_LICENSE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_LICENSE_T
/

CREATE TABLE OLE_DS_LICENSE_T
(
      LICENSE_ID INTEGER
        , CREATED_BY VARCHAR(40)
        , DATE_CREATED DATETIME
        , UPDATED_BY VARCHAR(40)
        , DATE_UPDATED DATETIME
        , FILE_NAME VARCHAR(400)
        , DOCUMENT_TITLE VARCHAR(40)
        , DOCUMENT_MIME_TYPE VARCHAR(100)
        , DOCUMENT_NOTE VARCHAR(400)
        , AGREEMENT_TYPE VARCHAR(40)
        , AGREEMENT_NOTE VARCHAR(400)
        , UNIQUE_ID_PREFIX VARCHAR(10)
        , CONTENT LONGBLOB
    
    , CONSTRAINT OLE_DS_LICENSE_TP1 PRIMARY KEY(LICENSE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_ITM_CLM_RTND_HSTRY_T
# -----------------------------------------------------------------------
drop table if exists OLE_ITM_CLM_RTND_HSTRY_T
/

CREATE TABLE OLE_ITM_CLM_RTND_HSTRY_T
(
      CLAIMS_RETURNED_ID INTEGER
        , CLAIMS_RETURNED_DATE_CREATED DATETIME
        , CLAIMS_RETURNED_NOTE VARCHAR(400)
        , CLAIMS_RETURNED_PATRON_BARCODE VARCHAR(100)
        , CLAIMS_RETURNED_PATRON_ID VARCHAR(40)
        , CLAIMS_RETURNED_OPERATOR_ID VARCHAR(40)
        , ITEM_ID INTEGER
    
    , CONSTRAINT OLE_ITM_CLM_RTND_HSTRY_TP1 PRIMARY KEY(CLAIMS_RETURNED_ID)





    
                                                                                                                                                                                                                                    
                                    
                                                                                                                                                                                                                                    
                                    
                                                                                                                                                                                                                                    
                                    
, INDEX OLE_ITM_CLM_RTND_HSTRY_T_I (CLAIMS_RETURNED_PATRON_BARCODE , CLAIMS_RETURNED_PATRON_ID , CLAIMS_RETURNED_OPERATOR_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_DELETED_BIB_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_DELETED_BIB_T
/

CREATE TABLE OLE_DS_DELETED_BIB_T
(
      ID BIGINT(19)
        , DELETED_BIB_ID VARCHAR(11)
        , IS_BIB_DELETED VARCHAR(1)
        , DELETED_HOLDINGS_ID VARCHAR(11)
        , IS_HOLDINGS_DELETED VARCHAR(1)
        , DELETED_ITEM_ID VARCHAR(11)
        , IS_ITEM_DELETED VARCHAR(1)
        , DATE_UPDATED DATETIME
        , CONTENT LONGTEXT
    
    , CONSTRAINT OLE_DS_DELETED_BIB_TP1 PRIMARY KEY(ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_DMGD_ITM_HSTRY_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_DMGD_ITM_HSTRY_T
/

CREATE TABLE OLE_DS_DMGD_ITM_HSTRY_T
(
      ITM_DMGD_ID INTEGER
        , DMGD_ITM_DATE DATETIME
        , DMGD_ITM_NOTE VARCHAR(400)
        , PATRON_BARCODE VARCHAR(100)
        , DMGD_PATRON_ID VARCHAR(40)
        , OPERATOR_ID VARCHAR(40)
        , ITEM_ID INTEGER
    
    , CONSTRAINT OLE_DS_DMGD_ITM_HSTRY_TP1 PRIMARY KEY(ITM_DMGD_ID)





    
                                                                                                                                                                                                                                    
                                    
                                                                                                                                                                                                                                    
                                    
                                                                                                                                                                                                                                    
                                    
, INDEX OLE_DS_DMGD_ITM_HSTRY_T_I (OPERATOR_ID , PATRON_BARCODE , DMGD_PATRON_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_MISS_PCE_ITM_HSTRY_T
# -----------------------------------------------------------------------
drop table if exists OLE_MISS_PCE_ITM_HSTRY_T
/

CREATE TABLE OLE_MISS_PCE_ITM_HSTRY_T
(
      MISSING_PIECE_ID INTEGER
        , ITEM_ID INTEGER
        , OPERATOR_ID VARCHAR(40)
        , PATRON_BARCODE VARCHAR(100)
        , PATRON_ID VARCHAR(40)
        , MISSING_PIECE_NOTE VARCHAR(400)
        , MISSING_PIECE_COUNT INTEGER
        , MISSING_PIECE_DATE DATETIME
    
    , CONSTRAINT OLE_MISS_PCE_ITM_HSTRY_TP1 PRIMARY KEY(MISSING_PIECE_ID)





    
                                                                                                                                                                                                                                                        
                                    
                                                                                                                                                                                                                                                        
                                    
                                                                                                                                                                                                                                                        
                                    
, INDEX OLE_MISS_PCE_ITM_HSTRY_I (OPERATOR_ID , PATRON_BARCODE , PATRON_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_MISS_PCE_ITM_HSTRY_S
# -----------------------------------------------------------------------
drop table if exists OLE_MISS_PCE_ITM_HSTRY_S
/

CREATE TABLE OLE_MISS_PCE_ITM_HSTRY_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_MISS_PCE_ITM_HSTRY_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_ACCESS_LOCATION_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ACCESS_LOCATION_S
/

CREATE TABLE OLE_DS_ACCESS_LOCATION_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_ACCESS_LOCATION_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_AUTHENTICATION_TYPE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_AUTHENTICATION_TYPE_S
/

CREATE TABLE OLE_DS_AUTHENTICATION_TYPE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_AUTHENTICATION_TYPE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_BIB_HOLDINGS_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_BIB_HOLDINGS_S
/

CREATE TABLE OLE_DS_BIB_HOLDINGS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_BIB_HOLDINGS_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_BIB_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_BIB_S
/

CREATE TABLE OLE_DS_BIB_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_BIB_S auto_increment = 10000001
/

# -----------------------------------------------------------------------
# OLE_DS_DELETED_BIB_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_DELETED_BIB_S
/

CREATE TABLE OLE_DS_DELETED_BIB_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_DELETED_BIB_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_EXT_OWNERSHIP_NOTE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_EXT_OWNERSHIP_NOTE_S
/

CREATE TABLE OLE_DS_EXT_OWNERSHIP_NOTE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_EXT_OWNERSHIP_NOTE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_EXT_OWNERSHIP_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_EXT_OWNERSHIP_S
/

CREATE TABLE OLE_DS_EXT_OWNERSHIP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_EXT_OWNERSHIP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_ITM_FORMER_IDENTIFIER_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ITM_FORMER_IDENTIFIER_S
/

CREATE TABLE OLE_DS_ITM_FORMER_IDENTIFIER_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_ITM_FORMER_IDENTIFIER_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_HIGH_DENSITY_STORAGE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HIGH_DENSITY_STORAGE_S
/

CREATE TABLE OLE_DS_HIGH_DENSITY_STORAGE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_HIGH_DENSITY_STORAGE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_HOLDINGS_ACCESS_LOC_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HOLDINGS_ACCESS_LOC_S
/

CREATE TABLE OLE_DS_HOLDINGS_ACCESS_LOC_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_HOLDINGS_ACCESS_LOC_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_HOLDINGS_COVERAGE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HOLDINGS_COVERAGE_S
/

CREATE TABLE OLE_DS_HOLDINGS_COVERAGE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_HOLDINGS_COVERAGE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_HOLDINGS_DONOR_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HOLDINGS_DONOR_S
/

CREATE TABLE OLE_DS_HOLDINGS_DONOR_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_HOLDINGS_DONOR_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_HOLDINGS_NOTE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HOLDINGS_NOTE_S
/

CREATE TABLE OLE_DS_HOLDINGS_NOTE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_HOLDINGS_NOTE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_ITEM_HOLDINGS_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ITEM_HOLDINGS_S
/

CREATE TABLE OLE_DS_ITEM_HOLDINGS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_ITEM_HOLDINGS_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_HOLDINGS_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HOLDINGS_S
/

CREATE TABLE OLE_DS_HOLDINGS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_HOLDINGS_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_HOLDINGS_STAT_SEARCH_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HOLDINGS_STAT_SEARCH_S
/

CREATE TABLE OLE_DS_HOLDINGS_STAT_SEARCH_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_HOLDINGS_STAT_SEARCH_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_HOLDINGS_URI_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_HOLDINGS_URI_S
/

CREATE TABLE OLE_DS_HOLDINGS_URI_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_HOLDINGS_URI_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_ITEM_DONOR_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ITEM_DONOR_S
/

CREATE TABLE OLE_DS_ITEM_DONOR_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_ITEM_DONOR_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_ITEM_NOTE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ITEM_NOTE_S
/

CREATE TABLE OLE_DS_ITEM_NOTE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_ITEM_NOTE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_ITEM_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ITEM_S
/

CREATE TABLE OLE_DS_ITEM_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_ITEM_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_ITEM_STAT_SEARCH_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ITEM_STAT_SEARCH_S
/

CREATE TABLE OLE_DS_ITEM_STAT_SEARCH_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_ITEM_STAT_SEARCH_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_ITEM_TYPE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_ITEM_TYPE_S
/

CREATE TABLE OLE_DS_ITEM_TYPE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_ITEM_TYPE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_LOC_CHECKIN_COUNT_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_LOC_CHECKIN_COUNT_S
/

CREATE TABLE OLE_DS_LOC_CHECKIN_COUNT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_LOC_CHECKIN_COUNT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_PERPETUAL_ACCESS_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_PERPETUAL_ACCESS_S
/

CREATE TABLE OLE_DS_PERPETUAL_ACCESS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_PERPETUAL_ACCESS_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_LICENSE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_LICENSE_S
/

CREATE TABLE OLE_DS_LICENSE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_LICENSE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_ITM_CLM_RTND_HSTRY_S
# -----------------------------------------------------------------------
drop table if exists OLE_ITM_CLM_RTND_HSTRY_S
/

CREATE TABLE OLE_ITM_CLM_RTND_HSTRY_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_ITM_CLM_RTND_HSTRY_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DS_DMGD_ITM_HSTRY_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_DMGD_ITM_HSTRY_S
/

CREATE TABLE OLE_DS_DMGD_ITM_HSTRY_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_DMGD_ITM_HSTRY_S auto_increment = 1
/
