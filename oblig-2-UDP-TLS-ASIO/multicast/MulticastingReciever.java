import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MulticastingReciever {

    MulticastSocket ms = null;
    String multicastAddr = null;

    public MulticastingReciever(String multicastAddr, int port) throws IOException {
        // opens a Multicast socket on the specified port
        this.ms = new MulticastSocket(port);
        this.multicastAddr = multicastAddr;
        ms.joinGroup(InetAddress.getByName(multicastAddr));
    }

    public void recieveMessage() throws IOException {
        byte[] buf = new byte[1024];

        // constructs a datagram packet for recieving the packets of specified length
        DatagramPacket dp = new DatagramPacket(buf, buf.length);

        ms.receive(dp);
        String str = new String(dp.getData(), 0, dp.getLength());

        System.out.println("Message from multicasting server: " + str);
    }

    public void leaveGroup() throws UnknownHostException, IOException {
        //leave the group
        ms.leaveGroup(InetAddress.getByName(this.multicastAddr));
        //Closing the datagram socket
        ms.close();

    }
    




    public static void main(String[] args) throws IOException {
        
        String group = "226.4.5.6";

        MulticastingReciever mr = new MulticastingReciever(group, 5000);
        mr.recieveMessage();
        mr.leaveGroup();
    }
}