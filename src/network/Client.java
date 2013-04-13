package network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

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
					
					if (key.isConnectable())
						connect(key);
					this.key = key;
//					else if (key.isReadable())
//						read(key);

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
		
		writeClientTable(key);
//		write(key, "eu");
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
				
				//intorc raspunsul
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
	
	public void writeClientTable(SelectionKey key)
	{
		System.out.println("WRITE- client: ");
		SocketChannel socketChannel	= (SocketChannel)key.channel();
		
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
            oos.writeObject(mediator.getTableModel());
            buffer = ByteBuffer.wrap(baos.toByteArray());
            socketChannel.write(buffer);
            oos.flush();
            baos.flush();
        }catch(Exception e){
            System.err.println("Could not parse object.");
            e.printStackTrace();
        }

		//key.interestOps(SelectionKey.OP_READ);
			
	}


}
