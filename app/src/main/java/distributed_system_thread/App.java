package distributed_system_thread;

import distributed_system_thread.node.Node;
import distributed_system_thread.port.PortHandlerService;
import distributed_system_thread.util.CloseServersHook;

import java.util.ArrayList;
import java.util.List;

public class App {
    private static final List<Node> threadPoll = new ArrayList<>();
    private static final PortHandlerService portHandlerService = new PortHandlerService();

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 10; i++) {
            threadPoll.add(new Node(portHandlerService.createServer(), portHandlerService));
        }
        var serverSockets = threadPoll.stream()
                .map(Node::getServer)
                .toList();

        for (Thread t : threadPoll) {
            t.start();
        }

        Runtime.getRuntime().addShutdownHook(new CloseServersHook(serverSockets));

        for (Thread t : threadPoll) {
            t.join();
        }
    }
}
