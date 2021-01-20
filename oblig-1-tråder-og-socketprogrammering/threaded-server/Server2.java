import java.io.InputStreamReader;
import java.io.BufferedReader;
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 5; i ++){
            
            try {
                socket = server.accept();
                System.out.println("Client accepted!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ServerThread(socket).start();
        }
    }

    public static void main(String[] args){
        Server2 server = new Server2();
        server.open(1250);
    }

}