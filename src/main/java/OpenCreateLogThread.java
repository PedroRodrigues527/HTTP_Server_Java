import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Thread responsable to create the log file
 * (if it does not exist) or open.
 * Class extended {@link Thread} to enable creation of threads
 */
public class OpenCreateLogThread extends Thread{

    @Override
    public void run(){
        File myObj = new File("server.log");
        try {
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
