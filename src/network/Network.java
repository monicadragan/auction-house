package network;
import gui.TableView;

import java.util.List;
import java.util.Random;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;


public class Network {
	
	public Network(){
		
	}
	
	public void transferFile(TableView source, TableView destination, final int sourceRow,final int destRow){
		System.out.println("Transfer ");
		//System.out.println(((ProgressBarRenderer) source.table.getCellRenderer(tableRow, 5)).getValue());
		//System.out.println(((ProgressBarRenderer) source.table.getColumn("Progress Bar").getCellRenderer()).getValue());
		/*((ProgressBarRenderer) source.table.getCellRenderer(tableRow, 5)).setPerCent(70);
		((ProgressBarRenderer) destination.table.getCellRenderer(tableRow, 5)).setPerCent(70);
		source.table.updateUI();
		destination.table.updateUI();	*/		
		
		final DefaultTableModel modelSeller = source.getModel();
		final DefaultTableModel modelBuyer = destination.getModel();
				
		final int key = 2;
        SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>() {

            protected int sleepDummy = new Random().nextInt(100) + 1;
            protected int lengthOfTask = 120;
            
            @Override
            protected Integer doInBackground() {
                int current = 0;
                while (current < lengthOfTask && !isCancelled()) {

                    current++;
                    try {
                        Thread.sleep(sleepDummy);
                    } catch (InterruptedException ie) {
                        break;
                    }
                    publish(100 * current / lengthOfTask);
                }
                return sleepDummy * lengthOfTask;
            }
            @Override
            protected void process(List<Integer> c) {
                modelBuyer.setValueAt(c.get(c.size() - 1), destRow, 5);
                modelSeller.setValueAt(c.get(c.size() - 1), sourceRow, 5);
            	//((JProgressBar) source.table.getCellRenderer(2, 5)).setValue(c.get(c.size() - 1));
            }

            @Override
            protected void done() {
                String text;
                int i = -1;
                if (isCancelled()) {
                    text = "Cancelled";
                } else {
                    try {
                        i = get();
                        text = (i >= 0) ? "Done" : "Disposed";
                    } catch (Exception ignore) {
                        ignore.printStackTrace();
                        text = ignore.getMessage();
                    }
                }
                System.out.println(key + ":" + text + "(" + i + "ms)");
            }  
        };
        
        worker.execute();
	}

}
