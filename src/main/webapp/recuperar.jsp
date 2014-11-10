<%-- 
    Document   : recuperar
    Created on : Nov 8, 2014, 5:18:10 PM
    Author     : diego
--%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%><!DOCTYPE html>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
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
        <div>
            <f:view>
                <h:form id="recuperar">
                    <table border="2">
                        <tr>
                            <td>
                                <h1 class="cubic.ttf">Puerto Ciencias</h1>
                                <h2>Recuperar Contraseña</h2>
                                <h2>Correo(@ciencias.unam.mx):</h2>
                                <h:inputText id="correo"       
                                            requiredMessage="*"
                                            value="#{recuperar.correo}"
                                            required="true"/> 
                                <h:message for="create:correo" style="color: red"/>
                                </br>
                                <h2>Número de Cuenta:</h2>
                                <h:inputText id="numCuenta"       
                                            requiredMessage="*"
                                            value="#{recuperar.numCuenta}"
                                            required="true"/>  
                                <h:message for="create:numCuenta" style="color: red"/>
                                </br>
                                <h2>Nuevo Password:</h2>
                                <h:inputSecret id="pass1"    
                                            requiredMessage="*"
                                            value="#{recuperar.pass1}"
                                            required="true"/>
                                <h:message for="create:pass1" style="color: red"/>
                                </br>
                                <h2>Verificar Nuevo Password:</h2>
                                <h:inputSecret id="pass2"   
                                            requiredMessage="*"
                                            value="#{recuperar.pass2}"
                                            required="true"/>
                        <h:message for="create:pass2" style="color: red"/>
                        </br>
                        <h:outputText value="Te vamos a mandar un correo a #{recuperar.correo}@ciencias.unam.mx para verificar que seas estudiante de Ciencias." style="font-weight:bold" />                  

                        <br/>
                        <h:commandButton id="submit" 
                                         value="Enviar"
                                         action="#{recuperar.recuperar}"/>
                        <h:messages style="color: red" globalOnly="true"/>
                            </td>
                        </tr>
                    </table>
                </h:form>
            </f:view>
        </div>
    </body>
</html>
