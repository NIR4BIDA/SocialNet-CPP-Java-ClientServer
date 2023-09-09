package bgu.spl.net.api.bidi;

import java.util.concurrent.ConcurrentHashMap;

public class logStatMessage {
    DataBase dataBase;
    int connectionId;
    Connections connections;
    public logStatMessage (DataBase _dataBase,int _connectionId,Connections _connections) {
        dataBase = _dataBase;
        connectionId=_connectionId;
        connections=_connections;
    }
    public void LogStatMessage(){
       ConcurrentHashMap<String,StudentLogInDetails> connectStudentToDetailsHash=dataBase.getConnectStudentToDetailsHash();
        ConcurrentHashMap<Integer,String> connectIdToUserHash=dataBase.getConnectIdToUserHash();
        String currentUser= connectIdToUserHash.get(connectionId);
        boolean ans=message(connectStudentToDetailsHash,connectIdToUserHash);
        if(!ans){
            connections.send(connectionId,"ERROR 7");
        }
        else{
            String s="ACK 7 ";
            for(StudentLogInDetails current:connectStudentToDetailsHash.values()){
                if(current.getIsLogIn()&&!current.getBlockedMe().contains(currentUser)&&!current.getIblocked().contains(currentUser)){
                  s=s+current.toString()+" ";
                }
            }
            connections.send(connectionId,s);
        }


    }
    private boolean message(ConcurrentHashMap<String,StudentLogInDetails> connectStudentToDetailsHash,ConcurrentHashMap<Integer,String> connectIdToUserHash){
       String userCurrentName= connectIdToUserHash.get(connectionId);
       if(userCurrentName==null){
           return false;
       }
       StudentLogInDetails currentStudent= connectStudentToDetailsHash.get(userCurrentName);
       if(currentStudent==null){
           return false;
       }
       if(!currentStudent.getIsLogIn()){
           return false;
       }
       return true;
    }
}
