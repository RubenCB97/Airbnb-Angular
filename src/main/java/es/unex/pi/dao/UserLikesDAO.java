package es.unex.pi.dao;

import java.sql.Connection;
import java.util.List;

import es.unex.pi.model.UserLikes;


public interface UserLikesDAO {

	/**
	 * set the database connection in this DAO.
	 * 
	 * @param conn
	 *            database connection.
	 */
	public void setConnection(Connection conn);

	/**
	 * Gets all the hosting and the categories related to them from the database.
	 * 
	 * @return List of all the hosting and the categories related to them from the database.
	 */
	
	public List<UserLikes> getAll();

	/**
	 *Gets all the HostingUser that are related to a User.
	 * 
	 * @param idu
	 *            User identifier
	 * 
	 * @return List of all the HostingUser related to a User.
	 */
	public List<UserLikes> getAllByUser(long idu);
	
	/**
	 * Gets all the HostingUser that contains an specific hosting.
	 * 
	 * @param idh
	 *            Hosting Identifier
	 * 
	 * @return List of all the HostingUser that contains an specific hosting
	 */
	public List<UserLikes> getAllByHosting(long idh);

	/**
	 * Gets a HostingUser from the DB using idh and idu.
	 * 
	 * @param idr 
	 *            hosting identifier.
	 *            
	 * @param idu
	 *            User Identifier
	 * 
	 * @return HostingUser with that idh and idu.
	 */
	
	public UserLikes get(long idh,long idu);

	/**
	 * Adds an HostingUser to the database.
	 * 
	 * @param HostingUser
	 *            HostingUser object with the details of the relation between the hosting and the User.
	 * 
	 * @return hosting identifier or -1 in case the operation failed.
	 */
	
	public boolean add(UserLikes userLikes);

	/**
	 * Updates an existing HostingUser in the database.
	 * 
	 * @param HostingUser
	 *            HostingUser object with the new details of the existing relation between the hosting and the User. 
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	
	public boolean save(UserLikes userLikes);

	/**
	 * Deletes an HostingUser from the database.
	 * 
	 * @param idh
	 *            Hosting identifier.
	 *            
	 * @param idu
	 *            User Identifier
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	
	public boolean delete(long idh, long idu);
}