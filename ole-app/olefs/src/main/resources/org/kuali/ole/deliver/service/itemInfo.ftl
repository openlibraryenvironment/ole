<#macro item oleNoticeBo oleNoticeContentConfigurationBo>


<table>
    <#if oleNoticeContentConfigurationBo.oleNoticeFieldLabelMappings ??>
        <#list oleNoticeContentConfigurationBo.oleNoticeFieldLabelMappings as oleNoticeFieldLabelMapping>
          <#switch oleNoticeFieldLabelMapping.fieldName>
              <#case "Title">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Title")} :</TD>
                      <#if oleNoticeBo.title ??>
                          <TD>${oleNoticeBo.title}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
              <#case "Author">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Author")} :</TD>
                      <#if oleNoticeBo.author ??>
                          <TD>${oleNoticeBo.author}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
              <#case "CopyNumber">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("CopyNumber")} :</TD>
                      <#if oleNoticeBo.copyNumber ??>
                          <TD>${oleNoticeBo.copyNumber}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
              <#case "Enumeration">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Enumeration")} :</TD>
                      <#if oleNoticeBo.enumeration ??>
                          <TD>${oleNoticeBo.enumeration}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
              <#case "Chronology">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Chronology")} :</TD>
                      <#if oleNoticeBo.chronology ??>
                          <TD>${oleNoticeBo.chronology}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
              <#case "Call #">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Call #")} :</TD>
                      <#if oleNoticeBo.itemCallNumber ??>
                          <TD>${oleNoticeBo.itemCallNumber}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
              <#case "Call # Prefix">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Call # Prefix")} :</TD>
                      <#if oleNoticeBo.itemCallNumberPrefix ??>
                          <TD>${oleNoticeBo.itemCallNumberPrefix}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
              <#case "Item_Barcode">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Item_Barcode")} :</TD>
                      <#if oleNoticeBo.itemId ??>
                          <TD>${oleNoticeBo.itemId}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
              <#case "Item Due Date">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Item Due Date")} :</TD>
                      <#if oleNoticeBo.dueDateString ??>
                          <TD>${oleNoticeBo.dueDateString}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
              <#case "Library location">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Library location")} :</TD>
                      <#if oleNoticeBo.itemLibrary ??>
                          <TD>${oleNoticeBo.itemLibrary}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
              <#case "Library Shelving location">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Library Shelving location")} :</TD>
                      <#if oleNoticeBo.itemShelvingLocation ??>
                          <TD>${oleNoticeBo.itemShelvingLocation}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
              <#case "Check In Date">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Check In Date")} :</TD>
                      <#if oleNoticeBo.checkInDate ??>
                          <TD>${oleNoticeBo.checkInDate}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
              <#case "Missing Piece Note">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Missing Piece Note")} :</TD>
                      <#if oleNoticeBo.missingPieceNote ??>
                          <TD>${oleNoticeBo.missingPieceNote}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
              <#case "Claims Search Count">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Claims Search Count")} :</TD>
                      <#if oleNoticeBo.claimsSearchCount ??>
                          <TD>${oleNoticeBo.claimsSearchCount}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
              <#case "Item Type">
                  <TR>
                      <TD>${oleNoticeContentConfigurationBo.getFieldLabel("Item Type")} :</TD>
                      <#if oleNoticeBo.itemTypeDesc ??>
                          <TD>${oleNoticeBo.itemTypeDesc}</TD>
                      <#else>
                          <TD</TD>
                      </#if>
                  </TR>
                  <#break>
          </#switch>
        </#list>
    </#if>
</table>

</#macro>