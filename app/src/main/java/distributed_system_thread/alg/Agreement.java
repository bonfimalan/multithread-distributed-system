package distributed_system_thread.alg;

import distributed_system_thread.node.sender.AgreementSender;
import distributed_system_thread.port.PortHandlerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

@RequiredArgsConstructor
public class Agreement {
    private List<Integer> choices = new ArrayList<>();
    private final int nodeAmount;
    @Getter
    private int round = 1;
    @Getter
    private int choiceValue = -1;
    private Semaphore writeLock = new Semaphore(1);
    @Setter
    private AgreementSender sender;

    public void addChoice(int choice) {
        try {
            writeLock.acquire();
            this.choices.add(choice);
            writeLock.release();
        } catch (InterruptedException ignored) {}
    }

    public int getSetSize() {
        return choices.size();
    }

    public boolean shouldChoose() {
        return getSetSize() == nodeAmount;
    }

    public void nextRound() {
        round++;
        choices = new ArrayList<>();
        unlockSender();
    }

    public boolean decided() {
        return choiceValue != -1;
    }

    public void unlockSender() {
        sender.unlock();
    }

    public void majority() {
        Map<Integer, Integer> numberCount = new HashMap<>();

        // Count the occurrences of each number
        for (int number : choices) {
            numberCount.put(number, numberCount.getOrDefault(number, 0) + 1);
        }

        // Find the most repeated number and its frequency
        int mostRepeatedNumber = 0;
        int maxFrequency = 0;

        for (Map.Entry<Integer, Integer> entry : numberCount.entrySet()) {
            int number = entry.getKey();
            int frequency = entry.getValue();

            if (frequency > maxFrequency) {
                mostRepeatedNumber = number;
                maxFrequency = frequency;
            }
        }

        if(maxFrequency == (nodeAmount / 2) + 1) {
            choiceValue = mostRepeatedNumber;
        }
        else {
            choiceValue = -1;
        }
    }

}
