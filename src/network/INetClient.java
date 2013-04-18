package network;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface INetClient {

	public void makeConnection();
	public void connect(SelectionKey key) throws IOException;
	public Object readObject(SelectionKey key);
	public void writeObject(SelectionKey key, Object obj);
	public SelectionKey getKey();

}
