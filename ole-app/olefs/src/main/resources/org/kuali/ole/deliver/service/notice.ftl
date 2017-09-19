<#import "itemInfo.ftl" as itemInfo >

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
    <#if oleNoticeContentConfigurationBo.oleNoticeFieldLabelMappings ??>
        <#list oleNoticeContentConfigurationBo.oleNoticeFieldLabelMappings as oleNoticeFieldLabelMapping>
            <#switch oleNoticeFieldLabelMapping.fieldName>
                <#case "Circulation Location/Library Name">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Circulation Location/Library Name")} :</TD>
                        <#if oleNoticeBo.oleCirculationDesk ??>
                            <TD>${oleNoticeBo.oleCirculationDesk.circulationDeskPublicName}</TD>
                        <#else>
                            <TD</TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Patron Name">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Patron Name")} :</TD>
                        <#if oleNoticeBo.patronName ??  >
                            <TD>${oleNoticeBo.patronName}</TD>
                        <#else>
                            <TD></TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Address">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Address")} :</TD>
                        <#if oleNoticeBo.patronAddress ??  >
                            <TD>${oleNoticeBo.patronAddress}</TD>
                        <#else>
                            <TD></TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Email">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Email")} :</TD>
                        <#if oleNoticeBo.patronEmailAddress ??  >
                            <TD>${oleNoticeBo.patronEmailAddress}</TD>
                        <#else>
                            <TD></TD>
                        </#if>
                    </TR>
                    <#break>
                <#case "Phone #">
                    <TR>
                        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Phone #")} :</TD>
                        <#if oleNoticeBo.patronPhoneNumber ??  >
                            <TD>${oleNoticeBo.patronPhoneNumber}</TD>
                        <#else>
                            <TD></TD>
                        </#if>
                    </TR>
                    <#break>
            </#switch>
        </#list>
    </#if>
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