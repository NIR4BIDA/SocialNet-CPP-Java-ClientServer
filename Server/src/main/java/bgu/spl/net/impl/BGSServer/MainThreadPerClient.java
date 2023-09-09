package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.DataBase;
import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.Server;

public class MainThreadPerClient {
    public static void main(String[] args) {
        DataBase dataBase = DataBase.getInstance();
        try {
            BaseServer<Object> perClientServer = (BaseServer<Object>) Server.threadPerClient(
                    Integer.parseInt(args[0]), //port
                    () -> new BidiMessagingProtocolImpl<>(dataBase), //protocol factory
                    MessageEncoderDecoderImpl::new); //message encoder decoder factory
            perClientServer.serve();
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}