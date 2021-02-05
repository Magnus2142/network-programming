import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buffer = new byte[256];

    public UDPServer(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }


    public void run() throws IOException {
        System.out.println("Waiting for client to connect..");
        running = true;

        while(running){
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            //Waiting until we recieve a packet from a client.
            socket.receive(packet);
            System.out.println("Client connected\n");

            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();
            buffer = "Welcome! Enter two numbers that you want to\nsubtract or add. Example 1: 12 + 8\nExample 2: 12 - 8\nWrite exit to quit\n".getBytes();
            
            //Sends a message back that we recieved the request
            packet = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
            socket.send(packet);

            String recieved = "";
            while(!recieved.equals("exit")){
                packet = new DatagramPacket(buffer, buffer.length);
                //Waiting for response from client
                socket.receive(packet);

                recieved = new String(packet.getData(), 0, packet.getLength());

                String[] calculation = recieved.split(" ");

                if(recieved.equals("exit")){
                    buffer = new byte[512];
                    packet = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                    socket.send(packet);
                    recieved = "exit";
                }else if(calculation.length == 1){
                    buffer = "Wrong format, exiting...".getBytes();
                    packet = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                    socket.send(packet);
                    recieved = "exit";
                }else if(!calculation[1].equals("+") && !calculation[1].equals("-")){
                    buffer = "Can only add or subtract the numbers: (+ or -) or wrong format, exiting...".getBytes();
                    packet = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                    socket.send(packet);
                    recieved = "exit";
                }else{
                    int answer;
                    if(calculation[1].equals("+")){
                        answer = Integer.parseInt(calculation[0]) + Integer.parseInt(calculation[2]);
                    }else{
                        answer = Integer.parseInt(calculation[0]) - Integer.parseInt(calculation[2]);
                    }
                    String ans = "Answer to your calculation is: " + answer + "\nYou can write a new calculation or exit to exit";
                    buffer = ans.getBytes();
                    packet = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                    socket.send(packet);
                }
            }

            running = false;

        }
        socket.close();

    }
    public static void main(String[] args) throws IOException {

        UDPServer server = new UDPServer(4445);
        server.run();
    }
}
