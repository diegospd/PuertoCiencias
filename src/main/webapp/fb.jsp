<%-- 
    Document   : fb
    Created on : Nov 12, 2014, 2:16:46 AM
    Author     : diego
--%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@page import="model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
      <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Puerto Ciencias</title>
      </head>
      <body>
            <h1>¡Facebook!</h1>
            <%@page import="java.net.URLEncoder" %>
            <%
                  String fbURL = "http://www.facebook.com/dialog/oauth?client_id=732943860124575&redirect_uri=" + URLEncoder.encode("http://localhost:8080/P2/facebook", "UTF-8") + "&scope=publish_actions&state=";
                  Usuario user = (Usuario) request.getSession().getAttribute("user");
                  fbURL += user.getIdCuenta();

            %>

            Si quieres darnos permiso de publicar comentarios en Facebook a tu nombre pica el ícono.
            <br/>

            
            <br/>
            <a href="<%= fbURL%>">
                  <img src="resources/imagenes/fb.png" border="0"  height="72" width="72" />

            </a>
            <br/>

            
    
            
            
      </body>
</html>
