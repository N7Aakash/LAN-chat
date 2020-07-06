import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerProtocolThread extends Thread {
    private Socket socket = null;
    private String sender = " ";
    private Map<String,Socket> chatUser;
    private ArrayList<String> messages = new ArrayList<>();
    public ServerProtocolThread(Socket socket,String sender,Map<String,Socket> chatUser) {
        super("ServerProtocolThread");
        this.socket = socket;
        this.chatUser = chatUser;
        this.sender = sender;
    }
    
    public void run() {

        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
            String inputLine = new String();
            String outputLine = new String();
            while(true){
                inputLine = in.readLine();
                System.out.println(inputLine);
                String[] response = inputLine.split(":");
                String reciever = response[0];
                outputLine = response[1];
                Socket recieverSocket = chatUser.get(reciever);
                if (!chatUser.containsKey(reciever)) {
                    out.println("User does not exists!!");
                }else{
                    ExecutorService service = Executors.newFixedThreadPool(1);
                    Future<Socket> future = service.submit(new Callable<Socket>(){
                        public Socket call() throws Exception{
                            while(chatUser.get(reciever).isClosed()){
                                continue;
                            }
                            System.out.println(chatUser.get(reciever));
                            return chatUser.get(reciever);
                        }
                    });
                    try{
                         recieverSocket = future.get();
                         PrintWriter output = new PrintWriter(recieverSocket.getOutputStream(), true);
                         output.println(sender + " : " + outputLine);   
                    }catch(Exception e){
                        System.out.println("Issue : "+e.getMessage());
                    }
                    
                    
                }
            }  
        } catch (IOException e) {
            e.printStackTrace();
        // }finally{
        //     try{
        //     // socket.close();
        //     }catch(IOException e){
        //         System.out.println("Issue :" + e.getMessage());
        //     }
        }
    }
}
