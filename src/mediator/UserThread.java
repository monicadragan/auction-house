package mediator;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import gui.*;
import network.*;
import types.*;
/**
 * Clasa ce defineste un utilizator
 */
public class UserThread extends Thread{
	
	Mediator mediator;
	public IMainWindow gui;
	
	public UserThread()
	{
		connect();
	}
	
//	public UserThread(Mediator mediator){
//		this.mediator = mediator;
//	}

	public void run()
	{
		gui = new MainWindow(mediator);
	}
	public void cancel()
	{
		interrupt();
	}
	
	public void createGUI()
	{
		gui = new MainWindow(mediator);
	}
	
	public void connect()
	{
		java.nio.channels.Selector selector			= null;
		SocketChannel socketChannel	= null;
		boolean running = true;
		char fillChar =  'a';

		try {
			selector = Selector.open();
			
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(Server.IP, Server.PORT));
			
			ByteBuffer buf = ByteBuffer.allocateDirect(Server.BUF_SIZE);
			while (buf.hasRemaining())
				buf.put((byte)fillChar);
			buf.flip();
			
			socketChannel.register(selector, SelectionKey.OP_CONNECT, buf);
			
			while (running) {
				selector.select();
				
				for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
					SelectionKey key = it.next();
					it.remove();
					
					if (key.isConnectable())
					{
						System.out.print("CONNECT: ");
						
						SocketChannel sv_socketChannel = (SocketChannel)key.channel();
						if (! sv_socketChannel.finishConnect()) {
							System.err.println("Eroare finishConnect");
							running = false;
						}
						
						//socketChannel.close();
						key.interestOps(SelectionKey.OP_WRITE);
					}
					else if (key.isWritable())
					{
						System.out.println("WRITE: ");
						
						ByteBuffer buff = (ByteBuffer)key.attachment();		
						SocketChannel sv_socketChannel = (SocketChannel)key.channel();
						Packet p = new Packet("hello");
						buff.clear();
						buff.put(p.toString().getBytes());
						while (sv_socketChannel.write(buff) > 0);
						
						
						if (! buff.hasRemaining()) {
							sv_socketChannel.close();
							running = false;
						}
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
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
	
	public static void main(String[] args)
	{
		new UserThread();
	}
	
}