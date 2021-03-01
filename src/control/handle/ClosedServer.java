package control.handle;

import java.io.*;
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
            BufferedReader file = new BufferedReader(new FileReader("/res/403.html"));
            StringBuilder sb = new StringBuilder();
            String toAppend;
            while((toAppend = file.readLine()) != null) {
                sb.append(toAppend);
            }
            outNet.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
