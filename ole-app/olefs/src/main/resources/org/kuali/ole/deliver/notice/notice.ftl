<#import "itemInfo.ftl" as itemInfo>


<HTML>
<TITLE>${oleNoticeBo.noticeTitle}</TITLE>
<HEAD></HEAD>
<BODY>

<table>
    </BR></BR>
    <TR>
        <TD>Patron Name :</TD>
        <TD>${oleNoticeBo.patronName}</TD>
    </TR>
    <TR>
        <TD>Address :</TD>
        <TD>${oleNoticeBo.patronAddress}</TD>
    </TR>
    <TR>
        <TD>EMAIL :</TD>
        <TD>${oleNoticeBo.patronEmailAddress}</TD>
    </TR>
    <TR>
        <TD>Phone Number :</TD>
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
<#list oleNoticeBos as oleNoticeBo>
    <@itemInfo.item oleNoticeBo=oleNoticeBo></@itemInfo.item>
********************************************************************
</#list>

</BODY>
</HTML>