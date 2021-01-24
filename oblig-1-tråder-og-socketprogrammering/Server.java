import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    ServerSocket server = null;
    Socket client = null;
    
    public void open(int port){
        try {
            this.server = new ServerSocket(port);
            System.out.println("Waiting for clients to connect...");
            
            this.client = this.server.accept();
            handleClient(this.client);
            System.out.println("Client accepted!");

            server.close();

            System.out.println("Client disconnected");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleClient(Socket socket) throws IOException {

        InputStreamReader input = null;
        BufferedReader reader = null;
        PrintWriter writer = null;

        try{
            input = new InputStreamReader(this.client.getInputStream());
            reader = new BufferedReader(input);
            writer = new PrintWriter(client.getOutputStream(), true);

            writer.println("Hello! You do now have contact with the server!");
            writer.println("Write two numbers that you want to add or subtract, for example: 13 37");


            String line = reader.readLine();
            while(!line.equals("exit")){
                String[] numbers = line.split(" ");
                int number1 = Integer.parseInt(numbers[0]);
                int number2 = Integer.parseInt(numbers[1]);

                writer.println("Do you want to add or subtract these numbers? write + or -");
                String userInput = reader.readLine();
                    
                if(userInput.equals("+")){
                    writer.println("Answer is: " + (number1 + number2));
                    writer.println("You may write two more numbers or write exit to quit");
                }else{
                    writer.println("Answer is: " + (number1 - number2));
                    writer.println("You may write two more numbers or write exit to quit");
                }
                line = reader.readLine();
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            
            if(reader != null){
                reader.close();
            }
            if(writer != null){
                writer.close();
            }

            client.close();
            
        }
    }

    public static void main(String[] args){
        Server server = new Server();
        server.open(1250);
    }

}