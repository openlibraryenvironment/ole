<#macro bill oleNoticeBo oleNoticeContentConfigurationBo>


<table>
    </BR></BR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Bill Number")} :</TD>
        <TD>${oleNoticeBo.billNumber}</TD>
    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Fee Type")} :</TD>
        <TD>${oleNoticeBo.feeType}</TD>
    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Fee Amount")} :</TD>
        <TD>$${oleNoticeBo.feeAmount}</TD>
    </TR>
</table>

</#macro>