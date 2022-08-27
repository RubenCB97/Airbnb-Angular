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
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import es.unex.giiis.pi.resources.exceptions.CustomBadRequestException;
import es.unex.giiis.pi.resources.exceptions.CustomNotFoundException;
import es.unex.pi.dao.JDBCHostingDAOImpl;
import es.unex.pi.dao.UserLikesDAO;
import es.unex.pi.dao.JDBCUserLikesDAOImpl;
import es.unex.pi.model.UserLikes;
import es.unex.pi.dao.HostingDAO;
import es.unex.pi.model.Hosting;
import es.unex.pi.model.User;

@Path("/userLikes")
public class UserLikesResource {

    @Context
    HttpServletRequest request;
    @Context
    ServletContext sc;
    @Context
    UriInfo uriInfo;

    // Obtener likes del usuario a un host
    @GET
    @Path("/{host_id: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public int getUserLikesJSON(@PathParam("host_id") long host_id) {

        Connection conn = (Connection) sc.getAttribute("dbConn");
        UserLikesDAO userLikesDao = new JDBCUserLikesDAOImpl();
        userLikesDao.setConnection(conn);
        User user = (User) request.getSession().getAttribute("user");
        int like = 0;
        UserLikes userLikes = userLikesDao.get(host_id, user.getId());
        // Obtener los likes del usuario al host_id

        if (userLikes == null) {
            return like;
        }

        return like = 1;
    }

    // Devuelve los servicios de un alojamiento
    // Suma 1 like al alojamiento
    @POST
    @Path("/{host_id: [0-9]+}")
    public Response addUserLike(@PathParam("host_id") long host_id) {

        Connection conn = (Connection) sc.getAttribute("dbConn");
        UserLikesDAO userLikesDao = new JDBCUserLikesDAOImpl();
        userLikesDao.setConnection(conn);
        HostingDAO hostingDao = new JDBCHostingDAOImpl();
        hostingDao.setConnection(conn);
        Hosting hosting = hostingDao.get(host_id);
        UserLikes userLikes = new UserLikes();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomBadRequestException("No user logged");
        }
        if (hosting == null) {
            throw new CustomNotFoundException("Hosting not found");
        }
        if (userLikesDao.get(host_id,user.getId()) != null) {
            throw new CustomBadRequestException("User already liked this hosting");
        }
        userLikes.setIdu(user.getId());
        userLikes.setIdh(host_id);
        userLikesDao.add(userLikes);

        // Aumentar en 1 el valor like de hosting
        hosting.setLikes(hosting.getLikes() + 1);
        hostingDao.save(hosting);

        return Response.status(Response.Status.CREATED).build();
    }

    // Resta 1 like del alojamiento
    @DELETE
    @Path("/{host_id: [0-9]+}")
    public Response removeUserLike(@PathParam("host_id") long host_id) {

        Connection conn = (Connection) sc.getAttribute("dbConn");
        UserLikesDAO userLikesDao = new JDBCUserLikesDAOImpl();
        userLikesDao.setConnection(conn);
        HostingDAO hostingDao = new JDBCHostingDAOImpl();
        hostingDao.setConnection(conn);
        Hosting hosting = hostingDao.get(host_id);
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomBadRequestException("No user logged");
        }
        if (hosting == null) {
            throw new CustomNotFoundException("Hosting not found");
        }
        if (userLikesDao.get(host_id, user.getId())==null) {
            throw new CustomBadRequestException("User not liked this hosting");
        }
        userLikesDao.delete(host_id, user.getId());

        // Restar en 1 los likes del hosting
        hosting.setLikes(hosting.getLikes() - 1);
        hostingDao.save(hosting);

        return Response.status(Response.Status.OK).build();
    }

}
