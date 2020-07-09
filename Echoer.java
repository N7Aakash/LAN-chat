import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;

public class Echoer extends Thread{
	private Socket socket;
	Echoer(Socket socket){
		this.socket=socket;
	}
	public void run(){
		try{
			BufferedReader input=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true)
			{
				String echoString = input.readLine();
				if("exit".equals(echoString)){
					break;
				}
				/**Every message processed through ServerProtocolThread is recieved here*/
				System.out.println("Client Echoer: " + echoString);	
			}
		}catch(IOException e){
			System.out.println("Issue:"+e);
		}
		catch(Exception e){
			System.out.println("Issue:"+e);
		}
		finally{
			try{
				socket.close();
			}catch(IOException e){
				System.out.println("Issue:"+e);
			}
		}
	}
}