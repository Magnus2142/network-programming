import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2{

    ServerSocket server = null;
    Socket socket = null;
    
    public void open(int port){
        try {
            this.server = new ServerSocket(port);
            System.out.println("Waiting for clients to connect...");

            while(true){
                socket = server.accept();
                System.out.println("Client accepted!");

                new ServerThread(socket).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(server != null){
                try{
                    server.close();
                }catch(IOException e){
                    e.printStackTrace();
                } 
            }
        }
    }

    public static void main(String[] args){
        Server2 server = new Server2();
        server.open(1250);
    }

}