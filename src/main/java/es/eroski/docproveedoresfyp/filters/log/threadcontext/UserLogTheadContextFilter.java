package es.eroski.docproveedoresfyp.filters.log.threadcontext;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.sun.security.auth.UserPrincipal;

@Component
public class UserLogTheadContextFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		ThreadContext.put("iporigen", req.getRemoteAddr());
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof UserPrincipal) {
				ThreadContext.put("userLogin", ((UserPrincipal) authentication.getPrincipal()).getName());
			} else if (authentication.getPrincipal() instanceof User){
				ThreadContext.put("userLogin", ((User) authentication.getPrincipal()).getUsername()); 
			} else{
				ThreadContext.put("userLogin", (String) authentication.getPrincipal());
			}
		}
		try {
			chain.doFilter(req, resp);
		} finally {
			if (authentication != null) {
				ThreadContext.remove("userLogin");
				ThreadContext.remove("iporigen");
			}
		}
	}

	@Override
	public void destroy() {
	}

}
