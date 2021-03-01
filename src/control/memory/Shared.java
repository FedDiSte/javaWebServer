package control.memory;

public class Shared {

    private boolean accepting;
    private boolean generalShutdown;
    private static final int listeningPort = 6969;

    public void Shared() {
        accepting = false;
        generalShutdown = false;
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

}
