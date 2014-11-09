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
      </head>
      <body>
            <h1>Hello World!</h1>
      <f:view>
            <h:form id="recuperar">            
                  <h:panelGrid columns="3" border="0">
                        Correo: <h:inputText id="correo"       
                                             requiredMessage="*"
                                             value="#{recuperar.correo}"
                                             required="true"/>  @ciencias.unam.mx
                        <h:message for="create:correo" style="color: red"/>


                        Número de cuenta <h:inputText id="numCuenta"       
                                                      requiredMessage="*"
                                                      value="#{recuperar.numCuenta}"
                                                      required="true"/>  
                        <h:message for="create:numCuenta" style="color: red"/>

                        Nueva password: <h:inputSecret id="pass1"    
                                                 requiredMessage="*"
                                                 value="#{recuperar.pass1}"
                                                 required="true"/>
                        <h:message for="create:pass1" style="color: red"/>

                        Nueva password (verify): <h:inputSecret id="pass2"   
                                                          requiredMessage="*"
                                                          value="#{recuperar.pass2}"
                                                          required="true"/>
                        <h:message for="create:pass2" style="color: red"/>
                  </h:panelGrid>
                  
                  <h:outputText value="Te vamos a mandar un correo a #{recuperar.correo}@ciencias.unam.mx para verificar que seas estudiante de Ciencias." style="font-weight:bold" />                  
                  
                  <br/>
                  <h:commandButton id="submit" 
                                   value="Create"
                                   action="#{recuperar.recuperar}"/>
                  <h:messages style="color: red" globalOnly="true"/>
            </h:form>
      </f:view>
</body>
</html>
