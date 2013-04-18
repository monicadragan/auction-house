package mediator;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import types.User;
import types.UserType;
import gui.MainWindow;
import gui.TableView;

/**
 * Interfata modului Mediator pentru GUI
 *
 */
public interface IGUIMediator {
		
	public void sendRequest(String msg, int tableRow, int tableCol, TableView userPanel);
	public User readUserInformation(String username, String password, UserType uType);
	public boolean findUser(String name);

	public boolean isReadyToConnect();
	public void setReadyToConnect(boolean readyToConnect);
	public void logout();

}
