package network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import types.Packet;
import types.UserPublicInfo;

import mediator.INetMediator;

public class Client {
	
	INetMediator mediator;
	String ip;
	int port;
	public SelectionKey key;
	
	public Client(INetMediator med, String ip, int port) {
		this.mediator = med;
		this.ip = ip;
		this.port = port;
	}
	
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
						System.out.println(recvPacket.pType.toString());
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
		
		System.out.println("CONNECTION ESTABLISHED");
		ByteBuffer buf = ByteBuffer.allocateDirect(Server.BUF_SIZE);
		socketChannel.register(key.selector(), SelectionKey.OP_WRITE, buf);
		
		writeObject(key, mediator.getTableModel().getDataVector());
		writeObject(key, mediator.getClientPublicInfo());
	}
	
	public void read(SelectionKey key) {
		
		System.out.print("READ-client: ");
		
		int bytes;
		ByteBuffer buf				= (ByteBuffer)key.attachment();		
		SocketChannel socketChannel	= (SocketChannel)key.channel();
		
		buf.clear();

		try{	
			if((bytes = socketChannel.read(buf))>0){
				
				// check for EOF
				if (bytes == -1)
					throw new IOException("EOF");
				
				buf.flip();
				Channels.newChannel(System.out).write(buf);
				
				//intorc raspuhttp://us-mg4.mail.yahoo.com/neo/launch?.rand=eiq2arl1le508nsul
				key.interestOps(SelectionKey.OP_WRITE);
				
			}
		} catch (IOException e) {
			System.out.println("Connection closed: " + e.getMessage());
			try {
				socketChannel.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		}		
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

	    int bytesRead ;//= socketChannel.read(buffer);
	    try{
	    	while((bytesRead = socketChannel.read(buffer)) == 0);

	    	buffer.flip();
	        InputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
	        ObjectInputStream ois = new ObjectInputStream(bais);
	        Object obj = (Object)ois.readObject();
//	        Packet p = (Packet)obj;
//	        System.out.println("[CLIENT] A primit " + p.pType);
	        ois.close();
	        buffer.clear();
	        return obj;
		        
	    }
	    catch(Exception e){
	    	System.err.println("[CLIENT] Exceptie in readObject");
	    	try {
				socketChannel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

	    }
	    return null;
	}

	
	public void write(SelectionKey key, String msg) {
		
		System.out.println("WRITE- client: ");
		SocketChannel socketChannel	= (SocketChannel)key.channel();

		ByteBuffer buf	= ByteBuffer.allocate(msg.length());
		buf.clear();
		buf.put(msg.getBytes());
		
		buf.flip();
		try {
			while( socketChannel.write(buf) > 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		key.interestOps(SelectionKey.OP_READ);
		
	}
	
	/**
	 * Trimite informatii necesare pentru o comunicare mai facila
	 *  la server imediat dupa ce s-a conectat
	 * 
	 * @param key
	 */
	public void writeObject(SelectionKey key, Object obj)
	{
		System.out.println("WRITE- client: ");
		SocketChannel socketChannel	= (SocketChannel)key.channel();

		key.interestOps(SelectionKey.OP_WRITE);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
	    try{
	        oos = new ObjectOutputStream(baos);
	    }catch(Exception e){
	        System.err.println("Could not create object output stream. Aborting...");
	        return;
	    }
		ByteBuffer buffer;
		
        try{
//        	System.out.println(mediator.getTableModel().getDataVector().get(0));
            oos.writeObject(obj);//mediator.getTableModel().getDataVector());
            buffer = ByteBuffer.wrap(baos.toByteArray());
            socketChannel.write(buffer); 
            oos.flush();
            baos.flush();
        }catch(Exception e){
            System.err.println("Could not parse object.");
	    	try {
				socketChannel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

        }
		key.interestOps(SelectionKey.OP_READ);
	
	}
}
