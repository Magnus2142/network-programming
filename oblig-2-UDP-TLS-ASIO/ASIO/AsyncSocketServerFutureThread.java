import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncSocketServerFutureThread extends Thread {

    private AsynchronousSocketChannel clientChannel;

    public AsyncSocketServerFutureThread(AsynchronousSocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    public void run() {
        byte[] welcomeMsg = new String(
                "Welcome to this echoing server! Imagine you are yelling at a big mountain wall!\n"
                        + "Write anything you want and we will yell it right back at ya :D").getBytes();
        ByteBuffer welcomeMsgBuffer = ByteBuffer.wrap(welcomeMsg);
        Future<Integer> welcomeMsgResult = clientChannel.write(welcomeMsgBuffer);
        try {
            welcomeMsgResult.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (clientChannel.isOpen()) {

            boolean running = true;
            while (running) {
                ByteBuffer buffer = ByteBuffer.allocate(32);
                Future<Integer> readResult = clientChannel.read(buffer);

                // do some other computations

                try {
                    readResult.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                String fromClient = new String(buffer.array()).trim();
                if (fromClient.equals("exit")) {
                    running = false;
                }

                buffer.flip();
                Future<Integer> writeResult = clientChannel.write(buffer);

                // do some other cool stuff

                try {
                    writeResult.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                buffer.clear();
            }
            try {
                clientChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }   
}