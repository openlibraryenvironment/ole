<#import "PatronBillItemView.ftl" as bill>

<HTML>
<TITLE>Refund Notice</TITLE>
<HEAD></HEAD>
<BODY>
<BODY>
<center><B>Refund Notice</B></center>
<table>
    </BR></BR>
    <TR>
        <TD>Paton Id :</TD>
    <#if patronBillViewBo.patronId ??>
        <TD>${patronBillViewBo.patronId}</TD>
    <#else>
        <TD</TD>
    </#if>
    </TR>

    <TR>
        <TD>Patron Name :</TD>
    <#if patronBillViewBo.patronName ??>
        <TD>${patronBillViewBo.patronName}</TD>
    <#else>
        <TD</TD>
    </#if>
    </TR>

    <TR>
        <TD>Patron Address :</TD>
    <#if patronBillViewBo.patronAddress ??>
        <TD>${patronBillViewBo.patronAddress}</TD>
    <#else>
        <TD</TD>
    </#if>
    </TR>

</table>
<TR>
    <TD>
    </TD>

</TR>



<#list patronBillViewBo.patronBillItemViewList as patronBillItemView>
            <@bill.patronBillItemView  patronBillItemView=patronBillItemView></@bill.patronBillItemView>

<br/>
    </#list>



</BODY>