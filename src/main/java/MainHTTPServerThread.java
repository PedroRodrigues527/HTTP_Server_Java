import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.net.InetAddress;

//https://www.geeksforgeeks.org/java-program-to-search-for-a-file-in-a-directory/
class findFile implements FilenameFilter{
    String str;
    public findFile(String str)
    {
        this.str = str;
    }

    public boolean accept(File dir, String name)
    {
        return name.startsWith(str);
    }

}

public class MainHTTPServerThread extends Thread{

    //Variaveis de instancia
    private final String pathPedro = "/home/pedro/IdeaProjects/PROJETO_PA_1/server";
    private final String pathCupido = "\\Users\\jcupi\\Desktop\\IntelliJ_IDEA_Projects\\PROJETO_PA_1_1\\server";
    private final String pathDiogo = "\\Users\\Diogo\\IdeaProjects\\PROJETO_PA_1\\server";
    
    ReentrantLock _lock;
    private DataInputStream in;
    private ServerSocket server;
    private Socket client;
    private int port;
    public MainHTTPServerThread(int port, ReentrantLock lock) {
        this.port = port;
        _lock = lock;
    }


    /**
     * Reads a server document and returns it as an array of bytes
     *
     * @param path  path of the file
     * @return  <code>byte[]</code> with the html document at <code>path</code>
     */
    private byte[] readBinaryFile(String path){
        byte[] content = new byte[0];
        File f= new File(path);
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            content = fileInputStream.readAllBytes();
            return content;
        }catch(Exception e){
            //e.printStackTrace();
            return content;
        }
    }

    /**
     * Reads an HTML documents and returns it as string
     *
     * @param path  path of the file
     * @return  String with the html document at <code>path</code>
     */
    private String readFile (String path) {
        System.out.println( ">>> Reading the file" );
        File original = new File( path);
        Scanner reader = null;
        String content = "";
        try {
            reader = new Scanner( original );
            while ( reader.hasNextLine( ) ) {
                String input = reader.nextLine( );
                if ( content.isEmpty( ) ) {
                    content = input;
                } else {
                    content = content + "\n" + input;
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace( );
            return "";
        }
        System.out.println( ">>> Done reading the file" );
        return content;
    }

    /**
     * <b>Completar pelos alunos..</b>
     * <p>
     * Main cycle of the server, it creates the {@link ServerSocket} at the specified port, and then it creates a new {@link Socket}
     * for each new request
     * <p>
     * To refactor with:
     * <ul>
     *     <li>loading the server configurations from the server.config file</li>
     *     <li>Introduce parallelism to handle the requests</li>
     *     <li>Introduce parallelism to handle the documents</li>
     *     <li>Parse the request according as necessary for the implementation</li>
     *     <li>...</li>
     * </ul>
     */
    @Override
    public void run(){
        try {
            String server_root = System.getProperty("user.dir") + "/server_root"; //to be defined by the user
            server = new ServerSocket(port);
            System.out.println("Started Server Socket");
            System.out.println("Working Directory = " + System.getProperty("user.dir"));

            while(true){
                client = server.accept();

                System.out.println();    //Criar espaço no output
                System.out.println("Debug: got new client " + client.toString());
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

                StringBuilder requestBuilder = new StringBuilder();
                String line;
                try {
                    while ((line = br.readLine()) != null && !line.isBlank()) {
                        requestBuilder.append(line).append("\r\n");
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                /*
                Quite simple parsing, to be expanded by each group
                */
                _lock.lock();
                String request = requestBuilder.toString();
                String[] tokens = request.split(" ");
                String route = tokens[1];
                System.out.println(request);

                //ReentrantLock lock = new ReentrantLock();
                InetAddress thisIp = InetAddress.getLocalHost();
                Semaphore sem = new Semaphore(1);
                ServerLogThread sl = new ServerLogThread(tokens, thisIp.getHostAddress(), sem);
                sl.start();
                try {
                    sl.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                byte[] content;

                File directory = new File(server_root+route);
                if(!directory.exists())
                {
                    String path = server_root + route.substring(0, route.lastIndexOf('/') + 1) + "index.html";
                    File index = new File(path);
                    if(!index.exists())
                    {
                        content = readBinaryFile(System.getProperty("user.dir")+"/server/404.html");
                    }
                    else
                    {
                        content = readBinaryFile(path);
                    }
                }
                else
                {
                    content = readBinaryFile(server_root + route);
                }

                OutputStream clientOutput = client.getOutputStream();
                clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                clientOutput.write(("ContentType: text/html\r\n").getBytes());
                clientOutput.write("\r\n".getBytes());
                clientOutput.write(content);
                clientOutput.write("\r\n\r\n".getBytes());
                clientOutput.flush();
                client.close();
                _lock.unlock();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
