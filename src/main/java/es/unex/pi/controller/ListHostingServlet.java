package es.unex.pi.controller;

import java.io.IOException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import es.unex.pi.dao.CategoryDAO;
import es.unex.pi.dao.HostingDAO;
import es.unex.pi.dao.HostingCategoriesDAO;
import es.unex.pi.dao.JDBCCategoryDAOImpl;
import es.unex.pi.dao.JDBCHostingDAOImpl;
import es.unex.pi.dao.JDBCHostingCategoriesDAOImpl;
import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.Category;
import es.unex.pi.model.Hosting;
import es.unex.pi.model.HostingCategories;
import es.unex.pi.model.User;
import es.unex.pi.util.Triplet;

import jakarta.servlet.RequestDispatcher;

import java.sql.Connection;



/**
 * Servlet implementation class ListHostingServlet
 */
@WebServlet("/ListHostingServlet.do")
public class ListHostingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListHostingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		logger.info("Atendiendo GET");
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);

		HostingDAO HostingDAO = new JDBCHostingDAOImpl();
		HostingDAO.setConnection(conn);
		
		CategoryDAO categoryDAO = new JDBCCategoryDAOImpl();
		categoryDAO.setConnection(conn);
		
		HostingCategoriesDAO HostingsCategoriesDAO = new JDBCHostingCategoriesDAOImpl();
		HostingsCategoriesDAO.setConnection(conn);
		
		List<Hosting> HostingsList = HostingDAO.getAll();
		
		Iterator<Hosting> itHostingList = HostingsList.iterator();

		List<Triplet<Hosting, User, List<HostingCategories>>> HostingsUserList = new ArrayList<Triplet<Hosting, User, List<HostingCategories>>>();

		while(itHostingList.hasNext()) {
			Hosting Hosting = (Hosting) itHostingList.next();
			User user = userDAO.get(Hosting.getIdu());
			List<HostingCategories> HostingsCategories = HostingsCategoriesDAO.getAllByHosting(Hosting.getId());
			
			logger.info("User " + user.getUsername());

			HostingsUserList.add(new Triplet<Hosting, User, List<HostingCategories>>(Hosting,user,HostingsCategories));
		}
		
		
		List<User> listUser = new ArrayList<User>();
		listUser = userDAO.getAll();
		Iterator<User> itUser = listUser.iterator();
		Map<User,List<Hosting>> userHostingsMap = new HashMap<User,List<Hosting>>();
		
		while(itUser.hasNext()) {
			User user = itUser.next();
			HostingsList = HostingDAO.getAllByUser(user.getId());
			userHostingsMap.put(user, HostingsList);
		}
		
		request.setAttribute("HostingsList",HostingsUserList);
		request.setAttribute("usersMap", userHostingsMap);
		
		RequestDispatcher view = request.getRequestDispatcher("WEB-INF/ListHostingUser.jsp");
		view.forward(request,response);
		
	}

	
}
