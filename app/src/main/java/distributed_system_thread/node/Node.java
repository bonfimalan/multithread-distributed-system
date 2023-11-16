package distributed_system_thread.node;

import distributed_system_thread.node.sender.SenderHandler;
import distributed_system_thread.port.PortHandlerService;

import java.io.IOException;
import java.net.ServerSocket;


public class Node extends Thread {
    private final ServerSocket server;
    private final PortHandlerService portHandler;

    public Node(ServerSocket server, PortHandlerService portHandler) {
        this.server = server;
        this.portHandler = portHandler;
    }

    @Override
    public void run() {
        var sender = new SenderHandler(portHandler.getPorts(), super.getId(), server.getLocalPort());
        sender.start();

        while (true) {
            try {
                var client = server.accept();

                var receiveHandler = new ReceiveHandler(client, super.getId());
                receiveHandler.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
