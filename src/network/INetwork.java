package network;

import gui.MainWindow;
import gui.TableView;

public interface INetwork {

	public void transferFile(MainWindow source, MainWindow destination,
							int srcRow, int dstRow);
	public void setSourceRow(int sourceRow);
	public int getSourceRow();
		
	public void setDestRow(int destRow);	
	public int getDestRow();
	public void setSource(MainWindow source);
	public MainWindow getSource();
	public void setDest(MainWindow dest);
	public MainWindow getDest();

}
