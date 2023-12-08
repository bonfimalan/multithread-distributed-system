package distributed_system_thread.node.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import distributed_system_thread.App;
import distributed_system_thread.alg.Agreement;
import distributed_system_thread.domain.AgreementEntity;
import distributed_system_thread.util.Utils;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.Semaphore;

@RequiredArgsConstructor
public class AgreementSender extends Thread {
    private final Agreement agreement;
    private final List<Integer> ports;
    private final int currentPort;

    private Semaphore lock = new Semaphore(0);

    @Override
    public void run() {
        System.out.println("Sender first execution port " + currentPort);
        while (agreement.shouldRun()) {
            try {
                var mapper = new ObjectMapper().writer();
                var choice = propose();
                var round = agreement.getRound();
                var message = mapper.writeValueAsString(new AgreementEntity(round, choice));

                ports.stream()
                        .map(port -> new SenderWorker(port, message))
                        .forEach(Thread::start);
                System.out.println("Choose entity " + message + " Port " + currentPort);
                lock();
                System.out.println("Sender unlocked! Port " + currentPort);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException ignored) {

            }
        }
    }

    private int propose() {
        return Utils.randomBetween(1, App.NUMBER_OF_OPTIONS);
    }

    private void lock() throws InterruptedException {
        lock.acquire();
    }

    public void unlock() {
        if(lock.availablePermits() < 1) {
            lock.release();
        }
    }
}
