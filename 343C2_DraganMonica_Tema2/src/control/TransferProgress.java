package control;

import javax.swing.table.DefaultTableModel;
import types.Status;
/**
 * Clasa folosita pentru a face modificarile necesare cand s-a terminat
 * o licitatie si incepe transferul produsului/serviciului, schimband
 * statusul in functie de valoarea progress bar-ului
 * @author silvia
 *
 */
public class TransferProgress {
	
	public int value;
	
	public TransferProgress(int value) {
		this.value = value;
	}

	public void changeStatus(int tableRow, int tableCol, DefaultTableModel model )
	{
		if (value == -1)//s-a oprit transferul din cauza buyer-ului care s-a delogat
			model.setValueAt(
					Status.TRANSFER_FAILED.getName(),
					tableRow,
					tableCol);
		else if(value == 100)
			model.setValueAt(
					Status.TRANSFER_COMPLETED.getName(),
					tableRow,
					tableCol);
		else if(value < 20)
			model.setValueAt(
					Status.TRANSFER_STARTED.getName(),
					tableRow,
					tableCol);
		else if(value >= 20)
			model.setValueAt(
					Status.TRANSFER_IN_PROGRESS.getName(),
					tableRow,
					tableCol);
		
	}
	

}
