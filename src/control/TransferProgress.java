package control;

import types.Status;
import mediator.Mediator;
import gui.TableView;

public class TransferProgress implements Command{
	
	public int value;

	public Mediator med;
	
	public TransferProgress(Mediator med) {
		this.med = med;
	}

	@Override
	public void execute(int tableRow, int tableCol, TableView userPanel)
	{
		switch(value){
			case -1:
				userPanel.getModel().setValueAt(
						Status.TRANSFER_FAILED.getName(),
						tableRow,
						tableCol);
				break;
			case 1:
				userPanel.getModel().setValueAt(
						Status.TRANSFER_STARTED.getName(),
						tableRow,
						tableCol);
				break;
			case 20:
				userPanel.getModel().setValueAt(
						Status.TRANSFER_IN_PROGRESS.getName(),
						tableRow,
						tableCol);
				break;		
			case 100:
				userPanel.getModel().setValueAt(
						Status.TRANSFER_COMPLETED.getName(),
						tableRow,
						tableCol);
				break;		
		}
		
	}
	

}
