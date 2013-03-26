package network;

import gui.MainWindow;
import gui.TableView;

public interface INetwork {

	public void transferFile(MainWindow source, MainWindow destination,
							int srcRow, int dstRow);

}
