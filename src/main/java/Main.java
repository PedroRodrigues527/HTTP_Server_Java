import java.util.concurrent.locks.ReentrantLock;

public class Main {

    /**
     * Main class of the program, only minimal changes should be added to this method
     * @param args
     */
    public static void main(String[] args){
        ReentrantLock lock = new ReentrantLock();
        MainHTTPServerThread s = new MainHTTPServerThread(8888);
        s.start();
        ServerLogThread sl = new ServerLogThread(lock, s.getParametersRequest());
        sl.start();
        try {
            s.join();
            sl.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
