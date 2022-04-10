import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Thread that creates the string to
 * save on 'server.log' file
 * Class extended {@link Thread} to enable thread configurations
 */
public class SaveContentLogThread extends Thread{
    File _obj;
    String _content;
    String[] _data;

    /**
     * Constructs a new SaveContentLogThread by initializing the file, content of text and data request from server.
     * @param obj file to save content
     * @param content content(String) to save to obj
     * @param data Request information received from server
     */
    public SaveContentLogThread(File obj, String content, String[] data){
        _obj = obj;
        _content = content;
        _data = data;
    }

    /**
     * Writes the content of text on the file, adds a new empty paragraph and save the file.
     */
    @Override
    public void run(){
        try{
            if(_data[0] != null || _content == null){
                BufferedWriter fw = new BufferedWriter(new FileWriter(_obj.getName(), true));
                fw.write(_content);
                fw.write("\r\n");
                System.out.println("Successfully wrote to the server.log");
                fw.close();
            }
        }
        catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}