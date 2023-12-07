package distributed_system_thread.util;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

@RequiredArgsConstructor
public class CloseServersHook extends Thread {
    private final List<ServerSocket> serverSockets;
    @Override
    public void run() {
        System.out.println("Closing server sockets");
        serverSockets.forEach(socket -> {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
