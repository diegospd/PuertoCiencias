

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>

    <h1>Login</h1>
    
    <f:view>
        <h:messages style="color: red"
                    showDetail="true"/>
        <h:form id="login">
            <h:panelGrid columns="2" border="0">
                Username: <h:inputText id="username" 
                                       value="#{userManager.username}"/>        
                Password: <h:inputSecret id="password"
                                         value="#{userManager.pass1}"/>
            </h:panelGrid>
            <h:commandButton id="submit" 
                             type="submit"
                             value="Login"
                             action="#{userManager.validateUser}"/>
            <br>
            <h:commandLink id="create"
                           value="Create New Account"
                           action="create"/>
            
            <br/>
            <h:commandLink id="recuperar_contrasena"
                           value="¿Olvidaste tu contraseña?"
                           action="recuperar_contrasena"/>
        </h:form>
       
    </f:view>
    
    </body>
</html>
