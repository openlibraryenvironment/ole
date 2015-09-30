<#import "itemInfo.ftl" as itemInfo>

<HTML>
<TITLE>${oleNoticeBo.noticeTitle}</TITLE>
<HEAD></HEAD>
<BODY>

<table>
    </BR></BR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Patron Name")} :</TD>
        <TD>${oleNoticeBo.patronName}</TD>
    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Address")} :</TD>
        <TD>${oleNoticeBo.patronAddress}</TD>
    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("EMAIL")} :</TD>
        <TD>${oleNoticeBo.patronEmailAddress}</TD>
    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Phone Number")} :</TD>
        <TD>${oleNoticeBo.patronPhoneNumber}</TD>
    </TR>
</table>

<br/>
<br/>


<table width=\"100%\">
    <TR>
        <TD>
            <CENTER>${oleNoticeBo.noticeTitle}</CENTER>
        </TD>
    </TR>
    <TR>
        <TD>
            <p>
            ${oleNoticeBo.noticeSpecificContent}
            </p>
        </TD>
    </TR>
</table>

<br/>
<br/>


<#list oleNoticeBos as oleNoticeBo>
    <@itemInfo.item oleNoticeBo=oleNoticeBo oleNoticeContentConfigurationBo=oleNoticeContentConfigurationBo></@itemInfo.item>
********************************************************************
</#list>

</BODY>
</HTML>