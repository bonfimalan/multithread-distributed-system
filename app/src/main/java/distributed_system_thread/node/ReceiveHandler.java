package distributed_system_thread.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveHandler extends Thread {
    private final Socket socket;
    private final long nodeThreadId;

    public ReceiveHandler(Socket socket, long nodeThreadId) {
        this.socket = socket;
        this.nodeThreadId = nodeThreadId;
    }

    @Override
    public void run() {
        try( var inputBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream())) ) {
            String inputString = inputBuffer.readLine();

            System.out.println("Sou a thread " + nodeThreadId + " e recebi a mensagem: " + inputString);

            socket.close();
        } catch (IOException e) {

        }
    }
}
