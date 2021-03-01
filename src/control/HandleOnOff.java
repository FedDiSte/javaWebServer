package control;

import control.memory.Shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HandleOnOff implements Runnable{

    boolean currentState;
    boolean generalShutdown;

    private BufferedReader inTastiera;

    private Shared shared;

    public HandleOnOff() {
        currentState = false;
        generalShutdown = false;
        inTastiera = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        try {
            do{
                log("Stato server: ");
                if(currentState) {
                    log("online\n");
                } else {
                    log("offline\n");
                }
                normalLog("Per mandare online/offline il server: online/offline\n" +
                        "Per spegnere il server: stop");
                switch(inTastiera.readLine()) {
                    case "online": {
                        if(currentState) {
                            normalLog("Il server è gia online");
                        } else {
                            normalLog("Passaggio a stato online, ora il server accetterà connessioni");
                            currentState = true;
                            shared.setAccepting(currentState);
                        }
                        break;
                    }
                    case "offline": {
                        if(!currentState) {
                            normalLog("Il server è gia offline");
                        } else {
                            normalLog("Passaggio a stato offline, ora il server non accetterà connessioni");
                            currentState = false;
                            shared.setAccepting(currentState);
                        }
                        break;
                    }
                    case "stop": {
                        normalLog("Chiusura server, il programma terminerà a breve");
                        generalShutdown = true;
                    }
                }
            } while(!generalShutdown);
        } catch (IOException e) {
            normalLog("Errore generato in Handle On/Off, codice errore: " + e.getMessage() + "\n");
        }
    }

    private void log(String message) {
        System.err.print(message);
    }

    private void normalLog(String message) {
        System.out.println(message);
    }

}
