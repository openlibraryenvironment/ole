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
      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Volume/Issue/Copy Number")}  :</TD>
      <TD>${oleNoticeBo.volumeNumber}</TD>
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