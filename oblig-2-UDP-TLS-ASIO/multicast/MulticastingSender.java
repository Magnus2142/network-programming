import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastingSender {

    MulticastSocket ms = null;
    String group = null;
    int port = 0;

    public MulticastingSender(String multicastAddr, int port) throws IOException {
        //Multicast socket that binds to any available port in localhost
        this.ms = new MulticastSocket();
        this.group = multicastAddr;
        this.port = port;
    }

    public void sendMessageToGroup(String message) throws IOException {
        if(ms != null){
            //Create Datagram packet and send
            DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(this.group), this.port);
            this.ms.send(dp);
        }
    }

    public void closeMulticastSocket(){
        if(ms != null){
            this.ms.close();
        }
    }

    public static void main(String[] args) throws IOException {

        //Can pick any multicast address withing the range 224.0.0.0 to 239.255.255.255
        String group = "226.4.5.6";

        String message = "Hello everyone in this group! Stay strong, keen on the grind motherfuckers!!!";

        MulticastingSender ms = new MulticastingSender(group, 5000);
        ms.sendMessageToGroup(message);
        ms.closeMulticastSocket();
    }
}