<#macro item oleNoticeBo oleNoticeContentConfigurationBo>

<table>
    <#if oleNoticeContentConfigurationBo.oleNoticeFieldLabelMappings ??>
        <#list oleNoticeContentConfigurationBo.oleNoticeFieldLabelMappings as oleNoticeFieldLabelMapping>
            <#switch oleNoticeFieldLabelMapping.fieldName>
                <#case "Circulation Location/Library Name">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Circulation Location/Library Name")} :</TD>
                        <#if oleNoticeBo.circulationDeskName ??>
                            <TD>${oleNoticeBo.circulationDeskName}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Circulation Reply-To Email">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Circulation Reply-To Email")} :</TD>
                        <#if oleNoticeBo.circulationDeskReplyToEmail ??>
                            <TD>${oleNoticeBo.circulationDeskReplyToEmail}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Title">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Title")} :</TD>
                        <#if oleNoticeBo.title ??>
                            <TD>${oleNoticeBo.title}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Author">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Author")} :</TD>
                        <#if oleNoticeBo.author ??>
                            <TD>${oleNoticeBo.author}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "CopyNumber">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("CopyNumber")} :</TD>
                        <#if oleNoticeBo.copyNumber ??>
                            <TD>${oleNoticeBo.copyNumber}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Enumeration">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Enumeration")} :</TD>
                        <#if oleNoticeBo.enumeration ??>
                            <TD>${oleNoticeBo.enumeration}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Chronology">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Chronology")} :</TD>
                        <#if oleNoticeBo.chronology ??>
                            <TD>${oleNoticeBo.chronology}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Call #">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Call #")} :</TD>
                        <#if oleNoticeBo.itemCallNumber ??>
                            <TD>${oleNoticeBo.itemCallNumber}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Call # Prefix">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Call # Prefix")} :</TD>
                        <#if oleNoticeBo.itemCallNumberPrefix ??>
                            <TD>${oleNoticeBo.itemCallNumberPrefix}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Item_Barcode">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Item_Barcode")} :</TD>
                        <#if oleNoticeBo.itemId ??>
                            <TD>${oleNoticeBo.itemId}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Library shelving location">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Library shelving location")} :</TD>
                        <#if oleNoticeBo.itemShelvingLocation ??>
                            <TD>${oleNoticeBo.itemShelvingLocation}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Item Will Be Held until">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Item Will Be Held until")} :</TD>
                        <#if oleNoticeBo.expiredOnHoldDate ??>
                            <TD>${oleNoticeBo.expiredOnHoldDate}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Original Due Date">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Original Due Date")} :</TD>
                        <#if oleNoticeBo.originalDueDate ??>
                            <TD>${(oleNoticeBo.originalDueDate)?date}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Recall Due Date">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Recall Due Date")} :</TD>
                        <#if oleNoticeBo.newDueDate ??>
                            <TD>${(oleNoticeBo.newDueDate)?date}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "New Due Date">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("New Due Date")} :</TD>
                        <#if oleNoticeBo.newDueDate ??>
                            <TD>${(oleNoticeBo.newDueDate)?date}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Item Type">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Item Type")} :</TD>
                        <#if oleNoticeBo.itemTypeDesc ??>
                            <TD>${oleNoticeBo.itemTypeDesc}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>

            </#switch>
        </#list>
    </#if>
</table>

</#macro>