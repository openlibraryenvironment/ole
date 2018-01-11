
# -----------------------------------------------------------------------
# OLE_ACC_LOC_T
# -----------------------------------------------------------------------
drop table if exists OLE_ACC_LOC_T
/

CREATE TABLE OLE_ACC_LOC_T
(
      ACC_LOC_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , ACC_LOC_NM VARCHAR(40)
        , ACC_LOC_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
    
    , CONSTRAINT OLE_ACC_LOC_TP1 PRIMARY KEY(ACC_LOC_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_E_RES_ACC_ACT_CONFIG_T
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_ACC_ACT_CONFIG_T
/

CREATE TABLE OLE_E_RES_ACC_ACT_CONFIG_T
(
      ACC_ACT_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , WRK_FLW_NAME VARCHAR(100)
        , WRK_FLW_TYPE VARCHAR(40)
        , WRK_FLW_STATUS VARCHAR(100)
        , RCPNT_USER_ID VARCHAR(100)
        , RCPNT_ROLE_ID VARCHAR(100)
        , MAIL_ID VARCHAR(100)
        , MAIL_CNTNT VARCHAR(4000)
        , MAIL_NOTFCTN VARCHAR(1) default 'Y'
        , ROW_ACT_IND VARCHAR(1) default 'Y'
    
    , CONSTRAINT OLE_E_RES_ACC_ACT_CONFIG_TP1 PRIMARY KEY(ACC_ACT_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_E_RES_ACC_ACT_WRKFLW_T
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_ACC_ACT_WRKFLW_T
/

CREATE TABLE OLE_E_RES_ACC_ACT_WRKFLW_T
(
      ACCESS_ID VARCHAR(10)
        , ORDER_NO INTEGER(10)
        , STATUS VARCHAR(100)
        , ACC_ACT_ID VARCHAR(30)
        , ROLE_ID VARCHAR(100)
        , PRSN_ID VARCHAR(100)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
    
    , CONSTRAINT OLE_E_RES_ACC_ACT_WRKFLW_TP1 PRIMARY KEY(ACCESS_ID)





    
                                                                                                                                                                                                                                                        
                                    
, INDEX OLE_ACC_ACT_CONFIG_FK (ACC_ACT_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_ACC_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_ACC_TYP_T
/

CREATE TABLE OLE_ACC_TYP_T
(
      ACC_TYP_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , ACC_TYP_NM VARCHAR(40)
        , ACC_TYP_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
    
    , CONSTRAINT OLE_ACC_TYP_TP1 PRIMARY KEY(ACC_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_AGR_DOC_T
# -----------------------------------------------------------------------
drop table if exists OLE_AGR_DOC_T
/

CREATE TABLE OLE_AGR_DOC_T
(
      OLE_AGR_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , LIC_REQS_ID VARCHAR(40) NOT NULL
        , OLE_AGR_DOC_ID VARCHAR(100)
    
    , CONSTRAINT OLE_AGR_DOC_TP1 PRIMARY KEY(OLE_AGR_ID)





    
                                                                                                                                                                                            
                                    
, INDEX LIC_REQS_FK (LIC_REQS_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_AGR_DOC_TYPE_T
# -----------------------------------------------------------------------
drop table if exists OLE_AGR_DOC_TYPE_T
/

CREATE TABLE OLE_AGR_DOC_TYPE_T
(
      AGR_DOC_TYPE_ID VARCHAR(40) default '0'
        , AGR_DOC_TYPE_NM VARCHAR(100) NOT NULL
        , AGR_DOC_TYPE_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_AGR_DOC_TYPE_TP1 PRIMARY KEY(AGR_DOC_TYPE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_AGR_MTH_T
# -----------------------------------------------------------------------
drop table if exists OLE_AGR_MTH_T
/

CREATE TABLE OLE_AGR_MTH_T
(
      AGR_MTH_ID VARCHAR(40) default '0'
        , AGR_MTH_NM VARCHAR(100) NOT NULL
        , AGR_MTH_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_AGR_MTH_TP1 PRIMARY KEY(AGR_MTH_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_AGR_STAT_T
# -----------------------------------------------------------------------
drop table if exists OLE_AGR_STAT_T
/

CREATE TABLE OLE_AGR_STAT_T
(
      AGR_STAT_ID VARCHAR(40) default '0'
        , AGR_STAT_NM VARCHAR(100) NOT NULL
        , AGR_STAT_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_AGR_STAT_TP1 PRIMARY KEY(AGR_STAT_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_AGR_TYPE_T
# -----------------------------------------------------------------------
drop table if exists OLE_AGR_TYPE_T
/

CREATE TABLE OLE_AGR_TYPE_T
(
      AGR_TYPE_ID VARCHAR(40) default '0'
        , AGR_TYPE_NM VARCHAR(100) NOT NULL
        , AGR_TYPE_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_AGR_TYPE_TP1 PRIMARY KEY(AGR_TYPE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_AGREEMENT_T
# -----------------------------------------------------------------------
drop table if exists OLE_AGREEMENT_T
/

CREATE TABLE OLE_AGREEMENT_T
(
      AGR_ID VARCHAR(40)
        , AGR_LICENCE_TITLE VARCHAR(250)
        , AGR_CONTRACT_NBR VARCHAR(20)
        , PO_ID INTEGER
        , AGR_STATUS_ID VARCHAR(40)
        , AGR_CODINGD_STATUS_ID VARCHAR(40)
        , AGR_LICENSOR_ID VARCHAR(40)
        , AGR_LICENSEE_ID VARCHAR(40)
        , AGR_GENERAL_NTE VARCHAR(250)
        , AGR_LICENSE_SITE VARCHAR(250)
        , AGR_FEE_SCHDLE VARCHAR(250)
        , AGR_INFLATION_CAP VARCHAR(250)
        , AGR_PAYMENT_TERM VARCHAR(250)
        , AGR_GOVRNING_LAW VARCHAR(250)
        , AGR_NEW_TITLE_ACCESS VARCHAR(250)
        , AGR_CANCELLATION_RIGHTS VARCHAR(250)
        , AGR_ADDITIONAL_TERM VARCHAR(250)
        , AGR_NTC_PER_FOR_TERMN VARCHAR(250)
        , AGR_PERP_ACCESS VARCHAR(1)
        , AGR_PERP_ACC_NTE VARCHAR(250)
        , AGR_AUTH_USRS VARCHAR(250)
        , AGR_AUTH_USRS_GEN_NTE VARCHAR(250)
        , AGR_DEP_IN_JR VARCHAR(1)
        , AGR_FAIR_USE VARCHAR(1)
        , AGR_RTS_NT_GR_DE VARCHAR(1)
        , AGR_ILL_PR VARCHAR(1)
        , AGR_ILL_EL VARCHAR(1)
        , AGR_ILL_LON_DOC_PERM VARCHAR(1)
        , AGR_ILL_NON_PRFT_ONLY VARCHAR(1)
        , AGR_ILL_SAM_CNTRY_RES VARCHAR(1)
        , AGR_ILL_NTE VARCHAR(250)
        , AGR_LIB_RES_ELEC VARCHAR(1)
        , AGR_LIB_RES_PRI VARCHAR(1)
        , AGR_LIB_RES_CMS_NTE VARCHAR(250)
        , AGR_SCOH_SHA VARCHAR(1)
        , AGR_TXT_MIN VARCHAR(1)
        , AGR_PERM_RIGHTS VARCHAR(1)
        , AGR_STR_RIGHTS VARCHAR(1)
        , AGR_MUL_RIGHTS_NTE VARCHAR(250)
        , AGR_APC_OFFST VARCHAR(1)
        , AGR_APC_OFFST_NTE VARCHAR(250)
        , AGR_START_DT DATETIME
        , AGR_END_DT DATETIME
    
    , CONSTRAINT OLE_AGREEMENT_TP1 PRIMARY KEY(AGR_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_AUTHCAT_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_AUTHCAT_TYP_T
/

CREATE TABLE OLE_AUTHCAT_TYP_T
(
      AUTHCAT_TYP_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , AUTHCAT_TYP_NM VARCHAR(40)
        , AUTHCAT_TYP_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
    
    , CONSTRAINT OLE_AUTHCAT_TYP_TP1 PRIMARY KEY(AUTHCAT_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BDGT_CD_T
# -----------------------------------------------------------------------
drop table if exists OLE_BDGT_CD_T
/

CREATE TABLE OLE_BDGT_CD_T
(
      BDGT_CD_ID VARCHAR(40) default '0'
        , INPT_VAL VARCHAR(100) NOT NULL
        , CHRT_CD VARCHAR(100) NOT NULL
        , FUND_CD VARCHAR(100) NOT NULL
        , OBJT_CD VARCHAR(100) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y'
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_BDGT_CD_TP1 PRIMARY KEY(BDGT_CD_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CALL_NMBR_T
# -----------------------------------------------------------------------
drop table if exists OLE_CALL_NMBR_T
/

CREATE TABLE OLE_CALL_NMBR_T
(
      INPUT_VAL VARCHAR(40) default '0'
        , PROFILE_ID VARCHAR(100) NOT NULL
        , CALL_NUM_PREF_ONE VARCHAR(100)
        , CALL_NUM_PREF_TWO VARCHAR(100)
        , CALL_NUM_PREF_THREE VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_CALL_NMBR_TP1 PRIMARY KEY(INPUT_VAL)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_ACCS_MTHD_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_ACCS_MTHD_T
/

CREATE TABLE OLE_CAT_ACCS_MTHD_T
(
      ACCS_MTHD_ID DECIMAL(8) default 0
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , ACCS_MTHD_CD VARCHAR(40)
        , ACCS_MTHD_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_CAT_ACCS_MTHD_TP1 PRIMARY KEY(ACCS_MTHD_ID)

    , CONSTRAINT ACCS_MTHD_CD UNIQUE (ACCS_MTHD_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_ACQ_MTHD_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_ACQ_MTHD_T
/

CREATE TABLE OLE_CAT_ACQ_MTHD_T
(
      ACQ_MTHD_ID DECIMAL(8) default 0
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , ACQ_MTHD_CD VARCHAR(40)
        , ACQ_MTHD_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_CAT_ACQ_MTHD_TP1 PRIMARY KEY(ACQ_MTHD_ID)

    , CONSTRAINT ACQ_MTHD_CD UNIQUE (ACQ_MTHD_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_ACTION_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_ACTION_T
/

CREATE TABLE OLE_CAT_ACTION_T
(
      ACTION_ID DECIMAL(8) default 0
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , ACTION_CD VARCHAR(40) NOT NULL
        , ACTION_NM VARCHAR(100) NOT NULL
        , ACTION_DESC VARCHAR(100)
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_CAT_ACTION_TP1 PRIMARY KEY(ACTION_ID)

    , CONSTRAINT ACTION_CD UNIQUE (ACTION_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_BIB_RECORD_STAT_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_BIB_RECORD_STAT_T
/

CREATE TABLE OLE_CAT_BIB_RECORD_STAT_T
(
      BIB_RECORD_STAT_ID VARCHAR(40) default '0'
        , BIB_RECORD_STAT_CD VARCHAR(40) NOT NULL
        , BIB_RECORD_STAT_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_CAT_BIB_RECORD_STAT_TP1 PRIMARY KEY(BIB_RECORD_STAT_ID)

    , CONSTRAINT BIB_RECORD_STAT_CD UNIQUE (BIB_RECORD_STAT_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_CMPLT_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_CMPLT_T
/

CREATE TABLE OLE_CAT_CMPLT_T
(
      CMPLT_ID DECIMAL(8) default 0
        , CMPLT_CD VARCHAR(40) NOT NULL
        , CMPLT_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(700) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ROW_ACT_IND VARCHAR(1) NOT NULL
        , SRC_DT DATETIME NOT NULL
    
    , CONSTRAINT OLE_CAT_CMPLT_TP1 PRIMARY KEY(CMPLT_ID)

    , CONSTRAINT CMPLT_CD UNIQUE (CMPLT_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_ELA_RLSHP_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_ELA_RLSHP_T
/

CREATE TABLE OLE_CAT_ELA_RLSHP_T
(
      ELE_RLSHP_ID DECIMAL(8) default 0
        , ELE_RLSHP_CD VARCHAR(40)
        , ELE_RLSHP_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(700) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ROW_ACT_IND VARCHAR(1) NOT NULL
        , SRC_DT DATETIME NOT NULL
    
    , CONSTRAINT OLE_CAT_ELA_RLSHP_TP1 PRIMARY KEY(ELE_RLSHP_ID)

    , CONSTRAINT ELE_RLSHP_CD UNIQUE (ELE_RLSHP_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_ENCD_LVL_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_ENCD_LVL_T
/

CREATE TABLE OLE_CAT_ENCD_LVL_T
(
      ENCD_LVL_ID DECIMAL(8) default 0
        , ENCD_LVL_CD VARCHAR(40) NOT NULL
        , ENCD_LVL_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(700) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ROW_ACT_IND VARCHAR(1) NOT NULL
        , SRC_DT DATETIME NOT NULL
    
    , CONSTRAINT OLE_CAT_ENCD_LVL_TP1 PRIMARY KEY(ENCD_LVL_ID)

    , CONSTRAINT ENCD_LVL_CD UNIQUE (ENCD_LVL_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_FLD_ENCD_LVL_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_FLD_ENCD_LVL_T
/

CREATE TABLE OLE_CAT_FLD_ENCD_LVL_T
(
      FLD_ENCD_LVL_ID DECIMAL(8) default 0
        , FLD_ENCD_LVL_CD VARCHAR(40)
        , FLD_ENCD_LVL_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(700) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ROW_ACT_IND VARCHAR(1) NOT NULL
        , SRC_DT DATETIME NOT NULL
    
    , CONSTRAINT OLE_CAT_FLD_ENCD_LVL_TP1 PRIMARY KEY(FLD_ENCD_LVL_ID)

    , CONSTRAINT FLD_ENCD_LVL_CD UNIQUE (FLD_ENCD_LVL_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_GEN_RTN_POL_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_GEN_RTN_POL_T
/

CREATE TABLE OLE_CAT_GEN_RTN_POL_T
(
      GEN_RTN_POL_ID DECIMAL(8) default 0
        , GEN_RTN_POL_CD VARCHAR(40) NOT NULL
        , GEN_RTN_POL_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(700) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ROW_ACT_IND VARCHAR(1) NOT NULL
        , SRC_DT DATETIME NOT NULL
    
    , CONSTRAINT OLE_CAT_GEN_RTN_POL_TP1 PRIMARY KEY(GEN_RTN_POL_ID)

    , CONSTRAINT GEN_RTN_POL_CD UNIQUE (GEN_RTN_POL_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_ITM_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_ITM_TYP_T
/

CREATE TABLE OLE_CAT_ITM_TYP_T
(
      ITM_TYP_CD_ID VARCHAR(40) default '0'
        , ITM_TYP_CD VARCHAR(40) NOT NULL
        , ITM_TYP_NM VARCHAR(100) NOT NULL
        , ITM_TYP_DESC VARCHAR(700)
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_CAT_ITM_TYP_TP1 PRIMARY KEY(ITM_TYP_CD_ID)

    , CONSTRAINT ITM_TYP_CD UNIQUE (ITM_TYP_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_LND_POL_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_LND_POL_T
/

CREATE TABLE OLE_CAT_LND_POL_T
(
      LND_POL_ID DECIMAL(8) default 0
        , LND_POL_CD VARCHAR(30) NOT NULL
        , LND_POL_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_CAT_LND_POL_TP1 PRIMARY KEY(LND_POL_ID)

    , CONSTRAINT LND_POL_CD UNIQUE (LND_POL_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_LOC_COUNTRY_CD_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_LOC_COUNTRY_CD_T
/

CREATE TABLE OLE_CAT_LOC_COUNTRY_CD_T
(
      LOC_COUNTRY_CD_ID DECIMAL(8) default 0
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , LOC_COUNTRY_CD VARCHAR(40) NOT NULL
        , LOC_COUNTRY_NM VARCHAR(100) NOT NULL
        , LOC_COUNTRY_REGION_NM VARCHAR(100) NOT NULL
        , LOC_COUNTRY_SEQUENCE_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_CAT_LOC_COUNTRY_CD_TP1 PRIMARY KEY(LOC_COUNTRY_CD_ID)

    , CONSTRAINT LOC_COUNTRY_CD UNIQUE (LOC_COUNTRY_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_NTN_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_NTN_TYP_T
/

CREATE TABLE OLE_CAT_NTN_TYP_T
(
      NTN_TYP_ID VARCHAR(40) default '0'
        , NTN_TYP_CD VARCHAR(40) NOT NULL
        , NTN_TYP_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_CAT_NTN_TYP_TP1 PRIMARY KEY(NTN_TYP_ID)

    , CONSTRAINT NTN_TYP_CD UNIQUE (NTN_TYP_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_PRVCY_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_PRVCY_T
/

CREATE TABLE OLE_CAT_PRVCY_T
(
      PRVCY_ID VARCHAR(40) default '0'
        , PRVCY_CD VARCHAR(40)
        , PRVCY_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_CAT_PRVCY_TP1 PRIMARY KEY(PRVCY_ID)

    , CONSTRAINT PRVCY_CD UNIQUE (PRVCY_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_RCPT_STAT_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_RCPT_STAT_T
/

CREATE TABLE OLE_CAT_RCPT_STAT_T
(
      RCPT_STAT_ID VARCHAR(40) default '0'
        , RCPT_STAT_CD VARCHAR(40) NOT NULL
        , RCPT_STAT_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_CAT_RCPT_STAT_TP1 PRIMARY KEY(RCPT_STAT_ID)

    , CONSTRAINT RCPT_STAT_CD UNIQUE (RCPT_STAT_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_REC_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_REC_TYP_T
/

CREATE TABLE OLE_CAT_REC_TYP_T
(
      REC_TYP_ID DECIMAL(8) default 0
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , REC_TYP_CD VARCHAR(40) NOT NULL
        , REC_TYP_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_CAT_REC_TYP_TP1 PRIMARY KEY(REC_TYP_ID)

    , CONSTRAINT REC_TYP_CD UNIQUE (REC_TYP_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_REPRO_POL_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_REPRO_POL_T
/

CREATE TABLE OLE_CAT_REPRO_POL_T
(
      REPRO_POL_ID DECIMAL(8) default 0
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , REPRO_POL_CD VARCHAR(40) NOT NULL
        , REPRO_POL_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_CAT_REPRO_POL_TP1 PRIMARY KEY(REPRO_POL_ID)

    , CONSTRAINT REPRO_POL_CD UNIQUE (REPRO_POL_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_SHVLG_ORD_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_SHVLG_ORD_T
/

CREATE TABLE OLE_CAT_SHVLG_ORD_T
(
      SHVLG_ORD_ID DECIMAL(8) default 0
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , SHVLG_ORD_CD VARCHAR(40)
        , SHVLG_ORD_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_CAT_SHVLG_ORD_TP1 PRIMARY KEY(SHVLG_ORD_ID)

    , CONSTRAINT SHVLG_ORD_CD UNIQUE (SHVLG_ORD_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_SHVLG_SCHM_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_SHVLG_SCHM_T
/

CREATE TABLE OLE_CAT_SHVLG_SCHM_T
(
      SHVLG_SCHM_ID DECIMAL(8) default 0
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , SHVLG_SCHM_CD VARCHAR(40)
        , SHVLG_SCHM_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_CAT_SHVLG_SCHM_TP1 PRIMARY KEY(SHVLG_SCHM_ID)

    , CONSTRAINT SHVLG_SCHM_CD UNIQUE (SHVLG_SCHM_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_SPCP_RPT_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_SPCP_RPT_T
/

CREATE TABLE OLE_CAT_SPCP_RPT_T
(
      SPCP_RPT_ID DECIMAL(8) default 0
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , SPCP_RPT_CD VARCHAR(40) NOT NULL
        , SPCP_RPT_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_CAT_SPCP_RPT_TP1 PRIMARY KEY(SPCP_RPT_ID)

    , CONSTRAINT SPCP_RPT_CD UNIQUE (SPCP_RPT_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_SPFC_RTN_POL_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_SPFC_RTN_POL_TYP_T
/

CREATE TABLE OLE_CAT_SPFC_RTN_POL_TYP_T
(
      SPFC_RTN_POL_TYP_ID DECIMAL(8) default 0
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , SPFC_RTN_POL_TYP_CD VARCHAR(40) NOT NULL
        , SPFC_RTN_POL_TYP_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_CAT_SPFC_RTN_POL_TYP_TP1 PRIMARY KEY(SPFC_RTN_POL_TYP_ID)

    , CONSTRAINT SPFC_RTN_POL_TYP_CD UNIQUE (SPFC_RTN_POL_TYP_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_SPFC_RTN_POL_UNT_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_SPFC_RTN_POL_UNT_TYP_T
/

CREATE TABLE OLE_CAT_SPFC_RTN_POL_UNT_TYP_T
(
      SPFC_RTN_POL_UNT_TYP_ID VARCHAR(40) default '0'
        , SPFC_RTN_POL_UNT_TYP_CD VARCHAR(40) NOT NULL
        , SPFC_RTN_POL_UNT_TYP_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_CAT_SPFC_RTN_POL_UNT_TYP1 PRIMARY KEY(SPFC_RTN_POL_UNT_TYP_ID)

    , CONSTRAINT SPFC_RTN_POL_UNT_TYP_CD UNIQUE (SPFC_RTN_POL_UNT_TYP_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_SRC_TRM_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_SRC_TRM_T
/

CREATE TABLE OLE_CAT_SRC_TRM_T
(
      SRC_TRM_ID VARCHAR(40) default '0'
        , SRC_TRM_CD VARCHAR(40) NOT NULL
        , SRC_TRM_NM VARCHAR(501) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_CAT_SRC_TRM_TP1 PRIMARY KEY(SRC_TRM_ID)

    , CONSTRAINT SRC_TRM_CD UNIQUE (SRC_TRM_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_STAT_SRCH_CD_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_STAT_SRCH_CD_T
/

CREATE TABLE OLE_CAT_STAT_SRCH_CD_T
(
      STAT_SRCH_CD_ID DECIMAL(8) default 0
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , STAT_SRCH_CD VARCHAR(40) NOT NULL
        , STAT_SRCH_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , DATE_UPDATED DATETIME
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_CAT_STAT_SRCH_CD_TP1 PRIMARY KEY(STAT_SRCH_CD_ID)

    , CONSTRAINT STAT_SRCH_CD UNIQUE (STAT_SRCH_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CAT_TYPE_OWNERSHIP_T
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_TYPE_OWNERSHIP_T
/

CREATE TABLE OLE_CAT_TYPE_OWNERSHIP_T
(
      TYPE_OWNERSHIP_ID DECIMAL(8) default 0
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , TYPE_OWNERSHIP_CD VARCHAR(100) NOT NULL
        , TYPE_OWNERSHIP_NM VARCHAR(100) NOT NULL
        , SRC VARCHAR(100) NOT NULL
        , SRC_DT DATETIME NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_CAT_TYPE_OWNERSHIP_TP1 PRIMARY KEY(TYPE_OWNERSHIP_ID)

    , CONSTRAINT TYPE_OWNERSHIP_CD UNIQUE (TYPE_OWNERSHIP_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CIRC_DSK_DTL_T
# -----------------------------------------------------------------------
drop table if exists OLE_CIRC_DSK_DTL_T
/

CREATE TABLE OLE_CIRC_DSK_DTL_T
(
      CRCL_DSK_DTL_ID VARCHAR(40) default '0'
        , OPTR_ID VARCHAR(80)
        , DEFAULT_LOC VARCHAR(1) NOT NULL
        , ALLOWED_LOC VARCHAR(1) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , CRCL_DSK_ID VARCHAR(40)
    
    , CONSTRAINT OLE_CIRC_DSK_DTL_TP1 PRIMARY KEY(CRCL_DSK_DTL_ID)





    
                                                                                                                                                                                                                                    
                                    
, INDEX CRCL_ID_constr (CRCL_DSK_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CODE_T
# -----------------------------------------------------------------------
drop table if exists OLE_CODE_T
/

CREATE TABLE OLE_CODE_T
(
      CD_ID VARCHAR(40) default '0' NOT NULL
        , PROFILE_ID VARCHAR(100)
        , INPUT_VAL VARCHAR(100)
        , ITM_TYP VARCHAR(100) NOT NULL
        , ITM_STAT_CD VARCHAR(100) NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_CODE_TP1 PRIMARY KEY(PROFILE_ID,INPUT_VAL)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CONT_TYPS_T
# -----------------------------------------------------------------------
drop table if exists OLE_CONT_TYPS_T
/

CREATE TABLE OLE_CONT_TYPS_T
(
      CONT_TYPS_ID VARCHAR(10)
        , E_RES_REC_ID VARCHAR(10)
        , CONT_TYP_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_CONT_TYPS_TP1 PRIMARY KEY(CONT_TYPS_ID)





    
                                                                                                                                                                                            
                                    
, INDEX OLE_CONT_TYPS_FK (CONT_TYP_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CONT_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_CONT_TYP_T
/

CREATE TABLE OLE_CONT_TYP_T
(
      CONT_TYP_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , CONT_TYP_NM VARCHAR(40)
        , CONT_TYP_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
    
    , CONSTRAINT OLE_CONT_TYP_TP1 PRIMARY KEY(CONT_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CRCL_DSK_LOCN_T
# -----------------------------------------------------------------------
drop table if exists OLE_CRCL_DSK_LOCN_T
/

CREATE TABLE OLE_CRCL_DSK_LOCN_T
(
      OLE_CRCL_DSK_LOCN_ID VARCHAR(40) default '0'
        , OLE_CRCL_DSK_ID VARCHAR(40) NOT NULL
        , OLE_CRCL_DSK_LOCN VARCHAR(40) NOT NULL
        , OLE_CRCL_PICKUP_DSK_LOCN VARCHAR(40)
        , LOCN_POPUP VARCHAR(1) default 'N'
        , LOCN_POPUP_MSG VARCHAR(4000)
    
    , CONSTRAINT OLE_CRCL_DSK_LOCN_TP1 PRIMARY KEY(OLE_CRCL_DSK_LOCN_ID)





    
                                                                                                                                                                                                                
                                    
, INDEX OLE_CRCL_DSK_LOCN_constr (OLE_CRCL_DSK_LOCN )
    
                                                                                                                                                                                                                
                                    
, INDEX OLE_CRCL_LOCN_FK (OLE_CRCL_DSK_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CRCL_DSK_FEE_TYPE_T
# -----------------------------------------------------------------------
drop table if exists OLE_CRCL_DSK_FEE_TYPE_T
/

CREATE TABLE OLE_CRCL_DSK_FEE_TYPE_T
(
      OLE_CRCL_DSK_FEE_TYPE_ID VARCHAR(40) default '0'
        , OLE_CRCL_DSK_ID VARCHAR(40) NOT NULL
        , FEE_TYP_ID VARCHAR(40) NOT NULL
    
    , CONSTRAINT OLE_CRCL_DSK_FEE_TYPE_TP1 PRIMARY KEY(OLE_CRCL_DSK_FEE_TYPE_ID)





    
                                                                                                                                                    
                                    
, INDEX OLE_CRCL_FEE_TYPE_I (FEE_TYP_ID )
    
                                                                                                                                                    
                                    
, INDEX OLE_CRCL_DSK_I (OLE_CRCL_DSK_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CRCL_DSK_T
# -----------------------------------------------------------------------
drop table if exists OLE_CRCL_DSK_T
/

CREATE TABLE OLE_CRCL_DSK_T
(
      OLE_CRCL_DSK_CODE VARCHAR(40) NOT NULL
        , OLE_CRCL_DSK_PUB_NAME VARCHAR(100) NOT NULL
        , OLE_CRCL_DSK_STAFF_NAME VARCHAR(100) NOT NULL
        , ACTV_IND VARCHAR(1) default 'Y' NOT NULL
        , PK_UP_LOCN_IND VARCHAR(1) NOT NULL
        , ASR_PK_UP_LOCN_IND VARCHAR(1) NOT NULL
        , HLD_DAYS DECIMAL(8)
        , RQST_EXPIRTIN_DAYS DECIMAL(8)
        , SLVNG_LAG_TIM DECIMAL(8)
        , PRNT_SLP_IND VARCHAR(1) NOT NULL
        , OLE_CRCL_DSK_ID VARCHAR(40)
        , OLE_CLNDR_GRP_ID VARCHAR(40)
        , HOLD_FORMAT VARCHAR(40)
        , HOLD_QUEUE VARCHAR(1) NOT NULL
        , STAFFED VARCHAR(1) default 'Y' NOT NULL
        , REPLY_TO_EMAIL VARCHAR(100)
        , RENEW_LOST_ITM VARCHAR(1)
        , SHOW_ONHOLD_ITM VARCHAR(50) default 'CurrentCirculationDesk'
        , DFLT_RQST_TYP_ID VARCHAR(40)
        , DFLT_PICK_UP_LOCN_ID VARCHAR(40)
        , FROM_EMAIL VARCHAR(100)
    
    , CONSTRAINT OLE_CRCL_DSK_TP1 PRIMARY KEY(OLE_CRCL_DSK_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DATAFIELD_T
# -----------------------------------------------------------------------
drop table if exists OLE_DATAFIELD_T
/

CREATE TABLE OLE_DATAFIELD_T
(
      ID INTEGER
        , OVERLAY_OPTION_ID INTEGER NOT NULL
        , AGENDA_NAME VARCHAR(100)
        , DATAFIELD_TAG VARCHAR(10)
        , DATAFIELD_IND1 VARCHAR(10)
        , DATAFIELD_IND2 VARCHAR(10)
        , SUBFIELD_CODE VARCHAR(10)
    
    , CONSTRAINT OLE_DATAFIELD_TP1 PRIMARY KEY(ID)





    
                                                                                                                                                                                                
                                    
, INDEX OVERLAY_OPTION_FK (OVERLAY_OPTION_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DESC_EXT_DATASRC_T
# -----------------------------------------------------------------------
drop table if exists OLE_DESC_EXT_DATASRC_T
/

CREATE TABLE OLE_DESC_EXT_DATASRC_T
(
      DS_ID DECIMAL(8) default 0
        , DS_NAME VARCHAR(40) NOT NULL
        , DS_DESC VARCHAR(100)
        , DOMAIN_NAME VARCHAR(40)
        , PORT_NUM VARCHAR(40)
        , LOGIN_ID VARCHAR(40)
        , AUTH_KEY VARCHAR(40)
        , PWD VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_DESC_EXT_DATASRC_TP1 PRIMARY KEY(DS_ID)

    , CONSTRAINT DS_NAME UNIQUE (DS_NAME)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DESC_USER_PREF_T
# -----------------------------------------------------------------------
drop table if exists OLE_DESC_USER_PREF_T
/

CREATE TABLE OLE_DESC_USER_PREF_T
(
      PREF_ID INTEGER default 0
        , USER_ID VARCHAR(40) NOT NULL
        , USER_ROLE VARCHAR(40) NOT NULL
        , PREF_NAME VARCHAR(100) NOT NULL
        , IMPORT_TYPE VARCHAR(40)
        , IMPORT_STATUS VARCHAR(40)
        , PERM_LOC VARCHAR(40)
        , TEMP_LOC VARCHAR(40)
        , REMOVAL_TAGS VARCHAR(40)
        , PROTECTED_TAGS VARCHAR(40)
        , CLASSIFICATION_SCHEME VARCHAR(40)
        , CALL_NUM_1 VARCHAR(40)
        , CALL_NUM_2 VARCHAR(40)
        , CALL_NUM_3 VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_DESC_USER_PREF_TP1 PRIMARY KEY(PREF_ID)

    , CONSTRAINT PREF_NAME UNIQUE (PREF_NAME)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DISC_EXP_MAP_T
# -----------------------------------------------------------------------
drop table if exists OLE_DISC_EXP_MAP_T
/

CREATE TABLE OLE_DISC_EXP_MAP_T
(
      MAP_FIELD_ID VARCHAR(40)
        , MARC_FLD VARCHAR(100) NOT NULL
        , ITEM_FLD VARCHAR(100) NOT NULL
        , EXP_ID VARCHAR(40) NOT NULL
        , MAP_DESC VARCHAR(100) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_DISC_EXP_MAP_TP1 PRIMARY KEY(MAP_FIELD_ID)





    
                                                                                                                                                                                                                                    
                                    
, INDEX OLE_DISC_EXP_MAP_CNSTR (EXP_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DISC_EXP_PROFILE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DISC_EXP_PROFILE_T
/

CREATE TABLE OLE_DISC_EXP_PROFILE_T
(
      EXP_ID VARCHAR(40)
        , EXP_PROF_CD VARCHAR(100) NOT NULL
        , EXP_PROF_NM VARCHAR(100) NOT NULL
        , EXP_FORMAT VARCHAR(100) NOT NULL
        , EXP_TYPE VARCHAR(100) NOT NULL
        , EXP_TO VARCHAR(500) NOT NULL
        , NO_OF_THREADS INTEGER default 0 NOT NULL
        , NO_OF_RECORDS INTEGER default 0 NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , DATA_FIELD VARCHAR(100) NOT NULL
    
    , CONSTRAINT OLE_DISC_EXP_PROFILE_TP1 PRIMARY KEY(EXP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_ADDR_SRC_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_ADDR_SRC_T
/

CREATE TABLE OLE_DLVR_ADDR_SRC_T
(
      OLE_DLVR_ADDR_SRC_ID VARCHAR(40)
        , OLE_DLVR_ADDR_SRC_CD VARCHAR(40) NOT NULL
        , OLE_DLVR_ADDR_SRC_DESC VARCHAR(700) NOT NULL
        , OLE_DLVR_ADDR_SRC_NM VARCHAR(100) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ROW_ACT_IND VARCHAR(1) NOT NULL
    
    , CONSTRAINT OLE_DLVR_ADDR_SRC_TP1 PRIMARY KEY(OLE_DLVR_ADDR_SRC_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_ADD_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_ADD_T
/

CREATE TABLE OLE_DLVR_ADD_T
(
      DLVR_ADD_ID VARCHAR(36)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , OLE_PTRN_ID VARCHAR(36)
        , ENTITY_ADDR_ID VARCHAR(36)
        , DLVR_PTRN_ADD_VER VARCHAR(1)
        , PTRN_DLVR_ADD VARCHAR(1)
        , ADD_VALID_FROM DATETIME
        , ADD_VALID_TO DATETIME
        , OLE_ADDR_SRC VARCHAR(40)
    
    , CONSTRAINT OLE_DLVR_ADD_TP1 PRIMARY KEY(DLVR_ADD_ID)





    
                                                                                                                                                                                                                                                                                                
                                    
, INDEX ole_dlvr_add_fk (ENTITY_ADDR_ID )
    
                                                                                                                                                                                                                                                                                                
                                    
, INDEX ole_dlvr_add_fk2 (OLE_ADDR_SRC )
    
                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_DLVR_ADD_TI1 (OLE_PTRN_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_PHONE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_PHONE_T
/

CREATE TABLE OLE_DLVR_PHONE_T
(
      DLVR_PHONE_ID VARCHAR(36)
        , OLE_PTRN_ID VARCHAR(36)
        , ENTITY_PHONE_ID VARCHAR(36)
        , OLE_PHONE_SRC VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_DLVR_PHONE_TP1 PRIMARY KEY(DLVR_PHONE_ID)





    
                                                                                                                                                                                                                
                                    
, INDEX OLE_DLVR_PHONE_TI1 (ENTITY_PHONE_ID )
    
                                                                                                                                                                                                                
                                    
, INDEX OLE_DLVR_PHONE_TI2 (OLE_PHONE_SRC )
    
                                                                                                                                                                                                                
                                    
, INDEX OLE_DLVR_PHONE_TI3 (OLE_PTRN_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_EMAIL_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_EMAIL_T
/

CREATE TABLE OLE_DLVR_EMAIL_T
(
      DLVR_EMAIL_ID VARCHAR(36)
        , OLE_PTRN_ID VARCHAR(36)
        , ENTITY_EMAIL_ID VARCHAR(36)
        , OLE_EMAIL_SRC VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_DLVR_EMAIL_TP1 PRIMARY KEY(DLVR_EMAIL_ID)





    
                                                                                                                                                                                                                
                                    
, INDEX OLE_DLVR_EMAIL_TI1 (ENTITY_EMAIL_ID )
    
                                                                                                                                                                                                                
                                    
, INDEX OLE_DLVR_EMAIL_TI2 (OLE_EMAIL_SRC )
    
                                                                                                                                                                                                                
                                    
, INDEX OLE_DLVR_EMAIL_TI3 (OLE_PTRN_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_BARCD_STAT_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_BARCD_STAT_T
/

CREATE TABLE OLE_DLVR_BARCD_STAT_T
(
      DLVR_BARCD_STAT_ID DECIMAL(8) default 0
        , DLVR_BARCD_TYP_CD VARCHAR(40) NOT NULL
        , DLVR_BARCD_TYP_NM VARCHAR(100) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ROW_ACT_IND VARCHAR(1) NOT NULL
        , DEL_IND VARCHAR(1) NOT NULL
    
    , CONSTRAINT OLE_DLVR_BARCD_STAT_TP1 PRIMARY KEY(DLVR_BARCD_STAT_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_BATCH_JOB_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_BATCH_JOB_T
/

CREATE TABLE OLE_DLVR_BATCH_JOB_T
(
      JOB_ID VARCHAR(40)
        , JOB_TRG_NM VARCHAR(100) NOT NULL
        , ROW_ACT_IND VARCHAR(1) NOT NULL
        , PCK_UP_LOCN VARCHAR(100)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , JOB_CRON_EXPRSN VARCHAR(80)
    
    , CONSTRAINT OLE_DLVR_BATCH_JOB_TP1 PRIMARY KEY(JOB_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_BORR_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_BORR_TYP_T
/

CREATE TABLE OLE_DLVR_BORR_TYP_T
(
      DLVR_BORR_TYP_ID VARCHAR(40) default '0'
        , DLVR_BORR_TYP_CD VARCHAR(40) NOT NULL
        , DLVR_BORR_TYP_DESC VARCHAR(700) NOT NULL
        , DLVR_BORR_TYP_NM VARCHAR(100) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ROW_ACT_IND VARCHAR(1) NOT NULL
    
    , CONSTRAINT OLE_DLVR_BORR_TYP_TP1 PRIMARY KEY(DLVR_BORR_TYP_ID)

    , CONSTRAINT DLVR_BORR_TYP_CD UNIQUE (DLVR_BORR_TYP_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_CIRC_RECORD
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_CIRC_RECORD
/

CREATE TABLE OLE_DLVR_CIRC_RECORD
(
      CIR_HIS_REC_ID VARCHAR(40)
        , LOAN_TRAN_ID VARCHAR(40) NOT NULL
        , CIR_POLICY_ID VARCHAR(2000) NOT NULL
        , OLE_PTRN_ID VARCHAR(40) NOT NULL
        , PTRN_TYP_ID VARCHAR(40)
        , AFFILIATION_ID VARCHAR(40)
        , DEPARTMENT_ID VARCHAR(40)
        , OTHER_AFFILIATION VARCHAR(40)
        , STATISTICAL_CATEGORY VARCHAR(40)
        , ITM_ID VARCHAR(40) NOT NULL
        , ITM_LOCN VARCHAR(100)
        , HLDNG_LOCN VARCHAR(100)
        , BIB_TIT VARCHAR(4000) NOT NULL
        , BIB_AUTH VARCHAR(500)
        , BIB_EDITION VARCHAR(500)
        , BIB_PUB VARCHAR(500)
        , BIB_PUB_DT DATETIME
        , BIB_ISBN VARCHAR(50)
        , PROXY_PTRN_ID VARCHAR(40)
        , DUE_DT_TIME DATETIME
        , PAST_DUE_DT_TIME DATETIME
        , CRTE_DT_TIME DATETIME NOT NULL
        , MODI_DT_TIME DATETIME
        , CIRC_LOC_ID VARCHAR(40) NOT NULL
        , OPTR_CRTE_ID VARCHAR(40)
        , OPTR_MODI_ID VARCHAR(40)
        , MACH_ID VARCHAR(100)
        , OVRR_OPTR_ID VARCHAR(40)
        , NUM_RENEWALS VARCHAR(3)
        , NUM_OVERDUE_NOTICES_SENT VARCHAR(3)
        , OVERDUE_NOTICE_DATE DATETIME
        , OLE_RQST_ID VARCHAR(40)
        , REPMNT_FEE_PTRN_BILL_ID VARCHAR(40)
        , CHECK_IN_DT_TIME DATETIME
        , CHECK_IN_DT_TIME_OVR_RD DATETIME
        , CHECK_IN_OPTR_ID VARCHAR(40)
        , CHECK_IN_MACH_ID VARCHAR(100)
        , ITEM_TYP_ID VARCHAR(100)
        , TEMP_ITEM_TYP_ID VARCHAR(100)
        , ITEM_UUID VARCHAR(100) NOT NULL
    
    , CONSTRAINT OLE_DLVR_CIRC_RECORDP1 PRIMARY KEY(CIR_HIS_REC_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
                                    
, INDEX fk_OLE_DLVR_CIRC_RECORD_FK1 (OLE_PTRN_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
                                    
, INDEX fk_OLE_DLVR_CIRC_RECORD_FK2 (PROXY_PTRN_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
                                    
, INDEX OLE_DLVR_CIRC_RECORD_TI1 (LOAN_TRAN_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_FIXED_DUE_DATE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_FIXED_DUE_DATE_T
/

CREATE TABLE OLE_DLVR_FIXED_DUE_DATE_T
(
      DUE_DATE_ID VARCHAR(40) default '0'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , CIRCULATION_POLICY_SET_ID VARCHAR(1000) NOT NULL
    
    , CONSTRAINT OLE_DLVR_FIXED_DUE_DATE_TP1 PRIMARY KEY(DUE_DATE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_FXD_DUE_DT_SPAN_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_FXD_DUE_DT_SPAN_T
/

CREATE TABLE OLE_DLVR_FXD_DUE_DT_SPAN_T
(
      FIXED_DUE_DATE_TIME_SPAN_ID VARCHAR(40) default '0'
        , DUE_DATE_ID VARCHAR(40) NOT NULL
        , FIXED_DUE_DATE DATETIME NOT NULL
        , FROM_DATE DATETIME NOT NULL
        , TO_DATE DATETIME NOT NULL
        , TIME_SPAN VARCHAR(100) NOT NULL
    
    , CONSTRAINT OLE_DLVR_FXD_DUE_DT_SPAN_TP1 PRIMARY KEY(FIXED_DUE_DATE_TIME_SPAN_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_ITEM_AVAIL_STAT_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_ITEM_AVAIL_STAT_T
/

CREATE TABLE OLE_DLVR_ITEM_AVAIL_STAT_T
(
      ITEM_AVAIL_STAT_ID VARCHAR(40) default '0'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , ITEM_AVAIL_STAT_CD VARCHAR(40) NOT NULL
        , ITEM_AVAIL_STAT_NM VARCHAR(200) NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DLVR_ITEM_AVAIL_STAT_TP1 PRIMARY KEY(ITEM_AVAIL_STAT_ID)

    , CONSTRAINT ITEM_AVAIL_STAT_CD UNIQUE (ITEM_AVAIL_STAT_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_LOAN_STAT_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_LOAN_STAT_T
/

CREATE TABLE OLE_DLVR_LOAN_STAT_T
(
      LOAN_STAT_ID VARCHAR(36) default '0'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , LOAN_STAT_CD VARCHAR(40) NOT NULL
        , LOAN_STAT_NM VARCHAR(100) NOT NULL
    
    , CONSTRAINT OLE_DLVR_LOAN_STAT_TP1 PRIMARY KEY(LOAN_STAT_ID)

    , CONSTRAINT LOAN_STAT_CD UNIQUE (LOAN_STAT_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_LOAN_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_LOAN_T
/

CREATE TABLE OLE_DLVR_LOAN_T
(
      LOAN_TRAN_ID VARCHAR(40)
        , CIR_POLICY_ID VARCHAR(2000) NOT NULL
        , OLE_PTRN_ID VARCHAR(40) NOT NULL
        , ITM_ID VARCHAR(40)
        , OLE_PROXY_BORROWER_NM VARCHAR(60)
        , PROXY_PTRN_ID VARCHAR(40)
        , CURR_DUE_DT_TIME DATETIME
        , PAST_DUE_DT_TIME DATETIME
        , CRTE_DT_TIME DATETIME NOT NULL
        , CIRC_LOC_ID VARCHAR(40) NOT NULL
        , OPTR_CRTE_ID VARCHAR(40) NOT NULL
        , MACH_ID VARCHAR(100)
        , OVRR_OPTR_ID VARCHAR(40)
        , NUM_RENEWALS VARCHAR(3)
        , NUM_OVERDUE_NOTICES_SENT VARCHAR(3)
        , N_OVERDUE_NOTICE VARCHAR(3)
        , OVERDUE_NOTICE_DATE DATETIME
        , OLE_RQST_ID VARCHAR(40)
        , REPMNT_FEE_PTRN_BILL_ID VARCHAR(40)
        , CRTSY_NTCE VARCHAR(1) default 'N'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , ITEM_UUID VARCHAR(100) NOT NULL
        , NUM_CLAIMS_RTRN_NOTICES_SENT INTEGER
        , CLAIMS_SEARCH_COUNT INTEGER
        , LAST_CLAIMS_RTRN_SEARCH_DT DATETIME
    
    , CONSTRAINT OLE_DLVR_LOAN_TP1 PRIMARY KEY(LOAN_TRAN_ID)

    , CONSTRAINT itm_id_const UNIQUE (ITM_ID)




    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX new_fk_constraint2 (OLE_PTRN_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
                                    
, INDEX loan_curr_due_date_index (CURR_DUE_DT_TIME )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_DLVR_LOAN_TI1 (ITEM_UUID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_LOAN_TERM_UNIT_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_LOAN_TERM_UNIT_T
/

CREATE TABLE OLE_DLVR_LOAN_TERM_UNIT_T
(
      LOAN_TERM_UNIT_ID VARCHAR(36) default '0'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , LOAN_TERM_UNIT_CD VARCHAR(40) NOT NULL
        , LOAN_TERM_UNIT_NM VARCHAR(100) NOT NULL
    
    , CONSTRAINT OLE_DLVR_LOAN_TERM_UNIT_TP1 PRIMARY KEY(LOAN_TERM_UNIT_ID)

    , CONSTRAINT LOAN_TERM_UNIT_CD UNIQUE (LOAN_TERM_UNIT_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_PTRN_BILL_FEE_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_PTRN_BILL_FEE_TYP_T
/

CREATE TABLE OLE_DLVR_PTRN_BILL_FEE_TYP_T
(
      ID VARCHAR(40)
        , PTRN_BILL_ID VARCHAR(40) NOT NULL
        , ITM_UUID VARCHAR(100)
        , PAY_STATUS_ID VARCHAR(40) NOT NULL
        , FEE_TYP_ID VARCHAR(40) NOT NULL
        , FEE_TYP_AMT DECIMAL(10,4) NOT NULL
        , ITM_BARCODE VARCHAR(40)
        , BALANCE_AMT DECIMAL(10,4) default 0.00
        , PTRN_BILL_DATE DATETIME NOT NULL
        , PAY_FORGIVE_NOTE VARCHAR(500)
        , PAY_ERROR_NOTE VARCHAR(500)
        , PAY_CANCEL_NOTE VARCHAR(500)
        , PAY_GENERAL_NOTE VARCHAR(500)
        , DUE_DT_TIME DATETIME
        , CHECK_OUT_DT_TIME DATETIME
        , CHECK_IN_DT_TIME DATETIME
        , CHECK_IN_DT_TIME_OVR_RD DATETIME
        , RNWL_DT_TIME DATETIME
        , OPERATOR_ID VARCHAR(40)
        , ITM_TITLE VARCHAR(600)
        , ITM_AUTHOR VARCHAR(200)
        , ITM_TYPE VARCHAR(100)
        , ITM_CALL_NUM VARCHAR(100)
        , ITM_COPY_NUM VARCHAR(20)
        , ITM_ENUM VARCHAR(100)
        , ITM_CHRON VARCHAR(100)
        , ITM_LOC VARCHAR(600)
        , CRDT_ISSUED DECIMAL(10,4) default 0.00
        , CRDT_REMAINING DECIMAL(10,4) default 0.00
        , PAY_CREDIT_NOTE VARCHAR(500)
        , PAY_TRANSFER_NOTE VARCHAR(500)
        , PAY_REFUND_NOTE VARCHAR(500)
        , PAY_CAN_CRDT_NOTE VARCHAR(500)
        , MANUAL_BILL VARCHAR(1) default 'N'
    
    , CONSTRAINT OLE_DLVR_PTRN_BILL_FEE_TYP_P1 PRIMARY KEY(ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX FEE_TYP_BILL_ID (PTRN_BILL_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_PTRN_BILL_PAY_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_PTRN_BILL_PAY_T
/

CREATE TABLE OLE_DLVR_PTRN_BILL_PAY_T
(
      ID VARCHAR(40)
        , ITM_LINE_ID VARCHAR(40) NOT NULL
        , BILL_PAY_AMT DECIMAL(10,4) NOT NULL
        , CRTE_DT_TIME DATETIME NOT NULL
        , OPTR_CRTE_ID VARCHAR(40) NOT NULL
        , TRNS_NUMBER VARCHAR(40)
        , TRNS_NOTE VARCHAR(500)
        , TRNS_MODE VARCHAR(40)
        , NOTE VARCHAR(500)
    
    , CONSTRAINT OLE_DLVR_PTRN_BILL_PAY_TP1 PRIMARY KEY(ID)





    
                                                                                                                                                                                                                                                                            
                                    
, INDEX BILL_PAY_ID (ITM_LINE_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_PTRN_BILL_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_PTRN_BILL_T
/

CREATE TABLE OLE_DLVR_PTRN_BILL_T
(
      PTRN_BILL_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , OLE_PTRN_ID VARCHAR(40)
        , PROXY_PTRN_ID VARCHAR(40)
        , TOT_AMT_DUE DECIMAL(10,4) NOT NULL
        , UNPAID_BAL DECIMAL(10,4) default 0.00
        , PAY_METHOD_ID VARCHAR(40)
        , PAY_AMT DECIMAL(10,4) default 0.00
        , PAY_DT DATETIME
        , PAY_OPTR_ID VARCHAR(40)
        , PAY_MACHINE_ID VARCHAR(40)
        , CRTE_DT_TIME DATETIME
        , OPTR_CRTE_ID VARCHAR(40) NOT NULL
        , OPTR_MACHINE_ID VARCHAR(40)
        , PAY_NOTE VARCHAR(500)
        , NOTE VARCHAR(500)
        , BILL_REVIEWED VARCHAR(1)
        , CRDT_ISSUED DECIMAL(10,4) default 0.00
        , CRDT_REMAINING DECIMAL(10,4) default 0.00
        , MANUAL_BILL VARCHAR(1) default 'Y'
    
    , CONSTRAINT OLE_DLVR_PTRN_BILL_TP1 PRIMARY KEY(PTRN_BILL_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
                                    
, INDEX OLE_DLVR_PTRN_BILL_TI1 (OLE_PTRN_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_PTRN_FEE_TYPE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_PTRN_FEE_TYPE_T
/

CREATE TABLE OLE_DLVR_PTRN_FEE_TYPE_T
(
      FEE_TYP_ID VARCHAR(40)
        , FEE_TYP_CD VARCHAR(40) NOT NULL
        , FEE_TYP_NM VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_DLVR_PTRN_FEE_TYPE_TP1 PRIMARY KEY(FEE_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_COPY_FORMAT_T
# -----------------------------------------------------------------------
drop table if exists OLE_COPY_FORMAT_T
/

CREATE TABLE OLE_COPY_FORMAT_T
(
      COPY_FORMAT_ID VARCHAR(40)
        , COPY_FORMAT_CD VARCHAR(40)
        , COPY_FORMAT_NM VARCHAR(40)
        , COPY_FORMAT_ACTIVE VARCHAR(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_COPY_FORMAT_TP1 PRIMARY KEY(COPY_FORMAT_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_RECENTLY_RETURNED_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_RECENTLY_RETURNED_T
/

CREATE TABLE OLE_DLVR_RECENTLY_RETURNED_T
(
      ID VARCHAR(40)
        , CIRC_DESK_ID VARCHAR(40) NOT NULL
        , ITEM_UUID VARCHAR(100) NOT NULL
    
    , CONSTRAINT OLE_DLVR_RECENTLY_RETURNED_P1 PRIMARY KEY(ID)

    , CONSTRAINT ITEM_UUID UNIQUE (ITEM_UUID)




    
                                                                                                                                                    
                                    
, INDEX fk1_crcl_dsk (CIRC_DESK_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_RQST_HSTRY_REC_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_RQST_HSTRY_REC_T
/

CREATE TABLE OLE_DLVR_RQST_HSTRY_REC_T
(
      OLE_RQST_HSTRY_ID VARCHAR(40)
        , OLE_RQST_ID VARCHAR(80) NOT NULL
        , OLE_PTRN_ID VARCHAR(40) NOT NULL
        , OLE_ITEM_ID VARCHAR(80) NOT NULL
        , OLE_ITEM_BARCODE VARCHAR(80) NOT NULL
        , OLE_LOAN_ID VARCHAR(80)
        , OLE_LN_ITM_NUM VARCHAR(80)
        , OLE_RQST_TYP_CD VARCHAR(80) NOT NULL
        , OLE_PCK_LOC_CD VARCHAR(80)
        , OLE_OPRT_ID VARCHAR(80) NOT NULL
        , OLE_MACH_ID VARCHAR(80)
        , ARCH_DT_TIME DATETIME NOT NULL
        , OLE_REQ_OUTCOME_STATUS VARCHAR(80)
        , CRTE_DT_TIME DATETIME NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_DLVR_RQST_HSTRY_REC_TP1 PRIMARY KEY(OLE_RQST_HSTRY_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_RQST_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_RQST_T
/

CREATE TABLE OLE_DLVR_RQST_T
(
      OLE_RQST_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , PO_LN_ITM_NO VARCHAR(100)
        , ITM_ID VARCHAR(40)
        , OLE_PTRN_ID VARCHAR(40)
        , OLE_PTRN_BARCD VARCHAR(80)
        , PROXY_PTRN_ID VARCHAR(40)
        , PROXY_PTRN_BARCD VARCHAR(80)
        , OLE_RQST_TYP_ID VARCHAR(40)
        , CNTNT_DESC VARCHAR(800)
        , RQST_EXPIR_DT_TIME DATETIME
        , RCAL_NTC_SNT_DT DATETIME
        , ONHLD_NTC_SNT_DT DATETIME
        , CRTE_DT_TIME DATETIME
        , MODI_DT_TIME DATETIME
        , CPY_FRMT VARCHAR(40)
        , LOAN_TRAN_ID VARCHAR(40)
        , PCKUP_LOC_ID VARCHAR(40)
        , OPTR_CRTE_ID VARCHAR(40)
        , OPTR_MODI_ID VARCHAR(40)
        , CIRC_LOC_ID VARCHAR(40)
        , MACH_ID VARCHAR(80)
        , PTRN_Q_POS INTEGER
        , ITEM_UUID VARCHAR(100) NOT NULL
        , RQST_STAT VARCHAR(10)
        , RQST_LVL VARCHAR(40)
        , BIB_ID VARCHAR(40)
        , ASR_FLAG VARCHAR(1)
        , HOLD_EXP_DATE DATETIME
        , RQST_NOTE VARCHAR(4000)
    
    , CONSTRAINT OLE_DLVR_RQST_TP1 PRIMARY KEY(OLE_RQST_ID)

    , CONSTRAINT OLE_DLVR_RQST_T_ITEM_Q_INX_UK UNIQUE (OLE_PTRN_ID, PTRN_Q_POS, ITM_ID)




    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX flk1 (OLE_RQST_TYP_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX flk2 (PCKUP_LOC_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX flk3 (CIRC_LOC_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX flk4 (OLE_PTRN_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX flk5 (PROXY_PTRN_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX OLE_DLVR_RQST_TI1 (LOAN_TRAN_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX OLE_DLVR_RQST_TI2 (ITM_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX OLE_DLVR_RQST_TI3 (ITEM_UUID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_DLVR_RQST_TI4 (PTRN_Q_POS )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX OLE_DLVR_RQST_TI5 (BIB_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX OLE_DLVR_RQST_TI6 (OLE_PTRN_BARCD )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_RQST_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_RQST_TYP_T
/

CREATE TABLE OLE_DLVR_RQST_TYP_T
(
      OLE_RQST_TYP_ID VARCHAR(40)
        , OLE_RQST_TYP_CD VARCHAR(80) NOT NULL
        , OLE_RQST_TYP_NM VARCHAR(80) NOT NULL
        , ROW_ACT_IND VARCHAR(1) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , OLE_RQST_TYP_DESC VARCHAR(80)
    
    , CONSTRAINT OLE_DLVR_RQST_TYP_TP1 PRIMARY KEY(OLE_RQST_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_SRC_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_SRC_T
/

CREATE TABLE OLE_DLVR_SRC_T
(
      OLE_DLVR_SRC_ID VARCHAR(40)
        , OLE_DLVR_SRC_CD VARCHAR(40) NOT NULL
        , OLE_DLVR_SRC_DESC VARCHAR(700) NOT NULL
        , OLE_DLVR_SRC_NM VARCHAR(100) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ROW_ACT_IND VARCHAR(1) NOT NULL
    
    , CONSTRAINT OLE_DLVR_SRC_TP1 PRIMARY KEY(OLE_DLVR_SRC_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_STAT_CAT_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_STAT_CAT_T
/

CREATE TABLE OLE_DLVR_STAT_CAT_T
(
      OLE_DLVR_STAT_CAT_ID VARCHAR(40)
        , OLE_DLVR_STAT_CAT_CD VARCHAR(40) NOT NULL
        , OLE_DLVR_STAT_CAT_DESC VARCHAR(700) NOT NULL
        , OLE_DLVR_STAT_CAT_NM VARCHAR(100) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ROW_ACT_IND VARCHAR(1) NOT NULL
    
    , CONSTRAINT OLE_DLVR_STAT_CAT_TP1 PRIMARY KEY(OLE_DLVR_STAT_CAT_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_TEMP_CIRC_RECORD
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_TEMP_CIRC_RECORD
/

CREATE TABLE OLE_DLVR_TEMP_CIRC_RECORD
(
      TMP_CIR_HIS_REC_ID VARCHAR(40)
        , OLE_PTRN_ID VARCHAR(40) NOT NULL
        , ITM_ID VARCHAR(40) NOT NULL
        , CIRC_LOC_ID VARCHAR(40) NOT NULL
        , CHECK_IN_DT_TIME DATETIME
        , DUE_DT_TIME DATETIME
        , CHECK_OUT_DT_TIME DATETIME
        , OLE_PROXY_PTRN_ID VARCHAR(40)
        , ITEM_UUID VARCHAR(100) NOT NULL
    
    , CONSTRAINT OLE_DLVR_TEMP_CIRC_RECORDP1 PRIMARY KEY(TMP_CIR_HIS_REC_ID)





    
                                                                                                                                                                                                                                                                            
                                    
, INDEX fk1_ole_ptrn_t (OLE_PTRN_ID )
    
                                                                                                                                                                                                                                                                            
                                    
, INDEX OLE_DLVR_TEMP_CIRC_RECORD_TI1 (ITM_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_ERES_NTE_T
# -----------------------------------------------------------------------
drop table if exists OLE_ERES_NTE_T
/

CREATE TABLE OLE_ERES_NTE_T
(
      E_RES_NTE_ID VARCHAR(10)
        , E_RES_REC_ID VARCHAR(10)
        , E_RES_NTE_TXT VARCHAR(800)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_ERES_NTE_TP1 PRIMARY KEY(E_RES_NTE_ID)





    
                                                                                                                                                                                            
                                    
, INDEX OLE_ERES_NTE_FK (E_RES_REC_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_VRNT_TTL_T
# -----------------------------------------------------------------------
drop table if exists OLE_VRNT_TTL_T
/

CREATE TABLE OLE_VRNT_TTL_T
(
      E_RES_VRNT_TTL_ID VARCHAR(10)
        , E_RES_REC_ID VARCHAR(10)
        , E_RES_VRNT_TTL VARCHAR(800)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_VRNT_TTL_TP1 PRIMARY KEY(E_RES_VRNT_TTL_ID)





    
                                                                                                                                                                                            
                                    
, INDEX OLE_ERES_VRNT_TTL_FK (E_RES_REC_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_ERES_REQ_SEL_COMM_T
# -----------------------------------------------------------------------
drop table if exists OLE_ERES_REQ_SEL_COMM_T
/

CREATE TABLE OLE_ERES_REQ_SEL_COMM_T
(
      E_RES_REQ_SEL_COMM_ID VARCHAR(10)
        , E_RES_REC_ID VARCHAR(10)
        , REQ_SEL_COMM VARCHAR(800)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_ERES_REQ_SEL_COMM_TP1 PRIMARY KEY(E_RES_REQ_SEL_COMM_ID)





    
                                                                                                                                                                                            
                                    
, INDEX OLE_ERES_REQ_SEL_COMM_FK (E_RES_REC_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_ERES_REQ_T
# -----------------------------------------------------------------------
drop table if exists OLE_ERES_REQ_T
/

CREATE TABLE OLE_ERES_REQ_T
(
      E_RES_REQ_ID VARCHAR(10)
        , E_RES_REC_ID VARCHAR(10)
        , OLE_PTRN_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_ERES_REQ_TP1 PRIMARY KEY(E_RES_REQ_ID)





    
                                                                                                                                                                                            
                                    
, INDEX OLE_ERES_REQ_FK (E_RES_REC_ID )
    
                                                                                                                                                                                            
                                    
, INDEX OLE_ERES_REQ_TI1 (OLE_PTRN_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_ERES_SEL_T
# -----------------------------------------------------------------------
drop table if exists OLE_ERES_SEL_T
/

CREATE TABLE OLE_ERES_SEL_T
(
      E_RES_SEL_ID VARCHAR(10)
        , E_RES_REC_ID VARCHAR(10)
        , PRNCPL_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_ERES_SEL_TP1 PRIMARY KEY(E_RES_SEL_ID)





    
                                                                                                                                                                                            
                                    
, INDEX OLE_ERES_SEL_FK (E_RES_REC_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_E_RES_REC_EVNT_LOG_T
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_REC_EVNT_LOG_T
/

CREATE TABLE OLE_E_RES_REC_EVNT_LOG_T
(
      E_RES_EVNT_LOG_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , LOG_TYPE_ID VARCHAR(40)
        , EVNT_TYPE_ID VARCHAR(40)
        , PROBLM_TYPE_ID VARCHAR(40)
        , EVNT_NTE VARCHAR(800)
        , EVNT_USR VARCHAR(40)
        , EVNT_DT DATETIME
        , STATUS VARCHAR(40)
        , RESOLVED_DATE DATETIME
        , RESOLUTION VARCHAR(40)
        , E_RES_REC_ID VARCHAR(10)
        , SAVE_FLAG VARCHAR(1)
        , ATT_FILE_NAME1 VARCHAR(40)
        , ATT_CONTENT1 LONGBLOB
        , ATT_MIME_TYPE1 VARCHAR(100)
        , ATT_FILE_NAME2 VARCHAR(40)
        , ATT_CONTENT2 LONGBLOB
        , ATT_MIME_TYPE2 VARCHAR(100)
    
    , CONSTRAINT OLE_E_RES_REC_EVNT_LOG_TP1 PRIMARY KEY(E_RES_EVNT_LOG_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
                                    
, INDEX OLE_E_RES_REC_EVNT_LOG_FK (E_RES_REC_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_E_RES_REC_INS_T
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_REC_INS_T
/

CREATE TABLE OLE_E_RES_REC_INS_T
(
      E_RES_INS_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , INST_ID VARCHAR(50)
        , HOLD_ID VARCHAR(50)
        , INST_FLAG VARCHAR(10)
        , INST_NM VARCHAR(500)
        , ISBN VARCHAR(800)
        , INST_HOLD VARCHAR(500)
        , PUB_DISP_NTE VARCHAR(800)
        , PUBHR VARCHAR(200)
        , PLTFRM_ID VARCHAR(100)
        , STATUS VARCHAR(40)
        , TIPP_STATUS VARCHAR(40)
        , SUB_STATUS VARCHAR(40)
        , AUTO_INST_REC VARCHAR(100)
        , COV_SRT_DT DATETIME
        , COV_SRT_VOL VARCHAR(40)
        , COV_SRT_ISS VARCHAR(40)
        , COV_END_DT DATETIME
        , COV_END_VOL VARCHAR(40)
        , COV_END_ISS VARCHAR(40)
        , PRPTL_ACC_SRT_DT DATETIME
        , PRPTL_ACC_SRT_VOL VARCHAR(40)
        , PRPTL_ACC_SRT_ISS VARCHAR(40)
        , PRPTL_ACC_END_DT DATETIME
        , PRPTL_ACC_END_VOL VARCHAR(40)
        , PRPTL_ACC_END_ISS VARCHAR(40)
        , E_RES_REC_ID VARCHAR(10)
        , BIB_ID VARCHAR(40)
        , GOKB_ID INTEGER
    
    , CONSTRAINT OLE_E_RES_REC_INS_TP1 PRIMARY KEY(E_RES_INS_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX OLE_E_RES_REC_INS_FK (E_RES_REC_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX OLE_E_RES_REC_INS_ID (INST_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_E_RES_REC_INV_T
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_REC_INV_T
/

CREATE TABLE OLE_E_RES_REC_INV_T
(
      E_RES_INV_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , E_RES_REC_ID VARCHAR(10)
        , INV_ID DECIMAL(9)
        , PO_ID DECIMAL(9)
        , HOLDING_ID VARCHAR(40)
        , INV_DT DATETIME
        , INV_NUM VARCHAR(25)
        , VND_NM VARCHAR(100)
        , INV_LI_AMT VARCHAR(40)
        , FD_CD VARCHAR(40)
        , INV_STAT VARCHAR(40)
        , PD_DT DATETIME
        , CHK_CLR_DT DATETIME
        , CHK_NUM VARCHAR(40)
    
    , CONSTRAINT OLE_E_RES_REC_INV_TP1 PRIMARY KEY(E_RES_INV_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_E_RES_REC_LIC_T
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_REC_LIC_T
/

CREATE TABLE OLE_E_RES_REC_LIC_T
(
      E_RES_LIC_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , LIC_RQST_DOC_NUM VARCHAR(40)
        , LIC_RQST_ID VARCHAR(40)
        , E_RES_REC_ID VARCHAR(10)
    
    , CONSTRAINT OLE_E_RES_REC_LIC_TP1 PRIMARY KEY(E_RES_LIC_ID)





    
                                                                                                                                                                                                                
                                    
, INDEX OLE_E_RES_REC_LIC_FK1 (E_RES_REC_ID )
    
                                                                                                                                                                                                                
                                    
, INDEX OLE_E_RES_REC_LIC_FK2 (LIC_RQST_DOC_NUM )
    
                                                                                                                                                                                                                
                                    
, INDEX OLE_E_RES_REC_LIC_FK3 (LIC_RQST_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_LINK_E_RES_T
# -----------------------------------------------------------------------
drop table if exists OLE_LINK_E_RES_T
/

CREATE TABLE OLE_LINK_E_RES_T
(
      LINK_E_RES_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , E_RES_REC_ID VARCHAR(10)
        , LINK_E_RES_REC_ID VARCHAR(10)
        , RLTNSHP_TYPE VARCHAR(10)
        , CHAIN_STRING VARCHAR(100)
    
    , CONSTRAINT OLE_LINK_E_RES_TP1 PRIMARY KEY(LINK_E_RES_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_E_RES_REC_PO_T
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_REC_PO_T
/

CREATE TABLE OLE_E_RES_REC_PO_T
(
      E_RES_PO_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , E_RES_REC_ID VARCHAR(10)
        , VER_NBR DECIMAL(8)
        , PO_ID DECIMAL(9)
        , INST_ID VARCHAR(40)
        , TITLE VARCHAR(100)
        , PD_AMT_CURR_FY DECIMAL(19,4)
        , PD_AMT_PREV_FY DECIMAL(19,4)
        , PD_AMT_TWO_YRS_PREV_FY DECIMAL(19,4)
    
    , CONSTRAINT OLE_E_RES_REC_PO_TP1 PRIMARY KEY(E_RES_PO_ID)





    
                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_E_RES_REC_PO_FK (E_RES_REC_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_E_RES_REC_T
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_REC_T
/

CREATE TABLE OLE_E_RES_REC_T
(
      E_RES_REC_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , TITLE VARCHAR(200)
        , DESCR VARCHAR(800)
        , PUBHR_ID VARCHAR(40)
        , GOKB_ID INTEGER
        , ISBN VARCHAR(100)
        , STAT_ID VARCHAR(40)
        , STAT_DT VARCHAR(40)
        , PLTFRM_PROV VARCHAR(250)
        , FD_CD VARCHAR(10)
        , WRK_FLW_STAT VARCHAR(40)
        , VNDR_NM VARCHAR(45)
        , VNDR_ID VARCHAR(40)
        , ESTD_PR VARCHAR(40)
        , ORD_TYP_ID DECIMAL(8)
        , PYMT_TYP_ID VARCHAR(10)
        , PCKG_TYP_ID VARCHAR(10)
        , PCKG_SCP_ID VARCHAR(10)
        , BRKBLE VARCHAR(1)
        , FD_TITLE_LST VARCHAR(1)
        , NTE VARCHAR(800)
        , PUB_DISP_NOTE VARCHAR(800)
        , REQ_SEL_COMM VARCHAR(800)
        , REQ_PRTY_ID VARCHAR(40)
        , STAT_SRCH_CD_ID DECIMAL(8)
        , TRL_ND VARCHAR(1)
        , TRL_STAT VARCHAR(40)
        , LIC_ND VARCHAR(1)
        , LIC_REQ_STAT VARCHAR(40)
        , ORD_PAY_STAT VARCHAR(40)
        , ACT_STAT VARCHAR(40)
        , DEF_COVR VARCHAR(100)
        , DEF_PER_ACC VARCHAR(100)
        , EINST_FLAG VARCHAR(1)
        , E_RES_REC_DOC_NUM VARCHAR(40)
        , E_RES_SBSCRP_STS VARCHAR(40)
        , E_RES_INIT_SBSCRP_STRT_DT DATETIME
        , E_RES_CUR_SBSCRP_STRT_DT DATETIME
        , E_RES_CUR_SBSCRP_END_DT DATETIME
        , E_RES_CAN_DCSN_DT DATETIME
        , E_RES_CAN_EFCT_DT DATETIME
        , E_RES_CAN_CAND VARCHAR(1)
        , E_RES_RNW_ALERT VARCHAR(1)
        , E_RES_CAN_RSN VARCHAR(40)
        , E_RES_RNW_NTC_PRD VARCHAR(4)
        , E_RES_RCP_ID VARCHAR(40)
        , E_RES_WK_FL_IND VARCHAR(40)
        , ACC_CONFMN_DT DATETIME
        , ACC_USR_NM VARCHAR(50)
        , ACC_PWD VARCHAR(50)
        , PROXY_URL VARCHAR(200)
        , PROXY_RES VARCHAR(1)
        , MOB_ACC_ID VARCHAR(40)
        , MOB_ACC_NOTE VARCHAR(800)
        , BRANDING_CMPLT VARCHAR(1)
        , PLTFRM_CONFIG_CMPLT VARCHAR(1)
        , MARC_REC_SRC_TYPE_ID VARCHAR(40)
        , LAST_REC_LOAD_DT DATETIME
        , MARC_REC_SRC VARCHAR(40)
        , MARC_REC_UPDT_FREQ_ID VARCHAR(40)
        , MARC_REC_URL VARCHAR(200)
        , MARC_REC_USR_NM VARCHAR(50)
        , MARC_REC_PWD VARCHAR(50)
        , MARC_REC_NOTE VARCHAR(800)
        , GOKB_CONFIG VARCHAR(800)
        , ACCES_ACTVTN_DOC_NMBR VARCHAR(800)
        , GOKB_IMPORT_PROFILE VARCHAR(10)
        , GOKB_PROFILE VARCHAR(10)
        , GOKB_PACKAGE_STATUS VARCHAR(40)
        , GOKB_LAST_UPDATED_DATE DATETIME
        , E_RES_RCP_ROLE_ID VARCHAR(40)
        , E_RES_RCP_GROUP_ID VARCHAR(40)
    
    , CONSTRAINT OLE_E_RES_REC_TP1 PRIMARY KEY(E_RES_REC_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_E_RES_REC_FK6 (PCKG_SCP_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_E_RES_REC_FK7 (REQ_PRTY_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_E_RES_REC_FK8 (PCKG_TYP_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_E_RES_REC_FK9 (STAT_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_E_RES_REC_FK10 (PYMT_TYP_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
                                    
, INDEX OLE_E_RES_REC_FK11 (STAT_SRCH_CD_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_E_RES_ACCESS_T
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_ACCESS_T
/

CREATE TABLE OLE_E_RES_ACCESS_T
(
      E_RES_ACCESS_ID VARCHAR(10)
        , E_RES_REC_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , ACCESS_STATUS VARCHAR(20)
        , ACC_CONFMN_DT DATETIME
        , ACC_LOC_ID VARCHAR(40)
        , ACC_TYP_ID VARCHAR(40)
        , ACC_USR_NM VARCHAR(50)
        , AUTHCAT_TYP_ID VARCHAR(40)
        , ACC_PWD VARCHAR(50)
        , NUM_SIMULT_USER VARCHAR(25)
        , PROXY_URL VARCHAR(200)
        , PROXY_RES VARCHAR(1)
        , MOB_ACC_ID VARCHAR(40)
        , MOB_ACC_NOTE VARCHAR(800)
        , BRANDING_CMPLT VARCHAR(1)
        , PLTFRM_CONFIG_CMPLT VARCHAR(1)
        , TECH_REQ VARCHAR(800)
        , MARC_REC_SRC_TYPE_ID VARCHAR(40)
        , LAST_REC_LOAD_DT DATETIME
        , MARC_REC_SRC VARCHAR(40)
        , MARC_REC_UPDT_FREQ INTEGER
        , MARC_REC_UPDT_REGULARITY VARCHAR(200)
        , MARC_REC_URL VARCHAR(200)
        , MARC_REC_USR_NM VARCHAR(50)
        , MARC_REC_PWD VARCHAR(50)
        , MARC_REC_NOTE VARCHAR(800)
        , WRKFLW_ID VARCHAR(800)
    
    , CONSTRAINT OLE_E_RES_ACCESS_TP1 PRIMARY KEY(E_RES_ACCESS_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PUR_FRMT_T
# -----------------------------------------------------------------------
drop table if exists OLE_PUR_FRMT_T
/

CREATE TABLE OLE_PUR_FRMT_T
(
      OLE_FRMT_ID DECIMAL(8)
        , OLE_FORMAT VARCHAR(45)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ROW_ACTV_IND VARCHAR(1)
    
    , CONSTRAINT OLE_PUR_FRMT_TP1 PRIMARY KEY(OLE_FRMT_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_FRMT_TYPS_T
# -----------------------------------------------------------------------
drop table if exists OLE_FRMT_TYPS_T
/

CREATE TABLE OLE_FRMT_TYPS_T
(
      FRMT_TYPS_ID VARCHAR(10)
        , E_RES_REC_ID VARCHAR(10)
        , OLE_FRMT_ID DECIMAL(8)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_FRMT_TYPS_TP1 PRIMARY KEY(FRMT_TYPS_ID)





    
                                                                                                                                                        
                                    
, INDEX OLE_FRMT_TYPS_FK (OLE_FRMT_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_E_RES_STAT_T
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_STAT_T
/

CREATE TABLE OLE_E_RES_STAT_T
(
      E_RES_STAT_ID VARCHAR(10)
        , E_RES_STAT_NM VARCHAR(40)
        , E_RES_STAT_DESC VARCHAR(100)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ROW_ACT_IND VARCHAR(1)
    
    , CONSTRAINT OLE_E_RES_STAT_TP1 PRIMARY KEY(E_RES_STAT_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_E_RES_ACCTG_LN_T
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_ACCTG_LN_T
/

CREATE TABLE OLE_E_RES_ACCTG_LN_T
(
      E_RES_ACCTG_LN_ID VARCHAR(10)
        , E_RES_REC_ID VARCHAR(10)
        , FIN_COA_CD VARCHAR(2)
        , ACCOUNT_NBR VARCHAR(7)
        , SUB_ACCT_NBR VARCHAR(5)
        , FIN_OBJECT_CD VARCHAR(4)
        , FIN_SUB_OBJ_CD VARCHAR(3)
        , PROJECT_CD VARCHAR(10)
        , ORG_REFERENCE_ID VARCHAR(8)
        , ACLN_PCT DECIMAL(35,20)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_E_RES_ACCTG_LN_TP1 PRIMARY KEY(E_RES_ACCTG_LN_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_GLBLY_PRCT_FLD_T
# -----------------------------------------------------------------------
drop table if exists OLE_GLBLY_PRCT_FLD_T
/

CREATE TABLE OLE_GLBLY_PRCT_FLD_T
(
      OLE_GLBLY_PRCT_FLD_ID VARCHAR(40) default '0'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , TAG VARCHAR(40)
        , FIRST_IND VARCHAR(40)
        , SEC_IND VARCHAR(40)
        , SUB_FLD VARCHAR(40)
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_GLBLY_PRCT_FLD_TP1 PRIMARY KEY(OLE_GLBLY_PRCT_FLD_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_LIC_CHK_LST_T
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_CHK_LST_T
/

CREATE TABLE OLE_LIC_CHK_LST_T
(
      LIC_CHK_LST_ID VARCHAR(40) default '0'
        , OBJ_ID VARCHAR(40) NOT NULL
        , VER_NBR VARCHAR(40) NOT NULL
        , LIC_CHK_LST_AUTH VARCHAR(40) NOT NULL
        , LIC_CHK_LST_FILE_NM VARCHAR(250) NOT NULL
        , LIC_CHK_MIME_TYP VARCHAR(255)
        , LIC_CHK_LST_MOD_DATE DATETIME NOT NULL
        , LIC_CHK_LST_DESC VARCHAR(800)
        , LIC_CHK_LST_NM VARCHAR(40) NOT NULL
        , LIC_RM_ID VARCHAR(40) NOT NULL
        , ROW_ACT_IND VARCHAR(1)
    
    , CONSTRAINT OLE_LIC_CHK_LST_TP1 PRIMARY KEY(LIC_CHK_LST_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_LIC_DOC_LOCN_T
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_DOC_LOCN_T
/

CREATE TABLE OLE_LIC_DOC_LOCN_T
(
      LIC_DOC_LOCN_ID VARCHAR(40) default '0'
        , LIC_DOC_LOCN_NM VARCHAR(40) NOT NULL
        , LIC_DOC_LOCN_DESC VARCHAR(150)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_LIC_DOC_LOCN_TP1 PRIMARY KEY(LIC_DOC_LOCN_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_LIC_EVNT_LOG_T
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_EVNT_LOG_T
/

CREATE TABLE OLE_LIC_EVNT_LOG_T
(
      LIC_EVNT_LOG_ID VARCHAR(40)
        , LIC_EVNT_MSG_TYP VARCHAR(50) NOT NULL
        , LIC_EVNT_LOG_DATE DATETIME NOT NULL
        , LIC_EVNT_LOG_USER VARCHAR(40) NOT NULL
        , LIC_EVNT_LOG_MSG VARCHAR(2000) NOT NULL
        , LIC_REQS_ID VARCHAR(40) NOT NULL
    
    , CONSTRAINT OLE_LIC_EVNT_LOG_TP1 PRIMARY KEY(LIC_EVNT_LOG_ID)





    
                                                                                                                                                                                                                
                                    
, INDEX LIC_EVNT_REQS_FK (LIC_REQS_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_LIC_EVNT_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_EVNT_TYP_T
/

CREATE TABLE OLE_LIC_EVNT_TYP_T
(
      LIC_EVNT_TYP_ID VARCHAR(40)
        , LIC_EVNT_TYP_NM VARCHAR(40) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_LIC_EVNT_TYP_TP1 PRIMARY KEY(LIC_EVNT_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_LIC_REQS_T
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_REQS_T
/

CREATE TABLE OLE_LIC_REQS_T
(
      LIC_REQS_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , E_RES_REC_DOC_NUM VARCHAR(40) NOT NULL
        , LIC_AGR_ID VARCHAR(100)
        , OWNER VARCHAR(40)
        , AGR_MTH_ID VARCHAR(40)
        , LIC_STAT_CD VARCHAR(40)
        , LIC_DOC_LOCN_ID VARCHAR(40)
        , LIC_REQS_TYP_ID VARCHAR(40)
        , LIC_WRK_FLW_TYP_CD VARCHAR(40)
    
    , CONSTRAINT OLE_LIC_REQS_TP1 PRIMARY KEY(LIC_REQS_ID)





    
                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX LIC_STAT_FK (LIC_STAT_CD )
    
                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX LIC_DOC_LOCN_FK (LIC_DOC_LOCN_ID )
    
                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX LIC_REQS_TYP_FK (LIC_REQS_TYP_ID )
    
                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX LIC_WRK_FLW_TYP_CD (LIC_WRK_FLW_TYP_CD )
    
                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX AGR_MTH_FK (AGR_MTH_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_LIC_REQS_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_REQS_TYP_T
/

CREATE TABLE OLE_LIC_REQS_TYP_T
(
      LIC_REQS_TYP_ID VARCHAR(40)
        , LIC_REQS_TYP_NM VARCHAR(40) NOT NULL
        , LIC_REQS_TYP_DESC VARCHAR(150)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_LIC_REQS_TYP_TP1 PRIMARY KEY(LIC_REQS_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_LIC_RQST_ITM_TITL_T
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_RQST_ITM_TITL_T
/

CREATE TABLE OLE_LIC_RQST_ITM_TITL_T
(
      OLE_ITM_ID VARCHAR(40)
        , OLE_LIC_RQST_ID VARCHAR(40) NOT NULL
        , OLE_LIC_RQST_ITM_UUID VARCHAR(40) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_LIC_RQST_ITM_TITL_TP1 PRIMARY KEY(OLE_ITM_ID)





    
                                                                                                                                                                                            
                                    
, INDEX LIC_REQS_ITM_FK (OLE_LIC_RQST_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_LIC_STAT_T
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_STAT_T
/

CREATE TABLE OLE_LIC_STAT_T
(
      LIC_STAT_CD VARCHAR(40)
        , LIC_STAT_NM VARCHAR(100) NOT NULL
        , LIC_STAT_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_LIC_STAT_TP1 PRIMARY KEY(LIC_STAT_CD)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_LIC_WRK_FLW_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_WRK_FLW_TYP_T
/

CREATE TABLE OLE_LIC_WRK_FLW_TYP_T
(
      LIC_WRK_FLW_TYP_CD VARCHAR(40) default '0'
        , LIC_WRK_FLW_TYP_NM VARCHAR(100) NOT NULL
        , LIC_WRK_FLW_TYP_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_LIC_WRK_FLW_TYP_TP1 PRIMARY KEY(LIC_WRK_FLW_TYP_CD)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_LOCN_LEVEL_T
# -----------------------------------------------------------------------
drop table if exists OLE_LOCN_LEVEL_T
/

CREATE TABLE OLE_LOCN_LEVEL_T
(
      LEVEL_ID VARCHAR(40) default '0'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , LEVEL_CD VARCHAR(40) NOT NULL
        , LEVEL_NAME VARCHAR(100) NOT NULL
        , PARENT_LEVEL VARCHAR(40)
    
    , CONSTRAINT OLE_LOCN_LEVEL_TP1 PRIMARY KEY(LEVEL_ID)

    , CONSTRAINT LEVEL_CD UNIQUE (LEVEL_CD)




    
                                                                                                                                                                                                                
                                    
, INDEX new_fk_constraint (PARENT_LEVEL )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_LOCN_SUM_T
# -----------------------------------------------------------------------
drop table if exists OLE_LOCN_SUM_T
/

CREATE TABLE OLE_LOCN_SUM_T
(
      OLE_LOCN_SUM_ID VARCHAR(40)
        , FILE_NM VARCHAR(40)
        , LOCN_TOT_CNT DECIMAL(8)
        , LOCN_CRE_CNT DECIMAL(8)
        , LOCN_REJ_CNT DECIMAL(8)
        , LOCN_FL_CNT DECIMAL(8)
        , LOCN_UP_CNT DECIMAL(8)
        , LOCN_PRI_NAME VARCHAR(40)
        , LOCN_DATE DATETIME
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_LOCN_SUM_TP1 PRIMARY KEY(OLE_LOCN_SUM_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_LOCN_T
# -----------------------------------------------------------------------
drop table if exists OLE_LOCN_T
/

CREATE TABLE OLE_LOCN_T
(
      LOCN_ID VARCHAR(40) default '0'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , LOCN_CD VARCHAR(40) NOT NULL
        , LOCN_NAME VARCHAR(100) NOT NULL
        , LEVEL_ID VARCHAR(40) NOT NULL
        , PARENT_LOCN_ID VARCHAR(40)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
    
    , CONSTRAINT OLE_LOCN_TP1 PRIMARY KEY(LOCN_ID)

    , CONSTRAINT LOCN_CD UNIQUE (LOCN_CD)




    
                                                                                                                                                                                                                                                        
                                    
, INDEX OLE_LOCN_FK1 (PARENT_LOCN_ID )
    
                                                                                                                                                                                                                                                        
                                    
, INDEX OLE_LOCN_FK2 (LEVEL_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_MTRL_TYPS_T
# -----------------------------------------------------------------------
drop table if exists OLE_MTRL_TYPS_T
/

CREATE TABLE OLE_MTRL_TYPS_T
(
      MTRL_TYPS_ID VARCHAR(10)
        , E_RES_REC_ID VARCHAR(10)
        , MTRL_TYP_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_MTRL_TYPS_TP1 PRIMARY KEY(MTRL_TYPS_ID)





    
                                                                                                                                                                                            
                                    
, INDEX OLE_MTRL_TYPS_FK (MTRL_TYP_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_MTRL_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_MTRL_TYP_T
/

CREATE TABLE OLE_MTRL_TYP_T
(
      MTRL_TYP_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , MTRL_TYP_NM VARCHAR(40)
        , MTRL_TYP_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
    
    , CONSTRAINT OLE_MTRL_TYP_TP1 PRIMARY KEY(MTRL_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_OVERLAY_ACTN_T
# -----------------------------------------------------------------------
drop table if exists OLE_OVERLAY_ACTN_T
/

CREATE TABLE OLE_OVERLAY_ACTN_T
(
      OLE_OVR_ACT_ID VARCHAR(40)
        , OLE_PRFL_NM VARCHAR(100) NOT NULL
        , OLE_OVR_ACT_DESC VARCHAR(1000) NOT NULL
    
    , CONSTRAINT OLE_OVERLAY_ACTN_TP1 PRIMARY KEY(OLE_OVR_ACT_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_OVERLAY_LOOKUP_ACTION_T
# -----------------------------------------------------------------------
drop table if exists OLE_OVERLAY_LOOKUP_ACTION_T
/

CREATE TABLE OLE_OVERLAY_LOOKUP_ACTION_T
(
      ID INTEGER
        , AGENDA_NAME VARCHAR(100)
        , ACTION_ID VARCHAR(100)
        , DESCRIPTION VARCHAR(100)
        , ACTION_TYPE VARCHAR(100)
        , EVENT VARCHAR(100)
        , MAPPING VARCHAR(100)
        , INCOMING_DATA_FIELD VARCHAR(100)
        , INCOMING_SUB_FIELD VARCHAR(100)
        , EXISTING_FIELD VARCHAR(100)
    
    , CONSTRAINT OLE_OVERLAY_LOOKUP_ACTION_TP1 PRIMARY KEY(ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_OVERLAY_LOOKUP_T
# -----------------------------------------------------------------------
drop table if exists OLE_OVERLAY_LOOKUP_T
/

CREATE TABLE OLE_OVERLAY_LOOKUP_T
(
      ID INTEGER
        , OVERLAY_LOOKUP_ACTION_ID INTEGER NOT NULL
        , AGENDA_NAME VARCHAR(100)
        , ACTION_ID VARCHAR(10)
        , MAPPING_TABLE_NAME VARCHAR(100)
        , FIELD VARCHAR(100)
        , NEXT_ACTION VARCHAR(100)
    
    , CONSTRAINT OLE_OVERLAY_LOOKUP_TP1 PRIMARY KEY(ID)





    
                                                                                                                                                                                                
                                    
, INDEX OVERLAY_LOOKUP_ACTION_FK (OVERLAY_LOOKUP_ACTION_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_OVERLAY_MAP_FLD_T
# -----------------------------------------------------------------------
drop table if exists OLE_OVERLAY_MAP_FLD_T
/

CREATE TABLE OLE_OVERLAY_MAP_FLD_T
(
      OLE_MAP_FLD_ID VARCHAR(40)
        , FILE_FMT VARCHAR(40) NOT NULL
        , INCOMING_FLD VARCHAR(10) NOT NULL
        , INCOMING_FLD_VAL VARCHAR(50) NOT NULL
        , OLE_OVR_ACT_ID VARCHAR(40) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(9) NOT NULL
    
    , CONSTRAINT OLE_OVERLAY_MAP_FLD_TP1 PRIMARY KEY(OLE_MAP_FLD_ID)





    
                                                                                                                                                                                                                                    
                                    
, INDEX OLE_OVERLAY_MAP_FLD_CNSTR (OLE_OVR_ACT_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_OVERLAY_OPTION_T
# -----------------------------------------------------------------------
drop table if exists OLE_OVERLAY_OPTION_T
/

CREATE TABLE OLE_OVERLAY_OPTION_T
(
      ID INTEGER
        , AGENDA_NAME VARCHAR(100)
        , OPTION_NAME VARCHAR(100)
    
    , CONSTRAINT OLE_OVERLAY_OPTION_TP1 PRIMARY KEY(ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_OVERLAY_OUT_FLD_T
# -----------------------------------------------------------------------
drop table if exists OLE_OVERLAY_OUT_FLD_T
/

CREATE TABLE OLE_OVERLAY_OUT_FLD_T
(
      OLE_OUT_FLD_ID VARCHAR(40)
        , FLD_NAME VARCHAR(100) NOT NULL
        , FLD_VAL VARCHAR(100) NOT NULL
        , TARGET_FLD VARCHAR(100) NOT NULL
        , OLE_OVR_ACT_ID VARCHAR(40) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , LOOKUP_IND VARCHAR(1) NOT NULL
    
    , CONSTRAINT OLE_OVERLAY_OUT_FLD_TP1 PRIMARY KEY(OLE_OUT_FLD_ID)





    
                                                                                                                                                                                                                                                        
                                    
, INDEX OLE_OVERLAY_OUT_FLD_CNSTR (OLE_OVR_ACT_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PCKG_SCP_T
# -----------------------------------------------------------------------
drop table if exists OLE_PCKG_SCP_T
/

CREATE TABLE OLE_PCKG_SCP_T
(
      PCKG_SCP_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , PCKG_SCP_NM VARCHAR(40)
        , PCKG_SCP_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
    
    , CONSTRAINT OLE_PCKG_SCP_TP1 PRIMARY KEY(PCKG_SCP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PCKG_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_PCKG_TYP_T
/

CREATE TABLE OLE_PCKG_TYP_T
(
      PCKG_TYP_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , PCKG_TYP_NM VARCHAR(40)
        , PCKG_TYP_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
    
    , CONSTRAINT OLE_PCKG_TYP_TP1 PRIMARY KEY(PCKG_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PROFILE_ATTR_T
# -----------------------------------------------------------------------
drop table if exists OLE_PROFILE_ATTR_T
/

CREATE TABLE OLE_PROFILE_ATTR_T
(
      ID INTEGER
        , AGENDA_NAME VARCHAR(100)
        , NAME VARCHAR(100)
        , VALUE VARCHAR(100)
    
    , CONSTRAINT OLE_PROFILE_ATTR_TP1 PRIMARY KEY(ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PROFILE_FACT_T
# -----------------------------------------------------------------------
drop table if exists OLE_PROFILE_FACT_T
/

CREATE TABLE OLE_PROFILE_FACT_T
(
      ID INTEGER
        , AGENDA_NAME VARCHAR(100)
        , TERM_NAME VARCHAR(100)
        , DOC_TYPE VARCHAR(100)
        , INCOMING_FIELD VARCHAR(100)
        , EXISTING_FIELD VARCHAR(100)
    
    , CONSTRAINT OLE_PROFILE_FACT_TP1 PRIMARY KEY(ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PROXY_PTRN_T
# -----------------------------------------------------------------------
drop table if exists OLE_PROXY_PTRN_T
/

CREATE TABLE OLE_PROXY_PTRN_T
(
      OLE_PROXY_PTRN_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , OLE_PTRN_ID VARCHAR(40)
        , OLE_PROXY_PTRN_REF_ID VARCHAR(40) NOT NULL
        , OLE_PROXY_PTRN_EXP_DT DATETIME
        , OLE_PROXY_PTRN_ACT_DT DATETIME
        , ACTV_IND VARCHAR(1)
    
    , CONSTRAINT OLE_PROXY_PTRN_TP1 PRIMARY KEY(OLE_PROXY_PTRN_ID)





    
                                                                                                                                                                                                                                                        
                                    
, INDEX OLE_PROXY_PTRN_FK1 (OLE_PTRN_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PTRN_LOCAL_ID_T
# -----------------------------------------------------------------------
drop table if exists OLE_PTRN_LOCAL_ID_T
/

CREATE TABLE OLE_PTRN_LOCAL_ID_T
(
      OLE_PTRN_LOCAL_SEQ_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , OLE_PTRN_ID VARCHAR(40)
        , LOCAL_ID VARCHAR(40)
    
    , CONSTRAINT OLE_PTRN_LOCAL_ID_TP1 PRIMARY KEY(OLE_PTRN_LOCAL_SEQ_ID)





    
                                                                                                                                                                                            
                                    
, INDEX OLE_PTRN_LOCAL_FK (OLE_PTRN_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PTRN_LOST_BAR_T
# -----------------------------------------------------------------------
drop table if exists OLE_PTRN_LOST_BAR_T
/

CREATE TABLE OLE_PTRN_LOST_BAR_T
(
      OLE_PTRN_LOST_BAR_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , OLE_PTRN_ID VARCHAR(40)
        , OLE_PTRN_LOST_BAR VARCHAR(100)
        , OLE_PTRN_LOST_STATUS VARCHAR(50)
        , OLE_PTRN_LOST_DESC VARCHAR(300)
        , OPTR_CRTE_ID VARCHAR(40)
        , OLE_PTRN_LOST_BAR_EFF_DT DATETIME
        , OLE_PTRN_LOST_BAR_ACTIVE VARCHAR(1)
    
    , CONSTRAINT OLE_PTRN_LOST_BAR_TP1 PRIMARY KEY(OLE_PTRN_LOST_BAR_ID)





    
                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_PTRN_LOST_BAR_TI1 (OLE_PTRN_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PTRN_NTE_T
# -----------------------------------------------------------------------
drop table if exists OLE_PTRN_NTE_T
/

CREATE TABLE OLE_PTRN_NTE_T
(
      OLE_PTRN_NTE_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , OLE_PTRN_ID VARCHAR(40)
        , OPTR_ID VARCHAR(40)
        , NTE_CRT_OR_UPDT_DATE DATETIME
        , OLE_PTRN_NTE_TYP_ID VARCHAR(40)
        , OLE_PTRN_NTE_TXT VARCHAR(800)
        , ACTV_IND VARCHAR(1) NOT NULL
    
    , CONSTRAINT OLE_PTRN_NTE_TP1 PRIMARY KEY(OLE_PTRN_NTE_ID)





    
                                                                                                                                                                                                                                                                            
                                    
, INDEX OLE_PTRN_NTE_FK1 (OLE_PTRN_ID )
    
                                                                                                                                                                                                                                                                            
                                    
, INDEX OLE_PTRN_NTE_FK2 (OLE_PTRN_NTE_TYP_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PTRN_NTE_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_PTRN_NTE_TYP_T
/

CREATE TABLE OLE_PTRN_NTE_TYP_T
(
      OLE_PTRN_NTE_TYP_ID VARCHAR(40)
        , OLE_PTRN_NTE_TYP_NM VARCHAR(100)
        , OLE_PTRN_NTE_TYPE_CD VARCHAR(8)
        , OBJ_ID VARCHAR(36)
        , ACTV_IND VARCHAR(1)
    
    , CONSTRAINT OLE_PTRN_NTE_TYP_TP1 PRIMARY KEY(OLE_PTRN_NTE_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PTRN_PAY_STA_T
# -----------------------------------------------------------------------
drop table if exists OLE_PTRN_PAY_STA_T
/

CREATE TABLE OLE_PTRN_PAY_STA_T
(
      PAY_STA_ID VARCHAR(40) default '0'
        , PAY_STA_CODE VARCHAR(40) NOT NULL
        , PAY_STA_NAME VARCHAR(40) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_PTRN_PAY_STA_TP1 PRIMARY KEY(PAY_STA_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PTRN_SUM_T
# -----------------------------------------------------------------------
drop table if exists OLE_PTRN_SUM_T
/

CREATE TABLE OLE_PTRN_SUM_T
(
      OLE_PTRN_SUM_ID VARCHAR(40)
        , FILE_NM VARCHAR(256)
        , PTRN_TOT_CNT DECIMAL(8)
        , PTRN_CRE_CNT DECIMAL(8)
        , PTRN_REJ_CNT DECIMAL(8)
        , PTRN_FL_CNT DECIMAL(8)
        , PTRN_UP_CNT DECIMAL(8)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , PRNCPL_NM VARCHAR(100)
        , CRT_DT DATETIME
    
    , CONSTRAINT OLE_PTRN_SUM_TP1 PRIMARY KEY(OLE_PTRN_SUM_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PTRN_T
# -----------------------------------------------------------------------
drop table if exists OLE_PTRN_T
/

CREATE TABLE OLE_PTRN_T
(
      OLE_PTRN_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , BARCODE VARCHAR(100)
        , BORR_TYP VARCHAR(40) NOT NULL
        , ACTV_IND VARCHAR(1)
        , GENERAL_BLOCK VARCHAR(1)
        , PAGING_PRIVILEGE VARCHAR(1)
        , COURTESY_NOTICE VARCHAR(1)
        , DELIVERY_PRIVILEGE VARCHAR(1)
        , CHECKOUT_RECEIPT_OPT_OUT VARCHAR(1)
        , EXPIRATION_DATE DATETIME
        , ACTIVATION_DATE DATETIME
        , GENERAL_BLOCK_NT VARCHAR(250)
        , INV_BARCODE_NUM VARCHAR(20)
        , INV_BARCODE_NUM_EFF_DATE DATETIME
        , OLE_SRC VARCHAR(40)
        , OLE_STAT_CAT VARCHAR(40)
        , PHOTOGRAPH LONGBLOB
    
    , CONSTRAINT OLE_PTRN_TP1 PRIMARY KEY(OLE_PTRN_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX OLE_DLVR_BORR_FK2 (BORR_TYP )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX OLE_DLVR_SRC_FK3 (OLE_SRC )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX OLE_DLVR_STAT_CAT_FK4 (OLE_STAT_CAT )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX OLE_DLVR_PTRN_BRCD_FK6 (BARCODE )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_PYMT_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_PYMT_TYP_T
/

CREATE TABLE OLE_PYMT_TYP_T
(
      PYMT_TYP_ID VARCHAR(10)
        , PYMT_TYP_NM VARCHAR(40)
        , PYMT_TYP_DESC VARCHAR(100)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ROW_ACT_IND VARCHAR(1)
    
    , CONSTRAINT OLE_PYMT_TYP_TP1 PRIMARY KEY(PYMT_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_REQ_PRTY_T
# -----------------------------------------------------------------------
drop table if exists OLE_REQ_PRTY_T
/

CREATE TABLE OLE_REQ_PRTY_T
(
      REQ_PRTY_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
        , REQ_PRTY_NM VARCHAR(40)
        , REQ_PRTY_DESC VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
    
    , CONSTRAINT OLE_REQ_PRTY_TP1 PRIMARY KEY(REQ_PRTY_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_SER_RCV_HIS_REC
# -----------------------------------------------------------------------
drop table if exists OLE_SER_RCV_HIS_REC
/

CREATE TABLE OLE_SER_RCV_HIS_REC
(
      SER_RCPT_HIS_REC_ID VARCHAR(40)
        , SER_RCV_REC_ID VARCHAR(40)
        , RCV_REC_TYP VARCHAR(40)
        , CHRON_LVL_1 VARCHAR(50)
        , CHRON_LVL_2 VARCHAR(50)
        , CHRON_LVL_3 VARCHAR(50)
        , CHRON_LVL_4 VARCHAR(50)
        , CLAIM_COUNT VARCHAR(50)
        , CLAIM_DATE DATETIME
        , CLAIM_NOTE VARCHAR(100)
        , CLAIM_TYPE VARCHAR(40)
        , CLAIM_RESP VARCHAR(40)
        , ENUM_LVL_1 VARCHAR(50)
        , ENUM_LVL_2 VARCHAR(50)
        , ENUM_LVL_3 VARCHAR(50)
        , ENUM_LVL_4 VARCHAR(50)
        , ENUM_LVL_5 VARCHAR(50)
        , ENUM_LVL_6 VARCHAR(50)
        , PUB_DISPLAY VARCHAR(1)
        , SER_RCPT_NOTE VARCHAR(100)
        , OPTR_ID VARCHAR(40)
        , MACH_ID VARCHAR(100)
        , RCPT_STAT VARCHAR(40)
        , RCPT_DATE DATETIME
        , PUB_RCPT VARCHAR(40)
        , STAFF_ONLY_RCPT VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_SER_RCV_HIS_RECP1 PRIMARY KEY(SER_RCPT_HIS_REC_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
                                    
, INDEX FK_SER_ID (SER_RCV_REC_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_SER_RCV_REC
# -----------------------------------------------------------------------
drop table if exists OLE_SER_RCV_REC
/

CREATE TABLE OLE_SER_RCV_REC
(
      SER_RCV_REC_ID VARCHAR(40)
        , FDOC_NBR VARCHAR(14)
        , BIB_ID VARCHAR(40)
        , RCV_REC_TYP VARCHAR(40)
        , CLAIM VARCHAR(1)
        , CLAIM_INTRVL_INFO VARCHAR(500)
        , CREATE_ITEM VARCHAR(1)
        , GEN_RCV_NOTE VARCHAR(500)
        , INSTANCE_ID VARCHAR(40)
        , PO_ID VARCHAR(50)
        , PRINT_LBL VARCHAR(1)
        , PUBLIC_DISPLAY VARCHAR(1)
        , SER_RCPT_LOC VARCHAR(40)
        , SER_RCV_REC VARCHAR(40)
        , SUBSCR_STAT VARCHAR(40)
        , TREATMENT_INSTR_NOTE VARCHAR(500)
        , UNBOUND_LOC VARCHAR(100)
        , URGENT_NOTE VARCHAR(500)
        , VENDOR VARCHAR(30)
        , CREATE_DATE DATETIME
        , OPTR_ID VARCHAR(40)
        , MACH_ID VARCHAR(100)
        , SUBSCR_STAT_DT DATETIME
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
        , ACTIVE VARCHAR(1)
    
    , CONSTRAINT OLE_SER_RCV_RECP1 PRIMARY KEY(SER_RCV_REC_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_SER_RCV_REC_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_SER_RCV_REC_TYP_T
/

CREATE TABLE OLE_SER_RCV_REC_TYP_T
(
      SER_RCV_REC_TYP_ID VARCHAR(40)
        , SER_RCV_REC_ID VARCHAR(40)
        , RCV_REC_TYP VARCHAR(40)
        , ACTN_DATE DATETIME
        , ACTN_INTRVL VARCHAR(40)
        , CHRON_CAPTN_LVL1 VARCHAR(50)
        , CHRON_CAPTN_LVL2 VARCHAR(50)
        , CHRON_CAPTN_LVL3 VARCHAR(50)
        , CHRON_CAPTN_LVL4 VARCHAR(50)
        , ENUM_CAPTN_LVL1 VARCHAR(50)
        , ENUM_CAPTN_LVL2 VARCHAR(50)
        , ENUM_CAPTN_LVL3 VARCHAR(50)
        , ENUM_CAPTN_LVL4 VARCHAR(50)
        , ENUM_CAPTN_LVL5 VARCHAR(50)
        , ENUM_CAPTN_LVL6 VARCHAR(50)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT OLE_SER_RCV_REC_TYP_TP1 PRIMARY KEY(SER_RCV_REC_TYP_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                                                            
                                    
, INDEX FK_SER_TPY_ID (SER_RCV_REC_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_VNDR_ACC_INFO_T
# -----------------------------------------------------------------------
drop table if exists OLE_VNDR_ACC_INFO_T
/

CREATE TABLE OLE_VNDR_ACC_INFO_T
(
      OLE_VNDR_ACC_INFO_ID VARCHAR(40) default '0'
        , VNDR_REF_NUMBER VARCHAR(100) NOT NULL
        , ACC_NUM VARCHAR(100) NOT NULL
        , OBJ_CD VARCHAR(100) NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_VNDR_ACC_INFO_TP1 PRIMARY KEY(OLE_VNDR_ACC_INFO_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_PRF_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_PRF_T
/

CREATE TABLE OLE_BAT_PRCS_PRF_T
(
      BAT_PRCS_PRF_ID VARCHAR(40)
        , BAT_PRCS_PRF_NM VARCHAR(50)
        , BAT_PRCS_PRF_DESC VARCHAR(1000)
        , OLE_BAT_PRCS_TYP VARCHAR(100)
        , KRMS_PRFL_NM VARCHAR(100)
        , BAT_PRCS_PRF_DT_TO_IMPRT VARCHAR(50)
        , BAT_PRCS_PRF_DT_TO_EXPRT VARCHAR(50)
        , BAT_PRCS_PRF_EXPRT_SCP VARCHAR(50)
        , BAT_PRCS_PRF_REQ_FR_TITL VARCHAR(50)
        , BAT_PRCS_BIB_OVRL_ADD VARCHAR(50)
        , BAT_PRCS_BIB_NO_MTCH VARCHAR(50)
        , BAT_PRCS_INST_OVRL_ADD VARCHAR(50)
        , BAT_PRCS_INST_NO_MTCH VARCHAR(50)
        , BAT_PRCS_NEW_BIB_STS VARCHAR(50)
        , BAT_PRCS_EXST_BIB_STS VARCHAR(50)
        , BAT_PRCS_NOCHNG_SET VARCHAR(50)
        , BAT_PRCS_OVERLAY_NOCHNG_SET VARCHAR(50)
        , IS_OVERLAY_BIB_STF_ONLY VARCHAR(1)
        , IS_BIB_STF_ONLY VARCHAR(1)
        , IS_INST_STF_ONLY VARCHAR(1)
        , IS_ITM_STF_ONLY VARCHAR(1)
        , BAT_PRCS_DNT_CHNG_001 VARCHAR(50)
        , PRPND_003_TO_035 VARCHAR(20)
        , PRPND_VAL_TO_035 VARCHAR(20)
        , BAT_PRCS_VAL_TO_PRPND VARCHAR(50)
        , BAT_PRCS_RMV_IND VARCHAR(20)
        , BAT_PRCS_VAL_TO_RMV VARCHAR(20)
        , BAT_PRCS_MARC_ONLY VARCHAR(1)
        , BAT_PRCS_BIB_IMP_PRF VARCHAR(100)
        , BAT_PRCS_MATCH_PROFILE LONGTEXT
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_PRCS_PRF_TP1 PRIMARY KEY(BAT_PRCS_PRF_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_PRF_MTCH_POINT_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_PRF_MTCH_POINT_T
/

CREATE TABLE OLE_BAT_PRCS_PRF_MTCH_POINT_T
(
      MATCH_POINT_ID VARCHAR(40)
        , CAS_MATCH_POINT VARCHAR(40)
        , MATCH_POINT VARCHAR(100)
        , MATCH_POINT_TYPE VARCHAR(40)
        , BAT_PRCS_PRF_ID VARCHAR(40) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_PRCS_PRF_MTCH_POINTP1 PRIMARY KEY(MATCH_POINT_ID)





    
                                                                                                                                                                                                                                    
                                    
, INDEX BAT_PRF_MTCH_PNT_I (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_PRFLE_CNST_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_PRFLE_CNST_T
/

CREATE TABLE OLE_BAT_PRCS_PRFLE_CNST_T
(
      OLE_USR_DEF_VAL_ID VARCHAR(40)
        , ATT_NM VARCHAR(100)
        , ATT_VAL VARCHAR(500)
        , DATA_TYPE VARCHAR(20)
        , BAT_PRCS_PRF_ID VARCHAR(40) NOT NULL
        , DEF_VAL VARCHAR(20)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_PRCS_PRFLE_CNST_TP1 PRIMARY KEY(OLE_USR_DEF_VAL_ID)





    
                                                                                                                                                                                                                                                        
                                    
, INDEX BAT_PRCS_PRFL_CNST_FK_KEY (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_TYP_T
/

CREATE TABLE OLE_BAT_PRCS_TYP_T
(
      OLE_BAT_PRCS_TYP_ID VARCHAR(40)
        , OLE_BAT_PRCS_TYP_CODE VARCHAR(45) NOT NULL
        , OLE_BAT_PRCS_TYP_NM VARCHAR(100) NOT NULL
        , ACTIVE_IND VARCHAR(1) NOT NULL
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
    
    , CONSTRAINT OLE_BAT_PRCS_TYP_TP1 PRIMARY KEY(OLE_BAT_PRCS_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_DT_MAP_OPT_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_DT_MAP_OPT_T
/

CREATE TABLE OLE_BAT_PRCS_DT_MAP_OPT_T
(
      BATCH_PRCS_DT_MAP_OPT_ID VARCHAR(40)
        , BAT_PRCS_DT_MAP_DATA_TYP VARCHAR(10)
        , DATA_TYP_DEST_FLD VARCHAR(15)
        , SRC_FLD VARCHAR(70)
        , SRC_VAL VARCHAR(70)
        , DEST_FLD VARCHAR(70)
        , DEST_VAL VARCHAR(70)
        , GOKB_FIELD VARCHAR(100)
        , PRIORITY INTEGER default 1
        , IS_LOOKUP VARCHAR(1)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , OLE_BAT_PRCS_DT_MAP_ID VARCHAR(40) NOT NULL
    
    , CONSTRAINT OLE_BAT_PRCS_DT_MAP_OPT_TP1 PRIMARY KEY(BATCH_PRCS_DT_MAP_OPT_ID)





    
                                                                                                                                                                                                                                                                                                                                                            
                                    
, INDEX MAP_FK_CONSTRAINT (OLE_BAT_PRCS_DT_MAP_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_DT_MAP_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_DT_MAP_T
/

CREATE TABLE OLE_BAT_PRCS_DT_MAP_T
(
      OLE_BAT_PRCS_DT_MAP_ID VARCHAR(40)
        , OLE_BAT_PRCS_DT_MAP_OPTN_NUM INTEGER
        , BAT_PRCS_PRF_ID VARCHAR(40) NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_PRCS_DT_MAP_TP1 PRIMARY KEY(OLE_BAT_PRCS_DT_MAP_ID)





    
                                                                                                                                                                                            
                                    
, INDEX BAT_FK_CONSTRAINT (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_FILTER_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_FILTER_T
/

CREATE TABLE OLE_BAT_PRCS_FILTER_T
(
      OLE_BAT_PRCS_FILTER_ID VARCHAR(40)
        , OLE_BAT_PRCS_FILTER_NM VARCHAR(500)
        , OLE_BAT_PRCS_FILTER_VAL VARCHAR(500)
        , OLE_BAT_PRCS_FILTER_RANGE_FROM VARCHAR(20)
        , OLE_BAT_PRCS_FILTER_RANGE_TO VARCHAR(20)
        , BAT_PRCS_PRF_ID VARCHAR(40)
        , BAT_PRCS_DATA_TYP VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_PRCS_FILTER_TP1 PRIMARY KEY(OLE_BAT_PRCS_FILTER_ID)





    
                                                                                                                                                                                                                                                                            
                                    
, INDEX BAT_PRCS_FILTER_FK_KEY (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_FLE_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_FLE_TYP_T
/

CREATE TABLE OLE_BAT_PRCS_FLE_TYP_T
(
      FLE_TYP_ID VARCHAR(40)
        , FLE_TYP_NM VARCHAR(75)
        , FLE_TYP_DESC VARCHAR(100)
        , ACT_IND VARCHAR(1)
        , OBJ_ID VARCHAR(36)
        , VER_NBR VARCHAR(36)
    
    , CONSTRAINT OLE_BAT_PRCS_FLE_TYP_TP1 PRIMARY KEY(FLE_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_MNTN_FIELD_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_MNTN_FIELD_T
/

CREATE TABLE OLE_BAT_PRCS_MNTN_FIELD_T
(
      OLE_BAT_FIELD_ID VARCHAR(40)
        , OLE_BAT_FIELD_DISPLY_NM VARCHAR(50)
        , OLE_BAT_FIELD_NM VARCHAR(50)
        , OLE_BAT_FIELD_TYP VARCHAR(50)
        , ACT_IND VARCHAR(1)
        , OBJ_ID VARCHAR(36)
        , VER_NBR VARCHAR(36)
    
    , CONSTRAINT OLE_BAT_PRCS_MNTN_FIELD_TP1 PRIMARY KEY(OLE_BAT_FIELD_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_PRCT_FLD_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_PRCT_FLD_T
/

CREATE TABLE OLE_BAT_PRCS_PRCT_FLD_T
(
      USR_DEF_PRCT_FLD_ID VARCHAR(40)
        , DATA_TYP VARCHAR(10)
        , USR_DEF_PRCT_FLD_TAG VARCHAR(10)
        , USR_DEF_PRCT_FLD_IND1 VARCHAR(5)
        , USR_DEF_PRCT_FLD_IND2 VARCHAR(5)
        , USR_DEF_PRCT_FLD_SUBFLD VARCHAR(3)
        , USR_DEF_PRCT_FLD_SBFLD_CNTS VARCHAR(500)
        , BAT_PRCS_PRF_ID VARCHAR(40)
        , IGN_VAL VARCHAR(1) default 'Y'
        , OLE_GLBLY_PRCT_FLD_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_PRCS_PRCT_FLD_TP1 PRIMARY KEY(USR_DEF_PRCT_FLD_ID)





    
                                                                                                                                                                                                                                                                                                                                        
                                    
, INDEX BAT_PRCS_USR_PRCT_FK_KEY (BAT_PRCS_PRF_ID )
    
                                                                                                                                                                                                                                                                                                                                        
                                    
, INDEX BAT_PRCS_GLBLY_PRCT_FK_KEY (OLE_GLBLY_PRCT_FLD_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRF_BIB_DT_MAP_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRF_BIB_DT_MAP_T
/

CREATE TABLE OLE_BAT_PRF_BIB_DT_MAP_T
(
      OLE_BAT_PRF_BIB_DT_MAP_ID VARCHAR(40)
        , TAG VARCHAR(10)
        , GOKB_FIELD VARCHAR(100)
        , BAT_PRCS_PRF_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_PRF_BIB_DT_MAP_TP1 PRIMARY KEY(OLE_BAT_PRF_BIB_DT_MAP_ID)





    
                                                                                                                                                                                                                
                                    
, INDEX OLE_BAT_PRCS_BIB_DT_MAP_I (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRF_BIB_DT_MAP_OVER_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRF_BIB_DT_MAP_OVER_T
/

CREATE TABLE OLE_BAT_PRF_BIB_DT_MAP_OVER_T
(
      OLE_BAT_PRF_BIB_DT_MAP_OVER_ID VARCHAR(40)
        , TAG VARCHAR(10)
        , ADD_OR_REPLACE VARCHAR(10)
        , BAT_PRCS_PRF_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_PRF_BIB_DT_MAP_OVERP1 PRIMARY KEY(OLE_BAT_PRF_BIB_DT_MAP_OVER_ID)





    
                                                                                                                                                                                                                
                                    
, INDEX OLE_BAT_PRCS_BIB_DT_MAP_OVER_I (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_T
/

CREATE TABLE OLE_BAT_PRCS_T
(
      BAT_PRCS_ID VARCHAR(40)
        , FDOC_NBR VARCHAR(14)
        , USER_NAME VARCHAR(40)
        , BAT_PRCS_NM VARCHAR(200)
        , BAT_PRCS_PRF_NM VARCHAR(40)
        , BAT_PRCS_PRF_TYP VARCHAR(40)
        , BAT_PRCS_KRMS_PRF VARCHAR(40)
        , BAT_PRCS_PRF_ID VARCHAR(40)
        , SRC_FLE_PATH VARCHAR(500)
        , SRC_DIR_PATH VARCHAR(500)
        , SRC_FLE_MSK VARCHAR(200)
        , FLE_FRMT_ID VARCHAR(40)
        , OUTPUT_FRMT VARCHAR(40)
        , DEST_DIR_PATH VARCHAR(500)
        , CHUNK_SIZE INTEGER
        , MAX_REC_PER_FILE INTEGER
        , MAX_NUMBER_OF_THREADS INTEGER
        , EMAIL_IDS VARCHAR(400)
        , CRON_OR_SCHEDULE VARCHAR(50)
        , ONE_TIME_OR_RECUR VARCHAR(50)
        , SCHEDULE_TYPE VARCHAR(50)
        , UPLOAD_FLE_NM VARCHAR(60)
        , BAT_PRCS_MARC_ONLY VARCHAR(1)
        , BAT_PRCS_UNMATCHED_PTRN VARCHAR(1)
    
    , CONSTRAINT OLE_BAT_PRCS_TP1 PRIMARY KEY(BAT_PRCS_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
                                    
, INDEX BAT_PRCS_FK_CONST (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_JOB_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_JOB_T
/

CREATE TABLE OLE_BAT_PRCS_JOB_T
(
      JOB_ID INTEGER(20)
        , JOB_NAME VARCHAR(100)
        , BAT_PRFLE_NM VARCHAR(100)
        , USR_NM VARCHAR(100)
        , UPLOAD_FLE_NM VARCHAR(200)
        , NUM_REC VARCHAR(20)
        , NUM_REC_PRCS VARCHAR(20)
        , START_TIME DATETIME
        , END_TIME DATETIME
        , TIME_SPENT VARCHAR(40)
        , PER_COMPLTED VARCHAR(20)
        , CRTE_TIME DATETIME
        , STATUS VARCHAR(20)
        , STATUS_DESC VARCHAR(400)
        , BAT_PRCS_ID VARCHAR(40)
        , BAT_PRCS_TYPE VARCHAR(40)
        , NUM_SUC_REC VARCHAR(40)
        , NUM_FAL_REC VARCHAR(40)
        , HSTRY_SUC_CNT VARCHAR(40)
        , HSTRY_FAL_CNT VARCHAR(40)
        , TYPE_SUC_CNT VARCHAR(40)
        , TYPE_FAL_CNT VARCHAR(40)
        , CREATE_BIB_CNT INTEGER(20)
        , UPDATE_BIB_CNT INTEGER(20)
        , CREATE_HLD_CNT INTEGER(20)
        , ORD_RCD_SUCC_CNT INTEGER(20)
        , ORD_RCD_FAIL_CNT INTEGER(20)
        , NUM_EINSTANCES_ADDED INTEGER(20)
        , NUM_EINSTANCES_DELETED INTEGER(20)
        , NUM_EINSTANCES_WITH_NO_LINK INTEGER(20)
        , NUM_BIBS_GT1_EINSTANCES INTEGER(20)
        , NUM_EHOL_WITH_NO_PLAT INTEGER(20)
        , NUM_EHOL_WITH_NO_ERES INTEGER(20)
    
    , CONSTRAINT OLE_BAT_PRCS_JOB_TP1 PRIMARY KEY(JOB_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
                                    
, INDEX PRCS_FK_CONSTRAINT (BAT_PRCS_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_GLBLY_PRCT_FLD_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_GLBLY_PRCT_FLD_T
/

CREATE TABLE OLE_BAT_GLBLY_PRCT_FLD_T
(
      GLBY_PRCT_FLD_ID VARCHAR(40)
        , BAT_GLBY_PRCT_FLD_ID VARCHAR(10)
        , BAT_GLBY_PRCT_FLD_TAG VARCHAR(5)
        , BAT_GLBY_PRCT_FLD_IND1 VARCHAR(3)
        , BAT_GLBY_PRCT_FLD_IND2 VARCHAR(3)
        , BAT_GLBY_PRCT_FLD_SUBFLD VARCHAR(3)
        , BAT_GLBY_PRCT_FLD_IGN_VAL VARCHAR(1)
        , BAT_PRCS_PRF_ID VARCHAR(40)
    
    , CONSTRAINT OLE_BAT_GLBLY_PRCT_FLD_TP1 PRIMARY KEY(GLBY_PRCT_FLD_ID)





    
                                                                                                                                                                                                                                                        
                                    
, INDEX OLE_BAT_PRCS_GLBLY_PRCT_FK (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_SCHDULE_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_SCHDULE_T
/

CREATE TABLE OLE_BAT_PRCS_SCHDULE_T
(
      OLE_BAT_PRCS_SCHDULE_ID VARCHAR(40)
        , BAT_PRCS_ID VARCHAR(40) NOT NULL
        , USR_NM VARCHAR(100)
        , UPLOAD_FLE_NM VARCHAR(200)
        , CRTE_TIME DATETIME
        , OLE_BAT_PRCS_BAT_TYP VARCHAR(40)
        , OLE_BAT_PRCS_ONE_OR_RECUR VARCHAR(40)
        , OLE_BAT_PRCS_ONE_DATE DATETIME
        , OLE_BAT_PRCS_ONE_TIME VARCHAR(40)
        , OLE_BAT_PRCS_SCHDULE_TYPE VARCHAR(40)
        , OLE_BAT_PRCS_START_TIME VARCHAR(40)
        , OLE_BAT_PRCS_WEEK_DAYS VARCHAR(40)
        , OLE_BAT_PRCS_DAY_NUM VARCHAR(40)
        , OLE_BAT_PRCS_MONTH_NUM VARCHAR(40)
        , OLE_BAT_PRCS_CRON_EXP VARCHAR(100)
    
    , CONSTRAINT OLE_BAT_PRCS_SCHDULE_TP1 PRIMARY KEY(OLE_BAT_PRCS_SCHDULE_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX FK_BAT_PRCS_ID (BAT_PRCS_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRF_DEL_FLD_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRF_DEL_FLD_T
/

CREATE TABLE OLE_BAT_PRF_DEL_FLD_T
(
      BAT_PRF_DEL_ID VARCHAR(40)
        , BAT_PRCS_PRF_ID VARCHAR(40)
        , BAT_PRF_DEL_TAG VARCHAR(5)
        , BAT_PRF_DEL_IND1 VARCHAR(3)
        , BAT_PRF_DEL_IND2 VARCHAR(3)
        , BAT_PRF_DEL_SUBFLD VARCHAR(3)
        , BAT_PRF_DEL_SUB_CNTN VARCHAR(100)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_PRF_DEL_FLD_TP1 PRIMARY KEY(BAT_PRF_DEL_ID)





    
                                                                                                                                                                                                                                                                            
                                    
, INDEX OLE_BAT_PRCS_DEL_FK (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRF_RNM_FLD_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRF_RNM_FLD_T
/

CREATE TABLE OLE_BAT_PRF_RNM_FLD_T
(
      BAT_PRF_RNM_ID VARCHAR(40)
        , BAT_PRCS_PRF_ID VARCHAR(40)
        , BAT_PRF_ORGNL_TAG VARCHAR(5)
        , BAT_PRF_ORGNL_IND1 VARCHAR(3)
        , BAT_PRF_ORGNL_IND2 VARCHAR(3)
        , BAT_PRF_ORGNL_SUBFLD VARCHAR(3)
        , BAT_PRF_ORG_SUB_CNTN VARCHAR(100)
        , BAT_PRF_RNM_TAG VARCHAR(5)
        , BAT_PRF_RNM_IND1 VARCHAR(3)
        , BAT_PRF_RNM_IND2 VARCHAR(3)
        , BAT_PRF_RNM_SUBFLD VARCHAR(3)
        , BAT_PRF_RNM_SUB_CNTN VARCHAR(100)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_PRF_RNM_FLD_TP1 PRIMARY KEY(BAT_PRF_RNM_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_BAT_PRCS_RNM_FK (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_INST_MTCH_PNT_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_INST_MTCH_PNT_T
/

CREATE TABLE OLE_BAT_INST_MTCH_PNT_T
(
      BAT_INST_MTCH_PNT_ID VARCHAR(40)
        , BAT_INST_MTCH_PNT_NME VARCHAR(100)
        , BAT_PRCS_PRF_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_INST_MTCH_PNT_TP1 PRIMARY KEY(BAT_INST_MTCH_PNT_ID)





    
                                                                                                                                                                                            
                                    
, INDEX BAT_INST_MTCH_PNT_FK (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_BIB_MTCH_PNT_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_BIB_MTCH_PNT_T
/

CREATE TABLE OLE_BAT_BIB_MTCH_PNT_T
(
      BAT_BIB_MTCH_PNT_ID VARCHAR(40)
        , BAT_BIB_MTCH_PNT_NME VARCHAR(100)
        , BAT_PRCS_PRF_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_BIB_MTCH_PNT_TP1 PRIMARY KEY(BAT_BIB_MTCH_PNT_ID)





    
                                                                                                                                                                                            
                                    
, INDEX BAT_BIB_MTCH_PNT_FK (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_PRCS_BIB_STUS_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_BIB_STUS_T
/

CREATE TABLE OLE_BAT_PRCS_BIB_STUS_T
(
      BAT_PRCS_BIB_STUS_ID VARCHAR(40)
        , BAT_PRCS_BIB_STUS_NME VARCHAR(100)
        , BAT_PRCS_PRF_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_PRCS_BIB_STUS_TP1 PRIMARY KEY(BAT_PRCS_BIB_STUS_ID)





    
                                                                                                                                                                                            
                                    
, INDEX BAT_BIB_STS_FK (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_BIB_WRK_UNT_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_BIB_WRK_UNT_T
/

CREATE TABLE OLE_BAT_BIB_WRK_UNT_T
(
      BAT_BIB_WRK_UNT_ID VARCHAR(40)
        , BAT_BIB_WRK_UNT_NME VARCHAR(100)
        , BAT_PRCS_PRF_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_BIB_WRK_UNT_TP1 PRIMARY KEY(BAT_BIB_WRK_UNT_ID)





    
                                                                                                                                                                                            
                                    
, INDEX BAT_BIB_WRK_UNT_FK (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_BAT_INST_WRK_UNT_T
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_INST_WRK_UNT_T
/

CREATE TABLE OLE_BAT_INST_WRK_UNT_T
(
      BAT_INST_WRK_UNT_ID VARCHAR(40)
        , BAT_INST_WRK_UNT_NME VARCHAR(100)
        , BAT_PRCS_PRF_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_BAT_INST_WRK_UNT_TP1 PRIMARY KEY(BAT_INST_WRK_UNT_ID)





    
                                                                                                                                                                                            
                                    
, INDEX BAT_INST_WRK_UNT_FK (BAT_PRCS_PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CLNDR_T
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_T
/

CREATE TABLE OLE_CLNDR_T
(
      OLE_CLNDR_ID VARCHAR(40)
        , OLE_CLNDR_DESC VARCHAR(100)
        , CL_SEQ VARCHAR(40)
        , OLE_CLNDR_GRP_ID VARCHAR(40)
        , BEGIN_DT DATETIME
        , END_DT DATETIME
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_CLNDR_TP1 PRIMARY KEY(OLE_CLNDR_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CLNDR_EXP_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_EXP_TYP_T
/

CREATE TABLE OLE_CLNDR_EXP_TYP_T
(
      EXP_TYP_ID VARCHAR(40)
        , EXP_TYP_CODE VARCHAR(100)
        , EXP_TYP_NAME VARCHAR(40)
        , EXP_TYP_IND VARCHAR(1) default 'Y' NOT NULL
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_CLNDR_EXP_TYP_TP1 PRIMARY KEY(EXP_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CLNDR_EXCP_DAT_T
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_EXCP_DAT_T
/

CREATE TABLE OLE_CLNDR_EXCP_DAT_T
(
      OLE_CLNDR_EXCP_DAT_ID VARCHAR(40)
        , OLE_CLNDR_ID VARCHAR(40)
        , OLE_CLNDR_EXCPTN_DAT DATETIME
        , EXCPTN_DESC VARCHAR(40)
        , EXCPTN_TYP VARCHAR(40)
        , OPN_TIM VARCHAR(40)
        , CLOS_TIM VARCHAR(40)
        , OPN_TIM_SESS VARCHAR(2)
        , CLOS_TIM_SESS VARCHAR(2)
    
    , CONSTRAINT OLE_CLNDR_EXCP_DAT_TP1 PRIMARY KEY(OLE_CLNDR_EXCP_DAT_ID)





    
                                                                                                                                                                                                                                                                            
                                    
, INDEX OLE_CLNDR_EXCP_DAT_CN1 (OLE_CLNDR_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CLNDR_WK_T
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_WK_T
/

CREATE TABLE OLE_CLNDR_WK_T
(
      OLE_CLNDR_WK_ID VARCHAR(40)
        , OLE_CLNDR_ID VARCHAR(40)
        , OPN_TIM VARCHAR(40)
        , CLOS_TIM VARCHAR(40)
        , FROM_DAY VARCHAR(40)
        , TO_DAY VARCHAR(40)
        , OPN_TIM_SESS VARCHAR(40)
        , CLOS_TIM_SESS VARCHAR(40)
        , WEEK_ACTIVE VARCHAR(40)
    
    , CONSTRAINT OLE_CLNDR_WK_TP1 PRIMARY KEY(OLE_CLNDR_WK_ID)





    
                                                                                                                                                                                                                                                                            
                                    
, INDEX OLE_CLNDR_WK_CN1 (OLE_CLNDR_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CLNDR_EXCP_PRD_T
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_EXCP_PRD_T
/

CREATE TABLE OLE_CLNDR_EXCP_PRD_T
(
      OLE_CLNDR_EXCP_PRD_ID VARCHAR(40)
        , OLE_CLNDR_ID VARCHAR(40)
        , OLE_CLNDR_EXCPTN_PRD_DESC VARCHAR(100)
        , OLE_CLNDR_EXCPTN_PRD_WEEK_ID VARCHAR(40)
        , EXP_PRD_TYP VARCHAR(40)
        , BEGIN_DT DATETIME
        , END_DT DATETIME
    
    , CONSTRAINT OLE_CLNDR_EXCP_PRD_TP1 PRIMARY KEY(OLE_CLNDR_EXCP_PRD_ID)





    
                                                                                                                                                                                                                                    
                                    
, INDEX OLE_CLNDR_EXCP_PRD_CN1 (OLE_CLNDR_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CLNDR_EXCP_PRD_WK_T
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_EXCP_PRD_WK_T
/

CREATE TABLE OLE_CLNDR_EXCP_PRD_WK_T
(
      OLE_CLNDR_EXCP_PRD_WK_ID VARCHAR(40)
        , OLE_CLNDR_EXCP_PRD_ID VARCHAR(40)
        , FROM_DAY VARCHAR(40)
        , TO_DAY VARCHAR(40)
        , OPN_TIM VARCHAR(40)
        , CLOS_TIM VARCHAR(40)
        , OPN_TIM_SESS VARCHAR(2)
        , CLOS_TIM_SESS VARCHAR(2)
        , PERIOD_WEEK_ACTIVE VARCHAR(40)
    
    , CONSTRAINT OLE_CLNDR_EXCP_PRD_WK_TP1 PRIMARY KEY(OLE_CLNDR_EXCP_PRD_WK_ID)





    
                                                                                                                                                                                                                                                                            
                                    
, INDEX OLE_CLNDR_EXCP_PRD_WK_CN1 (OLE_CLNDR_EXCP_PRD_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CLNDR_GRP_T
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_GRP_T
/

CREATE TABLE OLE_CLNDR_GRP_T
(
      OLE_CLNDR_GRP_ID VARCHAR(40) default '0'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , CLNDR_GRP_CD VARCHAR(40) NOT NULL
        , CLNDR_GRP_NM VARCHAR(200) NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_CLNDR_GRP_TP1 PRIMARY KEY(OLE_CLNDR_GRP_ID)

    , CONSTRAINT CLNDR_GRP_CD UNIQUE (CLNDR_GRP_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CNCL_RSN_T
# -----------------------------------------------------------------------
drop table if exists OLE_CNCL_RSN_T
/

CREATE TABLE OLE_CNCL_RSN_T
(
      CNCL_RSN_ID VARCHAR(40) default '0'
        , CNCL_RSN_NM VARCHAR(100) NOT NULL
        , CNCL_RSN_TXT VARCHAR(100)
        , ROW_ACT_IND VARCHAR(1) default 'Y'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_CNCL_RSN_TP1 PRIMARY KEY(CNCL_RSN_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CLAIM_TYP_T
# -----------------------------------------------------------------------
drop table if exists OLE_CLAIM_TYP_T
/

CREATE TABLE OLE_CLAIM_TYP_T
(
      OLE_CLAIM_TYP_ID VARCHAR(40) default '0'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , OLE_CLAIM_TYP_CD VARCHAR(40) NOT NULL
        , OLE_CLAIM_TYP_NM VARCHAR(200) NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
    
    , CONSTRAINT OLE_CLAIM_TYP_TP1 PRIMARY KEY(OLE_CLAIM_TYP_ID)

    , CONSTRAINT OLE_CLAIM_TYP_CD UNIQUE (OLE_CLAIM_TYP_CD)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_CLM_NTCE_T
# -----------------------------------------------------------------------
drop table if exists OLE_CLM_NTCE_T
/

CREATE TABLE OLE_CLM_NTCE_T
(
      CLM_NTCE_ID VARCHAR(40) default '0'
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
        , CLM_NTCE_SNDR_NM VARCHAR(40)
        , CLM_NTCE_VNDR_NM VARCHAR(40)
        , CLM_NTCE_CLM_DT VARCHAR(40)
        , CLM_NTCE_CLM_CNT VARCHAR(40)
        , CLM_NTCE_CLM_TYP VARCHAR(40)
        , CLM_NTCE_TIT VARCHAR(500)
        , CLM_NTCE_PLC_OF_PUB VARCHAR(500)
        , CLM_NTCE_PUB VARCHAR(500)
        , CLM_NTCE_PUB_DT VARCHAR(40)
        , CLM_NTCE_ENUM VARCHAR(40)
        , CLM_NTCE_CHRON VARCHAR(40)
        , CLM_NTCE_VNDR_LIB_NUM VARCHAR(40)
        , CLM_NTCE_VNDR_ORD_NUM VARCHAR(40)
        , CLM_NTCE_VNDR_TIT_NUM VARCHAR(40)
        , CLM_NTCE_LIB_PO_NUM VARCHAR(40)
        , CLM_NTCE_UN_BND_LOC VARCHAR(40)
    
    , CONSTRAINT OLE_CLM_NTCE_TP1 PRIMARY KEY(CLM_NTCE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DONOR_T
# -----------------------------------------------------------------------
drop table if exists OLE_DONOR_T
/

CREATE TABLE OLE_DONOR_T
(
      DONOR_ID VARCHAR(10)
        , DONOR_CODE VARCHAR(10)
        , DONOR_NAME VARCHAR(255)
        , DONOR_NOTE VARCHAR(40)
        , DONOR_PUBLIC_DISPLAY VARCHAR(4000)
        , DONOR_AMT DECIMAL(19,2)
        , DONOR_BOOKPLATE_URL VARCHAR(40)
        , DONOR_PUBLIC_URL VARCHAR(40)
        , ROW_ACT_IND VARCHAR(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) default 1 NOT NULL
    
    , CONSTRAINT OLE_DONOR_TP1 PRIMARY KEY(DONOR_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_ASR_ITM_T
# -----------------------------------------------------------------------
drop table if exists OLE_ASR_ITM_T
/

CREATE TABLE OLE_ASR_ITM_T
(
      ID VARCHAR(40)
        , ITM_BAR_CD VARCHAR(40)
        , TITLE VARCHAR(37)
        , AUTHOR VARCHAR(37)
        , CALL_NBR VARCHAR(37)
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_ASR_ITM_TP1 PRIMARY KEY(ID)





    
                                                                                                                                                                                                                                    
                                    
, INDEX OLE_ASR_ITM_TI1 (ITM_BAR_CD )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_ASR_RQST_T
# -----------------------------------------------------------------------
drop table if exists OLE_ASR_RQST_T
/

CREATE TABLE OLE_ASR_RQST_T
(
      ID VARCHAR(40)
        , ITM_BAR_CD VARCHAR(40)
        , PTRN_ID VARCHAR(37)
        , PK_UP_LOCN VARCHAR(37)
        , PTRN_NM VARCHAR(37)
        , RQST_ID VARCHAR(37)
        , RQST_STAT VARCHAR(37)
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_ASR_RQST_TP1 PRIMARY KEY(ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_DOC_TYPE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_DOC_TYPE_T
/

CREATE TABLE OLE_DS_DOC_TYPE_T
(
      DOC_TYPE_ID INTEGER
        , NAME VARCHAR(100) NOT NULL
        , DISPLAY_LABEL VARCHAR(100)
        , DESCRIPTION VARCHAR(400)
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_DOC_TYPE_TP1 PRIMARY KEY(DOC_TYPE_ID)

    , CONSTRAINT OLE_DS_DOC_TYPE_TCONST1 UNIQUE (NAME)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_DOC_FORMAT_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_DOC_FORMAT_T
/

CREATE TABLE OLE_DS_DOC_FORMAT_T
(
      DOC_FORMAT_ID INTEGER
        , NAME VARCHAR(100) NOT NULL
        , DISPLAY_LABEL VARCHAR(100)
        , DESCRIPTION VARCHAR(400)
        , DOC_TYPE_ID INTEGER
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_DOC_FORMAT_TP1 PRIMARY KEY(DOC_FORMAT_ID)

    , CONSTRAINT OLE_DS_DOC_FORMAT_TCONST1 UNIQUE (NAME, DOC_TYPE_ID)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_DOC_FIELD_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_DOC_FIELD_T
/

CREATE TABLE OLE_DS_DOC_FIELD_T
(
      DOC_FIELD_ID INTEGER
        , DOC_TYPE_ID INTEGER
        , DOC_FORMAT_ID INTEGER
        , NAME VARCHAR(100)
        , DISPLAY_LABEL VARCHAR(100)
        , DESCRIPTION VARCHAR(400)
        , INCLUDE_PATH VARCHAR(500)
        , EXCLUDE_PATH VARCHAR(500)
        , IS_SEARCH VARCHAR(1)
        , IS_DISPLAY VARCHAR(1)
        , IS_FACET VARCHAR(1)
        , IS_EXPORT VARCHAR(1)
        , IS_GLOBAL_EDIT VARCHAR(1)
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_DOC_FIELD_TP1 PRIMARY KEY(DOC_FIELD_ID)

    , CONSTRAINT OLE_DS_DOC_FIELD_TCONST1 UNIQUE (NAME, DOC_TYPE_ID, DOC_FORMAT_ID)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_SEARCH_RESULT_PAGE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_SEARCH_RESULT_PAGE_T
/

CREATE TABLE OLE_DS_SEARCH_RESULT_PAGE_T
(
      DOC_SEARCH_PAGE_SIZE_ID INTEGER
        , PAGE_SIZE INTEGER
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_SEARCH_RESULT_PAGE_TP1 PRIMARY KEY(DOC_SEARCH_PAGE_SIZE_ID)

    , CONSTRAINT PAGE_SIZE UNIQUE (PAGE_SIZE)





) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DS_SEARCH_FACET_SIZE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DS_SEARCH_FACET_SIZE_T
/

CREATE TABLE OLE_DS_SEARCH_FACET_SIZE_T
(
      DOC_SEARCH_FACET_SIZE_ID INTEGER
        , SHORT_SIZE INTEGER
        , LONG_SIZE INTEGER
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
        , DATE_UPDATED DATETIME
    
    , CONSTRAINT OLE_DS_SEARCH_FACET_SIZE_TP1 PRIMARY KEY(DOC_SEARCH_FACET_SIZE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_LOAN_NOTICE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_LOAN_NOTICE_T
/

CREATE TABLE OLE_DLVR_LOAN_NOTICE_T
(
      ID VARCHAR(40)
        , LOAN_ID VARCHAR(40)
        , PTRN_ID VARCHAR(37)
        , NTC_TYP VARCHAR(100)
        , NTC_CNTNT_CONFIG_NAME VARCHAR(400)
        , NTC_SND_TYP VARCHAR(37)
        , RQST_ID VARCHAR(37)
        , RPLCMNT_FEE_AMNT DECIMAL(8)
        , LOST_ITM_PRCS_FEE_AMNT DECIMAL(8)
        , NTC_TO_SND_DT DATETIME
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_DLVR_LOAN_NOTICE_TP1 PRIMARY KEY(ID)





    
                                                                                                                                                                                                                                                                                                    
                                    
, INDEX NTC_TO_SND_DT_INDX (NTC_TO_SND_DT )
    
                                                                                                                                                                                                                                                                                                                                        
                                    
, INDEX NTC_TYP_INDX (NTC_TYP )
    
                                                                                                                                                                                                                                                                                                                                        
                                    
, INDEX PTRN_ID_INDX (PTRN_ID )
    
                                                                                                                                                                                                                                                                                                                                        
                                    
, INDEX ODLN_LOAN_ID_INDX (LOAN_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DLVR_LOAN_NOTICE_HSTRY_T
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_LOAN_NOTICE_HSTRY_T
/

CREATE TABLE OLE_DLVR_LOAN_NOTICE_HSTRY_T
(
      ID VARCHAR(40)
        , LOAN_ID VARCHAR(40)
        , PTRN_ID VARCHAR(37)
        , RQST_ID VARCHAR(37)
        , NTC_SNT_DT DATETIME
        , NTC_TYP VARCHAR(100)
        , NTC_SND_TYP VARCHAR(37)
        , NTC_CNTNT LONGTEXT
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_DLVR_LOAN_NOTICE_HSTRY_P1 PRIMARY KEY(ID)





    
                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_DLVR_LOAN_NOTICE_HSTRY_TI1 (PTRN_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_RETURN_HISTORY_T
# -----------------------------------------------------------------------
drop table if exists OLE_RETURN_HISTORY_T
/

CREATE TABLE OLE_RETURN_HISTORY_T
(
      ID VARCHAR(36)
        , ITEM_BARCODE VARCHAR(40)
        , ITEM_UUID VARCHAR(40)
        , ITEM_RETURNED_DT DATETIME
        , OPERATOR VARCHAR(36)
        , CIR_DESK_LOC VARCHAR(100)
        , CIR_DESK_ROUTE_TO VARCHAR(100)
        , RETURNED_ITEM_STATUS VARCHAR(200)
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_RETURN_HISTORY_TP1 PRIMARY KEY(ID)





    
                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_LOAN_INTRANSIT_HST_TI1 (ITEM_UUID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_RNWL_HISTORY_T
# -----------------------------------------------------------------------
drop table if exists OLE_RNWL_HISTORY_T
/

CREATE TABLE OLE_RNWL_HISTORY_T
(
      RNWL_HSTRY_ID VARCHAR(36)
        , LOAN_ID VARCHAR(40)
        , PTRN_BARCODE VARCHAR(100)
        , ITEM_BARCODE VARCHAR(100)
        , ITEM_UUID VARCHAR(100)
        , OPRTR_ID VARCHAR(100)
        , RNWD_DT DATETIME
        , RNWL_DUE_DT DATETIME
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_RNWL_HISTORY_TP1 PRIMARY KEY(RNWL_HSTRY_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DRL_EDITOR_T
# -----------------------------------------------------------------------
drop table if exists OLE_DRL_EDITOR_T
/

CREATE TABLE OLE_DRL_EDITOR_T
(
      EDITOR_ID VARCHAR(40)
        , EDITOR_TYP VARCHAR(40)
        , FILE_NM VARCHAR(40)
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_DRL_EDITOR_TP1 PRIMARY KEY(EDITOR_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DRL_RULE_T
# -----------------------------------------------------------------------
drop table if exists OLE_DRL_RULE_T
/

CREATE TABLE OLE_DRL_RULE_T
(
      RULE_ID VARCHAR(40)
        , AGENDA_GROUP VARCHAR(200)
        , EDITOR_ID VARCHAR(200)
        , ACTIVATION_GROUP VARCHAR(200)
        , RULE_TYP VARCHAR(40)
        , RULE_NM VARCHAR(200)
        , ITEM_TYPES LONGBLOB
        , BORROWER_TYPES LONGBLOB
        , INST_LOCATIONS LONGBLOB
        , CAMPUS_LOCATIONS LONGBLOB
        , COLL_LOCATIONS LONGBLOB
        , LIBRARY_LOCATIONS LONGBLOB
        , SHELVING_LOCATIONS LONGBLOB
        , CIRC_POLICY VARCHAR(200)
        , LOAN_PERIOD VARCHAR(50)
        , DFLT_RECALL_PERIOD VARCHAR(50)
        , ITEM_TYP_OPERATOR VARCHAR(50)
        , BORROWER_TYP_OPERATOR VARCHAR(50)
        , INST_LOCATION_OPERATOR VARCHAR(50)
        , CAMPUS_LOCATION_OPERATOR VARCHAR(50)
        , LIBRARY_LOCATION_OPERATOR VARCHAR(50)
        , COLL_LOCATION_OPERATOR VARCHAR(50)
        , SHELVING_LOCATION_OPERATOR VARCHAR(50)
        , LOAN_TYPE VARCHAR(200)
        , ITM_TYP_COUNT VARCHAR(50)
        , ITM_TYP_COUNT_OPTR VARCHAR(50)
        , ERROR_MSG VARCHAR(500)
        , OVERRIDE_PERMISSION VARCHAR(200)
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_DRL_RULE_TP1 PRIMARY KEY(RULE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_DRL_FINE_LIMITS_T
# -----------------------------------------------------------------------
drop table if exists OLE_DRL_FINE_LIMITS_T
/

CREATE TABLE OLE_DRL_FINE_LIMITS_T
(
      ID VARCHAR(40)
        , RULE_ID VARCHAR(40)
        , BORROWER_TYPE VARCHAR(200)
        , LIMIT_AMOUNT VARCHAR(8)
        , OVERDUE_LIMIT VARCHAR(8)
        , OPERATOR VARCHAR(8)
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_DRL_FINE_LIMITS_TP1 PRIMARY KEY(ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_NOTICE_TYPE_CONFIG_T
# -----------------------------------------------------------------------
drop table if exists OLE_NOTICE_TYPE_CONFIG_T
/

CREATE TABLE OLE_NOTICE_TYPE_CONFIG_T
(
      NOTICE_TYPE_CONFIG_ID INTEGER
        , CIRC_POLICY_ID VARCHAR(1000)
        , NOTICE_TYPE VARCHAR(1000)
    
    , CONSTRAINT OLE_NOTICE_TYPE_CONFIG_TP1 PRIMARY KEY(NOTICE_TYPE_CONFIG_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# ALRT_T
# -----------------------------------------------------------------------
drop table if exists ALRT_T
/

CREATE TABLE ALRT_T
(
      ALRT_ID VARCHAR(10)
        , DCMNT_ID VARCHAR(40)
        , ALRT_DT DATETIME
        , ALRT_NTE VARCHAR(2000)
        , ALRT_CRT_DT DATETIME
        , ALRT_MDFD_DT DATETIME
        , ALRT_INIT_ID VARCHAR(40)
        , ALRT_MODFIR_ID VARCHAR(40)
        , ALRT_STAT VARCHAR(1)
        , RCVNG_USR_ID VARCHAR(40)
        , RCVNG_GRP_ID VARCHAR(40)
        , RCVNG_ROLE_ID VARCHAR(40)
        , ALRT_APRVR_ID VARCHAR(40)
        , ALRT_APROVD_DT DATETIME
        , ALRT_INTRVL VARCHAR(40)
        , ALRT_RPTBLE VARCHAR(1)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT ALRT_TP1 PRIMARY KEY(ALRT_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX DCMNT_ID_IND1 (DCMNT_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                            
                                    
, INDEX ALRT_DT_IND1 (ALRT_DT )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX RCVNG_USR_ID_IND1 (RCVNG_USR_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX ALRT_STAT_IND1 (ALRT_STAT )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# ALRT_DOC_TYP_T
# -----------------------------------------------------------------------
drop table if exists ALRT_DOC_TYP_T
/

CREATE TABLE ALRT_DOC_TYP_T
(
      ALRT_DOC_TYP_ID VARCHAR(40)
        , ALRT_DOC_TYP_NAME VARCHAR(200)
        , ALRT_DOC_TYP_DESC VARCHAR(800)
        , ALRT_DOC_CLASS VARCHAR(400)
        , ALRT_REM_INTERVAL VARCHAR(100)
        , ACTV_IND VARCHAR(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
    
    , CONSTRAINT ALRT_DOC_TYP_TP1 PRIMARY KEY(ALRT_DOC_TYP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# ALRT_EVENT_T
# -----------------------------------------------------------------------
drop table if exists ALRT_EVENT_T
/

CREATE TABLE ALRT_EVENT_T
(
      ALRT_EVENT_ID VARCHAR(40)
        , ALRT_EVENT_NAME VARCHAR(40)
        , ALRT_DOC_TYP_ID VARCHAR(100)
        , ACTV_IND VARCHAR(1) default 'Y' NOT NULL
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
    
    , CONSTRAINT ALRT_EVENT_TP1 PRIMARY KEY(ALRT_EVENT_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# ALRT_EVENT_FIELD_T
# -----------------------------------------------------------------------
drop table if exists ALRT_EVENT_FIELD_T
/

CREATE TABLE ALRT_EVENT_FIELD_T
(
      ALRT_EVENT_FIELD_ID VARCHAR(40)
        , ALRT_EVENT_ID VARCHAR(40)
        , ALRT_FIELD_NM VARCHAR(100)
        , ALRT_FIELD_TYP VARCHAR(100)
        , ALRT_FIELD_VAL VARCHAR(40)
        , ALRT_CRIT VARCHAR(40)
        , ACTV_IND VARCHAR(1)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
    
    , CONSTRAINT ALRT_EVENT_FIELD_TP1 PRIMARY KEY(ALRT_EVENT_FIELD_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# ALRT_DOC_T
# -----------------------------------------------------------------------
drop table if exists ALRT_DOC_T
/

CREATE TABLE ALRT_DOC_T
(
      ALRT_DOC_ID VARCHAR(40)
        , DOC_TYP_ID VARCHAR(40)
        , DOC_TYP_NM VARCHAR(200)
        , ALRT_DOC_CRTR_ID VARCHAR(100)
        , ALRT_RPT VARCHAR(1)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
    
    , CONSTRAINT ALRT_DOC_TP1 PRIMARY KEY(ALRT_DOC_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# ALRT_CNDTN_NTFCN_INFO_T
# -----------------------------------------------------------------------
drop table if exists ALRT_CNDTN_NTFCN_INFO_T
/

CREATE TABLE ALRT_CNDTN_NTFCN_INFO_T
(
      ALRT_CNDTN_ID VARCHAR(40)
        , GRP_ID VARCHAR(40)
        , ROLE_ID VARCHAR(100)
        , PRNCPL_ID VARCHAR(100)
        , ALRT_EVENT_ID VARCHAR(100)
        , ALRT_NOTE VARCHAR(2000)
        , IS_EMAIL VARCHAR(100)
        , IS_ALRT_LST VARCHAR(100)
        , ALRT_INTRVL VARCHAR(100)
        , ALRT_DOC_ID VARCHAR(40)
        , OBJ_ID VARCHAR(36)
        , VER_NBR DECIMAL(8)
    
    , CONSTRAINT ALRT_CNDTN_NTFCN_INFO_TP1 PRIMARY KEY(ALRT_CNDTN_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# ACTN_LST_ALRT_T
# -----------------------------------------------------------------------
drop table if exists ACTN_LST_ALRT_T
/

CREATE TABLE ACTN_LST_ALRT_T
(
      ACTN_LST_ALRT_ID VARCHAR(10)
        , DCMNT_ID VARCHAR(40)
        , TITL VARCHAR(800)
        , RCRD_TYP VARCHAR(80)
        , ALRT_DT DATETIME
        , NTE VARCHAR(800)
        , ACTV VARCHAR(1)
        , ALRT_USR_ID VARCHAR(40)
        , ALRT_INIT_ID VARCHAR(40)
        , ALRT_APRVR_ID VARCHAR(40)
        , ALRT_APROVD_DT DATETIME
        , ALRT_ID VARCHAR(10)
        , OBJ_ID VARCHAR(36) NOT NULL
        , VER_NBR DECIMAL(8) NOT NULL
    
    , CONSTRAINT ACTN_LST_ALRT_TP1 PRIMARY KEY(ACTN_LST_ALRT_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX DCMNT_ID_IND2 (DCMNT_ID )
    
                                                                                                                                                                                                                                                                                                                                            
                                    
, INDEX ALRT_DT_IND2 (ALRT_DT )
    
                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX ALRT_USR_ID_IND2 (ALRT_USR_ID )
    
                                                                                                                                                                                                                                                                                                                                                                                
                                    
, INDEX ACTV_IND2 (ACTV )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_GOKB_PKG_T
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_PKG_T
/

CREATE TABLE OLE_GOKB_PKG_T
(
      GOKB_PKG_ID INTEGER
        , PKG_NAME VARCHAR(1000)
        , VARIANT_NAME VARCHAR(4000)
        , PKG_STATUS VARCHAR(20)
        , PKG_SCOPE VARCHAR(20)
        , BREAKABLE VARCHAR(10)
        , FXD VARCHAR(10)
        , AVLBLE VARCHAR(10)
        , DATE_CREATED DATETIME
        , DATE_UPDATED DATETIME
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_GOKB_PKG_TP1 PRIMARY KEY(GOKB_PKG_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_GOKB_TIPP_T
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_TIPP_T
/

CREATE TABLE OLE_GOKB_TIPP_T
(
      GOKB_TIPP_ID INTEGER
        , GOKB_PKG_ID INTEGER
        , GOKB_TITLE_ID INTEGER
        , GOKB_PLTFRM_ID INTEGER
        , TIPP_STATUS VARCHAR(100)
        , STATUS_REASON VARCHAR(300)
        , STRT_DT DATETIME
        , STRT_VOL VARCHAR(100)
        , STRT_ISSUE VARCHAR(100)
        , END_DT DATETIME
        , END_VOL VARCHAR(100)
        , END_ISSUE VARCHAR(100)
        , EMBARGO VARCHAR(50)
        , PLTFRM_HOST_URL VARCHAR(300)
        , DATE_CREATED DATETIME
        , DATE_UPDATED DATETIME
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_GOKB_TIPP_TP1 PRIMARY KEY(GOKB_TIPP_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_GOKB_TITLE_T
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_TITLE_T
/

CREATE TABLE OLE_GOKB_TITLE_T
(
      GOKB_TITLE_ID INTEGER
        , TITLE_NAME VARCHAR(500)
        , VARIANT_NAME VARCHAR(4000)
        , MEDIUM VARCHAR(50)
        , PURE_QA VARCHAR(50)
        , TI_ISSN_ONLINE VARCHAR(50)
        , TI_ISSN_PRNT VARCHAR(50)
        , TI_ISSN_L VARCHAR(50)
        , OCLC_NUM INTEGER
        , TI_DOI VARCHAR(50)
        , TI_PROPRIETARY_ID INTEGER
        , TI_SUNCAT VARCHAR(50)
        , TI_LCCN VARCHAR(50)
        , PUBLSHR_ID INTEGER
        , IMPRINT VARCHAR(1000)
        , DATE_CREATED DATETIME
        , DATE_UPDATED DATETIME
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_GOKB_TITLE_TP1 PRIMARY KEY(GOKB_TITLE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_GOKB_PLTFRM_T
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_PLTFRM_T
/

CREATE TABLE OLE_GOKB_PLTFRM_T
(
      GOKB_PLTFRM_ID INTEGER
        , PLTFRM_NAME VARCHAR(500)
        , PLTFRM_STATUS VARCHAR(100)
        , PLTFRM_PRVDR_ID INTEGER
        , AUTH VARCHAR(50)
        , SOFTWARE_PLTFRM VARCHAR(50)
        , DATE_CREATED DATETIME
        , DATE_UPDATED DATETIME
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_GOKB_PLTFRM_TP1 PRIMARY KEY(GOKB_PLTFRM_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_GOKB_ORG_T
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_ORG_T
/

CREATE TABLE OLE_GOKB_ORG_T
(
      GOKB_ORG_ID INTEGER
        , ORG_NAME VARCHAR(1000)
        , VARIANT_NAME VARCHAR(4000)
        , DATE_CREATED DATETIME
        , DATE_UPDATED DATETIME
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_GOKB_ORG_TP1 PRIMARY KEY(GOKB_ORG_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_GOKB_UPDATE_LOG_T
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_UPDATE_LOG_T
/

CREATE TABLE OLE_GOKB_UPDATE_LOG_T
(
      ID INTEGER
        , NUM_PKGS INTEGER
        , NUM_TIPPS INTEGER
        , NUM_TITLES INTEGER
        , NUM_ORGS INTEGER
        , NUM_PLTFRMS INTEGER
        , USER_NM VARCHAR(100)
        , STATUS VARCHAR(20)
        , START_TIME DATETIME
        , END_TIME DATETIME
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_GOKB_UPDATE_LOG_TP1 PRIMARY KEY(ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_GOKB_ORG_ROLE_T
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_ORG_ROLE_T
/

CREATE TABLE OLE_GOKB_ORG_ROLE_T
(
      GOKB_ORG_ROLE_ID INTEGER
        , GOKB_ORG_ID INTEGER
        , ROLE VARCHAR(100)
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_GOKB_ORG_ROLE_TP1 PRIMARY KEY(GOKB_ORG_ROLE_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_GOKB_REVIEW_T
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_REVIEW_T
/

CREATE TABLE OLE_GOKB_REVIEW_T
(
      GOKB_REVIEW_ID INTEGER
        , REVIEW_DT DATETIME
        , E_RES_REC_ID VARCHAR(10)
        , TYPE VARCHAR(100)
        , DETAILS VARCHAR(1000)
        , GOKB_TIPP_ID INTEGER
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_GOKB_REVIEW_TP1 PRIMARY KEY(GOKB_REVIEW_ID)





    
                                                                                                                                                                                                                                                        
                                    
, INDEX OLE_GOKB_REVIEW_I (E_RES_REC_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_GOKB_CHANGE_LOG_T
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_CHANGE_LOG_T
/

CREATE TABLE OLE_GOKB_CHANGE_LOG_T
(
      GOKB_CHANGE_LOG_ID INTEGER
        , CHANGLOG_DT DATETIME
        , E_RES_REC_ID VARCHAR(10)
        , TYPE VARCHAR(100)
        , DETAILS VARCHAR(1000)
        , ORIGIN VARCHAR(100)
        , GOKB_TIPP_ID INTEGER
        , ARCHIVED_DT DATETIME
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_GOKB_CHANGE_LOG_TP1 PRIMARY KEY(GOKB_CHANGE_LOG_ID)





    
                                                                                                                                                                                                                                                                                                
                                    
, INDEX OLE_GOKB_CHANGE_LOG_I (E_RES_REC_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_GOKB_ARCHIVE_T
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_ARCHIVE_T
/

CREATE TABLE OLE_GOKB_ARCHIVE_T
(
      GOKB_ARCHIVE_ID INTEGER
        , E_RES_REC_ID VARCHAR(10)
        , TYPE VARCHAR(100)
        , DETAILS VARCHAR(1000)
        , ORIGIN VARCHAR(100)
        , GOKB_TIPP_ID INTEGER
        , ARCHIVED_DT DATETIME
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_GOKB_ARCHIVE_TP1 PRIMARY KEY(GOKB_ARCHIVE_ID)





    
                                                                                                                                                                                                                                                                            
                                    
, INDEX OLE_GOKB_ARCHIVE_I (E_RES_REC_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_NOTC_FIELD_LABEL_MAPNG_T
# -----------------------------------------------------------------------
drop table if exists OLE_NOTC_FIELD_LABEL_MAPNG_T
/

CREATE TABLE OLE_NOTC_FIELD_LABEL_MAPNG_T
(
      OLE_NOTC_FIELD_LABEL_MAPNG_ID VARCHAR(40) default '0'
        , FLD_NM VARCHAR(40) NOT NULL
        , FLD_LBL VARCHAR(40)
        , BLNG_TO VARCHAR(40)
        , OLE_NOTC_CNTNT_CONFIG_ID VARCHAR(40)
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_NOTC_FIELD_LABEL_MAPNG_P1 PRIMARY KEY(OLE_NOTC_FIELD_LABEL_MAPNG_ID)





    
                                                                                                                                                                                                                                    
                                    
, INDEX FLD_NM_INDX (FLD_NM )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_NOTC_CNTNT_CONFIG_T
# -----------------------------------------------------------------------
drop table if exists OLE_NOTC_CNTNT_CONFIG_T
/

CREATE TABLE OLE_NOTC_CNTNT_CONFIG_T
(
      OLE_NOTC_CNTNT_CONFIG_ID VARCHAR(40)
        , NOTC_TYP VARCHAR(40) NOT NULL
        , NOTC_NM VARCHAR(400) NOT NULL
        , NOTC_TITL VARCHAR(400) NOT NULL
        , ACTV_IND VARCHAR(1) default 'Y' NOT NULL
        , NOTC_BDY VARCHAR(4000) NOT NULL
        , NOTC_FTR_BDY VARCHAR(4000) NOT NULL
        , NOTC_SUBJ_LN VARCHAR(4000) NOT NULL
        , VER_NBR DECIMAL(8)
        , OBJ_ID VARCHAR(36)
    
    , CONSTRAINT OLE_NOTC_CNTNT_CONFIG_TP1 PRIMARY KEY(OLE_NOTC_CNTNT_CONFIG_ID)





    
                                                                                                                                                                                                                                                                                                
                                    
, INDEX NOTC_TYP_INDX (NOTC_TYP )
    
                                                                                                                                                                                                                                                                                                                    
                                    
, INDEX NOTC_NM_INDEX (NOTC_NM (255))

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# ITEM_AUDIT_T
# -----------------------------------------------------------------------
drop table if exists ITEM_AUDIT_T
/

CREATE TABLE ITEM_AUDIT_T
(
      AUDIT_ID INTEGER(10)
        , FOREIGN_KEY_REF INTEGER(10)
        , ACTOR VARCHAR(40)
        , UPDATE_DATE DATETIME
        , COLUMN_UPDATED VARCHAR(40)
        , COLUMN_VALUE LONGBLOB
        , OBJ_ID VARCHAR(36)
        , VER_NBR INTEGER(8)
    
    , CONSTRAINT ITEM_AUDIT_TP1 PRIMARY KEY(AUDIT_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# BIB_AUDIT_T
# -----------------------------------------------------------------------
drop table if exists BIB_AUDIT_T
/

CREATE TABLE BIB_AUDIT_T
(
      AUDIT_ID INTEGER(10)
        , FOREIGN_KEY_REF INTEGER(10)
        , ACTOR VARCHAR(40)
        , UPDATE_DATE DATETIME
        , COLUMN_UPDATED VARCHAR(40)
        , COLUMN_VALUE LONGBLOB
        , OBJ_ID VARCHAR(36)
        , VER_NBR INTEGER(8)
    
    , CONSTRAINT BIB_AUDIT_TP1 PRIMARY KEY(AUDIT_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# HOLDINGS_AUDIT_T
# -----------------------------------------------------------------------
drop table if exists HOLDINGS_AUDIT_T
/

CREATE TABLE HOLDINGS_AUDIT_T
(
      AUDIT_ID INTEGER(10)
        , FOREIGN_KEY_REF INTEGER(10)
        , ACTOR VARCHAR(40)
        , UPDATE_DATE DATETIME
        , COLUMN_UPDATED VARCHAR(40)
        , COLUMN_VALUE LONGBLOB
        , OBJ_ID VARCHAR(36)
        , VER_NBR INTEGER(8)
    
    , CONSTRAINT HOLDINGS_AUDIT_TP1 PRIMARY KEY(AUDIT_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_NG_BAT_PRF_T
# -----------------------------------------------------------------------
drop table if exists OLE_NG_BAT_PRF_T
/

CREATE TABLE OLE_NG_BAT_PRF_T
(
      PRF_ID INTEGER(10)
        , PRF_NM VARCHAR(40)
        , PRF_TYP VARCHAR(40)
        , CONTENT LONGBLOB
        , OBJ_ID VARCHAR(36)
        , VER_NBR INTEGER(8)
    
    , CONSTRAINT OLE_NG_BAT_PRF_TP1 PRIMARY KEY(PRF_ID)






) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_NG_BAT_PRCS_JOB_T
# -----------------------------------------------------------------------
drop table if exists OLE_NG_BAT_PRCS_JOB_T
/

CREATE TABLE OLE_NG_BAT_PRCS_JOB_T
(
      JOB_ID INTEGER(10)
        , JOB_NAME VARCHAR(100)
        , PROFILE_TYPE VARCHAR(40)
        , PRF_ID INTEGER(10)
        , JOB_TYPE VARCHAR(40)
        , CRON_EXP VARCHAR(100)
        , CREATED_BY VARCHAR(40)
        , CREATED_ON DATETIME
        , NEXT_RUN_TIME DATETIME
        , NUM_OF_RECORD INTEGER(10)
        , OUTPUT_FORMAT VARCHAR(40)
        , OBJ_ID VARCHAR(36)
        , VER_NBR INTEGER(8)
    
    , CONSTRAINT OLE_NG_BAT_PRCS_JOB_TP1 PRIMARY KEY(JOB_ID)





    
                                                                                                                                                                                                                                                                                                                        
                                    
, INDEX OLE_NG_BAT_PRCS_I (PRF_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_NG_BAT_JOB_DETAILS_T
# -----------------------------------------------------------------------
drop table if exists OLE_NG_BAT_JOB_DETAILS_T
/

CREATE TABLE OLE_NG_BAT_JOB_DETAILS_T
(
      JOB_DETAIL_ID INTEGER(10)
        , JOB_NAME VARCHAR(100)
        , JOB_ID INTEGER(10)
        , PROFILE_TYPE VARCHAR(40)
        , PROFILE_NAME VARCHAR(100)
        , FILE_NAME VARCHAR(100)
        , CREATED_BY VARCHAR(40)
        , START_TIME DATETIME
        , END_TIME DATETIME
        , PER_COMPLETED VARCHAR(40)
        , TIME_SPENT VARCHAR(40)
        , TOTAL_RECORDS VARCHAR(40)
        , TOTAL_RECORDS_PRCSD VARCHAR(40)
        , TOTAL_FAILURE_RECORDS VARCHAR(40)
        , STATUS VARCHAR(40)
        , OBJ_ID VARCHAR(36)
        , VER_NBR INTEGER(8)
    
    , CONSTRAINT OLE_NG_BAT_JOB_DETAILS_TP1 PRIMARY KEY(JOB_DETAIL_ID)





    
                                                                                                                                                                                                                                                                                                                                                                                                        
                                    
, INDEX OLE_NG_BAT_JOB_I (JOB_ID )

) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_bin
/


# -----------------------------------------------------------------------
# OLE_GOKB_V
# -----------------------------------------------------------------------
drop view if exists OLE_GOKB_V
/
CREATE VIEW OLE_GOKB_V AS 
SELECT TIPP.GOKB_TIPP_ID,TIPP.TIPP_STATUS,TITLE.GOKB_TITLE_ID,TITLE.TITLE_NAME,TITLE.PUBLSHR_ID,TITLE.TI_ISSN_ONLINE,TITLE.TI_ISSN_PRNT,TITLE.TI_ISSN_L,TITLE.MEDIUM,PACKAGE.GOKB_PKG_ID,PACKAGE.PKG_STATUS,PACKAGE.PKG_NAME,PLATFORM.GOKB_PLTFRM_ID,PLATFORM.PLTFRM_NAME,PLATFORM.PLTFRM_STATUS,PLATFORM.SOFTWARE_PLTFRM,ORG.GOKB_ORG_ID,ORG.ORG_NAME FROM  OLE_GOKB_TIPP_T TIPP, OLE_GOKB_PLTFRM_T PLATFORM, OLE_GOKB_ORG_T ORG, OLE_GOKB_TITLE_T TITLE, OLE_GOKB_PKG_T PACKAGE WHERE TIPP.GOKB_PLTFRM_ID=PLATFORM.GOKB_PLTFRM_ID AND TIPP.GOKB_PKG_ID=PACKAGE.GOKB_PKG_ID AND TIPP.GOKB_TITLE_ID=TITLE.GOKB_TITLE_ID AND PLATFORM.PLTFRM_PRVDR_ID=ORG.GOKB_ORG_ID
/


# -----------------------------------------------------------------------
# OLE_PTRN_ENTITY_V
# -----------------------------------------------------------------------
drop view if exists OLE_PTRN_ENTITY_V
/
CREATE VIEW OLE_PTRN_ENTITY_V AS 
(SELECT     P.OLE_PTRN_ID,     P.BARCODE AS PTRN_BRCD,     P.BORR_TYP AS PTRN_TYP_ID,     ENT.ENTITY_ID,     ENT_NM.FIRST_NM,     ENT_NM.MIDDLE_NM,     ENT_NM.LAST_NM,     ENT_NM.SUFFIX_NM,     ENT_NM.PREFIX_NM,     (SELECT ENT_EM.EMAIL_ADDR FROM KRIM_ENTITY_EMAIL_T ENT_EM WHERE ENT.ENTITY_ID = ENT_EM.ENTITY_ID AND DFLT_IND='Y') AS EMAIL,     (SELECT ENT_PH.PHONE_NBR FROM KRIM_ENTITY_PHONE_T ENT_PH WHERE ENT.ENTITY_ID = ENT_PH.ENTITY_ID AND DFLT_IND='Y') AS PHONE,     concat(concat(ENT_NM.LAST_NM,', '),ENT_NM.FIRST_NM) AS NAME,     P.ACTV_IND AS ACTV_IND     FROM OLE_PTRN_T P,     KRIM_ENTITY_T ENT,     KRIM_ENTITY_NM_T ENT_NM     WHERE     P.OLE_PTRN_ID = ENT.ENTITY_ID     AND     ENT.ENTITY_ID = ENT_NM.ENTITY_ID)




/


# -----------------------------------------------------------------------
# OLE_ACC_LOC_S
# -----------------------------------------------------------------------
drop table if exists OLE_ACC_LOC_S
/

CREATE TABLE OLE_ACC_LOC_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_ACC_LOC_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_E_RES_ACC_ACT_WRKFLW_S
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_ACC_ACT_WRKFLW_S
/

CREATE TABLE OLE_E_RES_ACC_ACT_WRKFLW_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_E_RES_ACC_ACT_WRKFLW_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_E_RES_ACC_ACT_CONFIG_S
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_ACC_ACT_CONFIG_S
/

CREATE TABLE OLE_E_RES_ACC_ACT_CONFIG_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_E_RES_ACC_ACT_CONFIG_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_ACC_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_ACC_TYP_S
/

CREATE TABLE OLE_ACC_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_ACC_TYP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_AGR_DOC_S
# -----------------------------------------------------------------------
drop table if exists OLE_AGR_DOC_S
/

CREATE TABLE OLE_AGR_DOC_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_AGR_DOC_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_AGR_DOC_TYPE_S
# -----------------------------------------------------------------------
drop table if exists OLE_AGR_DOC_TYPE_S
/

CREATE TABLE OLE_AGR_DOC_TYPE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_AGR_DOC_TYPE_S auto_increment = 9
/

# -----------------------------------------------------------------------
# OLE_AGR_MTH_S
# -----------------------------------------------------------------------
drop table if exists OLE_AGR_MTH_S
/

CREATE TABLE OLE_AGR_MTH_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_AGR_MTH_S auto_increment = 6
/

# -----------------------------------------------------------------------
# OLE_AGR_STAT_S
# -----------------------------------------------------------------------
drop table if exists OLE_AGR_STAT_S
/

CREATE TABLE OLE_AGR_STAT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_AGR_STAT_S auto_increment = 5
/

# -----------------------------------------------------------------------
# OLE_AGR_TYPE_S
# -----------------------------------------------------------------------
drop table if exists OLE_AGR_TYPE_S
/

CREATE TABLE OLE_AGR_TYPE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_AGR_TYPE_S auto_increment = 6
/

# -----------------------------------------------------------------------
# OLE_AGREEMENT_S
# -----------------------------------------------------------------------
drop table if exists OLE_AGREEMENT_S
/

CREATE TABLE OLE_AGREEMENT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_AGREEMENT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_AUTHCAT_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_AUTHCAT_TYP_S
/

CREATE TABLE OLE_AUTHCAT_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_AUTHCAT_TYP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BDGT_CD_S
# -----------------------------------------------------------------------
drop table if exists OLE_BDGT_CD_S
/

CREATE TABLE OLE_BDGT_CD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BDGT_CD_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_CAT_ACCS_MTHD_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_ACCS_MTHD_S
/

CREATE TABLE OLE_CAT_ACCS_MTHD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_ACCS_MTHD_S auto_increment = 8
/

# -----------------------------------------------------------------------
# OLE_CAT_ACQ_MTHD_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_ACQ_MTHD_S
/

CREATE TABLE OLE_CAT_ACQ_MTHD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_ACQ_MTHD_S auto_increment = 12
/

# -----------------------------------------------------------------------
# OLE_CAT_ACTION_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_ACTION_S
/

CREATE TABLE OLE_CAT_ACTION_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_ACTION_S auto_increment = 21
/

# -----------------------------------------------------------------------
# OLE_CAT_BIB_RECORD_STAT_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_BIB_RECORD_STAT_S
/

CREATE TABLE OLE_CAT_BIB_RECORD_STAT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_BIB_RECORD_STAT_S auto_increment = 5
/

# -----------------------------------------------------------------------
# OLE_CAT_CMPLT_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_CMPLT_S
/

CREATE TABLE OLE_CAT_CMPLT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_CMPLT_S auto_increment = 6
/

# -----------------------------------------------------------------------
# OLE_CAT_ELA_RLSHP_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_ELA_RLSHP_S
/

CREATE TABLE OLE_CAT_ELA_RLSHP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_ELA_RLSHP_S auto_increment = 6
/

# -----------------------------------------------------------------------
# OLE_CAT_ENCD_LVL_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_ENCD_LVL_S
/

CREATE TABLE OLE_CAT_ENCD_LVL_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_ENCD_LVL_S auto_increment = 9
/

# -----------------------------------------------------------------------
# OLE_CAT_FLD_ENCD_LVL_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_FLD_ENCD_LVL_S
/

CREATE TABLE OLE_CAT_FLD_ENCD_LVL_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_FLD_ENCD_LVL_S auto_increment = 5
/

# -----------------------------------------------------------------------
# OLE_CAT_GEN_RTN_POL_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_GEN_RTN_POL_S
/

CREATE TABLE OLE_CAT_GEN_RTN_POL_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_GEN_RTN_POL_S auto_increment = 10
/

# -----------------------------------------------------------------------
# OLE_CAT_ITM_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_ITM_TYP_S
/

CREATE TABLE OLE_CAT_ITM_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_ITM_TYP_S auto_increment = 53
/

# -----------------------------------------------------------------------
# OLE_CAT_LND_POL_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_LND_POL_S
/

CREATE TABLE OLE_CAT_LND_POL_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_LND_POL_S auto_increment = 6
/

# -----------------------------------------------------------------------
# OLE_CAT_LOC_COUNTRY_CD_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_LOC_COUNTRY_CD_S
/

CREATE TABLE OLE_CAT_LOC_COUNTRY_CD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_LOC_COUNTRY_CD_S auto_increment = 3
/

# -----------------------------------------------------------------------
# OLE_CAT_NTN_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_NTN_TYP_S
/

CREATE TABLE OLE_CAT_NTN_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_NTN_TYP_S auto_increment = 5
/

# -----------------------------------------------------------------------
# OLE_CAT_PRVCY_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_PRVCY_S
/

CREATE TABLE OLE_CAT_PRVCY_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_PRVCY_S auto_increment = 5
/

# -----------------------------------------------------------------------
# OLE_CAT_RCPT_STAT_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_RCPT_STAT_S
/

CREATE TABLE OLE_CAT_RCPT_STAT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_RCPT_STAT_S auto_increment = 10
/

# -----------------------------------------------------------------------
# OLE_CAT_REC_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_REC_TYP_S
/

CREATE TABLE OLE_CAT_REC_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_REC_TYP_S auto_increment = 5
/

# -----------------------------------------------------------------------
# OLE_CAT_REPRO_POL_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_REPRO_POL_S
/

CREATE TABLE OLE_CAT_REPRO_POL_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_REPRO_POL_S auto_increment = 4
/

# -----------------------------------------------------------------------
# OLE_CAT_SHVLG_ORD_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_SHVLG_ORD_S
/

CREATE TABLE OLE_CAT_SHVLG_ORD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_SHVLG_ORD_S auto_increment = 5
/

# -----------------------------------------------------------------------
# OLE_CAT_SHVLG_SCHM_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_SHVLG_SCHM_S
/

CREATE TABLE OLE_CAT_SHVLG_SCHM_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_SHVLG_SCHM_S auto_increment = 11
/

# -----------------------------------------------------------------------
# OLE_CAT_SPCP_RPT_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_SPCP_RPT_S
/

CREATE TABLE OLE_CAT_SPCP_RPT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_SPCP_RPT_S auto_increment = 3
/

# -----------------------------------------------------------------------
# OLE_CAT_SPFC_RTN_POL_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_SPFC_RTN_POL_TYP_S
/

CREATE TABLE OLE_CAT_SPFC_RTN_POL_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_SPFC_RTN_POL_TYP_S auto_increment = 3
/

# -----------------------------------------------------------------------
# OLE_CAT_SPFC_RTN_POL_UNT_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_SPFC_RTN_POL_UNT_TYP_S
/

CREATE TABLE OLE_CAT_SPFC_RTN_POL_UNT_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_SPFC_RTN_POL_UNT_TYP_S auto_increment = 8
/

# -----------------------------------------------------------------------
# OLE_CAT_SRC_TRM_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_SRC_TRM_S
/

CREATE TABLE OLE_CAT_SRC_TRM_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_SRC_TRM_S auto_increment = 5
/

# -----------------------------------------------------------------------
# OLE_CAT_STAT_SRCH_CD_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_STAT_SRCH_CD_S
/

CREATE TABLE OLE_CAT_STAT_SRCH_CD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_STAT_SRCH_CD_S auto_increment = 51
/

# -----------------------------------------------------------------------
# OLE_CAT_TYPE_OWNERSHIP_S
# -----------------------------------------------------------------------
drop table if exists OLE_CAT_TYPE_OWNERSHIP_S
/

CREATE TABLE OLE_CAT_TYPE_OWNERSHIP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CAT_TYPE_OWNERSHIP_S auto_increment = 4
/

# -----------------------------------------------------------------------
# OLE_CIRC_DSK_DTL_S
# -----------------------------------------------------------------------
drop table if exists OLE_CIRC_DSK_DTL_S
/

CREATE TABLE OLE_CIRC_DSK_DTL_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CIRC_DSK_DTL_S auto_increment = 68
/

# -----------------------------------------------------------------------
# OLE_CODE_S
# -----------------------------------------------------------------------
drop table if exists OLE_CODE_S
/

CREATE TABLE OLE_CODE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CODE_S auto_increment = 4
/

# -----------------------------------------------------------------------
# OLE_CONT_TYPS_S
# -----------------------------------------------------------------------
drop table if exists OLE_CONT_TYPS_S
/

CREATE TABLE OLE_CONT_TYPS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CONT_TYPS_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_CONT_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_CONT_TYP_S
/

CREATE TABLE OLE_CONT_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CONT_TYP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_CRCL_DSK_LOCN_S
# -----------------------------------------------------------------------
drop table if exists OLE_CRCL_DSK_LOCN_S
/

CREATE TABLE OLE_CRCL_DSK_LOCN_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CRCL_DSK_LOCN_S auto_increment = 24
/

# -----------------------------------------------------------------------
# OLE_CRCL_DSK_FEE_TYPE_S
# -----------------------------------------------------------------------
drop table if exists OLE_CRCL_DSK_FEE_TYPE_S
/

CREATE TABLE OLE_CRCL_DSK_FEE_TYPE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CRCL_DSK_FEE_TYPE_S auto_increment = 24
/

# -----------------------------------------------------------------------
# OLE_CRCL_DSK_S
# -----------------------------------------------------------------------
drop table if exists OLE_CRCL_DSK_S
/

CREATE TABLE OLE_CRCL_DSK_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CRCL_DSK_S auto_increment = 12
/

# -----------------------------------------------------------------------
# OLE_DATAFIELD_ID_S
# -----------------------------------------------------------------------
drop table if exists OLE_DATAFIELD_ID_S
/

CREATE TABLE OLE_DATAFIELD_ID_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DATAFIELD_ID_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DESC_EXT_DATASRC_S
# -----------------------------------------------------------------------
drop table if exists OLE_DESC_EXT_DATASRC_S
/

CREATE TABLE OLE_DESC_EXT_DATASRC_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DESC_EXT_DATASRC_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DESC_USER_PREF_S
# -----------------------------------------------------------------------
drop table if exists OLE_DESC_USER_PREF_S
/

CREATE TABLE OLE_DESC_USER_PREF_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DESC_USER_PREF_S auto_increment = 2
/

# -----------------------------------------------------------------------
# OLE_DISC_EXP_MAP_S
# -----------------------------------------------------------------------
drop table if exists OLE_DISC_EXP_MAP_S
/

CREATE TABLE OLE_DISC_EXP_MAP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DISC_EXP_MAP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DISC_EXP_PROFILE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DISC_EXP_PROFILE_S
/

CREATE TABLE OLE_DISC_EXP_PROFILE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DISC_EXP_PROFILE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_ADDR_SRC_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_ADDR_SRC_S
/

CREATE TABLE OLE_DLVR_ADDR_SRC_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_ADDR_SRC_S auto_increment = 5
/

# -----------------------------------------------------------------------
# OLE_DLVR_ADD_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_ADD_S
/

CREATE TABLE OLE_DLVR_ADD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_ADD_S auto_increment = 73
/

# -----------------------------------------------------------------------
# OLE_DLVR_BARCD_STAT_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_BARCD_STAT_S
/

CREATE TABLE OLE_DLVR_BARCD_STAT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_BARCD_STAT_S auto_increment = 4
/

# -----------------------------------------------------------------------
# OLE_DLVR_BATCH_JOB_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_BATCH_JOB_S
/

CREATE TABLE OLE_DLVR_BATCH_JOB_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_BATCH_JOB_S auto_increment = 9
/

# -----------------------------------------------------------------------
# OLE_DLVR_BORR_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_BORR_TYP_S
/

CREATE TABLE OLE_DLVR_BORR_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_BORR_TYP_S auto_increment = 10
/

# -----------------------------------------------------------------------
# OLE_DLVR_CIRC_RECORD_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_CIRC_RECORD_S
/

CREATE TABLE OLE_DLVR_CIRC_RECORD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_CIRC_RECORD_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_FIXED_DUE_DATE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_FIXED_DUE_DATE_S
/

CREATE TABLE OLE_DLVR_FIXED_DUE_DATE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_FIXED_DUE_DATE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_FXD_DUE_DT_SPAN_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_FXD_DUE_DT_SPAN_S
/

CREATE TABLE OLE_DLVR_FXD_DUE_DT_SPAN_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_FXD_DUE_DT_SPAN_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_ITEM_AVAIL_STAT_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_ITEM_AVAIL_STAT_S
/

CREATE TABLE OLE_DLVR_ITEM_AVAIL_STAT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_ITEM_AVAIL_STAT_S auto_increment = 16
/

# -----------------------------------------------------------------------
# OLE_DLVR_LOAN_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_LOAN_S
/

CREATE TABLE OLE_DLVR_LOAN_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_LOAN_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_LOAN_STAT_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_LOAN_STAT_S
/

CREATE TABLE OLE_DLVR_LOAN_STAT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_LOAN_STAT_S auto_increment = 3
/

# -----------------------------------------------------------------------
# OLE_DLVR_LOAN_TERM_UNIT_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_LOAN_TERM_UNIT_S
/

CREATE TABLE OLE_DLVR_LOAN_TERM_UNIT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_LOAN_TERM_UNIT_S auto_increment = 9
/

# -----------------------------------------------------------------------
# OLE_DLVR_PTRN_BILL_FEE_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_PTRN_BILL_FEE_TYP_S
/

CREATE TABLE OLE_DLVR_PTRN_BILL_FEE_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_PTRN_BILL_FEE_TYP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_PTRN_BILL_PAY_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_PTRN_BILL_PAY_S
/

CREATE TABLE OLE_DLVR_PTRN_BILL_PAY_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_PTRN_BILL_PAY_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_PTRN_BILL_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_PTRN_BILL_S
/

CREATE TABLE OLE_DLVR_PTRN_BILL_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_PTRN_BILL_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_PTRN_FEE_TYPE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_PTRN_FEE_TYPE_S
/

CREATE TABLE OLE_DLVR_PTRN_FEE_TYPE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_PTRN_FEE_TYPE_S auto_increment = 5
/

# -----------------------------------------------------------------------
# OLE_DLVR_RECENTLY_RETURNED_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_RECENTLY_RETURNED_S
/

CREATE TABLE OLE_DLVR_RECENTLY_RETURNED_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_RECENTLY_RETURNED_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_RQST_HSTRY_REC_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_RQST_HSTRY_REC_S
/

CREATE TABLE OLE_DLVR_RQST_HSTRY_REC_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_RQST_HSTRY_REC_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_RQST_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_RQST_S
/

CREATE TABLE OLE_DLVR_RQST_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_RQST_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_RQST_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_RQST_TYP_S
/

CREATE TABLE OLE_DLVR_RQST_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_RQST_TYP_S auto_increment = 10
/

# -----------------------------------------------------------------------
# OLE_DLVR_SRC_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_SRC_S
/

CREATE TABLE OLE_DLVR_SRC_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_SRC_S auto_increment = 5
/

# -----------------------------------------------------------------------
# OLE_DLVR_STAT_CAT_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_STAT_CAT_S
/

CREATE TABLE OLE_DLVR_STAT_CAT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_STAT_CAT_S auto_increment = 2
/

# -----------------------------------------------------------------------
# OLE_DLVR_TEMP_CIRC_RECORD_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_TEMP_CIRC_RECORD_S
/

CREATE TABLE OLE_DLVR_TEMP_CIRC_RECORD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_TEMP_CIRC_RECORD_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_ERES_NTE_S
# -----------------------------------------------------------------------
drop table if exists OLE_ERES_NTE_S
/

CREATE TABLE OLE_ERES_NTE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_ERES_NTE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_ERES_REQ_S
# -----------------------------------------------------------------------
drop table if exists OLE_ERES_REQ_S
/

CREATE TABLE OLE_ERES_REQ_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_ERES_REQ_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_ERES_REQ_SEL_COMM_S
# -----------------------------------------------------------------------
drop table if exists OLE_ERES_REQ_SEL_COMM_S
/

CREATE TABLE OLE_ERES_REQ_SEL_COMM_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_ERES_REQ_SEL_COMM_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_ERES_SEL_S
# -----------------------------------------------------------------------
drop table if exists OLE_ERES_SEL_S
/

CREATE TABLE OLE_ERES_SEL_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_ERES_SEL_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_E_RES_REC_EVNT_LOG_S
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_REC_EVNT_LOG_S
/

CREATE TABLE OLE_E_RES_REC_EVNT_LOG_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_E_RES_REC_EVNT_LOG_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_E_RES_REC_INS_S
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_REC_INS_S
/

CREATE TABLE OLE_E_RES_REC_INS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_E_RES_REC_INS_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_E_RES_REC_INV_S
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_REC_INV_S
/

CREATE TABLE OLE_E_RES_REC_INV_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_E_RES_REC_INV_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_E_RES_REC_LIC_S
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_REC_LIC_S
/

CREATE TABLE OLE_E_RES_REC_LIC_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_E_RES_REC_LIC_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_E_RES_REC_PO_S
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_REC_PO_S
/

CREATE TABLE OLE_E_RES_REC_PO_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_E_RES_REC_PO_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_LINK_E_RES_S
# -----------------------------------------------------------------------
drop table if exists OLE_LINK_E_RES_S
/

CREATE TABLE OLE_LINK_E_RES_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_LINK_E_RES_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_E_RES_REC_S
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_REC_S
/

CREATE TABLE OLE_E_RES_REC_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_E_RES_REC_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_E_RES_STAT_S
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_STAT_S
/

CREATE TABLE OLE_E_RES_STAT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_E_RES_STAT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_GLBLY_PRCT_FLD_S
# -----------------------------------------------------------------------
drop table if exists OLE_GLBLY_PRCT_FLD_S
/

CREATE TABLE OLE_GLBLY_PRCT_FLD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_GLBLY_PRCT_FLD_S auto_increment = 5
/

# -----------------------------------------------------------------------
# OLE_LIC_CHK_LST_S
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_CHK_LST_S
/

CREATE TABLE OLE_LIC_CHK_LST_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_LIC_CHK_LST_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_LIC_DOC_LOCN_S
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_DOC_LOCN_S
/

CREATE TABLE OLE_LIC_DOC_LOCN_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_LIC_DOC_LOCN_S auto_increment = 6
/

# -----------------------------------------------------------------------
# OLE_LIC_EVNT_LOG_S
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_EVNT_LOG_S
/

CREATE TABLE OLE_LIC_EVNT_LOG_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_LIC_EVNT_LOG_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_LIC_EVNT_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_EVNT_TYP_S
/

CREATE TABLE OLE_LIC_EVNT_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_LIC_EVNT_TYP_S auto_increment = 8
/

# -----------------------------------------------------------------------
# OLE_LIC_REQS_S
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_REQS_S
/

CREATE TABLE OLE_LIC_REQS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_LIC_REQS_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_LIC_REQS_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_REQS_TYP_S
/

CREATE TABLE OLE_LIC_REQS_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_LIC_REQS_TYP_S auto_increment = 4
/

# -----------------------------------------------------------------------
# OLE_LIC_RQST_ITM_TITL_S
# -----------------------------------------------------------------------
drop table if exists OLE_LIC_RQST_ITM_TITL_S
/

CREATE TABLE OLE_LIC_RQST_ITM_TITL_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_LIC_RQST_ITM_TITL_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_LOCN_LEVEL_S
# -----------------------------------------------------------------------
drop table if exists OLE_LOCN_LEVEL_S
/

CREATE TABLE OLE_LOCN_LEVEL_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_LOCN_LEVEL_S auto_increment = 6
/

# -----------------------------------------------------------------------
# OLE_LOCN_S
# -----------------------------------------------------------------------
drop table if exists OLE_LOCN_S
/

CREATE TABLE OLE_LOCN_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_LOCN_S auto_increment = 10000
/

# -----------------------------------------------------------------------
# OLE_LOCN_SUM_S
# -----------------------------------------------------------------------
drop table if exists OLE_LOCN_SUM_S
/

CREATE TABLE OLE_LOCN_SUM_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_LOCN_SUM_S auto_increment = 4
/

# -----------------------------------------------------------------------
# OLE_MTRL_TYPS_S
# -----------------------------------------------------------------------
drop table if exists OLE_MTRL_TYPS_S
/

CREATE TABLE OLE_MTRL_TYPS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_MTRL_TYPS_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_MTRL_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_MTRL_TYP_S
/

CREATE TABLE OLE_MTRL_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_MTRL_TYP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_OVERLAY_ACTN_S
# -----------------------------------------------------------------------
drop table if exists OLE_OVERLAY_ACTN_S
/

CREATE TABLE OLE_OVERLAY_ACTN_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_OVERLAY_ACTN_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_OVERLAY_LOOKUP_ACTION_ID_S
# -----------------------------------------------------------------------
drop table if exists OLE_OVERLAY_LOOKUP_ACTION_ID_S
/

CREATE TABLE OLE_OVERLAY_LOOKUP_ACTION_ID_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_OVERLAY_LOOKUP_ACTION_ID_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_OVERLAY_LOOKUP_ID_S
# -----------------------------------------------------------------------
drop table if exists OLE_OVERLAY_LOOKUP_ID_S
/

CREATE TABLE OLE_OVERLAY_LOOKUP_ID_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_OVERLAY_LOOKUP_ID_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_OVERLAY_MAP_FLD_S
# -----------------------------------------------------------------------
drop table if exists OLE_OVERLAY_MAP_FLD_S
/

CREATE TABLE OLE_OVERLAY_MAP_FLD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_OVERLAY_MAP_FLD_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_OVERLAY_OPTION_ID_S
# -----------------------------------------------------------------------
drop table if exists OLE_OVERLAY_OPTION_ID_S
/

CREATE TABLE OLE_OVERLAY_OPTION_ID_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_OVERLAY_OPTION_ID_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_OVERLAY_OUT_FLD_S
# -----------------------------------------------------------------------
drop table if exists OLE_OVERLAY_OUT_FLD_S
/

CREATE TABLE OLE_OVERLAY_OUT_FLD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_OVERLAY_OUT_FLD_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_PCKG_SCP_S
# -----------------------------------------------------------------------
drop table if exists OLE_PCKG_SCP_S
/

CREATE TABLE OLE_PCKG_SCP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_PCKG_SCP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_PCKG_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_PCKG_TYP_S
/

CREATE TABLE OLE_PCKG_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_PCKG_TYP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_PROFILE_ATTR_S
# -----------------------------------------------------------------------
drop table if exists OLE_PROFILE_ATTR_S
/

CREATE TABLE OLE_PROFILE_ATTR_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_PROFILE_ATTR_S auto_increment = 35
/

# -----------------------------------------------------------------------
# OLE_PROFILE_FACT_S
# -----------------------------------------------------------------------
drop table if exists OLE_PROFILE_FACT_S
/

CREATE TABLE OLE_PROFILE_FACT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_PROFILE_FACT_S auto_increment = 146
/

# -----------------------------------------------------------------------
# OLE_PROXY_PTRN_S
# -----------------------------------------------------------------------
drop table if exists OLE_PROXY_PTRN_S
/

CREATE TABLE OLE_PROXY_PTRN_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_PROXY_PTRN_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_PTRN_LOCAL_ID_S
# -----------------------------------------------------------------------
drop table if exists OLE_PTRN_LOCAL_ID_S
/

CREATE TABLE OLE_PTRN_LOCAL_ID_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_PTRN_LOCAL_ID_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_PTRN_LOST_BAR_S
# -----------------------------------------------------------------------
drop table if exists OLE_PTRN_LOST_BAR_S
/

CREATE TABLE OLE_PTRN_LOST_BAR_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_PTRN_LOST_BAR_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_PTRN_NTE_S
# -----------------------------------------------------------------------
drop table if exists OLE_PTRN_NTE_S
/

CREATE TABLE OLE_PTRN_NTE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_PTRN_NTE_S auto_increment = 52
/

# -----------------------------------------------------------------------
# OLE_PTRN_NTE_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_PTRN_NTE_TYP_S
/

CREATE TABLE OLE_PTRN_NTE_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_PTRN_NTE_TYP_S auto_increment = 6
/

# -----------------------------------------------------------------------
# OLE_PTRN_PAY_STA_S
# -----------------------------------------------------------------------
drop table if exists OLE_PTRN_PAY_STA_S
/

CREATE TABLE OLE_PTRN_PAY_STA_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_PTRN_PAY_STA_S auto_increment = 20
/

# -----------------------------------------------------------------------
# OLE_PTRN_SUM_S
# -----------------------------------------------------------------------
drop table if exists OLE_PTRN_SUM_S
/

CREATE TABLE OLE_PTRN_SUM_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_PTRN_SUM_S auto_increment = 2
/

# -----------------------------------------------------------------------
# OLE_PYMT_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_PYMT_TYP_S
/

CREATE TABLE OLE_PYMT_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_PYMT_TYP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_REQ_PRTY_S
# -----------------------------------------------------------------------
drop table if exists OLE_REQ_PRTY_S
/

CREATE TABLE OLE_REQ_PRTY_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_REQ_PRTY_S auto_increment = 9
/

# -----------------------------------------------------------------------
# OLE_SER_RCV_HIS_SEQ
# -----------------------------------------------------------------------
drop table if exists OLE_SER_RCV_HIS_SEQ
/

CREATE TABLE OLE_SER_RCV_HIS_SEQ
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_SER_RCV_HIS_SEQ auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_SER_RCV_SEQ
# -----------------------------------------------------------------------
drop table if exists OLE_SER_RCV_SEQ
/

CREATE TABLE OLE_SER_RCV_SEQ
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_SER_RCV_SEQ auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_VNDR_ACC_INFO_S
# -----------------------------------------------------------------------
drop table if exists OLE_VNDR_ACC_INFO_S
/

CREATE TABLE OLE_VNDR_ACC_INFO_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_VNDR_ACC_INFO_S auto_increment = 19
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_PRF_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_PRF_S
/

CREATE TABLE OLE_BAT_PRCS_PRF_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_PRF_S auto_increment = 22
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_DT_MAP_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_DT_MAP_S
/

CREATE TABLE OLE_BAT_PRCS_DT_MAP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_DT_MAP_S auto_increment = 10
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_DT_MAP_OPT_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_DT_MAP_OPT_S
/

CREATE TABLE OLE_BAT_PRCS_DT_MAP_OPT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_DT_MAP_OPT_S auto_increment = 19
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_FILTER_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_FILTER_S
/

CREATE TABLE OLE_BAT_PRCS_FILTER_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_FILTER_S auto_increment = 5
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_FLE_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_FLE_TYP_S
/

CREATE TABLE OLE_BAT_PRCS_FLE_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_FLE_TYP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_MNTN_FIELD_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_MNTN_FIELD_S
/

CREATE TABLE OLE_BAT_PRCS_MNTN_FIELD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_MNTN_FIELD_S auto_increment = 10
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_PRFLE_CNST_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_PRFLE_CNST_S
/

CREATE TABLE OLE_BAT_PRCS_PRFLE_CNST_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_PRFLE_CNST_S auto_increment = 66
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_TYP_S
/

CREATE TABLE OLE_BAT_PRCS_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_TYP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_PRCT_FLD_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_PRCT_FLD_S
/

CREATE TABLE OLE_BAT_PRCS_PRCT_FLD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_PRCT_FLD_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_PRF_BIB_DT_MAP_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRF_BIB_DT_MAP_S
/

CREATE TABLE OLE_BAT_PRF_BIB_DT_MAP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRF_BIB_DT_MAP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_PRF_BIB_DT_MAP_OVER_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRF_BIB_DT_MAP_OVER_S
/

CREATE TABLE OLE_BAT_PRF_BIB_DT_MAP_OVER_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRF_BIB_DT_MAP_OVER_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_S
/

CREATE TABLE OLE_BAT_PRCS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_JOB_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_JOB_S
/

CREATE TABLE OLE_BAT_PRCS_JOB_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_JOB_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_GLBLY_PRCT_FLD_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_GLBLY_PRCT_FLD_S
/

CREATE TABLE OLE_BAT_GLBLY_PRCT_FLD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_GLBLY_PRCT_FLD_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_SCHDULE_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_SCHDULE_S
/

CREATE TABLE OLE_BAT_PRCS_SCHDULE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_SCHDULE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_PRF_DEL_FLD_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRF_DEL_FLD_S
/

CREATE TABLE OLE_BAT_PRF_DEL_FLD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRF_DEL_FLD_S auto_increment = 12
/

# -----------------------------------------------------------------------
# OLE_BAT_PRF_RNM_FLD_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRF_RNM_FLD_S
/

CREATE TABLE OLE_BAT_PRF_RNM_FLD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRF_RNM_FLD_S auto_increment = 12
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_PRF_MTCH_POINT_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_PRF_MTCH_POINT_S
/

CREATE TABLE OLE_BAT_PRCS_PRF_MTCH_POINT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_PRF_MTCH_POINT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_INST_MTCH_PNT_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_INST_MTCH_PNT_S
/

CREATE TABLE OLE_BAT_INST_MTCH_PNT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_INST_MTCH_PNT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_BIB_MTCH_PNT_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_BIB_MTCH_PNT_S
/

CREATE TABLE OLE_BAT_BIB_MTCH_PNT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_BIB_MTCH_PNT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_PRCS_BIB_STUS_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_PRCS_BIB_STUS_S
/

CREATE TABLE OLE_BAT_PRCS_BIB_STUS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_PRCS_BIB_STUS_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_BIB_WRK_UNT_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_BIB_WRK_UNT_S
/

CREATE TABLE OLE_BAT_BIB_WRK_UNT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_BIB_WRK_UNT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_BAT_INST_WRK_UNT_S
# -----------------------------------------------------------------------
drop table if exists OLE_BAT_INST_WRK_UNT_S
/

CREATE TABLE OLE_BAT_INST_WRK_UNT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_BAT_INST_WRK_UNT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_CLNDR_EXCP_DAT_S
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_EXCP_DAT_S
/

CREATE TABLE OLE_CLNDR_EXCP_DAT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CLNDR_EXCP_DAT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_CLNDR_EXCP_PRD_S
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_EXCP_PRD_S
/

CREATE TABLE OLE_CLNDR_EXCP_PRD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CLNDR_EXCP_PRD_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_CLNDR_EXCP_PRD_WK_S
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_EXCP_PRD_WK_S
/

CREATE TABLE OLE_CLNDR_EXCP_PRD_WK_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CLNDR_EXCP_PRD_WK_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_CLNDR_S
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_S
/

CREATE TABLE OLE_CLNDR_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CLNDR_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_CLNDR_WK_S
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_WK_S
/

CREATE TABLE OLE_CLNDR_WK_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CLNDR_WK_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_CLNDR_GRP_S
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_GRP_S
/

CREATE TABLE OLE_CLNDR_GRP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CLNDR_GRP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_FRMT_TYPS_S
# -----------------------------------------------------------------------
drop table if exists OLE_FRMT_TYPS_S
/

CREATE TABLE OLE_FRMT_TYPS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_FRMT_TYPS_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_SER_RCV_REC_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_SER_RCV_REC_TYP_S
/

CREATE TABLE OLE_SER_RCV_REC_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_SER_RCV_REC_TYP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_CLNDR_EXP_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_CLNDR_EXP_TYP_S
/

CREATE TABLE OLE_CLNDR_EXP_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CLNDR_EXP_TYP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_CNCL_RSN_S
# -----------------------------------------------------------------------
drop table if exists OLE_CNCL_RSN_S
/

CREATE TABLE OLE_CNCL_RSN_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CNCL_RSN_S auto_increment = 6
/

# -----------------------------------------------------------------------
# OLE_CLAIM_TYP_S
# -----------------------------------------------------------------------
drop table if exists OLE_CLAIM_TYP_S
/

CREATE TABLE OLE_CLAIM_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CLAIM_TYP_S auto_increment = 14
/

# -----------------------------------------------------------------------
# OLE_CLM_NTCE_S
# -----------------------------------------------------------------------
drop table if exists OLE_CLM_NTCE_S
/

CREATE TABLE OLE_CLM_NTCE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_CLM_NTCE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_MISING_PICS_ITEM_S
# -----------------------------------------------------------------------
drop table if exists OLE_MISING_PICS_ITEM_S
/

CREATE TABLE OLE_MISING_PICS_ITEM_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_MISING_PICS_ITEM_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_COPY_FORMAT_TYPE_S
# -----------------------------------------------------------------------
drop table if exists OLE_COPY_FORMAT_TYPE_S
/

CREATE TABLE OLE_COPY_FORMAT_TYPE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_COPY_FORMAT_TYPE_S auto_increment = 3
/

# -----------------------------------------------------------------------
# OLE_ASR_ITM_S
# -----------------------------------------------------------------------
drop table if exists OLE_ASR_ITM_S
/

CREATE TABLE OLE_ASR_ITM_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_ASR_ITM_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_ASR_RQST_S
# -----------------------------------------------------------------------
drop table if exists OLE_ASR_RQST_S
/

CREATE TABLE OLE_ASR_RQST_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_ASR_RQST_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DONOR_S
# -----------------------------------------------------------------------
drop table if exists OLE_DONOR_S
/

CREATE TABLE OLE_DONOR_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DONOR_S auto_increment = 3
/

# -----------------------------------------------------------------------
# OLE_DS_DOC_TYPE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_DOC_TYPE_S
/

CREATE TABLE OLE_DS_DOC_TYPE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_DOC_TYPE_S auto_increment = 10
/

# -----------------------------------------------------------------------
# OLE_DS_DOC_FORMAT_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_DOC_FORMAT_S
/

CREATE TABLE OLE_DS_DOC_FORMAT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_DOC_FORMAT_S auto_increment = 20
/

# -----------------------------------------------------------------------
# OLE_DS_DOC_FIELD_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_DOC_FIELD_S
/

CREATE TABLE OLE_DS_DOC_FIELD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_DOC_FIELD_S auto_increment = 1000
/

# -----------------------------------------------------------------------
# OLE_DS_SEARCH_RESULT_PAGE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_SEARCH_RESULT_PAGE_S
/

CREATE TABLE OLE_DS_SEARCH_RESULT_PAGE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_SEARCH_RESULT_PAGE_S auto_increment = 20
/

# -----------------------------------------------------------------------
# OLE_DS_SEARCH_FACET_SIZE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DS_SEARCH_FACET_SIZE_S
/

CREATE TABLE OLE_DS_SEARCH_FACET_SIZE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DS_SEARCH_FACET_SIZE_S auto_increment = 2
/

# -----------------------------------------------------------------------
# OLE_DLVR_LOAN_NOTICE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_LOAN_NOTICE_S
/

CREATE TABLE OLE_DLVR_LOAN_NOTICE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_LOAN_NOTICE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_LOAN_NOTICE_HSTRY_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_LOAN_NOTICE_HSTRY_S
/

CREATE TABLE OLE_DLVR_LOAN_NOTICE_HSTRY_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_LOAN_NOTICE_HSTRY_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_RETURN_HISTORY_S
# -----------------------------------------------------------------------
drop table if exists OLE_RETURN_HISTORY_S
/

CREATE TABLE OLE_RETURN_HISTORY_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_RETURN_HISTORY_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_VRNT_TTL_S
# -----------------------------------------------------------------------
drop table if exists OLE_VRNT_TTL_S
/

CREATE TABLE OLE_VRNT_TTL_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_VRNT_TTL_S auto_increment = 1
/

# -----------------------------------------------------------------------
# ACTN_LST_ALRT_S
# -----------------------------------------------------------------------
drop table if exists ACTN_LST_ALRT_S
/

CREATE TABLE ACTN_LST_ALRT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE ACTN_LST_ALRT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# ALRT_S
# -----------------------------------------------------------------------
drop table if exists ALRT_S
/

CREATE TABLE ALRT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE ALRT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_E_RES_ACCTG_LN_S
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_ACCTG_LN_S
/

CREATE TABLE OLE_E_RES_ACCTG_LN_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_E_RES_ACCTG_LN_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_GOKB_PKG_S
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_PKG_S
/

CREATE TABLE OLE_GOKB_PKG_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_GOKB_PKG_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_GOKB_TIPP_S
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_TIPP_S
/

CREATE TABLE OLE_GOKB_TIPP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_GOKB_TIPP_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_GOKB_TITLE_S
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_TITLE_S
/

CREATE TABLE OLE_GOKB_TITLE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_GOKB_TITLE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_GOKB_PLTFRM_S
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_PLTFRM_S
/

CREATE TABLE OLE_GOKB_PLTFRM_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_GOKB_PLTFRM_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_GOKB_ORG_S
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_ORG_S
/

CREATE TABLE OLE_GOKB_ORG_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_GOKB_ORG_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_GOKB_UPDATE_LOG_S
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_UPDATE_LOG_S
/

CREATE TABLE OLE_GOKB_UPDATE_LOG_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_GOKB_UPDATE_LOG_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_GOKB_ORG_ROLE_S
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_ORG_ROLE_S
/

CREATE TABLE OLE_GOKB_ORG_ROLE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_GOKB_ORG_ROLE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_GOKB_REVIEW_S
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_REVIEW_S
/

CREATE TABLE OLE_GOKB_REVIEW_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_GOKB_REVIEW_S auto_increment = 1
/

# -----------------------------------------------------------------------
# GOKB_CHANGE_LOG_S
# -----------------------------------------------------------------------
drop table if exists GOKB_CHANGE_LOG_S
/

CREATE TABLE GOKB_CHANGE_LOG_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE GOKB_CHANGE_LOG_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_GOKB_ARCHIVE_S
# -----------------------------------------------------------------------
drop table if exists OLE_GOKB_ARCHIVE_S
/

CREATE TABLE OLE_GOKB_ARCHIVE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_GOKB_ARCHIVE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# ALRT_DOC_TYP_S
# -----------------------------------------------------------------------
drop table if exists ALRT_DOC_TYP_S
/

CREATE TABLE ALRT_DOC_TYP_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE ALRT_DOC_TYP_S auto_increment = 71
/

# -----------------------------------------------------------------------
# OLE_E_RES_ACCESS_S
# -----------------------------------------------------------------------
drop table if exists OLE_E_RES_ACCESS_S
/

CREATE TABLE OLE_E_RES_ACCESS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_E_RES_ACCESS_S auto_increment = 1
/

# -----------------------------------------------------------------------
# ALRT_DOC_S
# -----------------------------------------------------------------------
drop table if exists ALRT_DOC_S
/

CREATE TABLE ALRT_DOC_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE ALRT_DOC_S auto_increment = 1
/

# -----------------------------------------------------------------------
# ALRT_CNDTN_NTFCN_INFO_S
# -----------------------------------------------------------------------
drop table if exists ALRT_CNDTN_NTFCN_INFO_S
/

CREATE TABLE ALRT_CNDTN_NTFCN_INFO_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE ALRT_CNDTN_NTFCN_INFO_S auto_increment = 1
/

# -----------------------------------------------------------------------
# ALRT_EVENT_S
# -----------------------------------------------------------------------
drop table if exists ALRT_EVENT_S
/

CREATE TABLE ALRT_EVENT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE ALRT_EVENT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# ALRT_EVENT_FIELD_S
# -----------------------------------------------------------------------
drop table if exists ALRT_EVENT_FIELD_S
/

CREATE TABLE ALRT_EVENT_FIELD_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE ALRT_EVENT_FIELD_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_RNWL_HISTORY_S
# -----------------------------------------------------------------------
drop table if exists OLE_RNWL_HISTORY_S
/

CREATE TABLE OLE_RNWL_HISTORY_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_RNWL_HISTORY_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DRL_EDITOR_S
# -----------------------------------------------------------------------
drop table if exists OLE_DRL_EDITOR_S
/

CREATE TABLE OLE_DRL_EDITOR_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DRL_EDITOR_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DRL_RULE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DRL_RULE_S
/

CREATE TABLE OLE_DRL_RULE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DRL_RULE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DRL_FINE_LIMITS_S
# -----------------------------------------------------------------------
drop table if exists OLE_DRL_FINE_LIMITS_S
/

CREATE TABLE OLE_DRL_FINE_LIMITS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DRL_FINE_LIMITS_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_NOTICE_TYPE_CONFIG_S
# -----------------------------------------------------------------------
drop table if exists OLE_NOTICE_TYPE_CONFIG_S
/

CREATE TABLE OLE_NOTICE_TYPE_CONFIG_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_NOTICE_TYPE_CONFIG_S auto_increment = 7
/

# -----------------------------------------------------------------------
# OLE_NOTC_FIELD_LABEL_MAPNG_S
# -----------------------------------------------------------------------
drop table if exists OLE_NOTC_FIELD_LABEL_MAPNG_S
/

CREATE TABLE OLE_NOTC_FIELD_LABEL_MAPNG_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_NOTC_FIELD_LABEL_MAPNG_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_NOTC_CNTNT_CONFIG_S
# -----------------------------------------------------------------------
drop table if exists OLE_NOTC_CNTNT_CONFIG_S
/

CREATE TABLE OLE_NOTC_CNTNT_CONFIG_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_NOTC_CNTNT_CONFIG_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_PHONE_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_PHONE_S
/

CREATE TABLE OLE_DLVR_PHONE_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_PHONE_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_DLVR_EMAIL_S
# -----------------------------------------------------------------------
drop table if exists OLE_DLVR_EMAIL_S
/

CREATE TABLE OLE_DLVR_EMAIL_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_DLVR_EMAIL_S auto_increment = 1
/

# -----------------------------------------------------------------------
# ITEM_AUDIT_S
# -----------------------------------------------------------------------
drop table if exists ITEM_AUDIT_S
/

CREATE TABLE ITEM_AUDIT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE ITEM_AUDIT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# BIB_AUDIT_S
# -----------------------------------------------------------------------
drop table if exists BIB_AUDIT_S
/

CREATE TABLE BIB_AUDIT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE BIB_AUDIT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# HOLDINGS_AUDIT_S
# -----------------------------------------------------------------------
drop table if exists HOLDINGS_AUDIT_S
/

CREATE TABLE HOLDINGS_AUDIT_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE HOLDINGS_AUDIT_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_NG_BAT_PRF_S
# -----------------------------------------------------------------------
drop table if exists OLE_NG_BAT_PRF_S
/

CREATE TABLE OLE_NG_BAT_PRF_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_NG_BAT_PRF_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_NG_BAT_PRCS_JOB_S
# -----------------------------------------------------------------------
drop table if exists OLE_NG_BAT_PRCS_JOB_S
/

CREATE TABLE OLE_NG_BAT_PRCS_JOB_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_NG_BAT_PRCS_JOB_S auto_increment = 1
/

# -----------------------------------------------------------------------
# OLE_NG_BAT_JOB_DETAILS_S
# -----------------------------------------------------------------------
drop table if exists OLE_NG_BAT_JOB_DETAILS_S
/

CREATE TABLE OLE_NG_BAT_JOB_DETAILS_S
(
	id bigint(19) not null auto_increment, primary key (id) 
) ENGINE MyISAM
/
ALTER TABLE OLE_NG_BAT_JOB_DETAILS_S auto_increment = 1
/
