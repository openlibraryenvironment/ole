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
<#--
    Css Grid Layout Manager

    This is a layout that uses divs instead of a table to achieve a table look and feel.  This is done through the
    use of css with divs which represent "rows" and "cells" of the layout.  Two variations can be achieved through
    this layout, either a fluid version (stretches and reacts to resizing the window) or fixed (does not change
    the size of the "cells").
 -->

<#macro uif_cssGrid items manager container>
    <#if manager.styleClassesAsString?has_content>
        <#local styleClass="class=\"${manager.styleClassesAsString}\""/>
    </#if>

    <#if manager.style?has_content>
        <#local style="style=\"${manager.style}\""/>
    </#if>


    <div id="${manager.id}_cssGridLayout" ${style!} ${styleClass!}>

        <#local rowIndex = 0/>
        <#local cellIndex = 0/>
        <#list manager.rows as rowItems>
            <div class="${manager.rowCssClassAttributes[rowIndex]}">
                <#list rowItems as item>
                    <div class="${manager.cellCssClassAttributes[cellIndex]}">
                        <@krad.template component=item/>
                    </div>
                    <#local cellIndex = cellIndex + 1/>
                </#list>
            </div>
            <#local rowIndex = rowIndex + 1/>
        </#list>

    </div>
</#macro>