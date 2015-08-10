<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.users.User" %>

<%--
  Created by IntelliJ IDEA.
  User: David
  Date: 6/18/2015
  Time: 3:11 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
        <link type="text/css" rel="stylesheet" href="/css/common.css">
        <script type="text/javascript" src="/js/test.js" > </script>
        <title>Test title</title>
    </head>
    <body>
    <%
//        UserService userService = UserServiceFactory.getUserService();
//        User usr = userService.getCurrentUser();
        String testVar = "Test Varzzzzzzzz!!!";
        String newInput = "this is the value of the input textbox";

        pageContext.setAttribute("testVar", testVar);
        pageContext.setAttribute("newInput", newInput);
//        pageContext.setAttribute("serverResponse", response.getOutputStream());
    %>
        <h1 class="test">This is regular html!! with ${fn:escapeXml(testVar)}</h1>
        <%--<input type="text" name="testInput" value="${fn:escapeXml(newInput)}">--%>
    </body>
</html>
