import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncSocketServerFuture {

    private AsynchronousServerSocketChannel server;
    private InetSocketAddress address;

    public AsyncSocketServerFuture(String address, int port) {
        this.address = new InetSocketAddress(address, port);
    }

    public void run() throws IOException, InterruptedException, ExecutionException {
        server = AsynchronousServerSocketChannel.open();
        server.bind(this.address);

        for(int i = 0; i < 5; i ++){
            //initiate the accepting of connections to the channel's socket
            Future<AsynchronousSocketChannel> acceptFuture = server.accept();
            System.out.println("Waiting for client to connect!");

            //Do some computations
            
            AsynchronousSocketChannel clientChannel = acceptFuture.get();
            System.out.println("Client connected from " + clientChannel.getLocalAddress() + "\n");

            new AsyncSocketServerFutureThread(clientChannel).start();
            
        }
        server.close();
        
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        AsyncSocketServerFuture server = new AsyncSocketServerFuture("localhost", 4555);
        server.run();
    }
}
