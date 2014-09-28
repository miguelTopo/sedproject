package co.edu.udistrital.core.login.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.edu.udistrital.core.login.controller.LoginBean;

public class LoginFilter implements Filter {


	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;

			LoginBean session = (LoginBean) req.getSession().getAttribute("loginBean");

			String url = req.getRequestURI();

			if (session == null || !session.isValidLogin()) {
				if (url.contains("css"))
					chain.doFilter(request, response);
				else if (!url.endsWith("login") && !url.endsWith("recuperar")) {
					res.sendRedirect(req.getServletContext().getContextPath() + "/portal/login");
				} else {
					chain.doFilter(request, response);
				}
			} else {
				if (url.contains("css"))
					chain.doFilter(request, response);
				else if (url.endsWith("login") || url.endsWith("recuperar")) {
					res.sendRedirect(req.getServletContext().getContextPath() + "/portal/menu");
				} else {
					chain.doFilter(request, response);
				}
			}



		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init(FilterConfig arg0) throws ServletException {

	}


}
