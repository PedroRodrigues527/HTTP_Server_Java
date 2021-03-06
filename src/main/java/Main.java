import java.util.concurrent.locks.ReentrantLock;

public class Main {

    /**
     * Main class of the program, only minimal changes should be added to this method
     * @param args
     */
    public static void main(String[] args){
        ReentrantLock lockHTML = new ReentrantLock();
        MainHTTPServerThread s = new MainHTTPServerThread(8888, lockHTML);
        s.start();
        try {
            s.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
