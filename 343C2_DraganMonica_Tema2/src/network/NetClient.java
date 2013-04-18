package network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import types.Packet;

import mediator.INetMediator;

public class NetClient implements INetClient{

	INetMediator mediator;
	String ip;
	int port;
	SelectionKey key;
	
	public NetClient(INetMediator med, String ip, int port) {
		this.mediator = med;
		this.ip = ip;
		this.port = port;

	}
	
	/**
	 * Crearea conexiunii cu serverul si asteptarea mesajelor
	 * 
	 */
	public void makeConnection(){
		
		Selector selector = null;
		SocketChannel socketChannel	= null;
		
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(ip, port));
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
			
			// main loop
			while (true) {
				// wait for something to happen
				selector.select();
				
				// iterate over the events
				for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
					// get current event and REMOVE it from the list!!!
					SelectionKey key = it.next();
					it.remove();
					this.key = key;
					
					if (key.isConnectable())
						connect(key);
					else if (key.isReadable())
					{
						Packet recvPacket = (Packet)readObject(key);
						mediator.processReplyFromServer(recvPacket);
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			// cleanup			
			if (selector != null)
				try {
					selector.close();
				} catch (IOException e) {}
			
			if (socketChannel != null)
				try {
					socketChannel.close();
				} catch (IOException e) {}
		}
	}

	public void connect(SelectionKey key) throws IOException {
		
		SocketChannel socketChannel = (SocketChannel)key.channel();
		socketChannel.finishConnect();
		
		ByteBuffer buf = ByteBuffer.allocateDirect(Server.BUF_SIZE);
		socketChannel.register(key.selector(), SelectionKey.OP_WRITE, buf);
		
		//trimit serverului tabela cu produse si informatiile publice: username si tip
		writeObject(key, mediator.getTableModel().getDataVector());
		writeObject(key, mediator.getClientPublicInfo());
	}
	
	/**
	 * Primirea unui obiect de la server
	 * 
	 * @param key
	 * @return
	 */
	public Object readObject(SelectionKey key)
	{
		SocketChannel socketChannel = (SocketChannel) key.channel();
		
	    ByteBuffer buffer = ByteBuffer.allocate(8192);

	    int bytesRead;
	    try{
	    	while((bytesRead = socketChannel.read(buffer)) == 0);

	    	buffer.flip();
	        InputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
	        ObjectInputStream ois = new ObjectInputStream(bais);
	        Object obj = (Object)ois.readObject();
	        ois.close();
	        buffer.clear();
	        return obj;
		        
	    }
	    catch(Exception e){
	    	System.err.println("Exception in readObject");
	    	try {
				socketChannel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

	    }
	    return null;
	}

		
	/**
	 * Trimite informatii necesare pentru o comunicare mai facila
	 *  la server imediat dupa ce s-a conectat
	 * 
	 * @param key
	 */
	public void writeObject(SelectionKey key, Object obj)
	{
		SocketChannel socketChannel	= (SocketChannel)key.channel();

		key.interestOps(SelectionKey.OP_WRITE);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
	    try {
	        oos = new ObjectOutputStream(baos);
	    } catch(Exception e) {
	    	System.err.println("Could not create object output stream. Aborting...");
	        return;
	    }
		ByteBuffer buffer;	
        try {
        	
            oos.writeObject(obj);
            buffer = ByteBuffer.wrap(baos.toByteArray());
            socketChannel.write(buffer); 
            oos.flush();
            baos.flush();
        } catch(Exception e){
        	System.err.println("Could not parse object.");
	    	try {
				socketChannel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

        }
		key.interestOps(SelectionKey.OP_READ);
	
	}
	
	public SelectionKey getKey()
	{
		return key;
	}
}
