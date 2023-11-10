package distributed_system_thread.node.sender;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

@RequiredArgsConstructor
public class SenderWorker extends Thread {
    private final int targetPort;
    private final String message;
    private final static String SERVER_ADDRESS = "localhost";

    @Override
    public void run() {
        try (var socket = new Socket(SERVER_ADDRESS, targetPort)) {
            var socketOut = new PrintWriter(socket.getOutputStream(), true);
            socketOut.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
