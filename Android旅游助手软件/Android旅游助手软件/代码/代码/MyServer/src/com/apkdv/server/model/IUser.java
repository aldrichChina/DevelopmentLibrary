package com.apkdv.server.model;

/**
 * user的处理方法
 * @author LengYue
 *
 */
public interface IUser {
	public boolean addUser(String username,String password,String myName,String phone);
	public String login(String name,String password);
	public void upData(String string);
}
