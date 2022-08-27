package filter;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(dispatcherTypes = {DispatcherType.REQUEST}, urlPatterns = {"/waterbnb/*","/pages/*", "/rest/*","/user/*","/user/hosting/*"})
public class LoginFilter implements Filter{
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());

	private FilterConfig fc;
	
	public void init (FilterConfig fc) {

		//TODO complete the code here


	}
	
	public void doFilter(ServletRequest request,
						ServletResponse response,
						FilterChain chain) throws IOException, ServletException {
		
	logger.info("LoginFilter--");
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		HttpSession session = ((HttpServletRequest) request).getSession(true);
		
		if (session.getAttribute("user") == null) { //Usuario debe iniciar sesion para acceder
			res.sendRedirect(req.getContextPath() + "/LoginUserServlet");
		} 
		else { //Usuario ya ha iniciado sesion y puede acceder
			chain.doFilter(request, response);
		}
	}
	public void destroy() {
		// do the cleanup staff
	}
	
}
