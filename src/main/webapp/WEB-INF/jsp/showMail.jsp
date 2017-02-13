<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>{result.subject}</title>
    </head>
    <body>
        <table border="1">
            <tr>
                <td>From:</td><td>${result.author}</td>
            </tr>
            <tr>
                <td>Sent:</td><td>${result.date}</td>
            </tr> 
            <tr>
                <td>Subject:</td><td>${result.subject}</td>
            </tr>           
        </table> 
        ${result.body}<br/>
        <form action="searchMail"><input type="submit" value="Back to search"/></form>
    </body>
</html>
