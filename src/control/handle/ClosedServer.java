package control.handle;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClosedServer implements Runnable{

    private Socket socket;

    private PrintWriter outNet;

    public ClosedServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            outNet = new PrintWriter(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
