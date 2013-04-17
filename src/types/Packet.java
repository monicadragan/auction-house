package types;

import java.io.Serializable;
import java.nio.channels.SelectionKey;

import javax.swing.table.DefaultTableModel;

import gui.TableView;

public class Packet implements Serializable {

	private static final long serialVersionUID = 1L;
	public PacketType pType;
	public String msg;//tipul request-ului unui client
	public int tableRow;
	public int tableCol;
	public Object[] rowData;
	public Object value;
	public String price;//folosit la Make offer - cand furnizorul a schimbat pretul
	
	public long transferSize;
	public int transferOffset;
	public Object product;
	public String from;
	public String to;
	public int fromRow;
	public int toRow;
	public byte buffer[];
	public int sizeBuffer;
	
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
	
	public Packet(PacketType pType, String msg, Object product, String from, String to, long size, int offset, int fromRow, int toRow)
	{
		this.pType = pType;
		this.msg = msg;
		this.product = product;
		this.from = from;
		this.to = to;
		this.transferSize = size;
		this.transferOffset = offset;
		this.fromRow = fromRow;
		this.toRow = toRow;
	}
	
	public Packet(PacketType pType, String msg, Object product, String from, String to, byte buf[], int sizeBuffer, long size, int offset, int fromRow, int toRow)
	{
		this.pType = pType;
		this.msg = msg;
		this.product = product;
		this.from = from;
		this.to = to;
		this.buffer = buf;
		this.sizeBuffer = sizeBuffer;
		this.transferSize = size;
		this.transferOffset = offset;
		this.fromRow = fromRow;
		this.toRow = toRow;
	}
	
	public Packet(PacketType pType, String msg, Object product, String from, String to, int fromRow, int toRow)
	{
		this.pType = pType;
		this.msg = msg;
		this.product = product;
		this.from = from;
		this.to = to;
		this.fromRow = fromRow;
		this.toRow = toRow;
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
