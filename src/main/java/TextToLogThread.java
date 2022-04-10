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
     * Constructs a new Business (flight) by specifying its Data and user Ip
     *
     * @param data request data from client
     * @param ip ip of the user
     */
    public TextToLogThread(String[] data, String ip){
        _data = data;
        _userIp = ip;
    }

    /**
     * Set text to local variable
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get content from text local variable
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Create mount and save a line of text to local variable
     *
     */
    @Override
    public void run(){
        try{
            if(_data[0] != null) {
                //Criação dos objetos
                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.ms");
                String formattedDate = myDateObj.format(myFormatObj);
                String log = formattedDate + "-Method:" + _data[0] + "-Route:" + _data[1] + "-/" + _userIp;//Montagem de texto para o server.log
                setText(log);
                System.out.println(log);//Escrita no server.log
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
