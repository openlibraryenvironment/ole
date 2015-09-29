<HTML>
<TITLE>${title}</TITLE>
<HEAD></HEAD>
<BODY>

<table>
    </BR></BR>
    <TR>
        <TD>Patron Name :</TD>
        <TD>${oleNoticeBo.patronName}</TD>
    </TR>
    <TR>
        <TD>Address :</TD>
        <TD>${oleNoticeBo.patronAddress}</TD>
    </TR>
    <TR>
        <TD>EMAIL :</TD>
        <TD>${oleNoticeBo.patronEmailAddress}</TD>
    </TR>
    <TR>
        <TD>Phone Number :</TD>
        <TD>${oleNoticeBo.patronPhoneNumber}</TD>
    </TR>
</table>

<br/>
<br/>

<table width=\"100%\">
    <TR>
        <TD>
            <CENTER>${title}</CENTER>
        </TD>
    </TR>
    <TR>
        <TD>
            <p>
            ${oleNoticeBo.noticeSpecificContent}
            </p>
        </TD>
    </TR>
</table>

<br/>
<br/>


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
        <TD>${dueDate}</TD>
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
</BODY>
</HTML>