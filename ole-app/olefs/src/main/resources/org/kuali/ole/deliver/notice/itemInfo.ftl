<#macro item oleNoticeBo>

<table>
  <TR>
      <TD>Title :</TD>
      <TD>${oleNoticeBo.title}</TD>
  </TR>
  <TR>
      <TD>Author :</TD>
      <TD>${oleNoticeBo.author}</TD>
  </TR>
  <TR>
      <TD>Volume/Issue/Copy Number :</TD>
      <TD>${oleNoticeBo.volumeNumber}</TD>
  </TR>
  <TR>
      <TD>Item was due :</TD>
      <TD>${oleNoticeBo.dueDateString}</TD>
  </TR>
  <TR>
      <TD>Library shelving location :</TD>
      <TD>${oleNoticeBo.itemShelvingLocation}</TD>
  </TR>
  <TR>
      <TD>Call Number :</TD>
      <TD>${oleNoticeBo.itemCallNumber}</TD>
  </TR>
  <TR>
      <TD>Item Barcode :</TD>
      <TD>${oleNoticeBo.itemId}</TD>
  </TR>
</table>

</#macro>