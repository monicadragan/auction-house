package network;

import java.nio.channels.SelectionKey;

import javax.swing.table.DefaultTableModel;

import types.UserPublicInfo;
import types.UserType;

public class ClientInformation {
	
	UserPublicInfo userInfo;
	DefaultTableModel tableModel;
	public SelectionKey key;
	
	public ClientInformation(UserPublicInfo userInfo, DefaultTableModel tableModel, SelectionKey key)
	{
		this.userInfo = userInfo;
		this.tableModel = tableModel;
		this.key = key;
	}
	
	public String toString()
	{
		return userInfo.toString();
	}
	
	public DefaultTableModel getModel()
	{
		return tableModel;
	}

	public UserType getUType()
	{
		return this.userInfo.uType;
	}
	
	public String getUsername()
	{
		return userInfo.username;
	}
}
