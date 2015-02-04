package org.kuali.ole.docstore.discovery.model;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 22/2/13
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class CallNumberBrowseParams {

    private String location;
    private String classificationScheme;
    private String callNumberBrowseText;

    private String title;

    private int numRows;
    private int startIndex;
    private int matchIndex;

    private int totalCallNumberCount;
    private int totalForwardCallNumberCount;
    private String docTye;

    public CallNumberBrowseParams() {

    }

    public CallNumberBrowseParams(String location, String classificationScheme, String callNumberBrowseText, int numRows, int startIndex, int matchIndex, int totalCallNumberCount, int totalForwardCallNumberCount, String docTye , String title) {
        this.location = location;
        this.classificationScheme = classificationScheme;
        this.callNumberBrowseText = callNumberBrowseText;
        this.numRows = numRows;
        this.startIndex = startIndex;
        this.matchIndex = matchIndex;
        this.totalCallNumberCount = totalCallNumberCount;
        this.totalForwardCallNumberCount = totalForwardCallNumberCount;
        this.docTye = docTye;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClassificationScheme() {
        return classificationScheme;
    }

    public void setClassificationScheme(String classificationScheme) {
        this.classificationScheme = classificationScheme;
    }

    public String getCallNumberBrowseText() {
        return callNumberBrowseText;
    }

    public void setCallNumberBrowseText(String callNumberBrowseText) {
        this.callNumberBrowseText = callNumberBrowseText;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getMatchIndex() {
        return matchIndex;
    }

    public void setMatchIndex(int matchIndex) {
        this.matchIndex = matchIndex;
    }

    public int getTotalCallNumberCount() {
        return totalCallNumberCount;
    }

    public void setTotalCallNumberCount(int totalCallNumberCount) {
        this.totalCallNumberCount = totalCallNumberCount;
    }

    public int getTotalForwardCallNumberCount() {
        return totalForwardCallNumberCount;
    }

    public void setTotalForwardCallNumberCount(int totalForwardCallNumberCount) {
        this.totalForwardCallNumberCount = totalForwardCallNumberCount;
    }

    public String getDocTye() {
        return docTye;
    }

    public void setDocTye(String docTye) {
        this.docTye = docTye;
    }

    @Override
    public String toString() {
        return "CallNumberBrowseParams{" +
                "location='" + location + '\'' +
                ", classificationScheme='" + classificationScheme + '\'' +
                ", callNumberBrowseText='" + callNumberBrowseText + '\'' +
                ", numRows=" + numRows +
                ", startIndex=" + startIndex +
                ", matchIndex=" + matchIndex +
                ", totalCallNumberCount=" + totalCallNumberCount +
                ", totalForwardCallNumberCount=" + totalForwardCallNumberCount +
                ", docType=" + docTye +
                '}';
    }
}
