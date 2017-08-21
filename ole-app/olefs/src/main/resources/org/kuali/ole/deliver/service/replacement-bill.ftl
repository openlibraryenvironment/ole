<#macro bill oleNoticeBo oleNoticeContentConfigurationBo>


<table>
    </BR></BR>
    <#if oleNoticeContentConfigurationBo.oleNoticeFieldLabelMappings ??>
        <#list oleNoticeContentConfigurationBo.oleNoticeFieldLabelMappings as oleNoticeFieldLabelMapping>
            <#switch oleNoticeFieldLabelMapping.fieldName>
                <#case "Bill Number">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Bill Number")} :</TD>
                        <#if oleNoticeBo.billNumber ??  >
                            <TD>${oleNoticeBo.billNumber}</TD>
                        <#else>
                            <TD></TD>
                        </#if>

                    </TR>
                    <#break>
                <#case "Fee Type">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Fee Type")} :</TD>
                        <#if oleNoticeBo.feeType ??  >
                            <TD>${oleNoticeBo.feeType}</TD>
                        <#else>
                            <TD></TD>
                        </#if>

                    </TR>
                    <#break>
                <#case "Fee Amount">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Fee Amount")} :</TD>
                        <#if oleNoticeBo.feeAmount ??  >
                            <TD>${oleNoticeBo.feeAmount}</TD>
                        <#else>
                            <TD></TD>
                        </#if>

                    </TR>
                    <#break>
            </#switch>
        </#list>
    </#if>
</table>

</#macro>