import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ServerLogThread extends Thread{
    String[] _data;
    String _ipclient;
    String logContent;
    Semaphore _sem;


    public ServerLogThread(String[] data, String ipclient, Semaphore sem){
        _data = data;
        _ipclient = ipclient;
        _sem = sem;
    }

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
