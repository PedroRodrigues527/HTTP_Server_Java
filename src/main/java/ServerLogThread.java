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

    public ServerLogThread(String[] data, String ipclient){
        _data = data;
        _ipclient = ipclient;
    }

    @Override
    public void run(){
        try {
            File myObj = new File("server.log");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            if(_data[0] != null) {
                BufferedWriter fw = new BufferedWriter(new FileWriter(myObj.getName(), true));

                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.ms");
                String formattedDate = myDateObj.format(myFormatObj);

                fw.write(formattedDate + "-Method:" + _data[0] + "-Route:" + _data[1] + "-/" + _ipclient);
                fw.write("\r\n");

                fw.close();
                System.out.println("Successfully wrote to the server.log");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
