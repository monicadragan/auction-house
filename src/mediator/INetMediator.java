package mediator;

import javax.swing.table.DefaultTableModel;

import types.UserPublicInfo;

import gui.MainWindow;

/**
 * Interfata modului Mediator pentru Network
 *
 */
public interface INetMediator {

	public void changeTransferProgress(Integer val, MainWindow src, MainWindow destination, int srcRow, int dstRow);
	public boolean findUser(String name);
	public DefaultTableModel getTableModel();
	public UserPublicInfo getClientPublicInfo();
}
