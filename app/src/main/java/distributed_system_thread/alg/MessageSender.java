package distributed_system_thread.alg;

import java.io.PrintWriter;

public class MessageSender extends Thread {
    private PrintWriter output;
    private long fatherThreadId;

    public MessageSender(PrintWriter output, long fatherThreadId) {
        this.output = output;
        this.fatherThreadId = fatherThreadId;
    }

    @Override
    public void run() {
        while (true) {
            try {
                var sleepTime = sleepTime();
                Thread.sleep(sleepTime);
                output.println("Oi, sou a thread " + fatherThreadId + " e estou enviando mensagem depois de " + sleepTime + " milisegundos!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int sleepTime() {
        return (int) (Math.random() * 5000) + 1000;
    }
}
