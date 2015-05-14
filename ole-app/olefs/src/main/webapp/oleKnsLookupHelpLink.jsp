<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Kuali Portal Index</title>
    <c:set var="oleFsKnsLookupHelpUrl" value="${ConfigProperties.ole.fs.kns.lookup.help.url}"/>
    <script type="text/javascript">
        window.onload = function() {
            var helpLinkNavigateLocation = '${oleFsKnsLookupHelpUrl}';
            window.location.href = helpLinkNavigateLocation;
        };
    </script>
</head>
    