<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>search through eMail</title>
    </head>    
    <body>
        <table border="1">
            <form method="POST" action="searchMail">


                <tr>
                    <td>From contains:</td>
                    <td><input type ="text" name="from" value="${pfrom}"/></td>
                </tr>
                <tr>
                    <td>Subject contains:</td>
                    <td><input type ="text" name="subject" value="${psubject}"/></td>   
                </tr>
                <tr>
                    <td>Before date:</td>
                    <td><input type ="text" name="before" value="${pbefore}"/></td>
                </tr>
                <tr>
                    <td>After date:</td>
                    <td><input type ="text" name="after" value="${pafter}"/></td>   
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="Search!"></td>
                </tr>
            </form>
        </table>
        <table border="1">
            <tr>
                <th>Author</th>
                <th>Subject</th> 
                <th>Date</th>
            </tr> 
            <c:forEach var="result" items="${results}"> 
                <tr>        
                    <td>${result.author}</td>
                    <td>${result.subject}</td>
                    <td>${result.date}<td>
                    <td><form method="Get" action="showMail">
                            <input type ="hidden" name="id" value="${result.id}"/>
                            <input type="submit" value="show"/>
                        </form></td>

                </tr>
            </c:forEach>
        </table>
        <form action="checkMail"><input type="submit" value="Check new mail!"/></form>        
    </body>
</html>
