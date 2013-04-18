package mediator;

import javax.swing.table.DefaultTableModel;

import types.Packet;
import types.UserPublicInfo;

import gui.MainWindow;

/**
 * Interfata modului Mediator pentru Network
 *
 */
public interface INetMediator {

	public boolean findUser(String name);
	public DefaultTableModel getTableModel();
	public UserPublicInfo getClientPublicInfo();
	public void processReplyFromServer(Packet recvPacket);
	public String getUsername();
}
