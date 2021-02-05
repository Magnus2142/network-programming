import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UDPClient {

    String hostname;
    int port;

    public UDPClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void run() throws IOException {
        InetAddress address = InetAddress.getByName(hostname);
        DatagramSocket socket = new DatagramSocket();

        byte[] buffer = new byte[512];

        //Sender signal om å at klienten vil ha noe data
        DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(request);

        //SVar fra server
        DatagramPacket response = new DatagramPacket(buffer, buffer.length);
        socket.receive(response);

        String answer = new String(response.getData(), 0, response.getLength());
        System.out.println(answer);

        boolean running = true;
        Scanner in = new Scanner(System.in);

        while(running){
            String calculation = in.nextLine();
            if(calculation.equals("exit")){
                running = false;
            }
            buffer = calculation.getBytes();
            DatagramPacket calculationPacket = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(calculationPacket);

            buffer = new byte[512];
            DatagramPacket calcResponsePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(calcResponsePacket);
            try {
                Integer.parseInt(new String(calcResponsePacket.getData(), 0, calcResponsePacket.getLength()));
            } catch (Exception e) {
                running = false;
            }
            String calcAnswer = new String(calcResponsePacket.getData(), 0, calcResponsePacket.getLength());
            System.out.println(calcAnswer);
        }

        socket.close();
        in.close();
    }

    public static void main(String[] args) throws IOException {


        UDPClient client = new UDPClient("localhost", 4445);
        client.run();

        
    }
}