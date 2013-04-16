package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import network.Client;

import types.Packet;
import types.Status;
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
	private TableView tableView = null;
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
	    userView.setStateView(this);//seteaza tabela in panel
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
	
	public void addRowTable(final Object[] rowData)
	{
		final DefaultTableModel model = this.tableView.getModel();
		SwingUtilities.invokeLater(new Runnable() {

		    @Override
		    public void run() {
				if(rowData[1].equals("Inactive"))//am primit sa adaug o linie inactiva = DropRequest
				{
					String prodName = rowData[0].toString();
					//sterg toate liniile corespunzatoare acestui produs
					for(int j = 0; j < model.getRowCount(); j++)
					{
						if(model.getValueAt(j, 0).toString().equals(prodName))
						{
							model.removeRow(j);
							j--;
						}
					}
					model.addRow(rowData); //adaug linia cu produsul inactiv
				} else {  //LaunchRequest
			    	model.addRow(rowData);
					for(int j = 0; j < model.getRowCount(); ++j)
						if(model.getValueAt(j, 0).equals(rowData[0].toString()) &&
								model.getValueAt(j, 1).toString().equals("Inactive"))
						{
							model.removeRow(j);
							break;
						}
				}	
		    }

		});

	}
	
	public void removeRowTable(final int row)
	{
		final DefaultTableModel model = this.tableView.getModel();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
		    public void run() {
				String prodName = model.getValueAt(row, 0).toString();
				String price = model.getValueAt(row, 4).toString();
				model.removeRow(row);
				//verific daca mai exista vreo linie cu acest produs
				int noLines = 0;
				for(int j = 0; j < model.getRowCount(); j++)
					if(model.getValueAt(j, 0).toString().equals(prodName) )
					{
						noLines++;
						break;
					}
				if(noLines == 0)
				{
					Object[] rowData = createInactiveRowDataTable(model.getColumnCount(), prodName, price, 0);
					model.addRow(rowData);
				}
				
			}
		});
	}
	
	public void setValueAt(final Object[] rowData, final int row)
	{
		final DefaultTableModel model = this.tableView.getModel();
		final UserType usertype = uType; 
		SwingUtilities.invokeLater(new Runnable() {
			@Override
		    public void run() {
				
				Packet.setRowTable(model, row, rowData);
				//Status.OFFER_ACCEPTED si e cumparator- trebuie sa refuze restul ofertelor
				if(rowData[3].toString().equals(Status.OFFER_ACCEPTED)
						&& usertype.equals(UserType.BUYER)) 
				{
					for(int j = 0; j < model.getRowCount(); j++)
						if(model.getValueAt(j, 0).toString().equals(rowData[0]) && j != row)
							model.setValueAt(Status.OFFER_REFUSED.getName(), j, 3);

				}

			}
		});
	}
	
	Object[] createInactiveRowDataTable(int colsCount, String prodName, String price, int progressBar)
	{
		Object[] rowData = new Object[colsCount];
		rowData[0] = prodName;
		rowData[1] = "Inactive";
		rowData[2] = "";
		rowData[3] = Status.INACTIVE.getName();
		rowData[4] = price;//pret
		rowData[5] = 0;//progress bar 
		return rowData;
	}
}
