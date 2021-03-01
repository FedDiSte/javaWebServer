package test;

import control.HandleOnOff;
import control.accept.HandleAccept;

public class testJavaWebServer {

    public static void main(String[] args) {
        new Thread(new HandleOnOff()).start();
    }

}
