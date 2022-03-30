import java.io.File;
import java.io.IOException;

/**
 * Thread responsible to create and/or open the server.log file
 * Class extended {@link Thread} to enable thread configurations
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
