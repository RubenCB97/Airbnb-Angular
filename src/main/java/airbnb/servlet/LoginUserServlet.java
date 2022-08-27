package airbnb.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; 

import es.unex.pi.model.*;
import es.unex.pi.dao.*;

import java.util.logging.Logger;

/**
 * Servlet implementation class LoginUserServlet
 */
@WebServlet (urlPatterns = { "/LoginUserServlet" })
public class LoginUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());   

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginUserServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("LoginUserServlet-GET");
		HttpSession session = request.getSession();
		
		if (session.getAttribute("user")!=null) {
			logger.info("entrando");
			response.sendRedirect("pages/index.html");
		}
		else {
			RequestDispatcher view = request.getRequestDispatcher("/pages/login.jsp");
			view.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info("LoginUserServlet-POST");
		
		//Conexion con BD
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		try {
			conn.createStatement().execute("PRAGMA foreign_keys = ON");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);
		

		//Valores del login
		String name = request.getParameter("username");
		String password = request.getParameter("password");
		
		//Creacion de user con el usuario obtenido
		User user = userDAO.get(name);

		if (user !=null && user.getPassword().equals(password)) 
		{

			//Recuperamos la sesion
			HttpSession session = request.getSession();
			
			session.setAttribute("user", user);
			response.sendRedirect("pages/index.html");
		}
		
		else 
		{
			if(user == null)
				request.setAttribute("messages", "Usuario no válido");
			else
				request.setAttribute("messages", "Contraseña no válida");	
			RequestDispatcher view = request.getRequestDispatcher("/pages/login.jsp");
			view.forward(request, response);
			doGet(request, response);
		}
		
	}

}
