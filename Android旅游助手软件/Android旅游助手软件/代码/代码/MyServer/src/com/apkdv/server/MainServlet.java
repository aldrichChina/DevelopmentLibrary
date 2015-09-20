package com.apkdv.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.phprpc.PHPRPC_Server;

import com.apkdv.server.model.IMessage;
import com.apkdv.server.model.IUser;
import com.apkdv.server.model.impl.MessageImpl;
import com.apkdv.server.model.impl.UserImpl;


public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println(request.getRemoteAddr()+"¡¡¡¡·ÃÎÊ·þÎñÆ÷");
		if (request.getRequestURI()
				.substring(request.getRequestURI().lastIndexOf("/") + 1)
				.equals("mobile")) {
			mobile(request,response);
		}
		
	}
	
	private void mobile(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = new UserImpl();
			IMessage iMessage = new MessageImpl();
			PHPRPC_Server server = new PHPRPC_Server();
			server.add(user);
			server.add(iMessage);
			server.start(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
