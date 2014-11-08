<%-- 
    Document   : principal
    Created on : Nov 8, 2014, 3:51:20 PM
    Author     : diego
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE html>
<html>
      <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Puerto Ciencias</title>
      </head>
      <body>
            <f:view>
            <h1><h:outputText value="Página Principal Puerto Ciencias" /></h1>
            Welcome ${user.username}!  You've been registered.
            <h:form>
                <h:commandButton id="logout" value="Logout" action="#{userManager.logout}"/>
            </h:form>
        </f:view>
      </body>
</html>
