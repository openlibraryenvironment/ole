<#macro ole_bib_eresource_search items manager container>
    <#if manager.totalLines gt 0>
        <@search_pagesize manager />
        <@search_pager_top manager container/>
    <table class="table table-condensed table-bordered uif-tableCollectionLayout dataTable">
        <thead>
        <tr>
            <#if manager.searchResultDisplayFields.localId><th><a style="text-decoration:underline;" onclick="bibSortBy('local');">Local Identifier</a></th></#if>
            <#if manager.searchResultDisplayFields.title><th><a style="text-decoration:underline;" onclick="bibSortBy('title');">Title</a></th></#if>
            <#if manager.searchResultDisplayFields.author><th><a style="text-decoration:underline;" onclick="bibSortBy('author');">Author</a></th></#if>
            <#if manager.searchResultDisplayFields.publisher><th><a style="text-decoration:underline;" onclick="bibSortBy('publisher');">Publisher</a></th></#if>
            <#if manager.searchResultDisplayFields.isbn><th>ISBN</th></#if>
            <#if manager.searchResultDisplayFields.issn><th>ISSN</th></#if>
            <#if manager.searchResultDisplayFields.subject><th>Subject</th></#if>
            <#if manager.searchResultDisplayFields.publicationPlace><th>Publication Place</th></#if>
            <#if manager.searchResultDisplayFields.format><th>Format</th></#if>
            <#if manager.searchResultDisplayFields.formGenre><th>Form Genre</th></#if>
            <#if manager.searchResultDisplayFields.language><th>Language</th></#if>
            <#if manager.searchResultDisplayFields.description><th>Description</th></#if>
            <#if manager.searchResultDisplayFields.publicationDate><th><a style="text-decoration:underline;" onclick="bibSortBy('publicationDate');">Pub Date</a></th></#if>
            <#if manager.searchResultDisplayFields.barcode><th>Bar Code</th></#if>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
            <#list manager.searchLines as line>
            <tr class="odd">
                <#if manager.searchResultDisplayFields.localId><td>${line.row.localId!}</td></#if>
                <#if manager.searchResultDisplayFields.title><td>${line.row.title!}</td></#if>
                <#if manager.searchResultDisplayFields.author><td>${line.row.author!}</td></#if>
                <#if manager.searchResultDisplayFields.publisher><td>${line.row.publisher!}</td></#if>
                <#if manager.searchResultDisplayFields.isbn><td>${line.row.isbn!}</td></#if>
                <#if manager.searchResultDisplayFields.issn><td>${line.row.issn!}</td></#if>
                <#if manager.searchResultDisplayFields.subject><td>${line.row.subject!}</td></#if>
                <#if manager.searchResultDisplayFields.publicationPlace><td>${line.row.publicationPlace!}</td></#if>
                <#if manager.searchResultDisplayFields.format><td>${line.row.format!}</td></#if>
                <#if manager.searchResultDisplayFields.formGenre><td>${line.row.formGenre!}</td></#if>
                <#if manager.searchResultDisplayFields.language><td>${line.row.language!}</td></#if>
                <#if manager.searchResultDisplayFields.description><td>${line.row.description!}</td></#if>
                <#if manager.searchResultDisplayFields.publicationDate><td>${line.row.publicationDate!}</td></#if>
                <#if manager.searchResultDisplayFields.barcode><td>${line.row.barcode!}</td></#if>
                <td><div>
                   <a target="_blank" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=bibliographic&amp;docFormat=${line.row.docFormat}&amp;docId=${line.row.localId}&amp;editable=false&amp;fromSearch=true">View Bib</a>
                  </div></td>
                <td> <div>
                        <a target="_blank" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=eHoldings&amp;docFormat=oleml&amp;bibId=${line.row.localId}&amp;editable=true&amp;fromSearch=true&amp;eResourceId=${line.row.oleERSIdentifier}">Create New EInstance</a>
                </div></td>
            </tr>
            </#list>
        </tbody>
        <tfoot><tr>
            <#if manager.searchResultDisplayFields.title><th></th></#if>
            <#if manager.searchResultDisplayFields.localId><th></th></#if>
            <#if manager.searchResultDisplayFields.author><th></th></#if>
            <#if manager.searchResultDisplayFields.publisher><th></th></#if>
            <#if manager.searchResultDisplayFields.isbn><th></th></#if>
            <#if manager.searchResultDisplayFields.issn><th></th></#if>
            <#if manager.searchResultDisplayFields.subject><th></th></#if>
            <#if manager.searchResultDisplayFields.publicationPlace><th></th></#if>
            <#if manager.searchResultDisplayFields.format><th></th></#if>
            <#if manager.searchResultDisplayFields.formGenre><th></th></#if>
            <#if manager.searchResultDisplayFields.language><th></th></#if>
            <#if manager.searchResultDisplayFields.description><th></th></#if>
            <#if manager.searchResultDisplayFields.publicationDate><th></th></#if>
            <#if manager.searchResultDisplayFields.barcode><th></th></#if>
            <th></th>
            <th></th>
        </tr></tfoot>
    </table>
        <@search_pager manager container />
    </#if>
</#macro>




<#macro ole_bib_ers_search items manager container>
    <#if manager.totalLines gt 0>
        <@search_pagesize manager />
        <@search_pager_top manager container/>
    <table class="table table-condensed table-bordered uif-tableCollectionLayout dataTable">
        <thead>
        <tr>
            <th></th>
            <#if manager.searchResultDisplayFields.localId><th><a style="text-decoration:underline;" onclick="bibSortBy('local');">Local Identifier</a></th></#if>
            <#if manager.searchResultDisplayFields.title><th><a style="text-decoration:underline;" onclick="bibSortBy('title');">Title</a></th></#if>
            <#if manager.searchResultDisplayFields.author><th><a style="text-decoration:underline;" onclick="bibSortBy('author');">Author</a></th></#if>
            <#if manager.searchResultDisplayFields.publisher><th><a style="text-decoration:underline;" onclick="bibSortBy('publisher');">Publisher</a></th></#if>
            <#if manager.searchResultDisplayFields.isbn><th>ISBN</th></#if>
            <#if manager.searchResultDisplayFields.issn><th>ISSN</th></#if>
            <#if manager.searchResultDisplayFields.subject><th>Subject</th></#if>
            <#if manager.searchResultDisplayFields.publicationPlace><th>Publication Place</th></#if>
            <#if manager.searchResultDisplayFields.format><th>Format</th></#if>
            <#if manager.searchResultDisplayFields.formGenre><th>Form Genre</th></#if>
            <#if manager.searchResultDisplayFields.language><th>Language</th></#if>
            <#if manager.searchResultDisplayFields.description><th>Description</th></#if>
            <#if manager.searchResultDisplayFields.publicationDate><th><a style="text-decoration:underline;" onclick="bibSortBy('publicationDate');">Pub Date</a></th></#if>
            <#if manager.searchResultDisplayFields.barcode><th>Bar Code</th></#if>
            <th></th>

        </tr>
        </thead>
        <tbody>
            <#list manager.searchLines as line>
            <tr class="odd">
                <td class="sorting_1"><@checkbox_control line 'select' container "" /></td>
                <#if manager.searchResultDisplayFields.localId><td>${line.row.localId!}</td></#if>
                <#if manager.searchResultDisplayFields.title><td>${line.row.title!}</td></#if>
                <#if manager.searchResultDisplayFields.author><td>${line.row.author!}</td></#if>
                <#if manager.searchResultDisplayFields.publisher><td>${line.row.publisher!}</td></#if>
                <#if manager.searchResultDisplayFields.isbn><td>${line.row.isbn!}</td></#if>
                <#if manager.searchResultDisplayFields.issn><td>${line.row.issn!}</td></#if>
                <#if manager.searchResultDisplayFields.subject><td>${line.row.subject!}</td></#if>
                <#if manager.searchResultDisplayFields.publicationPlace><td>${line.row.publicationPlace!}</td></#if>
                <#if manager.searchResultDisplayFields.format><td>${line.row.format!}</td></#if>
                <#if manager.searchResultDisplayFields.formGenre><td>${line.row.formGenre!}</td></#if>
                <#if manager.searchResultDisplayFields.language><td>${line.row.language!}</td></#if>
                <#if manager.searchResultDisplayFields.description><td>${line.row.description!}</td></#if>
                <#if manager.searchResultDisplayFields.publicationDate><td>${line.row.publicationDate!}</td></#if>
                <#if manager.searchResultDisplayFields.barcode><td>${line.row.barcode!}</td></#if>
                <td><div>
                   <a target="_blank" href="editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=bibliographic&amp;docFormat=${line.row.docFormat}&amp;docId=${line.row.localId}&amp;editable=false&amp;fromSearch=true">View Bib</a>
                </div></td>
            </tr>
            </#list>
        </tbody>
        <tfoot><tr>
            <th></th>
            <#if manager.searchResultDisplayFields.title><th></th></#if>
            <#if manager.searchResultDisplayFields.localId><th></th></#if>
            <#if manager.searchResultDisplayFields.author><th></th></#if>
            <#if manager.searchResultDisplayFields.publisher><th></th></#if>
            <#if manager.searchResultDisplayFields.isbn><th></th></#if>
            <#if manager.searchResultDisplayFields.issn><th></th></#if>
            <#if manager.searchResultDisplayFields.subject><th></th></#if>
            <#if manager.searchResultDisplayFields.publicationPlace><th></th></#if>
            <#if manager.searchResultDisplayFields.format><th></th></#if>
            <#if manager.searchResultDisplayFields.formGenre><th></th></#if>
            <#if manager.searchResultDisplayFields.language><th></th></#if>
            <#if manager.searchResultDisplayFields.description><th></th></#if>
            <#if manager.searchResultDisplayFields.publicationDate><th></th></#if>
            <#if manager.searchResultDisplayFields.barcode><th></th></#if>
            <th></th>
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
    <div style="float : right">
        <@krad.template component=manager.pager parent=container />
    </div>
    </#if>
</#macro>

<#macro search_pager_top manager container>
    <#if manager.pager?? && manager.pager.currentPage gt 0>
    <div style="float : right">
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

<#macro checkbox_control line prop container label>
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

