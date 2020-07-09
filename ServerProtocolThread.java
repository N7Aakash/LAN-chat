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
        this.socket = socket;
        this.chatUser = chatUser;
        this.sender = sender;
    }
    public void run(){
        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
            String inputLine = new String();
            String outputLine = new String();
            while(true){
                /*Input recieved from client **/
                inputLine = in.readLine();
                System.out.println(inputLine);
                if ("exit".equals(inputLine)) {
                    out.println("exit");
                    break;
                }else{
                    /*Reciever's name is extracted from input**/
                    String[] response = inputLine.split(":");
                    String reciever = response[0].trim();
                    /*Condition to check whether the reciever is registered in our system**/
                    if (!chatUser.containsKey(reciever)) {
                        out.println("User does not exists!!");
                    }else{
                        /*Reciever's message is extracted from input**/
                        outputLine = response[1];
                        /*Reciever's socket is extracted from hashMap**/
                        Socket recieverSocket = chatUser.get(reciever);

                        ExecutorService service = Executors.newFixedThreadPool(1);
                        Future<Socket> future = service.submit(new Callable<Socket>(){
                            public Socket call() throws Exception{
                                /*Waiting for the receiver socket if he/she is offline**/
                                while(chatUser.get(reciever).isClosed()){
                                    continue;
                                }
                                /*Returning Socket if reciever is online**/
                                return chatUser.get(reciever);
                            }
                        });
                        try{
                             /*Future's get method waits until the receiver socket is available**/
                             recieverSocket = future.get();
                             /*Sending message to reciever using reciever's socket outputstream**/
                             PrintWriter output = new PrintWriter(recieverSocket.getOutputStream(), true);
                             output.println("Message from " + sender + " : " + outputLine);   
                        }catch(Exception e){
                            System.out.println("Issue : "+e.getMessage());
                        }
                    }
                }
            }  
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
