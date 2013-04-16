package network;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.table.DefaultTableModel;

import types.Packet;
import types.UserPublicInfo;
import control.*;

public class Server {
	
	public static final int BUF_SIZE	= 1024;			// buffer size
	public static final String IP		= "127.0.0.1";	// server IP
	public static final int PORT		= 30000;		// server port
	
	public static ExecutorService pool = Executors.newFixedThreadPool(5);	// thread pool - 5 threads
	
	private static Object[] headTable = { "Produs/Serviciu", "Status" ,"Furnizori", "StatusLicitatie", "Pret", "TranferProgress"};
	public HashMap<SelectionKey, ClientInformation> clientsMap = new HashMap<SelectionKey, ClientInformation>(); 
	Packet currentPacket; //pachet care urmeaza sa fie trimis unui client
	public HashMap<SelectionKey, ArrayList<Packet> > packets = new HashMap<SelectionKey, ArrayList<Packet>>();
	StatusManager statManager = new StatusManager();
	
	public void accept(SelectionKey key) throws IOException {
		
		System.out.print("ACCEPT: ");
		
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel(); // initialize from key
		SocketChannel socketChannel = serverSocketChannel.accept();				// initialize from accept

		socketChannel.configureBlocking(false);
		ByteBuffer buf = ByteBuffer.allocateDirect(BUF_SIZE);

		socketChannel.register(key.selector(), SelectionKey.OP_READ, buf);
		
		// display remote client address
		System.out.println("Connection from: " + socketChannel.socket().getRemoteSocketAddress());
		
	}
	
	/**
	 * primesc informatii suplimentare de la client
	 * 
	 * @param key
	 * @return
	 */
	public Object readObject(SelectionKey key)
	{
		SocketChannel socketChannel = (SocketChannel) key.channel();
		
		DefaultTableModel model = null;
	    ByteBuffer buffer = ByteBuffer.allocate(8192);

	    int bytesRead ;//= socketChannel.read(buffer);
	    try{
	    	while((bytesRead = socketChannel.read(buffer)) == 0);
//	    		System.out.println("N-am primit...");
	    	{
	    		buffer.flip();
		        InputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
		        ObjectInputStream ois = new ObjectInputStream(bais);
		        Object obj = (Object)ois.readObject();
		        ois.close();
		        buffer.clear();
		        return obj;
		        
	    	}
	    }
	    catch(Exception e){
	    	System.err.println("Exceptie la server read");
	    	e.printStackTrace();
	    }
	    return null;
	}
	
	/**
	 * Functie folosita pentru a trimite un obiect @obj
	 * pe canalul asociat cheii @key
	 * 
	 * @param key
	 */
	public void writeObject(SelectionKey key, Object obj)
	{
		System.out.println("[Server] WRITE- object: ");
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
            oos.writeObject(obj);
            buffer = ByteBuffer.wrap(baos.toByteArray());
            socketChannel.write(buffer); 
            oos.flush();
            baos.flush();
        }catch(Exception e){
            System.err.println("Could not parse object.");
            e.printStackTrace();
        }

		key.interestOps(SelectionKey.OP_READ); //a scris => isi schimba interesul pt citit
			
	}

	public void read(SelectionKey key) throws IOException {
		
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
//				Command cmd = new LaunchRequest(this);
//				
//				if(msg.equals("Launch Offer request"))
//					cmd = new LaunchRequest(this);
//				else if(msg.equals("Drop Offer request"))
//					cmd = new DropRequest(this);
//				else if(msg.equals("Make offer"))
//					cmd = new MakeOffer(this);
//				else if(msg.equals("Drop auction"))
//					cmd = new DropAuction(this);
//				else if(msg.equals("Accept Offer"))
//					cmd = new AcceptOffer(this);
//				else if(msg.equals("Refuse Offer"))
//					cmd = new RefuseOffer(this);
//				else if(msg.equals("View Best Offer"))
//					cmd = new ViewBestOffer(this);
//				else // este un transfer
//				{
//					cmd = new TransferProgress(this);
//					((TransferProgress)cmd).value = Integer.parseInt(msg);
//				}
//				
//				statManager.processRequest(cmd, tableRow, tableCol, userPanel);

				//intorc raspunsul
				key.interestOps(SelectionKey.OP_WRITE);
				
			}
		} catch (IOException e) {
			System.out.println("Connection closed: " + e.getMessage());
			socketChannel.close();
			
		}
		
	}
	
	public void write(SelectionKey key) throws IOException {
		
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
	
	public void processClientRequest(Packet packet, ClientInformation clInfo)
	{
		String msg = packet.msg;
		Command cmd = new LaunchRequest(this);
		
		if(msg.equals("Launch Offer request"))
			cmd = new LaunchRequest(this);
		else if(msg.equals("Drop Offer request"))
			cmd = new DropRequest(this);
		else if(msg.equals("Make offer"))
			cmd = new MakeOffer(this);
		else if(msg.equals("Drop auction"))
			cmd = new DropAuction(this);
		else if(msg.equals("Accept Offer"))
			cmd = new AcceptOffer(this);
		else if(msg.equals("Refuse Offer"))
			cmd = new RefuseOffer(this);
		else if(msg.equals("View Best Offer"))
			cmd = new ViewBestOffer(this);
		else // este un transfer
		{
			cmd = new TransferProgress(this);
			((TransferProgress)cmd).value = Integer.parseInt(msg);
		}
		statManager.processRequest(cmd, packet.tableRow, packet.tableCol, clInfo);

	}
	
	public static void main(String[] args) {
		
		Selector selector						= null;
		ServerSocketChannel serverSocketChannel	= null;

		Server server = new Server();
		
		try {
			selector = Selector.open();
			
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
						server.accept(key);
					else if (key.isReadable())
					{
						if(!server.clientsMap.containsKey(key)) //client nou (abia s-a conectat)
						{
							Vector data = (Vector)server.readObject(key);
					        System.out.println(data.get(0));
					        UserPublicInfo userInfo = (UserPublicInfo)server.readObject(key);
					        System.out.println("A primit " + userInfo);
					        DefaultTableModel model = new DefaultTableModel(data, new Vector(Arrays.asList(headTable)));
					        server.clientsMap.put(key, new ClientInformation(userInfo, model, key));
					        System.out.println((ClientInformation)server.clientsMap.get(key));
						}
						else {
							Packet packet = (Packet)server.readObject(key);
							ClientInformation clInfo = server.clientsMap.get(key);
							System.out.println("[SRV] A primit de  la client  " + clInfo.getUsername());
							server.processClientRequest(packet, clInfo);
						}
					}
//					else if (key.isWritable())
//					{
//						server.writeObject(key, server.getCurrentPacket(key));
//					}
						
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
	
	public ArrayList<ClientInformation> getUsers()
	{
		ArrayList<ClientInformation> users = new ArrayList<ClientInformation>();
		for (Map.Entry<SelectionKey, ClientInformation> entry : clientsMap.entrySet())
		{
			users.add(entry.getValue());
		}
		return users;
	}

	public void addPacketToSend(SelectionKey key, Packet packet)
	{
		ArrayList<Packet> packetList;
		if( this.packets.containsKey(key) )//mai exista si alte mesaje de trimis catre acest client
			packetList = this.packets.get(key);
		else
			packetList = new ArrayList<Packet>();
		
		packetList.add(packet);//adaug la coada
		this.packets.put(key, packetList);		
	}
	
	public Packet getCurrentPacket(SelectionKey key)
	{
		ArrayList<Packet> packetList;
		packetList = this.packets.get(key);
		Packet p = packetList.remove(0);//scot primul mesaj din list
		this.packets.put(key, packetList);
		
		return p;
	}
}