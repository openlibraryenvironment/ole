<#macro bill oleNoticeBo oleNoticeContentConfigurationBo>


<table>
    </BR></BR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Bill Number")} :</TD>
        <#if oleNoticeBo.billNumber ??  >
            <TD>${oleNoticeBo.billNumber}</TD>
        <#else>
            <TD></TD>
        </#if>

    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Fee Type")} :</TD>
        <#if oleNoticeBo.feeType ??  >
            <TD>${oleNoticeBo.feeType}</TD>
        <#else>
            <TD></TD>
        </#if>
    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Fee Amount")} :</TD>
        <#if oleNoticeBo.feeAmount ??  >
            <TD>${oleNoticeBo.feeAmount}</TD>
        <#else>
            <TD></TD>
        </#if>
    </TR>
</table>

</#macro>