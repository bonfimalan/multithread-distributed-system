package distributed_system_thread.node.sender;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.Semaphore;

@RequiredArgsConstructor
public class SenderHandler extends Thread {
    private final List<Integer> ports;
    private final long nodeId;
    private final int currentPort;

    @Override
    public void run() {
        while (true) {
            try {
                var sleepTime = sleepTime();
                Thread.sleep(sleepTime);

                var message = "Oi, sou a thread " + nodeId + " e estou enviando mensagem depois de " + sleepTime + " milisegundos!";

                ports.stream()
                        .filter(port -> port != currentPort)
                        .map(port -> new SenderWorker(port, message))
                        .forEach(Thread::start);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int sleepTime() {
        return (int) (Math.random() * 5000) + 1000;
    }
}
