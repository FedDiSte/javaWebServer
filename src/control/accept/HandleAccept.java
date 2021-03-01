package control.accept;

import control.handle.ClosedServer;
import control.memory.Shared;

import java.io.IOException;
import java.net.ServerSocket;

public class HandleAccept implements Runnable{

    ServerSocket serverSocket;

    Shared shared;

    public HandleAccept(Shared shared) {
        try {
            this.shared = shared;
            serverSocket = new ServerSocket(shared.getListeningPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //qui verranno accettati i diversi client che richiedono una connessione
    @Override
    public void run() {
        try {
            do{
                if(shared.isAccepting()) {
                    
                } else {
                    new Thread(new ClosedServer(serverSocket.accept())).start();
                }
            } while(!shared.isGeneralShutdown());
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
