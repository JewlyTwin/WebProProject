<%-- 
    Document   : Login
    Created on : Nov 15, 2018, 3:47:40 PM
    Author     : Computer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <h1>Login ::</h1>
        
        <form action="Login" method="post">
            Username <input type="text" name="username" required/><br>
            Password <input type="text" name="pass" required/><br>
            <input type="submit" value="Login"/>
        </form>
    </body>
</html>
