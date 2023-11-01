package distributed_system_thread;

import distributed_system_thread.node.Node;
import distributed_system_thread.server.DistributedServer;

import java.util.ArrayList;
import java.util.List;

public class App {
    private static final List<Thread> threadPoll = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        threadPoll.add(new DistributedServer());
        for(int i = 0; i < 5; i++) {
            threadPoll.add(new Node());
        }

        for (Thread t : threadPoll) {
            t.start();
        }

        for (Thread t : threadPoll) {
            t.join();
        }
    }
}
