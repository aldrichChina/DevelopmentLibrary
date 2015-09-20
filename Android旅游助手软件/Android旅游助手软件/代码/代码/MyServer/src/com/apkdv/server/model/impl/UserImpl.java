package com.apkdv.server.model.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.apkdv.server.entity.User;
import com.apkdv.server.model.IUser;
import com.apkdv.server.utils.HibernateUtil;
import com.apkdv.server.utils.Tools;




public class UserImpl implements IUser {
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean addUser(String userString,String password,String myName,String phone) {
		User user = new User();
		user.setUserName(userString);
		user.setPassword(password);
		user.setMyName(myName);
		user.setPhone(phone);
		Session s = null;
		try { 
			s = HibernateUtil.getSession();
			Criteria criteria = s.createCriteria(User.class);
			criteria.add(Restrictions.eq("userName", userString));
			//只查询出一条信息，如果有多条则会异常
			List<User> users= criteria.list();
			if (users !=null && !users.isEmpty()) {
				return false;
			}else {
				if (HibernateUtil.add(user) !=0) {
					return true;
				}else{
					return false;
				}
			}
		}finally{
			if (s!=null) {
				s.close();
			}
		}
		
	}

	//criteria 查询
	@Override
	public String  login(String name, String password) {
		Session s = null;
		try { 
			s = HibernateUtil.getSession();
			Criteria criteria = s.createCriteria(User.class);
			criteria.add(Restrictions.eq("userName", name));
			criteria.add(Restrictions.eq("password", password));
			//只查询出一条信息，如果有多条则会异常
			User user = (User)criteria.uniqueResult();
			if (user !=null) {
				return Tools.gson.toJson(user);
			}else {
				return null;
			}
		}finally{
			if (s!=null) {
				s.close();
			}
		}
	}

	@Override
	public void upData(String string) {
		User user = Tools.gson.fromJson(string, User.class);
		System.out.println(user.toString());
		HibernateUtil.update(user);
		
	}

}
