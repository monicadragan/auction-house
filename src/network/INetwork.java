package network;

import gui.MainWindow;

public interface INetwork {

	public void transferFile(MainWindow source, MainWindow destination,
							int srcRow, int dstRow);

}
