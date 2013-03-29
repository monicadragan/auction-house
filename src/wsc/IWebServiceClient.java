package wsc;

import types.User;
import types.UserType;

/**
 * Interfata pentru WebServiceClient
 */
public interface IWebServiceClient {

	public User readInfoAboutUser(String username, String passwd, UserType uType);

	public boolean checkUserInfo(String username, String passwd, UserType uType);

}
