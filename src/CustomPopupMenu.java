import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * Clasa ce implementeaza un popup menu cu 2 itemi
 * @author silvia
 *
 */
public class CustomPopupMenu extends JPopupMenu{
	JTable table;
	
	public CustomPopupMenu(String item1, String item2, JTable tbl) {
		
		super();
		this.table = tbl;

   	    // Item 1
	    JMenuItem menuItem1 = new JMenuItem(item1);
	    menuItem1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JMenuItem it = (JMenuItem)arg0.getSource();
				System.out.println(it.getText());				
			}
		});
	    this.add(menuItem1);

	    // Item 2
	    JMenuItem menuItem2 = new JMenuItem(item2);
	    menuItem2.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JMenuItem it = (JMenuItem)arg0.getSource();
				System.out.println(it.getText());				
			}
		});	    
	    this.add(menuItem2); 
	    
	    
//	    this.addPopupMenuListener(new PopupMenuListener() {
//			
//			@Override
//			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
//				System.out.println("popup");
//				
//        		Point p = getLocation();
//        		int row = table.rowAtPoint(p);
//        		int col = table.columnAtPoint(p);
//        		System.out.println(row+" "+col);
//        		
//        		table.addRowSelectionInterval(row,row);
//        		
//				if(table.getSelectedRow() == -1){
//				
//				}
//			}
//			
//			@Override
//			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
//
//			}
//			
//			@Override
//			public void popupMenuCanceled(PopupMenuEvent e) {				
//			}
//		});
	}
}
