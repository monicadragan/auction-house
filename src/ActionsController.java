import java.awt.Dimension;

public class ActionsController implements IMediator{

	MainWindow mainWindow;
	
	public ActionsController(MainWindow main) {
		this.mainWindow = main;
	}
	
	public void loginRequest(String username, String password, UserType uType)
	{
		//TODO se verifica daca credentialele sunt valide (exista in DB)
		TableView tableView = null;
		mainWindow.setVisible(false);
		switch(uType)
		{
			case BUYER:
			{
				tableView = new BuyerTableView(username, uType);
				break;
			}
			case SELLER:
			{			
				tableView = new SellerTableView(username, uType);
				break;
			}
		}
		mainWindow.setContentPane(tableView);
		mainWindow.setVisible(true);
	    mainWindow.resize(new Dimension(600, 200));
	    mainWindow.setTitle("Products/Services List");
		
	}
	
}
