package gui;

import types.UserType;

public interface IMainWindow {

	public void initWindow();
	public void loginRequest(String username, String password, UserType uType);

}
