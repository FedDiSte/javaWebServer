package control.handle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import control.memory.Shared;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class ClosedServer implements Runnable {

    private Socket socket;

    BufferedReader inNet;

    private BufferedOutputStream outNet;
    private String percorso = "src/res/html/403.html";
    private Path pathFile = Paths.get(percorso);

    private String css = "src/res/css/grafica.css";
    private Path pathToCss = Paths.get(css);

    String pathImg = "src/res/images/img.png";
    Path percorsoImg = Paths.get(pathImg);

    Shared shared;

    public ClosedServer(Socket socket, Shared shared) {
        try {
            this.shared = shared;
            this.socket = socket;
            inNet = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outNet = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String request;
            while ((request = getRequest()) != null) {
                Scanner s = new Scanner(request);
                s.next(); //Skip "GET"
                String readed = s.next();
                if(readed.contains(".png") || readed.contains(".jpg")) {
                    sendImg();
                } else if(readed.contains(".css")) {
                    sendCss();
                } else {
                    log("here");
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

    public void sendImg() {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(percorsoImg.toFile()));

            send("HTTP/1.1 200 OK");
            send("Content-Type: image/png");
            send("Content-Length: " + Files.size(percorsoImg));
            send("\r\n");

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                outNet.write(buffer, 0, bytesRead);
            }
            outNet.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String message) {
        System.out.println(message);
    }

    private void send(String message) {
        try {
            outNet.write(message.getBytes());
            outNet.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRequest() throws IOException{
        StringBuilder sb = new StringBuilder();
        String message;
        for(int i = 0; i < 3; i++) {
            if((message = inNet.readLine()) != null) {
                sb.append(message);
            }
        }
        if(sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }

}
