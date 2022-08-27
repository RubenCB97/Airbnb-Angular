package airbnb.resources;

import java.sql.Connection;
import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import es.unex.giiis.pi.resources.exceptions.CustomBadRequestException;
import es.unex.giiis.pi.resources.exceptions.CustomNotFoundException;
import es.unex.pi.dao.ServiceDAO;
import es.unex.pi.dao.JDBCServiceDAOImpl;
import es.unex.pi.model.Service;

@Path("/services")
public class ServiceResource {
	
	@Context 
	HttpServletRequest request;
	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;

	//Devuelve la lista de todas las categorias
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Service> getServices() {

		Connection conn = (Connection) sc.getAttribute("dbConn");
		ServiceDAO s = new JDBCServiceDAOImpl();
		s.setConnection(conn);
		List<Service> services = s.getAll();
		return services;
	
	}
	
	//Crear una categoria por json
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createService(Service service) {
		
		Connection conn = (Connection) sc.getAttribute("dbConn");
		ServiceDAO s = new JDBCServiceDAOImpl();
		s.setConnection(conn);
		
		if(s.get(service.getId()) != null || s.get(service.getName()) != null) {
			throw new CustomBadRequestException("Service already exists");
		}
		
		s.add(service);
		
		Response resp = Response
				  .created(uriInfo.getAbsolutePathBuilder()
						  		  .path(Long.toString(service.getId()))
						  		  .build())
				  .contentLocation(uriInfo.getAbsolutePathBuilder()
				  		  .path(Long.toString(service.getId()))
				  		  .build())
				  .build();
		  return resp;
		
	}

	//Borra una categoria por id
	@DELETE
	@Path("/{id: [0-9]+}")
	public Response deleteService(@PathParam("id") int id) {
		
		Connection conn = (Connection) sc.getAttribute("dbConn");
		ServiceDAO s = new JDBCServiceDAOImpl();
		s.setConnection(conn);
		
		if(s.get(id) == null) {
			throw new CustomNotFoundException("Service not found");
		}
		
		s.delete(id);
		
		return Response.noContent().build(); //204 no content 
		
	}

		 
}