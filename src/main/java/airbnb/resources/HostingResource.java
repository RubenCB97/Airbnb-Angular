package airbnb.resources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import es.unex.giiis.pi.resources.exceptions.CustomNotFoundException;
import es.unex.pi.dao.JDBCHostingDAOImpl;
import es.unex.pi.dao.JDBCHostingServicesDAOImpl;
import es.unex.pi.dao.JDBCServiceDAOImpl;
import es.unex.pi.dao.ServiceDAO;
import es.unex.pi.dao.CategoryDAO;
import es.unex.pi.dao.HostingCategoriesDAO;
import es.unex.pi.dao.HostingDAO;
import es.unex.pi.dao.HostingServicesDAO;
import es.unex.pi.dao.JDBCCategoryDAOImpl;
import es.unex.pi.dao.JDBCHostingCategoriesDAOImpl;
import es.unex.pi.model.Category;
import es.unex.pi.model.Service;
import es.unex.pi.model.Hosting;
import es.unex.pi.model.HostingCategories;
import es.unex.pi.model.HostingServices;
import es.unex.pi.model.User;

@Path("/hosts")
public class HostingResource {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;

	// Devuelve un alojamiento de la base de datos
	@GET
	@Path("/{host_id: [0-9]+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Hosting getHostingJSON(@PathParam("host_id") long host_id) {

		Connection conn = (Connection) sc.getAttribute("dbConn");
		HostingDAO hostingDao = new JDBCHostingDAOImpl();
		hostingDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		Hosting returnHosting = hostingDao.get(host_id);

		if (returnHosting == null) {
			throw new CustomNotFoundException("No existe el alojamiento con id: " + host_id);
		} else {

			return returnHosting;

		}
	}

	// Devuelve un alojamiento de la base de datos y que sea del usuario logueado
	@GET
	@Path("/edit/{host_id: [0-9]+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Hosting getEditHostingJSON(@PathParam("host_id") long host_id) {

		Connection conn = (Connection) sc.getAttribute("dbConn");
		HostingDAO hostingDao = new JDBCHostingDAOImpl();
		hostingDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		Hosting returnHosting = hostingDao.get(host_id);
		// Comrpueba que el alojamiento es del usuario logueado
		

		if (returnHosting == null || returnHosting.getIdu() != user.getId()) {
			throw new CustomNotFoundException("No existe el alojamiento con id: " + host_id +" o no" );
		} else {

			return returnHosting;

		}

	}
	// Devuelve todos los alojamientos que existen en la base de datos
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Hosting> getAllHostingsJSON() {

		Connection conn = (Connection) sc.getAttribute("dbConn");
		HostingDAO hostingDao = new JDBCHostingDAOImpl();
		hostingDao.setConnection(conn);

		List<Hosting> returnHostings = hostingDao.getAll();

		// Comprobar que almenos hay un alojamiento sino error
		if (returnHostings == null) {
			throw new CustomNotFoundException("No hay ningun alojamiento");
		} else {
			return returnHostings;
		}
	}

	// Devuelve todos los alojamientos de la base de datos propios
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Hosting> getHostingsJSON() {

		Connection conn = (Connection) sc.getAttribute("dbConn");
		HostingDAO hostingDao = new JDBCHostingDAOImpl();
		hostingDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		List<Hosting> returnHostings = hostingDao.getAllByUser(user.getId());

		// Comprobar que almenos hay un alojamiento sino error
		if (returnHostings.size() == 0) {
			throw new CustomNotFoundException("No tienes ningun alojamiento");
		} else {
			return returnHostings;
		}
	}

	// *****************************
	// Devuelve los alojamientos de un usuario con sus categorias
	@GET
	@Path("/myHosts/categories")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<Hosting, List<Category>> getMyHostJSON() {

		Connection conn = (Connection) sc.getAttribute("dbConn");
		HostingDAO hostingDao = new JDBCHostingDAOImpl();
		hostingDao.setConnection(conn);
		CategoryDAO categoryDAO = new JDBCCategoryDAOImpl();
		categoryDAO.setConnection(conn);
		HostingCategoriesDAO HostingsCategoriesDAO = new JDBCHostingCategoriesDAOImpl();
		HostingsCategoriesDAO.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		List<Hosting> Hosts = hostingDao.getAllByUser(user.getId());
		Map<Hosting, List<Category>> userHostingMap = new HashMap<Hosting, List<Category>>();

		for (Hosting hs : Hosts) {
			List<HostingCategories> HC = HostingsCategoriesDAO.getAllByHosting(hs.getId());
			List<Category> caux = new ArrayList<Category>();

			for (HostingCategories hc : HC) {
				caux.add(categoryDAO.get(hc.getIdct()));

			}
			userHostingMap.put(hs, caux);
		}

		// Comprobar que almenos hay un alojamiento sino error
		if (userHostingMap.size() == 0) {
			throw new CustomNotFoundException("No tienes ningun alojamiento");
		} else {
			return userHostingMap;
		}
	}

	// Devuelve los alojamientos de un usuario con sus servicios
	@GET
	@Path("/myHosts/services")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<Hosting, List<Service>> getMyHostServicesJSON() {

		Connection conn = (Connection) sc.getAttribute("dbConn");
		HostingDAO hostingDao = new JDBCHostingDAOImpl();
		hostingDao.setConnection(conn);
		ServiceDAO serviceDAO = new JDBCServiceDAOImpl();
		serviceDAO.setConnection(conn);
		HostingServicesDAO HostingsServicesDAO = new JDBCHostingServicesDAOImpl();
		HostingsServicesDAO.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		List<Hosting> Hosts = hostingDao.getAllByUser(user.getId());
		Map<Hosting, List<Service>> userHostingMap = new HashMap<Hosting, List<Service>>();

		for (Hosting hs : Hosts) {
			List<HostingServices> HS = HostingsServicesDAO.getAllByHosting(hs.getId());
			List<Service> caux = new ArrayList<Service>();

			for (HostingServices hc : HS) {
				caux.add(serviceDAO.get(hc.getIds()));

			}
			userHostingMap.put(hs, caux);
		}

		// Comprobar que almenos hay un alojamiento sino error
		if (userHostingMap.size() == 0) {
			throw new CustomNotFoundException("No tienes ningun alojamiento");
		} else {
			return userHostingMap;
		}
	}

	

	// Crea un alojamiento en la base de datos
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Long createHosting(Hosting hosting) {

		Connection conn = (Connection) sc.getAttribute("dbConn");
		HostingDAO hostingDao = new JDBCHostingDAOImpl();
		hostingDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		hosting.setIdu((int) user.getId());
		Long hostID = hostingDao.add(hosting);

		return hostID;
	}

	// Actualiza un alojamiento
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateHosting(@PathParam("id") Long id, Hosting hosting) {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		HostingDAO hostingDao = new JDBCHostingDAOImpl();
		hostingDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// Comprobar que el alojamiento es del usuario para
		// que pueda ser actualizado
		if (hostingDao.get(id).getIdu() == user.getId()) {
			hosting.setId(id);
			hosting.setIdu((int) user.getId());
			hostingDao.save(hosting);
		} else {
			throw new CustomNotFoundException("No tienes permiso para actualizar este alojamiento");
		}
		Response resp = Response
				.created(uriInfo.getAbsolutePathBuilder()
						.path(Long.toString(id))
						.build())
				.contentLocation(uriInfo.getAbsolutePathBuilder()
						.path(Long.toString(id))
						.build())
				.build();
		return resp;
	}

	// Borrar un alojamiento de la base de datos
	@DELETE
	@Path("/{host_id: [0-9]+}")
	public Response deleteHosting(@PathParam("host_id") long host_id) {

		Connection conn = (Connection) sc.getAttribute("dbConn");
		HostingDAO hostingDao = new JDBCHostingDAOImpl();
		hostingDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		Hosting returnHosting = hostingDao.get(host_id);
		// Comprobar que el alojamiento pertence al usuario logeado y si es asi,
		// borrarlo
		if (returnHosting == null) {
			throw new CustomNotFoundException("No existe el alojamiento con id: " + host_id);
		} else {
			if (returnHosting.getIdu() != user.getId()) {
				throw new CustomNotFoundException("No tienes permisos para borrar este alojamiento");
			} else {
				hostingDao.delete(host_id);
				return Response.noContent().build();
			}
		}
	}

}