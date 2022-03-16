import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class MainHTTPServerThread extends Thread {

    //Variaveis de instancia
    private final String pathPedro = "/home/pedro/IdeaProjects/PROJETO_PA_1/server";
    private final String pathCupido = "\\Users\\jcupi\\Desktop\\IntelliJ_IDEA_Projects\\PROJETO_PA_1\\server";
    private final String pathDiogo = "\\Users\\Diogo\\IdeaProjects\\PROJETO_PA_1\\server";

    private DataInputStream in;
    private ServerSocket server;
    private Socket client;
    private int port;
    private String[] parametersRequest;
    public MainHTTPServerThread(int port) {
        this.port = port;
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
            e.printStackTrace();
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
                while (!(line = br.readLine()).isBlank()) {
                    requestBuilder.append(line + "\r\n");
                }

                /*
                Quite simple parsing, to be expanded by each group
                */
                String request = requestBuilder.toString();
                String[] tokens = request.split(" ");
                String route = tokens[1];
                System.out.println(request);
                parametersRequest = tokens;

                //fecha trinco?
                if (route.equals("/")){
                    route = "/user/profile/index.html";

                }
                byte[] content =  readBinaryFile(server_root+route);
                //abre trinco?

                OutputStream clientOutput = client.getOutputStream();
                clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                clientOutput.write(("ContentType: text/html\r\n").getBytes());
                clientOutput.write("\r\n".getBytes());
                clientOutput.write(content);
                clientOutput.write("\r\n\r\n".getBytes());
                clientOutput.flush();
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter of variable "parametersRequest"
     * @return
     */
    public String[] getParametersRequest(){
        return parametersRequest;
    }
}
