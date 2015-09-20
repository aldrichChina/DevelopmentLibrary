package com.apkdv.server.model;

/**
 * user的处理方法
 * @author LengYue
 *
 */
public interface IMessage {
	public void addMessage(String username,String msg);
	public String findMessage(String userName);
}
