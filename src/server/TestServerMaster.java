import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import server.ServerMaster;

public class TestServerMaster
{
	@Test
	public void testServer()
	{
       byte[] emptyPayload = new byte[1001];

        // Using Mockito
        final Socket socket = mock(Socket.class);
        final ServerSocket serverSocket = mock(ServerSocket.class);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Mockito.when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);
        Mockito.when(serverSocket.accept()).thenReturn(socket);

        ServerMaster text = new ServerMaster(1024) {
            @Override
            protected Socket createSocket() {
                return serverSocket;
            }
        };

        //Assert.assertTrue("Message sent successfully", text.sendTo("localhost", "1234"));
        //Assert.assertEquals("whatever you wanted to send".getBytes(), byteArrayOutputStream.getBytes());
		
	}
}