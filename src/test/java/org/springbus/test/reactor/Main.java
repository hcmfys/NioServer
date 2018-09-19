package org.springbus.test.reactor;

import org.springbus.test.TCPReactor;

import java.io.IOException;

public class Main {


    public static void main(String[] args) {

        try {
            TCPReactor reactor = new TCPReactor(8013);
            new Thread(reactor).start();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


}
