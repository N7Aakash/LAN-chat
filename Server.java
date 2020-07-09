import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server{
    static Map<String,Socket> chatUsers = new HashMap<>();;
	public static void main(String[] args) throws Exception
	 {	
		try(ServerSocket serverSocket=new ServerSocket(7777))
		{	
			while(true){
				/*Socket requests at port 7777 accepted here**/
				Socket socket = serverSocket.accept();
				socket.setKeepAlive(true);
				
				/*Sender's name initialized here*/
				InputStream in = socket.getInputStream();
				byte[] name = new byte[100]; 
				in.read(name);
				String sender = new String(name).trim();
				System.out.println(sender);

				/*New Sender or offline sender is registered in hashMap*/
				if (!chatUsers.containsKey(sender) || chatUsers.get(sender).isClosed()) {
					chatUsers.put(sender,socket);
				}

				/*Every message sent by client will first get processed through this thread**/
				new ServerProtocolThread(socket,sender,chatUsers).start();
			}

		}catch(IOException e)
		{
			System.out.println("Issue:"+e);
		}
	}
}