
-----------------------------------------------------------------------------
-- EN_UNITTEST_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
	SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'EN_UNITTEST_T';
	IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE EN_UNITTEST_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE EN_UNITTEST_T
(
      COL VARCHAR2(1)
    

)
/

ALTER TABLE EN_UNITTEST_T
    ADD CONSTRAINT EN_UNITTEST_TP1
PRIMARY KEY (COL)
/






