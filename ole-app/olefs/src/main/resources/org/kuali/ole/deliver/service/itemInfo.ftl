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
      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Item was due")} :</TD>
      <TD>${oleNoticeBo.dueDateString}</TD>
  </TR>
  <TR>
      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Library shelving location")} :</TD>
      <TD>${oleNoticeBo.itemShelvingLocation}</TD>
  </TR>
  <TR>
      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Call Number")} :</TD>
      <TD>${oleNoticeBo.itemCallNumber}</TD>
  </TR>
  <TR>
      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Item Barcode")} :</TD>
      <TD>${oleNoticeBo.itemId}</TD>
  </TR>
</table>

</#macro>