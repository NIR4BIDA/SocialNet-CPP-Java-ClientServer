package bgu.spl.net.api.bidi;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class PMMessage {
    String message;
    DataBase dataBase;
    int connectionId;
    Connections connections;

    public PMMessage(String _message, DataBase _dataBase,int _connectionId,Connections _connections) {
        message = _message;
        dataBase = _dataBase;
        connectionId=_connectionId;
        connections=_connections;
    }

    public void processMessage() {
        String userName = "";
        String content = "";
        message=message.substring(1);
        int index=message.indexOf(' ');
        userName=message.substring(0,index);
        content=message.substring(index+1,message.length());
//        StringTokenizer x = new StringTokenizer(message);
//        userName = x.nextToken();
//        content = x.nextToken();
        ConcurrentHashMap<String, StudentLogInDetails> connectStudentToDetailsHash = dataBase.getConnectStudentToDetailsHash();
        StudentLogInDetails otherUser = connectStudentToDetailsHash.get(userName);
        if(PMMessageStudent(userName, content)){
          connections.send(connectionId,"ACK 6");
        }
        else{
            connections.send(connectionId,"ERROR 6" );
        }
    }

    private boolean PMMessageStudent(String userName, String content) {
        ConcurrentHashMap<String, StudentLogInDetails> connectStudentToDetailsHash = dataBase.getConnectStudentToDetailsHash();
        StudentLogInDetails otherUser = connectStudentToDetailsHash.get(userName);
        ConcurrentHashMap<Integer,String>connectIdToUserHash=dataBase.getConnectIdToUserHash();
        String currentUser=connectIdToUserHash.get(connectionId);
        if(currentUser==null){ //no user exist
            return false;
        }
        StudentLogInDetails currentStudent=connectStudentToDetailsHash.get(currentUser);
        if (currentStudent == null) { //current user not
            return false;
        } else if (!currentStudent.getIsLogIn()) { //current user not log in
                return false;
            }

       else if(otherUser==null){  //other user is not register
            return false;
        }
        else if(!otherUser.getFollowers().contains(currentUser)){ //current user not follow other user
        return false;
        }
        if(otherUser.getIblocked().contains(currentUser)){
            return false;
        }
        //implements here
        content=filterWords(content);
        LocalDate today=LocalDate.now();
        content=content+" "+String.valueOf(today);
        currentStudent.getPMPost().add(content);
        if(otherUser.getIsLogIn()) {
            sendPM(currentUser, otherUser.getConnectionId(), content);
        }
        else{
            otherUser.getMessagesWhenDisconnect().add("NOTIFICATION PM "+currentUser+" "+content);
        }
        return true;
    }
    private void sendPM(String currentUserName,int otherUserConnId, String content){
        connections.send(otherUserConnId,"NOTIFICATION PM "+currentUserName+" "+content);
    }
    private String filterWords(String content){
        String[]filtered=dataBase.getFilterWords();
        for(String current:filtered) {
            content= content.replaceAll(current, "<filtered>");
        }
        return content;
    }
}
