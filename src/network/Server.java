package network;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	public static final int BUF_SIZE	= 1024;			// buffer size
	public static final String IP		= "127.0.0.1";	// server IP
	public static final int PORT		= 30000;		// server port
	
	public static ExecutorService pool = Executors.newFixedThreadPool(5);	// thread pool - 5 threads
	
	public static void accept(SelectionKey key) throws IOException {
		
		System.out.print("ACCEPT: ");
		
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel(); // initialize from key
		SocketChannel socketChannel = serverSocketChannel.accept();				// initialize from accept

		socketChannel.configureBlocking(false);
		ByteBuffer buf = ByteBuffer.allocateDirect(BUF_SIZE);

		socketChannel.register(key.selector(), SelectionKey.OP_READ, buf);
		
		// display remote client address
		System.out.println("Connection from: " + socketChannel.socket().getRemoteSocketAddress());
	}
	
	public static void read(SelectionKey key) throws IOException {
		
		System.out.print("READ: ");
		
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
				String msg = "";
				Channels.newChannel(System.out).write(buf);

				System.out.println();
				
	//			if(msg.equals("Launch Offer request"))
	//				cmd = new LaunchRequest(this);
	//			else if(msg.equals("Drop Offer request"))
	//				cmd = new DropRequest(this);
	//			else if(msg.equals("Make offer"))
	//				cmd = new MakeOffer(this);
	//			else if(msg.equals("Drop auction"))
	//				cmd = new DropAuction(this);
	//			else if(msg.equals("Accept Offer"))
	//				cmd = new AcceptOffer(this);
	//			else if(msg.equals("Refuse Offer"))
	//				cmd = new RefuseOffer(this);
	//			else if(msg.equals("View Best Offer"))
	//				cmd = new ViewBestOffer(this);
	//			else // este un transfer
				
				//intorc raspunsul
				key.interestOps(SelectionKey.OP_WRITE);
				
			}
		} catch (IOException e) {
			System.out.println("Connection closed: " + e.getMessage());
			socketChannel.close();
			
		}
		
	}
	
	public static void write(SelectionKey key) throws IOException {
		
		System.out.println("WRITE: ");
		String s = "Am primit";
		SocketChannel socketChannel	= (SocketChannel)key.channel();
		ByteBuffer buf	= ByteBuffer.allocate(s.length());
		buf.clear();
		buf.put(s.getBytes());
		
		buf.flip();
		while(socketChannel.write(buf) > 0);
		
		key.interestOps(SelectionKey.OP_READ);
		
	}
	
	public static void main(String[] args) {
		
		Selector selector						= null;
		ServerSocketChannel serverSocketChannel	= null;
		
		try {
			selector = Selector.open();
			
			// TODO 2.3: init server socket and register it with the selector
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(IP, PORT));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			// main loop
			while (true) {
				// wait for something to happen
				selector.select();
				
				// iterate over the events
				for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
					// get current event and REMOVE it from the list!!!
					SelectionKey key = it.next();
					it.remove();
					
					if (key.isAcceptable())
						accept(key);
					else if (key.isReadable())
						read(key);
					else if (key.isWritable())
						write(key);
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
			
			if (serverSocketChannel != null)
				try {
					serverSocketChannel.close();
				} catch (IOException e) {}
		}

	}

}