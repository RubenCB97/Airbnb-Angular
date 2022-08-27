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
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import es.unex.giiis.pi.resources.exceptions.CustomBadRequestException;
import es.unex.giiis.pi.resources.exceptions.CustomNotFoundException;
import es.unex.pi.dao.JDBCHostingDAOImpl;
import es.unex.pi.dao.JDBCHostingServicesDAOImpl;
import es.unex.pi.dao.ServiceDAO;
import es.unex.pi.dao.HostingDAO;
import es.unex.pi.dao.HostingServicesDAO;
import es.unex.pi.dao.JDBCServiceDAOImpl;
import es.unex.pi.model.Service;
import es.unex.pi.model.Hosting;
import es.unex.pi.model.HostingServices;
import es.unex.pi.model.User;

@Path("/hostServices")
public class HostServiceResource {

    @Context
    HttpServletRequest request;
    @Context
    ServletContext sc;
    @Context
    UriInfo uriInfo;

    // Devuelve los servicios de un alojamiento
    @GET
    @Path("/{hostingId: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Service> getServices(@PathParam("hostingId") int hostingId) {

        Connection conn = (Connection) sc.getAttribute("dbConn");
        HostingServicesDAO Hs = new JDBCHostingServicesDAOImpl();
        Hs.setConnection(conn);
        ServiceDAO s = new JDBCServiceDAOImpl();
        s.setConnection(conn);
        HostingDAO Host = new JDBCHostingDAOImpl();
        Host.setConnection(conn);

        Hosting host = Host.get(hostingId);

        if (host == null)
            throw new CustomNotFoundException("Host error");

        List<HostingServices> listHS = Hs.getAllByHosting(hostingId);

        List<Service> allSer = s.getAll();
        List<Service> result = new ArrayList<Service>();

        for (HostingServices hostingServices : listHS) {
            for (Service service : allSer) {
                if (hostingServices.getIds() == service.getId())
                    result.add(service);
            }
        }

        return result;

    }

    // Borra una service de un alojamiento
    @DELETE
    @Path("/{hostingId: [0-9]+}/{serviceId: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteService(@PathParam("hostingId") int hostingId, @PathParam("serviceId") int serviceId) {

        Connection conn = (Connection) sc.getAttribute("dbConn");
        HostingServicesDAO Hs = new JDBCHostingServicesDAOImpl();
        Hs.setConnection(conn);
        ServiceDAO s = new JDBCServiceDAOImpl();
        s.setConnection(conn);

        List<HostingServices> listHS = Hs.getAllByHosting(hostingId);

        for (HostingServices hostingServices : listHS) {
            if (hostingServices.getIds() == serviceId) {
                Hs.delete(hostingId, serviceId);
                return Response.noContent().build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }

    // Añade un servicio al alojamiento
    @POST
    @Path("/{hostingId: [0-9]+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createService(@PathParam("hostingId") long hostingId, ArrayList<Service> service) {

        Connection conn = (Connection) sc.getAttribute("dbConn");
        HostingDAO Host = new JDBCHostingDAOImpl();
        Host.setConnection(conn);
        HostingServicesDAO Hs = new JDBCHostingServicesDAOImpl();
        Hs.setConnection(conn);
        ServiceDAO s = new JDBCServiceDAOImpl();
        s.setConnection(conn);

        List<Service> allServices = s.getAll();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        Hosting hosting = Host.get(hostingId);

        if (user == null)
            throw new CustomBadRequestException("No estas logeado");

        if (hosting == null)
            throw new CustomNotFoundException("Hosting not found");

        if (hosting.getIdu() != user.getId())
            throw new CustomBadRequestException("You can't add a service to a hosting that you don't own");
        long v= 0;
        
        for (Service allServ : allServices) {
        	Hs.delete(hostingId, allServ.getId());
			HostingServices h = new HostingServices();
			if(service!=null) {
				for (Service serv : service) {
					if(allServ.getId() == serv.getId()){
							h.setIds(allServ.getId());
							h.setIdh(hosting.getId());
							Hs.add(h);
					}
				}
			}
        }

        Response response = Response
                .created(
                        uriInfo.getAbsolutePathBuilder()
                                .path(Long.toString(v))
                                .build())
                .contentLocation(
                        uriInfo.getAbsolutePathBuilder()
                                .path(Long.toString(v))
                                .build())
                .build();
        return response;
    }

}
