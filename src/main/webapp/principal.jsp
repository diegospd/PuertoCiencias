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

        <style type="text/css">
            body{background-color:#036fab;
                 font-family: 'Allerta Stencil', sans-serif;}
            header{background-color: #DAA520;
                   text-align: left;}
            @font-face{
                font-family: "cubic";
                src:url("resources/cubic/cubic.ttf") format("truetype");
            }

            div   { text-align:center; }
            table { margin:auto; }
            header figure{float: left;
                          margin-right: 10px;}
            h1{font-family:"cubic";
                font-size: 1cm;}
           
            header h1{font-family:"cubic";
                      color: #f7f7f7;
                      float: left;
                      font-size: 1.6cm;
                      display: block;
                      margin: 7px 80px 0px 50px;
                      padding: 0px;}
            header figure img{width: 100px;
                              height: 100px;
                              border-radius:120%;
                              src:url("resources/imagenes/tron2.jpg");
            }
        </style>
      </head>
      <body>
          <header>
            </br>
                <figure>
                    <img src="<%=request.getContextPath()%>/resources/imagenes/tron2.jpg">
                </figure>
                <h1 class="cubic.ttf">Puerto Ciencias </h1>
                </br>
                </br>
                </br>
                </br>
                </br>
                </br>
                </br>
        </header>
            <f:view>
            <h1><h:outputText value="Página Principal Puerto Ciencias" /></h1>
            Welcome ${user.username}!  You've been registered.
            <h:form>
                <h:commandButton id="logout" value="Logout" action="#{userManager.logout}"/>
            </h:form>
        </f:view>
      </body>
</html>
