import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


public class CustomPopupMenu extends JPopupMenu{
	JTable table;
	
	public CustomPopupMenu(String title, JTable tbl) {
		
		super(title);
		this.table = tbl;

   	    // Launch Request
	    JMenuItem menuItem1 = new JMenuItem("Launch Request");
	    menuItem1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Launch Request");				
			}
		});
	    this.add(menuItem1);

	    // Drop Request
	    JMenuItem menuItem2 = new JMenuItem("Drop Request");
	    menuItem2.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Drop Request");				
			}
		});	    
	    this.add(menuItem2); 
	    
	    
	    this.addPopupMenuListener(new PopupMenuListener() {
			
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				System.out.println("popup");
				
        		Point p = getLocation();
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
	}
}
