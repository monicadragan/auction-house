package control;
import gui.TableView;

public class StatusManager {
	
	public StatusManager() {
	}

	public void processRequest(Command cmd, int tableRow, int tableCol, TableView userPanel)
	{
		cmd.execute(tableRow, tableCol, userPanel);
	}
	
}
