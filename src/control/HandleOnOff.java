package control;

import control.accept.HandleAccept;
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
        shared = new Shared();
    }

    @Override
    public void run() {
        try {
            //Start del thread che permette la gestione delle richieste
            new Thread(new HandleAccept(shared)).start();

            normalLog("Per mandare online/offline il server: online/offline \n" +
                      "Per spegnere il server: stop \n" +
                      "Per stampare informazioni su i comandi: help");
            do{
                log("Stato server: ");
                if(currentState) {
                    log("online\n");
                } else {
                    log("offline\n");
                }
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
                    case "help": {
                        normalLog("Per mandare online/offline il server: online/offline \n" +
                                "Per spegnere il server: stop \n" +
                                "Per stampare informazioni su i comandi: help");
                    }
                    default: {
                        log("Comando non trovato\n");
                        normalLog("Per mandare online/offline il server: online/offline \n" +
                                "Per spegnere il server: stop \n" +
                                "Per stampare informazioni su i comandi: help");
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
