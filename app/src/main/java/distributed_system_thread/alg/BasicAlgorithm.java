package distributed_system_thread.alg;

import java.io.PrintWriter;

public interface BasicAlgorithm extends Runnable{
    String getMessage(PrintWriter output);
}
