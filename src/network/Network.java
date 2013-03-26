package network;
import gui.MainWindow;
import gui.TableView;

import java.util.List;
import java.util.Random;

import javax.swing.SwingWorker;

import mediator.INetMediator;
import mediator.Mediator;


public class Network implements INetwork{
	
	INetMediator med;
	private int sourceRow;
	private int destRow;
	private MainWindow source;
	private MainWindow dest;
	
	public Network(Mediator med){
		this.med = med;
	}
	
	public void transferFile(MainWindow source, MainWindow destination, int srcRow, int dstRow){
		System.out.println("Transfer");
		this.setSourceRow(srcRow);
		this.setDestRow(dstRow);
		this.setSource(source);
		this.dest = destination;
				
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
                    if(!med.findUser(dest.getUsername()))
                    	cancel(true);
                }
                return sleepDummy * lengthOfTask;
            }
            @Override
            protected void process(List<Integer> c) {
            	med.changeTransferProgress(c.get(c.size() - 1));
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
                System.out.println(text + "(" + i + "ms)");
            }  
        };
        
        worker.execute();
	}

	public void setSourceRow(int sourceRow) {
		this.sourceRow = sourceRow;
	}

	public int getSourceRow() {
		return sourceRow;
	}

	public void setDestRow(int destRow) {
		this.destRow = destRow;
	}

	public int getDestRow() {
		return destRow;
	}

	public void setSource(MainWindow source) {
		this.source = source;
	}

	public MainWindow getSource() {
		return source;
	}

	public void setDest(MainWindow dest) {
		this.dest = dest;
	}

	public MainWindow getDest() {
		return dest;
	}


}
