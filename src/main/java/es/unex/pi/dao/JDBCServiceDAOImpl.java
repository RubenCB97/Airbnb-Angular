package es.unex.pi.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.unex.pi.model.Service;


public class JDBCServiceDAOImpl implements ServiceDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCServiceDAOImpl.class.getName());
	
	@Override
	public Service get(long id) {
		if (conn == null) return null;
		
		Service service = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Service WHERE id ="+id);			 
			if (!rs.next()) return null; 
			service  = new Service();	 
			service.setId(rs.getInt("id"));
			service.setName(rs.getString("name"));
			
			logger.info("fetching Service by id: "+id+" -> "+service.getId()+" "+service.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return service;
	}
	
	
	@Override
	public Service get(String name) {
		if (conn == null) return null;
		
		Service service = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Service WHERE name = '"+name+"'");			 
			if (!rs.next()) return null; 
			service  = new Service();	 
			service.setId(rs.getInt("id"));
			service.setName(rs.getString("name"));
			
			logger.info("fetching Service by name: "+service.getId()+" "+service.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return service;
	}
	
	
	
	public List<Service> getAll() {

		if (conn == null) return null;
		
		ArrayList<Service> services = new ArrayList<Service>();
		try {
			Statement stmt;
			ResultSet rs;
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Service");
			while ( rs.next() ) {
				Service service = new Service();
				service.setId(rs.getInt("id"));
				service.setName(rs.getString("name"));
				
				services.add(service);
				logger.info("fetching Service: "+service.getId()+" "+service.getName());
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return services;
	}
	
	public List<Service> getAllBySearchName(String search) {
		search = search.toUpperCase();
		if (conn == null)
			return null;

		ArrayList<Service> services = new ArrayList<Service>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Service WHERE UPPER(name) LIKE '%" + search + "%'");

			while (rs.next()) {
				Service service = new Service();
				
				service.setId(rs.getInt("id"));
				service.setName(rs.getString("name"));
				
				services.add(service);
				
				logger.info("fetching Services by text in the name: "+search+": "+service.getId()+" "+service.getName());
				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return services;
	}
	

	@Override
	public long add(Service service) {
		long id=-1;
		long lastid=-1;
		if (conn != null){

			Statement stmt;
			
			
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_sequence WHERE name ='service'");			 
				if (!rs.next()) return -1; 
				lastid=rs.getInt("seq");
								
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO Service (name,description) VALUES('"
									+service.getName()+ "')");
				
								
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_sequence WHERE name ='service'");			 
				if (!rs.next()) return -1; 
				id=rs.getInt("seq");
				if (id<=lastid) return -1;
											
				logger.info("CREATING Service("+id+"): "+service.getName());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return id;
	}

	@Override
	public boolean save(Service service) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				
				stmt.executeUpdate("UPDATE Service SET name='"+service.getName()
				+ "' WHERE id = "+service.getId());
				
				logger.info("updating Service: "+service.getId()+" "+service.getName());
				
				done= true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean delete(long id) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM service WHERE id ="+id);
				
				logger.info("deleting service: "+id);
				
				done= true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	
}
