package bgu.spl.net.api.bidi;

import java.util.concurrent.ConcurrentHashMap;

public class postMessage {
    DataBase dataBase;
    int connectionId;
    String messageDetails;

    public postMessage(DataBase _dataBase, int _connectionId) {
        dataBase = _dataBase;
        connectionId = _connectionId;
    }

    public boolean processMessage() {
        ConcurrentHashMap<String, StudentLogInDetails> connectStudentToDetailsHash = dataBase.getConnectStudentToDetailsHash();
        ConcurrentHashMap<Integer, String> connectIdToUserHash = dataBase.getConnectIdToUserHash();
        String userName = connectIdToUserHash.get(connectionId);
        if (userName == null) {
            return false;
        }
        StudentLogInDetails student=connectStudentToDetailsHash.get(userName);
        if(student==null){
            return false;
        }
        else if(!student.getIsLogIn()){
            return false;
        }
        else {
            student.setPosts(messageDetails);
            return true;
        }
    }
    public String ans(boolean isSuccess){
        if(isSuccess){
          return "ACK 5";
        }
        else{
            return "ERROR 5";
        }
    }

}
