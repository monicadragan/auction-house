package control;
import gui.TableView;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;




public class StatusManager {
	
	public StatusManager() {
	}

	public void processRequest(Command cmd, int tableRow, int tableCol, TableView userPanel)
	{
		cmd.execute(tableRow, tableCol, userPanel);
	}
	
}
