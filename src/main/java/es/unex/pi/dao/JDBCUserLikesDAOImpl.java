package es.unex.pi.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.unex.pi.model.UserLikes;

public class JDBCUserLikesDAOImpl implements UserLikesDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCUserLikesDAOImpl.class.getName());

	@Override
	public List<UserLikes> getAll() {

		if (conn == null) return null;
						
		ArrayList<UserLikes> UserLikesList = new ArrayList<UserLikes>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM userlikes");
						
			while ( rs.next() ) {
				UserLikes UserLikes = new UserLikes();
				UserLikes.setIdh(rs.getInt("idh"));
				UserLikes.setIdu(rs.getInt("idu"));
						
				UserLikesList.add(UserLikes);
				logger.info("fetching all userlikes: "+UserLikes.getIdh()+" "+UserLikes.getIdu());
					
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return UserLikesList;
	}

	@Override
	public List<UserLikes> getAllByUser(long idu) {
		
		if (conn == null) return null;
						
		ArrayList<UserLikes> UserLikesList = new ArrayList<UserLikes>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM userlikes WHERE idu="+idu);

			while ( rs.next() ) {
				UserLikes UserLikes = new UserLikes();
				UserLikes.setIdh(rs.getInt("idh"));
				UserLikes.setIdu(rs.getInt("idu"));

				UserLikesList.add(UserLikes);
				logger.info("fetching all UserLikes by idu: "+UserLikes.getIdh()+" "+UserLikes.getIdu());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return UserLikesList;
	}
	
	@Override
	public List<UserLikes> getAllByHosting(long idh) {
		
		if (conn == null) return null;
						
		ArrayList<UserLikes> UserLikesList = new ArrayList<UserLikes>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM userlikes WHERE Idh="+idh);

			while ( rs.next() ) {
				UserLikes UserLikes = new UserLikes();
				UserLikes.setIdh(rs.getInt("idh"));
				UserLikes.setIdu(rs.getInt("idu"));
							
				UserLikesList.add(UserLikes);
				logger.info("fetching all UserLikes by idu: "+UserLikes.getIdh()+" "+UserLikes.getIdu());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return UserLikesList;
	}
	
	
	@Override
	public UserLikes get(long idh,long idu) {
		if (conn == null) return null;
		
		UserLikes UserLikes = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UserLikes WHERE Idh="+idh+" AND idu="+idu);			 
			if (!rs.next()) return null;
			UserLikes= new UserLikes();
			UserLikes.setIdh(rs.getInt("idh"));
			UserLikes.setIdu(rs.getInt("idu"));

			logger.info("fetching UserLikes by idh: "+UserLikes.getIdh()+"  and idct: "+UserLikes.getIdu());
		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return UserLikes;
	}
	
	

	@Override
	public boolean add(UserLikes UserLikes) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO UserLikes (idh,idu) VALUES("+
						UserLikes.getIdh()+","+
						UserLikes.getIdu()+")");
						
				logger.info("creating UserLikes:"+UserLikes.getIdh()+" "+UserLikes.getIdu());
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean save(UserLikes UserLikes) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				
				ResultSet rs = stmt.executeQuery("SELECT * FROM UserLikes WHERE idh="+UserLikes.getIdh()+" AND idu="+UserLikes.getIdu());			 
				if (!rs.next()) 
					return false;

				stmt.executeUpdate("UPDATE UserLikes SET idu="+UserLikes.getIdu()
				+" WHERE idh = "+UserLikes.getIdh() + " AND idu = " + UserLikes.getIdu());
				
				logger.info("updating UserLikes:"+UserLikes.getIdh()+" "+UserLikes.getIdu());
				
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean delete(long idh, long idu) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM UserLikes WHERE idh ="+idh+" AND idu="+idu);
				logger.info("deleting UserLikes: "+idh+" , idu="+idu);
				done= true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public void setConnection(Connection conn) {
		// TODO Auto-generated method stub
		this.conn = conn;
	}
	
}