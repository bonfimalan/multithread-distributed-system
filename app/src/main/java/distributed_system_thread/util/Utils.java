package distributed_system_thread.util;

public class Utils {
    public static int randomBetween(int floor, int roof) {
        return (int) (Math.random() * roof) + floor;
    }
}
