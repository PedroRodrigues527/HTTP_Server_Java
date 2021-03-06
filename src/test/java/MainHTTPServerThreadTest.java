import org.junit.jupiter.api.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.*;
import java.net.InetAddress;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.jupiter.api.Assertions.*;

class MainHTTPServerThreadTest{

    @Nested
    @DisplayName ("Server Requests tests")
    class MainTest{
        ReentrantLock _lock;

        @BeforeEach
        void setUp()
        {
            _lock = new ReentrantLock();
            MainHTTPServerThread serverThreadJava = new MainHTTPServerThread(8888, _lock);
            serverThreadJava.start();
        }

        @DisplayName ("Create multiples threads")
        @Test
        public void testRequests() throws IOException, InterruptedException {
            int requestNumber = 0;
            boolean statusBool = false;
            while(requestNumber < 99){
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8888/user/profile/page.html"))
                        .build();
                HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if(response.statusCode() != 200) { statusBool = true; break; }
                requestNumber++;
            }
            assertFalse(statusBool);
            assertEquals(99, requestNumber);
        }

    }
    @Nested
    @DisplayName ("URL testing")
    class UrlTest{
        ReentrantLock _lock;

        @BeforeEach
        void setUp(){
            _lock = new ReentrantLock();
            MainHTTPServerThread serverThreadJava = new MainHTTPServerThread(8888, _lock);
            serverThreadJava.start();
        }

        @Test
        @DisplayName("Send requested page")
        void sendRequestedPage()throws IOException, InterruptedException{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8888/user/profile/page.html"))
                    .build();

            //Server finds that has page.html in that path
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //Is the page shown the expected page.html?
            assertTrue(response.body().toString().contains("<h1>TESTE PAGE</h1>"));
        }

        @Test
        @DisplayName("Sending Index")
        void notFoundPageRedirectToIndex() throws IOException, InterruptedException {
            //Server doesn't have html file!
            //Invalid client request?
            //Exist directory? YES -> send Index

            HttpClient client = HttpClient.newHttpClient();

            //Test with valid directory

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8888/user/about.html"))
                    .build();

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //Server response == index.html?
            assertTrue(response.body().toString().contains("<title>index</title>"));
        }

        @Test
        @DisplayName("Redirect to error page")
        void notFoundIndexRedirectTo404() throws IOException, InterruptedException{

            //Server dont have index.html or folder
            //Dont exist ^? YES -> Send 404.html (assertTrue)

            HttpClient client = HttpClient.newHttpClient();

            //Client request: get a/v/a.html (DO NOT EXIST!!!)
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8888/a/v/a.html"))
                    .build();

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertTrue(response.body().toString().contains("<h1>404 Not Found</h1>"));
        }
    }


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
        @DisplayName ("TextToLogThread Test") //Testar se o texto ?? o expect??vel
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
                    conteudo = ttlt.getText();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            @Test
            @DisplayName("Test creation of Data String")
            void testTextToLogThread(){
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
                    assertTrue(lines.contains("ContentV2"));
                    assertFalse(lines.contains("backup"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
