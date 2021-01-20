import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    Socket connection = null;

    InputStreamReader input = null;
    BufferedReader reader = null;
    PrintWriter writer = null;

    public void connect(String serverName, int port){

        try {
            Scanner in = new Scanner(System.in);
            this.connection = new Socket(serverName, port);
            System.out.println("Connection established!");

            input = new InputStreamReader(connection.getInputStream());
            reader = new BufferedReader(input);
            writer = new PrintWriter(connection.getOutputStream(), true);

            String introLine1 = reader.readLine();
            String introLine2 = reader.readLine();
            System.out.println(introLine1 + "\n" + introLine2);

            String inputLine = in.nextLine();
            writer.println(inputLine);

            while(!inputLine.equals("exit")){
                String respons = reader.readLine();
                System.out.println("From server: " + respons);

                inputLine = in.nextLine();
                writer.println(inputLine);

                String answer = reader.readLine();
                String question = reader.readLine();
                System.out.println("From server: " + answer + "\n" + question);
                
                inputLine = in.nextLine();
                writer.println(inputLine);
            }



            reader.close();
            writer.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args){

        Scanner in = new Scanner(System.in);
        System.out.println("Enter the name to the machine where the server is run: ");
        String serverName = in.nextLine();

        Client client = new Client();
        client.connect(serverName, 1250);




    }
}