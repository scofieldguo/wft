package com.bw30.open.wftAdm.interceptor;

import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bw30.open.common.model.login.WftAdminUser;
import com.bw30.open.wftAdm.service.IUserService;


public class PermissionInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOG = Logger.getLogger(PermissionInterceptor.class);
	
	private Set<String> excludeUris;
	@Resource
	private IUserService userService;
	
	public void setExcludeUris(Set<String> excludeUris) {
		this.excludeUris = excludeUris;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		String permissionUri = "",api="";
		String p = "[\\/\\.]";
		String[] ss = uri.split(p);
		if(ss.length>1){
			permissionUri = ss[ss.length-2];
			api=ss[ss.length-3];
			System.out.println("URL===="+api);
		}
		if(excludeUris.contains(permissionUri)||excludeUris.contains(api)){
			return true;
		}else{
			HttpSession session = request.getSession(true);		
			Object user = session.getAttribute("user");
			if(null != user){
				WftAdminUser userInfo = (WftAdminUser)user;
				if(userService.isUser(userInfo)){
				return true;
				}
//				LOG.warn(String.format("no permission[%s] for user[%s]", permissionUri, userInfo.getLoginName()));
				response.sendRedirect("errorpm.do");
				return false;
			}else{
//				LOG.warn(String.format("unlogin to req[%s]", permissionUri));
				response.sendRedirect("toLogin.do");
				return false;
			}
		}
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
}
