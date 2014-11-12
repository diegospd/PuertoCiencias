<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
      "http://www.w3.org/TR/html4/loose.dtd">

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

      <div>
            <table border="2">
                  <tr>
                        <td>
                              <h1 class="cubic.ttf">Puerto Ciencias</h1>
                              <h2>Registrarse</h2>

                              <f:view>
                                    <h:form id="create">
                                          <h2>Correo (@ciencias.unam.mx): </h2>
                                          <h:inputText id="correo"       
                                                       requiredMessage="*Verifica tu correo, campo necesario"
                                                       value="#{userManager.correo}"
                                                       required="true"/>  
                                          <h:message for="create:correo" style="color: red"/>

                                          <h2>Username:</h2>
                                          <h:inputText id="username" 
                                                       requiredMessage="verifica tu usuario, campo necesario"
                                                       value="#{userManager.username}"
                                                       required="true"/>
                                          <h:message for="create:username" style="color: red"/>
                                          </br>
                                          <h2>Número de cuenta:</h2>
                                          <h:inputText id="numCuenta"       
                                                       requiredMessage="*Verifica tu número de cuenta, campo necesario"
                                                       value="#{userManager.numCuenta}"
                                                       required="true"/>  
                                          <h:message for="create:numCuenta" style="color: red"/>
                                          </br>
                                          <h2>Password:</h2>
                                          <h:inputSecret id="pass1"    
                                                         requiredMessage="*Verifica tu password, campo necesario"
                                                         value="#{userManager.pass1}"
                                                         required="true"/>
                                          <h:message for="create:pass1" style="color: red"/>
                                          </br>
                                          <h2>Verificar Password:</h2>
                                          <h:inputSecret id="pass2"   
                                                         requiredMessage="*Verifica tu password, campo necesario"
                                                         value="#{userManager.pass2}"
                                                         required="true"/>
                                          <h:message for="create:pass2" style="color: red"/>
                                          </br>
                                          <h:outputText value="Te vamos a mandar un correo a #{userManager.correo}@ciencias.unam.mx para verificar que seas estudiante de Ciencias." style="font-weight:bold" />                  

                                          <br/>
                                          <h:commandButton id="submit" 
                                                           value="Registrarse"
                                                           action="#{userManager.createUser}"/>
                                          <h:messages style="color: red" globalOnly="true"/>

                                    </h:form>
                              </f:view>
                        </td>   
                  </tr>
            </table>
      </div>  
</body>
</html>