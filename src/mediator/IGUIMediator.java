package mediator;
import java.util.ArrayList;

import types.User;
import types.UserType;
import gui.MainWindow;
import gui.TableView;


public interface IGUIMediator {
	
	public void sendRequest(String msg, int tableRow, int tableCol, TableView userPanel);
	public User readUserInformation(String username, String password, UserType uType);

	public void sendFile(MainWindow source, MainWindow destination, int sourceRow, int destRow);

	public ArrayList<UserThread> getUsers();
	public void setUsers(ArrayList<UserThread> users);


}
