package distributed_system_thread;

import distributed_system_thread.node.Node;
import distributed_system_thread.port.PortHandlerService;

import java.util.ArrayList;
import java.util.List;

public class App {
    private static final List<Thread> threadPoll = new ArrayList<>();
    private static final PortHandlerService portHandlerService = new PortHandlerService();

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 5; i++) {
            threadPoll.add(new Node(portHandlerService.createServer(), portHandlerService));
        }

        for (Thread t : threadPoll) {
            t.start();
        }

        for (Thread t : threadPoll) {
            t.join();
        }
    }
}
