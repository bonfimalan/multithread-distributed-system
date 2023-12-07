package distributed_system_thread.node;

import distributed_system_thread.alg.Agreement;
import distributed_system_thread.alg.AgreementWorker;
import distributed_system_thread.node.reveiver.AgreementReceiver;
import distributed_system_thread.node.sender.AgreementSender;
import distributed_system_thread.node.sender.SenderHandler;
import distributed_system_thread.port.PortHandlerService;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;


public class Node extends Thread {
    @Getter
    private final ServerSocket server;
    private final PortHandlerService portHandler;

    public Node(ServerSocket server, PortHandlerService portHandler) {
        this.server = server;
        this.portHandler = portHandler;
    }

    @Override
    public void run() {
        var agreement = new Agreement(portHandler.getPorts().size());
        var sender = new AgreementSender(agreement, portHandler.getPorts(), server.getLocalPort());
        sender.start();

        agreement.setSender(sender);
        var agreementWorker = new AgreementWorker(agreement, getId());
        agreementWorker.start();

        while (!agreement.decided()) {
            try {
                var client = server.accept();

                var receiveHandler = new AgreementReceiver(client, super.getId(), agreement);
                receiveHandler.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
