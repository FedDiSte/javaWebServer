package control.memory;

import model.Route;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Shared {

    private boolean accepting;
    private boolean generalShutdown;
    private static final int listeningPort = 6969;
    String percorsoRoutes = "src/res/routes.txt";

    private ArrayList<Route> routes;

    public Shared() {
        routes = new ArrayList<>();
        accepting = false;
        generalShutdown = false;
        loadRoutes();
    }

    public String getRoutes() {
        StringBuilder sb = new StringBuilder();
        for (Route r : routes) {
            sb.append(r.getRequest());
        }
        return sb.toString();
    }

    public void addRoute(String percorso, String richiesta) {
        if (Files.exists(Paths.get(percorso))) {
            routes.add(new Route(percorso, richiesta));
            updateRoutes();
        } else {
            System.err.println("Il file non è stato trovato");
        }
    }

    public String getPercorso(String richiesta) {
        for (Route r : routes) {
            if (r.getRequest().equals(richiesta)) {
                return r.getPercorso();
            }
        }
        return "404";
    }

    public boolean isAccepting() {
        return accepting;
    }

    public void setAccepting(boolean b) {
        accepting = b;
    }

    public int getListeningPort() {
        return listeningPort;
    }

    public boolean isGeneralShutdown() {
        return generalShutdown;
    }

    public void setGeneralShutdown(boolean b) {
        generalShutdown = b;
    }

    public synchronized void loadRoutes() {
        try {
            String percorsoRoutes = "src/res/routes.txt";
            BufferedReader inFile = new BufferedReader(new FileReader(Paths.get(percorsoRoutes).toFile()));
            String line = null;
            while ((line = inFile.readLine()) != null) {
                Scanner s = new Scanner(line);
                String percorso = s.next();
                String richiesta = s.next();
                routes.add(new Route(percorso, richiesta));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateRoutes() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(percorsoRoutes));
            for (Route r : routes) {
                out.println(r.getPercorso() + " " + r.getRequest());
                out.flush();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

