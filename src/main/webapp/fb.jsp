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
            <title>Registro Puerto Ciencias</title>
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





            <div align="center">

                  <h1>¡Facebook!</h1>
                  <%@page import="java.net.URLEncoder" %>
                  <%
                        String fbURL = "http://www.facebook.com/dialog/oauth?client_id=732943860124575&redirect_uri=" + URLEncoder.encode("http://localhost:8080/P2/facebook", "UTF-8") + "&scope=publish_actions&state=";
                        Usuario user = (Usuario) request.getSession().getAttribute("user");
                        fbURL += user.getIdCuenta();

                  %>
                  <f:view>

                        <h:form>
                              <h:outputText value="Si quieres darnos permiso de publicar comentarios en Facebook a tu nombre pica el ícono." style="font-weight:bold" />

                              <br/>
                              <br/>
                              <br/>

                              <a href="<%= fbURL%>">
                                    <img src="resources/imagenes/facebook-login-button.png" border="0"   />

                              </a>


                              <br/>
                              <br/>
                              <br/>

                              <h:outputText value="Para que ya no publiquemos a tu nombre da click en este botón" style="font-weight:bold" />
                              <br/>
                              <h:commandButton value="Desvincular Facebook" action="#{menuView.desvincularFacebook(user)}" />

                              <br/>
                              <br/>
                              <br/>
                              <br/>
                              <br/>
                              <br/>

                              <h:commandButton value="Regresar" action="app-main"/>


                        </h:form>
                  </f:view>



            </div>

      </body>
</html>
