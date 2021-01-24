import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {

    Socket client;

    public ServerThread(Socket clientSocket){
        this.client = clientSocket;
    }

    @Override
    public void run() {
        InputStreamReader input = null;
        BufferedReader reader = null;
        PrintWriter writer = null;

        try{
            input = new InputStreamReader(client.getInputStream());
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

            System.out.println("Client disconnected");
            
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
                try{
                    writer.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            try{
                client.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
}
