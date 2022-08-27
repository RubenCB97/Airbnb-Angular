package airbnb.resources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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
import es.unex.pi.dao.JDBCHostingDAOImpl;
import es.unex.pi.dao.CategoryDAO;
import es.unex.pi.dao.HostingCategoriesDAO;
import es.unex.pi.dao.HostingDAO;
import es.unex.pi.dao.JDBCCategoryDAOImpl;
import es.unex.pi.dao.JDBCHostingCategoriesDAOImpl;
import es.unex.pi.model.Category;
import es.unex.pi.model.Hosting;
import es.unex.pi.model.HostingCategories;
import es.unex.pi.model.User;

@Path("/hostCategories")
public class HostCategoryResource {

    @Context
    HttpServletRequest request;
    @Context
    ServletContext sc;
    @Context
    UriInfo uriInfo;

    // Devuelve las categorias de un alojamiento
    @GET
    @Path("/{hostingId: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> getCategories(@PathParam("hostingId") int hostingId) {

        Connection conn = (Connection) sc.getAttribute("dbConn");
        HostingCategoriesDAO Hc = new JDBCHostingCategoriesDAOImpl();
        Hc.setConnection(conn);
        CategoryDAO c = new JDBCCategoryDAOImpl();
        c.setConnection(conn);

        HostingDAO Host = new JDBCHostingDAOImpl();
        Host.setConnection(conn);

        Hosting host = Host.get(hostingId);

        if (host == null)
            throw new CustomNotFoundException("Host error");

        List<HostingCategories> listHC = Hc.getAllByHosting(hostingId);

        List<Category> allCat = c.getAll();
        List<Category> result = new ArrayList<Category>();

        for (HostingCategories hostingCategories : listHC) {
            for (Category category : allCat) {
                if (hostingCategories.getIdct() == category.getId())
                    result.add(category);
            }
        }

        return result;

    }

    // Borra una cateogria de un alojamiento
    @DELETE
    @Path("/{hostingId: [0-9]+}/{categoryId: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategory(@PathParam("hostingId") int hostingId, @PathParam("categoryId") int categoryId) {

        Connection conn = (Connection) sc.getAttribute("dbConn");
        HostingCategoriesDAO Hc = new JDBCHostingCategoriesDAOImpl();
        Hc.setConnection(conn);
        CategoryDAO c = new JDBCCategoryDAOImpl();
        c.setConnection(conn);

        List<HostingCategories> listHC = Hc.getAllByHosting(hostingId);

        for (HostingCategories hostingCategories : listHC) {
            if (hostingCategories.getIdct() == categoryId) {
                Hc.delete(hostingId, categoryId);
                return Response.noContent().build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }

    // actualizar las categorias de un alojamiento
    @PUT
    @Path("/{hostingId: [0-9]+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCategory(@PathParam("hostingId") int hostingId,
            Category category) {

        Connection conn = (Connection) sc.getAttribute("dbConn");
        HostingCategoriesDAO Hc = new JDBCHostingCategoriesDAOImpl();
        Hc.setConnection(conn);
        CategoryDAO c = new JDBCCategoryDAOImpl();
        c.setConnection(conn);

        List<HostingCategories> listHC = Hc.getAllByHosting(hostingId);

        for (HostingCategories hostingCategories : listHC) {
            if (hostingCategories.getIdct() == category.getId()) {
                Hc.save(hostingCategories);
                return Response.noContent().build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }

    // Añade una categoria a un alojamiento
    @POST
    @Path("/{hostingId: [0-9]+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCategory(@PathParam("hostingId") int hostingId, ArrayList <Category> category) {

        Connection conn = (Connection) sc.getAttribute("dbConn");
        HostingCategoriesDAO Hc = new JDBCHostingCategoriesDAOImpl();
        Hc.setConnection(conn);
        HostingDAO Host = new JDBCHostingDAOImpl();
        Host.setConnection(conn);
        CategoryDAO categoryDAO = new JDBCCategoryDAOImpl();
		categoryDAO.setConnection(conn);
		List<Category> allCategory = categoryDAO.getAll();

        
		

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Hosting hosting = Host.get(hostingId);
        long v= 0;
        if (user == null)
            throw new CustomBadRequestException("No estas logeado");

        if (hosting == null)
            throw new CustomNotFoundException("Hosting not found");

        if (hosting.getIdu() != user.getId())
            throw new CustomBadRequestException("You can't add a service to a hosting that you don't own");

      
        for (Category cat : allCategory) {
			Hc.delete(hostingId, cat.getId());
			HostingCategories h = new HostingCategories();
			if(category!=null) {
				for (Category caHost : category) {
					if(cat.getId() == caHost.getId()){
							h.setIdct(cat.getId());
							h.setIdh(hosting.getId());
							Hc.add(h);
					}
				}
			}
        }


		Response resp = Response
				  .created(uriInfo.getAbsolutePathBuilder()
						  		  .path(Long.toString(v))
						  		  .build())
				  .contentLocation(uriInfo.getAbsolutePathBuilder()
				  		  .path(Long.toString(v))
				  		  .build())
				  .build();
		  return resp;

    }

}
