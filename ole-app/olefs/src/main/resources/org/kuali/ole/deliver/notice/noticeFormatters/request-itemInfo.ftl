<#macro item oleNoticeBo oleNoticeContentConfigurationBo>

<table>

    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Circulation Location/Library Name")} :</TD>
        <#if oleNoticeBo.circulationDeskName ??>
            <TD>${oleNoticeBo.circulationDeskName}</TD>
        <#else>
            <TD</TD>
        </#if>

    </TR>


    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Circulation Reply-To Email")} :</TD>
        <#if oleNoticeBo.circulationDeskReplyToEmail ??>
            <TD>${oleNoticeBo.circulationDeskReplyToEmail}</TD>
        <#else>
            <TD</TD>
        </#if>

    </TR>

  <TR>
      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Title")} :</TD>
      <#if oleNoticeBo.title ??>
          <TD>${oleNoticeBo.title}</TD>
      <#else>
          <TD</TD>
      </#if>
  </TR>
  <TR>
      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Author")} :</TD>
      <#if oleNoticeBo.author ??>
          <TD>${oleNoticeBo.author}</TD>
      <#else>
          <TD</TD>
      </#if>
  </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("CopyNumber")}  :</TD>
        <#if oleNoticeBo.copyNumber ??>
            <TD>${oleNoticeBo.copyNumber}</TD>
        <#else>
            <TD</TD>
        </#if>
    </TR>

    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Enumeration")}  :</TD>
        <#if oleNoticeBo.enumeration ??>
            <TD>${oleNoticeBo.enumeration}</TD>
        <#else>
            <TD</TD>
        </#if>
    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Chronology")}  :</TD>
        <#if oleNoticeBo.chronology ??>
            <TD>${oleNoticeBo.chronology}</TD>
        <#else>
            <TD</TD>
        </#if>
    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Call Number")} :</TD>
        <#if oleNoticeBo.itemCallNumber ??>
            <TD>${oleNoticeBo.itemCallNumber}</TD>
        <#else>
            <TD</TD>
        </#if>
    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Item Barcode")} :</TD>
        <#if oleNoticeBo.itemId ??>
            <TD>${oleNoticeBo.itemId}</TD>
        <#else>
            <TD</TD>
        </#if>
    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Library shelving location")} :</TD>
        <#if oleNoticeBo.itemShelvingLocation ??>
            <TD>${oleNoticeBo.itemShelvingLocation}</TD>
        <#else>
            <TD</TD>
        </#if>
    </TR>


    <#if oleNoticeBo.noticeType??>
        <#if oleNoticeBo.noticeType == "OnHoldNotice">
            <TR>
                <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Hold Expiration Date")} :</TD>
                <#if oleNoticeBo.expiredOnHoldDate ??>
                    <TD>${oleNoticeBo.expiredOnHoldDate}</TD>
                <#else>
                    <TD</TD>
                </#if>
            </TR>
        </#if>

        <#if oleNoticeBo.noticeType == "RecallNotice">
            <TR>
                <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Original Due Date")} :</TD>
                <#if oleNoticeBo.originalDueDate ??>
                    <TD>${(oleNoticeBo.originalDueDate)?date}</TD>
                <#else>
                    <TD</TD>
                </#if>

            </TR>
            <TR>
                <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Recall Due Date")} :</TD>
                <#if oleNoticeBo.newDueDate ??>
                    <TD>${(oleNoticeBo.newDueDate)?date}</TD>
                <#else>
                    <TD</TD>
                </#if>

            </TR>
        </#if>
    </#if>


</table>

</#macro>