import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client{
	public static void main(String[] args) {
		try{
			Socket socket = new Socket("localhost",7777);
			BufferedReader input=new BufferedReader(new InputStreamReader(System.in));

			OutputStream out = socket.getOutputStream();
			System.out.println("Please Enter your name:");
			byte[] name = input.readLine().getBytes();
			out.write(name);

		    new Echoer(socket).start();	
			new EchoerOutput(socket).start();
			
		}catch(SocketTimeoutException e){
			System.out.println("Issue:"+e);
		}catch(IOException e){
			System.out.println("Issue:"+e);
		}
	}
}