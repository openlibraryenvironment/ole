
<HTML>
<#if oleNoticeBo.noticeTitle ??  >
<TITLE>${oleNoticeBo.noticeTitle}</TITLE>
<#else>
<TITLE></TITLE>
</#if>
<HEAD></HEAD>
<BODY>

<BODY>

<table>
    </BR></BR>
    <TR>
        <TD>E-Resource Name : </TD>
        <#if oleNoticeBo.noticeName ??>
            <TD>${oleNoticeBo.noticeName}</TD>
        <#else>
            <TD</TD>
        </#if>
    </TR>

    <TR>
        <TD></TD>
        <#if oleNoticeBo.noticeSpecificContent ??>
            <TD>${oleNoticeBo.noticeSpecificContent}</TD>
        <#else>
            <TD</TD>
        </#if>
    </TR>
    </table>

</BODY>