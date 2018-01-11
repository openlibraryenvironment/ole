
-----------------------------------------------------------------------------
-- OLE_ACC_LOC_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_ACC_LOC_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_ACC_LOC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_ACC_LOC_T
(
      ACC_LOC_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , ACC_LOC_NM VARCHAR2(40)
        , ACC_LOC_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
    

)
/

ALTER TABLE OLE_ACC_LOC_T
    ADD CONSTRAINT OLE_ACC_LOC_TP1
PRIMARY KEY (ACC_LOC_ID)
/







-----------------------------------------------------------------------------
-- OLE_E_RES_ACC_ACT_CONFIG_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_E_RES_ACC_ACT_CONFIG_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_E_RES_ACC_ACT_CONFIG_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_E_RES_ACC_ACT_CONFIG_T
(
      ACC_ACT_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , WRK_FLW_NAME VARCHAR2(100)
        , WRK_FLW_TYPE VARCHAR2(40)
        , WRK_FLW_STATUS VARCHAR2(100)
        , RCPNT_USER_ID VARCHAR2(100)
        , RCPNT_ROLE_ID VARCHAR2(100)
        , MAIL_ID VARCHAR2(100)
        , MAIL_CNTNT VARCHAR2(4000)
        , MAIL_NOTFCTN VARCHAR2(1) default 'Y'
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
    

)
/

ALTER TABLE OLE_E_RES_ACC_ACT_CONFIG_T
    ADD CONSTRAINT OLE_E_RES_ACC_ACT_CONFIG_TP1
PRIMARY KEY (ACC_ACT_ID)
/







-----------------------------------------------------------------------------
-- OLE_E_RES_ACC_ACT_WRKFLW_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_E_RES_ACC_ACT_WRKFLW_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_E_RES_ACC_ACT_WRKFLW_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_E_RES_ACC_ACT_WRKFLW_T
(
      ACCESS_ID VARCHAR2(10)
        , ORDER_NO NUMBER(10,0)
        , STATUS VARCHAR2(100)
        , ACC_ACT_ID VARCHAR2(30)
        , ROLE_ID VARCHAR2(100)
        , PRSN_ID VARCHAR2(100)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
    

)
/

ALTER TABLE OLE_E_RES_ACC_ACT_WRKFLW_T
    ADD CONSTRAINT OLE_E_RES_ACC_ACT_WRKFLW_TP1
PRIMARY KEY (ACCESS_ID)
/


CREATE INDEX OLE_ACC_ACT_CONFIG_FK 
  ON OLE_E_RES_ACC_ACT_WRKFLW_T 
  (ACC_ACT_ID)
/





-----------------------------------------------------------------------------
-- OLE_ACC_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_ACC_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_ACC_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_ACC_TYP_T
(
      ACC_TYP_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , ACC_TYP_NM VARCHAR2(40)
        , ACC_TYP_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
    

)
/

ALTER TABLE OLE_ACC_TYP_T
    ADD CONSTRAINT OLE_ACC_TYP_TP1
PRIMARY KEY (ACC_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_AGR_DOC_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_AGR_DOC_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_AGR_DOC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_AGR_DOC_T
(
      OLE_AGR_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , LIC_REQS_ID VARCHAR2(40) NOT NULL
        , OLE_AGR_DOC_ID VARCHAR2(100)
    

)
/

ALTER TABLE OLE_AGR_DOC_T
    ADD CONSTRAINT OLE_AGR_DOC_TP1
PRIMARY KEY (OLE_AGR_ID)
/


CREATE INDEX LIC_REQS_FK 
  ON OLE_AGR_DOC_T 
  (LIC_REQS_ID)
/





-----------------------------------------------------------------------------
-- OLE_AGR_DOC_TYPE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_AGR_DOC_TYPE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_AGR_DOC_TYPE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_AGR_DOC_TYPE_T
(
      AGR_DOC_TYPE_ID VARCHAR2(40) default '0'
        , AGR_DOC_TYPE_NM VARCHAR2(100) NOT NULL
        , AGR_DOC_TYPE_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_AGR_DOC_TYPE_T
    ADD CONSTRAINT OLE_AGR_DOC_TYPE_TP1
PRIMARY KEY (AGR_DOC_TYPE_ID)
/







-----------------------------------------------------------------------------
-- OLE_AGR_MTH_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_AGR_MTH_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_AGR_MTH_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_AGR_MTH_T
(
      AGR_MTH_ID VARCHAR2(40) default '0'
        , AGR_MTH_NM VARCHAR2(100) NOT NULL
        , AGR_MTH_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_AGR_MTH_T
    ADD CONSTRAINT OLE_AGR_MTH_TP1
PRIMARY KEY (AGR_MTH_ID)
/







-----------------------------------------------------------------------------
-- OLE_AGR_STAT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_AGR_STAT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_AGR_STAT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_AGR_STAT_T
(
      AGR_STAT_ID VARCHAR2(40) default '0'
        , AGR_STAT_NM VARCHAR2(100) NOT NULL
        , AGR_STAT_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_AGR_STAT_T
    ADD CONSTRAINT OLE_AGR_STAT_TP1
PRIMARY KEY (AGR_STAT_ID)
/







-----------------------------------------------------------------------------
-- OLE_AGR_TYPE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_AGR_TYPE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_AGR_TYPE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_AGR_TYPE_T
(
      AGR_TYPE_ID VARCHAR2(40) default '0'
        , AGR_TYPE_NM VARCHAR2(100) NOT NULL
        , AGR_TYPE_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_AGR_TYPE_T
    ADD CONSTRAINT OLE_AGR_TYPE_TP1
PRIMARY KEY (AGR_TYPE_ID)
/







-----------------------------------------------------------------------------
-- OLE_AGREEMENT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_AGREEMENT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_AGREEMENT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_AGREEMENT_T
(
      AGR_ID VARCHAR2(40)
        , AGR_LICENCE_TITLE VARCHAR2(250)
        , AGR_CONTRACT_NBR VARCHAR2(20)
        , PO_ID NUMBER(10,0)
        , AGR_STATUS_ID VARCHAR2(40)
        , AGR_CODINGD_STATUS_ID VARCHAR2(40)
        , AGR_LICENSOR_ID VARCHAR2(40)
        , AGR_LICENSEE_ID VARCHAR2(40)
        , AGR_GENERAL_NTE VARCHAR2(250)
        , AGR_LICENSE_SITE VARCHAR2(250)
        , AGR_FEE_SCHDLE VARCHAR2(250)
        , AGR_INFLATION_CAP VARCHAR2(250)
        , AGR_PAYMENT_TERM VARCHAR2(250)
        , AGR_GOVRNING_LAW VARCHAR2(250)
        , AGR_NEW_TITLE_ACCESS VARCHAR2(250)
        , AGR_CANCELLATION_RIGHTS VARCHAR2(250)
        , AGR_ADDITIONAL_TERM VARCHAR2(250)
        , AGR_NTC_PER_FOR_TERMN VARCHAR2(250)
        , AGR_PERP_ACCESS VARCHAR2(1)
        , AGR_PERP_ACC_NTE VARCHAR2(250)
        , AGR_AUTH_USRS VARCHAR2(250)
        , AGR_AUTH_USRS_GEN_NTE VARCHAR2(250)
        , AGR_DEP_IN_JR VARCHAR2(1)
        , AGR_FAIR_USE VARCHAR2(1)
        , AGR_RTS_NT_GR_DE VARCHAR2(1)
        , AGR_ILL_PR VARCHAR2(1)
        , AGR_ILL_EL VARCHAR2(1)
        , AGR_ILL_LON_DOC_PERM VARCHAR2(1)
        , AGR_ILL_NON_PRFT_ONLY VARCHAR2(1)
        , AGR_ILL_SAM_CNTRY_RES VARCHAR2(1)
        , AGR_ILL_NTE VARCHAR2(250)
        , AGR_LIB_RES_ELEC VARCHAR2(1)
        , AGR_LIB_RES_PRI VARCHAR2(1)
        , AGR_LIB_RES_CMS_NTE VARCHAR2(250)
        , AGR_SCOH_SHA VARCHAR2(1)
        , AGR_TXT_MIN VARCHAR2(1)
        , AGR_PERM_RIGHTS VARCHAR2(1)
        , AGR_STR_RIGHTS VARCHAR2(1)
        , AGR_MUL_RIGHTS_NTE VARCHAR2(250)
        , AGR_APC_OFFST VARCHAR2(1)
        , AGR_APC_OFFST_NTE VARCHAR2(250)
        , AGR_START_DT DATE
        , AGR_END_DT DATE
    

)
/

ALTER TABLE OLE_AGREEMENT_T
    ADD CONSTRAINT OLE_AGREEMENT_TP1
PRIMARY KEY (AGR_ID)
/







-----------------------------------------------------------------------------
-- OLE_AUTHCAT_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_AUTHCAT_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_AUTHCAT_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_AUTHCAT_TYP_T
(
      AUTHCAT_TYP_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , AUTHCAT_TYP_NM VARCHAR2(40)
        , AUTHCAT_TYP_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
    

)
/

ALTER TABLE OLE_AUTHCAT_TYP_T
    ADD CONSTRAINT OLE_AUTHCAT_TYP_TP1
PRIMARY KEY (AUTHCAT_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_BDGT_CD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BDGT_CD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BDGT_CD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BDGT_CD_T
(
      BDGT_CD_ID VARCHAR2(40) default '0'
        , INPT_VAL VARCHAR2(100) NOT NULL
        , CHRT_CD VARCHAR2(100) NOT NULL
        , FUND_CD VARCHAR2(100) NOT NULL
        , OBJT_CD VARCHAR2(100) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_BDGT_CD_T
    ADD CONSTRAINT OLE_BDGT_CD_TP1
PRIMARY KEY (BDGT_CD_ID)
/







-----------------------------------------------------------------------------
-- OLE_CALL_NMBR_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CALL_NMBR_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CALL_NMBR_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CALL_NMBR_T
(
      INPUT_VAL VARCHAR2(40) default '0'
        , PROFILE_ID VARCHAR2(100) NOT NULL
        , CALL_NUM_PREF_ONE VARCHAR2(100)
        , CALL_NUM_PREF_TWO VARCHAR2(100)
        , CALL_NUM_PREF_THREE VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_CALL_NMBR_T
    ADD CONSTRAINT OLE_CALL_NMBR_TP1
PRIMARY KEY (INPUT_VAL)
/







-----------------------------------------------------------------------------
-- OLE_CAT_ACCS_MTHD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_ACCS_MTHD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_ACCS_MTHD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_ACCS_MTHD_T
(
      ACCS_MTHD_ID NUMBER(8) default 0
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , ACCS_MTHD_CD VARCHAR2(40)
        , ACCS_MTHD_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
    
    , CONSTRAINT ACCS_MTHD_CD UNIQUE (ACCS_MTHD_CD)

)
/

ALTER TABLE OLE_CAT_ACCS_MTHD_T
    ADD CONSTRAINT OLE_CAT_ACCS_MTHD_TP1
PRIMARY KEY (ACCS_MTHD_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_ACQ_MTHD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_ACQ_MTHD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_ACQ_MTHD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_ACQ_MTHD_T
(
      ACQ_MTHD_ID NUMBER(8) default 0
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , ACQ_MTHD_CD VARCHAR2(40)
        , ACQ_MTHD_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
    
    , CONSTRAINT ACQ_MTHD_CD UNIQUE (ACQ_MTHD_CD)

)
/

ALTER TABLE OLE_CAT_ACQ_MTHD_T
    ADD CONSTRAINT OLE_CAT_ACQ_MTHD_TP1
PRIMARY KEY (ACQ_MTHD_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_ACTION_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_ACTION_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_ACTION_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_ACTION_T
(
      ACTION_ID NUMBER(8) default 0
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , ACTION_CD VARCHAR2(40) NOT NULL
        , ACTION_NM VARCHAR2(100) NOT NULL
        , ACTION_DESC VARCHAR2(100)
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
    
    , CONSTRAINT ACTION_CD UNIQUE (ACTION_CD)

)
/

ALTER TABLE OLE_CAT_ACTION_T
    ADD CONSTRAINT OLE_CAT_ACTION_TP1
PRIMARY KEY (ACTION_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_BIB_RECORD_STAT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_BIB_RECORD_STAT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_BIB_RECORD_STAT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_BIB_RECORD_STAT_T
(
      BIB_RECORD_STAT_ID VARCHAR2(40) default '0'
        , BIB_RECORD_STAT_CD VARCHAR2(40) NOT NULL
        , BIB_RECORD_STAT_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    
    , CONSTRAINT BIB_RECORD_STAT_CD UNIQUE (BIB_RECORD_STAT_CD)

)
/

ALTER TABLE OLE_CAT_BIB_RECORD_STAT_T
    ADD CONSTRAINT OLE_CAT_BIB_RECORD_STAT_TP1
PRIMARY KEY (BIB_RECORD_STAT_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_CMPLT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_CMPLT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_CMPLT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_CMPLT_T
(
      CMPLT_ID NUMBER(8) default 0
        , CMPLT_CD VARCHAR2(40) NOT NULL
        , CMPLT_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(700) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
    
    , CONSTRAINT CMPLT_CD UNIQUE (CMPLT_CD)

)
/

ALTER TABLE OLE_CAT_CMPLT_T
    ADD CONSTRAINT OLE_CAT_CMPLT_TP1
PRIMARY KEY (CMPLT_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_ELA_RLSHP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_ELA_RLSHP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_ELA_RLSHP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_ELA_RLSHP_T
(
      ELE_RLSHP_ID NUMBER(8) default 0
        , ELE_RLSHP_CD VARCHAR2(40)
        , ELE_RLSHP_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(700) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
    
    , CONSTRAINT ELE_RLSHP_CD UNIQUE (ELE_RLSHP_CD)

)
/

ALTER TABLE OLE_CAT_ELA_RLSHP_T
    ADD CONSTRAINT OLE_CAT_ELA_RLSHP_TP1
PRIMARY KEY (ELE_RLSHP_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_ENCD_LVL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_ENCD_LVL_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_ENCD_LVL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_ENCD_LVL_T
(
      ENCD_LVL_ID NUMBER(8) default 0
        , ENCD_LVL_CD VARCHAR2(40) NOT NULL
        , ENCD_LVL_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(700) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
    
    , CONSTRAINT ENCD_LVL_CD UNIQUE (ENCD_LVL_CD)

)
/

ALTER TABLE OLE_CAT_ENCD_LVL_T
    ADD CONSTRAINT OLE_CAT_ENCD_LVL_TP1
PRIMARY KEY (ENCD_LVL_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_FLD_ENCD_LVL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_FLD_ENCD_LVL_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_FLD_ENCD_LVL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_FLD_ENCD_LVL_T
(
      FLD_ENCD_LVL_ID NUMBER(8) default 0
        , FLD_ENCD_LVL_CD VARCHAR2(40)
        , FLD_ENCD_LVL_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(700) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
    
    , CONSTRAINT FLD_ENCD_LVL_CD UNIQUE (FLD_ENCD_LVL_CD)

)
/

ALTER TABLE OLE_CAT_FLD_ENCD_LVL_T
    ADD CONSTRAINT OLE_CAT_FLD_ENCD_LVL_TP1
PRIMARY KEY (FLD_ENCD_LVL_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_GEN_RTN_POL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_GEN_RTN_POL_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_GEN_RTN_POL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_GEN_RTN_POL_T
(
      GEN_RTN_POL_ID NUMBER(8) default 0
        , GEN_RTN_POL_CD VARCHAR2(40) NOT NULL
        , GEN_RTN_POL_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(700) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
    
    , CONSTRAINT GEN_RTN_POL_CD UNIQUE (GEN_RTN_POL_CD)

)
/

ALTER TABLE OLE_CAT_GEN_RTN_POL_T
    ADD CONSTRAINT OLE_CAT_GEN_RTN_POL_TP1
PRIMARY KEY (GEN_RTN_POL_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_ITM_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_ITM_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_ITM_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_ITM_TYP_T
(
      ITM_TYP_CD_ID VARCHAR2(40) default '0'
        , ITM_TYP_CD VARCHAR2(40) NOT NULL
        , ITM_TYP_NM VARCHAR2(100) NOT NULL
        , ITM_TYP_DESC VARCHAR2(700)
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT DATE NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , DATE_UPDATED TIMESTAMP
    
    , CONSTRAINT ITM_TYP_CD UNIQUE (ITM_TYP_CD)

)
/

ALTER TABLE OLE_CAT_ITM_TYP_T
    ADD CONSTRAINT OLE_CAT_ITM_TYP_TP1
PRIMARY KEY (ITM_TYP_CD_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_LND_POL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_LND_POL_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_LND_POL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_LND_POL_T
(
      LND_POL_ID NUMBER(8) default 0
        , LND_POL_CD VARCHAR2(30) NOT NULL
        , LND_POL_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT DATE NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    
    , CONSTRAINT LND_POL_CD UNIQUE (LND_POL_CD)

)
/

ALTER TABLE OLE_CAT_LND_POL_T
    ADD CONSTRAINT OLE_CAT_LND_POL_TP1
PRIMARY KEY (LND_POL_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_LOC_COUNTRY_CD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_LOC_COUNTRY_CD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_LOC_COUNTRY_CD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_LOC_COUNTRY_CD_T
(
      LOC_COUNTRY_CD_ID NUMBER(8) default 0
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , LOC_COUNTRY_CD VARCHAR2(40) NOT NULL
        , LOC_COUNTRY_NM VARCHAR2(100) NOT NULL
        , LOC_COUNTRY_REGION_NM VARCHAR2(100) NOT NULL
        , LOC_COUNTRY_SEQUENCE_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
    
    , CONSTRAINT LOC_COUNTRY_CD UNIQUE (LOC_COUNTRY_CD)

)
/

ALTER TABLE OLE_CAT_LOC_COUNTRY_CD_T
    ADD CONSTRAINT OLE_CAT_LOC_COUNTRY_CD_TP1
PRIMARY KEY (LOC_COUNTRY_CD_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_NTN_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_NTN_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_NTN_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_NTN_TYP_T
(
      NTN_TYP_ID VARCHAR2(40) default '0'
        , NTN_TYP_CD VARCHAR2(40) NOT NULL
        , NTN_TYP_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    
    , CONSTRAINT NTN_TYP_CD UNIQUE (NTN_TYP_CD)

)
/

ALTER TABLE OLE_CAT_NTN_TYP_T
    ADD CONSTRAINT OLE_CAT_NTN_TYP_TP1
PRIMARY KEY (NTN_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_PRVCY_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_PRVCY_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_PRVCY_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_PRVCY_T
(
      PRVCY_ID VARCHAR2(40) default '0'
        , PRVCY_CD VARCHAR2(40)
        , PRVCY_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    
    , CONSTRAINT PRVCY_CD UNIQUE (PRVCY_CD)

)
/

ALTER TABLE OLE_CAT_PRVCY_T
    ADD CONSTRAINT OLE_CAT_PRVCY_TP1
PRIMARY KEY (PRVCY_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_RCPT_STAT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_RCPT_STAT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_RCPT_STAT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_RCPT_STAT_T
(
      RCPT_STAT_ID VARCHAR2(40) default '0'
        , RCPT_STAT_CD VARCHAR2(40) NOT NULL
        , RCPT_STAT_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , DATE_UPDATED TIMESTAMP
    
    , CONSTRAINT RCPT_STAT_CD UNIQUE (RCPT_STAT_CD)

)
/

ALTER TABLE OLE_CAT_RCPT_STAT_T
    ADD CONSTRAINT OLE_CAT_RCPT_STAT_TP1
PRIMARY KEY (RCPT_STAT_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_REC_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_REC_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_REC_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_REC_TYP_T
(
      REC_TYP_ID NUMBER(8) default 0
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , REC_TYP_CD VARCHAR2(40) NOT NULL
        , REC_TYP_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
    
    , CONSTRAINT REC_TYP_CD UNIQUE (REC_TYP_CD)

)
/

ALTER TABLE OLE_CAT_REC_TYP_T
    ADD CONSTRAINT OLE_CAT_REC_TYP_TP1
PRIMARY KEY (REC_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_REPRO_POL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_REPRO_POL_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_REPRO_POL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_REPRO_POL_T
(
      REPRO_POL_ID NUMBER(8) default 0
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , REPRO_POL_CD VARCHAR2(40) NOT NULL
        , REPRO_POL_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
    
    , CONSTRAINT REPRO_POL_CD UNIQUE (REPRO_POL_CD)

)
/

ALTER TABLE OLE_CAT_REPRO_POL_T
    ADD CONSTRAINT OLE_CAT_REPRO_POL_TP1
PRIMARY KEY (REPRO_POL_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_SHVLG_ORD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_SHVLG_ORD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_SHVLG_ORD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_SHVLG_ORD_T
(
      SHVLG_ORD_ID NUMBER(8) default 0
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , SHVLG_ORD_CD VARCHAR2(40)
        , SHVLG_ORD_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
    
    , CONSTRAINT SHVLG_ORD_CD UNIQUE (SHVLG_ORD_CD)

)
/

ALTER TABLE OLE_CAT_SHVLG_ORD_T
    ADD CONSTRAINT OLE_CAT_SHVLG_ORD_TP1
PRIMARY KEY (SHVLG_ORD_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_SHVLG_SCHM_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_SHVLG_SCHM_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_SHVLG_SCHM_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_SHVLG_SCHM_T
(
      SHVLG_SCHM_ID NUMBER(8) default 0
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , SHVLG_SCHM_CD VARCHAR2(40)
        , SHVLG_SCHM_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
        , DATE_UPDATED TIMESTAMP
    
    , CONSTRAINT SHVLG_SCHM_CD UNIQUE (SHVLG_SCHM_CD)

)
/

ALTER TABLE OLE_CAT_SHVLG_SCHM_T
    ADD CONSTRAINT OLE_CAT_SHVLG_SCHM_TP1
PRIMARY KEY (SHVLG_SCHM_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_SPCP_RPT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_SPCP_RPT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_SPCP_RPT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_SPCP_RPT_T
(
      SPCP_RPT_ID NUMBER(8) default 0
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , SPCP_RPT_CD VARCHAR2(40) NOT NULL
        , SPCP_RPT_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
    
    , CONSTRAINT SPCP_RPT_CD UNIQUE (SPCP_RPT_CD)

)
/

ALTER TABLE OLE_CAT_SPCP_RPT_T
    ADD CONSTRAINT OLE_CAT_SPCP_RPT_TP1
PRIMARY KEY (SPCP_RPT_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_SPFC_RTN_POL_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_SPFC_RTN_POL_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_SPFC_RTN_POL_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_SPFC_RTN_POL_TYP_T
(
      SPFC_RTN_POL_TYP_ID NUMBER(8) default 0
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , SPFC_RTN_POL_TYP_CD VARCHAR2(40) NOT NULL
        , SPFC_RTN_POL_TYP_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
    
    , CONSTRAINT SPFC_RTN_POL_TYP_CD UNIQUE (SPFC_RTN_POL_TYP_CD)

)
/

ALTER TABLE OLE_CAT_SPFC_RTN_POL_TYP_T
    ADD CONSTRAINT OLE_CAT_SPFC_RTN_POL_TYP_TP1
PRIMARY KEY (SPFC_RTN_POL_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_SPFC_RTN_POL_UNT_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_SPFC_RTN_POL_UNT_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_SPFC_RTN_POL_UNT_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_SPFC_RTN_POL_UNT_TYP_T
(
      SPFC_RTN_POL_UNT_TYP_ID VARCHAR2(40) default '0'
        , SPFC_RTN_POL_UNT_TYP_CD VARCHAR2(40) NOT NULL
        , SPFC_RTN_POL_UNT_TYP_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    
    , CONSTRAINT SPFC_RTN_POL_UNT_TYP_CD UNIQUE (SPFC_RTN_POL_UNT_TYP_CD)

)
/

ALTER TABLE OLE_CAT_SPFC_RTN_POL_UNT_TYP_T
    ADD CONSTRAINT OLE_CAT_SPFC_RTN_POL_UNT_TYP1
PRIMARY KEY (SPFC_RTN_POL_UNT_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_SRC_TRM_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_SRC_TRM_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_SRC_TRM_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_SRC_TRM_T
(
      SRC_TRM_ID VARCHAR2(40) default '0'
        , SRC_TRM_CD VARCHAR2(40) NOT NULL
        , SRC_TRM_NM VARCHAR2(501) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    
    , CONSTRAINT SRC_TRM_CD UNIQUE (SRC_TRM_CD)

)
/

ALTER TABLE OLE_CAT_SRC_TRM_T
    ADD CONSTRAINT OLE_CAT_SRC_TRM_TP1
PRIMARY KEY (SRC_TRM_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_STAT_SRCH_CD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_STAT_SRCH_CD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_STAT_SRCH_CD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_STAT_SRCH_CD_T
(
      STAT_SRCH_CD_ID NUMBER(8) default 0
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , STAT_SRCH_CD VARCHAR2(40) NOT NULL
        , STAT_SRCH_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT DATE NOT NULL
        , DATE_UPDATED TIMESTAMP
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
    
    , CONSTRAINT STAT_SRCH_CD UNIQUE (STAT_SRCH_CD)

)
/

ALTER TABLE OLE_CAT_STAT_SRCH_CD_T
    ADD CONSTRAINT OLE_CAT_STAT_SRCH_CD_TP1
PRIMARY KEY (STAT_SRCH_CD_ID)
/







-----------------------------------------------------------------------------
-- OLE_CAT_TYPE_OWNERSHIP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CAT_TYPE_OWNERSHIP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CAT_TYPE_OWNERSHIP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CAT_TYPE_OWNERSHIP_T
(
      TYPE_OWNERSHIP_ID NUMBER(8) default 0
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , TYPE_OWNERSHIP_CD VARCHAR2(100) NOT NULL
        , TYPE_OWNERSHIP_NM VARCHAR2(100) NOT NULL
        , SRC VARCHAR2(100) NOT NULL
        , SRC_DT TIMESTAMP NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
        , DATE_UPDATED TIMESTAMP
    
    , CONSTRAINT TYPE_OWNERSHIP_CD UNIQUE (TYPE_OWNERSHIP_CD)

)
/

ALTER TABLE OLE_CAT_TYPE_OWNERSHIP_T
    ADD CONSTRAINT OLE_CAT_TYPE_OWNERSHIP_TP1
PRIMARY KEY (TYPE_OWNERSHIP_ID)
/







-----------------------------------------------------------------------------
-- OLE_CIRC_DSK_DTL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CIRC_DSK_DTL_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CIRC_DSK_DTL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CIRC_DSK_DTL_T
(
      CRCL_DSK_DTL_ID VARCHAR2(40) default '0'
        , OPTR_ID VARCHAR2(80)
        , DEFAULT_LOC VARCHAR2(1) NOT NULL
        , ALLOWED_LOC VARCHAR2(1) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , CRCL_DSK_ID VARCHAR2(40)
    

)
/

ALTER TABLE OLE_CIRC_DSK_DTL_T
    ADD CONSTRAINT OLE_CIRC_DSK_DTL_TP1
PRIMARY KEY (CRCL_DSK_DTL_ID)
/


CREATE INDEX CRCL_ID_constr 
  ON OLE_CIRC_DSK_DTL_T 
  (CRCL_DSK_ID)
/





-----------------------------------------------------------------------------
-- OLE_CODE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CODE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CODE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CODE_T
(
      CD_ID VARCHAR2(40) default '0' NOT NULL
        , PROFILE_ID VARCHAR2(100)
        , INPUT_VAL VARCHAR2(100)
        , ITM_TYP VARCHAR2(100) NOT NULL
        , ITM_STAT_CD VARCHAR2(100) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_CODE_T
    ADD CONSTRAINT OLE_CODE_TP1
PRIMARY KEY (PROFILE_ID,INPUT_VAL)
/







-----------------------------------------------------------------------------
-- OLE_CONT_TYPS_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CONT_TYPS_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CONT_TYPS_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CONT_TYPS_T
(
      CONT_TYPS_ID VARCHAR2(10)
        , E_RES_REC_ID VARCHAR2(10)
        , CONT_TYP_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_CONT_TYPS_T
    ADD CONSTRAINT OLE_CONT_TYPS_TP1
PRIMARY KEY (CONT_TYPS_ID)
/


CREATE INDEX OLE_CONT_TYPS_FK 
  ON OLE_CONT_TYPS_T 
  (CONT_TYP_ID)
/





-----------------------------------------------------------------------------
-- OLE_CONT_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CONT_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CONT_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CONT_TYP_T
(
      CONT_TYP_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , CONT_TYP_NM VARCHAR2(40)
        , CONT_TYP_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
    

)
/

ALTER TABLE OLE_CONT_TYP_T
    ADD CONSTRAINT OLE_CONT_TYP_TP1
PRIMARY KEY (CONT_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_CRCL_DSK_LOCN_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CRCL_DSK_LOCN_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CRCL_DSK_LOCN_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CRCL_DSK_LOCN_T
(
      OLE_CRCL_DSK_LOCN_ID VARCHAR2(40) default '0'
        , OLE_CRCL_DSK_ID VARCHAR2(40) NOT NULL
        , OLE_CRCL_DSK_LOCN VARCHAR2(40) NOT NULL
        , OLE_CRCL_PICKUP_DSK_LOCN VARCHAR2(40)
        , LOCN_POPUP VARCHAR2(1) default 'N'
        , LOCN_POPUP_MSG VARCHAR2(4000)
    

)
/

ALTER TABLE OLE_CRCL_DSK_LOCN_T
    ADD CONSTRAINT OLE_CRCL_DSK_LOCN_TP1
PRIMARY KEY (OLE_CRCL_DSK_LOCN_ID)
/


CREATE INDEX OLE_CRCL_DSK_LOCN_constr 
  ON OLE_CRCL_DSK_LOCN_T 
  (OLE_CRCL_DSK_LOCN)
/
CREATE INDEX OLE_CRCL_LOCN_FK 
  ON OLE_CRCL_DSK_LOCN_T 
  (OLE_CRCL_DSK_ID)
/





-----------------------------------------------------------------------------
-- OLE_CRCL_DSK_FEE_TYPE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CRCL_DSK_FEE_TYPE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CRCL_DSK_FEE_TYPE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CRCL_DSK_FEE_TYPE_T
(
      OLE_CRCL_DSK_FEE_TYPE_ID VARCHAR2(40) default '0'
        , OLE_CRCL_DSK_ID VARCHAR2(40) NOT NULL
        , FEE_TYP_ID VARCHAR2(40) NOT NULL
    

)
/

ALTER TABLE OLE_CRCL_DSK_FEE_TYPE_T
    ADD CONSTRAINT OLE_CRCL_DSK_FEE_TYPE_TP1
PRIMARY KEY (OLE_CRCL_DSK_FEE_TYPE_ID)
/


CREATE INDEX OLE_CRCL_FEE_TYPE_I 
  ON OLE_CRCL_DSK_FEE_TYPE_T 
  (FEE_TYP_ID)
/
CREATE INDEX OLE_CRCL_DSK_I 
  ON OLE_CRCL_DSK_FEE_TYPE_T 
  (OLE_CRCL_DSK_ID)
/





-----------------------------------------------------------------------------
-- OLE_CRCL_DSK_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CRCL_DSK_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CRCL_DSK_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CRCL_DSK_T
(
      OLE_CRCL_DSK_CODE VARCHAR2(40) NOT NULL
        , OLE_CRCL_DSK_PUB_NAME VARCHAR2(100) NOT NULL
        , OLE_CRCL_DSK_STAFF_NAME VARCHAR2(100) NOT NULL
        , ACTV_IND VARCHAR2(1) default 'Y' NOT NULL
        , PK_UP_LOCN_IND VARCHAR2(1) NOT NULL
        , ASR_PK_UP_LOCN_IND VARCHAR2(1) NOT NULL
        , HLD_DAYS NUMBER(8)
        , RQST_EXPIRTIN_DAYS NUMBER(8)
        , SLVNG_LAG_TIM NUMBER(8)
        , PRNT_SLP_IND VARCHAR2(1) NOT NULL
        , OLE_CRCL_DSK_ID VARCHAR2(40)
        , OLE_CLNDR_GRP_ID VARCHAR2(40)
        , HOLD_FORMAT VARCHAR2(40)
        , HOLD_QUEUE VARCHAR2(1) NOT NULL
        , STAFFED VARCHAR2(1) default 'Y' NOT NULL
        , REPLY_TO_EMAIL VARCHAR2(100)
        , RENEW_LOST_ITM VARCHAR2(1)
        , SHOW_ONHOLD_ITM VARCHAR2(50) default 'CurrentCirculationDesk'
        , DFLT_RQST_TYP_ID VARCHAR2(40)
        , DFLT_PICK_UP_LOCN_ID VARCHAR2(40)
        , FROM_EMAIL VARCHAR2(100)
    

)
/

ALTER TABLE OLE_CRCL_DSK_T
    ADD CONSTRAINT OLE_CRCL_DSK_TP1
PRIMARY KEY (OLE_CRCL_DSK_ID)
/







-----------------------------------------------------------------------------
-- OLE_DATAFIELD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DATAFIELD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DATAFIELD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DATAFIELD_T
(
      ID NUMBER(10,0)
        , OVERLAY_OPTION_ID NUMBER(10,0) NOT NULL
        , AGENDA_NAME VARCHAR2(100)
        , DATAFIELD_TAG VARCHAR2(10)
        , DATAFIELD_IND1 VARCHAR2(10)
        , DATAFIELD_IND2 VARCHAR2(10)
        , SUBFIELD_CODE VARCHAR2(10)
    

)
/

ALTER TABLE OLE_DATAFIELD_T
    ADD CONSTRAINT OLE_DATAFIELD_TP1
PRIMARY KEY (ID)
/


CREATE INDEX OVERLAY_OPTION_FK 
  ON OLE_DATAFIELD_T 
  (OVERLAY_OPTION_ID)
/





-----------------------------------------------------------------------------
-- OLE_DESC_EXT_DATASRC_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DESC_EXT_DATASRC_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DESC_EXT_DATASRC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DESC_EXT_DATASRC_T
(
      DS_ID NUMBER(8) default 0
        , DS_NAME VARCHAR2(40) NOT NULL
        , DS_DESC VARCHAR2(100)
        , DOMAIN_NAME VARCHAR2(40)
        , PORT_NUM VARCHAR2(40)
        , LOGIN_ID VARCHAR2(40)
        , AUTH_KEY VARCHAR2(40)
        , PWD VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    
    , CONSTRAINT DS_NAME UNIQUE (DS_NAME)

)
/

ALTER TABLE OLE_DESC_EXT_DATASRC_T
    ADD CONSTRAINT OLE_DESC_EXT_DATASRC_TP1
PRIMARY KEY (DS_ID)
/







-----------------------------------------------------------------------------
-- OLE_DESC_USER_PREF_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DESC_USER_PREF_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DESC_USER_PREF_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DESC_USER_PREF_T
(
      PREF_ID NUMBER(10,0) default 0
        , USER_ID VARCHAR2(40) NOT NULL
        , USER_ROLE VARCHAR2(40) NOT NULL
        , PREF_NAME VARCHAR2(100) NOT NULL
        , IMPORT_TYPE VARCHAR2(40)
        , IMPORT_STATUS VARCHAR2(40)
        , PERM_LOC VARCHAR2(40)
        , TEMP_LOC VARCHAR2(40)
        , REMOVAL_TAGS VARCHAR2(40)
        , PROTECTED_TAGS VARCHAR2(40)
        , CLASSIFICATION_SCHEME VARCHAR2(40)
        , CALL_NUM_1 VARCHAR2(40)
        , CALL_NUM_2 VARCHAR2(40)
        , CALL_NUM_3 VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    
    , CONSTRAINT PREF_NAME UNIQUE (PREF_NAME)

)
/

ALTER TABLE OLE_DESC_USER_PREF_T
    ADD CONSTRAINT OLE_DESC_USER_PREF_TP1
PRIMARY KEY (PREF_ID)
/







-----------------------------------------------------------------------------
-- OLE_DISC_EXP_MAP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DISC_EXP_MAP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DISC_EXP_MAP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DISC_EXP_MAP_T
(
      MAP_FIELD_ID VARCHAR2(40)
        , MARC_FLD VARCHAR2(100) NOT NULL
        , ITEM_FLD VARCHAR2(100) NOT NULL
        , EXP_ID VARCHAR2(40) NOT NULL
        , MAP_DESC VARCHAR2(100) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_DISC_EXP_MAP_T
    ADD CONSTRAINT OLE_DISC_EXP_MAP_TP1
PRIMARY KEY (MAP_FIELD_ID)
/


CREATE INDEX OLE_DISC_EXP_MAP_CNSTR 
  ON OLE_DISC_EXP_MAP_T 
  (EXP_ID)
/





-----------------------------------------------------------------------------
-- OLE_DISC_EXP_PROFILE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DISC_EXP_PROFILE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DISC_EXP_PROFILE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DISC_EXP_PROFILE_T
(
      EXP_ID VARCHAR2(40)
        , EXP_PROF_CD VARCHAR2(100) NOT NULL
        , EXP_PROF_NM VARCHAR2(100) NOT NULL
        , EXP_FORMAT VARCHAR2(100) NOT NULL
        , EXP_TYPE VARCHAR2(100) NOT NULL
        , EXP_TO VARCHAR2(500) NOT NULL
        , NO_OF_THREADS NUMBER(10,0) default 0 NOT NULL
        , NO_OF_RECORDS NUMBER(10,0) default 0 NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , DATA_FIELD VARCHAR2(100) NOT NULL
    

)
/

ALTER TABLE OLE_DISC_EXP_PROFILE_T
    ADD CONSTRAINT OLE_DISC_EXP_PROFILE_TP1
PRIMARY KEY (EXP_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_ADDR_SRC_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_ADDR_SRC_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_ADDR_SRC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_ADDR_SRC_T
(
      OLE_DLVR_ADDR_SRC_ID VARCHAR2(40)
        , OLE_DLVR_ADDR_SRC_CD VARCHAR2(40) NOT NULL
        , OLE_DLVR_ADDR_SRC_DESC VARCHAR2(700) NOT NULL
        , OLE_DLVR_ADDR_SRC_NM VARCHAR2(100) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) NOT NULL
    

)
/

ALTER TABLE OLE_DLVR_ADDR_SRC_T
    ADD CONSTRAINT OLE_DLVR_ADDR_SRC_TP1
PRIMARY KEY (OLE_DLVR_ADDR_SRC_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_ADD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_ADD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_ADD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_ADD_T
(
      DLVR_ADD_ID VARCHAR2(36)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , OLE_PTRN_ID VARCHAR2(36)
        , ENTITY_ADDR_ID VARCHAR2(36)
        , DLVR_PTRN_ADD_VER VARCHAR2(1)
        , PTRN_DLVR_ADD VARCHAR2(1)
        , ADD_VALID_FROM DATE
        , ADD_VALID_TO DATE
        , OLE_ADDR_SRC VARCHAR2(40)
    

)
/

ALTER TABLE OLE_DLVR_ADD_T
    ADD CONSTRAINT OLE_DLVR_ADD_TP1
PRIMARY KEY (DLVR_ADD_ID)
/


CREATE INDEX ole_dlvr_add_fk 
  ON OLE_DLVR_ADD_T 
  (ENTITY_ADDR_ID)
/
CREATE INDEX ole_dlvr_add_fk2 
  ON OLE_DLVR_ADD_T 
  (OLE_ADDR_SRC)
/
CREATE INDEX OLE_DLVR_ADD_TI1 
  ON OLE_DLVR_ADD_T 
  (OLE_PTRN_ID)
/





-----------------------------------------------------------------------------
-- OLE_DLVR_PHONE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_PHONE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_PHONE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_PHONE_T
(
      DLVR_PHONE_ID VARCHAR2(36)
        , OLE_PTRN_ID VARCHAR2(36)
        , ENTITY_PHONE_ID VARCHAR2(36)
        , OLE_PHONE_SRC VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_DLVR_PHONE_T
    ADD CONSTRAINT OLE_DLVR_PHONE_TP1
PRIMARY KEY (DLVR_PHONE_ID)
/


CREATE INDEX OLE_DLVR_PHONE_TI1 
  ON OLE_DLVR_PHONE_T 
  (ENTITY_PHONE_ID)
/
CREATE INDEX OLE_DLVR_PHONE_TI2 
  ON OLE_DLVR_PHONE_T 
  (OLE_PHONE_SRC)
/
CREATE INDEX OLE_DLVR_PHONE_TI3 
  ON OLE_DLVR_PHONE_T 
  (OLE_PTRN_ID)
/





-----------------------------------------------------------------------------
-- OLE_DLVR_EMAIL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_EMAIL_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_EMAIL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_EMAIL_T
(
      DLVR_EMAIL_ID VARCHAR2(36)
        , OLE_PTRN_ID VARCHAR2(36)
        , ENTITY_EMAIL_ID VARCHAR2(36)
        , OLE_EMAIL_SRC VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_DLVR_EMAIL_T
    ADD CONSTRAINT OLE_DLVR_EMAIL_TP1
PRIMARY KEY (DLVR_EMAIL_ID)
/


CREATE INDEX OLE_DLVR_EMAIL_TI1 
  ON OLE_DLVR_EMAIL_T 
  (ENTITY_EMAIL_ID)
/
CREATE INDEX OLE_DLVR_EMAIL_TI2 
  ON OLE_DLVR_EMAIL_T 
  (OLE_EMAIL_SRC)
/
CREATE INDEX OLE_DLVR_EMAIL_TI3 
  ON OLE_DLVR_EMAIL_T 
  (OLE_PTRN_ID)
/





-----------------------------------------------------------------------------
-- OLE_DLVR_BARCD_STAT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_BARCD_STAT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_BARCD_STAT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_BARCD_STAT_T
(
      DLVR_BARCD_STAT_ID NUMBER(8) default 0
        , DLVR_BARCD_TYP_CD VARCHAR2(40) NOT NULL
        , DLVR_BARCD_TYP_NM VARCHAR2(100) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) NOT NULL
        , DEL_IND VARCHAR2(1) NOT NULL
    

)
/

ALTER TABLE OLE_DLVR_BARCD_STAT_T
    ADD CONSTRAINT OLE_DLVR_BARCD_STAT_TP1
PRIMARY KEY (DLVR_BARCD_STAT_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_BATCH_JOB_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_BATCH_JOB_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_BATCH_JOB_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_BATCH_JOB_T
(
      JOB_ID VARCHAR2(40)
        , JOB_TRG_NM VARCHAR2(100) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) NOT NULL
        , PCK_UP_LOCN VARCHAR2(100)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , JOB_CRON_EXPRSN VARCHAR2(80)
    

)
/

ALTER TABLE OLE_DLVR_BATCH_JOB_T
    ADD CONSTRAINT OLE_DLVR_BATCH_JOB_TP1
PRIMARY KEY (JOB_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_BORR_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_BORR_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_BORR_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_BORR_TYP_T
(
      DLVR_BORR_TYP_ID VARCHAR2(40) default '0'
        , DLVR_BORR_TYP_CD VARCHAR2(40) NOT NULL
        , DLVR_BORR_TYP_DESC VARCHAR2(700) NOT NULL
        , DLVR_BORR_TYP_NM VARCHAR2(100) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) NOT NULL
    
    , CONSTRAINT DLVR_BORR_TYP_CD UNIQUE (DLVR_BORR_TYP_CD)

)
/

ALTER TABLE OLE_DLVR_BORR_TYP_T
    ADD CONSTRAINT OLE_DLVR_BORR_TYP_TP1
PRIMARY KEY (DLVR_BORR_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_CIRC_RECORD
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_CIRC_RECORD';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_CIRC_RECORD CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_CIRC_RECORD
(
      CIR_HIS_REC_ID VARCHAR2(40)
        , LOAN_TRAN_ID VARCHAR2(40) NOT NULL
        , CIR_POLICY_ID VARCHAR2(2000) NOT NULL
        , OLE_PTRN_ID VARCHAR2(40) NOT NULL
        , PTRN_TYP_ID VARCHAR2(40)
        , AFFILIATION_ID VARCHAR2(40)
        , DEPARTMENT_ID VARCHAR2(40)
        , OTHER_AFFILIATION VARCHAR2(40)
        , STATISTICAL_CATEGORY VARCHAR2(40)
        , ITM_ID VARCHAR2(40) NOT NULL
        , ITM_LOCN VARCHAR2(100)
        , HLDNG_LOCN VARCHAR2(100)
        , BIB_TIT VARCHAR2(4000) NOT NULL
        , BIB_AUTH VARCHAR2(500)
        , BIB_EDITION VARCHAR2(500)
        , BIB_PUB VARCHAR2(500)
        , BIB_PUB_DT DATE
        , BIB_ISBN VARCHAR2(50)
        , PROXY_PTRN_ID VARCHAR2(40)
        , DUE_DT_TIME TIMESTAMP
        , PAST_DUE_DT_TIME TIMESTAMP
        , CRTE_DT_TIME TIMESTAMP NOT NULL
        , MODI_DT_TIME TIMESTAMP
        , CIRC_LOC_ID VARCHAR2(40) NOT NULL
        , OPTR_CRTE_ID VARCHAR2(40)
        , OPTR_MODI_ID VARCHAR2(40)
        , MACH_ID VARCHAR2(100)
        , OVRR_OPTR_ID VARCHAR2(40)
        , NUM_RENEWALS VARCHAR2(3)
        , NUM_OVERDUE_NOTICES_SENT VARCHAR2(3)
        , OVERDUE_NOTICE_DATE DATE
        , OLE_RQST_ID VARCHAR2(40)
        , REPMNT_FEE_PTRN_BILL_ID VARCHAR2(40)
        , CHECK_IN_DT_TIME TIMESTAMP
        , CHECK_IN_DT_TIME_OVR_RD TIMESTAMP
        , CHECK_IN_OPTR_ID VARCHAR2(40)
        , CHECK_IN_MACH_ID VARCHAR2(100)
        , ITEM_TYP_ID VARCHAR2(100)
        , TEMP_ITEM_TYP_ID VARCHAR2(100)
        , ITEM_UUID VARCHAR2(100) NOT NULL
    

)
/

ALTER TABLE OLE_DLVR_CIRC_RECORD
    ADD CONSTRAINT OLE_DLVR_CIRC_RECORDP1
PRIMARY KEY (CIR_HIS_REC_ID)
/


CREATE INDEX fk_OLE_DLVR_CIRC_RECORD_FK1 
  ON OLE_DLVR_CIRC_RECORD 
  (OLE_PTRN_ID)
/
CREATE INDEX fk_OLE_DLVR_CIRC_RECORD_FK2 
  ON OLE_DLVR_CIRC_RECORD 
  (PROXY_PTRN_ID)
/
CREATE INDEX OLE_DLVR_CIRC_RECORD_TI1 
  ON OLE_DLVR_CIRC_RECORD 
  (LOAN_TRAN_ID)
/





-----------------------------------------------------------------------------
-- OLE_DLVR_FIXED_DUE_DATE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_FIXED_DUE_DATE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_FIXED_DUE_DATE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_FIXED_DUE_DATE_T
(
      DUE_DATE_ID VARCHAR2(40) default '0'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , CIRCULATION_POLICY_SET_ID VARCHAR2(1000) NOT NULL
    

)
/

ALTER TABLE OLE_DLVR_FIXED_DUE_DATE_T
    ADD CONSTRAINT OLE_DLVR_FIXED_DUE_DATE_TP1
PRIMARY KEY (DUE_DATE_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_FXD_DUE_DT_SPAN_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_FXD_DUE_DT_SPAN_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_FXD_DUE_DT_SPAN_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_FXD_DUE_DT_SPAN_T
(
      FIXED_DUE_DATE_TIME_SPAN_ID VARCHAR2(40) default '0'
        , DUE_DATE_ID VARCHAR2(40) NOT NULL
        , FIXED_DUE_DATE DATE NOT NULL
        , FROM_DATE DATE NOT NULL
        , TO_DATE DATE NOT NULL
        , TIME_SPAN VARCHAR2(100) NOT NULL
    

)
/

ALTER TABLE OLE_DLVR_FXD_DUE_DT_SPAN_T
    ADD CONSTRAINT OLE_DLVR_FXD_DUE_DT_SPAN_TP1
PRIMARY KEY (FIXED_DUE_DATE_TIME_SPAN_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_ITEM_AVAIL_STAT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_ITEM_AVAIL_STAT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_ITEM_AVAIL_STAT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_ITEM_AVAIL_STAT_T
(
      ITEM_AVAIL_STAT_ID VARCHAR2(40) default '0'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , ITEM_AVAIL_STAT_CD VARCHAR2(40) NOT NULL
        , ITEM_AVAIL_STAT_NM VARCHAR2(200) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
        , DATE_UPDATED TIMESTAMP
    
    , CONSTRAINT ITEM_AVAIL_STAT_CD UNIQUE (ITEM_AVAIL_STAT_CD)

)
/

ALTER TABLE OLE_DLVR_ITEM_AVAIL_STAT_T
    ADD CONSTRAINT OLE_DLVR_ITEM_AVAIL_STAT_TP1
PRIMARY KEY (ITEM_AVAIL_STAT_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_LOAN_STAT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_LOAN_STAT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_LOAN_STAT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_LOAN_STAT_T
(
      LOAN_STAT_ID VARCHAR2(36) default '0'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , LOAN_STAT_CD VARCHAR2(40) NOT NULL
        , LOAN_STAT_NM VARCHAR2(100) NOT NULL
    
    , CONSTRAINT LOAN_STAT_CD UNIQUE (LOAN_STAT_CD)

)
/

ALTER TABLE OLE_DLVR_LOAN_STAT_T
    ADD CONSTRAINT OLE_DLVR_LOAN_STAT_TP1
PRIMARY KEY (LOAN_STAT_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_LOAN_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_LOAN_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_LOAN_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_LOAN_T
(
      LOAN_TRAN_ID VARCHAR2(40)
        , CIR_POLICY_ID VARCHAR2(2000) NOT NULL
        , OLE_PTRN_ID VARCHAR2(40) NOT NULL
        , ITM_ID VARCHAR2(40)
        , OLE_PROXY_BORROWER_NM VARCHAR2(60)
        , PROXY_PTRN_ID VARCHAR2(40)
        , CURR_DUE_DT_TIME TIMESTAMP
        , PAST_DUE_DT_TIME TIMESTAMP
        , CRTE_DT_TIME TIMESTAMP NOT NULL
        , CIRC_LOC_ID VARCHAR2(40) NOT NULL
        , OPTR_CRTE_ID VARCHAR2(40) NOT NULL
        , MACH_ID VARCHAR2(100)
        , OVRR_OPTR_ID VARCHAR2(40)
        , NUM_RENEWALS VARCHAR2(3)
        , NUM_OVERDUE_NOTICES_SENT VARCHAR2(3)
        , N_OVERDUE_NOTICE VARCHAR2(3)
        , OVERDUE_NOTICE_DATE DATE
        , OLE_RQST_ID VARCHAR2(40)
        , REPMNT_FEE_PTRN_BILL_ID VARCHAR2(40)
        , CRTSY_NTCE VARCHAR2(1) default 'N'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , ITEM_UUID VARCHAR2(100) NOT NULL
        , NUM_CLAIMS_RTRN_NOTICES_SENT NUMBER(10,0)
        , CLAIMS_SEARCH_COUNT NUMBER(10,0)
        , LAST_CLAIMS_RTRN_SEARCH_DT TIMESTAMP
    
    , CONSTRAINT itm_id_const UNIQUE (ITM_ID)

)
/

ALTER TABLE OLE_DLVR_LOAN_T
    ADD CONSTRAINT OLE_DLVR_LOAN_TP1
PRIMARY KEY (LOAN_TRAN_ID)
/


CREATE INDEX new_fk_constraint2 
  ON OLE_DLVR_LOAN_T 
  (OLE_PTRN_ID)
/
CREATE INDEX loan_curr_due_date_index 
  ON OLE_DLVR_LOAN_T 
  (CURR_DUE_DT_TIME)
/
CREATE INDEX OLE_DLVR_LOAN_TI1 
  ON OLE_DLVR_LOAN_T 
  (ITEM_UUID)
/





-----------------------------------------------------------------------------
-- OLE_DLVR_LOAN_TERM_UNIT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_LOAN_TERM_UNIT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_LOAN_TERM_UNIT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_LOAN_TERM_UNIT_T
(
      LOAN_TERM_UNIT_ID VARCHAR2(36) default '0'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , LOAN_TERM_UNIT_CD VARCHAR2(40) NOT NULL
        , LOAN_TERM_UNIT_NM VARCHAR2(100) NOT NULL
    
    , CONSTRAINT LOAN_TERM_UNIT_CD UNIQUE (LOAN_TERM_UNIT_CD)

)
/

ALTER TABLE OLE_DLVR_LOAN_TERM_UNIT_T
    ADD CONSTRAINT OLE_DLVR_LOAN_TERM_UNIT_TP1
PRIMARY KEY (LOAN_TERM_UNIT_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_PTRN_BILL_FEE_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_PTRN_BILL_FEE_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_PTRN_BILL_FEE_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_PTRN_BILL_FEE_TYP_T
(
      ID VARCHAR2(40)
        , PTRN_BILL_ID VARCHAR2(40) NOT NULL
        , ITM_UUID VARCHAR2(100)
        , PAY_STATUS_ID VARCHAR2(40) NOT NULL
        , FEE_TYP_ID VARCHAR2(40) NOT NULL
        , FEE_TYP_AMT NUMBER(10,4) NOT NULL
        , ITM_BARCODE VARCHAR2(40)
        , BALANCE_AMT NUMBER(10,4) default 0.00
        , PTRN_BILL_DATE TIMESTAMP NOT NULL
        , PAY_FORGIVE_NOTE VARCHAR2(500)
        , PAY_ERROR_NOTE VARCHAR2(500)
        , PAY_CANCEL_NOTE VARCHAR2(500)
        , PAY_GENERAL_NOTE VARCHAR2(500)
        , DUE_DT_TIME TIMESTAMP
        , CHECK_OUT_DT_TIME TIMESTAMP
        , CHECK_IN_DT_TIME TIMESTAMP
        , CHECK_IN_DT_TIME_OVR_RD TIMESTAMP
        , RNWL_DT_TIME TIMESTAMP
        , OPERATOR_ID VARCHAR2(40)
        , ITM_TITLE VARCHAR2(600)
        , ITM_AUTHOR VARCHAR2(200)
        , ITM_TYPE VARCHAR2(100)
        , ITM_CALL_NUM VARCHAR2(100)
        , ITM_COPY_NUM VARCHAR2(20)
        , ITM_ENUM VARCHAR2(100)
        , ITM_CHRON VARCHAR2(100)
        , ITM_LOC VARCHAR2(600)
        , CRDT_ISSUED NUMBER(10,4) default 0.00
        , CRDT_REMAINING NUMBER(10,4) default 0.00
        , PAY_CREDIT_NOTE VARCHAR2(500)
        , PAY_TRANSFER_NOTE VARCHAR2(500)
        , PAY_REFUND_NOTE VARCHAR2(500)
        , PAY_CAN_CRDT_NOTE VARCHAR2(500)
        , MANUAL_BILL VARCHAR2(1) default 'N'
    

)
/

ALTER TABLE OLE_DLVR_PTRN_BILL_FEE_TYP_T
    ADD CONSTRAINT OLE_DLVR_PTRN_BILL_FEE_TYP_P1
PRIMARY KEY (ID)
/


CREATE INDEX FEE_TYP_BILL_ID 
  ON OLE_DLVR_PTRN_BILL_FEE_TYP_T 
  (PTRN_BILL_ID)
/





-----------------------------------------------------------------------------
-- OLE_DLVR_PTRN_BILL_PAY_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_PTRN_BILL_PAY_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_PTRN_BILL_PAY_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_PTRN_BILL_PAY_T
(
      ID VARCHAR2(40)
        , ITM_LINE_ID VARCHAR2(40) NOT NULL
        , BILL_PAY_AMT NUMBER(10,4) NOT NULL
        , CRTE_DT_TIME TIMESTAMP NOT NULL
        , OPTR_CRTE_ID VARCHAR2(40) NOT NULL
        , TRNS_NUMBER VARCHAR2(40)
        , TRNS_NOTE VARCHAR2(500)
        , TRNS_MODE VARCHAR2(40)
        , NOTE VARCHAR2(500)
    

)
/

ALTER TABLE OLE_DLVR_PTRN_BILL_PAY_T
    ADD CONSTRAINT OLE_DLVR_PTRN_BILL_PAY_TP1
PRIMARY KEY (ID)
/


CREATE INDEX BILL_PAY_ID 
  ON OLE_DLVR_PTRN_BILL_PAY_T 
  (ITM_LINE_ID)
/





-----------------------------------------------------------------------------
-- OLE_DLVR_PTRN_BILL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_PTRN_BILL_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_PTRN_BILL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_PTRN_BILL_T
(
      PTRN_BILL_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , OLE_PTRN_ID VARCHAR2(40)
        , PROXY_PTRN_ID VARCHAR2(40)
        , TOT_AMT_DUE NUMBER(10,4) NOT NULL
        , UNPAID_BAL NUMBER(10,4) default 0.00
        , PAY_METHOD_ID VARCHAR2(40)
        , PAY_AMT NUMBER(10,4) default 0.00
        , PAY_DT DATE
        , PAY_OPTR_ID VARCHAR2(40)
        , PAY_MACHINE_ID VARCHAR2(40)
        , CRTE_DT_TIME DATE
        , OPTR_CRTE_ID VARCHAR2(40) NOT NULL
        , OPTR_MACHINE_ID VARCHAR2(40)
        , PAY_NOTE VARCHAR2(500)
        , NOTE VARCHAR2(500)
        , BILL_REVIEWED VARCHAR2(1)
        , CRDT_ISSUED NUMBER(10,4) default 0.00
        , CRDT_REMAINING NUMBER(10,4) default 0.00
        , MANUAL_BILL VARCHAR2(1) default 'Y'
    

)
/

ALTER TABLE OLE_DLVR_PTRN_BILL_T
    ADD CONSTRAINT OLE_DLVR_PTRN_BILL_TP1
PRIMARY KEY (PTRN_BILL_ID)
/


CREATE INDEX OLE_DLVR_PTRN_BILL_TI1 
  ON OLE_DLVR_PTRN_BILL_T 
  (OLE_PTRN_ID)
/





-----------------------------------------------------------------------------
-- OLE_DLVR_PTRN_FEE_TYPE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_PTRN_FEE_TYPE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_PTRN_FEE_TYPE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_PTRN_FEE_TYPE_T
(
      FEE_TYP_ID VARCHAR2(40)
        , FEE_TYP_CD VARCHAR2(40) NOT NULL
        , FEE_TYP_NM VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_DLVR_PTRN_FEE_TYPE_T
    ADD CONSTRAINT OLE_DLVR_PTRN_FEE_TYPE_TP1
PRIMARY KEY (FEE_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_COPY_FORMAT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_COPY_FORMAT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_COPY_FORMAT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_COPY_FORMAT_T
(
      COPY_FORMAT_ID VARCHAR2(40)
        , COPY_FORMAT_CD VARCHAR2(40)
        , COPY_FORMAT_NM VARCHAR2(40)
        , COPY_FORMAT_ACTIVE VARCHAR2(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_COPY_FORMAT_T
    ADD CONSTRAINT OLE_COPY_FORMAT_TP1
PRIMARY KEY (COPY_FORMAT_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_RECENTLY_RETURNED_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_RECENTLY_RETURNED_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_RECENTLY_RETURNED_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_RECENTLY_RETURNED_T
(
      ID VARCHAR2(40)
        , CIRC_DESK_ID VARCHAR2(40) NOT NULL
        , ITEM_UUID VARCHAR2(100) NOT NULL
    
    , CONSTRAINT ITEM_UUID UNIQUE (ITEM_UUID)

)
/

ALTER TABLE OLE_DLVR_RECENTLY_RETURNED_T
    ADD CONSTRAINT OLE_DLVR_RECENTLY_RETURNED_P1
PRIMARY KEY (ID)
/


CREATE INDEX fk1_crcl_dsk 
  ON OLE_DLVR_RECENTLY_RETURNED_T 
  (CIRC_DESK_ID)
/





-----------------------------------------------------------------------------
-- OLE_DLVR_RQST_HSTRY_REC_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_RQST_HSTRY_REC_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_RQST_HSTRY_REC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_RQST_HSTRY_REC_T
(
      OLE_RQST_HSTRY_ID VARCHAR2(40)
        , OLE_RQST_ID VARCHAR2(80) NOT NULL
        , OLE_PTRN_ID VARCHAR2(40) NOT NULL
        , OLE_ITEM_ID VARCHAR2(80) NOT NULL
        , OLE_ITEM_BARCODE VARCHAR2(80) NOT NULL
        , OLE_LOAN_ID VARCHAR2(80)
        , OLE_LN_ITM_NUM VARCHAR2(80)
        , OLE_RQST_TYP_CD VARCHAR2(80) NOT NULL
        , OLE_PCK_LOC_CD VARCHAR2(80)
        , OLE_OPRT_ID VARCHAR2(80) NOT NULL
        , OLE_MACH_ID VARCHAR2(80)
        , ARCH_DT_TIME DATE NOT NULL
        , OLE_REQ_OUTCOME_STATUS VARCHAR2(80)
        , CRTE_DT_TIME TIMESTAMP NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_DLVR_RQST_HSTRY_REC_T
    ADD CONSTRAINT OLE_DLVR_RQST_HSTRY_REC_TP1
PRIMARY KEY (OLE_RQST_HSTRY_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_RQST_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_RQST_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_RQST_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_RQST_T
(
      OLE_RQST_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , PO_LN_ITM_NO VARCHAR2(100)
        , ITM_ID VARCHAR2(40)
        , OLE_PTRN_ID VARCHAR2(40)
        , OLE_PTRN_BARCD VARCHAR2(80)
        , PROXY_PTRN_ID VARCHAR2(40)
        , PROXY_PTRN_BARCD VARCHAR2(80)
        , OLE_RQST_TYP_ID VARCHAR2(40)
        , CNTNT_DESC VARCHAR2(800)
        , RQST_EXPIR_DT_TIME DATE
        , RCAL_NTC_SNT_DT DATE
        , ONHLD_NTC_SNT_DT DATE
        , CRTE_DT_TIME TIMESTAMP
        , MODI_DT_TIME DATE
        , CPY_FRMT VARCHAR2(40)
        , LOAN_TRAN_ID VARCHAR2(40)
        , PCKUP_LOC_ID VARCHAR2(40)
        , OPTR_CRTE_ID VARCHAR2(40)
        , OPTR_MODI_ID VARCHAR2(40)
        , CIRC_LOC_ID VARCHAR2(40)
        , MACH_ID VARCHAR2(80)
        , PTRN_Q_POS NUMBER(10,0)
        , ITEM_UUID VARCHAR2(100) NOT NULL
        , RQST_STAT VARCHAR2(10)
        , RQST_LVL VARCHAR2(40)
        , BIB_ID VARCHAR2(40)
        , ASR_FLAG VARCHAR2(1)
        , HOLD_EXP_DATE DATE
        , RQST_NOTE VARCHAR2(4000)
    
    , CONSTRAINT OLE_DLVR_RQST_T_ITEM_Q_INX_UK UNIQUE (OLE_PTRN_ID, PTRN_Q_POS, ITM_ID)

)
/

ALTER TABLE OLE_DLVR_RQST_T
    ADD CONSTRAINT OLE_DLVR_RQST_TP1
PRIMARY KEY (OLE_RQST_ID)
/


CREATE INDEX flk1 
  ON OLE_DLVR_RQST_T 
  (OLE_RQST_TYP_ID)
/
CREATE INDEX flk2 
  ON OLE_DLVR_RQST_T 
  (PCKUP_LOC_ID)
/
CREATE INDEX flk3 
  ON OLE_DLVR_RQST_T 
  (CIRC_LOC_ID)
/
CREATE INDEX flk4 
  ON OLE_DLVR_RQST_T 
  (OLE_PTRN_ID)
/
CREATE INDEX flk5 
  ON OLE_DLVR_RQST_T 
  (PROXY_PTRN_ID)
/
CREATE INDEX OLE_DLVR_RQST_TI1 
  ON OLE_DLVR_RQST_T 
  (LOAN_TRAN_ID)
/
CREATE INDEX OLE_DLVR_RQST_TI2 
  ON OLE_DLVR_RQST_T 
  (ITM_ID)
/
CREATE INDEX OLE_DLVR_RQST_TI3 
  ON OLE_DLVR_RQST_T 
  (ITEM_UUID)
/
CREATE INDEX OLE_DLVR_RQST_TI4 
  ON OLE_DLVR_RQST_T 
  (PTRN_Q_POS)
/
CREATE INDEX OLE_DLVR_RQST_TI5 
  ON OLE_DLVR_RQST_T 
  (BIB_ID)
/
CREATE INDEX OLE_DLVR_RQST_TI6 
  ON OLE_DLVR_RQST_T 
  (OLE_PTRN_BARCD)
/





-----------------------------------------------------------------------------
-- OLE_DLVR_RQST_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_RQST_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_RQST_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_RQST_TYP_T
(
      OLE_RQST_TYP_ID VARCHAR2(40)
        , OLE_RQST_TYP_CD VARCHAR2(80) NOT NULL
        , OLE_RQST_TYP_NM VARCHAR2(80) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , OLE_RQST_TYP_DESC VARCHAR2(80)
    

)
/

ALTER TABLE OLE_DLVR_RQST_TYP_T
    ADD CONSTRAINT OLE_DLVR_RQST_TYP_TP1
PRIMARY KEY (OLE_RQST_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_SRC_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_SRC_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_SRC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_SRC_T
(
      OLE_DLVR_SRC_ID VARCHAR2(40)
        , OLE_DLVR_SRC_CD VARCHAR2(40) NOT NULL
        , OLE_DLVR_SRC_DESC VARCHAR2(700) NOT NULL
        , OLE_DLVR_SRC_NM VARCHAR2(100) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) NOT NULL
    

)
/

ALTER TABLE OLE_DLVR_SRC_T
    ADD CONSTRAINT OLE_DLVR_SRC_TP1
PRIMARY KEY (OLE_DLVR_SRC_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_STAT_CAT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_STAT_CAT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_STAT_CAT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_STAT_CAT_T
(
      OLE_DLVR_STAT_CAT_ID VARCHAR2(40)
        , OLE_DLVR_STAT_CAT_CD VARCHAR2(40) NOT NULL
        , OLE_DLVR_STAT_CAT_DESC VARCHAR2(700) NOT NULL
        , OLE_DLVR_STAT_CAT_NM VARCHAR2(100) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) NOT NULL
    

)
/

ALTER TABLE OLE_DLVR_STAT_CAT_T
    ADD CONSTRAINT OLE_DLVR_STAT_CAT_TP1
PRIMARY KEY (OLE_DLVR_STAT_CAT_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_TEMP_CIRC_RECORD
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_TEMP_CIRC_RECORD';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_TEMP_CIRC_RECORD CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_TEMP_CIRC_RECORD
(
      TMP_CIR_HIS_REC_ID VARCHAR2(40)
        , OLE_PTRN_ID VARCHAR2(40) NOT NULL
        , ITM_ID VARCHAR2(40) NOT NULL
        , CIRC_LOC_ID VARCHAR2(40) NOT NULL
        , CHECK_IN_DT_TIME TIMESTAMP
        , DUE_DT_TIME TIMESTAMP
        , CHECK_OUT_DT_TIME TIMESTAMP
        , OLE_PROXY_PTRN_ID VARCHAR2(40)
        , ITEM_UUID VARCHAR2(100) NOT NULL
    

)
/

ALTER TABLE OLE_DLVR_TEMP_CIRC_RECORD
    ADD CONSTRAINT OLE_DLVR_TEMP_CIRC_RECORDP1
PRIMARY KEY (TMP_CIR_HIS_REC_ID)
/


CREATE INDEX fk1_ole_ptrn_t 
  ON OLE_DLVR_TEMP_CIRC_RECORD 
  (OLE_PTRN_ID)
/
CREATE INDEX OLE_DLVR_TEMP_CIRC_RECORD_TI1 
  ON OLE_DLVR_TEMP_CIRC_RECORD 
  (ITM_ID)
/





-----------------------------------------------------------------------------
-- OLE_ERES_NTE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_ERES_NTE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_ERES_NTE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_ERES_NTE_T
(
      E_RES_NTE_ID VARCHAR2(10)
        , E_RES_REC_ID VARCHAR2(10)
        , E_RES_NTE_TXT VARCHAR2(800)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_ERES_NTE_T
    ADD CONSTRAINT OLE_ERES_NTE_TP1
PRIMARY KEY (E_RES_NTE_ID)
/


CREATE INDEX OLE_ERES_NTE_FK 
  ON OLE_ERES_NTE_T 
  (E_RES_REC_ID)
/





-----------------------------------------------------------------------------
-- OLE_VRNT_TTL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_VRNT_TTL_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_VRNT_TTL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_VRNT_TTL_T
(
      E_RES_VRNT_TTL_ID VARCHAR2(10)
        , E_RES_REC_ID VARCHAR2(10)
        , E_RES_VRNT_TTL VARCHAR2(800)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_VRNT_TTL_T
    ADD CONSTRAINT OLE_VRNT_TTL_TP1
PRIMARY KEY (E_RES_VRNT_TTL_ID)
/


CREATE INDEX OLE_ERES_VRNT_TTL_FK 
  ON OLE_VRNT_TTL_T 
  (E_RES_REC_ID)
/





-----------------------------------------------------------------------------
-- OLE_ERES_REQ_SEL_COMM_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_ERES_REQ_SEL_COMM_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_ERES_REQ_SEL_COMM_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_ERES_REQ_SEL_COMM_T
(
      E_RES_REQ_SEL_COMM_ID VARCHAR2(10)
        , E_RES_REC_ID VARCHAR2(10)
        , REQ_SEL_COMM VARCHAR2(800)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_ERES_REQ_SEL_COMM_T
    ADD CONSTRAINT OLE_ERES_REQ_SEL_COMM_TP1
PRIMARY KEY (E_RES_REQ_SEL_COMM_ID)
/


CREATE INDEX OLE_ERES_REQ_SEL_COMM_FK 
  ON OLE_ERES_REQ_SEL_COMM_T 
  (E_RES_REC_ID)
/





-----------------------------------------------------------------------------
-- OLE_ERES_REQ_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_ERES_REQ_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_ERES_REQ_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_ERES_REQ_T
(
      E_RES_REQ_ID VARCHAR2(10)
        , E_RES_REC_ID VARCHAR2(10)
        , OLE_PTRN_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_ERES_REQ_T
    ADD CONSTRAINT OLE_ERES_REQ_TP1
PRIMARY KEY (E_RES_REQ_ID)
/


CREATE INDEX OLE_ERES_REQ_FK 
  ON OLE_ERES_REQ_T 
  (E_RES_REC_ID)
/
CREATE INDEX OLE_ERES_REQ_TI1 
  ON OLE_ERES_REQ_T 
  (OLE_PTRN_ID)
/





-----------------------------------------------------------------------------
-- OLE_ERES_SEL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_ERES_SEL_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_ERES_SEL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_ERES_SEL_T
(
      E_RES_SEL_ID VARCHAR2(10)
        , E_RES_REC_ID VARCHAR2(10)
        , PRNCPL_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_ERES_SEL_T
    ADD CONSTRAINT OLE_ERES_SEL_TP1
PRIMARY KEY (E_RES_SEL_ID)
/


CREATE INDEX OLE_ERES_SEL_FK 
  ON OLE_ERES_SEL_T 
  (E_RES_REC_ID)
/





-----------------------------------------------------------------------------
-- OLE_E_RES_REC_EVNT_LOG_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_E_RES_REC_EVNT_LOG_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_E_RES_REC_EVNT_LOG_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_E_RES_REC_EVNT_LOG_T
(
      E_RES_EVNT_LOG_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , LOG_TYPE_ID VARCHAR2(40)
        , EVNT_TYPE_ID VARCHAR2(40)
        , PROBLM_TYPE_ID VARCHAR2(40)
        , EVNT_NTE VARCHAR2(800)
        , EVNT_USR VARCHAR2(40)
        , EVNT_DT DATE
        , STATUS VARCHAR2(40)
        , RESOLVED_DATE DATE
        , RESOLUTION VARCHAR2(40)
        , E_RES_REC_ID VARCHAR2(10)
        , SAVE_FLAG VARCHAR2(1)
        , ATT_FILE_NAME1 VARCHAR2(40)
        , ATT_CONTENT1 BLOB
        , ATT_MIME_TYPE1 VARCHAR2(100)
        , ATT_FILE_NAME2 VARCHAR2(40)
        , ATT_CONTENT2 BLOB
        , ATT_MIME_TYPE2 VARCHAR2(100)
    

)
/

ALTER TABLE OLE_E_RES_REC_EVNT_LOG_T
    ADD CONSTRAINT OLE_E_RES_REC_EVNT_LOG_TP1
PRIMARY KEY (E_RES_EVNT_LOG_ID)
/


CREATE INDEX OLE_E_RES_REC_EVNT_LOG_FK 
  ON OLE_E_RES_REC_EVNT_LOG_T 
  (E_RES_REC_ID)
/





-----------------------------------------------------------------------------
-- OLE_E_RES_REC_INS_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_E_RES_REC_INS_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_E_RES_REC_INS_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_E_RES_REC_INS_T
(
      E_RES_INS_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , INST_ID VARCHAR2(50)
        , HOLD_ID VARCHAR2(50)
        , INST_FLAG VARCHAR2(10)
        , INST_NM VARCHAR2(500)
        , ISBN VARCHAR2(800)
        , INST_HOLD VARCHAR2(500)
        , PUB_DISP_NTE VARCHAR2(800)
        , PUBHR VARCHAR2(200)
        , PLTFRM_ID VARCHAR2(100)
        , STATUS VARCHAR2(40)
        , TIPP_STATUS VARCHAR2(40)
        , SUB_STATUS VARCHAR2(40)
        , AUTO_INST_REC VARCHAR2(100)
        , COV_SRT_DT DATE
        , COV_SRT_VOL VARCHAR2(40)
        , COV_SRT_ISS VARCHAR2(40)
        , COV_END_DT DATE
        , COV_END_VOL VARCHAR2(40)
        , COV_END_ISS VARCHAR2(40)
        , PRPTL_ACC_SRT_DT DATE
        , PRPTL_ACC_SRT_VOL VARCHAR2(40)
        , PRPTL_ACC_SRT_ISS VARCHAR2(40)
        , PRPTL_ACC_END_DT DATE
        , PRPTL_ACC_END_VOL VARCHAR2(40)
        , PRPTL_ACC_END_ISS VARCHAR2(40)
        , E_RES_REC_ID VARCHAR2(10)
        , BIB_ID VARCHAR2(40)
        , GOKB_ID NUMBER(10,0)
    

)
/

ALTER TABLE OLE_E_RES_REC_INS_T
    ADD CONSTRAINT OLE_E_RES_REC_INS_TP1
PRIMARY KEY (E_RES_INS_ID)
/


CREATE INDEX OLE_E_RES_REC_INS_FK 
  ON OLE_E_RES_REC_INS_T 
  (E_RES_REC_ID)
/
CREATE INDEX OLE_E_RES_REC_INS_ID 
  ON OLE_E_RES_REC_INS_T 
  (INST_ID)
/





-----------------------------------------------------------------------------
-- OLE_E_RES_REC_INV_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_E_RES_REC_INV_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_E_RES_REC_INV_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_E_RES_REC_INV_T
(
      E_RES_INV_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , E_RES_REC_ID VARCHAR2(10)
        , INV_ID NUMBER(9)
        , PO_ID NUMBER(9)
        , HOLDING_ID VARCHAR2(40)
        , INV_DT DATE
        , INV_NUM VARCHAR2(25)
        , VND_NM VARCHAR2(100)
        , INV_LI_AMT VARCHAR2(40)
        , FD_CD VARCHAR2(40)
        , INV_STAT VARCHAR2(40)
        , PD_DT DATE
        , CHK_CLR_DT DATE
        , CHK_NUM VARCHAR2(40)
    

)
/

ALTER TABLE OLE_E_RES_REC_INV_T
    ADD CONSTRAINT OLE_E_RES_REC_INV_TP1
PRIMARY KEY (E_RES_INV_ID)
/







-----------------------------------------------------------------------------
-- OLE_E_RES_REC_LIC_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_E_RES_REC_LIC_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_E_RES_REC_LIC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_E_RES_REC_LIC_T
(
      E_RES_LIC_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , LIC_RQST_DOC_NUM VARCHAR2(40)
        , LIC_RQST_ID VARCHAR2(40)
        , E_RES_REC_ID VARCHAR2(10)
    

)
/

ALTER TABLE OLE_E_RES_REC_LIC_T
    ADD CONSTRAINT OLE_E_RES_REC_LIC_TP1
PRIMARY KEY (E_RES_LIC_ID)
/


CREATE INDEX OLE_E_RES_REC_LIC_FK1 
  ON OLE_E_RES_REC_LIC_T 
  (E_RES_REC_ID)
/
CREATE INDEX OLE_E_RES_REC_LIC_FK2 
  ON OLE_E_RES_REC_LIC_T 
  (LIC_RQST_DOC_NUM)
/
CREATE INDEX OLE_E_RES_REC_LIC_FK3 
  ON OLE_E_RES_REC_LIC_T 
  (LIC_RQST_ID)
/





-----------------------------------------------------------------------------
-- OLE_LINK_E_RES_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_LINK_E_RES_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_LINK_E_RES_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_LINK_E_RES_T
(
      LINK_E_RES_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , E_RES_REC_ID VARCHAR2(10)
        , LINK_E_RES_REC_ID VARCHAR2(10)
        , RLTNSHP_TYPE VARCHAR2(10)
        , CHAIN_STRING VARCHAR2(100)
    

)
/

ALTER TABLE OLE_LINK_E_RES_T
    ADD CONSTRAINT OLE_LINK_E_RES_TP1
PRIMARY KEY (LINK_E_RES_ID)
/







-----------------------------------------------------------------------------
-- OLE_E_RES_REC_PO_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_E_RES_REC_PO_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_E_RES_REC_PO_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_E_RES_REC_PO_T
(
      E_RES_PO_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , E_RES_REC_ID VARCHAR2(10)
        , VER_NBR NUMBER(8)
        , PO_ID NUMBER(9)
        , INST_ID VARCHAR2(40)
        , TITLE VARCHAR2(100)
        , PD_AMT_CURR_FY NUMBER(19,4)
        , PD_AMT_PREV_FY NUMBER(19,4)
        , PD_AMT_TWO_YRS_PREV_FY NUMBER(19,4)
    

)
/

ALTER TABLE OLE_E_RES_REC_PO_T
    ADD CONSTRAINT OLE_E_RES_REC_PO_TP1
PRIMARY KEY (E_RES_PO_ID)
/


CREATE INDEX OLE_E_RES_REC_PO_FK 
  ON OLE_E_RES_REC_PO_T 
  (E_RES_REC_ID)
/





-----------------------------------------------------------------------------
-- OLE_E_RES_REC_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_E_RES_REC_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_E_RES_REC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_E_RES_REC_T
(
      E_RES_REC_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , TITLE VARCHAR2(200)
        , DESCR VARCHAR2(800)
        , PUBHR_ID VARCHAR2(40)
        , GOKB_ID NUMBER(10,0)
        , ISBN VARCHAR2(100)
        , STAT_ID VARCHAR2(40)
        , STAT_DT VARCHAR2(40)
        , PLTFRM_PROV VARCHAR2(250)
        , FD_CD VARCHAR2(10)
        , WRK_FLW_STAT VARCHAR2(40)
        , VNDR_NM VARCHAR2(45)
        , VNDR_ID VARCHAR2(40)
        , ESTD_PR VARCHAR2(40)
        , ORD_TYP_ID NUMBER(8)
        , PYMT_TYP_ID VARCHAR2(10)
        , PCKG_TYP_ID VARCHAR2(10)
        , PCKG_SCP_ID VARCHAR2(10)
        , BRKBLE VARCHAR2(1)
        , FD_TITLE_LST VARCHAR2(1)
        , NTE VARCHAR2(800)
        , PUB_DISP_NOTE VARCHAR2(800)
        , REQ_SEL_COMM VARCHAR2(800)
        , REQ_PRTY_ID VARCHAR2(40)
        , STAT_SRCH_CD_ID NUMBER(8)
        , TRL_ND VARCHAR2(1)
        , TRL_STAT VARCHAR2(40)
        , LIC_ND VARCHAR2(1)
        , LIC_REQ_STAT VARCHAR2(40)
        , ORD_PAY_STAT VARCHAR2(40)
        , ACT_STAT VARCHAR2(40)
        , DEF_COVR VARCHAR2(100)
        , DEF_PER_ACC VARCHAR2(100)
        , EINST_FLAG VARCHAR2(1)
        , E_RES_REC_DOC_NUM VARCHAR2(40)
        , E_RES_SBSCRP_STS VARCHAR2(40)
        , E_RES_INIT_SBSCRP_STRT_DT TIMESTAMP
        , E_RES_CUR_SBSCRP_STRT_DT TIMESTAMP
        , E_RES_CUR_SBSCRP_END_DT TIMESTAMP
        , E_RES_CAN_DCSN_DT TIMESTAMP
        , E_RES_CAN_EFCT_DT TIMESTAMP
        , E_RES_CAN_CAND VARCHAR2(1)
        , E_RES_RNW_ALERT VARCHAR2(1)
        , E_RES_CAN_RSN VARCHAR2(40)
        , E_RES_RNW_NTC_PRD VARCHAR2(4)
        , E_RES_RCP_ID VARCHAR2(40)
        , E_RES_WK_FL_IND VARCHAR2(40)
        , ACC_CONFMN_DT TIMESTAMP
        , ACC_USR_NM VARCHAR2(50)
        , ACC_PWD VARCHAR2(50)
        , PROXY_URL VARCHAR2(200)
        , PROXY_RES VARCHAR2(1)
        , MOB_ACC_ID VARCHAR2(40)
        , MOB_ACC_NOTE VARCHAR2(800)
        , BRANDING_CMPLT VARCHAR2(1)
        , PLTFRM_CONFIG_CMPLT VARCHAR2(1)
        , MARC_REC_SRC_TYPE_ID VARCHAR2(40)
        , LAST_REC_LOAD_DT TIMESTAMP
        , MARC_REC_SRC VARCHAR2(40)
        , MARC_REC_UPDT_FREQ_ID VARCHAR2(40)
        , MARC_REC_URL VARCHAR2(200)
        , MARC_REC_USR_NM VARCHAR2(50)
        , MARC_REC_PWD VARCHAR2(50)
        , MARC_REC_NOTE VARCHAR2(800)
        , GOKB_CONFIG VARCHAR2(800)
        , ACCES_ACTVTN_DOC_NMBR VARCHAR2(800)
        , GOKB_IMPORT_PROFILE VARCHAR2(10)
        , GOKB_PROFILE VARCHAR2(10)
        , GOKB_PACKAGE_STATUS VARCHAR2(40)
        , GOKB_LAST_UPDATED_DATE DATE
        , E_RES_RCP_ROLE_ID VARCHAR2(40)
        , E_RES_RCP_GROUP_ID VARCHAR2(40)
    

)
/

ALTER TABLE OLE_E_RES_REC_T
    ADD CONSTRAINT OLE_E_RES_REC_TP1
PRIMARY KEY (E_RES_REC_ID)
/


CREATE INDEX OLE_E_RES_REC_FK6 
  ON OLE_E_RES_REC_T 
  (PCKG_SCP_ID)
/
CREATE INDEX OLE_E_RES_REC_FK7 
  ON OLE_E_RES_REC_T 
  (REQ_PRTY_ID)
/
CREATE INDEX OLE_E_RES_REC_FK8 
  ON OLE_E_RES_REC_T 
  (PCKG_TYP_ID)
/
CREATE INDEX OLE_E_RES_REC_FK9 
  ON OLE_E_RES_REC_T 
  (STAT_ID)
/
CREATE INDEX OLE_E_RES_REC_FK10 
  ON OLE_E_RES_REC_T 
  (PYMT_TYP_ID)
/
CREATE INDEX OLE_E_RES_REC_FK11 
  ON OLE_E_RES_REC_T 
  (STAT_SRCH_CD_ID)
/





-----------------------------------------------------------------------------
-- OLE_E_RES_ACCESS_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_E_RES_ACCESS_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_E_RES_ACCESS_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_E_RES_ACCESS_T
(
      E_RES_ACCESS_ID VARCHAR2(10)
        , E_RES_REC_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , ACCESS_STATUS VARCHAR2(20)
        , ACC_CONFMN_DT TIMESTAMP
        , ACC_LOC_ID VARCHAR2(40)
        , ACC_TYP_ID VARCHAR2(40)
        , ACC_USR_NM VARCHAR2(50)
        , AUTHCAT_TYP_ID VARCHAR2(40)
        , ACC_PWD VARCHAR2(50)
        , NUM_SIMULT_USER VARCHAR2(25)
        , PROXY_URL VARCHAR2(200)
        , PROXY_RES VARCHAR2(1)
        , MOB_ACC_ID VARCHAR2(40)
        , MOB_ACC_NOTE VARCHAR2(800)
        , BRANDING_CMPLT VARCHAR2(1)
        , PLTFRM_CONFIG_CMPLT VARCHAR2(1)
        , TECH_REQ VARCHAR2(800)
        , MARC_REC_SRC_TYPE_ID VARCHAR2(40)
        , LAST_REC_LOAD_DT TIMESTAMP
        , MARC_REC_SRC VARCHAR2(40)
        , MARC_REC_UPDT_FREQ NUMBER(10,0)
        , MARC_REC_UPDT_REGULARITY VARCHAR2(200)
        , MARC_REC_URL VARCHAR2(200)
        , MARC_REC_USR_NM VARCHAR2(50)
        , MARC_REC_PWD VARCHAR2(50)
        , MARC_REC_NOTE VARCHAR2(800)
        , WRKFLW_ID VARCHAR2(800)
    

)
/

ALTER TABLE OLE_E_RES_ACCESS_T
    ADD CONSTRAINT OLE_E_RES_ACCESS_TP1
PRIMARY KEY (E_RES_ACCESS_ID)
/







-----------------------------------------------------------------------------
-- OLE_PUR_FRMT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PUR_FRMT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PUR_FRMT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PUR_FRMT_T
(
      OLE_FRMT_ID NUMBER(8)
        , OLE_FORMAT VARCHAR2(45)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ROW_ACTV_IND VARCHAR2(1)
    

)
/

ALTER TABLE OLE_PUR_FRMT_T
    ADD CONSTRAINT OLE_PUR_FRMT_TP1
PRIMARY KEY (OLE_FRMT_ID)
/







-----------------------------------------------------------------------------
-- OLE_FRMT_TYPS_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_FRMT_TYPS_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_FRMT_TYPS_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_FRMT_TYPS_T
(
      FRMT_TYPS_ID VARCHAR2(10)
        , E_RES_REC_ID VARCHAR2(10)
        , OLE_FRMT_ID NUMBER(8)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_FRMT_TYPS_T
    ADD CONSTRAINT OLE_FRMT_TYPS_TP1
PRIMARY KEY (FRMT_TYPS_ID)
/


CREATE INDEX OLE_FRMT_TYPS_FK 
  ON OLE_FRMT_TYPS_T 
  (OLE_FRMT_ID)
/





-----------------------------------------------------------------------------
-- OLE_E_RES_STAT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_E_RES_STAT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_E_RES_STAT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_E_RES_STAT_T
(
      E_RES_STAT_ID VARCHAR2(10)
        , E_RES_STAT_NM VARCHAR2(40)
        , E_RES_STAT_DESC VARCHAR2(100)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ROW_ACT_IND VARCHAR2(1)
    

)
/

ALTER TABLE OLE_E_RES_STAT_T
    ADD CONSTRAINT OLE_E_RES_STAT_TP1
PRIMARY KEY (E_RES_STAT_ID)
/







-----------------------------------------------------------------------------
-- OLE_E_RES_ACCTG_LN_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_E_RES_ACCTG_LN_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_E_RES_ACCTG_LN_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_E_RES_ACCTG_LN_T
(
      E_RES_ACCTG_LN_ID VARCHAR2(10)
        , E_RES_REC_ID VARCHAR2(10)
        , FIN_COA_CD VARCHAR2(2)
        , ACCOUNT_NBR VARCHAR2(7)
        , SUB_ACCT_NBR VARCHAR2(5)
        , FIN_OBJECT_CD VARCHAR2(4)
        , FIN_SUB_OBJ_CD VARCHAR2(3)
        , PROJECT_CD VARCHAR2(10)
        , ORG_REFERENCE_ID VARCHAR2(8)
        , ACLN_PCT NUMBER(35,20)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_E_RES_ACCTG_LN_T
    ADD CONSTRAINT OLE_E_RES_ACCTG_LN_TP1
PRIMARY KEY (E_RES_ACCTG_LN_ID)
/







-----------------------------------------------------------------------------
-- OLE_GLBLY_PRCT_FLD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_GLBLY_PRCT_FLD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_GLBLY_PRCT_FLD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_GLBLY_PRCT_FLD_T
(
      OLE_GLBLY_PRCT_FLD_ID VARCHAR2(40) default '0'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , TAG VARCHAR2(40)
        , FIRST_IND VARCHAR2(40)
        , SEC_IND VARCHAR2(40)
        , SUB_FLD VARCHAR2(40)
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
    

)
/

ALTER TABLE OLE_GLBLY_PRCT_FLD_T
    ADD CONSTRAINT OLE_GLBLY_PRCT_FLD_TP1
PRIMARY KEY (OLE_GLBLY_PRCT_FLD_ID)
/







-----------------------------------------------------------------------------
-- OLE_LIC_CHK_LST_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_LIC_CHK_LST_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_LIC_CHK_LST_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_LIC_CHK_LST_T
(
      LIC_CHK_LST_ID VARCHAR2(40) default '0'
        , OBJ_ID VARCHAR2(40) NOT NULL
        , VER_NBR VARCHAR2(40) NOT NULL
        , LIC_CHK_LST_AUTH VARCHAR2(40) NOT NULL
        , LIC_CHK_LST_FILE_NM VARCHAR2(250) NOT NULL
        , LIC_CHK_MIME_TYP VARCHAR2(255)
        , LIC_CHK_LST_MOD_DATE TIMESTAMP NOT NULL
        , LIC_CHK_LST_DESC VARCHAR2(800)
        , LIC_CHK_LST_NM VARCHAR2(40) NOT NULL
        , LIC_RM_ID VARCHAR2(40) NOT NULL
        , ROW_ACT_IND VARCHAR2(1)
    

)
/

ALTER TABLE OLE_LIC_CHK_LST_T
    ADD CONSTRAINT OLE_LIC_CHK_LST_TP1
PRIMARY KEY (LIC_CHK_LST_ID)
/







-----------------------------------------------------------------------------
-- OLE_LIC_DOC_LOCN_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_LIC_DOC_LOCN_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_LIC_DOC_LOCN_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_LIC_DOC_LOCN_T
(
      LIC_DOC_LOCN_ID VARCHAR2(40) default '0'
        , LIC_DOC_LOCN_NM VARCHAR2(40) NOT NULL
        , LIC_DOC_LOCN_DESC VARCHAR2(150)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_LIC_DOC_LOCN_T
    ADD CONSTRAINT OLE_LIC_DOC_LOCN_TP1
PRIMARY KEY (LIC_DOC_LOCN_ID)
/







-----------------------------------------------------------------------------
-- OLE_LIC_EVNT_LOG_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_LIC_EVNT_LOG_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_LIC_EVNT_LOG_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_LIC_EVNT_LOG_T
(
      LIC_EVNT_LOG_ID VARCHAR2(40)
        , LIC_EVNT_MSG_TYP VARCHAR2(50) NOT NULL
        , LIC_EVNT_LOG_DATE TIMESTAMP NOT NULL
        , LIC_EVNT_LOG_USER VARCHAR2(40) NOT NULL
        , LIC_EVNT_LOG_MSG VARCHAR2(2000) NOT NULL
        , LIC_REQS_ID VARCHAR2(40) NOT NULL
    

)
/

ALTER TABLE OLE_LIC_EVNT_LOG_T
    ADD CONSTRAINT OLE_LIC_EVNT_LOG_TP1
PRIMARY KEY (LIC_EVNT_LOG_ID)
/


CREATE INDEX LIC_EVNT_REQS_FK 
  ON OLE_LIC_EVNT_LOG_T 
  (LIC_REQS_ID)
/





-----------------------------------------------------------------------------
-- OLE_LIC_EVNT_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_LIC_EVNT_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_LIC_EVNT_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_LIC_EVNT_TYP_T
(
      LIC_EVNT_TYP_ID VARCHAR2(40)
        , LIC_EVNT_TYP_NM VARCHAR2(40) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_LIC_EVNT_TYP_T
    ADD CONSTRAINT OLE_LIC_EVNT_TYP_TP1
PRIMARY KEY (LIC_EVNT_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_LIC_REQS_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_LIC_REQS_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_LIC_REQS_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_LIC_REQS_T
(
      LIC_REQS_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , E_RES_REC_DOC_NUM VARCHAR2(40) NOT NULL
        , LIC_AGR_ID VARCHAR2(100)
        , OWNER VARCHAR2(40)
        , AGR_MTH_ID VARCHAR2(40)
        , LIC_STAT_CD VARCHAR2(40)
        , LIC_DOC_LOCN_ID VARCHAR2(40)
        , LIC_REQS_TYP_ID VARCHAR2(40)
        , LIC_WRK_FLW_TYP_CD VARCHAR2(40)
    

)
/

ALTER TABLE OLE_LIC_REQS_T
    ADD CONSTRAINT OLE_LIC_REQS_TP1
PRIMARY KEY (LIC_REQS_ID)
/


CREATE INDEX LIC_STAT_FK 
  ON OLE_LIC_REQS_T 
  (LIC_STAT_CD)
/
CREATE INDEX LIC_DOC_LOCN_FK 
  ON OLE_LIC_REQS_T 
  (LIC_DOC_LOCN_ID)
/
CREATE INDEX LIC_REQS_TYP_FK 
  ON OLE_LIC_REQS_T 
  (LIC_REQS_TYP_ID)
/
CREATE INDEX LIC_WRK_FLW_TYP_CD 
  ON OLE_LIC_REQS_T 
  (LIC_WRK_FLW_TYP_CD)
/
CREATE INDEX AGR_MTH_FK 
  ON OLE_LIC_REQS_T 
  (AGR_MTH_ID)
/





-----------------------------------------------------------------------------
-- OLE_LIC_REQS_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_LIC_REQS_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_LIC_REQS_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_LIC_REQS_TYP_T
(
      LIC_REQS_TYP_ID VARCHAR2(40)
        , LIC_REQS_TYP_NM VARCHAR2(40) NOT NULL
        , LIC_REQS_TYP_DESC VARCHAR2(150)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_LIC_REQS_TYP_T
    ADD CONSTRAINT OLE_LIC_REQS_TYP_TP1
PRIMARY KEY (LIC_REQS_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_LIC_RQST_ITM_TITL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_LIC_RQST_ITM_TITL_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_LIC_RQST_ITM_TITL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_LIC_RQST_ITM_TITL_T
(
      OLE_ITM_ID VARCHAR2(40)
        , OLE_LIC_RQST_ID VARCHAR2(40) NOT NULL
        , OLE_LIC_RQST_ITM_UUID VARCHAR2(40) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_LIC_RQST_ITM_TITL_T
    ADD CONSTRAINT OLE_LIC_RQST_ITM_TITL_TP1
PRIMARY KEY (OLE_ITM_ID)
/


CREATE INDEX LIC_REQS_ITM_FK 
  ON OLE_LIC_RQST_ITM_TITL_T 
  (OLE_LIC_RQST_ID)
/





-----------------------------------------------------------------------------
-- OLE_LIC_STAT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_LIC_STAT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_LIC_STAT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_LIC_STAT_T
(
      LIC_STAT_CD VARCHAR2(40)
        , LIC_STAT_NM VARCHAR2(100) NOT NULL
        , LIC_STAT_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_LIC_STAT_T
    ADD CONSTRAINT OLE_LIC_STAT_TP1
PRIMARY KEY (LIC_STAT_CD)
/







-----------------------------------------------------------------------------
-- OLE_LIC_WRK_FLW_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_LIC_WRK_FLW_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_LIC_WRK_FLW_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_LIC_WRK_FLW_TYP_T
(
      LIC_WRK_FLW_TYP_CD VARCHAR2(40) default '0'
        , LIC_WRK_FLW_TYP_NM VARCHAR2(100) NOT NULL
        , LIC_WRK_FLW_TYP_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_LIC_WRK_FLW_TYP_T
    ADD CONSTRAINT OLE_LIC_WRK_FLW_TYP_TP1
PRIMARY KEY (LIC_WRK_FLW_TYP_CD)
/







-----------------------------------------------------------------------------
-- OLE_LOCN_LEVEL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_LOCN_LEVEL_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_LOCN_LEVEL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_LOCN_LEVEL_T
(
      LEVEL_ID VARCHAR2(40) default '0'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , LEVEL_CD VARCHAR2(40) NOT NULL
        , LEVEL_NAME VARCHAR2(100) NOT NULL
        , PARENT_LEVEL VARCHAR2(40)
    
    , CONSTRAINT LEVEL_CD UNIQUE (LEVEL_CD)

)
/

ALTER TABLE OLE_LOCN_LEVEL_T
    ADD CONSTRAINT OLE_LOCN_LEVEL_TP1
PRIMARY KEY (LEVEL_ID)
/


CREATE INDEX new_fk_constraint 
  ON OLE_LOCN_LEVEL_T 
  (PARENT_LEVEL)
/





-----------------------------------------------------------------------------
-- OLE_LOCN_SUM_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_LOCN_SUM_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_LOCN_SUM_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_LOCN_SUM_T
(
      OLE_LOCN_SUM_ID VARCHAR2(40)
        , FILE_NM VARCHAR2(40)
        , LOCN_TOT_CNT NUMBER(8)
        , LOCN_CRE_CNT NUMBER(8)
        , LOCN_REJ_CNT NUMBER(8)
        , LOCN_FL_CNT NUMBER(8)
        , LOCN_UP_CNT NUMBER(8)
        , LOCN_PRI_NAME VARCHAR2(40)
        , LOCN_DATE TIMESTAMP
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_LOCN_SUM_T
    ADD CONSTRAINT OLE_LOCN_SUM_TP1
PRIMARY KEY (OLE_LOCN_SUM_ID)
/







-----------------------------------------------------------------------------
-- OLE_LOCN_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_LOCN_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_LOCN_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_LOCN_T
(
      LOCN_ID VARCHAR2(40) default '0'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , LOCN_CD VARCHAR2(40) NOT NULL
        , LOCN_NAME VARCHAR2(100) NOT NULL
        , LEVEL_ID VARCHAR2(40) NOT NULL
        , PARENT_LOCN_ID VARCHAR2(40)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
    
    , CONSTRAINT LOCN_CD UNIQUE (LOCN_CD)

)
/

ALTER TABLE OLE_LOCN_T
    ADD CONSTRAINT OLE_LOCN_TP1
PRIMARY KEY (LOCN_ID)
/


CREATE INDEX OLE_LOCN_FK1 
  ON OLE_LOCN_T 
  (PARENT_LOCN_ID)
/
CREATE INDEX OLE_LOCN_FK2 
  ON OLE_LOCN_T 
  (LEVEL_ID)
/





-----------------------------------------------------------------------------
-- OLE_MTRL_TYPS_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_MTRL_TYPS_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_MTRL_TYPS_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_MTRL_TYPS_T
(
      MTRL_TYPS_ID VARCHAR2(10)
        , E_RES_REC_ID VARCHAR2(10)
        , MTRL_TYP_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_MTRL_TYPS_T
    ADD CONSTRAINT OLE_MTRL_TYPS_TP1
PRIMARY KEY (MTRL_TYPS_ID)
/


CREATE INDEX OLE_MTRL_TYPS_FK 
  ON OLE_MTRL_TYPS_T 
  (MTRL_TYP_ID)
/





-----------------------------------------------------------------------------
-- OLE_MTRL_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_MTRL_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_MTRL_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_MTRL_TYP_T
(
      MTRL_TYP_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , MTRL_TYP_NM VARCHAR2(40)
        , MTRL_TYP_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
    

)
/

ALTER TABLE OLE_MTRL_TYP_T
    ADD CONSTRAINT OLE_MTRL_TYP_TP1
PRIMARY KEY (MTRL_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_OVERLAY_ACTN_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_OVERLAY_ACTN_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_OVERLAY_ACTN_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_OVERLAY_ACTN_T
(
      OLE_OVR_ACT_ID VARCHAR2(40)
        , OLE_PRFL_NM VARCHAR2(100) NOT NULL
        , OLE_OVR_ACT_DESC VARCHAR2(1000) NOT NULL
    

)
/

ALTER TABLE OLE_OVERLAY_ACTN_T
    ADD CONSTRAINT OLE_OVERLAY_ACTN_TP1
PRIMARY KEY (OLE_OVR_ACT_ID)
/







-----------------------------------------------------------------------------
-- OLE_OVERLAY_LOOKUP_ACTION_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_OVERLAY_LOOKUP_ACTION_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_OVERLAY_LOOKUP_ACTION_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_OVERLAY_LOOKUP_ACTION_T
(
      ID NUMBER(10,0)
        , AGENDA_NAME VARCHAR2(100)
        , ACTION_ID VARCHAR2(100)
        , DESCRIPTION VARCHAR2(100)
        , ACTION_TYPE VARCHAR2(100)
        , EVENT VARCHAR2(100)
        , MAPPING VARCHAR2(100)
        , INCOMING_DATA_FIELD VARCHAR2(100)
        , INCOMING_SUB_FIELD VARCHAR2(100)
        , EXISTING_FIELD VARCHAR2(100)
    

)
/

ALTER TABLE OLE_OVERLAY_LOOKUP_ACTION_T
    ADD CONSTRAINT OLE_OVERLAY_LOOKUP_ACTION_TP1
PRIMARY KEY (ID)
/







-----------------------------------------------------------------------------
-- OLE_OVERLAY_LOOKUP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_OVERLAY_LOOKUP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_OVERLAY_LOOKUP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_OVERLAY_LOOKUP_T
(
      ID NUMBER(10,0)
        , OVERLAY_LOOKUP_ACTION_ID NUMBER(10,0) NOT NULL
        , AGENDA_NAME VARCHAR2(100)
        , ACTION_ID VARCHAR2(10)
        , MAPPING_TABLE_NAME VARCHAR2(100)
        , FIELD VARCHAR2(100)
        , NEXT_ACTION VARCHAR2(100)
    

)
/

ALTER TABLE OLE_OVERLAY_LOOKUP_T
    ADD CONSTRAINT OLE_OVERLAY_LOOKUP_TP1
PRIMARY KEY (ID)
/


CREATE INDEX OVERLAY_LOOKUP_ACTION_FK 
  ON OLE_OVERLAY_LOOKUP_T 
  (OVERLAY_LOOKUP_ACTION_ID)
/





-----------------------------------------------------------------------------
-- OLE_OVERLAY_MAP_FLD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_OVERLAY_MAP_FLD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_OVERLAY_MAP_FLD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_OVERLAY_MAP_FLD_T
(
      OLE_MAP_FLD_ID VARCHAR2(40)
        , FILE_FMT VARCHAR2(40) NOT NULL
        , INCOMING_FLD VARCHAR2(10) NOT NULL
        , INCOMING_FLD_VAL VARCHAR2(50) NOT NULL
        , OLE_OVR_ACT_ID VARCHAR2(40) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(9) NOT NULL
    

)
/

ALTER TABLE OLE_OVERLAY_MAP_FLD_T
    ADD CONSTRAINT OLE_OVERLAY_MAP_FLD_TP1
PRIMARY KEY (OLE_MAP_FLD_ID)
/


CREATE INDEX OLE_OVERLAY_MAP_FLD_CNSTR 
  ON OLE_OVERLAY_MAP_FLD_T 
  (OLE_OVR_ACT_ID)
/





-----------------------------------------------------------------------------
-- OLE_OVERLAY_OPTION_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_OVERLAY_OPTION_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_OVERLAY_OPTION_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_OVERLAY_OPTION_T
(
      ID NUMBER(10,0)
        , AGENDA_NAME VARCHAR2(100)
        , OPTION_NAME VARCHAR2(100)
    

)
/

ALTER TABLE OLE_OVERLAY_OPTION_T
    ADD CONSTRAINT OLE_OVERLAY_OPTION_TP1
PRIMARY KEY (ID)
/







-----------------------------------------------------------------------------
-- OLE_OVERLAY_OUT_FLD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_OVERLAY_OUT_FLD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_OVERLAY_OUT_FLD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_OVERLAY_OUT_FLD_T
(
      OLE_OUT_FLD_ID VARCHAR2(40)
        , FLD_NAME VARCHAR2(100) NOT NULL
        , FLD_VAL VARCHAR2(100) NOT NULL
        , TARGET_FLD VARCHAR2(100) NOT NULL
        , OLE_OVR_ACT_ID VARCHAR2(40) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , LOOKUP_IND VARCHAR2(1) NOT NULL
    

)
/

ALTER TABLE OLE_OVERLAY_OUT_FLD_T
    ADD CONSTRAINT OLE_OVERLAY_OUT_FLD_TP1
PRIMARY KEY (OLE_OUT_FLD_ID)
/


CREATE INDEX OLE_OVERLAY_OUT_FLD_CNSTR 
  ON OLE_OVERLAY_OUT_FLD_T 
  (OLE_OVR_ACT_ID)
/





-----------------------------------------------------------------------------
-- OLE_PCKG_SCP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PCKG_SCP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PCKG_SCP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PCKG_SCP_T
(
      PCKG_SCP_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , PCKG_SCP_NM VARCHAR2(40)
        , PCKG_SCP_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
    

)
/

ALTER TABLE OLE_PCKG_SCP_T
    ADD CONSTRAINT OLE_PCKG_SCP_TP1
PRIMARY KEY (PCKG_SCP_ID)
/







-----------------------------------------------------------------------------
-- OLE_PCKG_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PCKG_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PCKG_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PCKG_TYP_T
(
      PCKG_TYP_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , PCKG_TYP_NM VARCHAR2(40)
        , PCKG_TYP_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
    

)
/

ALTER TABLE OLE_PCKG_TYP_T
    ADD CONSTRAINT OLE_PCKG_TYP_TP1
PRIMARY KEY (PCKG_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_PROFILE_ATTR_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PROFILE_ATTR_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PROFILE_ATTR_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PROFILE_ATTR_T
(
      ID NUMBER(10,0)
        , AGENDA_NAME VARCHAR2(100)
        , NAME VARCHAR2(100)
        , VALUE VARCHAR2(100)
    

)
/

ALTER TABLE OLE_PROFILE_ATTR_T
    ADD CONSTRAINT OLE_PROFILE_ATTR_TP1
PRIMARY KEY (ID)
/







-----------------------------------------------------------------------------
-- OLE_PROFILE_FACT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PROFILE_FACT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PROFILE_FACT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PROFILE_FACT_T
(
      ID NUMBER(10,0)
        , AGENDA_NAME VARCHAR2(100)
        , TERM_NAME VARCHAR2(100)
        , DOC_TYPE VARCHAR2(100)
        , INCOMING_FIELD VARCHAR2(100)
        , EXISTING_FIELD VARCHAR2(100)
    

)
/

ALTER TABLE OLE_PROFILE_FACT_T
    ADD CONSTRAINT OLE_PROFILE_FACT_TP1
PRIMARY KEY (ID)
/







-----------------------------------------------------------------------------
-- OLE_PROXY_PTRN_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PROXY_PTRN_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PROXY_PTRN_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PROXY_PTRN_T
(
      OLE_PROXY_PTRN_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , OLE_PTRN_ID VARCHAR2(40)
        , OLE_PROXY_PTRN_REF_ID VARCHAR2(40) NOT NULL
        , OLE_PROXY_PTRN_EXP_DT DATE
        , OLE_PROXY_PTRN_ACT_DT DATE
        , ACTV_IND VARCHAR2(1)
    

)
/

ALTER TABLE OLE_PROXY_PTRN_T
    ADD CONSTRAINT OLE_PROXY_PTRN_TP1
PRIMARY KEY (OLE_PROXY_PTRN_ID)
/


CREATE INDEX OLE_PROXY_PTRN_FK1 
  ON OLE_PROXY_PTRN_T 
  (OLE_PTRN_ID)
/





-----------------------------------------------------------------------------
-- OLE_PTRN_LOCAL_ID_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PTRN_LOCAL_ID_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PTRN_LOCAL_ID_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PTRN_LOCAL_ID_T
(
      OLE_PTRN_LOCAL_SEQ_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , OLE_PTRN_ID VARCHAR2(40)
        , LOCAL_ID VARCHAR2(40)
    

)
/

ALTER TABLE OLE_PTRN_LOCAL_ID_T
    ADD CONSTRAINT OLE_PTRN_LOCAL_ID_TP1
PRIMARY KEY (OLE_PTRN_LOCAL_SEQ_ID)
/


CREATE INDEX OLE_PTRN_LOCAL_FK 
  ON OLE_PTRN_LOCAL_ID_T 
  (OLE_PTRN_ID)
/





-----------------------------------------------------------------------------
-- OLE_PTRN_LOST_BAR_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PTRN_LOST_BAR_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PTRN_LOST_BAR_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PTRN_LOST_BAR_T
(
      OLE_PTRN_LOST_BAR_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , OLE_PTRN_ID VARCHAR2(40)
        , OLE_PTRN_LOST_BAR VARCHAR2(100)
        , OLE_PTRN_LOST_STATUS VARCHAR2(50)
        , OLE_PTRN_LOST_DESC VARCHAR2(300)
        , OPTR_CRTE_ID VARCHAR2(40)
        , OLE_PTRN_LOST_BAR_EFF_DT DATE
        , OLE_PTRN_LOST_BAR_ACTIVE VARCHAR2(1)
    

)
/

ALTER TABLE OLE_PTRN_LOST_BAR_T
    ADD CONSTRAINT OLE_PTRN_LOST_BAR_TP1
PRIMARY KEY (OLE_PTRN_LOST_BAR_ID)
/


CREATE INDEX OLE_PTRN_LOST_BAR_TI1 
  ON OLE_PTRN_LOST_BAR_T 
  (OLE_PTRN_ID)
/





-----------------------------------------------------------------------------
-- OLE_PTRN_NTE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PTRN_NTE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PTRN_NTE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PTRN_NTE_T
(
      OLE_PTRN_NTE_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , OLE_PTRN_ID VARCHAR2(40)
        , OPTR_ID VARCHAR2(40)
        , NTE_CRT_OR_UPDT_DATE TIMESTAMP
        , OLE_PTRN_NTE_TYP_ID VARCHAR2(40)
        , OLE_PTRN_NTE_TXT VARCHAR2(800)
        , ACTV_IND VARCHAR2(1) NOT NULL
    

)
/

ALTER TABLE OLE_PTRN_NTE_T
    ADD CONSTRAINT OLE_PTRN_NTE_TP1
PRIMARY KEY (OLE_PTRN_NTE_ID)
/


CREATE INDEX OLE_PTRN_NTE_FK1 
  ON OLE_PTRN_NTE_T 
  (OLE_PTRN_ID)
/
CREATE INDEX OLE_PTRN_NTE_FK2 
  ON OLE_PTRN_NTE_T 
  (OLE_PTRN_NTE_TYP_ID)
/





-----------------------------------------------------------------------------
-- OLE_PTRN_NTE_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PTRN_NTE_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PTRN_NTE_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PTRN_NTE_TYP_T
(
      OLE_PTRN_NTE_TYP_ID VARCHAR2(40)
        , OLE_PTRN_NTE_TYP_NM VARCHAR2(100)
        , OLE_PTRN_NTE_TYPE_CD VARCHAR2(8)
        , OBJ_ID VARCHAR2(36)
        , ACTV_IND VARCHAR2(1)
    

)
/

ALTER TABLE OLE_PTRN_NTE_TYP_T
    ADD CONSTRAINT OLE_PTRN_NTE_TYP_TP1
PRIMARY KEY (OLE_PTRN_NTE_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_PTRN_PAY_STA_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PTRN_PAY_STA_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PTRN_PAY_STA_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PTRN_PAY_STA_T
(
      PAY_STA_ID VARCHAR2(40) default '0'
        , PAY_STA_CODE VARCHAR2(40) NOT NULL
        , PAY_STA_NAME VARCHAR2(40) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_PTRN_PAY_STA_T
    ADD CONSTRAINT OLE_PTRN_PAY_STA_TP1
PRIMARY KEY (PAY_STA_ID)
/







-----------------------------------------------------------------------------
-- OLE_PTRN_SUM_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PTRN_SUM_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PTRN_SUM_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PTRN_SUM_T
(
      OLE_PTRN_SUM_ID VARCHAR2(40)
        , FILE_NM VARCHAR2(256)
        , PTRN_TOT_CNT NUMBER(8)
        , PTRN_CRE_CNT NUMBER(8)
        , PTRN_REJ_CNT NUMBER(8)
        , PTRN_FL_CNT NUMBER(8)
        , PTRN_UP_CNT NUMBER(8)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , PRNCPL_NM VARCHAR2(100)
        , CRT_DT TIMESTAMP
    

)
/

ALTER TABLE OLE_PTRN_SUM_T
    ADD CONSTRAINT OLE_PTRN_SUM_TP1
PRIMARY KEY (OLE_PTRN_SUM_ID)
/







-----------------------------------------------------------------------------
-- OLE_PTRN_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PTRN_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PTRN_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PTRN_T
(
      OLE_PTRN_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , BARCODE VARCHAR2(100)
        , BORR_TYP VARCHAR2(40) NOT NULL
        , ACTV_IND VARCHAR2(1)
        , GENERAL_BLOCK VARCHAR2(1)
        , PAGING_PRIVILEGE VARCHAR2(1)
        , COURTESY_NOTICE VARCHAR2(1)
        , DELIVERY_PRIVILEGE VARCHAR2(1)
        , CHECKOUT_RECEIPT_OPT_OUT VARCHAR2(1)
        , EXPIRATION_DATE DATE
        , ACTIVATION_DATE DATE
        , GENERAL_BLOCK_NT VARCHAR2(250)
        , INV_BARCODE_NUM VARCHAR2(20)
        , INV_BARCODE_NUM_EFF_DATE DATE
        , OLE_SRC VARCHAR2(40)
        , OLE_STAT_CAT VARCHAR2(40)
        , PHOTOGRAPH BLOB
    

)
/

ALTER TABLE OLE_PTRN_T
    ADD CONSTRAINT OLE_PTRN_TP1
PRIMARY KEY (OLE_PTRN_ID)
/


CREATE INDEX OLE_DLVR_BORR_FK2 
  ON OLE_PTRN_T 
  (BORR_TYP)
/
CREATE INDEX OLE_DLVR_SRC_FK3 
  ON OLE_PTRN_T 
  (OLE_SRC)
/
CREATE INDEX OLE_DLVR_STAT_CAT_FK4 
  ON OLE_PTRN_T 
  (OLE_STAT_CAT)
/
CREATE INDEX OLE_DLVR_PTRN_BRCD_FK6 
  ON OLE_PTRN_T 
  (BARCODE)
/





-----------------------------------------------------------------------------
-- OLE_PYMT_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_PYMT_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_PYMT_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_PYMT_TYP_T
(
      PYMT_TYP_ID VARCHAR2(10)
        , PYMT_TYP_NM VARCHAR2(40)
        , PYMT_TYP_DESC VARCHAR2(100)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ROW_ACT_IND VARCHAR2(1)
    

)
/

ALTER TABLE OLE_PYMT_TYP_T
    ADD CONSTRAINT OLE_PYMT_TYP_TP1
PRIMARY KEY (PYMT_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_REQ_PRTY_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_REQ_PRTY_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_REQ_PRTY_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_REQ_PRTY_T
(
      REQ_PRTY_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
        , REQ_PRTY_NM VARCHAR2(40)
        , REQ_PRTY_DESC VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
    

)
/

ALTER TABLE OLE_REQ_PRTY_T
    ADD CONSTRAINT OLE_REQ_PRTY_TP1
PRIMARY KEY (REQ_PRTY_ID)
/







-----------------------------------------------------------------------------
-- OLE_SER_RCV_HIS_REC
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_SER_RCV_HIS_REC';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_SER_RCV_HIS_REC CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_SER_RCV_HIS_REC
(
      SER_RCPT_HIS_REC_ID VARCHAR2(40)
        , SER_RCV_REC_ID VARCHAR2(40)
        , RCV_REC_TYP VARCHAR2(40)
        , CHRON_LVL_1 VARCHAR2(50)
        , CHRON_LVL_2 VARCHAR2(50)
        , CHRON_LVL_3 VARCHAR2(50)
        , CHRON_LVL_4 VARCHAR2(50)
        , CLAIM_COUNT VARCHAR2(50)
        , CLAIM_DATE DATE
        , CLAIM_NOTE VARCHAR2(100)
        , CLAIM_TYPE VARCHAR2(40)
        , CLAIM_RESP VARCHAR2(40)
        , ENUM_LVL_1 VARCHAR2(50)
        , ENUM_LVL_2 VARCHAR2(50)
        , ENUM_LVL_3 VARCHAR2(50)
        , ENUM_LVL_4 VARCHAR2(50)
        , ENUM_LVL_5 VARCHAR2(50)
        , ENUM_LVL_6 VARCHAR2(50)
        , PUB_DISPLAY VARCHAR2(1)
        , SER_RCPT_NOTE VARCHAR2(100)
        , OPTR_ID VARCHAR2(40)
        , MACH_ID VARCHAR2(100)
        , RCPT_STAT VARCHAR2(40)
        , RCPT_DATE DATE
        , PUB_RCPT VARCHAR2(40)
        , STAFF_ONLY_RCPT VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_SER_RCV_HIS_REC
    ADD CONSTRAINT OLE_SER_RCV_HIS_RECP1
PRIMARY KEY (SER_RCPT_HIS_REC_ID)
/


CREATE INDEX FK_SER_ID 
  ON OLE_SER_RCV_HIS_REC 
  (SER_RCV_REC_ID)
/





-----------------------------------------------------------------------------
-- OLE_SER_RCV_REC
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_SER_RCV_REC';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_SER_RCV_REC CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_SER_RCV_REC
(
      SER_RCV_REC_ID VARCHAR2(40)
        , FDOC_NBR VARCHAR2(14)
        , BIB_ID VARCHAR2(40)
        , RCV_REC_TYP VARCHAR2(40)
        , CLAIM VARCHAR2(1)
        , CLAIM_INTRVL_INFO VARCHAR2(500)
        , CREATE_ITEM VARCHAR2(1)
        , GEN_RCV_NOTE VARCHAR2(500)
        , INSTANCE_ID VARCHAR2(40)
        , PO_ID VARCHAR2(50)
        , PRINT_LBL VARCHAR2(1)
        , PUBLIC_DISPLAY VARCHAR2(1)
        , SER_RCPT_LOC VARCHAR2(40)
        , SER_RCV_REC VARCHAR2(40)
        , SUBSCR_STAT VARCHAR2(40)
        , TREATMENT_INSTR_NOTE VARCHAR2(500)
        , UNBOUND_LOC VARCHAR2(100)
        , URGENT_NOTE VARCHAR2(500)
        , VENDOR VARCHAR2(30)
        , CREATE_DATE DATE
        , OPTR_ID VARCHAR2(40)
        , MACH_ID VARCHAR2(100)
        , SUBSCR_STAT_DT DATE
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
        , ACTIVE VARCHAR2(1)
    

)
/

ALTER TABLE OLE_SER_RCV_REC
    ADD CONSTRAINT OLE_SER_RCV_RECP1
PRIMARY KEY (SER_RCV_REC_ID)
/







-----------------------------------------------------------------------------
-- OLE_SER_RCV_REC_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_SER_RCV_REC_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_SER_RCV_REC_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_SER_RCV_REC_TYP_T
(
      SER_RCV_REC_TYP_ID VARCHAR2(40)
        , SER_RCV_REC_ID VARCHAR2(40)
        , RCV_REC_TYP VARCHAR2(40)
        , ACTN_DATE DATE
        , ACTN_INTRVL VARCHAR2(40)
        , CHRON_CAPTN_LVL1 VARCHAR2(50)
        , CHRON_CAPTN_LVL2 VARCHAR2(50)
        , CHRON_CAPTN_LVL3 VARCHAR2(50)
        , CHRON_CAPTN_LVL4 VARCHAR2(50)
        , ENUM_CAPTN_LVL1 VARCHAR2(50)
        , ENUM_CAPTN_LVL2 VARCHAR2(50)
        , ENUM_CAPTN_LVL3 VARCHAR2(50)
        , ENUM_CAPTN_LVL4 VARCHAR2(50)
        , ENUM_CAPTN_LVL5 VARCHAR2(50)
        , ENUM_CAPTN_LVL6 VARCHAR2(50)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE OLE_SER_RCV_REC_TYP_T
    ADD CONSTRAINT OLE_SER_RCV_REC_TYP_TP1
PRIMARY KEY (SER_RCV_REC_TYP_ID)
/


CREATE INDEX FK_SER_TPY_ID 
  ON OLE_SER_RCV_REC_TYP_T 
  (SER_RCV_REC_ID)
/





-----------------------------------------------------------------------------
-- OLE_VNDR_ACC_INFO_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_VNDR_ACC_INFO_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_VNDR_ACC_INFO_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_VNDR_ACC_INFO_T
(
      OLE_VNDR_ACC_INFO_ID VARCHAR2(40) default '0'
        , VNDR_REF_NUMBER VARCHAR2(100) NOT NULL
        , ACC_NUM VARCHAR2(100) NOT NULL
        , OBJ_CD VARCHAR2(100) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_VNDR_ACC_INFO_T
    ADD CONSTRAINT OLE_VNDR_ACC_INFO_TP1
PRIMARY KEY (OLE_VNDR_ACC_INFO_ID)
/







-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_PRF_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_PRF_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_PRF_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_PRF_T
(
      BAT_PRCS_PRF_ID VARCHAR2(40)
        , BAT_PRCS_PRF_NM VARCHAR2(50)
        , BAT_PRCS_PRF_DESC VARCHAR2(1000)
        , OLE_BAT_PRCS_TYP VARCHAR2(100)
        , KRMS_PRFL_NM VARCHAR2(100)
        , BAT_PRCS_PRF_DT_TO_IMPRT VARCHAR2(50)
        , BAT_PRCS_PRF_DT_TO_EXPRT VARCHAR2(50)
        , BAT_PRCS_PRF_EXPRT_SCP VARCHAR2(50)
        , BAT_PRCS_PRF_REQ_FR_TITL VARCHAR2(50)
        , BAT_PRCS_BIB_OVRL_ADD VARCHAR2(50)
        , BAT_PRCS_BIB_NO_MTCH VARCHAR2(50)
        , BAT_PRCS_INST_OVRL_ADD VARCHAR2(50)
        , BAT_PRCS_INST_NO_MTCH VARCHAR2(50)
        , BAT_PRCS_NEW_BIB_STS VARCHAR2(50)
        , BAT_PRCS_EXST_BIB_STS VARCHAR2(50)
        , BAT_PRCS_NOCHNG_SET VARCHAR2(50)
        , BAT_PRCS_OVERLAY_NOCHNG_SET VARCHAR2(50)
        , IS_OVERLAY_BIB_STF_ONLY VARCHAR2(1)
        , IS_BIB_STF_ONLY VARCHAR2(1)
        , IS_INST_STF_ONLY VARCHAR2(1)
        , IS_ITM_STF_ONLY VARCHAR2(1)
        , BAT_PRCS_DNT_CHNG_001 VARCHAR2(50)
        , PRPND_003_TO_035 VARCHAR2(20)
        , PRPND_VAL_TO_035 VARCHAR2(20)
        , BAT_PRCS_VAL_TO_PRPND VARCHAR2(50)
        , BAT_PRCS_RMV_IND VARCHAR2(20)
        , BAT_PRCS_VAL_TO_RMV VARCHAR2(20)
        , BAT_PRCS_MARC_ONLY VARCHAR2(1)
        , BAT_PRCS_BIB_IMP_PRF VARCHAR2(100)
        , BAT_PRCS_MATCH_PROFILE CLOB
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_PRCS_PRF_T
    ADD CONSTRAINT OLE_BAT_PRCS_PRF_TP1
PRIMARY KEY (BAT_PRCS_PRF_ID)
/







-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_PRF_MTCH_POINT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_PRF_MTCH_POINT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_PRF_MTCH_POINT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_PRF_MTCH_POINT_T
(
      MATCH_POINT_ID VARCHAR2(40)
        , CAS_MATCH_POINT VARCHAR2(40)
        , MATCH_POINT VARCHAR2(100)
        , MATCH_POINT_TYPE VARCHAR2(40)
        , BAT_PRCS_PRF_ID VARCHAR2(40) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_PRCS_PRF_MTCH_POINT_T
    ADD CONSTRAINT OLE_BAT_PRCS_PRF_MTCH_POINTP1
PRIMARY KEY (MATCH_POINT_ID)
/


CREATE INDEX BAT_PRF_MTCH_PNT_I 
  ON OLE_BAT_PRCS_PRF_MTCH_POINT_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_PRFLE_CNST_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_PRFLE_CNST_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_PRFLE_CNST_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_PRFLE_CNST_T
(
      OLE_USR_DEF_VAL_ID VARCHAR2(40)
        , ATT_NM VARCHAR2(100)
        , ATT_VAL VARCHAR2(500)
        , DATA_TYPE VARCHAR2(20)
        , BAT_PRCS_PRF_ID VARCHAR2(40) NOT NULL
        , DEF_VAL VARCHAR2(20)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_PRCS_PRFLE_CNST_T
    ADD CONSTRAINT OLE_BAT_PRCS_PRFLE_CNST_TP1
PRIMARY KEY (OLE_USR_DEF_VAL_ID)
/


CREATE INDEX BAT_PRCS_PRFL_CNST_FK_KEY 
  ON OLE_BAT_PRCS_PRFLE_CNST_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_TYP_T
(
      OLE_BAT_PRCS_TYP_ID VARCHAR2(40)
        , OLE_BAT_PRCS_TYP_CODE VARCHAR2(45) NOT NULL
        , OLE_BAT_PRCS_TYP_NM VARCHAR2(100) NOT NULL
        , ACTIVE_IND VARCHAR2(1) NOT NULL
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
    

)
/

ALTER TABLE OLE_BAT_PRCS_TYP_T
    ADD CONSTRAINT OLE_BAT_PRCS_TYP_TP1
PRIMARY KEY (OLE_BAT_PRCS_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_DT_MAP_OPT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_DT_MAP_OPT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_DT_MAP_OPT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_DT_MAP_OPT_T
(
      BATCH_PRCS_DT_MAP_OPT_ID VARCHAR2(40)
        , BAT_PRCS_DT_MAP_DATA_TYP VARCHAR2(10)
        , DATA_TYP_DEST_FLD VARCHAR2(15)
        , SRC_FLD VARCHAR2(70)
        , SRC_VAL VARCHAR2(70)
        , DEST_FLD VARCHAR2(70)
        , DEST_VAL VARCHAR2(70)
        , GOKB_FIELD VARCHAR2(100)
        , PRIORITY NUMBER(10,0) default 1
        , IS_LOOKUP VARCHAR2(1)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , OLE_BAT_PRCS_DT_MAP_ID VARCHAR2(40) NOT NULL
    

)
/

ALTER TABLE OLE_BAT_PRCS_DT_MAP_OPT_T
    ADD CONSTRAINT OLE_BAT_PRCS_DT_MAP_OPT_TP1
PRIMARY KEY (BATCH_PRCS_DT_MAP_OPT_ID)
/


CREATE INDEX MAP_FK_CONSTRAINT 
  ON OLE_BAT_PRCS_DT_MAP_OPT_T 
  (OLE_BAT_PRCS_DT_MAP_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_DT_MAP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_DT_MAP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_DT_MAP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_DT_MAP_T
(
      OLE_BAT_PRCS_DT_MAP_ID VARCHAR2(40)
        , OLE_BAT_PRCS_DT_MAP_OPTN_NUM NUMBER(10,0)
        , BAT_PRCS_PRF_ID VARCHAR2(40) NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_PRCS_DT_MAP_T
    ADD CONSTRAINT OLE_BAT_PRCS_DT_MAP_TP1
PRIMARY KEY (OLE_BAT_PRCS_DT_MAP_ID)
/


CREATE INDEX BAT_FK_CONSTRAINT 
  ON OLE_BAT_PRCS_DT_MAP_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_FILTER_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_FILTER_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_FILTER_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_FILTER_T
(
      OLE_BAT_PRCS_FILTER_ID VARCHAR2(40)
        , OLE_BAT_PRCS_FILTER_NM VARCHAR2(500)
        , OLE_BAT_PRCS_FILTER_VAL VARCHAR2(500)
        , OLE_BAT_PRCS_FILTER_RANGE_FROM VARCHAR2(20)
        , OLE_BAT_PRCS_FILTER_RANGE_TO VARCHAR2(20)
        , BAT_PRCS_PRF_ID VARCHAR2(40)
        , BAT_PRCS_DATA_TYP VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_PRCS_FILTER_T
    ADD CONSTRAINT OLE_BAT_PRCS_FILTER_TP1
PRIMARY KEY (OLE_BAT_PRCS_FILTER_ID)
/


CREATE INDEX BAT_PRCS_FILTER_FK_KEY 
  ON OLE_BAT_PRCS_FILTER_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_FLE_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_FLE_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_FLE_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_FLE_TYP_T
(
      FLE_TYP_ID VARCHAR2(40)
        , FLE_TYP_NM VARCHAR2(75)
        , FLE_TYP_DESC VARCHAR2(100)
        , ACT_IND VARCHAR2(1)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR VARCHAR2(36)
    

)
/

ALTER TABLE OLE_BAT_PRCS_FLE_TYP_T
    ADD CONSTRAINT OLE_BAT_PRCS_FLE_TYP_TP1
PRIMARY KEY (FLE_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_MNTN_FIELD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_MNTN_FIELD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_MNTN_FIELD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_MNTN_FIELD_T
(
      OLE_BAT_FIELD_ID VARCHAR2(40)
        , OLE_BAT_FIELD_DISPLY_NM VARCHAR2(50)
        , OLE_BAT_FIELD_NM VARCHAR2(50)
        , OLE_BAT_FIELD_TYP VARCHAR2(50)
        , ACT_IND VARCHAR2(1)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR VARCHAR2(36)
    

)
/

ALTER TABLE OLE_BAT_PRCS_MNTN_FIELD_T
    ADD CONSTRAINT OLE_BAT_PRCS_MNTN_FIELD_TP1
PRIMARY KEY (OLE_BAT_FIELD_ID)
/







-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_PRCT_FLD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_PRCT_FLD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_PRCT_FLD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_PRCT_FLD_T
(
      USR_DEF_PRCT_FLD_ID VARCHAR2(40)
        , DATA_TYP VARCHAR2(10)
        , USR_DEF_PRCT_FLD_TAG VARCHAR2(10)
        , USR_DEF_PRCT_FLD_IND1 VARCHAR2(5)
        , USR_DEF_PRCT_FLD_IND2 VARCHAR2(5)
        , USR_DEF_PRCT_FLD_SUBFLD VARCHAR2(3)
        , USR_DEF_PRCT_FLD_SBFLD_CNTS VARCHAR2(500)
        , BAT_PRCS_PRF_ID VARCHAR2(40)
        , IGN_VAL VARCHAR2(1) default 'Y'
        , OLE_GLBLY_PRCT_FLD_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_PRCS_PRCT_FLD_T
    ADD CONSTRAINT OLE_BAT_PRCS_PRCT_FLD_TP1
PRIMARY KEY (USR_DEF_PRCT_FLD_ID)
/


CREATE INDEX BAT_PRCS_USR_PRCT_FK_KEY 
  ON OLE_BAT_PRCS_PRCT_FLD_T 
  (BAT_PRCS_PRF_ID)
/
CREATE INDEX BAT_PRCS_GLBLY_PRCT_FK_KEY 
  ON OLE_BAT_PRCS_PRCT_FLD_T 
  (OLE_GLBLY_PRCT_FLD_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_PRF_BIB_DT_MAP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRF_BIB_DT_MAP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRF_BIB_DT_MAP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRF_BIB_DT_MAP_T
(
      OLE_BAT_PRF_BIB_DT_MAP_ID VARCHAR2(40)
        , TAG VARCHAR2(10)
        , GOKB_FIELD VARCHAR2(100)
        , BAT_PRCS_PRF_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_PRF_BIB_DT_MAP_T
    ADD CONSTRAINT OLE_BAT_PRF_BIB_DT_MAP_TP1
PRIMARY KEY (OLE_BAT_PRF_BIB_DT_MAP_ID)
/


CREATE INDEX OLE_BAT_PRCS_BIB_DT_MAP_I 
  ON OLE_BAT_PRF_BIB_DT_MAP_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_PRF_BIB_DT_MAP_OVER_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRF_BIB_DT_MAP_OVER_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRF_BIB_DT_MAP_OVER_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRF_BIB_DT_MAP_OVER_T
(
      OLE_BAT_PRF_BIB_DT_MAP_OVER_ID VARCHAR2(40)
        , TAG VARCHAR2(10)
        , ADD_OR_REPLACE VARCHAR2(10)
        , BAT_PRCS_PRF_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_PRF_BIB_DT_MAP_OVER_T
    ADD CONSTRAINT OLE_BAT_PRF_BIB_DT_MAP_OVERP1
PRIMARY KEY (OLE_BAT_PRF_BIB_DT_MAP_OVER_ID)
/


CREATE INDEX OLE_BAT_PRCS_BIB_DT_MAP_OVER_I 
  ON OLE_BAT_PRF_BIB_DT_MAP_OVER_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_T
(
      BAT_PRCS_ID VARCHAR2(40)
        , FDOC_NBR VARCHAR2(14)
        , USER_NAME VARCHAR2(40)
        , BAT_PRCS_NM VARCHAR2(200)
        , BAT_PRCS_PRF_NM VARCHAR2(40)
        , BAT_PRCS_PRF_TYP VARCHAR2(40)
        , BAT_PRCS_KRMS_PRF VARCHAR2(40)
        , BAT_PRCS_PRF_ID VARCHAR2(40)
        , SRC_FLE_PATH VARCHAR2(500)
        , SRC_DIR_PATH VARCHAR2(500)
        , SRC_FLE_MSK VARCHAR2(200)
        , FLE_FRMT_ID VARCHAR2(40)
        , OUTPUT_FRMT VARCHAR2(40)
        , DEST_DIR_PATH VARCHAR2(500)
        , CHUNK_SIZE NUMBER(10,0)
        , MAX_REC_PER_FILE NUMBER(10,0)
        , MAX_NUMBER_OF_THREADS NUMBER(10,0)
        , EMAIL_IDS VARCHAR2(400)
        , CRON_OR_SCHEDULE VARCHAR2(50)
        , ONE_TIME_OR_RECUR VARCHAR2(50)
        , SCHEDULE_TYPE VARCHAR2(50)
        , UPLOAD_FLE_NM VARCHAR2(60)
        , BAT_PRCS_MARC_ONLY VARCHAR2(1)
        , BAT_PRCS_UNMATCHED_PTRN VARCHAR2(1)
    

)
/

ALTER TABLE OLE_BAT_PRCS_T
    ADD CONSTRAINT OLE_BAT_PRCS_TP1
PRIMARY KEY (BAT_PRCS_ID)
/


CREATE INDEX BAT_PRCS_FK_CONST 
  ON OLE_BAT_PRCS_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_JOB_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_JOB_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_JOB_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_JOB_T
(
      JOB_ID NUMBER(20,0)
        , JOB_NAME VARCHAR2(100)
        , BAT_PRFLE_NM VARCHAR2(100)
        , USR_NM VARCHAR2(100)
        , UPLOAD_FLE_NM VARCHAR2(200)
        , NUM_REC VARCHAR2(20)
        , NUM_REC_PRCS VARCHAR2(20)
        , START_TIME TIMESTAMP
        , END_TIME TIMESTAMP
        , TIME_SPENT VARCHAR2(40)
        , PER_COMPLTED VARCHAR2(20)
        , CRTE_TIME DATE
        , STATUS VARCHAR2(20)
        , STATUS_DESC VARCHAR2(400)
        , BAT_PRCS_ID VARCHAR2(40)
        , BAT_PRCS_TYPE VARCHAR2(40)
        , NUM_SUC_REC VARCHAR2(40)
        , NUM_FAL_REC VARCHAR2(40)
        , HSTRY_SUC_CNT VARCHAR2(40)
        , HSTRY_FAL_CNT VARCHAR2(40)
        , TYPE_SUC_CNT VARCHAR2(40)
        , TYPE_FAL_CNT VARCHAR2(40)
        , CREATE_BIB_CNT NUMBER(20,0)
        , UPDATE_BIB_CNT NUMBER(20,0)
        , CREATE_HLD_CNT NUMBER(20,0)
        , ORD_RCD_SUCC_CNT NUMBER(20,0)
        , ORD_RCD_FAIL_CNT NUMBER(20,0)
        , NUM_EINSTANCES_ADDED NUMBER(20,0)
        , NUM_EINSTANCES_DELETED NUMBER(20,0)
        , NUM_EINSTANCES_WITH_NO_LINK NUMBER(20,0)
        , NUM_BIBS_GT1_EINSTANCES NUMBER(20,0)
        , NUM_EHOL_WITH_NO_PLAT NUMBER(20,0)
        , NUM_EHOL_WITH_NO_ERES NUMBER(20,0)
    

)
/

ALTER TABLE OLE_BAT_PRCS_JOB_T
    ADD CONSTRAINT OLE_BAT_PRCS_JOB_TP1
PRIMARY KEY (JOB_ID)
/


CREATE INDEX PRCS_FK_CONSTRAINT 
  ON OLE_BAT_PRCS_JOB_T 
  (BAT_PRCS_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_GLBLY_PRCT_FLD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_GLBLY_PRCT_FLD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_GLBLY_PRCT_FLD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_GLBLY_PRCT_FLD_T
(
      GLBY_PRCT_FLD_ID VARCHAR2(40)
        , BAT_GLBY_PRCT_FLD_ID VARCHAR2(10)
        , BAT_GLBY_PRCT_FLD_TAG VARCHAR2(5)
        , BAT_GLBY_PRCT_FLD_IND1 VARCHAR2(3)
        , BAT_GLBY_PRCT_FLD_IND2 VARCHAR2(3)
        , BAT_GLBY_PRCT_FLD_SUBFLD VARCHAR2(3)
        , BAT_GLBY_PRCT_FLD_IGN_VAL VARCHAR2(1)
        , BAT_PRCS_PRF_ID VARCHAR2(40)
    

)
/

ALTER TABLE OLE_BAT_GLBLY_PRCT_FLD_T
    ADD CONSTRAINT OLE_BAT_GLBLY_PRCT_FLD_TP1
PRIMARY KEY (GLBY_PRCT_FLD_ID)
/


CREATE INDEX OLE_BAT_PRCS_GLBLY_PRCT_FK 
  ON OLE_BAT_GLBLY_PRCT_FLD_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_SCHDULE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_SCHDULE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_SCHDULE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_SCHDULE_T
(
      OLE_BAT_PRCS_SCHDULE_ID VARCHAR2(40)
        , BAT_PRCS_ID VARCHAR2(40) NOT NULL
        , USR_NM VARCHAR2(100)
        , UPLOAD_FLE_NM VARCHAR2(200)
        , CRTE_TIME DATE
        , OLE_BAT_PRCS_BAT_TYP VARCHAR2(40)
        , OLE_BAT_PRCS_ONE_OR_RECUR VARCHAR2(40)
        , OLE_BAT_PRCS_ONE_DATE DATE
        , OLE_BAT_PRCS_ONE_TIME VARCHAR2(40)
        , OLE_BAT_PRCS_SCHDULE_TYPE VARCHAR2(40)
        , OLE_BAT_PRCS_START_TIME VARCHAR2(40)
        , OLE_BAT_PRCS_WEEK_DAYS VARCHAR2(40)
        , OLE_BAT_PRCS_DAY_NUM VARCHAR2(40)
        , OLE_BAT_PRCS_MONTH_NUM VARCHAR2(40)
        , OLE_BAT_PRCS_CRON_EXP VARCHAR2(100)
    

)
/

ALTER TABLE OLE_BAT_PRCS_SCHDULE_T
    ADD CONSTRAINT OLE_BAT_PRCS_SCHDULE_TP1
PRIMARY KEY (OLE_BAT_PRCS_SCHDULE_ID)
/


CREATE INDEX FK_BAT_PRCS_ID 
  ON OLE_BAT_PRCS_SCHDULE_T 
  (BAT_PRCS_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_PRF_DEL_FLD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRF_DEL_FLD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRF_DEL_FLD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRF_DEL_FLD_T
(
      BAT_PRF_DEL_ID VARCHAR2(40)
        , BAT_PRCS_PRF_ID VARCHAR2(40)
        , BAT_PRF_DEL_TAG VARCHAR2(5)
        , BAT_PRF_DEL_IND1 VARCHAR2(3)
        , BAT_PRF_DEL_IND2 VARCHAR2(3)
        , BAT_PRF_DEL_SUBFLD VARCHAR2(3)
        , BAT_PRF_DEL_SUB_CNTN VARCHAR2(100)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_PRF_DEL_FLD_T
    ADD CONSTRAINT OLE_BAT_PRF_DEL_FLD_TP1
PRIMARY KEY (BAT_PRF_DEL_ID)
/


CREATE INDEX OLE_BAT_PRCS_DEL_FK 
  ON OLE_BAT_PRF_DEL_FLD_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_PRF_RNM_FLD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRF_RNM_FLD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRF_RNM_FLD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRF_RNM_FLD_T
(
      BAT_PRF_RNM_ID VARCHAR2(40)
        , BAT_PRCS_PRF_ID VARCHAR2(40)
        , BAT_PRF_ORGNL_TAG VARCHAR2(5)
        , BAT_PRF_ORGNL_IND1 VARCHAR2(3)
        , BAT_PRF_ORGNL_IND2 VARCHAR2(3)
        , BAT_PRF_ORGNL_SUBFLD VARCHAR2(3)
        , BAT_PRF_ORG_SUB_CNTN VARCHAR2(100)
        , BAT_PRF_RNM_TAG VARCHAR2(5)
        , BAT_PRF_RNM_IND1 VARCHAR2(3)
        , BAT_PRF_RNM_IND2 VARCHAR2(3)
        , BAT_PRF_RNM_SUBFLD VARCHAR2(3)
        , BAT_PRF_RNM_SUB_CNTN VARCHAR2(100)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_PRF_RNM_FLD_T
    ADD CONSTRAINT OLE_BAT_PRF_RNM_FLD_TP1
PRIMARY KEY (BAT_PRF_RNM_ID)
/


CREATE INDEX OLE_BAT_PRCS_RNM_FK 
  ON OLE_BAT_PRF_RNM_FLD_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_INST_MTCH_PNT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_INST_MTCH_PNT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_INST_MTCH_PNT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_INST_MTCH_PNT_T
(
      BAT_INST_MTCH_PNT_ID VARCHAR2(40)
        , BAT_INST_MTCH_PNT_NME VARCHAR2(100)
        , BAT_PRCS_PRF_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_INST_MTCH_PNT_T
    ADD CONSTRAINT OLE_BAT_INST_MTCH_PNT_TP1
PRIMARY KEY (BAT_INST_MTCH_PNT_ID)
/


CREATE INDEX BAT_INST_MTCH_PNT_FK 
  ON OLE_BAT_INST_MTCH_PNT_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_BIB_MTCH_PNT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_BIB_MTCH_PNT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_BIB_MTCH_PNT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_BIB_MTCH_PNT_T
(
      BAT_BIB_MTCH_PNT_ID VARCHAR2(40)
        , BAT_BIB_MTCH_PNT_NME VARCHAR2(100)
        , BAT_PRCS_PRF_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_BIB_MTCH_PNT_T
    ADD CONSTRAINT OLE_BAT_BIB_MTCH_PNT_TP1
PRIMARY KEY (BAT_BIB_MTCH_PNT_ID)
/


CREATE INDEX BAT_BIB_MTCH_PNT_FK 
  ON OLE_BAT_BIB_MTCH_PNT_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_PRCS_BIB_STUS_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_PRCS_BIB_STUS_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_PRCS_BIB_STUS_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_PRCS_BIB_STUS_T
(
      BAT_PRCS_BIB_STUS_ID VARCHAR2(40)
        , BAT_PRCS_BIB_STUS_NME VARCHAR2(100)
        , BAT_PRCS_PRF_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_PRCS_BIB_STUS_T
    ADD CONSTRAINT OLE_BAT_PRCS_BIB_STUS_TP1
PRIMARY KEY (BAT_PRCS_BIB_STUS_ID)
/


CREATE INDEX BAT_BIB_STS_FK 
  ON OLE_BAT_PRCS_BIB_STUS_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_BIB_WRK_UNT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_BIB_WRK_UNT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_BIB_WRK_UNT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_BIB_WRK_UNT_T
(
      BAT_BIB_WRK_UNT_ID VARCHAR2(40)
        , BAT_BIB_WRK_UNT_NME VARCHAR2(100)
        , BAT_PRCS_PRF_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_BIB_WRK_UNT_T
    ADD CONSTRAINT OLE_BAT_BIB_WRK_UNT_TP1
PRIMARY KEY (BAT_BIB_WRK_UNT_ID)
/


CREATE INDEX BAT_BIB_WRK_UNT_FK 
  ON OLE_BAT_BIB_WRK_UNT_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_BAT_INST_WRK_UNT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_BAT_INST_WRK_UNT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_BAT_INST_WRK_UNT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_BAT_INST_WRK_UNT_T
(
      BAT_INST_WRK_UNT_ID VARCHAR2(40)
        , BAT_INST_WRK_UNT_NME VARCHAR2(100)
        , BAT_PRCS_PRF_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_BAT_INST_WRK_UNT_T
    ADD CONSTRAINT OLE_BAT_INST_WRK_UNT_TP1
PRIMARY KEY (BAT_INST_WRK_UNT_ID)
/


CREATE INDEX BAT_INST_WRK_UNT_FK 
  ON OLE_BAT_INST_WRK_UNT_T 
  (BAT_PRCS_PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_CLNDR_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CLNDR_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CLNDR_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CLNDR_T
(
      OLE_CLNDR_ID VARCHAR2(40)
        , OLE_CLNDR_DESC VARCHAR2(100)
        , CL_SEQ VARCHAR2(40)
        , OLE_CLNDR_GRP_ID VARCHAR2(40)
        , BEGIN_DT DATE
        , END_DT DATE
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_CLNDR_T
    ADD CONSTRAINT OLE_CLNDR_TP1
PRIMARY KEY (OLE_CLNDR_ID)
/







-----------------------------------------------------------------------------
-- OLE_CLNDR_EXP_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CLNDR_EXP_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CLNDR_EXP_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CLNDR_EXP_TYP_T
(
      EXP_TYP_ID VARCHAR2(40)
        , EXP_TYP_CODE VARCHAR2(100)
        , EXP_TYP_NAME VARCHAR2(40)
        , EXP_TYP_IND VARCHAR2(1) default 'Y' NOT NULL
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_CLNDR_EXP_TYP_T
    ADD CONSTRAINT OLE_CLNDR_EXP_TYP_TP1
PRIMARY KEY (EXP_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_CLNDR_EXCP_DAT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CLNDR_EXCP_DAT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CLNDR_EXCP_DAT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CLNDR_EXCP_DAT_T
(
      OLE_CLNDR_EXCP_DAT_ID VARCHAR2(40)
        , OLE_CLNDR_ID VARCHAR2(40)
        , OLE_CLNDR_EXCPTN_DAT DATE
        , EXCPTN_DESC VARCHAR2(40)
        , EXCPTN_TYP VARCHAR2(40)
        , OPN_TIM VARCHAR2(40)
        , CLOS_TIM VARCHAR2(40)
        , OPN_TIM_SESS VARCHAR2(2)
        , CLOS_TIM_SESS VARCHAR2(2)
    

)
/

ALTER TABLE OLE_CLNDR_EXCP_DAT_T
    ADD CONSTRAINT OLE_CLNDR_EXCP_DAT_TP1
PRIMARY KEY (OLE_CLNDR_EXCP_DAT_ID)
/


CREATE INDEX OLE_CLNDR_EXCP_DAT_CN1 
  ON OLE_CLNDR_EXCP_DAT_T 
  (OLE_CLNDR_ID)
/





-----------------------------------------------------------------------------
-- OLE_CLNDR_WK_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CLNDR_WK_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CLNDR_WK_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CLNDR_WK_T
(
      OLE_CLNDR_WK_ID VARCHAR2(40)
        , OLE_CLNDR_ID VARCHAR2(40)
        , OPN_TIM VARCHAR2(40)
        , CLOS_TIM VARCHAR2(40)
        , FROM_DAY VARCHAR2(40)
        , TO_DAY VARCHAR2(40)
        , OPN_TIM_SESS VARCHAR2(40)
        , CLOS_TIM_SESS VARCHAR2(40)
        , WEEK_ACTIVE VARCHAR2(40)
    

)
/

ALTER TABLE OLE_CLNDR_WK_T
    ADD CONSTRAINT OLE_CLNDR_WK_TP1
PRIMARY KEY (OLE_CLNDR_WK_ID)
/


CREATE INDEX OLE_CLNDR_WK_CN1 
  ON OLE_CLNDR_WK_T 
  (OLE_CLNDR_ID)
/





-----------------------------------------------------------------------------
-- OLE_CLNDR_EXCP_PRD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CLNDR_EXCP_PRD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CLNDR_EXCP_PRD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CLNDR_EXCP_PRD_T
(
      OLE_CLNDR_EXCP_PRD_ID VARCHAR2(40)
        , OLE_CLNDR_ID VARCHAR2(40)
        , OLE_CLNDR_EXCPTN_PRD_DESC VARCHAR2(100)
        , OLE_CLNDR_EXCPTN_PRD_WEEK_ID VARCHAR2(40)
        , EXP_PRD_TYP VARCHAR2(40)
        , BEGIN_DT DATE
        , END_DT DATE
    

)
/

ALTER TABLE OLE_CLNDR_EXCP_PRD_T
    ADD CONSTRAINT OLE_CLNDR_EXCP_PRD_TP1
PRIMARY KEY (OLE_CLNDR_EXCP_PRD_ID)
/


CREATE INDEX OLE_CLNDR_EXCP_PRD_CN1 
  ON OLE_CLNDR_EXCP_PRD_T 
  (OLE_CLNDR_ID)
/





-----------------------------------------------------------------------------
-- OLE_CLNDR_EXCP_PRD_WK_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CLNDR_EXCP_PRD_WK_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CLNDR_EXCP_PRD_WK_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CLNDR_EXCP_PRD_WK_T
(
      OLE_CLNDR_EXCP_PRD_WK_ID VARCHAR2(40)
        , OLE_CLNDR_EXCP_PRD_ID VARCHAR2(40)
        , FROM_DAY VARCHAR2(40)
        , TO_DAY VARCHAR2(40)
        , OPN_TIM VARCHAR2(40)
        , CLOS_TIM VARCHAR2(40)
        , OPN_TIM_SESS VARCHAR2(2)
        , CLOS_TIM_SESS VARCHAR2(2)
        , PERIOD_WEEK_ACTIVE VARCHAR2(40)
    

)
/

ALTER TABLE OLE_CLNDR_EXCP_PRD_WK_T
    ADD CONSTRAINT OLE_CLNDR_EXCP_PRD_WK_TP1
PRIMARY KEY (OLE_CLNDR_EXCP_PRD_WK_ID)
/


CREATE INDEX OLE_CLNDR_EXCP_PRD_WK_CN1 
  ON OLE_CLNDR_EXCP_PRD_WK_T 
  (OLE_CLNDR_EXCP_PRD_ID)
/





-----------------------------------------------------------------------------
-- OLE_CLNDR_GRP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CLNDR_GRP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CLNDR_GRP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CLNDR_GRP_T
(
      OLE_CLNDR_GRP_ID VARCHAR2(40) default '0'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , CLNDR_GRP_CD VARCHAR2(40) NOT NULL
        , CLNDR_GRP_NM VARCHAR2(200) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
    
    , CONSTRAINT CLNDR_GRP_CD UNIQUE (CLNDR_GRP_CD)

)
/

ALTER TABLE OLE_CLNDR_GRP_T
    ADD CONSTRAINT OLE_CLNDR_GRP_TP1
PRIMARY KEY (OLE_CLNDR_GRP_ID)
/







-----------------------------------------------------------------------------
-- OLE_CNCL_RSN_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CNCL_RSN_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CNCL_RSN_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CNCL_RSN_T
(
      CNCL_RSN_ID VARCHAR2(40) default '0'
        , CNCL_RSN_NM VARCHAR2(100) NOT NULL
        , CNCL_RSN_TXT VARCHAR2(100)
        , ROW_ACT_IND VARCHAR2(1) default 'Y'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_CNCL_RSN_T
    ADD CONSTRAINT OLE_CNCL_RSN_TP1
PRIMARY KEY (CNCL_RSN_ID)
/







-----------------------------------------------------------------------------
-- OLE_CLAIM_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CLAIM_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CLAIM_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CLAIM_TYP_T
(
      OLE_CLAIM_TYP_ID VARCHAR2(40) default '0'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , OLE_CLAIM_TYP_CD VARCHAR2(40) NOT NULL
        , OLE_CLAIM_TYP_NM VARCHAR2(200) NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_CLAIM_TYP_CD UNIQUE (OLE_CLAIM_TYP_CD)

)
/

ALTER TABLE OLE_CLAIM_TYP_T
    ADD CONSTRAINT OLE_CLAIM_TYP_TP1
PRIMARY KEY (OLE_CLAIM_TYP_ID)
/







-----------------------------------------------------------------------------
-- OLE_CLM_NTCE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_CLM_NTCE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_CLM_NTCE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_CLM_NTCE_T
(
      CLM_NTCE_ID VARCHAR2(40) default '0'
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
        , CLM_NTCE_SNDR_NM VARCHAR2(40)
        , CLM_NTCE_VNDR_NM VARCHAR2(40)
        , CLM_NTCE_CLM_DT VARCHAR2(40)
        , CLM_NTCE_CLM_CNT VARCHAR2(40)
        , CLM_NTCE_CLM_TYP VARCHAR2(40)
        , CLM_NTCE_TIT VARCHAR2(500)
        , CLM_NTCE_PLC_OF_PUB VARCHAR2(500)
        , CLM_NTCE_PUB VARCHAR2(500)
        , CLM_NTCE_PUB_DT VARCHAR2(40)
        , CLM_NTCE_ENUM VARCHAR2(40)
        , CLM_NTCE_CHRON VARCHAR2(40)
        , CLM_NTCE_VNDR_LIB_NUM VARCHAR2(40)
        , CLM_NTCE_VNDR_ORD_NUM VARCHAR2(40)
        , CLM_NTCE_VNDR_TIT_NUM VARCHAR2(40)
        , CLM_NTCE_LIB_PO_NUM VARCHAR2(40)
        , CLM_NTCE_UN_BND_LOC VARCHAR2(40)
    

)
/

ALTER TABLE OLE_CLM_NTCE_T
    ADD CONSTRAINT OLE_CLM_NTCE_TP1
PRIMARY KEY (CLM_NTCE_ID)
/







-----------------------------------------------------------------------------
-- OLE_DONOR_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DONOR_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DONOR_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DONOR_T
(
      DONOR_ID VARCHAR2(10)
        , DONOR_CODE VARCHAR2(10)
        , DONOR_NAME VARCHAR2(255)
        , DONOR_NOTE VARCHAR2(40)
        , DONOR_PUBLIC_DISPLAY VARCHAR2(4000)
        , DONOR_AMT NUMBER(19,2)
        , DONOR_BOOKPLATE_URL VARCHAR2(40)
        , DONOR_PUBLIC_URL VARCHAR2(40)
        , ROW_ACT_IND VARCHAR2(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    

)
/

ALTER TABLE OLE_DONOR_T
    ADD CONSTRAINT OLE_DONOR_TP1
PRIMARY KEY (DONOR_ID)
/







-----------------------------------------------------------------------------
-- OLE_ASR_ITM_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_ASR_ITM_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_ASR_ITM_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_ASR_ITM_T
(
      ID VARCHAR2(40)
        , ITM_BAR_CD VARCHAR2(40)
        , TITLE VARCHAR2(37)
        , AUTHOR VARCHAR2(37)
        , CALL_NBR VARCHAR2(37)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_ASR_ITM_T
    ADD CONSTRAINT OLE_ASR_ITM_TP1
PRIMARY KEY (ID)
/


CREATE INDEX OLE_ASR_ITM_TI1 
  ON OLE_ASR_ITM_T 
  (ITM_BAR_CD)
/





-----------------------------------------------------------------------------
-- OLE_ASR_RQST_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_ASR_RQST_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_ASR_RQST_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_ASR_RQST_T
(
      ID VARCHAR2(40)
        , ITM_BAR_CD VARCHAR2(40)
        , PTRN_ID VARCHAR2(37)
        , PK_UP_LOCN VARCHAR2(37)
        , PTRN_NM VARCHAR2(37)
        , RQST_ID VARCHAR2(37)
        , RQST_STAT VARCHAR2(37)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_ASR_RQST_T
    ADD CONSTRAINT OLE_ASR_RQST_TP1
PRIMARY KEY (ID)
/







-----------------------------------------------------------------------------
-- OLE_DS_DOC_TYPE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DS_DOC_TYPE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DS_DOC_TYPE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DS_DOC_TYPE_T
(
      DOC_TYPE_ID NUMBER(10,0)
        , NAME VARCHAR2(100) NOT NULL
        , DISPLAY_LABEL VARCHAR2(100)
        , DESCRIPTION VARCHAR2(400)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
        , DATE_UPDATED TIMESTAMP
    
    , CONSTRAINT OLE_DS_DOC_TYPE_TCONST1 UNIQUE (NAME)

)
/

ALTER TABLE OLE_DS_DOC_TYPE_T
    ADD CONSTRAINT OLE_DS_DOC_TYPE_TP1
PRIMARY KEY (DOC_TYPE_ID)
/







-----------------------------------------------------------------------------
-- OLE_DS_DOC_FORMAT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DS_DOC_FORMAT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DS_DOC_FORMAT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DS_DOC_FORMAT_T
(
      DOC_FORMAT_ID NUMBER(10,0)
        , NAME VARCHAR2(100) NOT NULL
        , DISPLAY_LABEL VARCHAR2(100)
        , DESCRIPTION VARCHAR2(400)
        , DOC_TYPE_ID NUMBER(10,0)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
        , DATE_UPDATED TIMESTAMP
    
    , CONSTRAINT OLE_DS_DOC_FORMAT_TCONST1 UNIQUE (NAME, DOC_TYPE_ID)

)
/

ALTER TABLE OLE_DS_DOC_FORMAT_T
    ADD CONSTRAINT OLE_DS_DOC_FORMAT_TP1
PRIMARY KEY (DOC_FORMAT_ID)
/







-----------------------------------------------------------------------------
-- OLE_DS_DOC_FIELD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DS_DOC_FIELD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DS_DOC_FIELD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DS_DOC_FIELD_T
(
      DOC_FIELD_ID NUMBER(10,0)
        , DOC_TYPE_ID NUMBER(10,0)
        , DOC_FORMAT_ID NUMBER(10,0)
        , NAME VARCHAR2(100)
        , DISPLAY_LABEL VARCHAR2(100)
        , DESCRIPTION VARCHAR2(400)
        , INCLUDE_PATH VARCHAR2(500)
        , EXCLUDE_PATH VARCHAR2(500)
        , IS_SEARCH VARCHAR2(1)
        , IS_DISPLAY VARCHAR2(1)
        , IS_FACET VARCHAR2(1)
        , IS_EXPORT VARCHAR2(1)
        , IS_GLOBAL_EDIT VARCHAR2(1)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
        , DATE_UPDATED TIMESTAMP
    
    , CONSTRAINT OLE_DS_DOC_FIELD_TCONST1 UNIQUE (NAME, DOC_TYPE_ID, DOC_FORMAT_ID)

)
/

ALTER TABLE OLE_DS_DOC_FIELD_T
    ADD CONSTRAINT OLE_DS_DOC_FIELD_TP1
PRIMARY KEY (DOC_FIELD_ID)
/







-----------------------------------------------------------------------------
-- OLE_DS_SEARCH_RESULT_PAGE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DS_SEARCH_RESULT_PAGE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DS_SEARCH_RESULT_PAGE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DS_SEARCH_RESULT_PAGE_T
(
      DOC_SEARCH_PAGE_SIZE_ID NUMBER(10,0)
        , PAGE_SIZE NUMBER(10,0)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
        , DATE_UPDATED TIMESTAMP
    
    , CONSTRAINT PAGE_SIZE UNIQUE (PAGE_SIZE)

)
/

ALTER TABLE OLE_DS_SEARCH_RESULT_PAGE_T
    ADD CONSTRAINT OLE_DS_SEARCH_RESULT_PAGE_TP1
PRIMARY KEY (DOC_SEARCH_PAGE_SIZE_ID)
/







-----------------------------------------------------------------------------
-- OLE_DS_SEARCH_FACET_SIZE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DS_SEARCH_FACET_SIZE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DS_SEARCH_FACET_SIZE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DS_SEARCH_FACET_SIZE_T
(
      DOC_SEARCH_FACET_SIZE_ID NUMBER(10,0)
        , SHORT_SIZE NUMBER(10,0)
        , LONG_SIZE NUMBER(10,0)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
        , DATE_UPDATED TIMESTAMP
    

)
/

ALTER TABLE OLE_DS_SEARCH_FACET_SIZE_T
    ADD CONSTRAINT OLE_DS_SEARCH_FACET_SIZE_TP1
PRIMARY KEY (DOC_SEARCH_FACET_SIZE_ID)
/







-----------------------------------------------------------------------------
-- OLE_DLVR_LOAN_NOTICE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_LOAN_NOTICE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_LOAN_NOTICE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_LOAN_NOTICE_T
(
      ID VARCHAR2(40)
        , LOAN_ID VARCHAR2(40)
        , PTRN_ID VARCHAR2(37)
        , NTC_TYP VARCHAR2(100)
        , NTC_CNTNT_CONFIG_NAME VARCHAR2(400)
        , NTC_SND_TYP VARCHAR2(37)
        , RQST_ID VARCHAR2(37)
        , RPLCMNT_FEE_AMNT NUMBER(8)
        , LOST_ITM_PRCS_FEE_AMNT NUMBER(8)
        , NTC_TO_SND_DT DATE
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_DLVR_LOAN_NOTICE_T
    ADD CONSTRAINT OLE_DLVR_LOAN_NOTICE_TP1
PRIMARY KEY (ID)
/


CREATE INDEX NTC_TO_SND_DT_INDX 
  ON OLE_DLVR_LOAN_NOTICE_T 
  (NTC_TO_SND_DT)
/
CREATE INDEX NTC_TYP_INDX 
  ON OLE_DLVR_LOAN_NOTICE_T 
  (NTC_TYP)
/
CREATE INDEX PTRN_ID_INDX 
  ON OLE_DLVR_LOAN_NOTICE_T 
  (PTRN_ID)
/
CREATE INDEX ODLN_LOAN_ID_INDX 
  ON OLE_DLVR_LOAN_NOTICE_T 
  (LOAN_ID)
/





-----------------------------------------------------------------------------
-- OLE_DLVR_LOAN_NOTICE_HSTRY_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DLVR_LOAN_NOTICE_HSTRY_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DLVR_LOAN_NOTICE_HSTRY_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DLVR_LOAN_NOTICE_HSTRY_T
(
      ID VARCHAR2(40)
        , LOAN_ID VARCHAR2(40)
        , PTRN_ID VARCHAR2(37)
        , RQST_ID VARCHAR2(37)
        , NTC_SNT_DT DATE
        , NTC_TYP VARCHAR2(100)
        , NTC_SND_TYP VARCHAR2(37)
        , NTC_CNTNT CLOB
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_DLVR_LOAN_NOTICE_HSTRY_T
    ADD CONSTRAINT OLE_DLVR_LOAN_NOTICE_HSTRY_P1
PRIMARY KEY (ID)
/


CREATE INDEX OLE_DLVR_LOAN_NOTICE_HSTRY_TI1 
  ON OLE_DLVR_LOAN_NOTICE_HSTRY_T 
  (PTRN_ID)
/





-----------------------------------------------------------------------------
-- OLE_RETURN_HISTORY_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_RETURN_HISTORY_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_RETURN_HISTORY_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_RETURN_HISTORY_T
(
      ID VARCHAR2(36)
        , ITEM_BARCODE VARCHAR2(40)
        , ITEM_UUID VARCHAR2(40)
        , ITEM_RETURNED_DT TIMESTAMP
        , OPERATOR VARCHAR2(36)
        , CIR_DESK_LOC VARCHAR2(100)
        , CIR_DESK_ROUTE_TO VARCHAR2(100)
        , RETURNED_ITEM_STATUS VARCHAR2(200)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_RETURN_HISTORY_T
    ADD CONSTRAINT OLE_RETURN_HISTORY_TP1
PRIMARY KEY (ID)
/


CREATE INDEX OLE_LOAN_INTRANSIT_HST_TI1 
  ON OLE_RETURN_HISTORY_T 
  (ITEM_UUID)
/





-----------------------------------------------------------------------------
-- OLE_RNWL_HISTORY_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_RNWL_HISTORY_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_RNWL_HISTORY_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_RNWL_HISTORY_T
(
      RNWL_HSTRY_ID VARCHAR2(36)
        , LOAN_ID VARCHAR2(40)
        , PTRN_BARCODE VARCHAR2(100)
        , ITEM_BARCODE VARCHAR2(100)
        , ITEM_UUID VARCHAR2(100)
        , OPRTR_ID VARCHAR2(100)
        , RNWD_DT TIMESTAMP
        , RNWL_DUE_DT TIMESTAMP
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_RNWL_HISTORY_T
    ADD CONSTRAINT OLE_RNWL_HISTORY_TP1
PRIMARY KEY (RNWL_HSTRY_ID)
/







-----------------------------------------------------------------------------
-- OLE_DRL_EDITOR_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DRL_EDITOR_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DRL_EDITOR_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DRL_EDITOR_T
(
      EDITOR_ID VARCHAR2(40)
        , EDITOR_TYP VARCHAR2(40)
        , FILE_NM VARCHAR2(40)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_DRL_EDITOR_T
    ADD CONSTRAINT OLE_DRL_EDITOR_TP1
PRIMARY KEY (EDITOR_ID)
/







-----------------------------------------------------------------------------
-- OLE_DRL_RULE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DRL_RULE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DRL_RULE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DRL_RULE_T
(
      RULE_ID VARCHAR2(40)
        , AGENDA_GROUP VARCHAR2(200)
        , EDITOR_ID VARCHAR2(200)
        , ACTIVATION_GROUP VARCHAR2(200)
        , RULE_TYP VARCHAR2(40)
        , RULE_NM VARCHAR2(200)
        , ITEM_TYPES BLOB
        , BORROWER_TYPES BLOB
        , INST_LOCATIONS BLOB
        , CAMPUS_LOCATIONS BLOB
        , COLL_LOCATIONS BLOB
        , LIBRARY_LOCATIONS BLOB
        , SHELVING_LOCATIONS BLOB
        , CIRC_POLICY VARCHAR2(200)
        , LOAN_PERIOD VARCHAR2(50)
        , DFLT_RECALL_PERIOD VARCHAR2(50)
        , ITEM_TYP_OPERATOR VARCHAR2(50)
        , BORROWER_TYP_OPERATOR VARCHAR2(50)
        , INST_LOCATION_OPERATOR VARCHAR2(50)
        , CAMPUS_LOCATION_OPERATOR VARCHAR2(50)
        , LIBRARY_LOCATION_OPERATOR VARCHAR2(50)
        , COLL_LOCATION_OPERATOR VARCHAR2(50)
        , SHELVING_LOCATION_OPERATOR VARCHAR2(50)
        , LOAN_TYPE VARCHAR2(200)
        , ITM_TYP_COUNT VARCHAR2(50)
        , ITM_TYP_COUNT_OPTR VARCHAR2(50)
        , ERROR_MSG VARCHAR2(500)
        , OVERRIDE_PERMISSION VARCHAR2(200)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_DRL_RULE_T
    ADD CONSTRAINT OLE_DRL_RULE_TP1
PRIMARY KEY (RULE_ID)
/







-----------------------------------------------------------------------------
-- OLE_DRL_FINE_LIMITS_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_DRL_FINE_LIMITS_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_DRL_FINE_LIMITS_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_DRL_FINE_LIMITS_T
(
      ID VARCHAR2(40)
        , RULE_ID VARCHAR2(40)
        , BORROWER_TYPE VARCHAR2(200)
        , LIMIT_AMOUNT VARCHAR2(8)
        , OVERDUE_LIMIT VARCHAR2(8)
        , OPERATOR VARCHAR2(8)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_DRL_FINE_LIMITS_T
    ADD CONSTRAINT OLE_DRL_FINE_LIMITS_TP1
PRIMARY KEY (ID)
/







-----------------------------------------------------------------------------
-- OLE_NOTICE_TYPE_CONFIG_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_NOTICE_TYPE_CONFIG_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_NOTICE_TYPE_CONFIG_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_NOTICE_TYPE_CONFIG_T
(
      NOTICE_TYPE_CONFIG_ID NUMBER(10,0)
        , CIRC_POLICY_ID VARCHAR2(1000)
        , NOTICE_TYPE VARCHAR2(1000)
    

)
/

ALTER TABLE OLE_NOTICE_TYPE_CONFIG_T
    ADD CONSTRAINT OLE_NOTICE_TYPE_CONFIG_TP1
PRIMARY KEY (NOTICE_TYPE_CONFIG_ID)
/







-----------------------------------------------------------------------------
-- ALRT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'ALRT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE ALRT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE ALRT_T
(
      ALRT_ID VARCHAR2(10)
        , DCMNT_ID VARCHAR2(40)
        , ALRT_DT DATE
        , ALRT_NTE VARCHAR2(2000)
        , ALRT_CRT_DT DATE
        , ALRT_MDFD_DT DATE
        , ALRT_INIT_ID VARCHAR2(40)
        , ALRT_MODFIR_ID VARCHAR2(40)
        , ALRT_STAT VARCHAR2(1)
        , RCVNG_USR_ID VARCHAR2(40)
        , RCVNG_GRP_ID VARCHAR2(40)
        , RCVNG_ROLE_ID VARCHAR2(40)
        , ALRT_APRVR_ID VARCHAR2(40)
        , ALRT_APROVD_DT DATE
        , ALRT_INTRVL VARCHAR2(40)
        , ALRT_RPTBLE VARCHAR2(1)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE ALRT_T
    ADD CONSTRAINT ALRT_TP1
PRIMARY KEY (ALRT_ID)
/


CREATE INDEX DCMNT_ID_IND1 
  ON ALRT_T 
  (DCMNT_ID)
/
CREATE INDEX ALRT_DT_IND1 
  ON ALRT_T 
  (ALRT_DT)
/
CREATE INDEX RCVNG_USR_ID_IND1 
  ON ALRT_T 
  (RCVNG_USR_ID)
/
CREATE INDEX ALRT_STAT_IND1 
  ON ALRT_T 
  (ALRT_STAT)
/





-----------------------------------------------------------------------------
-- ALRT_DOC_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'ALRT_DOC_TYP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE ALRT_DOC_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE ALRT_DOC_TYP_T
(
      ALRT_DOC_TYP_ID VARCHAR2(40)
        , ALRT_DOC_TYP_NAME VARCHAR2(200)
        , ALRT_DOC_TYP_DESC VARCHAR2(800)
        , ALRT_DOC_CLASS VARCHAR2(400)
        , ALRT_REM_INTERVAL VARCHAR2(100)
        , ACTV_IND VARCHAR2(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
    

)
/

ALTER TABLE ALRT_DOC_TYP_T
    ADD CONSTRAINT ALRT_DOC_TYP_TP1
PRIMARY KEY (ALRT_DOC_TYP_ID)
/







-----------------------------------------------------------------------------
-- ALRT_EVENT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'ALRT_EVENT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE ALRT_EVENT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE ALRT_EVENT_T
(
      ALRT_EVENT_ID VARCHAR2(40)
        , ALRT_EVENT_NAME VARCHAR2(40)
        , ALRT_DOC_TYP_ID VARCHAR2(100)
        , ACTV_IND VARCHAR2(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
    

)
/

ALTER TABLE ALRT_EVENT_T
    ADD CONSTRAINT ALRT_EVENT_TP1
PRIMARY KEY (ALRT_EVENT_ID)
/







-----------------------------------------------------------------------------
-- ALRT_EVENT_FIELD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'ALRT_EVENT_FIELD_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE ALRT_EVENT_FIELD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE ALRT_EVENT_FIELD_T
(
      ALRT_EVENT_FIELD_ID VARCHAR2(40)
        , ALRT_EVENT_ID VARCHAR2(40)
        , ALRT_FIELD_NM VARCHAR2(100)
        , ALRT_FIELD_TYP VARCHAR2(100)
        , ALRT_FIELD_VAL VARCHAR2(40)
        , ALRT_CRIT VARCHAR2(40)
        , ACTV_IND VARCHAR2(1)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
    

)
/

ALTER TABLE ALRT_EVENT_FIELD_T
    ADD CONSTRAINT ALRT_EVENT_FIELD_TP1
PRIMARY KEY (ALRT_EVENT_FIELD_ID)
/







-----------------------------------------------------------------------------
-- ALRT_DOC_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'ALRT_DOC_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE ALRT_DOC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE ALRT_DOC_T
(
      ALRT_DOC_ID VARCHAR2(40)
        , DOC_TYP_ID VARCHAR2(40)
        , DOC_TYP_NM VARCHAR2(200)
        , ALRT_DOC_CRTR_ID VARCHAR2(100)
        , ALRT_RPT VARCHAR2(1)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
    

)
/

ALTER TABLE ALRT_DOC_T
    ADD CONSTRAINT ALRT_DOC_TP1
PRIMARY KEY (ALRT_DOC_ID)
/







-----------------------------------------------------------------------------
-- ALRT_CNDTN_NTFCN_INFO_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'ALRT_CNDTN_NTFCN_INFO_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE ALRT_CNDTN_NTFCN_INFO_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE ALRT_CNDTN_NTFCN_INFO_T
(
      ALRT_CNDTN_ID VARCHAR2(40)
        , GRP_ID VARCHAR2(40)
        , ROLE_ID VARCHAR2(100)
        , PRNCPL_ID VARCHAR2(100)
        , ALRT_EVENT_ID VARCHAR2(100)
        , ALRT_NOTE VARCHAR2(2000)
        , IS_EMAIL VARCHAR2(100)
        , IS_ALRT_LST VARCHAR2(100)
        , ALRT_INTRVL VARCHAR2(100)
        , ALRT_DOC_ID VARCHAR2(40)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8)
    

)
/

ALTER TABLE ALRT_CNDTN_NTFCN_INFO_T
    ADD CONSTRAINT ALRT_CNDTN_NTFCN_INFO_TP1
PRIMARY KEY (ALRT_CNDTN_ID)
/







-----------------------------------------------------------------------------
-- ACTN_LST_ALRT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'ACTN_LST_ALRT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE ACTN_LST_ALRT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE ACTN_LST_ALRT_T
(
      ACTN_LST_ALRT_ID VARCHAR2(10)
        , DCMNT_ID VARCHAR2(40)
        , TITL VARCHAR2(800)
        , RCRD_TYP VARCHAR2(80)
        , ALRT_DT DATE
        , NTE VARCHAR2(800)
        , ACTV VARCHAR2(1)
        , ALRT_USR_ID VARCHAR2(40)
        , ALRT_INIT_ID VARCHAR2(40)
        , ALRT_APRVR_ID VARCHAR2(40)
        , ALRT_APROVD_DT DATE
        , ALRT_ID VARCHAR2(10)
        , OBJ_ID VARCHAR2(36) NOT NULL
        , VER_NBR NUMBER(8) NOT NULL
    

)
/

ALTER TABLE ACTN_LST_ALRT_T
    ADD CONSTRAINT ACTN_LST_ALRT_TP1
PRIMARY KEY (ACTN_LST_ALRT_ID)
/


CREATE INDEX DCMNT_ID_IND2 
  ON ACTN_LST_ALRT_T 
  (DCMNT_ID)
/
CREATE INDEX ALRT_DT_IND2 
  ON ACTN_LST_ALRT_T 
  (ALRT_DT)
/
CREATE INDEX ALRT_USR_ID_IND2 
  ON ACTN_LST_ALRT_T 
  (ALRT_USR_ID)
/
CREATE INDEX ACTV_IND2 
  ON ACTN_LST_ALRT_T 
  (ACTV)
/





-----------------------------------------------------------------------------
-- OLE_GOKB_PKG_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_GOKB_PKG_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_GOKB_PKG_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_GOKB_PKG_T
(
      GOKB_PKG_ID NUMBER(10,0)
        , PKG_NAME VARCHAR2(1000)
        , VARIANT_NAME VARCHAR2(4000)
        , PKG_STATUS VARCHAR2(20)
        , PKG_SCOPE VARCHAR2(20)
        , BREAKABLE VARCHAR2(10)
        , FXD VARCHAR2(10)
        , AVLBLE VARCHAR2(10)
        , DATE_CREATED DATE
        , DATE_UPDATED DATE
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_GOKB_PKG_T
    ADD CONSTRAINT OLE_GOKB_PKG_TP1
PRIMARY KEY (GOKB_PKG_ID)
/







-----------------------------------------------------------------------------
-- OLE_GOKB_TIPP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_GOKB_TIPP_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_GOKB_TIPP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_GOKB_TIPP_T
(
      GOKB_TIPP_ID NUMBER(10,0)
        , GOKB_PKG_ID NUMBER(10,0)
        , GOKB_TITLE_ID NUMBER(10,0)
        , GOKB_PLTFRM_ID NUMBER(10,0)
        , TIPP_STATUS VARCHAR2(100)
        , STATUS_REASON VARCHAR2(300)
        , STRT_DT DATE
        , STRT_VOL VARCHAR2(100)
        , STRT_ISSUE VARCHAR2(100)
        , END_DT DATE
        , END_VOL VARCHAR2(100)
        , END_ISSUE VARCHAR2(100)
        , EMBARGO VARCHAR2(50)
        , PLTFRM_HOST_URL VARCHAR2(300)
        , DATE_CREATED DATE
        , DATE_UPDATED DATE
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_GOKB_TIPP_T
    ADD CONSTRAINT OLE_GOKB_TIPP_TP1
PRIMARY KEY (GOKB_TIPP_ID)
/







-----------------------------------------------------------------------------
-- OLE_GOKB_TITLE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_GOKB_TITLE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_GOKB_TITLE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_GOKB_TITLE_T
(
      GOKB_TITLE_ID NUMBER(10,0)
        , TITLE_NAME VARCHAR2(500)
        , VARIANT_NAME VARCHAR2(4000)
        , MEDIUM VARCHAR2(50)
        , PURE_QA VARCHAR2(50)
        , TI_ISSN_ONLINE VARCHAR2(50)
        , TI_ISSN_PRNT VARCHAR2(50)
        , TI_ISSN_L VARCHAR2(50)
        , OCLC_NUM NUMBER(10,0)
        , TI_DOI VARCHAR2(50)
        , TI_PROPRIETARY_ID NUMBER(10,0)
        , TI_SUNCAT VARCHAR2(50)
        , TI_LCCN VARCHAR2(50)
        , PUBLSHR_ID NUMBER(10,0)
        , IMPRINT VARCHAR2(1000)
        , DATE_CREATED DATE
        , DATE_UPDATED DATE
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_GOKB_TITLE_T
    ADD CONSTRAINT OLE_GOKB_TITLE_TP1
PRIMARY KEY (GOKB_TITLE_ID)
/







-----------------------------------------------------------------------------
-- OLE_GOKB_PLTFRM_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_GOKB_PLTFRM_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_GOKB_PLTFRM_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_GOKB_PLTFRM_T
(
      GOKB_PLTFRM_ID NUMBER(10,0)
        , PLTFRM_NAME VARCHAR2(500)
        , PLTFRM_STATUS VARCHAR2(100)
        , PLTFRM_PRVDR_ID NUMBER(10,0)
        , AUTH VARCHAR2(50)
        , SOFTWARE_PLTFRM VARCHAR2(50)
        , DATE_CREATED DATE
        , DATE_UPDATED DATE
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_GOKB_PLTFRM_T
    ADD CONSTRAINT OLE_GOKB_PLTFRM_TP1
PRIMARY KEY (GOKB_PLTFRM_ID)
/







-----------------------------------------------------------------------------
-- OLE_GOKB_ORG_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_GOKB_ORG_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_GOKB_ORG_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_GOKB_ORG_T
(
      GOKB_ORG_ID NUMBER(10,0)
        , ORG_NAME VARCHAR2(1000)
        , VARIANT_NAME VARCHAR2(4000)
        , DATE_CREATED DATE
        , DATE_UPDATED DATE
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_GOKB_ORG_T
    ADD CONSTRAINT OLE_GOKB_ORG_TP1
PRIMARY KEY (GOKB_ORG_ID)
/







-----------------------------------------------------------------------------
-- OLE_GOKB_UPDATE_LOG_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_GOKB_UPDATE_LOG_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_GOKB_UPDATE_LOG_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_GOKB_UPDATE_LOG_T
(
      ID NUMBER(10,0)
        , NUM_PKGS NUMBER(10,0)
        , NUM_TIPPS NUMBER(10,0)
        , NUM_TITLES NUMBER(10,0)
        , NUM_ORGS NUMBER(10,0)
        , NUM_PLTFRMS NUMBER(10,0)
        , USER_NM VARCHAR2(100)
        , STATUS VARCHAR2(20)
        , START_TIME DATE
        , END_TIME DATE
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_GOKB_UPDATE_LOG_T
    ADD CONSTRAINT OLE_GOKB_UPDATE_LOG_TP1
PRIMARY KEY (ID)
/







-----------------------------------------------------------------------------
-- OLE_GOKB_ORG_ROLE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_GOKB_ORG_ROLE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_GOKB_ORG_ROLE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_GOKB_ORG_ROLE_T
(
      GOKB_ORG_ROLE_ID NUMBER(10,0)
        , GOKB_ORG_ID NUMBER(10,0)
        , ROLE VARCHAR2(100)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_GOKB_ORG_ROLE_T
    ADD CONSTRAINT OLE_GOKB_ORG_ROLE_TP1
PRIMARY KEY (GOKB_ORG_ROLE_ID)
/







-----------------------------------------------------------------------------
-- OLE_GOKB_REVIEW_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_GOKB_REVIEW_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_GOKB_REVIEW_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_GOKB_REVIEW_T
(
      GOKB_REVIEW_ID NUMBER(10,0)
        , REVIEW_DT DATE
        , E_RES_REC_ID VARCHAR2(10)
        , TYPE VARCHAR2(100)
        , DETAILS VARCHAR2(1000)
        , GOKB_TIPP_ID NUMBER(10,0)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_GOKB_REVIEW_T
    ADD CONSTRAINT OLE_GOKB_REVIEW_TP1
PRIMARY KEY (GOKB_REVIEW_ID)
/


CREATE INDEX OLE_GOKB_REVIEW_I 
  ON OLE_GOKB_REVIEW_T 
  (E_RES_REC_ID)
/





-----------------------------------------------------------------------------
-- OLE_GOKB_CHANGE_LOG_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_GOKB_CHANGE_LOG_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_GOKB_CHANGE_LOG_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_GOKB_CHANGE_LOG_T
(
      GOKB_CHANGE_LOG_ID NUMBER(10,0)
        , CHANGLOG_DT DATE
        , E_RES_REC_ID VARCHAR2(10)
        , TYPE VARCHAR2(100)
        , DETAILS VARCHAR2(1000)
        , ORIGIN VARCHAR2(100)
        , GOKB_TIPP_ID NUMBER(10,0)
        , ARCHIVED_DT DATE
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_GOKB_CHANGE_LOG_T
    ADD CONSTRAINT OLE_GOKB_CHANGE_LOG_TP1
PRIMARY KEY (GOKB_CHANGE_LOG_ID)
/


CREATE INDEX OLE_GOKB_CHANGE_LOG_I 
  ON OLE_GOKB_CHANGE_LOG_T 
  (E_RES_REC_ID)
/





-----------------------------------------------------------------------------
-- OLE_GOKB_ARCHIVE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_GOKB_ARCHIVE_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_GOKB_ARCHIVE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_GOKB_ARCHIVE_T
(
      GOKB_ARCHIVE_ID NUMBER(10,0)
        , E_RES_REC_ID VARCHAR2(10)
        , TYPE VARCHAR2(100)
        , DETAILS VARCHAR2(1000)
        , ORIGIN VARCHAR2(100)
        , GOKB_TIPP_ID NUMBER(10,0)
        , ARCHIVED_DT DATE
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_GOKB_ARCHIVE_T
    ADD CONSTRAINT OLE_GOKB_ARCHIVE_TP1
PRIMARY KEY (GOKB_ARCHIVE_ID)
/


CREATE INDEX OLE_GOKB_ARCHIVE_I 
  ON OLE_GOKB_ARCHIVE_T 
  (E_RES_REC_ID)
/





-----------------------------------------------------------------------------
-- OLE_NOTC_FIELD_LABEL_MAPNG_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_NOTC_FIELD_LABEL_MAPNG_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_NOTC_FIELD_LABEL_MAPNG_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_NOTC_FIELD_LABEL_MAPNG_T
(
      OLE_NOTC_FIELD_LABEL_MAPNG_ID VARCHAR2(40) default '0'
        , FLD_NM VARCHAR2(40) NOT NULL
        , FLD_LBL VARCHAR2(40)
        , BLNG_TO VARCHAR2(40)
        , OLE_NOTC_CNTNT_CONFIG_ID VARCHAR2(40)
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_NOTC_FIELD_LABEL_MAPNG_T
    ADD CONSTRAINT OLE_NOTC_FIELD_LABEL_MAPNG_P1
PRIMARY KEY (OLE_NOTC_FIELD_LABEL_MAPNG_ID)
/


CREATE INDEX FLD_NM_INDX 
  ON OLE_NOTC_FIELD_LABEL_MAPNG_T 
  (FLD_NM)
/





-----------------------------------------------------------------------------
-- OLE_NOTC_CNTNT_CONFIG_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_NOTC_CNTNT_CONFIG_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_NOTC_CNTNT_CONFIG_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_NOTC_CNTNT_CONFIG_T
(
      OLE_NOTC_CNTNT_CONFIG_ID VARCHAR2(40)
        , NOTC_TYP VARCHAR2(40) NOT NULL
        , NOTC_NM VARCHAR2(400) NOT NULL
        , NOTC_TITL VARCHAR2(400) NOT NULL
        , ACTV_IND VARCHAR2(1) default 'Y' NOT NULL
        , NOTC_BDY VARCHAR2(4000) NOT NULL
        , NOTC_FTR_BDY VARCHAR2(4000) NOT NULL
        , NOTC_SUBJ_LN VARCHAR2(4000) NOT NULL
        , VER_NBR NUMBER(8)
        , OBJ_ID VARCHAR2(36)
    

)
/

ALTER TABLE OLE_NOTC_CNTNT_CONFIG_T
    ADD CONSTRAINT OLE_NOTC_CNTNT_CONFIG_TP1
PRIMARY KEY (OLE_NOTC_CNTNT_CONFIG_ID)
/


CREATE INDEX NOTC_TYP_INDX 
  ON OLE_NOTC_CNTNT_CONFIG_T 
  (NOTC_TYP)
/
CREATE INDEX NOTC_NM_INDEX 
  ON OLE_NOTC_CNTNT_CONFIG_T 
  (NOTC_NM)
/





-----------------------------------------------------------------------------
-- ITEM_AUDIT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'ITEM_AUDIT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE ITEM_AUDIT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE ITEM_AUDIT_T
(
      AUDIT_ID NUMBER(10,0)
        , FOREIGN_KEY_REF NUMBER(10,0)
        , ACTOR VARCHAR2(40)
        , UPDATE_DATE TIMESTAMP
        , COLUMN_UPDATED VARCHAR2(40)
        , COLUMN_VALUE BLOB
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8,0)
    

)
/

ALTER TABLE ITEM_AUDIT_T
    ADD CONSTRAINT ITEM_AUDIT_TP1
PRIMARY KEY (AUDIT_ID)
/







-----------------------------------------------------------------------------
-- BIB_AUDIT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'BIB_AUDIT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE BIB_AUDIT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE BIB_AUDIT_T
(
      AUDIT_ID NUMBER(10,0)
        , FOREIGN_KEY_REF NUMBER(10,0)
        , ACTOR VARCHAR2(40)
        , UPDATE_DATE TIMESTAMP
        , COLUMN_UPDATED VARCHAR2(40)
        , COLUMN_VALUE BLOB
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8,0)
    

)
/

ALTER TABLE BIB_AUDIT_T
    ADD CONSTRAINT BIB_AUDIT_TP1
PRIMARY KEY (AUDIT_ID)
/







-----------------------------------------------------------------------------
-- HOLDINGS_AUDIT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'HOLDINGS_AUDIT_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE HOLDINGS_AUDIT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE HOLDINGS_AUDIT_T
(
      AUDIT_ID NUMBER(10,0)
        , FOREIGN_KEY_REF NUMBER(10,0)
        , ACTOR VARCHAR2(40)
        , UPDATE_DATE TIMESTAMP
        , COLUMN_UPDATED VARCHAR2(40)
        , COLUMN_VALUE BLOB
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8,0)
    

)
/

ALTER TABLE HOLDINGS_AUDIT_T
    ADD CONSTRAINT HOLDINGS_AUDIT_TP1
PRIMARY KEY (AUDIT_ID)
/







-----------------------------------------------------------------------------
-- OLE_NG_BAT_PRF_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_NG_BAT_PRF_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_NG_BAT_PRF_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_NG_BAT_PRF_T
(
      PRF_ID NUMBER(10,0)
        , PRF_NM VARCHAR2(40)
        , PRF_TYP VARCHAR2(40)
        , CONTENT BLOB
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8,0)
    

)
/

ALTER TABLE OLE_NG_BAT_PRF_T
    ADD CONSTRAINT OLE_NG_BAT_PRF_TP1
PRIMARY KEY (PRF_ID)
/







-----------------------------------------------------------------------------
-- OLE_NG_BAT_PRCS_JOB_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_NG_BAT_PRCS_JOB_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_NG_BAT_PRCS_JOB_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_NG_BAT_PRCS_JOB_T
(
      JOB_ID NUMBER(10,0)
        , JOB_NAME VARCHAR2(100)
        , PROFILE_TYPE VARCHAR2(40)
        , PRF_ID NUMBER(10,0)
        , JOB_TYPE VARCHAR2(40)
        , CRON_EXP VARCHAR2(100)
        , CREATED_BY VARCHAR2(40)
        , CREATED_ON TIMESTAMP
        , NEXT_RUN_TIME TIMESTAMP
        , NUM_OF_RECORD NUMBER(10,0)
        , OUTPUT_FORMAT VARCHAR2(40)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8,0)
    

)
/

ALTER TABLE OLE_NG_BAT_PRCS_JOB_T
    ADD CONSTRAINT OLE_NG_BAT_PRCS_JOB_TP1
PRIMARY KEY (JOB_ID)
/


CREATE INDEX OLE_NG_BAT_PRCS_I 
  ON OLE_NG_BAT_PRCS_JOB_T 
  (PRF_ID)
/





-----------------------------------------------------------------------------
-- OLE_NG_BAT_JOB_DETAILS_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'OLE_NG_BAT_JOB_DETAILS_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE OLE_NG_BAT_JOB_DETAILS_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE OLE_NG_BAT_JOB_DETAILS_T
(
      JOB_DETAIL_ID NUMBER(10,0)
        , JOB_NAME VARCHAR2(100)
        , JOB_ID NUMBER(10,0)
        , PROFILE_TYPE VARCHAR2(40)
        , PROFILE_NAME VARCHAR2(100)
        , FILE_NAME VARCHAR2(100)
        , CREATED_BY VARCHAR2(40)
        , START_TIME TIMESTAMP
        , END_TIME TIMESTAMP
        , PER_COMPLETED VARCHAR2(40)
        , TIME_SPENT VARCHAR2(40)
        , TOTAL_RECORDS VARCHAR2(40)
        , TOTAL_RECORDS_PRCSD VARCHAR2(40)
        , TOTAL_FAILURE_RECORDS VARCHAR2(40)
        , STATUS VARCHAR2(40)
        , OBJ_ID VARCHAR2(36)
        , VER_NBR NUMBER(8,0)
    

)
/

ALTER TABLE OLE_NG_BAT_JOB_DETAILS_T
    ADD CONSTRAINT OLE_NG_BAT_JOB_DETAILS_TP1
PRIMARY KEY (JOB_DETAIL_ID)
/


CREATE INDEX OLE_NG_BAT_JOB_I 
  ON OLE_NG_BAT_JOB_DETAILS_T 
  (JOB_ID)
/





-- -----------------------------------------------------------------------
-- OLE_GOKB_V
-- -----------------------------------------------------------------------
CREATE OR REPLACE FORCE VIEW OLE_GOKB_V AS 
SELECT TIPP.GOKB_TIPP_ID,TIPP.TIPP_STATUS,TITLE.GOKB_TITLE_ID,TITLE.TITLE_NAME,TITLE.PUBLSHR_ID,TITLE.TI_ISSN_ONLINE,TITLE.TI_ISSN_PRNT,TITLE.TI_ISSN_L,TITLE.MEDIUM,PACKAGE.GOKB_PKG_ID,PACKAGE.PKG_STATUS,PACKAGE.PKG_NAME,PLATFORM.GOKB_PLTFRM_ID,PLATFORM.PLTFRM_NAME,PLATFORM.PLTFRM_STATUS,PLATFORM.SOFTWARE_PLTFRM,ORG.GOKB_ORG_ID,ORG.ORG_NAME FROM  OLE_GOKB_TIPP_T TIPP, OLE_GOKB_PLTFRM_T PLATFORM, OLE_GOKB_ORG_T ORG, OLE_GOKB_TITLE_T TITLE, OLE_GOKB_PKG_T PACKAGE WHERE TIPP.GOKB_PLTFRM_ID=PLATFORM.GOKB_PLTFRM_ID AND TIPP.GOKB_PKG_ID=PACKAGE.GOKB_PKG_ID AND TIPP.GOKB_TITLE_ID=TITLE.GOKB_TITLE_ID AND PLATFORM.PLTFRM_PRVDR_ID=ORG.GOKB_ORG_ID
/

-- -----------------------------------------------------------------------
-- OLE_PTRN_ENTITY_V
-- -----------------------------------------------------------------------
CREATE OR REPLACE FORCE VIEW OLE_PTRN_ENTITY_V AS 
(SELECT     P.OLE_PTRN_ID,     P.BARCODE AS PTRN_BRCD,     P.BORR_TYP AS PTRN_TYP_ID,     ENT.ENTITY_ID,     ENT_NM.FIRST_NM,     ENT_NM.MIDDLE_NM,     ENT_NM.LAST_NM,     ENT_NM.SUFFIX_NM,     ENT_NM.PREFIX_NM,     (SELECT ENT_EM.EMAIL_ADDR FROM KRIM_ENTITY_EMAIL_T ENT_EM WHERE ENT.ENTITY_ID = ENT_EM.ENTITY_ID AND DFLT_IND='Y') AS EMAIL,     (SELECT ENT_PH.PHONE_NBR FROM KRIM_ENTITY_PHONE_T ENT_PH WHERE ENT.ENTITY_ID = ENT_PH.ENTITY_ID AND DFLT_IND='Y') AS PHONE,     concat(concat(ENT_NM.LAST_NM,', '),ENT_NM.FIRST_NM) AS NAME,     P.ACTV_IND AS ACTV_IND     FROM OLE_PTRN_T P,     KRIM_ENTITY_T ENT,     KRIM_ENTITY_NM_T ENT_NM     WHERE     P.OLE_PTRN_ID = ENT.ENTITY_ID     AND     ENT.ENTITY_ID = ENT_NM.ENTITY_ID)




/
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_ACC_LOC_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_ACC_LOC_S'; END IF;
END;
/

CREATE SEQUENCE OLE_ACC_LOC_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_E_RES_ACC_ACT_WRKFLW_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_E_RES_ACC_ACT_WRKFLW_S'; END IF;
END;
/

CREATE SEQUENCE OLE_E_RES_ACC_ACT_WRKFLW_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_E_RES_ACC_ACT_CONFIG_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_E_RES_ACC_ACT_CONFIG_S'; END IF;
END;
/

CREATE SEQUENCE OLE_E_RES_ACC_ACT_CONFIG_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_ACC_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_ACC_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_ACC_TYP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_AGR_DOC_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_AGR_DOC_S'; END IF;
END;
/

CREATE SEQUENCE OLE_AGR_DOC_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_AGR_DOC_TYPE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_AGR_DOC_TYPE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_AGR_DOC_TYPE_S INCREMENT BY 1 START WITH 9 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_AGR_MTH_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_AGR_MTH_S'; END IF;
END;
/

CREATE SEQUENCE OLE_AGR_MTH_S INCREMENT BY 1 START WITH 6 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_AGR_STAT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_AGR_STAT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_AGR_STAT_S INCREMENT BY 1 START WITH 5 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_AGR_TYPE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_AGR_TYPE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_AGR_TYPE_S INCREMENT BY 1 START WITH 6 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_AGREEMENT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_AGREEMENT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_AGREEMENT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_AUTHCAT_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_AUTHCAT_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_AUTHCAT_TYP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BDGT_CD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BDGT_CD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BDGT_CD_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_ACCS_MTHD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_ACCS_MTHD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_ACCS_MTHD_S INCREMENT BY 1 START WITH 8 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_ACQ_MTHD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_ACQ_MTHD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_ACQ_MTHD_S INCREMENT BY 1 START WITH 12 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_ACTION_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_ACTION_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_ACTION_S INCREMENT BY 1 START WITH 21 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_BIB_RECORD_STAT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_BIB_RECORD_STAT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_BIB_RECORD_STAT_S INCREMENT BY 1 START WITH 5 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_CMPLT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_CMPLT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_CMPLT_S INCREMENT BY 1 START WITH 6 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_ELA_RLSHP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_ELA_RLSHP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_ELA_RLSHP_S INCREMENT BY 1 START WITH 6 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_ENCD_LVL_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_ENCD_LVL_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_ENCD_LVL_S INCREMENT BY 1 START WITH 9 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_FLD_ENCD_LVL_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_FLD_ENCD_LVL_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_FLD_ENCD_LVL_S INCREMENT BY 1 START WITH 5 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_GEN_RTN_POL_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_GEN_RTN_POL_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_GEN_RTN_POL_S INCREMENT BY 1 START WITH 10 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_ITM_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_ITM_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_ITM_TYP_S INCREMENT BY 1 START WITH 53 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_LND_POL_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_LND_POL_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_LND_POL_S INCREMENT BY 1 START WITH 6 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_LOC_COUNTRY_CD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_LOC_COUNTRY_CD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_LOC_COUNTRY_CD_S INCREMENT BY 1 START WITH 3 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_NTN_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_NTN_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_NTN_TYP_S INCREMENT BY 1 START WITH 5 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_PRVCY_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_PRVCY_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_PRVCY_S INCREMENT BY 1 START WITH 5 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_RCPT_STAT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_RCPT_STAT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_RCPT_STAT_S INCREMENT BY 1 START WITH 10 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_REC_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_REC_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_REC_TYP_S INCREMENT BY 1 START WITH 5 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_REPRO_POL_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_REPRO_POL_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_REPRO_POL_S INCREMENT BY 1 START WITH 4 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_SHVLG_ORD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_SHVLG_ORD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_SHVLG_ORD_S INCREMENT BY 1 START WITH 5 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_SHVLG_SCHM_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_SHVLG_SCHM_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_SHVLG_SCHM_S INCREMENT BY 1 START WITH 11 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_SPCP_RPT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_SPCP_RPT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_SPCP_RPT_S INCREMENT BY 1 START WITH 3 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_SPFC_RTN_POL_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_SPFC_RTN_POL_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_SPFC_RTN_POL_TYP_S INCREMENT BY 1 START WITH 3 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_SPFC_RTN_POL_UNT_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_SPFC_RTN_POL_UNT_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_SPFC_RTN_POL_UNT_TYP_S INCREMENT BY 1 START WITH 8 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_SRC_TRM_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_SRC_TRM_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_SRC_TRM_S INCREMENT BY 1 START WITH 5 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_STAT_SRCH_CD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_STAT_SRCH_CD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_STAT_SRCH_CD_S INCREMENT BY 1 START WITH 51 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CAT_TYPE_OWNERSHIP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CAT_TYPE_OWNERSHIP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CAT_TYPE_OWNERSHIP_S INCREMENT BY 1 START WITH 4 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CIRC_DSK_DTL_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CIRC_DSK_DTL_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CIRC_DSK_DTL_S INCREMENT BY 1 START WITH 68 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CODE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CODE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CODE_S INCREMENT BY 1 START WITH 4 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CONT_TYPS_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CONT_TYPS_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CONT_TYPS_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CONT_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CONT_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CONT_TYP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CRCL_DSK_LOCN_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CRCL_DSK_LOCN_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CRCL_DSK_LOCN_S INCREMENT BY 1 START WITH 24 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CRCL_DSK_FEE_TYPE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CRCL_DSK_FEE_TYPE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CRCL_DSK_FEE_TYPE_S INCREMENT BY 1 START WITH 24 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CRCL_DSK_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CRCL_DSK_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CRCL_DSK_S INCREMENT BY 1 START WITH 12 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DATAFIELD_ID_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DATAFIELD_ID_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DATAFIELD_ID_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DESC_EXT_DATASRC_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DESC_EXT_DATASRC_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DESC_EXT_DATASRC_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DESC_USER_PREF_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DESC_USER_PREF_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DESC_USER_PREF_S INCREMENT BY 1 START WITH 2 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DISC_EXP_MAP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DISC_EXP_MAP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DISC_EXP_MAP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DISC_EXP_PROFILE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DISC_EXP_PROFILE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DISC_EXP_PROFILE_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_ADDR_SRC_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_ADDR_SRC_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_ADDR_SRC_S INCREMENT BY 1 START WITH 5 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_ADD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_ADD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_ADD_S INCREMENT BY 1 START WITH 73 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_BARCD_STAT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_BARCD_STAT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_BARCD_STAT_S INCREMENT BY 1 START WITH 4 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_BATCH_JOB_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_BATCH_JOB_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_BATCH_JOB_S INCREMENT BY 1 START WITH 9 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_BORR_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_BORR_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_BORR_TYP_S INCREMENT BY 1 START WITH 10 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_CIRC_RECORD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_CIRC_RECORD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_CIRC_RECORD_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_FIXED_DUE_DATE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_FIXED_DUE_DATE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_FIXED_DUE_DATE_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_FXD_DUE_DT_SPAN_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_FXD_DUE_DT_SPAN_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_FXD_DUE_DT_SPAN_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_ITEM_AVAIL_STAT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_ITEM_AVAIL_STAT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_ITEM_AVAIL_STAT_S INCREMENT BY 1 START WITH 16 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_LOAN_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_LOAN_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_LOAN_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_LOAN_STAT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_LOAN_STAT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_LOAN_STAT_S INCREMENT BY 1 START WITH 3 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_LOAN_TERM_UNIT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_LOAN_TERM_UNIT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_LOAN_TERM_UNIT_S INCREMENT BY 1 START WITH 9 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_PTRN_BILL_FEE_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_PTRN_BILL_FEE_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_PTRN_BILL_FEE_TYP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_PTRN_BILL_PAY_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_PTRN_BILL_PAY_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_PTRN_BILL_PAY_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_PTRN_BILL_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_PTRN_BILL_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_PTRN_BILL_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_PTRN_FEE_TYPE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_PTRN_FEE_TYPE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_PTRN_FEE_TYPE_S INCREMENT BY 1 START WITH 5 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_RECENTLY_RETURNED_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_RECENTLY_RETURNED_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_RECENTLY_RETURNED_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_RQST_HSTRY_REC_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_RQST_HSTRY_REC_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_RQST_HSTRY_REC_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_RQST_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_RQST_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_RQST_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_RQST_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_RQST_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_RQST_TYP_S INCREMENT BY 1 START WITH 10 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_SRC_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_SRC_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_SRC_S INCREMENT BY 1 START WITH 5 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_STAT_CAT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_STAT_CAT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_STAT_CAT_S INCREMENT BY 1 START WITH 2 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_TEMP_CIRC_RECORD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_TEMP_CIRC_RECORD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_TEMP_CIRC_RECORD_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_ERES_NTE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_ERES_NTE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_ERES_NTE_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_ERES_REQ_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_ERES_REQ_S'; END IF;
END;
/

CREATE SEQUENCE OLE_ERES_REQ_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_ERES_REQ_SEL_COMM_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_ERES_REQ_SEL_COMM_S'; END IF;
END;
/

CREATE SEQUENCE OLE_ERES_REQ_SEL_COMM_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_ERES_SEL_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_ERES_SEL_S'; END IF;
END;
/

CREATE SEQUENCE OLE_ERES_SEL_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_E_RES_REC_EVNT_LOG_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_E_RES_REC_EVNT_LOG_S'; END IF;
END;
/

CREATE SEQUENCE OLE_E_RES_REC_EVNT_LOG_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_E_RES_REC_INS_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_E_RES_REC_INS_S'; END IF;
END;
/

CREATE SEQUENCE OLE_E_RES_REC_INS_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_E_RES_REC_INV_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_E_RES_REC_INV_S'; END IF;
END;
/

CREATE SEQUENCE OLE_E_RES_REC_INV_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_E_RES_REC_LIC_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_E_RES_REC_LIC_S'; END IF;
END;
/

CREATE SEQUENCE OLE_E_RES_REC_LIC_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_E_RES_REC_PO_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_E_RES_REC_PO_S'; END IF;
END;
/

CREATE SEQUENCE OLE_E_RES_REC_PO_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_LINK_E_RES_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_LINK_E_RES_S'; END IF;
END;
/

CREATE SEQUENCE OLE_LINK_E_RES_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_E_RES_REC_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_E_RES_REC_S'; END IF;
END;
/

CREATE SEQUENCE OLE_E_RES_REC_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_E_RES_STAT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_E_RES_STAT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_E_RES_STAT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_GLBLY_PRCT_FLD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_GLBLY_PRCT_FLD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_GLBLY_PRCT_FLD_S INCREMENT BY 1 START WITH 5 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_LIC_CHK_LST_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_LIC_CHK_LST_S'; END IF;
END;
/

CREATE SEQUENCE OLE_LIC_CHK_LST_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_LIC_DOC_LOCN_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_LIC_DOC_LOCN_S'; END IF;
END;
/

CREATE SEQUENCE OLE_LIC_DOC_LOCN_S INCREMENT BY 1 START WITH 6 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_LIC_EVNT_LOG_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_LIC_EVNT_LOG_S'; END IF;
END;
/

CREATE SEQUENCE OLE_LIC_EVNT_LOG_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_LIC_EVNT_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_LIC_EVNT_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_LIC_EVNT_TYP_S INCREMENT BY 1 START WITH 8 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_LIC_REQS_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_LIC_REQS_S'; END IF;
END;
/

CREATE SEQUENCE OLE_LIC_REQS_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_LIC_REQS_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_LIC_REQS_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_LIC_REQS_TYP_S INCREMENT BY 1 START WITH 4 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_LIC_RQST_ITM_TITL_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_LIC_RQST_ITM_TITL_S'; END IF;
END;
/

CREATE SEQUENCE OLE_LIC_RQST_ITM_TITL_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_LOCN_LEVEL_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_LOCN_LEVEL_S'; END IF;
END;
/

CREATE SEQUENCE OLE_LOCN_LEVEL_S INCREMENT BY 1 START WITH 6 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_LOCN_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_LOCN_S'; END IF;
END;
/

CREATE SEQUENCE OLE_LOCN_S INCREMENT BY 1 START WITH 10000 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_LOCN_SUM_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_LOCN_SUM_S'; END IF;
END;
/

CREATE SEQUENCE OLE_LOCN_SUM_S INCREMENT BY 1 START WITH 4 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_MTRL_TYPS_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_MTRL_TYPS_S'; END IF;
END;
/

CREATE SEQUENCE OLE_MTRL_TYPS_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_MTRL_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_MTRL_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_MTRL_TYP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_OVERLAY_ACTN_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_OVERLAY_ACTN_S'; END IF;
END;
/

CREATE SEQUENCE OLE_OVERLAY_ACTN_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_OVERLAY_LOOKUP_ACTION_ID_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_OVERLAY_LOOKUP_ACTION_ID_S'; END IF;
END;
/

CREATE SEQUENCE OLE_OVERLAY_LOOKUP_ACTION_ID_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_OVERLAY_LOOKUP_ID_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_OVERLAY_LOOKUP_ID_S'; END IF;
END;
/

CREATE SEQUENCE OLE_OVERLAY_LOOKUP_ID_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_OVERLAY_MAP_FLD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_OVERLAY_MAP_FLD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_OVERLAY_MAP_FLD_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_OVERLAY_OPTION_ID_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_OVERLAY_OPTION_ID_S'; END IF;
END;
/

CREATE SEQUENCE OLE_OVERLAY_OPTION_ID_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_OVERLAY_OUT_FLD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_OVERLAY_OUT_FLD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_OVERLAY_OUT_FLD_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_PCKG_SCP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_PCKG_SCP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_PCKG_SCP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_PCKG_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_PCKG_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_PCKG_TYP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_PROFILE_ATTR_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_PROFILE_ATTR_S'; END IF;
END;
/

CREATE SEQUENCE OLE_PROFILE_ATTR_S INCREMENT BY 1 START WITH 35 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_PROFILE_FACT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_PROFILE_FACT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_PROFILE_FACT_S INCREMENT BY 1 START WITH 146 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_PROXY_PTRN_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_PROXY_PTRN_S'; END IF;
END;
/

CREATE SEQUENCE OLE_PROXY_PTRN_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_PTRN_LOCAL_ID_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_PTRN_LOCAL_ID_S'; END IF;
END;
/

CREATE SEQUENCE OLE_PTRN_LOCAL_ID_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_PTRN_LOST_BAR_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_PTRN_LOST_BAR_S'; END IF;
END;
/

CREATE SEQUENCE OLE_PTRN_LOST_BAR_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_PTRN_NTE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_PTRN_NTE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_PTRN_NTE_S INCREMENT BY 1 START WITH 52 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_PTRN_NTE_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_PTRN_NTE_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_PTRN_NTE_TYP_S INCREMENT BY 1 START WITH 6 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_PTRN_PAY_STA_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_PTRN_PAY_STA_S'; END IF;
END;
/

CREATE SEQUENCE OLE_PTRN_PAY_STA_S INCREMENT BY 1 START WITH 20 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_PTRN_SUM_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_PTRN_SUM_S'; END IF;
END;
/

CREATE SEQUENCE OLE_PTRN_SUM_S INCREMENT BY 1 START WITH 2 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_PYMT_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_PYMT_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_PYMT_TYP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_REQ_PRTY_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_REQ_PRTY_S'; END IF;
END;
/

CREATE SEQUENCE OLE_REQ_PRTY_S INCREMENT BY 1 START WITH 9 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_SER_RCV_HIS_SEQ';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_SER_RCV_HIS_SEQ'; END IF;
END;
/

CREATE SEQUENCE OLE_SER_RCV_HIS_SEQ INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_SER_RCV_SEQ';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_SER_RCV_SEQ'; END IF;
END;
/

CREATE SEQUENCE OLE_SER_RCV_SEQ INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_VNDR_ACC_INFO_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_VNDR_ACC_INFO_S'; END IF;
END;
/

CREATE SEQUENCE OLE_VNDR_ACC_INFO_S INCREMENT BY 1 START WITH 19 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_PRF_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_PRF_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_PRF_S INCREMENT BY 1 START WITH 22 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_DT_MAP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_DT_MAP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_DT_MAP_S INCREMENT BY 1 START WITH 10 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_DT_MAP_OPT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_DT_MAP_OPT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_DT_MAP_OPT_S INCREMENT BY 1 START WITH 19 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_FILTER_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_FILTER_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_FILTER_S INCREMENT BY 1 START WITH 5 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_FLE_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_FLE_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_FLE_TYP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_MNTN_FIELD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_MNTN_FIELD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_MNTN_FIELD_S INCREMENT BY 1 START WITH 10 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_PRFLE_CNST_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_PRFLE_CNST_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_PRFLE_CNST_S INCREMENT BY 1 START WITH 66 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_TYP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_PRCT_FLD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_PRCT_FLD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_PRCT_FLD_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRF_BIB_DT_MAP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRF_BIB_DT_MAP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRF_BIB_DT_MAP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRF_BIB_DT_MAP_OVER_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRF_BIB_DT_MAP_OVER_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRF_BIB_DT_MAP_OVER_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_JOB_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_JOB_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_JOB_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_GLBLY_PRCT_FLD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_GLBLY_PRCT_FLD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_GLBLY_PRCT_FLD_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_SCHDULE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_SCHDULE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_SCHDULE_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRF_DEL_FLD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRF_DEL_FLD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRF_DEL_FLD_S INCREMENT BY 1 START WITH 12 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRF_RNM_FLD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRF_RNM_FLD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRF_RNM_FLD_S INCREMENT BY 1 START WITH 12 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_PRF_MTCH_POINT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_PRF_MTCH_POINT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_PRF_MTCH_POINT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_INST_MTCH_PNT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_INST_MTCH_PNT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_INST_MTCH_PNT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_BIB_MTCH_PNT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_BIB_MTCH_PNT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_BIB_MTCH_PNT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_PRCS_BIB_STUS_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_PRCS_BIB_STUS_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_PRCS_BIB_STUS_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_BIB_WRK_UNT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_BIB_WRK_UNT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_BIB_WRK_UNT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_BAT_INST_WRK_UNT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_BAT_INST_WRK_UNT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_BAT_INST_WRK_UNT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CLNDR_EXCP_DAT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CLNDR_EXCP_DAT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CLNDR_EXCP_DAT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CLNDR_EXCP_PRD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CLNDR_EXCP_PRD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CLNDR_EXCP_PRD_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CLNDR_EXCP_PRD_WK_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CLNDR_EXCP_PRD_WK_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CLNDR_EXCP_PRD_WK_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CLNDR_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CLNDR_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CLNDR_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CLNDR_WK_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CLNDR_WK_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CLNDR_WK_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CLNDR_GRP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CLNDR_GRP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CLNDR_GRP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_FRMT_TYPS_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_FRMT_TYPS_S'; END IF;
END;
/

CREATE SEQUENCE OLE_FRMT_TYPS_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_SER_RCV_REC_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_SER_RCV_REC_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_SER_RCV_REC_TYP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CLNDR_EXP_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CLNDR_EXP_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CLNDR_EXP_TYP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CNCL_RSN_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CNCL_RSN_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CNCL_RSN_S INCREMENT BY 1 START WITH 6 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CLAIM_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CLAIM_TYP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CLAIM_TYP_S INCREMENT BY 1 START WITH 14 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_CLM_NTCE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_CLM_NTCE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_CLM_NTCE_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_MISING_PICS_ITEM_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_MISING_PICS_ITEM_S'; END IF;
END;
/

CREATE SEQUENCE OLE_MISING_PICS_ITEM_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_COPY_FORMAT_TYPE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_COPY_FORMAT_TYPE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_COPY_FORMAT_TYPE_S INCREMENT BY 1 START WITH 3 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_ASR_ITM_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_ASR_ITM_S'; END IF;
END;
/

CREATE SEQUENCE OLE_ASR_ITM_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_ASR_RQST_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_ASR_RQST_S'; END IF;
END;
/

CREATE SEQUENCE OLE_ASR_RQST_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DONOR_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DONOR_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DONOR_S INCREMENT BY 1 START WITH 3 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DS_DOC_TYPE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DS_DOC_TYPE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DS_DOC_TYPE_S INCREMENT BY 1 START WITH 10 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DS_DOC_FORMAT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DS_DOC_FORMAT_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DS_DOC_FORMAT_S INCREMENT BY 1 START WITH 20 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DS_DOC_FIELD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DS_DOC_FIELD_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DS_DOC_FIELD_S INCREMENT BY 1 START WITH 1000 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DS_SEARCH_RESULT_PAGE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DS_SEARCH_RESULT_PAGE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DS_SEARCH_RESULT_PAGE_S INCREMENT BY 1 START WITH 20 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DS_SEARCH_FACET_SIZE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DS_SEARCH_FACET_SIZE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DS_SEARCH_FACET_SIZE_S INCREMENT BY 1 START WITH 2 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_LOAN_NOTICE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_LOAN_NOTICE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_LOAN_NOTICE_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_LOAN_NOTICE_HSTRY_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_LOAN_NOTICE_HSTRY_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_LOAN_NOTICE_HSTRY_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_RETURN_HISTORY_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_RETURN_HISTORY_S'; END IF;
END;
/

CREATE SEQUENCE OLE_RETURN_HISTORY_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_VRNT_TTL_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_VRNT_TTL_S'; END IF;
END;
/

CREATE SEQUENCE OLE_VRNT_TTL_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'ACTN_LST_ALRT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE ACTN_LST_ALRT_S'; END IF;
END;
/

CREATE SEQUENCE ACTN_LST_ALRT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'ALRT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE ALRT_S'; END IF;
END;
/

CREATE SEQUENCE ALRT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_E_RES_ACCTG_LN_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_E_RES_ACCTG_LN_S'; END IF;
END;
/

CREATE SEQUENCE OLE_E_RES_ACCTG_LN_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_GOKB_PKG_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_GOKB_PKG_S'; END IF;
END;
/

CREATE SEQUENCE OLE_GOKB_PKG_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_GOKB_TIPP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_GOKB_TIPP_S'; END IF;
END;
/

CREATE SEQUENCE OLE_GOKB_TIPP_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_GOKB_TITLE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_GOKB_TITLE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_GOKB_TITLE_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_GOKB_PLTFRM_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_GOKB_PLTFRM_S'; END IF;
END;
/

CREATE SEQUENCE OLE_GOKB_PLTFRM_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_GOKB_ORG_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_GOKB_ORG_S'; END IF;
END;
/

CREATE SEQUENCE OLE_GOKB_ORG_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_GOKB_UPDATE_LOG_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_GOKB_UPDATE_LOG_S'; END IF;
END;
/

CREATE SEQUENCE OLE_GOKB_UPDATE_LOG_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_GOKB_ORG_ROLE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_GOKB_ORG_ROLE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_GOKB_ORG_ROLE_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_GOKB_REVIEW_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_GOKB_REVIEW_S'; END IF;
END;
/

CREATE SEQUENCE OLE_GOKB_REVIEW_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'GOKB_CHANGE_LOG_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE GOKB_CHANGE_LOG_S'; END IF;
END;
/

CREATE SEQUENCE GOKB_CHANGE_LOG_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_GOKB_ARCHIVE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_GOKB_ARCHIVE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_GOKB_ARCHIVE_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'ALRT_DOC_TYP_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE ALRT_DOC_TYP_S'; END IF;
END;
/

CREATE SEQUENCE ALRT_DOC_TYP_S INCREMENT BY 1 START WITH 71 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_E_RES_ACCESS_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_E_RES_ACCESS_S'; END IF;
END;
/

CREATE SEQUENCE OLE_E_RES_ACCESS_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'ALRT_DOC_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE ALRT_DOC_S'; END IF;
END;
/

CREATE SEQUENCE ALRT_DOC_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'ALRT_CNDTN_NTFCN_INFO_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE ALRT_CNDTN_NTFCN_INFO_S'; END IF;
END;
/

CREATE SEQUENCE ALRT_CNDTN_NTFCN_INFO_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'ALRT_EVENT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE ALRT_EVENT_S'; END IF;
END;
/

CREATE SEQUENCE ALRT_EVENT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'ALRT_EVENT_FIELD_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE ALRT_EVENT_FIELD_S'; END IF;
END;
/

CREATE SEQUENCE ALRT_EVENT_FIELD_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_RNWL_HISTORY_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_RNWL_HISTORY_S'; END IF;
END;
/

CREATE SEQUENCE OLE_RNWL_HISTORY_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DRL_EDITOR_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DRL_EDITOR_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DRL_EDITOR_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DRL_RULE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DRL_RULE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DRL_RULE_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DRL_FINE_LIMITS_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DRL_FINE_LIMITS_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DRL_FINE_LIMITS_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_NOTICE_TYPE_CONFIG_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_NOTICE_TYPE_CONFIG_S'; END IF;
END;
/

CREATE SEQUENCE OLE_NOTICE_TYPE_CONFIG_S INCREMENT BY 1 START WITH 7 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_NOTC_FIELD_LABEL_MAPNG_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_NOTC_FIELD_LABEL_MAPNG_S'; END IF;
END;
/

CREATE SEQUENCE OLE_NOTC_FIELD_LABEL_MAPNG_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_NOTC_CNTNT_CONFIG_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_NOTC_CNTNT_CONFIG_S'; END IF;
END;
/

CREATE SEQUENCE OLE_NOTC_CNTNT_CONFIG_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_PHONE_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_PHONE_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_PHONE_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_DLVR_EMAIL_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_DLVR_EMAIL_S'; END IF;
END;
/

CREATE SEQUENCE OLE_DLVR_EMAIL_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'ITEM_AUDIT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE ITEM_AUDIT_S'; END IF;
END;
/

CREATE SEQUENCE ITEM_AUDIT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'BIB_AUDIT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE BIB_AUDIT_S'; END IF;
END;
/

CREATE SEQUENCE BIB_AUDIT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'HOLDINGS_AUDIT_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE HOLDINGS_AUDIT_S'; END IF;
END;
/

CREATE SEQUENCE HOLDINGS_AUDIT_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_NG_BAT_PRF_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_NG_BAT_PRF_S'; END IF;
END;
/

CREATE SEQUENCE OLE_NG_BAT_PRF_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_NG_BAT_PRCS_JOB_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_NG_BAT_PRCS_JOB_S'; END IF;
END;
/

CREATE SEQUENCE OLE_NG_BAT_PRCS_JOB_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'OLE_NG_BAT_JOB_DETAILS_S';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE OLE_NG_BAT_JOB_DETAILS_S'; END IF;
END;
/

CREATE SEQUENCE OLE_NG_BAT_JOB_DETAILS_S INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

