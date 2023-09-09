package bgu.spl.net.api.bidi;

import java.util.concurrent.ConcurrentHashMap;

public class logOutMessage {
    DataBase dataBase;
    int connId;
    public logOutMessage(DataBase _dataBase,int _connId){
        dataBase=_dataBase;
        connId=_connId;
    }
    public boolean processMessage(){
        ConcurrentHashMap<String,StudentLogInDetails> connectStudentToDetailsHash=dataBase.getConnectStudentToDetailsHash();
        ConcurrentHashMap<Integer,String>connectIdToUserHash=dataBase.getConnectIdToUserHash();
        String userName=connectIdToUserHash.get(connId);
        if(userName==null){
            return false;
        }
        StudentLogInDetails student=connectStudentToDetailsHash.get(userName);
        if(student==null){
            return false;
        }
        if(!student.getIsLogIn()){
         return false;
        }
        else{
            student.setLogIn(false);
            return true;
        }
    }
    public String ans(boolean isSuccess){
        if(isSuccess){
            return "ACK 3";
        }
        else{
            return "ERROR 3";
        }
    }
}
