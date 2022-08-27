package es.unex.pi.dao;

import java.sql.Connection;
import java.util.List;

import es.unex.pi.model.Service;


public interface ServiceDAO {

	/**
	 * set the database connection in this DAO.
	 * 
	 * @param conn
	 *            database connection.
	 */
	public void setConnection(Connection conn);
	
	/**
	 * Gets a Service from the DB using id.
	 * 
	 * @param id
	 *            Service Identifier.
	 * 
	 * @return Service object with that id.
	 */
	public Service get(long id);

	/**
	 * Gets a Service from the DB using name.
	 * 
	 * @param name
	 *            Service name.
	 * 
	 * @return Service object with that name.
	 */
	public Service get(String name);

	
	/**
	 * Gets all the categories from the database.
	 * 
	 * @return List of all the categories from the database.
	 */
	public List<Service> getAll();
	
	/**
	 * Gets all the categories from the database that contain a text in the name.
	 * 
	 * @param search
	 *            Search string .
	 * 
	 * @return List of all the categories from the database that contain a text in the name.
	 */	
	public List<Service> getAllBySearchName(String search);


	/**
	 * Adds a Service to the database.
	 * 
	 * @param Service
	 *            Service object with the Service details.
	 * 
	 * @return Service identifier or -1 in case the operation failed.
	 */
	
	public long add(Service service);

	/**
	 * Updates an existing Service in the database.
	 * 
	 * @param Service
	 *            Service object with the new details of the existing Service.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	
	
	public boolean save(Service service);

	/**
	 * Deletes a Service from the database.
	 * 
	 * @param id
	 *            Service identifier.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	
	public boolean delete(long id);
}
