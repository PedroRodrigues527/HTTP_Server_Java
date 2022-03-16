import java.util.concurrent.locks.ReentrantLock;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLogThread extends Thread{
    ReentrantLock _lock;
    String[] _data;

    public ServerLogThread(ReentrantLock lock, String[] data){
        _lock = lock;
        _data = data;
    }

    @Override
    public void run(){
        _lock.lock();
        try {
            File myObj = new File("server.log");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            while(true)
            {
                if(_data[0] != null) {
                    FileWriter fw = new FileWriter(myObj.getName());

                    LocalDateTime myDateObj = LocalDateTime.now();
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.ms");
                    String formattedDate = myDateObj.format(myFormatObj);


                    fw.write(formattedDate + "-Method:" + _data[0] + "-Route:" + _data[1] + "-");

                    fw.close();
                    System.out.println("Successfully wrote to the server.log");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        _lock.unlock();
    }

}
