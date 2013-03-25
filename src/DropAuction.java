import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class DropAuction implements Command{

	private Mediator med;
	
	public DropAuction(Mediator med) {
		this.med = med;
	}
	
	@Override
	public void execute(int tableRow, int tableCol, TableView userPanel)
	{
		String prodStatusLicitatie = userPanel.getModel().getValueAt(tableRow, 3).toString();
		DefaultTableModel userReqModel = userPanel.getModel();		

		//seller-ul poate renunta la licitatie doar daca e exceeded
		if(prodStatusLicitatie.equals(Status.OFFER_EXCEEDED.getName()))
		{
			userReqModel.setValueAt(Status.NO_OFFER.getName(), tableRow, 3);
		}
		else {
			JOptionPane.showMessageDialog(null, "You can't drop the auction!");
			return;
		}
		
	}

}
