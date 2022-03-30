import org.junit.jupiter.api.*;

import java.io.File;
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

        }
    }
}