package distributed_system_thread.port;

import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class PortHandlerService {
    @Getter
    private final List<Integer> ports = new ArrayList<>();
    private int portCount = 12345;

    public ServerSocket createServer() {
        try {
            portCount++;
            var server = new ServerSocket(portCount);
            ports.add(portCount);

            return server;
        } catch (IOException e) {
            return createServer();
        }
    }
}
