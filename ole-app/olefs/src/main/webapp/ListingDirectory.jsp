<%@page import="java.io.*" %>
<%@page import="java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="org.kuali.rice.core.api.config.property.ConfigContext" %>
<%@ include file="/jsp/sys/kfsTldHeader.jsp" %>

<html>
<body>
<portal:olePortalHeader/>
<portal:olePortalTabs selectedTab="${sessionScope.selectedTab}"/>
<br/><br/><br/><br/><br/><br/><br/><br/>
<ul>
<%
    String workDirectory = "";
    String filePath=(String)request.getParameter("filePath");
    java.io.File file;
    if(filePath!=null && !filePath.trim().isEmpty()){
        workDirectory = filePath;
        java.io.File directory= new java.io.File(workDirectory);
        if(directory.isDirectory() || directory.isFile()){

            if (directory.list()==null || directory.list().length <= 0 )
            {
                out.println("<center><h1><font color=\"white\">There is no file to Download</font></h1></center>");
            }
            else
            {
                out.println("<center><table bgcolor='white' border='1' width='80%'>");
                out.println("<tr><th>File/Folder Name</th><th>Size / kb</th><th>Last Modified</th>");

                File[] files = directory.listFiles();

                Arrays.sort(files, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        return Long.valueOf(f1.lastModified()).compareTo(
                                f2.lastModified());
                    }
                });

                for (int i = files.length - 1; i >= 0; i--) {
                    file = files[i];
                    if(file.isDirectory()){
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        String lastModified = sdf.format(file.lastModified());
%>
    <tr>
        <td> <a href="fileDownloadServlet?fileName=<%=file.getName()%>&filePath=<%=file.getAbsolutePath()%>">
            <img src="images-portal/ole_folder_symbol.jpeg" height="20" width="20"/><%=file.getName()%>
        </a> </td>&nbsp;<td></td> <td align="right"><%=lastModified%></td>
    </tr>
    <%
            }
        }
        for (int i = files.length - 1; i >= 0; i--) {
            file = files[i];
            if (file.isFile()) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String lastModified = sdf.format(file.lastModified());
    %>
    <tr>
        <td> <a href="fileDownloadServlet?fileName=<%=file.getName()%>&filePath=<%=file.getAbsolutePath()%>">
            <img src="images-portal/ole_file_symbol.jpeg" height="20" width="20"/><%=file.getName()%>
        </a> </td><td align="right"><%=file.length()%></td> <td align="right"><%=lastModified%></td>
    </tr>
    </tr>
    <%
                        }
                    }
                    out.println("</table></center>");
                }
        }else{
             out.println("<center><h1><font color=\"white\">Folder/File Path is Invalid</font></h1></center>");
        }
    }else{
         out.println("<center><h1><font color=\"white\">Folder/File Path is Invalid</font></h1></center>");
    }

%>
</ul>
<portal:portalBottom/>
</body>
</html>