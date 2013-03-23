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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
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

public class TableDemo extends JFrame {

   	  public TableDemo() {
   	    super("Zanko Auction House");
    	    
   	    Border raisedBorder = BorderFactory.createRaisedBevelBorder();
   	    setLayout(new GridLayout(0, 1));
   	    setDefaultCloseOperation(EXIT_ON_CLOSE);
//   	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//   	    int x = (int) ((dimension.getWidth() - getWidth()) / 2);
//   	    int y = (int) ((dimension.getHeight() - getHeight()) / 2);
//   	    setLocation(x, y);
    	    
   	    JPanel buyerView = new JPanel();
    	        	    
   	    //buyerView.setLayout(new BoxLayout(buyerView,BoxLayout.PAGE_AXIS));    	    

   	    //tabel
   	    DefaultTableModel dm = new DefaultTableModel();    	    
   	    Object[][] corp = {{ "Aparat Foto", "Nikon","Inactiv ", "500",  "in progress..." },
       	                   { "Camera Foto", "Canon","Inactiv", "400", "in progress..." },
       	                   { "Mouse", "LogiTech", "Inactiv", "50","in progress..." },
       	                   { "Tastatura", "A4Tech", "Inactiv", "15","in progress..." }};
   	    Object[] cap = { "Produs/Serviciu", "Status" ,"Furnizori", "Pret", "Progress Bar"};
    	    
   	    dm.setDataVector(corp, cap);

   	    final JTable table = new JTable(dm);  
   	       	    
   	    final JPopupMenu popupMenu = new JPopupMenu("Title");
	    // Launch Request
	    JMenuItem menuItem1 = new JMenuItem("Launch Request");
	    menuItem1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Launch Request");				
			}
		});
	    popupMenu.add(menuItem1);

	    // Drop Request
	    JMenuItem menuItem2 = new JMenuItem("Drop Request");
	    menuItem2.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Drop Request");				
			}
		});	    
	    popupMenu.add(menuItem2); 
	    
	    
	    popupMenu.addPopupMenuListener(new PopupMenuListener() {
			
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				System.out.println("popup");
				
        		Point p = popupMenu.getLocation();
        		int row = table.rowAtPoint(p);
        		int col = table.columnAtPoint(p);
        		System.out.println(row+" "+col);
        		
        		table.addRowSelectionInterval(row,row);
        		
				if(table.getSelectedRow() == -1){
				
				}
			}
			
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

			}
			
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {				
			}
		});
	    
	    table.setComponentPopupMenu(popupMenu);    
   	    
        table.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		System.out.println("Click");         		
        	}
        });
   	    
//   	    table.getColumn("Actiuni").setCellRenderer(new ButtonRenderer());
   	    //table.getColumn("Actiuni").setCellEditor(new ButtonEditor(new JCheckBox()));
   	    table.getColumn("Progress Bar").setCellRenderer(new ProgressBarRenderer());
   	    JScrollPane scroll = new JScrollPane(table);
   	    getContentPane().add(scroll);
   	    
   	    //altele
    	    
        JPanel menu = new JPanel(new GridLayout(1, 0));
            
        JButton launch = new JButton("Launch Offer");
        JButton drop = new JButton("Drop Offer");
            
        menu.add(launch);
        menu.add(drop);
            
        buyerView.add(menu);
        buyerView.add(new JLabel("Hello2!"));
            
        getContentPane().add(buyerView);
    	    
  	    setSize(500, 200);
   	    setVisible(true);
   	  }

   	  public static void main(String[] args) {    	      	   
   		  TableDemo frame = new TableDemo();
   	  }
}
