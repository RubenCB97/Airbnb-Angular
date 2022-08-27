package airbnb.resources;

import java.sql.Connection;
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
import es.unex.giiis.pi.resources.exceptions.CustomBadRequestException;
import es.unex.giiis.pi.resources.exceptions.CustomNotFoundException;
import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.User;

@Path("/users")
public class UsersResource {
	
	  @Context 
	  HttpServletRequest request;
	  @Context
	  ServletContext sc;
	  @Context
	  UriInfo uriInfo;
	  
  
	  // Recuperar un usuario
	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public User getUserJSON() 
	  {
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		User returnUser = null;
		if(user!=null)
			returnUser = userDao.get(user.getUsername());
		
		return returnUser; 

		}
	
	  //Borrar usuario
	  @DELETE
	  @Path("/{user_id: [0-9]+}")
	  public Response delete(@PathParam("user_id") long user_id) 
	  {
		  Connection conn = (Connection) sc.getAttribute("dbConn");
		  UserDAO userDao = new JDBCUserDAOImpl();
		  userDao.setConnection(conn);
		  
		  HttpSession session = request.getSession();
		  User user = (User) session.getAttribute("user");
		
		  if (user_id == user.getId()) {
			  userDao.delete(user_id);
			  session.removeAttribute("user");
			  
			  return Response.noContent().build(); //204 no content
			  
		  }else {
			  throw new CustomNotFoundException("Error user");		
		  }
		
	  }
	  
	  //Crear un usuario
	  @POST
	  @Consumes(MediaType.APPLICATION_JSON)
	  public Response createUserJSON (User createUser) throws Exception
	  {
		  Connection conn = (Connection) sc.getAttribute("dbConn");
		  UserDAO userDao = new JDBCUserDAOImpl();
		  userDao.setConnection(conn);
		  List<User> allUsers = userDao.getAll();
		  Response resp = null;
		  boolean emailexist = false;
		  
		  User auxName = (User) userDao.get(createUser.getUsername());
		  for (User us : allUsers) {
				if (us.getEmail().equals(createUser.getEmail())) {
					emailexist = true;	
				}
			}
		  if(auxName != null || emailexist==true) {
			  throw new CustomBadRequestException("Usuario ya registrado");
		  }else {
			  Map<String, String> messages = new HashMap<String, String>();
			  if(!createUser.validate(messages)) {
				  throw new CustomBadRequestException("Parametros erroneos: " + messages);
			  }else {
				  long userID = userDao.add(createUser);
				  
				  resp = Response
						  .created(uriInfo.getAbsolutePathBuilder()
								  		  .path(Long.toString(userID))
								  		  .build())
						  .contentLocation(uriInfo.getAbsolutePathBuilder()
						  		  .path(Long.toString(userID))
						  		  .build())
						  .build();
				  return resp;
				  
			  }
		  }
	  	}

		  //Actualizar un usuario logueado
		  @PUT
		  @Consumes(MediaType.APPLICATION_JSON)
		  public Response putUserJSON (User updateUser) throws Exception
		  {
			  Connection conn = (Connection) sc.getAttribute("dbConn");
			  UserDAO userDao = new JDBCUserDAOImpl();
			  userDao.setConnection(conn);
			  Response resp = null;
			  //Obtenemos el usuario de la session
			  HttpSession session = request.getSession();
			  User user = (User) session.getAttribute("user");

			  //Comprobar que el usuario a actualizar exista en la base de datos
			  User auxUser = (User) userDao.get(updateUser.getId());
			  if(auxUser == null) {
				  throw new CustomNotFoundException("Error user");
			  }else {
				  
				  if(updateUser.getPassword().equals("**********"))
					  updateUser.setPassword(auxUser.getPassword());
				  
				  //Comprobamos que el usuario que quiere actualizar es el mismo que esta logueado
				  if (user.getId() == updateUser.getId()) {
					  Map<String, String> messages = new HashMap<String, String>();
					  if(!updateUser.validate(messages)) {
						  throw new CustomBadRequestException("Parametros erroneos: " + messages);
					  }else {
						  userDao.save(updateUser);
						  
						  resp = Response
							  .created(uriInfo.getAbsolutePathBuilder()
									  		  .path(Long.toString(updateUser.getId()))
									  		  .build())
							  .contentLocation(uriInfo.getAbsolutePathBuilder()
							  		  .path(Long.toString(updateUser.getId()))
							  		  .build())
							  .build();
						  
					  }
					}else {					  
							throw new CustomNotFoundException("Usuario erroneo");
					}
			  }
			  return resp;
		  }

} 
