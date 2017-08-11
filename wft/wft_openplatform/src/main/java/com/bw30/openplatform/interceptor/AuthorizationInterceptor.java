package com.bw30.openplatform.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.bw30.openplatform.global.Authorization;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AuthorizationInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String action = invocation.getInvocationContext().getName();
		String authorization = this.checkAuthorization(action); //判断权限
		if (session.getAttribute("userData") == null && (authorization == null || Integer.parseInt(authorization) == 1)){
			return "loginfail";
		}
		return invocation.invoke();
	}

	private String checkAuthorization(String name){
		Map<String, String> authStrMap = Authorization.getStrMap();
		Map<String, String> authObsMap = Authorization.getObsMap();
		String auth = "1";//用户权限默认为1：做权限判断
		
		String head = null;
		String[] strs = name.split("_");
		if(strs != null && strs.length > 0){
			head = strs[0];
		}
		if(authStrMap != null && authStrMap.size() > 0 && authStrMap.containsKey(name)){
			//获取action权限标识
			auth = authStrMap.get(name);
		}else if(authObsMap != null && authObsMap.size() > 0 && head != null && authObsMap.containsKey(head)){
			auth = authObsMap.get(head);
		}
		return auth;
	}
}
