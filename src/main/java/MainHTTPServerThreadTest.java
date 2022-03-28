import org.junit.jupiter.api.*;

import java.io.File;

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
        @DisplayName ("TextToLogThread Test")
        class TextToLogThreadTest {

        }

        @Nested
        @DisplayName ("SaveContentLogThread Test")
        class SaveContentLogThreadTest {

        }
    }
}