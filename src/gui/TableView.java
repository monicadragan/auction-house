package gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import types.Product;
import types.Status;
import types.User;
import types.UserType;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clasa ce creeaza un CellRenderer pentru a putea folosi
 * un ProgressBar intr-o celula din tabel 
 */
class ProgressBarRenderer extends DefaultTableCellRenderer implements Serializable {

	private static final long serialVersionUID = 1L;
	protected final JProgressBar b = new JProgressBar(0, 100);

    public ProgressBarRenderer() {
        super();
        setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        b.setValue(50);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
    		boolean isSelected, boolean hasFocus, int row, int column) {
        Integer i = (Integer) value;
        String text = "100%";
        if (i < 0) {
            text = "Error";
        } else if (i < 100) {
            b.setValue(i);
            return b;
        }
        super.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);
        return this;
    }
}

/**
 * Clasa ce defineste un panel-ul de vizualizare al listei de servicii
 * pentru un utilizator
 */
public class TableView extends JPanel {

	private static final long serialVersionUID = 1L;
	protected DefaultTableModel model = new DefaultTableModelSer() //model tabel
	{
	    
		private static final long serialVersionUID = 1L;

		@Override
	    public boolean isCellEditable(int row, int column) {
	    	if(column == 4)
	    		return true;
	        return false;
	    }
	};
    ArrayList<Object[]> bodyTable;
	protected Object[] headTable = { "Produs/Serviciu", "Status" ,"Furnizori", "StatusLicitatie", "Pret", "TranferProgress"};

	public JTable table;  
	public MainWindow mainFrame;
	public User userInfo;
	   	    
	protected JPopupMenu popupMenuProductsList;//nefolosit in cazul furnizorilor
	protected JPopupMenu popupMenuUsersList;

	public JButton bLogout = new JButton("Logout");
	
	public TableView(User userInfo, String usersNameColumn, MainWindow frame)
	{
		super();
		this.userInfo = userInfo;
		this.mainFrame = frame;
		headTable[2] = usersNameColumn;
		
		bodyTable = new ArrayList<Object[]>();
		for(int i=0; i<userInfo.products.size(); i++) {
			Product product = userInfo.products.get(i);
			ArrayList<Object> objects = new ArrayList<Object>();
			objects.add(product.name);
			objects.add(product.status);
			objects.add("");//user
			objects.add(product.statusLicitatie.getName());
			if(product.pret == 0)
				objects.add("");
			else
				objects.add(product.pret);
			objects.add(0);
			bodyTable.add(objects.toArray());
		}
		
		init(userInfo.username, userInfo.uType);
	}
   	  
   	 public void init(String username, UserType type)
   	 {
    	this.setLayout(new BorderLayout());
   	    JPanel tablePanel = new JPanel(new GridLayout(1, 0));
   	    JPanel bottomPanel = new JPanel(new GridLayout(0, 5));
   	    Object[][] body = new Object[bodyTable.size()][];
   	    bodyTable.toArray(body);
   	    model.setDataVector(body, headTable);

   	    table = new JTable(model);
   	    table.getColumnModel().getColumn(5).setCellRenderer(new ProgressBarRenderer());
   	    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        //adaugare popupMenu la right-click pe coloana produs sau user
        switch(type)
        {
        	case BUYER:
        	{
           	    popupMenuProductsList = new CustomPopupMenu(
           	    		"Launch Offer request",
           	    		"Drop Offer request",
           	    		this);
           	    popupMenuUsersList = new CustomPopupMenu(
        	    		"Accept Offer",
        	    		"Refuse Offer",
        	    		this);
           	    break;
        	}
        	case SELLER:
        	{ 
        		popupMenuUsersList = new CustomPopupMenu(
    	    		"Make offer",
    	    		"Drop auction",
    	    		this);
        		popupMenuProductsList = new CustomPopupMenu(
           	    		"View Best Offer",//pentru a vedea contraoferta
           	    		null,
           	    		this);
        		break;
        		
        	}
        }
   	    table.addMouseListener(new TableMouseSelect(type, table));
   	    
   	    bLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logout();
			}
		});

		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(500, 100));
		tablePanel.add(scroll);
		//dummy panels
		bottomPanel.add(new JPanel());
		bottomPanel.add(new JPanel()); bottomPanel.add(new JPanel());
		bottomPanel.add(new Label(username));
		bottomPanel.add(bLogout);
		this.add(bottomPanel, BorderLayout.SOUTH);
		this.add(tablePanel, BorderLayout.CENTER);
	}
   	 
   	 public void logout(){

   		boolean exit = true;
  		 //verifica daca este furnizor si este intr-o licitatie NU are voie sa faca logout
   		if(userInfo.uType.equals(UserType.SELLER))
		{
			for(int i = 0; i < model.getRowCount(); ++i)
				if(model.getValueAt(i, 3).toString().equals(Status.OFFER_MADE.getName())
						|| model.getValueAt(i, 3).toString().equals(Status.TRANSFER_STARTED.getName())
						|| model.getValueAt(i, 3).toString().equals(Status.TRANSFER_IN_PROGRESS.getName()))
					exit = false;
		} 
		else if(userInfo.uType.equals(UserType.BUYER))
		{
			for(int i = 0; i < model.getRowCount(); ++i)
				//trebuie sa refuze toate ofertele care i s-au facut
				if(model.getValueAt(i, 3).toString().equals(Status.OFFER_MADE.getName()))
					mainFrame.mediator.sendRequest("Refuse Offer", i, 2, this);
				//face drop request daca nu primise oferta
				else if(model.getValueAt(i, 3).toString().equals(Status.NO_OFFER.getName()))
					mainFrame.mediator.sendRequest("Drop Offer request", i, 0, this);
		}
		if(exit)
		{
			mainFrame.setVisible(false);
			mainFrame.mediator.logout();
		}
		else //nu are voie sa dea logout
		{
			JOptionPane.showMessageDialog(null, "Logout is forbidden in the mid of an auction.");
		}
   	 }

   	 /**
   	  * Creare listener pentru popup-menu
   	  */
     protected class TableMouseSelect extends MouseAdapter {
    	 UserType type;
    	 JTable table;
    	 public TableMouseSelect(UserType type, JTable table) {
			this.type = type;
			this.table = table;
		}
		public void mouseReleased(MouseEvent evt) {  
			if(evt.getSource() == table) {  
				selectTableRow(evt,table, type);  
			}  
		}  
		public void mousePressed(MouseEvent evt) {  
			if(evt.getSource() == table) {  
				selectTableRow(evt,table, type);  
			}  
		}  
		public void mouseClicked(MouseEvent evt) {  
			if(evt.getSource() == table) {  
				selectTableRow(evt,table, type);
			}  
		}  
	}    
     
	protected void selectTableRow(MouseEvent evt, JTable table, UserType type)
	{
		if (SwingUtilities.isRightMouseButton(evt))
		{
			Point p = evt.getPoint();
			int row = table.rowAtPoint(p);  
			int col = table.columnAtPoint(p);  
			table.setRowSelectionInterval(row, row);
			table.setColumnSelectionInterval(col,col);
			
			//coloana produs/serviciu
			if(col == 0) {// && type.equals(UserType.BUYER)){
				popupMenuProductsList.show(table, p.x, p.y);				
			}
			//coloana useri (furnizori/cumparatori)
			else if(col == 2){
				popupMenuUsersList.show(table, p.x, p.y);				
			}	
		}  
	}
	
	public DefaultTableModel getModel()
	{
		return model;
	}
	
	public void setModel(DefaultTableModel mdl)
	{
		this.model = mdl;
	}
	
}
