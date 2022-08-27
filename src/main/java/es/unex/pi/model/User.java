package es.unex.pi.model;

import java.util.Map;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
	
	private long id;
	private String username;
	private String email;
	private String password;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean validate (Map<String,String> messages) {
		if(username.trim().isEmpty() || username ==null || !username.matches("[A-Za-z]{1,15}")) {
			messages.put("error", "nombre vacio");
		}else if (email.trim().isEmpty() || email==null || !email.matches("[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-zA-Z]{2,4}")){
			messages.put("error","email vacio");
		}else if (!password.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}")) {
			messages.put("error","password incorrecta");
		} 
		if(messages.isEmpty()) {
			return true;
		}else {
			return false;
		}
			
	}
	
	
}
