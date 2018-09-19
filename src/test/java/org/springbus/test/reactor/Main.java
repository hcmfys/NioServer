package org.springbus.test.reactor;

import java.io.IOException;

public class Main {


    public static void main(String[] args) {

        try {
            TCPReactor reactor = new TCPReactor(8088);
            new Thread(reactor).start();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


}
