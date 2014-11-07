<%-- 
    Document   : registro
    Created on : Nov 7, 2014, 1:41:07 AM
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
        <h1>Esta es la ventana de registro!</h1>
        </br>
        <form action="RegistroUsuario" method="POST">
            Escribe tu correo
            </br>
            <input type="text" name="correo" value="" /> @ciencias.unam.mx
            </br>
            </br>
            
            Elige un usuario
            </br>
            <input type="text" name="usuario" value="" />
            </br>
            </br>
            
            Escribe tu número de cuenta
            </br>
            <input type="text" name="numCuenta" value="" />
            </br>
            </br>
            
            Escribe tu contraseña
            </br>
            <input type="password" name="pass1" value="" />
            </br>
            </br>
            
            Confirma tu contraseña
            </br>
            <input type="password" name="pass2" value="" />
            
            </br>
            </br>
            <input type="submit" value="Enviar" name="enviar" />
            
        </form>
    </body>
</html>
