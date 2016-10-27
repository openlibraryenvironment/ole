<#import "itemInfo.ftl" as itemInfo >
<#import "replacement-bill.ftl" as bill>

<HTML>
<#if oleNoticeBo.noticeTitle ??  >
<TITLE>${oleNoticeBo.noticeTitle}</TITLE>
<#else>
<TITLE></TITLE>
</#if>

<HEAD></HEAD>
<BODY>

<table>
    </BR></BR>
<#if oleNoticeBo.noticeTitle == "Return With Missing Item Notice">
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Circulation Location/Library Name")} :</TD>
        <#if oleNoticeBo.oleCirculationDesk ??>
            <TD>${oleNoticeBo.oleCirculationDesk.circulationDeskPublicName}</TD>
        <#else>
            <TD</TD>
        </#if>
    </TR>
</#if>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Patron Name")} :</TD>
        <#if oleNoticeBo.patronName ??  >
            <TD>${oleNoticeBo.patronName}</TD>
       <#else>
           <TD></TD>
        </#if>

    </TR>

    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Patron Name")} :</TD>
        <#if oleNoticeBo.patronName ??  >
            <TD>${oleNoticeBo.patronName}</TD>
       <#else>
           <TD></TD>
        </#if>

    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Address")} :</TD>
        <#if oleNoticeBo.patronAddress??>
            <TD>${oleNoticeBo.patronAddress}</TD>
        <#else>
            <TD</TD>
        </#if>

    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Email")} :</TD>
        <#if oleNoticeBo.patronEmailAddress??>
            <TD>${oleNoticeBo.patronEmailAddress}</TD>
        <#else>
            <TD</TD>
        </#if>

    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Phone #")} :</TD>
    <#if oleNoticeBo.patronPhoneNumber??>
        <TD>${oleNoticeBo.patronPhoneNumber}</TD>
    <#else>
        <TD</TD>
    </#if>

    </TR>
</table>

<br/>
<br/>


<table>
    <TR>

        <#if oleNoticeBo.noticeTitle??>
        <TD>
            <CENTER>${oleNoticeBo.noticeTitle}</CENTER>
        </TD>
        <#else>
        <TD></TD>
        </#if>

    </TR>
    <TR>

        <#if oleNoticeBo.noticeSpecificContent?? >
            <TD>
                <p>
                ${oleNoticeBo.noticeSpecificContent}
                </p>
            </TD>
        <#else>
            <TD>
                <p>
                </p>
            </TD>
        </#if>



    </TR>
</table>

<br/>
<br/>


<#list oleNoticeBos as oleNoticeBo>
    <#if oleNoticeContentConfigurationBo??>
        <#if oleNoticeBo.noticeTitle == "Lost">
            <@bill.bill oleNoticeBo=oleNoticeBo oleNoticeContentConfigurationBo=oleNoticeContentConfigurationBo></@bill.bill>
        </#if>
        <br/>
        <@itemInfo.item oleNoticeBo=oleNoticeBo oleNoticeContentConfigurationBo=oleNoticeContentConfigurationBo></@itemInfo.item>
    </#if>
<#if oleNoticeBo_has_next>
********************************************************************
</#if>
</#list>
<br/>
<#if oleNoticeBo.noticeSpecificFooterContent??>
${oleNoticeBo.noticeSpecificFooterContent}
</#if>
<br/>
</BODY>
</HTML>