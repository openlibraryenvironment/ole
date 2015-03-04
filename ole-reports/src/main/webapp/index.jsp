<html>
<head>
    <title>OLE Reporting Framework</title>
</head>
<style type="text/css">
    .reportingView {
        width: 900px;
        height: 700px;
        margin: 10px auto;
        padding: 10px;
        border: 7px solid gray;
        border-radius: 10px;
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        color: #444;
        background-color: #F0F0F0;
        box-shadow: 0 0 20px 0 #000000;
    }
</style>
<body>
<div id="bodyDive" class="reportingView">
    <table border="0" cellpadding="10">
        <tr>
            <td>
                <img src="images/logo-ole.gif" height="100" width="200">
            </td>
            <td>
                <h1>OLE Reporting Framework</h1>
            </td>
        </tr>
    </table>

    <p>This is the home page for the OLE Reporting Framework Application. </p>
    <ul>
        <li style="color: blue"><u>Deliver</u></li>
        <ul>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/Hold.rptdesign"%>" target="_blank">Hold Slip</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/InTransit.rptdesign"%>" target="_blank">In Transit Slip</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/InTransitOnHold.rptdesign"%>" target="_blank">In Transit On Hold Slip</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/ListItemsReport.rptdesign"%>" target="_blank">List Items Report</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/HoldPickList.rptdesign"%>" target="_blank">Hold Pick List</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/PickListsForPageRequests.rptdesign"%>" target="_blank">Pick Lists For Page Requests</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/MissingInTransit.rptdesign"%>" target="_blank">Missing In Transit</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/MissingItems.rptdesign"%>" target="_blank">Items Marked Missing That Need Searched For Staff</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/ShelfList.rptdesign"%>" target="_blank">Shelf List</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/HoldsPassedPickupPeriod.rptdesign"%>" target="_blank">Holds That Have Passed The Pickup Period And Need Removed From The Hold Shelf For Staff</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/PatronChargedItems.rptdesign"%>" target="_blank">Patron Charged Items</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/ClaimsReturnedItems.rptdesign"%>" target="_blank">Items Marked Claims Returned For Staff</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/OverdueItems.rptdesign"%>" target="_blank">Overdue Item Report</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/ExpiredPatronRecordsWithLoans.rptdesign"%>" target="_blank">Expired Patron Records With Loans</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/AvailableHoldPickupNoticeForCustomer.rptdesign"%>" target="_blank">Available Hold Pickup Notice For Customer</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/HoldsThatHaveBeenExpiredOrClosedButWereUnfilledForStaff.rptdesign"%>" target="_blank">Holds That Have Been Expired/Closed But Were Unfilled For Staff</a></li>
            <li><a href="<%= request.getContextPath() + "/frameset?__report=deliver/HoldsThatWereReadyToBePickedUpForStaff.rptdesign"%>" target="_blank">Holds That Were Trapped Since Report Last Ran And Need To Be Checked To Make Sure They Are On The Hold Shelf Ready To Be PickedUp For Staff</a></li>
        </ul>
    </ul>
</div>
</body>
</html>