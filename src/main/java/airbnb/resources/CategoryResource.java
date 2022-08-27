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
import es.unex.pi.dao.CategoryDAO;
import es.unex.pi.dao.JDBCCategoryDAOImpl;
import es.unex.pi.model.Category;

@Path("/categories")
public class CategoryResource {
	
	@Context 
	HttpServletRequest request;
	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;

	//Devuelve la lista de todas las categorias
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Category> getCategories() {

		Connection conn = (Connection) sc.getAttribute("dbConn");
		CategoryDAO c = new JDBCCategoryDAOImpl();
		c.setConnection(conn);
		List<Category> categories = c.getAll();
		return categories;
	
	}
	
	//Crear una categoria por json
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCategory(Category category) {
		
		Connection conn = (Connection) sc.getAttribute("dbConn");
		CategoryDAO c = new JDBCCategoryDAOImpl();
		c.setConnection(conn);
		
		if(c.get(category.getId()) != null || c.get(category.getName()) != null) {
			throw new CustomBadRequestException("Category already exists");
		}
		
		c.add(category);
		
		Response resp = Response
				  .created(uriInfo.getAbsolutePathBuilder()
						  		  .path(Long.toString(category.getId()))
						  		  .build())
				  .contentLocation(uriInfo.getAbsolutePathBuilder()
				  		  .path(Long.toString(category.getId()))
				  		  .build())
				  .build();
		  return resp;
		
	}

	//Borra una categoria por id
	@DELETE
	@Path("/{id: [0-9]+}")
	public Response deleteCategory(@PathParam("id") int id) {
		
		Connection conn = (Connection) sc.getAttribute("dbConn");
		CategoryDAO c = new JDBCCategoryDAOImpl();
		c.setConnection(conn);
		
		Category cat = c.get(id);
		if(cat == null) {
			throw new CustomNotFoundException("Category not found");
		}
		
		c.delete(id);
		
		return Response.noContent().build(); //204 no content 
		
	}

		 
}