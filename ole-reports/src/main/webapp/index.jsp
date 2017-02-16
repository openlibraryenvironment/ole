<%@ page import="java.util.Date" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Kuali Portal Index</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="keywords" content="jquery,ui,easy,easyui,web">
    <meta name="description" content="easyui help you build your web page easily!">

    <link rel="stylesheet" type="text/css" href="css/easyui.css">
    <link rel="stylesheet" type="text/css" href="css/icon.css">
    <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js"></script>
    <link type="text/css" rel="Stylesheet"
          href="css/jquery-ui-1.8.16.custom.css"/>

    <%--header start--%>
    <link href="./css/olePortal.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">
        .center {
            margin-left: auto;
            margin-right: auto;
            width: 95%;
        }
        p {
            font-size: 125%;
        }
        h5 {
            font-size: 200%;
        }
    </style>

</head>
<body>

<div id="header" title="Kuali Open Library Environment">
    <h1 class="kfs"></h1>
</div>
<div id="feedback">
    <a class="portal_link" href="#"
       title="Provide Feedback">Provide Feedback</a>
</div>
<div id="build"><%=new Date()%>
</div>
<div id="tabs" class="tabposition">
    <ul>
        <li class="red"><a class="red" href="." title="Main Menu" onclick="show()">Reports</a></li>
    </ul>
</div>
<div class="header2">
    <div class="header2-left-focus">
        <div class="breadcrumb-focus">

        </div>
    </div>
</div>
<div id="iframe_portlet_container_div">
    <br/>
    <div class="center">

        <%   if (request != null && request.getRequestURL() != null && request.getRequestURL().toString().contains("reports.staging.oleproject.org")) {%>
        <p>Note: The reports data source is pointing to the staging environment at the moment.</p>
        <% } %>

        <ul>
            <h5>Deliver</h5>
            <ul>
                <%--<li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/itemTypeDropDown.rptdesign"%>" target="_blank">Item Type Drop Down</a></li>

                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/Hold.rptdesign"%>" target="_blank">Hold Slip</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/InTransit.rptdesign"%>" target="_blank">In Transit Slip</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/InTransitOnHold.rptdesign"%>" target="_blank">In Transit On Hold Slip</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/ListItemsReport.rptdesign"%>" target="_blank">List Items Report</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/HoldPickList.rptdesign"%>" target="_blank">Hold Pick List</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/PickListsForPageRequests.rptdesign"%>" target="_blank">Pick Lists For Page Requests</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/MissingItems.rptdesign"%>" target="_blank">Items Marked Missing That Need Searched For Staff</a></li>

                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/ShelfList.rptdesign"%>" target="_blank">Shelf List</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/PatronChargedItems.rptdesign"%>" target="_blank">Patron Charged Items</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/ClaimsReturnedItems.rptdesign"%>" target="_blank">Items Marked Claims Returned For Staff</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/AvailableHoldPickupNoticeForCustomer.rptdesign"%>" target="_blank">Available Hold Pickup Notice For Customer</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/HoldsThatWereReadyToBePickedUpForStaff.rptdesign"%>" target="_blank">Holds That Were Trapped Since Report Last Ran And Need To Be Checked To Make Sure They Are On The Hold Shelf Ready To Be PickedUp For Staff</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/RecallNoticeForCustomer.rptdesign"%>" target="_blank">Recall Notice For Customer</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/BillNoticeForCustomer.rptdesign"%>" target="_blank">Bill Notice For Customer</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/MissingOrLostItems.rptdesign"%>" target="_blank">Missing/Lost Items</a></li>

                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/HoldsPassedPickupPeriod.rptdesign"%>" target="_blank">Holds That Have Passed The Pickup Period And Need Removed From The Hold Shelf For Staff</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/OverdueItems.rptdesign"%>" target="_blank">Overdue Item Report</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/ExpiredPatronRecordsWithLoans.rptdesign"%>" target="_blank">Expired Patron Records With Loans</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/HoldsThatHaveBeenExpiredOrClosedButWereUnfilledForStaff.rptdesign"%>" target="_blank">Holds That Have Been Expired/Closed But Were Unfilled For Staff</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/CourtesyNotices.rptdesign"%>" target="_blank">Courtesy Notices</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/OverdueRecallNoticeForCustomer.rptdesign"%>" target="_blank">Overdue Recall Notice For Customer</a></li>--%>

                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/OutstandingHolds.rptdesign"%>" target="_blank">Outstanding Holds</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/CashTransactions.rptdesign"%>" target="_blank">Cash Transactions</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/LostOrMissingItems.rptdesign"%>" target="_blank">Lost/Missing Items</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/ItemTypeStatistics.rptdesign"%>" target="_blank">Item Type Statistics</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/CollectionStatistics.rptdesign"%>" target="_blank">Collection Statistics</a></li>
                <%--<li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/LostItems.rptdesign"%>" target="_blank">Lost Items</a></li>--%>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/GeneralStatistics.rptdesign"%>" target="_blank">General Statistics</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/StandardLoanBooks.rptdesign"%>" target="_blank">Standard Loan Books</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/ExpiredOnHolds.rptdesign"%>" target="_blank">Expired On-Holds</a></li>
                <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/MissingInTransit.rptdesign"%>" target="_blank">Missing In Transit</a></li>

            </ul>
        </ul>
    </div>
    <br/><br/><br/><br/>
</div>
<%@ include file="oleFooter.jsp" %>
</body>
</html>
