import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoerOutput extends Thread{
	private Socket socket;
	private String echoString=" ";
	
	EchoerOutput(Socket socket){
		this.socket=socket;
	}

	
	EchoerOutput(Socket socket,String echoString){
		this.socket=socket;
		this.echoString = echoString;
	}
	public void run(){
		try{
			BufferedReader input=new BufferedReader(new InputStreamReader(System.in));
			PrintWriter output=new PrintWriter(socket.getOutputStream(),true);

			while(true)
			{			
				if (this.echoString.equals(" ")) {
					Thread.sleep(500);
					System.out.println("Please enter the name of the person and message separated by :");
					String echo = input.readLine();
					output.println(echo);
				}else{
					output.println(this.echoString);
					this.echoString = " ";
				}
				if(echoString.equals("exit"))
					break;	
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