package bgu.spl.net.impl.BGSServer;
import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.DataBase;
import bgu.spl.net.srv.Server;
public class reactorMain {
    public static void main(String[] args){
        DataBase dataBase = DataBase.getInstance();
        Server<String> server = Server.reactor(Integer.parseInt(args[1]), //Number of threads
                Integer.parseInt(args[0]),//port
                ()->new BidiMessagingProtocolImpl<>(dataBase), //factory
                ()->new MessageEncoderDecoderImpl<>()//factory
        );
        server.serve();
    }
}
