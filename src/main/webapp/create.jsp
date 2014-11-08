
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
      "http://www.w3.org/TR/html4/loose.dtd">

<html>
      <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Create Account</title>
      </head>
      <body>

            <h1>Create a New Account</h1>

      <f:view>
            <h:form id="create">            
                  <h:panelGrid columns="3" border="0">
                        Correo: <h:inputText id="correo"       
                                             requiredMessage="*"
                                             value="#{userManager.correo}"
                                             required="true"/>  @ciencias.unam.mx
                        <h:message for="create:correo" style="color: red"/>

                        Username : <h:inputText id="username" 
                                               requiredMessage="*"
                                               value="#{userManager.username}"
                                               required="true"/>
                        <h:message for="create:username" style="color: red"/>

                        Número de cuenta <h:inputText id="numCuenta"       
                                                      requiredMessage="*"
                                                      value="#{userManager.numCuenta}"
                                                      required="true"/>  
                        <h:message for="create:numCuenta" style="color: red"/>

                        Password: <h:inputSecret id="pass1"    
                                                 requiredMessage="*"
                                                 value="#{userManager.pass1}"
                                                 required="true"/>
                        <h:message for="create:pass1" style="color: red"/>

                        Password (verify): <h:inputSecret id="pass2"   
                                                          requiredMessage="*"
                                                          value="#{userManager.pass2}"
                                                          required="true"/>
                        <h:message for="create:pass2" style="color: red"/>
                  </h:panelGrid>
                  
                  <h:outputText value="Te vamos a mandar un correo a #{userManager.correo}@ciencias.unam.mx para verificar que seas estudiante de Ciencias." style="font-weight:bold" />                  
                  
                  <br/>
                  <h:commandButton id="submit" 
                                   value="Create"
                                   action="#{userManager.createUser}"/>
                  <h:messages style="color: red" globalOnly="true"/>
            </h:form>
      </f:view>

</body>
</html>
