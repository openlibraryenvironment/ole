package org.kuali.ole.select.form;

import org.kuali.ole.select.bo.OLEEResourceChangeDashBoard;
import org.kuali.ole.select.gokb.OleGokbChangeLog;
import org.kuali.ole.select.gokb.OleGokbReview;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by srirams on 18/9/14.
 */
public class OLEEResourceChangeDashboardForm extends UifFormBase {

    private List<String> date;

    private List<String> type;

    private List<String> eresource;

    private List<String> title;

    private List<String> detail;

    private List<String> origin;

    private String selectedDate;

    private String selectedType;

    private String selectedEresource;

    private String selectedDetails;

    private String selectedOrigin;

    private String selectedTitle;

    private String selectedTab = "To-Do";

    private Timestamp archivedDate;

    private List<OleGokbReview> oleGokbReviews = new ArrayList();

    private List<OleGokbReview> oleGokbReviewList = new ArrayList();

    private List<OleGokbChangeLog> oleGokbChangeLogs = new ArrayList();

    private List<OLEEResourceChangeDashBoard> oleEResourceChangeDashBoards;

    private List<OLEEResourceChangeDashBoard> oleeResourceChangeDashBoardList;

    public OLEEResourceChangeDashboardForm() {
        this.oleEResourceChangeDashBoards = new ArrayList<OLEEResourceChangeDashBoard>();
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public String getSelectedEresource() {
        return selectedEresource;
    }

    public void setSelectedEresource(String selectedEresource) {
        this.selectedEresource = selectedEresource;
    }

    public String getSelectedDetails() {
        return selectedDetails;
    }

    public void setSelectedDetails(String selectedDetails) {
        this.selectedDetails = selectedDetails;
    }

    public String getSelectedOrigin() {
        return selectedOrigin;
    }

    public void setSelectedOrigin(String selectedOrigin) {
        this.selectedOrigin = selectedOrigin;
    }

    public String getSelectedTitle() {
        return selectedTitle;
    }

    public void setSelectedTitle(String selectedTitle) {
        this.selectedTitle = selectedTitle;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<String> getEresource() {
        return eresource;
    }

    public void setEresource(List<String> eresource) {
        this.eresource = eresource;
    }

    public List<String> getDetail() {
        return detail;
    }

    public void setDetail(List<String> detail) {
        this.detail = detail;
    }

    public List<String> getOrigin() {
        return origin;
    }

    public void setOrigin(List<String> origin) {
        this.origin = origin;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }

    public List<OLEEResourceChangeDashBoard> getOleeResourceChangeDashBoardList() {
        return oleeResourceChangeDashBoardList;
    }

    public void setOleeResourceChangeDashBoardList(List<OLEEResourceChangeDashBoard> oleeResourceChangeDashBoardList) {
        this.oleeResourceChangeDashBoardList = oleeResourceChangeDashBoardList;
    }

    public List<OLEEResourceChangeDashBoard> getOleEResourceChangeDashBoards() {
        return oleEResourceChangeDashBoards;
    }

    public void setOleEResourceChangeDashBoards(List<OLEEResourceChangeDashBoard> oleEResourceChangeDashBoards) {
        this.oleEResourceChangeDashBoards = oleEResourceChangeDashBoards;
    }

    public List<OleGokbReview> getOleGokbReviews() {
        return oleGokbReviews;
    }

    public void setOleGokbReviews(List<OleGokbReview> oleGokbReviews) {
        this.oleGokbReviews = oleGokbReviews;
    }

    public List<OleGokbReview> getOleGokbReviewList() {
        return oleGokbReviewList;
    }

    public void setOleGokbReviewList(List<OleGokbReview> oleGokbReviewList) {
        this.oleGokbReviewList = oleGokbReviewList;
    }

    public List<OleGokbChangeLog> getOleGokbChangeLogs() {
        return oleGokbChangeLogs;
    }

    public void setOleGokbChangeLogs(List<OleGokbChangeLog> oleGokbChangeLogs) {
        this.oleGokbChangeLogs = oleGokbChangeLogs;
    }

    public Timestamp getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(Timestamp archivedDate) {
        this.archivedDate = archivedDate;
    }
}
