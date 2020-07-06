import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;

public class Echoer extends Thread{
	private Socket socket;
	private String sender;
	private String reciever;
	private Map<String,Socket> chatUser;
	Echoer(Socket socket){
		this.socket=socket;
	}
	Echoer(Socket socket,String sender,String reciever,Map<String,Socket> chatUser){
		this.socket = socket;
		this.sender = sender;
		this.reciever = reciever;
		this.chatUser = chatUser;
	}
	public void run(){
		try{
			BufferedReader input=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true)
			{
				String echoString = input.readLine();
				if("exit".equals(echoString))
					break;
				if (chatUser!=null && chatUser.get(reciever)!=null) {
					new EchoerOutput(chatUser.get(reciever),sender + ":" + echoString).start();
				}
				System.out.println(echoString);	
			}
		}catch(IOException e){
			System.out.println("Issue:"+e);
		}
		catch(Exception e){
			System.out.println("Issue:"+e);
		}
		// finally{
		// 	try{
		// 		socket.close();
		// 	}catch(IOException e){
		// 		System.out.println("Issue:"+e);
		// 	}
		// }
	}
}