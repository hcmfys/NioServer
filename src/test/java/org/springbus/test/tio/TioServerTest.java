package org.springbus.test.tio;

import org.tio.server.ServerGroupContext;
import org.tio.server.TioServer;

import java.io.IOException;

public class TioServerTest {



    public static  void  main(String[] args) {

        ServerGroupContext  serverGroupContext=new ServerGroupContext("my-io",
               new MyServerAioHandler(),new MyServerAioListener()
        );
        TioServer tioServer=new TioServer(serverGroupContext);
        try {
            tioServer.start("127.0.0.1",8088);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
