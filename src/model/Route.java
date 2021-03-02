package model;

public class Route {
    
    private String percorso;
    private String request;

    public Route(String percorso, String request) {
        this.percorso = percorso;
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getPercorso() {
        return percorso;
    }

    public void setPercorso(String percorso) {
        this.percorso = percorso;
    }
}
