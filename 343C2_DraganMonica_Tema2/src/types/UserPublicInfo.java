package types;

import java.io.Serializable;

public class UserPublicInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	public String username;
	public UserType uType;

	public UserPublicInfo(String uName, UserType uType) {
		this.username = uName;
		this.uType = uType;
	}
	
	public String toString()
	{
		return username + " :: " + uType;
	}
}
