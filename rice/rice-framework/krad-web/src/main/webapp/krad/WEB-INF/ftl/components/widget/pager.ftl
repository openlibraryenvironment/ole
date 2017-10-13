<#--

    Copyright 2005-2014 The Kuali Foundation

    Licensed under the Educational Community License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.opensource.org/licenses/ecl2.php

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<#macro uif_pager widget parent>

    <#if widget.styleClassesAsString?has_content>
        <#local styleClass="class=\"${widget.styleClassesAsString}\""/>
    </#if>

    <#if widget.style?has_content>
        <#local style="style=\"${widget.style}\""/>
    </#if>

<div ${style!} ${styleClass!}>
    <ul>
        <#if widget.currentPage == 1>
            <#local prevClass="class='disabled'"/>
        </#if>

        <#if widget.renderFirstLast>
            <li ${prevClass!}>
                <a data-onclick="${widget.linkScript}" data-num="first" class="uif-pageFirst"
                   href="#">First</a>
            </li>
        </#if>

        <#if widget.renderPrevNext>
            <li ${prevClass!}>
                <a data-onclick="${widget.linkScript}" data-num="prev" class="uif-pagePrev"
                   href="#">&laquo;</a>
            </li>
        </#if>

        <#list widget.pagesStart..widget.pagesEnd as pageNum>
            <#local liClass=""/>
            <#if widget.currentPage == pageNum>
                <#local liClass="class='active'"/>
            </#if>

            <li ${liClass!}>
                <a data-onclick="${widget.linkScript}" data-num="${pageNum}"
                   href="#">${pageNum}</a>
            </li>
        </#list>

        <#if widget.currentPage == widget.numberOfPages>
            <#local nextClass="class='disabled'"/>
        </#if>

        <#if widget.renderPrevNext>
            <li ${nextClass!}>
                <a data-onclick="${widget.linkScript}" data-num="next" class="uif-pageNext"
                   href="#">&raquo;</a>
            </li>
        </#if>

        <#if widget.renderFirstLast>
            <li ${nextClass!}>
                <a data-onclick="${widget.linkScript}" data-num="last" class="uif-pageLast"
                   href="#">Last</a>
            </li>
        </#if>
    </ul>
</div>

</#macro>



