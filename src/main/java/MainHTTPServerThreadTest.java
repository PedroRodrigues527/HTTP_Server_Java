import org.junit.jupiter.api.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.*;
import java.net.InetAddress;
import static org.junit.jupiter.api.Assertions.*;

class MainHTTPServerThreadTest {

    @Nested
    @DisplayName ("ServerLogThread Test")
    class ServerLogThreadTest {
        @Nested
        @DisplayName ("OpenCreateLogThread Test")
        class OpenCreateLogThreadTest {
            private OpenCreateLogThread oclt;
            private File myObj;

            @BeforeEach
            void setUp()
            {
                myObj = new File("server.log");
                this.oclt = new OpenCreateLogThread();
                oclt.start();
                try{
                    oclt.join();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Test
            @DisplayName("Test creation of server.log file")
            void testOpenCreateLogThread()
            {
                assertAll(
                        () -> assertTrue(myObj.exists()),
                        () -> assertEquals("server.log", myObj.getName())
                );
            }
        }

        @Nested
        @DisplayName ("TextToLogThread Test") //Testar se o texto é o expectável
        class TextToLogThreadTest {
            private TextToLogThread ttlt;
            private String[] _data;
            private InetAddress ipclient;
            private String conteudo;
            @BeforeEach
            void setUp()
            {
                _data = new String[]{"GET", "/user/profile/page.html"};
                try{
                    ipclient = InetAddress.getLocalHost();
                    this.ttlt= new TextToLogThread(_data,ipclient.getHostAddress());
                    ttlt.start();
                    ttlt.join();
                    if(ttlt.isReadyBool()){
                        conteudo = ttlt.getText();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            @Test
            @DisplayName("Test creation of Data String")
            void testTextToLogThread(){
                assertTrue(ttlt.isReadyBool());
                assertTrue((conteudo).contains("-Method:"+_data[0]));
                assertTrue((conteudo).contains("-Route:"+ _data[1]));
                assertTrue((conteudo).contains("-/"+ipclient.getHostAddress()));

            }

        }

        @Nested
        @DisplayName ("SaveContentLogThread Test")
        class SaveContentLogThreadTest {
            private SaveContentLogThread sclt;
            private File myObj;
            private String _content;
            @BeforeEach
            void setUp(){
                myObj = new File("server.log");
                _content= "ContentV2";
                sclt = new SaveContentLogThread(myObj,_content,new String[]{"GET", "/user/profile/page.html"});
                sclt.start();
                try{
                    sclt.join();

                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
            @Test
            @DisplayName("Open and close")
            void testSaveContentLogThread(){
                assertTrue(myObj.exists());
                try {
                    Path filePath = Paths.get("server.log");
                    Charset charset = StandardCharsets.UTF_8;
                    List<String> lines = Files.readAllLines(filePath, charset);
                    assertTrue(lines.contains(_content));
                    assertFalse(lines.contains("backup"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        }
    }
}