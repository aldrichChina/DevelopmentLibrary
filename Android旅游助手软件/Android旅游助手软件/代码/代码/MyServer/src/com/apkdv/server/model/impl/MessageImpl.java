package com.apkdv.server.model.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;



import com.apkdv.server.entity.Message;
import com.apkdv.server.model.IMessage;
import com.apkdv.server.utils.HibernateUtil;
import com.apkdv.server.utils.Tools;

public class MessageImpl implements IMessage {

	@Override
	public void addMessage(String username, String mge) {
		Message message = new Message();
		message.setMessage(mge);
		message.setUserName(username);
		System.out.println(message.toString());
		HibernateUtil.add(message);
	}

	@Override
	public String findMessage(String userName) {
		Session s = null;
		try { 
			s = HibernateUtil.getSession();
			Criteria criteria = s.createCriteria(Message.class);
			criteria.add(Restrictions.eq("userName", userName));
			//只查询出一条信息，如果有多条则会异常
			List<Message> messages = criteria.list();
			if (messages !=null && !messages.isEmpty()) {
				return Tools.gson.toJson(messages);
			}else {
				return null;
			}
		}finally{
			if (s!=null) {
				s.close();
			}
		}
	}

}
