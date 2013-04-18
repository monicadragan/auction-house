package gui;

import types.UserType;

/**
 * Interfata ce defineste fereastra principala 
 * a unui utilizator (GUI-ul)
 * @author silvia
 *
 */
public interface IMainWindow {

	public void initWindow();
	public void loginRequest(String username, String password, UserType uType);
	public void changeProgresBar(Integer value, int row, int column);
	public String getUsername();
	public void setUsername(String username);
	public void setTableView(TableView tableView);
	public TableView getTableView();
	public void setUType(UserType uType);
	public UserType getUType();
	public void addRowTable(Object[] rowData);
	public void removeRowTable(int row);
	public void setValueAt(Object[] rowData, int row);
	
}
