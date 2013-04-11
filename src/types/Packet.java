package types;

import gui.TableView;

public class Packet {

	String msg;//tipul request-ului unui client
	int tableRow;
	int tableCol;
	TableView userPanel;//tabela celui care a facut request
	String sourceIP;
	String destIP;
	String sourceUsername;
	String destUsername;
	
	public Packet(String msg, int tableRow, int tableCol, TableView userPanel, String sourceIP, String destIP, String sourceUsername, String destUsername) {
		this.msg = msg;
		this.tableRow = tableRow;
		this.tableCol = tableCol;
		this.userPanel = userPanel;
		this.sourceIP = sourceIP;
		this.destIP = destIP;
		this.sourceUsername = sourceUsername;
		this.destUsername = destUsername;
	}
	
	public Packet(String msg){
		this.msg = msg;
	}
		
}
