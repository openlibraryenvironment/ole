<#macro item oleNoticeBo oleNoticeContentConfigurationBo>

<table>
  <TR>
      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Title")} :</TD>
      <TD>${oleNoticeBo.title}</TD>
  </TR>
  <TR>
      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Author")} :</TD>
      <TD>${oleNoticeBo.author}</TD>
  </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("CopyNumber")}  :</TD>
        <TD>${oleNoticeBo.copyNumber}</TD>
    </TR>

    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Enumeration")}  :</TD>
        <TD>${oleNoticeBo.enumeration}</TD>
    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Chronology")}  :</TD>
        <TD>${oleNoticeBo.chronology}</TD>
    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Call Number")} :</TD>
        <TD>${oleNoticeBo.itemCallNumber}</TD>
    </TR>
    <TR>
        <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Item Barcode")} :</TD>
        <TD>${oleNoticeBo.itemId}</TD>
    </TR>
    <#if oleNoticeBo.noticeTitle == "Overdue Notice">
  <TR>
      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Item was due")} :</TD>
      <TD>${oleNoticeBo.dueDateString}</TD>
  </TR>
    </#if>
    <#if oleNoticeBo.noticeTitle == "Lost">
        <TR>
            <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Library location")} :</TD>
            <TD>${oleNoticeBo.itemLibrary}</TD>
        </TR>
    </#if>

  <TR>
      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Library Shelving location")} :</TD>
      <TD>${oleNoticeBo.itemShelvingLocation}</TD>
  </TR>

</table>

</#macro>