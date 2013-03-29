package gui;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 * Clasa ce implementeaza un popup menu cu 2 itemi;
 * Acesta este utilizata pentru orice tip de utilizator
 * @author silvia
 *
 */
public class CustomPopupMenu extends JPopupMenu{
	JTable table;
	TableView panel;
	int tableRow;
	int tableColumn;
	
	public CustomPopupMenu(String item1, String item2, TableView pan) {
		
		super();
		this.table = pan.table;
		this.panel = pan;

   	    // Item 1
	    JMenuItem menuItem1 = new JMenuItem(item1);
	    menuItem1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JMenuItem it = (JMenuItem)arg0.getSource();
				panel.mainFrame.mediator.sendRequest(
						it.getText(),
						tableRow, 
						tableColumn,
						panel);
			}
		});
	    this.add(menuItem1);

	    // Item 2
	    if(item2 != null) {
		    JMenuItem menuItem2 = new JMenuItem(item2);
		    menuItem2.addActionListener(new ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent arg0) {
					JMenuItem it = (JMenuItem)arg0.getSource();
					panel.mainFrame.mediator.sendRequest(
							it.getText(),
							tableRow, 
							tableColumn,
							panel);
				}
			});	    
		    this.add(menuItem2); 
	    }
	}
	
	public void show(Component invoker, int x, int y){
		super.show(invoker,x,y);

		Point p = new Point(x, y);
		tableRow = table.rowAtPoint(p);  
		tableColumn = table.columnAtPoint(p); 
	}
}
