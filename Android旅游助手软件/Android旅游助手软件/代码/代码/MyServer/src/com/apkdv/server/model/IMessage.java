package com.apkdv.server.model;

/**
 * user�Ĵ�����
 * @author LengYue
 *
 */
public interface IMessage {
	public void addMessage(String username,String msg);
	public String findMessage(String userName);
}
