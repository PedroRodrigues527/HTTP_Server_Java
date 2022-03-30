import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 *  Thread responsable for creation of string
 *  that goes to log file (server.log)
 */
public class TextToLogThread extends Thread{
    String[] _data;
    private String text;
    String _userIp;
    private boolean readyBool = false;

    public void setReadyBool(boolean readyBool) {
        this.readyBool = readyBool;
    }

    public boolean isReadyBool() {
        return readyBool;
    }


    /**
     * @param data request data from client
     * @param ip ip of the user
     */
    public TextToLogThread(String[] data, String ip){
        _data = data;
        _userIp = ip;
    }


    /**
     * Set text to variable
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get content from text variable
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Verifies if thread has finished
     * Setting content to private variable text
     * @return boolean
     */
    public boolean ready(){
        if(readyBool){
            return isReadyBool();
        }
        else{
            return false;
        }
    }

    @Override
    public void run(){
        //System.out.println("Text to log");
        try{
            if(_data[0] != null) {
                //fecha trinco?
                readyBool = false;
                //BufferedWriter fw = new BufferedWriter(new FileWriter(myObj.getName(), true));
                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.ms");
                String formattedDate = myDateObj.format(myFormatObj);
                String log = formattedDate + "-Method:" + _data[0] + "-Route:" + _data[1] + "-/" + _userIp;
                setText(log);
                readyBool = true;
                ready();
                //ERRO
                System.out.println(log);
                //abre trinco????
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
