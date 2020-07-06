import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.net.InetAddress;

public class Server{
    static Map<String,Socket> chatUsers = new HashMap<>();;
	public static void main(String[] args) throws Exception
	 {	
		try(ServerSocket serverSocket=new ServerSocket(7777))
		{	
			while(true){
				Socket socket = serverSocket.accept();
				socket.setKeepAlive(true);
				/*Sender's name */
				InputStream in = socket.getInputStream();
				byte[] name = new byte[100]; 
				in.read(name);
				String sender = new String(name).trim();
				System.out.println(sender);

				/*New Sender*/
				if (!chatUsers.containsKey(sender) || chatUsers.get(sender).isClosed()) {
					chatUsers.put(sender,socket);
				}
				
				new ServerProtocolThread(socket,sender,chatUsers).start();
			}

		}catch(IOException e)
		{
			System.out.println("Issue:"+e);
		}
	}
}