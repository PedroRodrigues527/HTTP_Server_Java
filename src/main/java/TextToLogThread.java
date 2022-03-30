import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 *  Thread responsible for creation of string
 *  that goes to log file (server.log)
 *  Class extended {@link Thread} to enable thread configurations
 */
public class TextToLogThread extends Thread{
    String[] _data;
    private String text;
    String _userIp;


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

    @Override
    public void run(){
        try{
            if(_data[0] != null) {
                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.ms");
                String formattedDate = myDateObj.format(myFormatObj);
                String log = formattedDate + "-Method:" + _data[0] + "-Route:" + _data[1] + "-/" + _userIp;
                setText(log);
                System.out.println(log);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
