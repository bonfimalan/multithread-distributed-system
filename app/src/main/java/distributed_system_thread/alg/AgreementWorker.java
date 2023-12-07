package distributed_system_thread.alg;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AgreementWorker extends Thread {
    private final Agreement agreement;
    private final long nodeId;

    @Override
    public void run() {
        while (true) {
            if (agreement.shouldChoose()) {
                agreement.majority();

                if(agreement.decided()) {
                    System.out.println("Thread " + nodeId + "  decided in value " + agreement.getChoiceValue());
                }
                else {
                    agreement.nextRound();
                }
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException ignored) {}

            if (agreement.decided()) break;
        }
    }
}
