package bgu.spl.net.api.bidi;

import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class logInMessage {
    String message;
    DataBase dataBase;
    int connectionId;
    Connections connections;
    public logInMessage(String _message,DataBase _dataBase,Connections _connections,int _connectionId){
     message=_message;
     dataBase=_dataBase;
     connectionId=_connectionId;
     connections=_connections;
    }
    public boolean processMessage(){
        String userName="";
        String password="";
        String captcha="";
        StringTokenizer x=new StringTokenizer(message);
        userName=x.nextToken();
        password=x.nextToken();
        captcha=x.nextToken();
        return LogInStudent(userName,password,captcha);
    }
    private boolean LogInStudent(String userName,String password,String captcha) { //return false if need to return error
        if(captcha.compareTo("0")==0){
            return false;
        }
        ConcurrentHashMap<String,StudentLogInDetails> connectStudentToDetailsHash=dataBase.getConnectStudentToDetailsHash();
        StudentLogInDetails currentStudent = connectStudentToDetailsHash.get(userName);
        if (currentStudent == null) {
            return false;
        } else if (currentStudent.getIsLogIn()) {
            return false;
        } else if (currentStudent.getPassword().compareTo(password)!=0) {
            return false;
        }
        else{
            currentStudent.setLogIn(true);
            if(currentStudent.getConnectionId()!=connectionId){
                currentStudent.setConnectionId(connectionId);
            }
            for(String msg:currentStudent.getMessagesWhenDisconnect()){
                connections.send(connectionId,msg);
            }
            return true;
        }
    }
    public String ans(boolean isSuccess){
        if(isSuccess){
            return "ACK 2";
        }
        else{
            return "ERROR 2";
        }
    }
}
