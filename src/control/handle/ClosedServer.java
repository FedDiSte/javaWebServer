package control.handle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class ClosedServer implements Runnable {

    private Socket socket;

    private PrintWriter outNet;
    private String percorso = "src/res/403.html";
    private Path pathFile = Paths.get(percorso);

    private String css = "src/res/grafica.css";
    private Path pathToCss = Paths.get(css);

    String pathImg = "src/res/images/img.png";
    Path percorsoImg = Paths.get(pathImg);

    public ClosedServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader inNet = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outNet = new PrintWriter(socket.getOutputStream(), true);
            String request = "";
            while (request != null || request.isEmpty() || request.isBlank()) {
                Scanner s = new Scanner(request = inNet.readLine());
                s.useDelimiter(" ");
                log(request);
                s.next(); //GET
                String check = s.next();
                if (check.contains(".css")) {
                    sendCss();
                } else if (check.contains(".png") || check.contains(".jpg")) {
                    sendImg();
                } else {
                    sendHtml();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendHtml() throws IOException {
        String send;
        BufferedReader file = new BufferedReader(new FileReader(pathFile.toFile()));
        send("HTTP/1.1 200 OK");
        send("Content-Type: text/html");
        send("Content-Length: " + pathFile.toFile().length());
        send("\r\n");
        while ((send = file.readLine()) != null) {
            send(send);
        }
        file.close();
    }

    public void sendCss() throws IOException{

        String toAppend;
        BufferedReader fileCss = new BufferedReader(new FileReader(pathToCss.toFile()));
        send("HTTP/1.1 200 OK");
        send("Content-Type: text/css");
        send("Content-Length: " + pathToCss.toFile().length());
        send("\r\n");
        while ((toAppend = fileCss.readLine()) != null) {
            send(toAppend);
        }
        fileCss.close();

    }

    public void sendImg() throws IOException {

        BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(percorsoImg.toFile()));

        send("HTTP/1.1 200 OK");
        send("Content-Type: image/png");
        send("Content-Length: " + percorsoImg.toFile().length());
        send("\r\n");

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        out.flush();
        out.close();
    }

    private void log(String message) {
        System.out.println(message);
    }

    private void send(String message) {
        outNet.println(message);
        outNet.flush();
    }
}
