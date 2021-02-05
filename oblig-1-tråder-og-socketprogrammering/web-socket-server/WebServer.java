import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class WebServer {
    

    private static void handleClient(Socket client){
        System.out.println("A client connected.");

        InputStreamReader input = null;
        BufferedReader reader = null;
        PrintWriter writer = null;

        try{
            input = new InputStreamReader(client.getInputStream());
            reader = new BufferedReader(input);
            writer = new PrintWriter(client.getOutputStream());

            StringBuilder requestBuilder = new StringBuilder();
            String line;

            while(!(line = reader.readLine()).isBlank()){
                requestBuilder.append(line + "\r\n");
            }

            String[] request = parseRequest(requestBuilder.toString());
            System.out.println(requestBuilder.toString());


            writer.print("HTTP/1.1 200 OK\r\n");
            writer.print("Content-Type: text/html; charset=UTF-8\r\n\r\n");

            writer.print("<html><body>\r\n");
            writer.print("<h1>Welcome! You are now connected to my simple web-server!</h1>\r\n");
            writer.print("Request header from client is: \r\n");
            writer.print("<ul>\r\n");
            for(int i = 0; i < request.length; i ++){
                writer.print("<li>" + request[i] + "</li>\r\n");
            }
            writer.print("</ul>\r\n");
            writer.print("</body></html>\r\n");
            writer.print("\r\n\r\n");

            writer.flush();

            System.out.println("Welcome message sent.");

        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader != null){
                try{
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(writer != null){
                writer.close();
            }
            try{
                client.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private static String[] parseRequest(String request){
        String[] requestLines = request.split("\r\n");
        return requestLines;
    }



    public static void main(String[] args){
        
        ServerSocket server = null;

        try{
            server = new ServerSocket(80);
            System.out.println("Server has started on 10.0.0.14. \r\nWaiting for a conncetion...");
            Socket client = server.accept();
            handleClient(client);
            
        }catch(Exception e){
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
}
