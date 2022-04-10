import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.io.File;

/**
 * Thread responsible for creating/opening
 * the log file, and saving log content
 * to 'server.log'
 * Class extended {@link Thread} to enable creation of threads
 */
public class ServerLogThread extends Thread{
    String[] _data;
    String _ipclient;
    String logContent;
    Semaphore _sem;

    /**
     * Creates thread with data, ip of the
     * client and semaphores
     * @param data data requested
     * @param ipclient ip of the client
     * @param sem semaphore object
     */
    public ServerLogThread(String[] data, String ipclient, Semaphore sem){
        _data = data;
        _ipclient = ipclient;
        _sem = sem;
    }

    /**
     * Combines Several Thread Parts and use of fork-join and Semaphore for Synchronization
     *
     */
    @Override
    public void run(){
        try {
            if(_sem.tryAcquire(200, TimeUnit.MILLISECONDS)) {
                //Criação dos Objetos
                File myObj = new File("server.log");
                OpenCreateLogThread openCreateThread = new OpenCreateLogThread();
                TextToLogThread textThread = new TextToLogThread(_data, _ipclient);

                openCreateThread.start();
                textThread.start();

                openCreateThread.join();
                textThread.join();
                logContent = textThread.getText();

                SaveContentLogThread saveThread = new SaveContentLogThread(myObj, logContent, _data);
                saveThread.start();
                saveThread.join();
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
