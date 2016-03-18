<%--
  Created by IntelliJ IDEA.
  User: srirams
  Date: 22/2/16
  Time: 2:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="documentSearchConfig" class="org.kuali.ole.docstore.common.document.config.DocumentSearchConfig" scope="session"/>
<jsp:useBean id="searchResultDisplayFields" class="org.kuali.ole.describe.bo.SearchResultDisplayFields" scope="session"/>
<html>
<head>
    <title></title>
</head>
<body>

<c:set var="docType" value="{{documentType}}"/>

<% documentSearchConfig = documentSearchConfig.getDocumentSearchConfig().reloadDocumentConfig();
    System.out.println(pageContext.getAttribute("type"));
searchResultDisplayFields.buildSearchResultDisplayFields(documentSearchConfig.getDocTypeConfigs(),"");
//System.out.print(searchResultDisplayFields.toString());

%>
<div>
    <div id="searchResultsSection" class="bs-component">
        {{documentType}}
        <c:out value="{{documentType}}"/>
        <c:out value="${docType}"/>
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
            <tr>
                <th></th>
                <th ng-show="${searchResultDisplayFields.isTitle()}">Title</th>
                <th ng-show="${searchResultDisplayFields.isAuthor()}">Author</th>
                <th ng-show="${searchResultDisplayFields.isLocalId()}">Local Identifier</th>
                <th ng-show="${searchResultDisplayFields.isJournalTitle()}">Journal Title</th>
                <th ng-show="${searchResultDisplayFields.isPublisher()}">Publisher</th>
                <th ng-show="${searchResultDisplayFields.isIsbn()}">ISBN</th>
                <th ng-show="${searchResultDisplayFields.isIssn()}">ISSN</th>
                <th ng-show="${searchResultDisplayFields.isSubject()}">Subject</th>
                <th ng-show="${searchResultDisplayFields.isPublicationPlace()}">Publication Place</th>
                <th ng-show="${searchResultDisplayFields.isFormat()}">Format</th>
                <th ng-show="${searchResultDisplayFields.isResourceType()}">Resource Type</th>
                <th ng-show="${searchResultDisplayFields.isCarrier()}">Carrier</th>
                <th ng-show="${searchResultDisplayFields.isFormGenre()}">Form Genre</th>
                <th ng-show="${searchResultDisplayFields.isLanguage()}">Language</th>
                <th ng-show="${searchResultDisplayFields.isDescription()}">Description</th>
                <th ng-show="${searchResultDisplayFields.isPublicationDate()}">Pub Date</th>
                <th ng-show="${searchResultDisplayFields.isBarcode()}">Bar Code</th>
            </tr>
            <tr dir-paginate="x in searchResults|itemsPerPage:itemsPerPage" total-items="total_count">
                <td><input type="checkbox" ng-model="x.selected"></td>
                <td ng-show="${searchResultDisplayFields.isTitle()}"><a target="_blank" href="ole-kr-krad/editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType={{x.DocType}}&amp;docFormat={{x.DocFormat}}&amp;docId={{x.LocalId_search}}&amp;editable=true&amp;fromSearch=true">{{ x.Title_display[0] }}</a></td>
                <td ng-show="${searchResultDisplayFields.isAuthor()}">{{x.Author_display[0]}}</td>
                <td ng-show="${searchResultDisplayFields.isLocalId()}">{{x.LocalId_display}}</td>
                <td ng-show="${searchResultDisplayFields.isJournalTitle()}">{{x.JournalTitle_display}}</td>
                <td ng-show="${searchResultDisplayFields.isPublisher()}">{{x.Publisher_display[0]}}</td>
                <td ng-show="${searchResultDisplayFields.isIsbn()}">{{x.ISBN_display[0]}}</td>
                <td ng-show="${searchResultDisplayFields.isIssn()}">{{x.ISSN_display[0]}}</td>
                <td ng-show="${searchResultDisplayFields.isSubject()}">{{x.Subject_display[0]}}</td>
                <td ng-show="${searchResultDisplayFields.isPublicationPlace()}">{{x.PublicationPlace_display[0]}}</td>
                <td ng-show="${searchResultDisplayFields.isFormat()}">{{x.Format_display[0]}}</td>
                <td ng-show="${searchResultDisplayFields.isResourceType()}">{{x.ResourceType_display[0]}}</td>
                <td ng-show="${searchResultDisplayFields.isCarrier()}">{{x.Carrier_display[0]}}</td>
                <td ng-show="${searchResultDisplayFields.isFormGenre()}">{{x.FormGenre_display[0]}}</td>
                <td ng-show="${searchResultDisplayFields.isLanguage()}">{{x.Language_display[0]}}</td>
                <td ng-show="${searchResultDisplayFields.isDescription()}">{{x.Description_display[0]}}</td>
                <td ng-show="${searchResultDisplayFields.isPublicationDate()}">{{x.PublicationDate_display[0]}}</td>
                <td ng-show="${searchResultDisplayFields.isBarcode()}">{{x.Barcode_display[0]}}</td>
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



</body>
</html>
