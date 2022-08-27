<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Waterbnb-Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css" />
  </head>

  <body>
    <div class="login">
      <div class="login-card">
        <h1>Inicia sesión</h1>
        <form method="POST" action="LoginUserServlet">
          <p>Usuario</p>
          <input
            class="input"
            type="text"
            name="username"
            id="user"
            placeholder="Usuario"
          />
          <p>Contraseña</p>
          <input
            class="input"
            type="password"
            name="password"
            id="pass"
            placeholder="Contraseña"
          />
          <input type="submit" value="Ingresar" />
          </form>
          
          <p class="error">${messages}</p>
          <a href="RegisterUserServlet">Registrarse</a><br />
        
      </div>
    </div>
  </body>
</html>
