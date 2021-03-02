package control.handle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import control.memory.Shared;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class OpenServer implements Runnable {

    private Socket socket;

    BufferedReader inNet;

    private PrintWriter outNet;

    Shared shared;

    public OpenServer(Socket socket, Shared shared) {
        try {
            this.socket = socket;
            inNet = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outNet = new PrintWriter(socket.getOutputStream(), true);
            this.shared = shared;
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
                    //sendImg();
                } else if(readed.contains(".css")) {
                    sendCss(readed);
                } else {
                    sendHtml(readed);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendHtml(String readed) throws IOException {
        String send;
        String path = shared.getPercorso(readed);
        if (!path.equals("404")) {
            Path pathFile = Paths.get(path);
            BufferedReader file = new BufferedReader(new FileReader(pathFile.toFile()));
            send("HTTP/1.1 200 OK");
            send("Content-Type: text/html");
            send("Content-Length: " + pathFile.toFile().length());
            send("\r\n");
            while ((send = file.readLine()) != null) {
                send(send);
            }
            file.close();
        } else {
            Path pathFile = Paths.get("src/res/404.html");
            BufferedReader file = new BufferedReader(new FileReader(pathFile.toFile()));
            send("HTTP/1.1 404 OK");
            send("Content-Type: text/html");
            send("Content-Length: " + pathFile.toFile().length());
            send("\r\n");
            while ((send = file.readLine()) != null) {
                send(send);
            }
            file.close();
        }
    }

    public void sendCss(String readed) throws IOException{
        String toAppend;
        Path pathToCss = Paths.get(shared.getPercorso(readed));
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

/*    public void sendImg() {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void log(String message) {
        System.out.println(message);
    }

    private void send(String message) {
        outNet.println(message);
        outNet.flush();
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
