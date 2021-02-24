package udit.android.optumsoftassignment.Socket;

import java.net.URI;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketClass {

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://interview.optumsoft.com:80");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
