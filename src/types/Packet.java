package types;

import java.io.Serializable;

import javax.swing.table.DefaultTableModel;

import gui.TableView;

class pair{
	
	String value;
	String key;
	
}

public class Packet implements Serializable{

	private static final long serialVersionUID = 1L;
	public PacketType pType;
	public String msg;//tipul request-ului unui client
	public int tableRow;
	public int tableCol;
	public Object[] rowData;
	public Object value;
	public pair additional_info;
	public String price;//folosit la Make offer - cand furnizorul a schimbat pretul
	
//	public Packet(String msg, int tableRow, int tableCol,
////			TableView userPanel, 
//			String sourceIP, String destIP, String sourceUsername, String destUsername) {
//		this.msg = msg;
//		this.tableRow = tableRow;
//		this.tableCol = tableCol;
////		this.userPanel = userPanel;
//		this.sourceIP = sourceIP;
//		this.destIP = destIP;
//		this.sourceUsername = sourceUsername;
//		this.destUsername = destUsername;
//	}
	
	public Packet(String msg)
	{
		this.msg = msg;
	}
	
	public Packet(String msg, int tableRow, int tableCol)
	{
		this.msg = msg;
		this.tableRow = tableRow;
		this.tableCol = tableCol;
	}
	
	public Packet(PacketType pType, int row)
	{
		this.pType = pType;
		this.tableRow = row; //linia ce va fi stearsa
	}
	
	public Packet(PacketType pType, Object[] rowData)
	{
		this.pType = pType;
		this.rowData = rowData;
	}
	
	public Packet(PacketType pType, Object[] rowData, int row)
	{
		this.pType = pType;
		this.rowData = rowData;
		this.tableRow = row;
	}
	
	public Packet(PacketType pType, int row, int col)
	{
		this.pType = pType;
		this.tableRow = row;
		this.tableCol = col;
	}
	
	public static Object[] getRowTable(DefaultTableModel model, int row)
	{
		Object[] rowData = new Object[model.getColumnCount()];
		for(int i = 0; i < model.getColumnCount(); ++i)
		{
			rowData[i] = model.getValueAt(row, i);
		}
		return rowData;
	}
	
	public static void setRowTable(DefaultTableModel model, int row, Object[] rowData)
	{
		for(int i = 0; i < model.getColumnCount(); ++i)
		{
			model.setValueAt(rowData[i], row, i);
		}

	}
}
