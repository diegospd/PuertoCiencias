
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>

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

    <div>
        <f:view>
            <h:messages style="color: red" showDetail="true"/>
            <h:form id="login">

                <table    border="2">
                    <tr>
                        <td>
                            <h1 class="cubic.ttf">Puerto Ciencias</h1>
                            <h2>Iniciar Sesión</h2>
                            <h2>Usuario:</h2>
                            <h:inputText id="username" 
                                 value="#{userManager.username}"/>
                            </br>

                            <h2> Password:</h2> 
                            <h:inputSecret id="password"
                                   value="#{userManager.pass1}"/>
                            </br>
                            </br>
                            <h:commandLink id="create"
                                   value="Registrarse"
                                   action="create"/>

                            </br>
                            <h:commandLink id="recuperar" 
                                   value="¿Olvidaste tu contraseña?"
                                   action="recuperar"/>
                            </br>
                            <h:commandButton id="submit" 
                                     type="submit"
                                     value="IniciarSesion"
                                     action="#{userManager.validateUser}"/>
                            <br>

                        </td>
                    </tr>
                </table>

            </h:form>

        </f:view>
    </div> 
</body>
</html>
