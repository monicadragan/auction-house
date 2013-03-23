import java.awt.Dimension;

/**
 * Controller pentru actiunile care se fac in interfata grafica (Viewer)
 * @author silvia
 *
 */
public class ActionsController{

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
