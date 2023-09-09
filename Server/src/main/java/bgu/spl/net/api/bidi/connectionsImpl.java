package bgu.spl.net.api.bidi;


import bgu.spl.net.srv.ConnectionHandler;

import java.util.concurrent.ConcurrentHashMap;

public class connectionsImpl<T> implements Connections<T> {
    private ConcurrentHashMap<Integer, ConnectionHandler<T>> connectClientToConnectionHandlerHashMap;
    private connectionsImpl(){
        connectClientToConnectionHandlerHashMap=new ConcurrentHashMap<Integer, ConnectionHandler<T>>();
    }
    private static class singletonHolder{
        private static connectionsImpl instance=new connectionsImpl();
    }
    public static connectionsImpl getInstance(){
        return singletonHolder.instance;
    }
    public boolean send(int connectionId, T msg){
        connectClientToConnectionHandlerHashMap.get(connectionId).send(msg);
        return true;
    }
    public void broadcast(T msg){
        for(ConnectionHandler<T> connectionHandler:connectClientToConnectionHandlerHashMap.values()){
            connectionHandler.send(msg);
        }
    }
    public void disconnect(int connectionId){
        connectClientToConnectionHandlerHashMap.remove(connectionId);
    }
    public void setHashMap(int connID,ConnectionHandler connectionHandler){
        connectClientToConnectionHandlerHashMap.put(connID,connectionHandler) ;
    }

}
