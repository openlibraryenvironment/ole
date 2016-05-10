<#macro patronBillItemView patronBillItemView >
<br/>
<br/>
<BODY>
<B>Bill Info</B>
<TABLE>
    </BR></BR>
    <TR>
        <td>
            Bill Number
        </td>
    <#if patronBillItemView.billNumber ??>
        <TD>${patronBillItemView.billNumber}</TD>
    <#else>
        <TD</TD>
    </#if>
    </TR>

    <TR>
        <td>
            Fee Type
        </td>
    <#if patronBillItemView.feeType ??>
        <TD>${patronBillItemView.feeType}</TD>
    <#else>
        <TD</TD>
    </#if>
    </TR>


    <TR>
        <td>
            Item Barcode
        </td>
    <#if patronBillItemView.itemBarcode ??>
        <TD>${patronBillItemView.itemBarcode}</TD>
    <#else>
        <TD</TD>
    </#if>
    </TR>



    <TR>
        <td>
            Amount to be Refunded
        </td>
    <#if patronBillItemView.refundAmount ??>
        <TD>${patronBillItemView.refundAmount}</TD>
    <#else>
        <TD</TD>
    </#if>
    </TR>
</TABLE>
</BODY>
</#macro>