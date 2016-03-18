<%--
  Created by IntelliJ IDEA.
  User: srirams
  Date: 24/2/16
  Time: 12:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="transferApp">
<head>
    <title>Search Workbench</title>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/angular-ui-tree.min.css">
    <link rel="stylesheet" href="css/app.css">
    <link rel="stylesheet" href="css/transfer.css">
    <!--<link rel="stylesheet" href="css/datatables.min.css">-->
    <link rel="stylesheet" href="css/oleng/bootstrap.min.css">
    <link rel="stylesheet" href="css/oleng/bootstrap.css">
    <link rel="stylesheet" href="css/oleng/bootstrap.css.map">
    <link rel="stylesheet" href="css/oleng/bootstrap.theme.css">
    <link rel="stylesheet" href="css/oleng/bootstrap.theme.css.map">
    <link rel="stylesheet" href="css/oleng/bootstrap.theme.min.css">
    <link rel="stylesheet" href="css/oleng/loading-bar.css">
    <link rel="stylesheet" href="css/oleng/styles.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.2/angular.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.2/angular-route.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="scripts/oleng/lib/bootstrap/ui-bootstrap-tpls-0.14.3.js"></script>
    <script src="scripts/oleng/ui-tree.js"></script>
    <script src="scripts/oleng/controllers/transfer.js"></script>
    <script src="scripts/oleng/controllers/treesController.js"></script>
    <script src="scripts/oleng/dirPagination.js"></script>
    <script src="scripts/oleng/lib/angular-animate.js"></script>
    <script src="scripts/oleng/lib/angular-loader.js"></script>
    <script src="scripts/oleng/lib/angular-sanitize.js"></script>
    <script src="scripts/oleng/lib/angular-scenario.js"></script>
    <script src="scripts/oleng/lib/angular-mocks.js"></script>
    <script src="scripts/oleng/lib/angular-strap.js"></script>

</head>
<body>
<div class="container">
    <form name="workBenchForm">
        <div>
            <br><br>
            <h2 style="display:inline !important; font-weight: bold;">Search Workbench</h2>
            <br><br>
        </div>
        <div>
            <div ng-controller="searchController" data-ng-init="init()">
                <div align="left">
                    <label for="DocumentType"> Document Type: </label>
                    <select name="DocumentType" id="DocumentType" name="documentType" ng-model="documentType" scope="session" >
                        <option ng-selected="{{option.id == documentType}}"
                                ng-repeat="option in documentTypes"  value="{{option.id}}">
                            {{option.name}}
                        </option>
                    </select>
                </div>
                <br>
                <div class="panel-group" ng-model="searchConditionActivePanel" data-allow-multiple="true" role="tablist"
                     aria-multiselectable="true" bs-collapse>
                    <div class="panel panel-default">
                        <div class="panel-heading" role="tab">
                            <h4 class="panel-title">
                                <a bs-collapse-toggle href="#" ng-click="searchConditionPanel.collapsed = !searchConditionPanel.collapsed">
                    <span class="glyphicon"
                          ng-class="{'glyphicon-chevron-right': searchConditionPanel.collapsed, 'glyphicon-chevron-down': !searchConditionPanel.collapsed}"></span>
                                    Search Conditions:
                                </a>
                            </h4>
                        </div>
                        <div class="panel-collapse" role="tabpanel" bs-collapse-target>
                            <div class="panel-body">

                                <div id="searchConditions">

                                    <div ng-repeat="condition in conditions">

                                        <label for="searchText_{{$index}}"> Search For: </label>
                                        <input type="text" ng-model="condition.value" id="searchText_{{$index}}"/>
                                        <select name="searchScope" id="searchScope_{{$index}}" ng-model="condition.searchScope">
                                            <option ng-repeat="option in searchScopes" value="{{option.id}}" ng-selected="{{option.id == condition.searchScope}}">
                                                {{option.name}}
                                            </option>
                                        </select>

                                        <label for="docType _{{$index}}"> In Doc Type: </label>
                                        <select name="docType _{{$index}}" id="docType _{{$index}}" ng-model="condition.inDocumentType">
                                            <option ng-repeat="option in inDoctypes" value="{{option.id}}" ng-selected="{{option.id == condition.inDocumentType}}">
                                                {{option.name}}
                                            </option>
                                        </select>

                                        <label for="inField _{{$index}}"> In Field: </label>
                                        <select name="inField _{{$index}}" id="inField _{{$index}}" ng-model="condition.inField" ng-show="condition.inDocumentType == 'bibliographic' ">
                                            <option ng-repeat="option in inFields" value="{{option.id}}" ng-selected="{{option.id == condition.inField}}">
                                                {{option.name}}
                                            </option>
                                        </select>

                                        <select name="inFieldHoldings _{{$index}}" id="inFieldHoldings _{{$index}}" ng-model="condition.inField" ng-show="condition.inDocumentType == 'holdings' ">
                                            <option ng-repeat="option in inFields_Holdings" value="{{option.id}}" ng-selected="{{option.id == condition.inField}}">
                                                {{option.name}}
                                            </option>
                                        </select>

                                        <select name="inFieldEHoldings _{{$index}}" id="inFieldEHoldings _{{$index}}" ng-model="condition.inField" ng-show="condition.inDocumentType == 'eHoldings' ">
                                            <option ng-repeat="option in inFields_EHoldings" value="{{option.id}}" ng-selected="{{option.id == condition.inField}}">
                                                {{option.name}}
                                            </option>
                                        </select>

                                        <select name="inFieldItem _{{$index}}" id="inFieldItem _{{$index}}" ng-model="condition.inField" ng-show="condition.inDocumentType == 'item' ">
                                            <option ng-repeat="option in inFields_Item" value="{{option.id}}" ng-selected="{{option.id == condition.inField}}">
                                                {{option.name}}
                                            </option>
                                        </select>

                                        <select name="operator _{{$index}}" ng-model="condition.operator">
                                            <option ng-repeat="option in operators" value="{{option.id}}" ng-selected="{{option.id == condition.operator}}">
                                                {{option.name}}
                                            </option>
                                        </select>
                                        <button ng-click="addCondition(condition)">add</button>
                                        <button ng-click="removeCondition(condition)" aria-label="Remove">Remove</button>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div align="center">
                        <button class="btn btn-primary btn-sm" type="submit" ng-click="search()"style="margin-top: 20px;">Search</button>
                        <button class="btn btn-primary btn-sm" ng-click="clear()" style="margin-top: 20px;">Clear</button>
                        <button class="btn btn-primary btn-sm" ng-click="cancel()" style="margin-top: 20px;">Cancel</button>
                    </div>
                    <br/>
                    <div class="panel-group" ng-model="searchResultActivePanel" data-allow-multiple="true" role="tablist"
                         aria-multiselectable="true" bs-collapse>
                        <div class="panel panel-default" ng-show="searched">
                            <div class="panel-heading" role="tab">
                                <h4 class="panel-title">
                                    <a bs-collapse-toggle href="#" ng-click="searchResultPanel.collapsed = !searchResultPanel.collapsed">
                    <span class="glyphicon"
                          ng-class="{'glyphicon-chevron-right': searchResultPanel.collapsed, 'glyphicon-chevron-down': !searchResultPanel.collapsed}"></span>
                                        Search Results:
                                    </a>
                                </h4>
                            </div>
                            <div class="panel-collapse" role="tabpanel" bs-collapse-target>
                                <div class="panel-body">

                                    <div>
                                        <div id="searchResultsSection" class="bs-component" style="overflow-y:auto;overflow-x:auto;">

                                            <table class="table table-striped table-hover">
                                                <thead>
                                                </thead>
                                                <div align="left" class="dataTables_length">
                                                    <label for="showEntry">Show</label>
                                                    <select name="showEntry" id="showEntry" ng-model="showEntry" ng-change="searchOnChange(showEntry)">
                                                        <option ng-selected="{{option.id == showEntry}}"
                                                                ng-repeat="option in showEntires" value="{{option.id}}">
                                                            {{option.name}}
                                                        </option>
                                                    </select>
                                                    <label for="showEntry">entries</label>
                                                </div>
                                                <div class="text-right">
                                                    <dir-pagination-controls max-size="8"
                                                                             direction-links="true"
                                                                             boundary-links="true"
                                                                             on-page-change="nextSearchSolr(newPageNumber)">
                                                    </dir-pagination-controls>
                                                </div>
                                                <tr ng-show="displayFields.documentType == 'bibliographic' " >
                                                    <th></th>

                                                    <th ng-show="displayFields.title">Title</th>
                                                    <th ng-show="displayFields.author">Author</th>
                                                    <th ng-show="displayFields.localId">Local Identifier</th>
                                                    <th ng-show="displayFields.journalTitle">Journal Title</th>
                                                    <th ng-show="displayFields.publisher">Publisher</th>
                                                    <th ng-show="displayFields.isbn">ISBN</th>
                                                    <th ng-show="displayFields.issn">ISSN</th>
                                                    <th ng-show="displayFields.subject">Subject</th>
                                                    <th ng-show="displayFields.publicationPlace">Publication Place</th>
                                                    <th ng-show="displayFields.format">Format</th>
                                                    <th ng-show="displayFields.resourceType">Resource Type</th>
                                                    <th ng-show="displayFields.carrier">Carrier</th>
                                                    <th ng-show="displayFields.formGenre">Form Genre</th>
                                                    <th ng-show="displayFields.language">Language</th>
                                                    <th ng-show="displayFields.description">Description</th>
                                                    <th ng-show="displayFields.publicationDate">Pub Date</th>
                                                </tr>
                                                <tr ng-show="displayFields.documentType == 'holdings' ">
                                                    <th ng-show="displayFields.title">Title</th>
                                                    <th ng-show="displayFields.localId">Local Identifier</th>
                                                    <th ng-show="displayFields.location">Location</th>
                                                    <th ng-show="displayFields.callNumber">Call Number</th>
                                                    <th ng-show="displayFields.callNumberPrefix">Call Number Prefix</th>
                                                    <th ng-show="displayFields.classificationPart">Classification Part</th>
                                                    <th ng-show="displayFields.shelvingOrder">Shelving Order</th>
                                                    <th ng-show="displayFields.shelvingOrderCode">Shelving Order Code</th>
                                                    <th ng-show="displayFields.shelvingSchemeCode">Shelving Scheme Code</th>
                                                    <th ng-show="displayFields.shelvingSchemeValue">Shelving Scheme Value</th>
                                                    <th ng-show="displayFields.uri">Uri</th>
                                                    <th ng-show="displayFields.receiptStatus">ReceiptStatus</th>
                                                    <th ng-show="displayFields.copyNumber">CopyNumber</th>
                                                    <th ng-show="displayFields.locationLevel">Location Level</th>
                                                    <th ng-show="displayFields.locationLevelName">Location Level Name</th>
                                                    <th ng-show="displayFields.holdingsNote">Holdings Note</th>
                                                    <th ng-show="displayFields.extentOfOwnershipNoteType">ExtentOfOwnership Note Type</th>
                                                    <th ng-show="displayFields.extentOfOwnershipNoteValue">ExtentOfOwnership Note Value</th>
                                                    <th ng-show="displayFields.extentOfOwnershipType">ExtentOfOwnership Type</th>
                                                </tr>
                                                <tr ng-show="displayFields.documentType == 'eHoldings' ">
                                                    <th ng-show="displayFields.title">Title</th>
                                                    <th ng-show="displayFields.localId">Local Identifier</th>
                                                    <th ng-show="displayFields.accessUserName">Access UserName</th>
                                                    <th ng-show="displayFields.accessPassword">Access Password</th>
                                                    <th ng-show="displayFields.accessLocation">Access Location</th>
                                                    <th ng-show="displayFields.accessStatus">Access Status</th>
                                                    <th ng-show="displayFields.adminPassword">Admin Password</th>
                                                    <th ng-show="displayFields.adminUrl">Admin Url</th>
                                                    <th ng-show="displayFields.adminUserName">Admin UserName</th>
                                                    <th ng-show="displayFields.authentication">Authentication</th>
                                                    <th ng-show="displayFields.callNumber">Call Number</th>
                                                    <th ng-show="displayFields.callNumberPrefix">Call Number Prefix</th>
                                                    <th ng-show="displayFields.classificationPart">Classification Part</th>
                                                    <th ng-show="displayFields.coverageDate">Coverage Date</th>
                                                    <th ng-show="displayFields.donorCode">Donor Code</th>
                                                    <th ng-show="displayFields.donorNote">Donor Note</th>
                                                    <th ng-show="displayFields.donorPublic">Donor Public</th>
                                                    <th ng-show="displayFields.publisher">Publisher</th>
                                                    <th ng-show="displayFields.holdingsNote">Holdings Note</th>
                                                    <th ng-show="displayFields.ill">ILL</th>
                                                    <th ng-show="displayFields.imprint">Imprint</th>
                                                    <th ng-show="displayFields.itemPart">Item Part</th>
                                                    <th ng-show="displayFields.linkText">Link Text</th>
                                                    <th ng-show="displayFields.location">Location</th>
                                                    <th ng-show="displayFields.locationLevel">Location Level</th>
                                                    <th ng-show="displayFields.locationLevelName">Location Level Name</th>
                                                    <th ng-show="displayFields.numberOfSimultaneousUses">Number Of Simultaneous Uses</th>
                                                    <th ng-show="displayFields.perpetualAccess">Extent Of OwnerShip</th>
                                                    <th ng-show="displayFields.persistLink">Persist Link</th>
                                                    <th ng-show="displayFields.platform">PlatForm</th>
                                                    <th ng-show="displayFields.proxied">Proxied</th>
                                                    <th ng-show="displayFields.publicNote">Donor Info</th>
                                                    <th ng-show="displayFields.receiptStatus">ReceiptStatus</th>
                                                    <th ng-show="displayFields.shelvingOrderCode">Shelving Order Code</th>
                                                    <th ng-show="displayFields.shelvingSchemeCode">Shelving Scheme Code</th>
                                                    <th ng-show="displayFields.shelvingSchemeValue">Shelving Scheme Value</th>
                                                    <th ng-show="displayFields.subscription">Subscription</th>
                                                    <th ng-show="displayFields.url">Url</th>
                                                    <th ng-show="displayFields.statisticalCode">Statistical Code</th>

                                                </tr>
                                                <tr ng-show="displayFields.documentType == 'item' ">
                                                    <th ng-show="displayFields.title">Title</th>
                                                    <th ng-show="displayFields.localId">Local Identifier</th>
                                                    <th ng-show="displayFields.location">Location</th>
                                                    <th ng-show="displayFields.callNumber">Call Number</th>
                                                    <th ng-show="displayFields.holdingsLocation">Holdings Location</th>
                                                    <th ng-show="displayFields.holdingsCallNumber">Holdings CallNumber</th>
                                                    <th ng-show="displayFields.barcode">Barcode</th>
                                                    <th ng-show="displayFields.barcodeArsl">BarcodeArsl></th>
                                                    <th ng-show="displayFields.callNumberPrefix">Call Number Prefix</th>
                                                    <th ng-show="displayFields.classificationPart">Classification Part</th>
                                                    <th ng-show="displayFields.shelvingOrder">Shelving Order</th>
                                                    <th ng-show="displayFields.shelvingOrderCode">Shelving Order Code</th>
                                                    <th ng-show="displayFields.shelvingSchemeCode">Shelving Scheme Code</th>
                                                    <th ng-show="displayFields.shelvingSchemeValue">Shelving Scheme Value</th>
                                                    <th ng-show="displayFields.itemPart">Item Part</th>
                                                    <th ng-show="displayFields.itemStatus">ItemStatus</th>
                                                    <th ng-show="displayFields.uri">Uri</th>
                                                    <th ng-show="displayFields.copyNumber">CopyNumber</th>
                                                    <th ng-show="displayFields.copyNumberLabel">CopyNumber Label</th>
                                                    <th ng-show="displayFields.volumeNumber">VolumeNumber</th>
                                                    <th ng-show="displayFields.volumeNumberLabel">VolumeNumber Label</th>
                                                    <th ng-show="displayFields.enumeration">Enumeration</th>
                                                    <th ng-show="displayFields.chronology">Chronology</th>
                                                    <th ng-show="displayFields.itemIdentifier">ItemIdentifier</th>
                                                    <th ng-show="displayFields.itemTypeCodeValue">Item Type Code Value</th>
                                                    <th ng-show="displayFields.itemTypeFullValue">Item Type Full Value</th>
                                                    <th ng-show="displayFields.donorCode">Donor Code</th>
                                                    <th ng-show="displayFields.itemType">Item Type</th>
                                                    <th ng-show="displayFields.dueDateTime">Due Date Time</th>
                                                </tr>
                                                <tr ng-show="displayFields.documentType == 'bibliographic' " dir-paginate="x in searchResults|itemsPerPage:itemsPerPage" total-items="total_count">
                                                    <td><input type="checkbox" ng-model="x.selected"></td>

                                                    <td ng-show="displayFields.title"><a target="_blank" href="ole-kr-krad/editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType={{x.DocType}}&amp;docFormat={{x.DocFormat}}&amp;docId={{x.LocalId_search}}&amp;bibId={{x.bibIdentifier[0]}}&amp;editable=true&amp;fromSearch=true">{{ x.Title_display[0] }}</a></td>
                                                    <td ng-show="displayFields.author">{{x.Author_display[0]}}</td>
                                                    <td ng-show="displayFields.localId">{{x.LocalId_display}}</td>
                                                    <td ng-show="displayFields.journalTitle">{{x.JournalTitle_display}}</td>
                                                    <td ng-show="displayFields.publisher">{{x.Publisher_display[0]}}</td>
                                                    <td ng-show="displayFields.isbn">{{x.ISBN_display[0]}}</td>
                                                    <td ng-show="displayFields.issn">{{x.ISSN_display[0]}}</td>
                                                    <td ng-show="displayFields.subject">{{x.Subject_display[0]}}</td>
                                                    <td ng-show="displayFields.publicationPlace">{{x.PublicationPlace_display[0]}}</td>
                                                    <td ng-show="displayFields.isFormat">{{x.Format_display[0]}}</td>
                                                    <td ng-show="displayFields.resourceType">{{x.ResourceType_display[0]}}</td>
                                                    <td ng-show="displayFields.carrier">{{x.Carrier_display[0]}}</td>
                                                    <td ng-show="displayFields.formGenre">{{x.FormGenre_display[0]}}</td>
                                                    <td ng-show="displayFields.language">{{x.Language_display[0]}}</td>
                                                    <td ng-show="displayFields.description">{{x.Description_display}}</td>
                                                    <td ng-show="displayFields.publicationDate">{{x.PublicationDate_display}}</td>
                                                </tr>
                                                <tr ng-show="displayFields.documentType == 'holdings' " dir-paginate="x in searchResults|itemsPerPage:itemsPerPage" total-items="total_count">
                                                    <td><input type="checkbox" ng-model="x.selected"></td>
                                                    <td ng-show="displayFields.title"><a target="_blank" href="ole-kr-krad/editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType={{x.DocType}}&amp;docFormat={{x.DocFormat}}&amp;docId={{x.LocalId_search}}&amp;bibId={{x.bibIdentifier[0]}}&amp;editable=true&amp;fromSearch=true">{{ x.Title_display[0] }}</a></td>
                                                    <td ng-show="displayFields.localId">{{x.LocalId_display}}</td>
                                                    <td ng-show="displayFields.location">{{x.Location_display[0]}}</td>
                                                    <td ng-show="displayFields.callNumber">{{x.CallNumber_display[0]}}</td>
                                                    <td ng-show="displayFields.callNumberPrefix">{{x.CallNumberPrefix_display[0]}}</td>
                                                    <td ng-show="displayFields.classificationPart">{{x.ClassificationPart_display}}</td>
                                                    <td ng-show="displayFields.shelvingOrder">{{x.ShelvingOrder_display[0]}}</td>
                                                    <td ng-show="displayFields.shelvingOrderCode">{{x.ShelvingOrderCode_display[0]}}</td>
                                                    <td ng-show="displayFields.shelvingSchemeCode">{{x.ShelvingSchemeCode_display[0]}}</td>
                                                    <td ng-show="displayFields.shelvingSchemeValue">{{x.ShelvingSchemeValue_display[0]}}</td>
                                                    <td ng-show="displayFields.uri">{{x.Uri_display[0]}}</td>
                                                    <td ng-show="displayFields.receiptStatus">{{x.ReceiptStatus_display}}</td>
                                                    <td ng-show="displayFields.copyNumber">{{x.CopyNumber_display}}</td>
                                                    <td ng-show="displayFields.locationLevel">{{x.Location_display}}</td>
                                                    <td ng-show="displayFields.locationLevelName">{{x.LocationLevelName_display[0]}}</td>
                                                    <td ng-show="displayFields.holdingsNote">{{x.HoldingsNote_display[0]}}</td>
                                                    <td ng-show="displayFields.extentOfOwnershipNoteType">{{x.ExtentOfOwnership_Note_Type_display[0]}}</td>
                                                    <td ng-show="displayFields.extentOfOwnershipNoteValue">{{x.ExtentOfOwnership_Note_Value_display[0]}}</td>
                                                    <td ng-show="displayFields.extentOfOwnershipType">{{x.ExtentOfOwnership_Type_display[0]}}</td>
                                                </tr>
                                                <tr ng-show="displayFields.documentType == 'eHoldings' " dir-paginate="x in searchResults|itemsPerPage:itemsPerPage" total-items="total_count">
                                                    <td><input type="checkbox" ng-model="x.selected"></td>
                                                    <td ng-show="displayFields.title"><a target="_blank" href="ole-kr-krad/editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType={{x.DocType}}&amp;docFormat={{x.DocFormat}}&amp;docId={{x.LocalId_search}}&amp;bibId={{x.bibIdentifier[0]}}&amp;editable=true&amp;fromSearch=true">{{ x.Title_display[0] }}</a></td>
                                                    <td ng-show="displayFields.localId">{{x.LocalId_display}}</td>
                                                    <td ng-show="displayFields.accessUserName">{{x.Access_UserName_display[0]}}</td>
                                                    <td ng-show="displayFields.accessPassword">{{x.Access_Password_display[0]}}</td>
                                                    <td ng-show="displayFields.accessLocation">{{x.AccessLocation_display}}</td>
                                                    <td ng-show="displayFields.accessStatus">{{x.AccessStatus_display[0]}}</td>
                                                    <td ng-show="displayFields.adminPassword">{{x.Admin_Password_display[0]}}</td>
                                                    <td ng-show="displayFields.adminUrl">{{x.Admin_url_display[0]}}</td>
                                                    <td ng-show="displayFields.adminUserName">{{x.Access_UserName_display[0]}}</td>
                                                    <td ng-show="displayFields.authentication">{{x.Authentication_display[0]}}</td>
                                                    <td ng-show="displayFields.callNumber">{{x.CallNumber_display[0]}}</td>
                                                    <td ng-show="displayFields.callNumberPrefix">{{x.CallNumberPrefix_display[0]}}</td>
                                                    <td ng-show="displayFields.classificationPart">{{x.ClassificationPart_display}}</td>
                                                    <td ng-show="displayFields.coverageDate">{{x.CoverageDate_display}}</td>
                                                    <td ng-show="displayFields.donorCode">{{x.DonorCode_display}}</td>
                                                    <td ng-show="displayFields.donorNote">{{x.DonorNote_display}}</td>
                                                    <td ng-show="displayFields.donorPublic">{{x.DonorPublic_display}}</td>
                                                    <td ng-show="displayFields.publisher">{{x.E_Publisher_display[0]}}</td>
                                                    <td ng-show="displayFields.holdingsNote">{{x.HoldingsNote_display}}</td>
                                                    <td ng-show="displayFields.ill">{{x.ILL_display}}</td>
                                                    <td ng-show="displayFields.imprint">{{x.Imprint_display[0]}}</td>
                                                    <td ng-show="displayFields.itemPart">{{x.ItemPart_display}}</td>
                                                    <td ng-show="displayFields.linkText">{{x.Link_Text_display}}</td>
                                                    <td ng-show="displayFields.location">{{x.Location_display[0]}}</td>
                                                    <td ng-show="displayFields.locationLevel">{{x.Location_display}}</td>
                                                    <td ng-show="displayFields.locationLevelName">{{x.}}</td>
                                                    <td ng-show="displayFields.numberOfSimultaneousUses">{{x.NumberOfSimultaneousUses_display}}</td>
                                                    <td ng-show="displayFields.perpetualAccess">{{x.PerpetualAccess_display}}</td>
                                                    <td ng-show="displayFields.persistLink">{{x.Persist_Link_display[0]}}</td>
                                                    <td ng-show="displayFields.platform">{{x.Platform_display[0]}}</td>
                                                    <td ng-show="displayFields.proxied">{{x.Proxied_display[0]}}</td>
                                                    <td ng-show="displayFields.publicNote">{{x.Public_Note_display}}</td>
                                                    <td ng-show="displayFields.receiptStatus">{{x.ReceiptStatus_display}}</td>
                                                    <td ng-show="displayFields.shelvingOrderCode">{{ShelvingOrderCode_display[0]}}</td>
                                                    <td ng-show="displayFields.shelvingSchemeCode">{{x.ShelvingSchemeCode_display[0]}}</td>
                                                    <td ng-show="displayFields.shelvingSchemeValue">{{x.ShelvingSchemeValue_display[0]}}</td>
                                                    <td ng-show="displayFields.subscription">{{x.Subscription_display[0]}}</td>
                                                    <td ng-show="displayFields.url">{{x.Url_display}}</td>
                                                    <td ng-show="displayFields.statisticalCode">{{x.StatisticalSearchingCodeValue_display}}</td>

                                                </tr>
                                                <tr ng-show="displayFields.documentType == 'item' " dir-paginate="x in searchResults|itemsPerPage:itemsPerPage" total-items="total_count">
                                                    <td><input type="checkbox" ng-model="x.selected"></td>
                                                    <td ng-show="displayFields.title"><a target="_blank" href="ole-kr-krad/editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType={{x.DocType}}&amp;docFormat={{x.DocFormat}}&amp;docId={{x.LocalId_search}}&amp;bibId={{x.bibIdentifier[0]}}&amp;instanceId={{x.holdingsIdentifier[0]}}&amp;editable=true&amp;fromSearch=true">{{ x.Title_display[0] }}</a></td>
                                                    <td ng-show="displayFields.localId">{{x.LocalId_display}}</td>
                                                    <td ng-show="displayFields.location">{{x.Location_display[0]}}</td>
                                                    <td ng-show="displayFields.callNumber">{{x.CallNumber_display[0]}}</td>
                                                    <td ng-show="displayFields.holdingsLocation">{{x.HoldingsLocation_display[0]}}</td>
                                                    <td ng-show="displayFields.holdingsCallNumber">{{x.HoldingsCallNumber_display[0]}}</td>
                                                    <td ng-show="displayFields.barcode">{{x.ItemBarcode_display[0]}}</td>
                                                    <td ng-show="displayFields.barcodeArsl">{{x.BarcodeARSL_display}}></td>
                                                    <td ng-show="displayFields.callNumberPrefix">{{x.CallNumberPrefix_display[0]}}</td>
                                                    <td ng-show="displayFields.classificationPart">{{x.ClassificationPart_display}}</td>
                                                    <td ng-show="displayFields.shelvingOrder">{{x.ShelvingOrder_display[0]}}</td>
                                                    <td ng-show="displayFields.shelvingOrderCode">{{x.ShelvingOrderCode_display[0]}}</td>
                                                    <td ng-show="displayFields.shelvingSchemeCode">{{x.ShelvingSchemeCode_display[0]}}</td>
                                                    <td ng-show="displayFields.shelvingSchemeValue">{{x.ShelvingSchemeValue_display[0]}}</td>
                                                    <td ng-show="displayFields.itemPart">{{x.ItemPart_display}}</td>
                                                    <td ng-show="displayFields.itemStatus">{{x.ItemStatus_display[0]}}</td>
                                                    <td ng-show="displayFields.uri">{{x.ItemUri_display[0]}}</td>
                                                    <td ng-show="displayFields.copyNumber">{{x.CopyNumber_display}}</td>
                                                    <td ng-show="displayFields.copyNumberLabel">{{x.CopyNumberLabel_display}}</td>
                                                    <td ng-show="displayFields.volumeNumber">{{x.VolumeNumber_display}}</td>
                                                    <td ng-show="displayFields.volumeNumberLabel">{{x.VolumeNumberLabel_display}}</td>
                                                    <td ng-show="displayFields.enumeration">{{x.Enumeration_display}}</td>
                                                    <td ng-show="displayFields.chronology">{{x.Chronology_display}}</td>
                                                    <td ng-show="displayFields.itemIdentifier">{{x.ItemIdentifier_display[0]}}</td>
                                                    <td ng-show="displayFields.itemTypeCodeValue">{{x.ItemTypeCodeValue_display[0]}}</td>
                                                    <td ng-show="displayFields.itemTypeFullValue">{{x.ItemTypeFullValue_display[0]}}</td>
                                                    <td ng-show="displayFields.donorCode">{{x.DonorCode_display}}</td>
                                                    <td ng-show="displayFields.itemType">{{x.ItemType_display}}</td>
                                                    <td ng-show="displayFields.dueDateTime">{{x.dueDateTime_display}}</td>

                                                </tr>
                                                <tfoot>
                                                </tfoot>
                                            </table>

                                            <div class="dataTables_info"> {{showEntryInfo}} </div>

                                            <div class="text-right">
                                                <dir-pagination-controls max-size="8"
                                                                         direction-links="true"
                                                                         boundary-links="true"
                                                                         on-page-change="nextSearchSolr(newPageNumber)">
                                                </dir-pagination-controls>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <br/>

            </div>
        </div>
    </form>
</div>
</body>
</html>
