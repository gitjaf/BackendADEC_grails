package org.sigma.code

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse


class CORSFilter implements Filter {
	public void init(FilterConfig fConfig) throws ServletException { }

	public void destroy() { }

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		response.addHeader('Access-Control-Allow-Origin', "*")
		response.addHeader('Access-Control-Allow-Methods', 'POST, PUT, GET, DELETE, OPTIONS, PATCH')
		response.addHeader('Access-Control-Allow-Headers', 'Origin, Content-Type, Accept' )
		response.addHeader('Access-Control-Allow-Credentials', 'true')
		response.addHeader('Access-Control-Max-Age', '1728000')
		
		chain.doFilter(request, response)
	}
}

