import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class ProgressBarRenderer extends JProgressBar implements TableCellRenderer {

	  public ProgressBarRenderer() {
	    super();
	  }

	  public Component getTableCellRendererComponent(JTable table, Object value,
	      boolean isSelected, boolean hasFocus, int row, int column) {
		  setValue(50);
		  setStringPainted(true);
		  return this;
	  }
}
 
class ButtonRenderer extends JButton implements TableCellRenderer {

	  public ButtonRenderer() {
	    setOpaque(true);
	  }

	  public Component getTableCellRendererComponent(JTable table, Object value,
	      boolean isSelected, boolean hasFocus, int row, int column) {
	    if (isSelected) {
	      setForeground(table.getSelectionForeground());
	      setBackground(table.getSelectionBackground());
	    } else {
	      setForeground(table.getForeground());
	      setBackground(UIManager.getColor("Button.background"));
	    }
	    setText((value == null) ? "" : value.toString());
	    return this;
	  }
}

class ButtonEditor extends DefaultCellEditor {
	  protected JButton button;

	  private String label;

	  private boolean isPushed;

	  public ButtonEditor(JCheckBox checkBox) {
	    super(checkBox);
	    button = new JButton();
	    button.setOpaque(true);
	    button.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        fireEditingStopped();
	      }
	    });
	  }
}

public class TableView extends JPanel {

	private DefaultTableModel dm = new DefaultTableModel();		//model tabel
    private Object[][] corp = {{ "Aparat Foto", "Inactiv", "Nikon", "500",  "in progress..." },
	                   { "Camera Foto", "Inactiv", "Canon", "400", "in progress..." },
	                   { "Mouse", "Inactiv","Logitech", "50","in progress..." },
	                   { "Tastatura","Inactiv", "A4Tech", "15","in progress..." },
	                   { "Tastatura","Inactiv", "Logitech", "59","in progress..." }};
	private Object[] headTable = { "Produs/Serviciu", "Status" ,"Furnizori", "Pret", "Progress Bar"};

	private JTable table;  
	   	    
	private JPopupMenu popupMenuProductsList;//nefolosit in cazul furnizorilor
	private JPopupMenu popupMenuUsersList;

	private JButton bLogout = new JButton("Logout");
	
	public TableView(String username, UserType type, String usersNameList)//TODO: datele vin ca parametri in constructor! 
	{
		super();
		headTable[2] = usersNameList;
		init(username, type);
	}
   	  
   	 public void init(String username, UserType type)
   	 {
    	this.setLayout(new BorderLayout());
   	    JPanel tablePanel = new JPanel(new GridLayout(1, 0));
   	    JPanel bottomPanel = new JPanel(new GridLayout(0, 6));
   	    dm.setDataVector(corp, headTable);

   	    table = new JTable(dm);
        table.getColumn("Progress Bar").setCellRenderer(new ProgressBarRenderer());

        //adaugare popupMenu la right-click pe coloana produs sau user
        switch(type)
        {
        	case BUYER:
        	{
           	    popupMenuProductsList = new CustomPopupMenu(
           	    		"Launch Offer request",
           	    		"Drop Offer request",
           	    		table);
           	    popupMenuUsersList = new CustomPopupMenu(
        	    		"Accept Offer",
        	    		"Refuse Offer",
        	    		table);
           	    break;
        	}
        	case SELLER:
        	{ 
        		popupMenuUsersList = new CustomPopupMenu(
    	    		"Make offer",
    	    		"Drop auction",
    	    		table);
        		break;
        	}
        }
   	    table.addMouseListener(new TableMouseSelect(type, table));

		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(500, 100));
		tablePanel.add(scroll);
		//dummy panels
		bottomPanel.add(new JPanel()); bottomPanel.add(new JPanel());
		bottomPanel.add(new JPanel()); bottomPanel.add(new JPanel());
		bottomPanel.add(new Label(username));
		bottomPanel.add(bLogout);
		this.add(bottomPanel, BorderLayout.SOUTH);
		this.add(tablePanel, BorderLayout.CENTER);
	}

     private class TableMouseSelect extends MouseAdapter {
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
     
	private void selectTableRow(MouseEvent evt, JTable table, UserType type)
	{
		if (SwingUtilities.isRightMouseButton(evt))
		{
			Point p = evt.getPoint();
			int row = table.rowAtPoint(p);  
			int col = table.columnAtPoint(p);  
			table.setRowSelectionInterval(row, row);
			table.setColumnSelectionInterval(col,col);
			
			//coloana produs/serviciu
			if(col == 0 && type.equals(UserType.BUYER))
				popupMenuProductsList.show(table, p.x, p.y);
			//coloana useri (furnizori/cumparatori)
			else if(col == 2)
				popupMenuUsersList.show(table, p.x, p.y);
	
		}  
	}
	
}
