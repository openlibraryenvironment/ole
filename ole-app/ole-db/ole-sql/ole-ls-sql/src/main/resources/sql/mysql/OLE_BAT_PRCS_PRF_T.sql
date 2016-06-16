TRUNCATE TABLE OLE_BAT_PRCS_PRF_T
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Location','1','Test_Location_Import','N','N','N','','3f2e31e8-e5aa-4edd-aab5-5d3e49fe5e1c','Location Import','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Patron','2','Test_Patron_Import','N','N','N','','8934fe2f-d2c5-4810-9f2a-2c2b33d39f06','Patron Import','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_BIB_OVRL_ADD,BAT_PRCS_BIB_NO_MTCH,BAT_PRCS_INST_OVRL_ADD,BAT_PRCS_INST_NO_MTCH,BAT_PRCS_DNT_CHNG_001,BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_IMPRT,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,BAT_PRCS_MATCH_PROFILE,OBJ_ID,BAT_PRCS_MARC_ONLY,OLE_BAT_PRCS_TYP,VER_NBR)
  VALUES ('add','add','keepOldAddNew','add','delete001','Test_Desc_Bib','Bibliographic, Holdings, and Item Data','3','Test_Bib_Import','matchBibs=false,noMatchBibs_addBibs=true,bibNotMatched_discardBib=false,bibNotMatched_addBib=true,bibMatched_addBib=false,bibMatched_discardBib=false,bibMatched_updateBib=true,matchHoldings=true,noMatchHoldings_discardHoldingsItems=true,noMatchHoldings_deleteAddHoldingsItems=false,noMatchHoldings_retainAddHoldingsItems=false,holdingsNotMatched_discardHoldings=false,holdingsNotMatched_addHoldings=true,holdingsNotMatched_addItems=false,holdingsMatched_addHoldings=false,holdingsMatched_addItems=false,holdingsMatched_discardHoldings=false,holdingsMatched_updateHoldings=true,matchItems=true,noMatchItem_discardItems=true,noMatchItem_deleteAddItems=false,noMatchItem_retainAddItems=false,itemNotMatched_discardItem=false,itemNotMatched_addItem=true,itemMatched_addItem=false,itemMatched_updateItem=true','f6f342ca-d6b6-435f-adea-a6bb6dc5cedf','N','Bib Import',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_delete','4','Test_Batch_Delete','N','N','N','','8940b8bc-6e5b-4c34-b648-303dfd3a9c3d','Batch Delete','N','N','N',2)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,BAT_PRCS_BIB_IMP_PRF,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Invoice','5','Test_Invoice_Import','N','N','N','','ec55b25e-7823-4e59-9b16-1976b5d8a2cf','Test_Bib_Import','Invoice Import','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_NEW_BIB_STS,BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_IMPRT,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,BAT_PRCS_PRF_REQ_FR_TITL,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,BAT_PRCS_BIB_IMP_PRF,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('','Test_Desc_Order','Bibliographic Data Only','6','Test_Order_Import','One Requisition Per Title','N','N','N','YBP','76921b38-3e5f-45a9-b212-b39b80437248','Test_Bib_Import','Order Record Import','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_EXPRT,BAT_PRCS_PRF_EXPRT_SCP,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Batch_full','BIBONLY','full','7','Test_Batch_Export_full','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d774ba10','Batch Export','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_EXPRT,BAT_PRCS_PRF_EXPRT_SCP,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Batch_filter','BIBONLY','filter','8','Test_Batch_Export_Filter','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d774ea11','Batch Export','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_EXPRT,BAT_PRCS_PRF_EXPRT_SCP,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Batch','BIBONLY','incremental','9','Test_Batch_Export_Incremental','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d874ba10','Batch Export','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_EXPRT,BAT_PRCS_PRF_EXPRT_SCP,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Batch_BibOnly_Filter1','BIBONLY','filter','10','Test_Batch_Export_Filter1','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d874ba10','Batch Export','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_EXPRT,BAT_PRCS_PRF_EXPRT_SCP,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Batch_BibOnly_Filter2','BIBONLY','filter','11','Test_Batch_Export_Filter2','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d874ba10','Batch Export','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_EXPRT,BAT_PRCS_PRF_EXPRT_SCP,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Batch_BibAndHoldings_Filter','BIBANDHOLDINGS','filter','12','Test_Batch_Export_BibAndHoldings_Filter','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d874ba10','Batch Export','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_EXPRT,BAT_PRCS_PRF_EXPRT_SCP,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Batch_BibOnly_Incremental','BIBONLY','incremental','13','Test_Batch_Export_BibOnly_Incremental','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d874ba10','Batch Export','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_EXPRT,BAT_PRCS_PRF_EXPRT_SCP,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Batch_BibHoldings_Incremental','BIBANDHOLDINGS','incremental','14','Test_Bat_Export_BibHoldings_Incremental','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d874ba10','Batch Export','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_EXPRT,BAT_PRCS_PRF_EXPRT_SCP,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Batch_BibOnly_Full','BIBONLY','full','15','Test_Batch_Export_BibOnly_Full','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d874ba10','Batch Export','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_EXPRT,BAT_PRCS_PRF_EXPRT_SCP,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Batch_BibAndHoldings_Full','BIBANDHOLDINGS','full','16','Test_Batch_Export_BibAndHoldings_Full','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d874ba10','Batch Export','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_EXPRT,BAT_PRCS_PRF_EXPRT_SCP,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Batch_BibOnly_ex_staff','BIBONLY','ex_staff','17','Test_Batch_Export_BibOnly_ex_staff','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d874ba10','Batch Export','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_EXPRT,BAT_PRCS_PRF_EXPRT_SCP,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Batch_BibAndHoldings_ex_staff','BIBANDHOLDINGS','ex_staff','18','Test_Batch_Export_BibHoldings_ex_staff','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d874ba10','Batch Export','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Batch_Claim_Report_Gen','19','Test_Desc_Batch_Claim_Report_Gen','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d874ba10','Claim Report','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Serial_Record_Import','20','Test_Serial_Record_Import','N','N','N','','bbe3ef4f-764b-45c5-a875-64f8d874ba10','Serial Record Import','N','N','N',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_BIB_OVRL_ADD,BAT_PRCS_BIB_NO_MTCH,BAT_PRCS_INST_OVRL_ADD,BAT_PRCS_INST_NO_MTCH,BAT_PRCS_DNT_CHNG_001,BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_DT_TO_IMPRT,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,BAT_PRCS_MATCH_PROFILE,OBJ_ID,BAT_PRCS_MARC_ONLY,OLE_BAT_PRCS_TYP,VER_NBR)
  VALUES ('add','add','keepOldAddNew','add','delete001','Test_Desc_Bib_callNumber','Bibliographic, Holdings, and Item Data','21','Test_Bib_Import_datamapping','matchBibs=false,noMatchBibs_addBibs=true,bibNotMatched_discardBib=false,bibNotMatched_addBib=true,bibMatched_addBib=false,bibMatched_discardBib=false,bibMatched_updateBib=true,matchHoldings=true,noMatchHoldings_discardHoldingsItems=true,noMatchHoldings_deleteAddHoldingsItems=false,noMatchHoldings_retainAddHoldingsItems=false,holdingsNotMatched_discardHoldings=false,holdingsNotMatched_addHoldings=true,holdingsNotMatched_addItems=false,holdingsMatched_addHoldings=false,holdingsMatched_addItems=false,holdingsMatched_discardHoldings=false,holdingsMatched_updateHoldings=true,matchItems=true,noMatchItem_discardItems=true,noMatchItem_deleteAddItems=false,noMatchItem_retainAddItems=false,itemNotMatched_discardItem=false,itemNotMatched_addItem=true,itemMatched_addItem=false,itemMatched_updateItem=true','f6f342ca-d6b6-435f-adea-a6bb6dc5cedf','N','Bib Import',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_BIB_OVRL_ADD,BAT_PRCS_BIB_NO_MTCH,BAT_PRCS_INST_OVRL_ADD,BAT_PRCS_INST_NO_MTCH,BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,OBJ_ID,BAT_PRCS_MARC_ONLY,OLE_BAT_PRCS_TYP,VER_NBR)
  VALUES ('add','add','keepOldAddNew','add','Default EDI Export Format','22','Default_EDI_Format','f6f342ca-d6b6-435f-adea-a6bbc10cey','N','EDI Export',1)
/
INSERT INTO OLE_BAT_PRCS_PRF_T (BAT_PRCS_PRF_DESC,BAT_PRCS_PRF_ID,BAT_PRCS_PRF_NM,IS_BIB_STF_ONLY,IS_INST_STF_ONLY,IS_ITM_STF_ONLY,KRMS_PRFL_NM,OBJ_ID,OLE_BAT_PRCS_TYP,PRPND_003_TO_035,BAT_PRCS_MARC_ONLY,PRPND_VAL_TO_035,VER_NBR)
  VALUES ('Test_Desc_Fund_Record_Import','23','Test_Fund_Record_Import','N','N','N','','bbe3ef4f-764b-45c5-a875-64f6d674ba10','Fund Record Import','N','N','N',1)
/
