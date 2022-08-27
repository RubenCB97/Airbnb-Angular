package airbnb.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.User;

/**
 * Servlet implementation class RegisterUserServlet
 */
@WebServlet(urlPatterns = { "/RegisterUserServlet" })
public class RegisterUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterUserServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("RegisterUserServlet-GET");
		request.setCharacterEncoding("UTF-8");

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/register.jsp");
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("RegisterUserServlet-POST");
		boolean emailexist = false;

		// Conexion con BD
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);

		// Obtenemos los valores del login
		User user = new User();
		user.setUsername(request.getParameter("username"));
		user.setEmail(request.getParameter("email"));
		List<User> allUsers = userDAO.getAll();

		logger.info("Usuario " + user.getUsername());
		logger.info("Email " + user.getEmail());

		for (User us : allUsers) {
			if (us.getEmail().equals(user.getEmail())) {
				request.setAttribute("messages", "Correo ya registrado");
				emailexist = true;
				doGet(request, response);
			}
		}

		if (userDAO.get(user.getUsername()) != null) {
			request.setAttribute("messages", "Usuario ya registrado");
			doGet(request, response);
		} else if (!emailexist) {
			user.setPassword(request.getParameter("password"));
			logger.info("Password" + user.getPassword());
			long id = userDAO.add(user);
			user.setId(id);

			// Recuperar sesion
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			response.sendRedirect("LoginUserServlet");

		}

	}

}
