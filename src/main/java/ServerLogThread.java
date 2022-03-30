import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Thread responsable for creating/opening
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
     * <p></p>
     */
    @Override
    public void run(){
        try {
            if(_sem.tryAcquire(200, TimeUnit.MILLISECONDS)) {
                File myObj = new File("server.log");
                OpenCreateLogThread openCreateThread = new OpenCreateLogThread();
                TextToLogThread textThread = new TextToLogThread(_data, _ipclient);

                openCreateThread.start();
                textThread.start();

                openCreateThread.join();
                textThread.join();
                if (textThread.isReadyBool()) {
                    logContent = textThread.getText();
                }

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
