package control;
import network.ClientInformation;

public class StatusManager {
	
	public StatusManager() {
	}

	public void processRequest(Command cmd, int tableRow, int tableCol, ClientInformation clientInfo)
	{
		cmd.execute(tableRow, tableCol, clientInfo);
	}
	
}
