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
import types.PacketType;
import types.UserPublicInfo;
import control.*;

public class Server {
	
	public static final int BUF_SIZE	= 1024;			// buffer size
	public static final String IP		= "127.0.0.1";	// server IP
	public static final int PORT		= 30000;		// server port
	
	public static ExecutorService pool = Executors.newFixedThreadPool(5);	// thread pool - 5 threads
	
	private static Object[] headTable = { "Produs/Serviciu", "Status" ,"Furnizori", "StatusLicitatie", "Pret", "TranferProgress"};
	public HashMap<SelectionKey, ClientInformation> clientsMap = new HashMap<SelectionKey, ClientInformation>(); 
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
	 * Primesc informatii de la client
	 * 
	 * @param key
	 * @return
	 */
	public Object readObject(SelectionKey key)
	{
		SocketChannel socketChannel = (SocketChannel) key.channel();
		
	    ByteBuffer buffer = ByteBuffer.allocate(8192);

	    int bytesRead ;
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
	    	System.err.println("Exceptie la server read");
	    	try {
				socketChannel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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

		key.interestOps(SelectionKey.OP_READ); //a scris => isi schimba interesul pt citit
			
	}
	
	public void processClientRequest(Packet packet, ClientInformation clInfo)
	{
		String msg = packet.msg;
		System.out.println("[Server] Am primit " + msg + " de la " + clInfo.getUsername());
		
		if(msg.equals("Transfer"))
		{
			//redirectionez pachetul mai departe
			System.out.println("Redirectionez pachetul de transfer catre " + packet.to);
			SelectionKey key = getUserKey(packet.to);
			if(key != null)
				writeObject(key, packet);
			else {
				System.out.println("Serverul nu a putut gasi clientul cerut");
			}
			return;
		}

		Command cmd = new LaunchRequest(this);
		
		if(msg.equals("Launch Offer request"))
			cmd = new LaunchRequest(this);
		else if(msg.equals("Drop Offer request"))
			cmd = new DropRequest(this);
		else if(msg.equals("Make offer"))
		{
			clInfo.tableModel.setValueAt(packet.price, packet.tableRow, 4);
			cmd = new MakeOffer(this);
		}
		else if(msg.equals("Drop auction"))
			cmd = new DropAuction(this);
		else if(msg.equals("Accept Offer"))
			cmd = new AcceptOffer(this);
		else if(msg.equals("Refuse Offer"))
			cmd = new RefuseOffer(this);
		else if(msg.equals("View Best Offer"))
			cmd = new ViewBestOffer(this);		
		
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
							server.processClientRequest(packet, clInfo);
						}
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
	
	public void sendFileRequest(String from, String to, Object product, int fromRow, int toRow) 
	{
		
		Packet toSend = new Packet(PacketType.TRANSFER, "Transfer", product, from, to, fromRow, toRow);
		writeObject(getUserKey(from), toSend);
		
	}
	
	/**
	 * Metoda ce cauta cheia asociata unui client dupa username
	 * 
	 * @param userName
	 * @return
	 */
	SelectionKey getUserKey(String userName)
	{
		for(int i = 0; i < getUsers().size(); i++) {			
			ClientInformation user = getUsers().get(i);
			if(user.getUsername().equals(userName)){
				return user.key;
			}
		}
		return null;
	}
}