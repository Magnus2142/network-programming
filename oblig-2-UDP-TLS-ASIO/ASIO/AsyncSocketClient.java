import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;
import java.util.concurrent.Future;

public class AsyncSocketClient{


    private AsynchronousSocketChannel client;

    public void init(String ip, int port) throws Exception{
        //The asynchronous socket channel is open, but we need to bind it aswell to an address and port
        client = AsynchronousSocketChannel.open();
        //Creating the address to connect to
        InetSocketAddress hostAddress = new InetSocketAddress(ip, port);
        //starts to connect to the server, but since it is asynchronous the client will 
        //keep running and execute other code until it needs the return value of the Future object.
        Future<Void> future = client.connect(hostAddress);

        future.get();//Waits for connection
        ByteBuffer welcomeBuffer = ByteBuffer.allocate(256);
        Future<Integer> readResult = client.read(welcomeBuffer);

        //DO some computations if you want

        readResult.get();
        String welcomeMessage = new String(welcomeBuffer.array()).trim();
        System.out.println(welcomeMessage + "\n");
    }

    public String sendMessage(String message) throws Exception {
        byte[] byteMsg = new String(message).getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(byteMsg);
        Future<Integer> writeResult = client.write(buffer);

        //Do some computations here because the asyncrhonous socket makes the method return right away.
        writeResult.get(); //Here however we have to wait for the result
        buffer.flip();
        Future<Integer> readResult = client.read(buffer);

        //Do some computations before getting the read result...
        readResult.get();
        String echo = new String(buffer.array()).trim();
        buffer.clear();
        return echo;
    }

    public void cleanUp() throws IOException{
        client.shutdownInput();
        client.shutdownOutput();
        client.close();
    }

    public static void main(String[] args) throws Exception {

        AsyncSocketClient client = new AsyncSocketClient();
        client.init("localhost", 4555);
        Scanner in = new Scanner(System.in);

        String input = "";
        while(!input.equals("exit")){
            input = in.nextLine();
            System.out.println(client.sendMessage(input));
        }
    }
}