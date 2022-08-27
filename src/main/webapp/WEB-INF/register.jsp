<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Waterbnb-Register</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/user.css" />
	
</head>

<body>
	<div class="register">
		<div class="login-card">
			<h1>Registro</h1>
			<form method="POST" action="RegisterUserServlet">
				<p>Usuario</p>
				<input class="input" type="text" pattern="[A-Za-z]{1,15}"
					title="Debe contener solo letras" required name="username"
					id="user" placeholder="Usuario" />
				<p>Contraseña</p>
				<input class="input" type="password"
					pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
					title="Debe contener 8 dígitos entre los que se incluye un número, una mayúscula y una minúscula"
					required name="password" id="pass" placeholder="Contraseña" />
				<p>Email</p>
				<input class="input" type="email"
					pattern="[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\.[a-zA-Z]{2,4}" name="email"
					required id="email" placeholder="Email" /> 
					<input type="submit"
					value="Registrarse" />
			</form>
			<p class="error">${messages}</p>
		</div>
	</div>
</body>
</html>
