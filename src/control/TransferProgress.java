package control;

import network.ClientInformation;
import network.Server;
import types.Status;
import mediator.IGUIMediator;
import mediator.Mediator;
/**
 * Clasa folosita pentru a face modificarile necesare cand s-a terminat
 * o licitatie si incepe transferul produsului/serviciului, schimband
 * statusul in functie de valoarea progress bar-ului
 * @author silvia
 *
 */
public class TransferProgress implements Command{
	
	public int value;
	public Server server;
	
	public TransferProgress(Server server) {
		this.server = server;
	}

	@Override
	public void execute(int tableRow, int tableCol, ClientInformation clientInfo)
	{
		switch(value){
			case -1://s-a oprit transferul din cauza buyer-ului care s-a delogat
				clientInfo.getModel().setValueAt(
						Status.TRANSFER_FAILED.getName(),
						tableRow,
						tableCol);
				break;
			case 1:
				clientInfo.getModel().setValueAt(
						Status.TRANSFER_STARTED.getName(),
						tableRow,
						tableCol);
				break;
			case 20:
				clientInfo.getModel().setValueAt(
						Status.TRANSFER_IN_PROGRESS.getName(),
						tableRow,
						tableCol);
				break;		
			case 100:
				clientInfo.getModel().setValueAt(
						Status.TRANSFER_COMPLETED.getName(),
						tableRow,
						tableCol);
				break;		
		}
		
	}
	

}
