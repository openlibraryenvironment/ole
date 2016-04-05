<#macro ole_bib_search items manager container>

    <#if manager.totalLines gt 0>
        <@search_pagesize manager />
        <@search_pager_top manager container/>
    <table class="table table-condensed table-bordered uif-tableCollectionLayout dataTable">
        <thead>
        <tr>
            <th></th>
            <#if manager.searchResultDisplayFields.title><th><a style="text-decoration:underline;" onclick="bibSortBy('title');">Title</a></th></#if>
            <#if manager.searchResultDisplayFields.localId><th><a style="text-decoration:underline;" onclick="bibSortBy('local');">Local Identifier</a></th></th></#if>
            <#if manager.searchResultDisplayFields.journalTitle><th><a style="text-decoration:underline;" onclick="bibSortBy('journal');">Journal Title</a></th></#if>
            <#if manager.searchResultDisplayFields.author><th><a style="text-decoration:underline;" onclick="bibSortBy('author');">Author</a></th></#if>
            <#if manager.searchResultDisplayFields.publisher><th><a style="text-decoration:underline;" onclick="bibSortBy('publisher');">Publisher</a></th></#if>
            <#if manager.searchResultDisplayFields.isbn><th>ISBN</th></#if>
            <#if manager.searchResultDisplayFields.issn><th>ISSN</th></#if>
            <#if manager.searchResultDisplayFields.subject><th>Subject</th></#if>
            <#if manager.searchResultDisplayFields.publicationPlace><th>Publication Place</th></#if>
            <#if manager.searchResultDisplayFields.format><th>Format</th></#if>
            <#if manager.searchResultDisplayFields.resourceType><th>Resource Type</th></#if>
            <#if manager.searchResultDisplayFields.carrier><th>Carrier</th></#if>
            <#if manager.searchResultDisplayFields.formGenre><th>Form Genre</th></#if>
            <#if manager.searchResultDisplayFields.language><th>Language</th></#if>
            <#if manager.searchResultDisplayFields.description><th>Description</th></#if>
            <#if manager.searchResultDisplayFields.publicationDate><th><a style="text-decoration:underline;" onclick="bibSortBy('publicationDate');">Pub Date</a></th></#if>
            <#if manager.searchResultDisplayFields.barcode><th>Bar Code</th></#if>
        </tr>
        </thead>
        <tbody>
            <#list manager.searchLines as line>
            <tr class="odd">
                <td class="sorting_1"><@search_checkbox_control line 'select' container "" /></td>
                <#if manager.searchResultDisplayFields.title>
                    <td>
                        <#if line.row.title??>
                            <#if line.row.staffOnly?? && line.row.staffOnly == 'true'>
                                <div class="staffOnlyForHiperlink">
                                    <a target="_blank" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=bibliographic&amp;docFormat=${line.row.docFormat}&amp;docId=${line.row.localId}&amp;bibId=${line.row.bibIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.title!?html}</a>
                                </div>
                            <#else>
                                <div>
                                    <a target="_blank" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=bibliographic&amp;docFormat=${line.row.docFormat}&amp;docId=${line.row.localId}&amp;bibId=${line.row.bibIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.title!?html}</a>
                                </div>
                            </#if>
                        <#else>
                            <div class="staffOnlyForHiperlink">
                                <a target="_blank" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=bibliographic&amp;docFormat=${line.row.docFormat}&amp;docId=${line.row.localId}&amp;bibId=${line.row.bibIdentifier!}&amp;editable=true&amp;fromSearch=true"></a>
                            </div>
                        </#if>
                    </td>
                </#if>
                <#if manager.searchResultDisplayFields.localId><td>${line.row.localId!}</td></#if>
                <#if manager.searchResultDisplayFields.journalTitle><td>${line.row.journalTitle!}</td></#if>
                <#if manager.searchResultDisplayFields.author><td>${line.row.author!}</td></#if>
                <#if manager.searchResultDisplayFields.publisher><td>${line.row.publisher!}</td></#if>
                <#if manager.searchResultDisplayFields.isbn><td>${line.row.isbn!}</td></#if>
                <#if manager.searchResultDisplayFields.issn><td>${line.row.issn!}</td></#if>
                <#if manager.searchResultDisplayFields.subject><td>${line.row.subject!}</td></#if>
                <#if manager.searchResultDisplayFields.publicationPlace><td>${line.row.publicationPlace!}</td></#if>
                <#if manager.searchResultDisplayFields.format><td>${line.row.format!}</td></#if>
                <#if manager.searchResultDisplayFields.resourceType><td>${line.row.resourceType!}</td></#if>
                <#if manager.searchResultDisplayFields.carrier><td>${line.row.carrier!}</td></#if>
                <#if manager.searchResultDisplayFields.formGenre><td>${line.row.formGenre!}</td></#if>
                <#if manager.searchResultDisplayFields.language><td>${line.row.language!}</td></#if>
                <#if manager.searchResultDisplayFields.description><td>${line.row.description!}</td></#if>
                <#if manager.searchResultDisplayFields.publicationDate><td>${line.row.publicationDate!}</td></#if>
                <#if manager.searchResultDisplayFields.barcode><td>${line.row.barcode!}</td></#if>
            </tr>
            </#list>
        </tbody>
        <tfoot><tr>
            <th></th>
            <#if manager.searchResultDisplayFields.title><th></th></#if>
            <#if manager.searchResultDisplayFields.localId><th></th></#if>
            <#if manager.searchResultDisplayFields.journalTitle><th></th></#if>
            <#if manager.searchResultDisplayFields.author><th></th></#if>
            <#if manager.searchResultDisplayFields.publisher><th></th></#if>
            <#if manager.searchResultDisplayFields.isbn><th></th></#if>
            <#if manager.searchResultDisplayFields.issn><th></th></#if>
            <#if manager.searchResultDisplayFields.subject><th></th></#if>
            <#if manager.searchResultDisplayFields.publicationPlace><th></th></#if>
            <#if manager.searchResultDisplayFields.format><th></th></#if>
            <#if manager.searchResultDisplayFields.resourceType><th></th></#if>
            <#if manager.searchResultDisplayFields.carrier><th></th></#if>
            <#if manager.searchResultDisplayFields.formGenre><th></th></#if>
            <#if manager.searchResultDisplayFields.language><th></th></#if>
            <#if manager.searchResultDisplayFields.description><th></th></#if>
            <#if manager.searchResultDisplayFields.publicationDate><th></th></#if>
            <#if manager.searchResultDisplayFields.barcode><th></th></#if>
        </tr></tfoot>
    </table>
        <@search_pager manager container />
    </#if>

</#macro>

<#macro ole_item_search items manager container>

    <#if manager.totalLines gt 0>
        <@search_pagesize manager />
        <@search_pager_top manager container />
    <table class="table table-condensed table-bordered uif-tableCollectionLayout dataTable">
        <thead>
        <tr>
            <th></th>
            <th>Relations</th>
            <#if manager.searchResultDisplayFields.localId><th><a style="text-decoration:underline;" onclick="itemSortBy('local');">Local Identifier</a></th></#if>
            <#if manager.searchResultDisplayFields.title><th><a style="text-decoration:underline;" onclick="itemSortBy('title');">Title</a></th></#if>
            <#if manager.searchResultDisplayFields.location><th><a style="text-decoration:underline;" onclick="itemSortBy('location');">Item Location</a></th></#if>
            <#if manager.searchResultDisplayFields.callNumber><th><a style="text-decoration:underline;" onclick="itemSortBy('callNumber');">Item Call Number</a></th></#if>
            <#if manager.searchResultDisplayFields.holdingsLocation><th>Holdings Location</th></#if>
            <#if manager.searchResultDisplayFields.holdingsCallNumber><th>Holdings Call Number</th></#if>
            <#if manager.searchResultDisplayFields.barcode><th><a style="text-decoration:underline;" onclick="itemSortBy('barcode');">Barcode</a></th></#if>
            <#if manager.searchResultDisplayFields.barcodeArsl><th>BarcodeARSL</th></#if>
            <#if manager.searchResultDisplayFields.callNumberPrefix><th>Call Number Prefix</th></#if>
            <#if manager.searchResultDisplayFields.classificationPart><th>Classification Part</th></#if>
            <#if manager.searchResultDisplayFields.shelvingOrder><th><a style="text-decoration:underline;" onclick="itemSortBy('shelvingOrder');">Shelving Order</a></th></#if>
            <#if manager.searchResultDisplayFields.shelvingOrderCode><th>Shelving Order Code</th></#if>
            <#if manager.searchResultDisplayFields.shelvingSchemeCode><th>Shelving Scheme Code</th></#if>
            <#if manager.searchResultDisplayFields.shelvingSchemeValue><th>Shelving Scheme Value</th></#if>
            <#if manager.searchResultDisplayFields.itemPart><th>Item Part</th></#if>
            <#if manager.searchResultDisplayFields.itemStatus><th><a style="text-decoration:underline;" onclick="itemSortBy('itemStatus');">ItemStatus</a></th></#if>
            <#if manager.searchResultDisplayFields.uri><th>Uri</th></#if>
            <#if manager.searchResultDisplayFields.copyNumber><th><a style="text-decoration:underline;" onclick="itemSortBy('copyNumber');">CopyNumber</a></th></#if>
            <#if manager.searchResultDisplayFields.copyNumberLabel><th>CopyNumber Label</th></#if>
            <#if manager.searchResultDisplayFields.volumeNumber><th>VolumeNumber</th></#if>
            <#if manager.searchResultDisplayFields.volumeNumberLabel><th>VolumeNumber Label</th></#if>
            <#if manager.searchResultDisplayFields.enumeration><th><a style="text-decoration:underline;" onclick="itemSortBy('enumeration');">Enumeration</a></th></#if>
            <#if manager.searchResultDisplayFields.chronology><th><a style="text-decoration:underline;" onclick="itemSortBy('chronology');">Chronology</a></th></#if>
            <#if manager.searchResultDisplayFields.itemIdentifier><th>Item Identifier</th></#if>
            <#if manager.searchResultDisplayFields.itemTypeCodeValue><th>Item Type Code Value</th></#if>
            <#if manager.searchResultDisplayFields.itemTypeFullValue><th>Item Type Full Value</th></#if>
            <#if manager.searchResultDisplayFields.donorCode><th>Donor Code</th></#if>
            <#if manager.searchResultDisplayFields.itemType><th>Item Type</th></#if>
            <#if manager.searchResultDisplayFields.dueDateTime><th>Due Date Time</th></#if>
        </tr>
        </thead>
        <tbody>
            <#list manager.searchLines as line>
            <tr class="odd">
                <td class="sorting_1"><@search_checkbox_control line 'select' container "" /></td>
                <td><#if line.row.analyticItem?? && line.row.analyticItem == 'true'>
                    <a onclick="viewAnalyticItemRelation('${line.row.itemIdentifier}');">AI</a>
                </#if></td>
                <#if manager.searchResultDisplayFields.localId><td>
                    <#if line.row.localId??>
                        <#if line.row.staffOnly?? && line.row.staffOnly == 'true'>
                            <div class="staffOnlyForHiperlink">
                                <a target="_blank" style="font-weight:bold;border:0px;" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=item&amp;docFormat=oleml&amp;docId=${line.row.localId!}&amp;bibId=${line.row.bibIdentifier!}&amp;instanceId=${line.row.holdingsIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.localId!?html}</a>
                            </div>
                        <#else>
                            <div>
                                <a target="_blank" style="font-weight:bold;border:0px;" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=item&amp;docFormat=oleml&amp;docId=${line.row.localId!}&amp;bibId=${line.row.bibIdentifier!}&amp;instanceId=${line.row.holdingsIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.localId!?html}</a>
                            </div>
                        </#if>
                    </#if>
                </td></#if>
                <#if manager.searchResultDisplayFields.title><td>
                    <#if line.row.title??>
                        <#if line.row.staffOnly?? && line.row.staffOnly == 'true'>
                            <div class="staffOnlyForHiperlink">
                                <a target="_blank" style="font-weight:bold;border:0px;" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=item&amp;docFormat=oleml&amp;docId=${line.row.localId!}&amp;bibId=${line.row.bibIdentifier!}&amp;instanceId=${line.row.holdingsIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.title!?html}</a>
                            </div>
                        <#else>
                            <div>
                                <a target="_blank" style="font-weight:bold;border:0px;" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=item&amp;docFormat=oleml&amp;docId=${line.row.localId!}&amp;bibId=${line.row.bibIdentifier!}&amp;instanceId=${line.row.holdingsIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.title!?html}</a>
                            </div>
                        </#if>
                    </#if>
                </td></#if>
                <#if manager.searchResultDisplayFields.location><td>${line.row.locationName!}</td></#if>
                <#if manager.searchResultDisplayFields.callNumber><td>${line.row.callNumber!}</td></#if>
                <#if manager.searchResultDisplayFields.holdingsLocation><td>${line.row.holdingsLocation!}</td></#if>
                <#if manager.searchResultDisplayFields.holdingsCallNumber><td>${line.row.holdingsCallNumber!}</td></#if>
                <#if manager.searchResultDisplayFields.barcode><td>${line.row.barcode!}</td></#if>
                <#if manager.searchResultDisplayFields.barcodeArsl><td>${line.row.barcodeArsl!}</td></#if>
                <#if manager.searchResultDisplayFields.callNumberPrefix><td>${line.row.callNumberPrefix!}</td></#if>
                <#if manager.searchResultDisplayFields.classificationPart><td>${line.row.classificationPart!}</td></#if>
                <#if manager.searchResultDisplayFields.shelvingOrder><td>${line.row.shelvingOrder!}</td></#if>
                <#if manager.searchResultDisplayFields.shelvingOrderCode><td>${line.row.shelvingOrderCode!}</td></#if>
                <#if manager.searchResultDisplayFields.shelvingSchemeCode><td>${line.row.shelvingSchemeCode!}</td></#if>
                <#if manager.searchResultDisplayFields.shelvingSchemeValue><td>${line.row.shelvingSchemeValue!}</td></#if>
                <#if manager.searchResultDisplayFields.itemPart><td>${line.row.itemPart!}</td></#if>
                <#if manager.searchResultDisplayFields.itemStatus><td>${line.row.itemStatus!}</td></#if>
                <#if manager.searchResultDisplayFields.uri><td>${line.row.uri!}</td></#if>
                <#if manager.searchResultDisplayFields.copyNumber><td>${line.row.copyNumber!}</td></#if>
                <#if manager.searchResultDisplayFields.copyNumberLabel><td>${line.row.copyNumberLabel!}</td></#if>
                <#if manager.searchResultDisplayFields.volumeNumber><td>${line.row.volumeNumber!}</td></#if>
                <#if manager.searchResultDisplayFields.volumeNumberLabel><td>${line.row.volumeNumberLabel!}</td></#if>
                <#if manager.searchResultDisplayFields.enumeration><td>${line.row.enumeration!}</td></#if>
                <#if manager.searchResultDisplayFields.chronology><td>${line.row.chronology!}</td></#if>
                <#if manager.searchResultDisplayFields.itemIdentifier><td>${line.row.itemIdentifier!}</td></#if>
                <#if manager.searchResultDisplayFields.itemTypeCodeValue><td>${line.row.itemTypeCodeValue!}</td></#if>
                <#if manager.searchResultDisplayFields.itemTypeFullValue><td>${line.row.itemTypeFullValue!}</td></#if>
                <#if manager.searchResultDisplayFields.donorCode><td>${line.row.donorCode!}</td></#if>
                <#if manager.searchResultDisplayFields.itemType><td>${line.row.itemTypeFullValue!}</td></#if>
                <#if manager.searchResultDisplayFields.dueDateTime><td>${line.row.dueDateTime!}</td></#if>
            </tr>
            </#list>
        </tbody>
        <tfoot><tr>
            <th></th>
            <th></th>
            <#if manager.searchResultDisplayFields.localId><th></th></#if>
            <#if manager.searchResultDisplayFields.title><th></th></#if>
            <#if manager.searchResultDisplayFields.location><th></th></#if>
            <#if manager.searchResultDisplayFields.callNumber><th></th></#if>
            <#if manager.searchResultDisplayFields.barcode><th></th></#if>
            <#if manager.searchResultDisplayFields.barcodeArsl><th></th></#if>
            <#if manager.searchResultDisplayFields.callNumberPrefix><th></th></#if>
            <#if manager.searchResultDisplayFields.classificationPart><th></th></#if>
            <#if manager.searchResultDisplayFields.shelvingOrder><th></th></#if>
            <#if manager.searchResultDisplayFields.shelvingOrderCode><th></th></#if>
            <#if manager.searchResultDisplayFields.shelvingSchemeCode><th></th></#if>
            <#if manager.searchResultDisplayFields.shelvingSchemeValue><th></th></#if>
            <#if manager.searchResultDisplayFields.itemPart><th></th></#if>
            <#if manager.searchResultDisplayFields.itemStatus><th></th></#if>
            <#if manager.searchResultDisplayFields.uri><th></th></#if>
            <#if manager.searchResultDisplayFields.copyNumber><th></th></#if>
            <#if manager.searchResultDisplayFields.copyNumberLabel><th></th></#if>
            <#if manager.searchResultDisplayFields.volumeNumber><th></th></#if>
            <#if manager.searchResultDisplayFields.volumeNumberLabel><th></th></#if>
            <#if manager.searchResultDisplayFields.enumeration><th></th></#if>
            <#if manager.searchResultDisplayFields.chronology><th></th></#if>
            <#if manager.searchResultDisplayFields.itemIdentifier><th></th></#if>
            <#if manager.searchResultDisplayFields.itemTypeCodeValue><th></th></#if>
            <#if manager.searchResultDisplayFields.itemTypeFullValue><th></th></#if>
            <#if manager.searchResultDisplayFields.donorCode><th></th></#if>
            <#if manager.searchResultDisplayFields.itemType><th></th></#if>
            <#if manager.searchResultDisplayFields.dueDateTime><th></th></#if>
        </tr></tfoot>
    </table>
        <@search_pager manager container />
    </#if>

</#macro>

<#macro ole_holdings_search items manager container>

    <#if manager.totalLines gt 0>
        <@search_pagesize manager />
        <@search_pager_top manager container />
    <table class="table table-condensed table-bordered uif-tableCollectionLayout dataTable">
        <thead>
        <tr>
            <th></th>
            <th>Relations</th>
            <#if manager.searchResultDisplayFields.localId><th><a style="text-decoration:underline;" onclick="holdingsSortBy('local');">Local Identifier</a></th></#if>
            <#if manager.searchResultDisplayFields.title><th><a style="text-decoration:underline;" onclick="holdingsSortBy('title');">Title</a></th></#if>
            <#if manager.searchResultDisplayFields.location><th><a style="text-decoration:underline;" onclick="holdingsSortBy('location');">Location</a></th></#if>
            <#if manager.searchResultDisplayFields.callNumber><th><a style="text-decoration:underline;" onclick="holdingsSortBy('callNumber');">Call Number</a></th></#if>
            <#if manager.searchResultDisplayFields.callNumberPrefix><th>Call Number Prefix</th></#if>
            <#if manager.searchResultDisplayFields.classificationPart><th>Classification Part</th></#if>
            <#if manager.searchResultDisplayFields.shelvingOrder><th><a style="text-decoration:underline;" onclick="itemSortBy('shelvingOrder');">Shelving Order</a></th></#if>
            <#if manager.searchResultDisplayFields.shelvingOrderCode><th>Shelving Order Code</th></#if>
            <#if manager.searchResultDisplayFields.shelvingSchemeCode><th>Shelving Scheme Code</th></#if>
            <#if manager.searchResultDisplayFields.shelvingSchemeValue><th>Shelving Scheme Value</th></#if>
            <#if manager.searchResultDisplayFields.uri><th>Uri</th></#if>
            <#if manager.searchResultDisplayFields.receiptStatus><th>ReceiptStatus</th></#if>
            <#if manager.searchResultDisplayFields.copyNumber><th>CopyNumber</th></#if>
            <#if manager.searchResultDisplayFields.locationLevel><th>Location Level</th></#if>
            <#if manager.searchResultDisplayFields.locationLevelName><th>Location Level Name</th></#if>
            <#if manager.searchResultDisplayFields.holdingsNote><th>Holdings Note</th></#if>
            <#if manager.searchResultDisplayFields.extentOfOwnershipNoteType><th>ExtentOfOwnership Note Type</th></#if>
            <#if manager.searchResultDisplayFields.extentOfOwnershipNoteValue><th>ExtentOfOwnership Note Value</th></#if>
            <#if manager.searchResultDisplayFields.extentOfOwnershipType><th>ExtentOfOwnership Type</th></#if>
        </tr>
        </thead>
        <tbody>
            <#list manager.searchLines as line>
            <tr class="odd">
                <td class="sorting_1"><@search_checkbox_control line 'select' container "" /></td>
                <td><div>
                    <#if line.row.boundWithHolding?? && line.row.boundWithHolding == 'true'>
                        <a onclick="viewBoundWithRelation('${line.row.holdingsIdentifier}');">BW</a>
                    </#if>
                    <#if line.row.seriesHolding?? && line.row.seriesHolding == 'true'>
                        <a onclick="viewSeriesHoldingRelation('${line.row.holdingsIdentifier}');">SH</a>
                    </#if>
                    <#if line.row.analyticItem?? && line.row.analyticItem == 'true'>
                        <a onclick="viewAnalyticItemRelation('${line.row.itemIdentifier}');">AI</a>
                    </#if>
                </div></td>
                <#if manager.searchResultDisplayFields.localId><td>
                    <#if line.row.localId??>
                        <#if line.row.staffOnly?? && line.row.staffOnly == 'true'>
                            <div class="staffOnlyForHiperlink">
                                <a target="_blank" style="font-weight:bold;border:0px;" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=holdings&amp;docFormat=oleml&amp;docId=${line.row.localId!}&amp;bibId=${line.row.bibIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.localId!?html}</a>
                            </div>
                        <#else>
                            <div>
                                <a target="_blank" style="font-weight:bold;border:0px;" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=holdings&amp;docFormat=oleml&amp;docId=${line.row.localId!}&amp;bibId=${line.row.bibIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.localId!?html}</a>
                            </div>
                        </#if>
                    </#if>
                </td></#if>
                <#if manager.searchResultDisplayFields.title><td>
                    <#if line.row.title??>
                        <#if line.row.staffOnly?? && line.row.staffOnly == 'true'>
                            <div class="staffOnlyForHiperlink">
                                <a target="_blank" style="font-weight:bold;border:0px;" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=holdings&amp;docFormat=oleml&amp;docId=${line.row.localId!}&amp;bibId=${line.row.bibIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.title!?html}</a>
                            </div>
                        <#else>
                            <div>
                                <a target="_blank" style="font-weight:bold;border:0px;" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=holdings&amp;docFormat=oleml&amp;docId=${line.row.localId!}&amp;bibId=${line.row.bibIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.title!?html}</a>
                            </div>
                        </#if>
                    </#if>
                </td></#if>
                <#if manager.searchResultDisplayFields.location><td>${line.row.locationName!}</td></#if>
                <#if manager.searchResultDisplayFields.callNumber><td>${line.row.callNumber!}</td></#if>
                <#if manager.searchResultDisplayFields.callNumberPrefix><td>${line.row.callNumberPrefix!}</td></#if>
                <#if manager.searchResultDisplayFields.classificationPart><td>${line.row.classificationPart!}</td></#if>
                <#if manager.searchResultDisplayFields.shelvingOrder><td>${line.row.shelvingOrder!}</td></#if>
                <#if manager.searchResultDisplayFields.shelvingOrderCode><td>${line.row.shelvingOrderCode!}</td></#if>
                <#if manager.searchResultDisplayFields.shelvingSchemeCode><td>${line.row.shelvingSchemeCode!}</td></#if>
                <#if manager.searchResultDisplayFields.shelvingSchemeValue><td>${line.row.shelvingSchemeValue!}</td></#if>
                <#if manager.searchResultDisplayFields.uri><td>${line.row.uri!}</td></#if>
                <#if manager.searchResultDisplayFields.receiptStatus><td>${line.row.receiptStatus!}</td></#if>
                <#if manager.searchResultDisplayFields.copyNumber><td>${line.row.copyNumber!}</td></#if>
                <#if manager.searchResultDisplayFields.locationLevel><td>${line.row.locationLevel!}</td></#if>
                <#if manager.searchResultDisplayFields.locationLevelName><td>${line.row.locationLevelName!}</td></#if>
                <#if manager.searchResultDisplayFields.holdingsNote><td>${line.row.holdingsNote!}</td></#if>
                <#if manager.searchResultDisplayFields.extentOfOwnershipNoteType><td>${line.row.extentOfOwnershipNoteType!}</td></#if>
                <#if manager.searchResultDisplayFields.extentOfOwnershipNoteValue><td>${line.row.extentOfOwnershipNoteValue!}</td></#if>
                <#if manager.searchResultDisplayFields.extentOfOwnershipType><td>${line.row.extentOfOwnershipType!}</td></#if>
            </tr>
            </#list>
        </tbody>
        <tfoot><tr>
            <th></th>
            <th></th>
            <#if manager.searchResultDisplayFields.localId><th></th></#if>
            <#if manager.searchResultDisplayFields.title><th></th></#if>
            <#if manager.searchResultDisplayFields.location><th></th></#if>
            <#if manager.searchResultDisplayFields.callNumber><th></th></#if>
            <#if manager.searchResultDisplayFields.callNumberPrefix><th></th></#if>
            <#if manager.searchResultDisplayFields.classificationPart><th></th></#if>
            <#if manager.searchResultDisplayFields.shelvingOrder><th></th></#if>
            <#if manager.searchResultDisplayFields.shelvingOrderCode><th></th></#if>
            <#if manager.searchResultDisplayFields.shelvingSchemeCode><th></th></#if>
            <#if manager.searchResultDisplayFields.shelvingSchemeValue><th></th></#if>
            <#if manager.searchResultDisplayFields.uri><th></th></#if>
            <#if manager.searchResultDisplayFields.receiptStatus><th></th></#if>
            <#if manager.searchResultDisplayFields.copyNumber><th></th></#if>
            <#if manager.searchResultDisplayFields.locationLevel><th></th></#if>
            <#if manager.searchResultDisplayFields.locationLevelName><th></th></#if>
            <#if manager.searchResultDisplayFields.holdingsNote><th></th></#if>
            <#if manager.searchResultDisplayFields.extentOfOwnershipNoteType><th></th></#if>
            <#if manager.searchResultDisplayFields.extentOfOwnershipNoteValue><th></th></#if>
            <#if manager.searchResultDisplayFields.extentOfOwnershipType><th></th></#if>
        </tr></tfoot>
    </table>
        <@search_pager manager container />
    </#if>

</#macro>

<#macro ole_eholdings_search items manager container>

    <#if manager.totalLines gt 0>
        <@search_pagesize manager />
        <@search_pager_top manager container />
    <table class="table table-condensed table-bordered uif-tableCollectionLayout dataTable">
        <thead>
        <tr>
            <th></th>
            <#if manager.searchResultDisplayFields.localId><th><a style="text-decoration:underline;" onclick="holdingsSortBy('local');">Local Identifier</a></th></#if>
            <#if manager.searchResultDisplayFields.title><th><a style="text-decoration:underline;" onclick="holdingsSortBy('title');">Title</a></th></#if>
            <#if manager.searchResultDisplayFields.accessPassword><th>Access Password</th></#if>
            <#if manager.searchResultDisplayFields.accessUserName><th>Access UserName</th></#if>
            <#if manager.searchResultDisplayFields.accessLocation><th>Access Location</th></#if>
            <#if manager.searchResultDisplayFields.accessStatus><th>Access Status</th></#if>
            <#if manager.searchResultDisplayFields.adminPassword><th>Admin Password</th></#if>
            <#if manager.searchResultDisplayFields.adminUrl><th>Admin Url</th></#if>
            <#if manager.searchResultDisplayFields.adminUserName><th>Admin UserName</th></#if>
            <#if manager.searchResultDisplayFields.authentication><th>Authentication</th></#if>
            <#if manager.searchResultDisplayFields.callNumber><th><a style="text-decoration:underline;" onclick="holdingsSortBy('callNumber');">Call Number</a></th></#if>
            <#if manager.searchResultDisplayFields.callNumberPrefix><th>Call Number Prefix</th></#if>
            <#if manager.searchResultDisplayFields.classificationPart><th>Classification Part</th></#if>
            <#if manager.searchResultDisplayFields.coverageDate><th>Coverage Date</th></#if>
            <#if manager.searchResultDisplayFields.donorCode><th>Donor Code</th></#if>
            <#if manager.searchResultDisplayFields.donorNote><th>Donor Note</th></#if>
            <#if manager.searchResultDisplayFields.donorPublic><th>Donor Public</th></#if>
            <#if manager.searchResultDisplayFields.publisher><th>Publisher</th></#if>
            <#if manager.searchResultDisplayFields.holdingsNote><th>Holdings Note</th></#if>
            <#if manager.searchResultDisplayFields.ill><th>ILL</th></#if>
            <#if manager.searchResultDisplayFields.imprint><th>Imprint</th></#if>
            <#if manager.searchResultDisplayFields.itemPart><th>Item Part</th></#if>
            <#if manager.searchResultDisplayFields.linkText><th>Link Text</th></#if>
            <#if manager.searchResultDisplayFields.location><th><a style="text-decoration:underline;" onclick="holdingsSortBy('location');">Location</a></th></#if>
            <#if manager.searchResultDisplayFields.locationLevel><th>Location Level</th></#if>
            <#if manager.searchResultDisplayFields.locationLevelName><th>Location Level Name</th></#if>
            <#if manager.searchResultDisplayFields.numberOfSimultaneousUses><th>Number Of Simultaneous Uses</th></#if>
            <#if manager.searchResultDisplayFields.perpetualAccess><th>Extent Of OwnerShip</th></#if>
            <#if manager.searchResultDisplayFields.persistLink><th>Persist Link</th></#if>
            <#if manager.searchResultDisplayFields.platform><th>PlatForm</th></#if>
            <#if manager.searchResultDisplayFields.proxied><th>Proxied</th></#if>
            <#if manager.searchResultDisplayFields.publicNote><th>Donor Info</th></#if>
            <#if manager.searchResultDisplayFields.receiptStatus><th>ReceiptStatus</th></#if>
            <#if manager.searchResultDisplayFields.shelvingOrderCode><th>Shelving Order Code</th></#if>
            <#if manager.searchResultDisplayFields.shelvingSchemeCode><th>Call Number Type Code</th></#if>
            <#if manager.searchResultDisplayFields.shelvingSchemeValue><th>Call Number Type Value</th></#if>
            <#if manager.searchResultDisplayFields.subscription><th>Subscription</th></#if>
            <#if manager.searchResultDisplayFields.url><th>Url</th></#if>
            <#if manager.searchResultDisplayFields.statisticalCode><th>Statistical Code</th></#if>
        </tr>
        </thead>
        <tbody>
            <#list manager.searchLines as line>
            <tr class="odd">
                <td class="sorting_1"><@search_checkbox_control line 'select' container "" /></td>
                <#if manager.searchResultDisplayFields.localId><td><div>
                    <#if line.row.localId??>
                        <#if line.row.staffOnly?? && line.row.staffOnly == 'true'>
                            <div class="staffOnlyForHiperlink">
                                <a target="_blank" style="font-weight:bold;border:0px;" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=eHoldings&amp;docFormat=oleml&amp;docId=${line.row.localId!}&amp;bibId=${line.row.bibIdentifier!}&amp;instanceId=${line.row.holdingsIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.localId!?html}</a>
                            </div>
                        <#else>
                            <div>
                                <a target="_blank" style="font-weight:bold;border:0px;" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=eHoldings&amp;docFormat=oleml&amp;docId=${line.row.localId!}&amp;bibId=${line.row.bibIdentifier!}&amp;instanceId=${line.row.holdingsIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.localId!?html}</a>
                            </div>
                        </#if>
                    </#if>
                </div></td></#if>
                <#if manager.searchResultDisplayFields.title><td>
                    <#if line.row.title??>
                        <#if line.row.staffOnly?? && line.row.staffOnly == 'true'>
                            <div class="staffOnlyForHiperlink">
                                <a target="_blank" style="font-weight:bold;border:0px;" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=eHoldings&amp;docFormat=oleml&amp;docId=${line.row.localId!}&amp;bibId=${line.row.bibIdentifier!}&amp;instanceId=${line.row.holdingsIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.title!?html}</a>
                            </div>
                        <#else>
                            <div>
                                <a target="_blank" style="font-weight:bold;border:0px;" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=eHoldings&amp;docFormat=oleml&amp;docId=${line.row.localId!}&amp;bibId=${line.row.bibIdentifier!}&amp;instanceId=${line.row.holdingsIdentifier!}&amp;editable=true&amp;fromSearch=true">${line.row.title!?html}</a>
                            </div>
                        </#if>
                    </#if>
                </td></#if>
                <#if manager.searchResultDisplayFields.accessPassword><td>${line.row.accessPassword!}</td></#if>
                <#if manager.searchResultDisplayFields.accessUserName><td>${line.row.accessUserName!}</td></#if>
                <#if manager.searchResultDisplayFields.accessLocation><td>${line.row.accessLocation!}</td></#if>
                <#if manager.searchResultDisplayFields.accessStatus><td>${line.row.accessStatus!}</td></#if>
                <#if manager.searchResultDisplayFields.adminPassword><td>${line.row.adminPassword!}</td></#if>
                <#if manager.searchResultDisplayFields.adminUrl><td>${line.row.adminUrl!}</td></#if>
                <#if manager.searchResultDisplayFields.adminUserName><td>${line.row.adminUserName!}</td></#if>
                <#if manager.searchResultDisplayFields.authentication><td>${line.row.authentication!}</td></#if>
                <#if manager.searchResultDisplayFields.callNumber><td>${line.row.callNumber!}</td></#if>
                <#if manager.searchResultDisplayFields.callNumberPrefix><td>${line.row.callNumberPrefix!}</td></#if>
                <#if manager.searchResultDisplayFields.classificationPart><td>${line.row.classificationPart!}</td></#if>
                <#if manager.searchResultDisplayFields.coverageDate><td>${line.row.coverageDate!}</td></#if>
                <#if manager.searchResultDisplayFields.donorCode><td>${line.row.donorCode!}</td></#if>
                <#if manager.searchResultDisplayFields.donorNote><td>${line.row.donorNote!}</td></#if>
                <#if manager.searchResultDisplayFields.donorPublic><td>${line.row.donorPublic!}</td></#if>
                <#if manager.searchResultDisplayFields.publisher><td>${line.row.publisher!}</td></#if>
                <#if manager.searchResultDisplayFields.holdingsNote><td>${line.row.holdingsNote!}</td></#if>
                <#if manager.searchResultDisplayFields.ill><td>${line.row.ill!}</td></#if>
                <#if manager.searchResultDisplayFields.imprint><td>${line.row.imprint!}</td></#if>
                <#if manager.searchResultDisplayFields.itemPart><td>${line.row.itemPart!}</td></#if>
                <#if manager.searchResultDisplayFields.linkText><td>${line.row.linkText!}</td></#if>
                <#if manager.searchResultDisplayFields.location><td>${line.row.locationName!}</td></#if>
                <#if manager.searchResultDisplayFields.locationLevel><td>${line.row.locationLevel!}</td></#if>
                <#if manager.searchResultDisplayFields.locationLevelName><td>${line.row.locationLevelName!}</td></#if>
                <#if manager.searchResultDisplayFields.numberOfSimultaneousUses><td>${line.row.numberOfSimultaneousUses!}</td></#if>
                <#if manager.searchResultDisplayFields.perpetualAccess><td>${line.row.perpetualAccess!}</td></#if>
                <#if manager.searchResultDisplayFields.persistLink><td>${line.row.persistLink!}</td></#if>
                <#if manager.searchResultDisplayFields.platform><td>${line.row.platForm!}</td></#if>
                <#if manager.searchResultDisplayFields.proxied><td>${line.row.proxied!}</td></#if>
                <#if manager.searchResultDisplayFields.publicNote><td>${line.row.publicNote!}</td></#if>
                <#if manager.searchResultDisplayFields.receiptStatus><td>${line.row.receiptStatus!}</td></#if>
                <#if manager.searchResultDisplayFields.shelvingOrderCode><td>${line.row.shelvingOrderCode!}</td></#if>
                <#if manager.searchResultDisplayFields.shelvingSchemeCode><td>${line.row.shelvingSchemeCode!}</td></#if>
                <#if manager.searchResultDisplayFields.shelvingSchemeValue><td>${line.row.shelvingSchemeValue!}</td></#if>
                <#if manager.searchResultDisplayFields.subscription><td>${line.row.subscription!}</td></#if>
                <#if manager.searchResultDisplayFields.url><td>${line.row.url!}</td></#if>
                <#if manager.searchResultDisplayFields.statisticalCode><td>${line.row.statisticalCode!}</td></#if>
            </tr>
            </#list>
        </tbody>
        <tfoot><tr>
            <th></th>
            <#if manager.searchResultDisplayFields.localId><th></th></#if>
            <#if manager.searchResultDisplayFields.title><th></th></#if>
            <#if manager.searchResultDisplayFields.accessPassword><th></th></#if>
            <#if manager.searchResultDisplayFields.accessUserName><th></th></#if>
            <#if manager.searchResultDisplayFields.accessLocation><th></th></#if>
            <#if manager.searchResultDisplayFields.accessStatus><th></th></#if>
            <#if manager.searchResultDisplayFields.adminPassword><th></th></#if>
            <#if manager.searchResultDisplayFields.adminUrl><th></th></#if>
            <#if manager.searchResultDisplayFields.adminUserName><th></th></#if>
            <#if manager.searchResultDisplayFields.authentication><th></th></#if>
            <#if manager.searchResultDisplayFields.callNumber><th></th></#if>
            <#if manager.searchResultDisplayFields.callNumberPrefix><th></th></#if>
            <#if manager.searchResultDisplayFields.classificationPart><th></th></#if>
            <#if manager.searchResultDisplayFields.coverageDate><th></th></#if>
            <#if manager.searchResultDisplayFields.donorCode><th></th></#if>
            <#if manager.searchResultDisplayFields.donorNote><th></th></#if>
            <#if manager.searchResultDisplayFields.donorPublic><th></th></#if>
            <#if manager.searchResultDisplayFields.publisher><th></th></#if>
            <#if manager.searchResultDisplayFields.holdingsNote><th></th></#if>
            <#if manager.searchResultDisplayFields.ill><th></th></#if>
            <#if manager.searchResultDisplayFields.imprint><th></th></#if>
            <#if manager.searchResultDisplayFields.itemPart><th></th></#if>
            <#if manager.searchResultDisplayFields.linkText><th></th></#if>
            <#if manager.searchResultDisplayFields.location><th></th></#if>
            <#if manager.searchResultDisplayFields.locationLevel><th></th></#if>
            <#if manager.searchResultDisplayFields.locationLevelName><th></th></#if>
            <#if manager.searchResultDisplayFields.numberOfSimultaneousUses><th></th></#if>
            <#if manager.searchResultDisplayFields.perpetualAccess><th></th></#if>
            <#if manager.searchResultDisplayFields.persistLink><th></th></#if>
            <#if manager.searchResultDisplayFields.platform><th></th></#if>
            <#if manager.searchResultDisplayFields.proxied><th></th></#if>
            <#if manager.searchResultDisplayFields.publicNote><th></th></#if>
            <#if manager.searchResultDisplayFields.receiptStatus><th></th></#if>
            <#if manager.searchResultDisplayFields.shelvingOrderCode><th></th></#if>
            <#if manager.searchResultDisplayFields.shelvingSchemeCode><th></th></#if>
            <#if manager.searchResultDisplayFields.shelvingSchemeValue><th></th></#if>
            <#if manager.searchResultDisplayFields.subscription><th></th></#if>
            <#if manager.searchResultDisplayFields.url><th></th></#if>
            <#if manager.searchResultDisplayFields.statisticalCode><th></th></#if>
        </tr></tfoot>
    </table>
        <@search_pager manager container />
    </#if>

</#macro>

<#macro search_pagesize manager>
<div class="dataTables_length">
    <label>Show
        <select onchange="jQuery('#hidden_pageSize').val(jQuery(this).val)">
            <#list manager.pageSizeOptions as pso>
                <#if pso.key != '' && manager.pageSize == pso.key?number>
                    <option selected value="${pso.key}">${pso.value}</option>
                <#else>
                    <option value="${pso.key}">${pso.value}</option>
                </#if>
            </#list>
        </select> entries</label>
</div>
</#macro>

<#macro search_pager manager container>
    <#if manager.pager?? && manager.pager.currentPage gt 0>
    <div class="dataTables_info">
        Showing ${(manager.pager.currentPage - 1) * manager.pageSize + 1}
        to ${(manager.pager.currentPage - 1) * manager.pageSize + manager.displayedLines}
        of ${manager.totalLines} entries
    </div>
    <div style="float : right;height:44px;margin-top: -18px">
        <@krad.template component=manager.pager parent=container />
    </div>
    </#if>
</#macro>

<#macro search_pager_top manager container>
    <#if manager.pager?? && manager.pager.currentPage gt 0>
    <div style="float : right;height:50px">
        <@krad.template component=manager.pager parent=container />
    </div>
    </#if>
</#macro>

<#macro search_control_wrapper id container label>
<div id="${id}" class="uif-inputField" data-parent="${container.id}" data-role="InputField" data-label="${label}">
    <#nested/>
    <span id="${id}_markers"></span>
    <div id="${id}_errors" class="uif-validationMessages" style="display: none;" data-messages_for="${id}"></div>
    <span id="${id}_info_message"></span>
</div>
</#macro>

<#macro search_checkbox_control line prop container label>
    <@search_control_wrapper "${line.lineId}_${prop}" container label>
        <@spring.formCheckbox id="${line.lineId}_${prop}_control" label=""
        attributes='class="uif-checkboxControl" data-role="Control" data-control_for="${line.lineId}_${prop}"'
        path="KualiForm.${line.bindPath}.${prop}" />
    </@search_control_wrapper>
</#macro>

<#macro search_select_control line prop container label options>
    <@search_control_wrapper "${line.lineId}_${prop}" container label>
        <@spring.formSingleSelect id="${line.lineId}_${prop}_control"
        attributes='class="uif-dropdownControl fixed-size-50-select" data-role="Control" data-control_for="${line.lineId}_${prop}"'
        path="KualiForm.${line.bindPath}.${prop}" options=options />
    </@search_control_wrapper>
</#macro>