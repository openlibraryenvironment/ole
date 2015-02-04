package org.kuali.ole.deliver.api;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 6/6/12
 * Time: 7:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLoanDefintionHelper {

    private static final Date DueDate =new Date((new Timestamp(System.currentTimeMillis())).getTime());
    //  private static final String OBJECT_ID = String.valueOf(UUID.randomUUID());
    private static final Date LoanPeriodDate = new Date((new Timestamp(System.currentTimeMillis())).getTime());
   // private static final Date AlterDueDate = mew Date(())
    private static final Integer LoanPeriod = new Integer(15);
    private static final Long VERSION_NUMBER = new Long(1);

    private static final String LoanStatusId = "10";
    private static final String LoanStatusCode = "MOCKLOANST";
    private static final String LoanStatusName = "MockLoanStatusNm";
    private static final Long LoanStatus_VERSION_NUMBER = new Long(1);
    private static final String LoanStatus_OBJECT_ID = String.valueOf(UUID.randomUUID());

    private static final String LoanTermUnitId = "70";
    private static final String LoanTermUnitCode = "MOCKTERMCD";
    private static final String LoanTermUnitName = "MockTermUnitNm";
    private static final Long LoanTermUnit_VERSION_NUMBER = new Long(1);
    private static final String LoanTermUnit_OBJECT_ID = String.valueOf(UUID.randomUUID());

    private static final String LocationId = "T1000";
    private static final String LocationCode = "MockShelving";
    private static final String LocationName = "Mock Shelving Location Name";
    private static final String LevelId = "5";
    private static final String ParentLocationId = "T999";
    private static final Long Location_VERSION_NUMBER = new Long(1);

    private static final String LocationId1 = "T999";
    private static final String LocationCode1 = "MockCOLLECTION";
    private static final String LocationName1 = "Mock Collection";
    private static final String LevelId1 = "4";
    private static final String ParentLocationId1 = "T998";

    private static final String LocationId2 = "T998";
    private static final String LocationCode2 = "MockLIBRARY";
    private static final String LocationName2 = "Mock Library";
    private static final String LevelId2 = "3";
    private static final String ParentLocationId2 = "T997";

    private static final String LocationId3 = "T997";
    private static final String LocationCode3 = "MockCAMPUS";
    private static final String LocationName3 = "Mock Campus";
    private static final String LevelId3 = "2";
    private static final String ParentLocationId3 = "T996";

    private static final String LocationId4 = "T996";
    private static final String LocationCode4 = "MockINSTITUTION";
    private static final String LocationName4 = "Mock Institution";
    private static final String LevelId4 = "1";
    private static final String ParentLocationId4 = null;







    public Long getLoanTermUnit_VERSION_NUMBER(){
        return LoanTermUnit_VERSION_NUMBER;
    }

    public Long getLoanStatus_VERSION_NUMBER(){
        return LoanStatus_VERSION_NUMBER;
    }


    public Long getLocation_VERSION_NUMBER(){
        return Location_VERSION_NUMBER;
    }

    public String getLocationId(){
        return LocationId;
    }

    public String getLocationCode(){
        return LocationCode;
    }

    public String getLocationName(){
        return LocationName;
    }

    public String getLevelId(){
        return LevelId;
    }

    public String getParentLocationId(){
        return ParentLocationId;
    }


    public Long getVersionNumber(){
        return VERSION_NUMBER;
    }

    public Date getDueDate(){
        return DueDate;
    }

    public Date getLoanPeriodDate(){
        return LoanPeriodDate;
    }

    public Integer getLoanPeriod(){
        return LoanPeriod;
    }

    public String getLoanStatusId(){
        return LoanStatusId;
    }

    public String getLoanTermUnitId(){
        return LoanTermUnitId;
    }

    public String getLoanTermUnitCode(){
        return LoanTermUnitCode;
    }

    public String getLoanStatusName(){
        return LoanStatusName;
    }

    public String getLoanStatusCode(){
        return LoanStatusCode;
    }

    public String getLoanTermUnitName(){

        return LoanTermUnitName;
    }

    public static String getLocationId1() {
        return LocationId1;
    }

    public static String getLocationCode1() {
        return LocationCode1;
    }

    public static String getLocationName1() {
        return LocationName1;
    }

    public static String getLevelId1() {
        return LevelId1;
    }

    public static String getParentLocationId1() {
        return ParentLocationId1;
    }

    public static String getLocationId2() {
        return LocationId2;
    }

    public static String getLocationCode2() {
        return LocationCode2;
    }

    public static String getLocationName2() {
        return LocationName2;
    }

    public static String getLevelId2() {
        return LevelId2;
    }

    public static String getParentLocationId2() {
        return ParentLocationId2;
    }

    public static String getLocationId3() {
        return LocationId3;
    }

    public static String getLocationCode3() {
        return LocationCode3;
    }

    public static String getLocationName3() {
        return LocationName3;
    }

    public static String getLevelId3() {
        return LevelId3;
    }

    public static String getParentLocationId3() {
        return ParentLocationId3;
    }

    public static String getLocationId4() {
        return LocationId4;
    }

    public static String getLocationCode4() {
        return LocationCode4;
    }

    public static String getLocationName4() {
        return LocationName4;
    }

    public static String getLevelId4() {
        return LevelId4;
    }

    public static String getParentLocationId4() {
        return ParentLocationId4;
    }
}
