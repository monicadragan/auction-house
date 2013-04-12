package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import network.Client;

import types.UserType;

import mediator.IGUIMediator;
import mediator.Mediator;

/**
 * Clasa ce defineste interfata grafica: fereastra principala
 * a unui utilizator
 * @author silvia
 *
 */
public class MainWindow extends JFrame implements IMainWindow {

	IGUIMediator mediator;
	private UserType uType;
	private String username;
	public String password;
	private TableView tableView;
	public ConcreteUserView userView;
	
	public MainWindow(IGUIMediator mediator){
		
 	    super("Zanko Auction House");
 	    		
		this.mediator = mediator;
		userView = new ConcreteUserView();
		initWindow();
		
	}

	/**
	 * Initializarea ferestrei principale
	 */
	public void initWindow()
	{
 	    setLayout(new BorderLayout());
  	    setDefaultCloseOperation(EXIT_ON_CLOSE);
  	    setSize(500, 300);
	    
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation((dim.width-getWidth())/2, (dim.height-getHeight())/2);
	    
	    userView.setStateView(this);
   	    setVisible(true);
   	    
	}
	
	/**
	 * Metoda folosita la logarea unui user in vederea afisarii
	 * listei de produse/servicii daca autentificarea s-a facut cu succes
	 */
	public void loginRequest(String username, String password, UserType uType)
	{
		if(this.mediator.findUser(username))//user-ul este deja logat!
		{
			JOptionPane.showMessageDialog(null, "This user is already login!");
			return;
		}
		this.setUType(uType);
		this.setUsername(username);
		this.password = password;
	    userView.setStateView(this);
		setVisible(true);
	}	
	
	/**
	 * Metoda ce modifica progressBar-ul
	 */
	public void changeProgresBar(Integer value, int row, int column)
	{
		DefaultTableModel model = this.tableView.getModel();
		model.setValueAt(value, row, column);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
	
	public void setTableView(TableView tableView) {
		this.tableView = tableView;
	}

	public TableView getTableView() {
		return tableView;
	}

	public void setUType(UserType uType) {
		this.uType = uType;
	}

	public UserType getUType() {
		return uType;
	}

}
