<%-- 
    Document   : recuperar_contrasena
    Created on : Nov 7, 2014, 1:49:12 PM
    Author     : diego
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Esta es la página de recuperación de contraseña!</h1>
      
        <form action="RecuperarPassword" method="POST">
            Escribe tu dirección de correo
            </br>
            <input type="text" name="correo" value="" />@ciencias.unam.mx
            </br>
            </br>
            
            Escribe tu número de cuenta
            </br>
            <input type="text" name="numCuenta" value="" />
            </br>
            </br>
            
            Escribe tu nueva contraseña
            </br>
            <input type="password" name="pass1" value="" />
            </br>
            </br>
            
            Confirma tu nueva contraseña
            </br>
            <input type="password" name="pass2" value="" />
            
            </br>
            </br>
            <input type="submit" value="Enviar" name="enviar" />
            
            
        </form>
    </body>
</html>
