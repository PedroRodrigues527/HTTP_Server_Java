import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveContentLogThread extends Thread{
    File _obj;
    String _content;
    String[] _data;

    public SaveContentLogThread(File obj, String content, String[] data){
        _obj = obj;
        _content = content;
        _data = data;
    }

    @Override
    public void run(){
        try{
            if(_data[0] != null || _content == null){
                BufferedWriter fw = new BufferedWriter(new FileWriter(_obj.getName(), true));
                System.out.println("CONTEUDO: " + _content);
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
