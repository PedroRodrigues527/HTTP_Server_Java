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
    ReentrantLock _lock;


    public ServerLogThread(String[] data, String ipclient){
        _data = data;
        _ipclient = ipclient;
    }

    @Override
    public void run(){
        try {
            File myObj = new File("server.log");
            OpenCreateLogThread openCreateThread = new OpenCreateLogThread();
            TextToLogThread textThread = new TextToLogThread(_data, _ipclient);


            openCreateThread.start();
            textThread.start();

            openCreateThread.join();
            /*while(!textThread.isReadyBool()){
                System.out.println("WAITING!!");
            }*/
            //if(textThread.isReadyBool()){logContent = textThread.getText();}
            textThread.join();
            if(textThread.isReadyBool()){logContent = textThread.getText();}
            //logContent = textThread.getText();
            SaveContentLogThread saveThread = new SaveContentLogThread(myObj, logContent, _data);
            saveThread.start();
            saveThread.join();




        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
