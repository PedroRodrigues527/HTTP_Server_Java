import java.util.concurrent.locks.ReentrantLock;

public class ServerLogThread extends Thread{
    ReentrantLock _lock;

    public ServerLogThread(ReentrantLock lock, String[] data){
        _lock = lock;
    }

    @Override
    public void run(){
        _lock.lock();
        //gravar ficheiro no ficheiro server.log
        _lock.unlock();
    }

}
