import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.Toolkit;
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
	private Object[] cap = { "Produs/Serviciu", "Status" ,"Furnizori", "Pret", "Progress Bar"};

	JTable table;  
	   	    
	JPopupMenu popupMenu;

	public TableView(String username)//TODO: datele vin ca parametri in constructor! 
	{
		super();
		init(username);
	}
   	  
   	 public void init(String username)
   	 {
    	this.setLayout(new BorderLayout());
   	    JPanel buyerView = new JPanel(new GridLayout(1, 0));
   	    dm.setDataVector(corp, cap);

   	    table = new JTable(dm);
        table.getColumn("Progress Bar").setCellRenderer(new ProgressBarRenderer());   	    
   	    popupMenu = new CustomPopupMenu("Title", table);

   	    table.setComponentPopupMenu(popupMenu);    //adaugare popupMenu

   	    table.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		System.out.println("Click");         		
        	}
        });


        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 100));
 		buyerView.add(scroll);
  	    this.add(new Label(username), BorderLayout.SOUTH);
   	    this.add(buyerView, BorderLayout.CENTER);
   	  }

}
