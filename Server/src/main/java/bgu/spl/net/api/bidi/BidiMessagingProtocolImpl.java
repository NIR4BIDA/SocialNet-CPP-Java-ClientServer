package bgu.spl.net.api.bidi;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {
    Connections<T> connections;
    int connectionId;
    DataBase dataBase;
public BidiMessagingProtocolImpl(DataBase _dataBase){
   dataBase=_dataBase;
}
    @Override
    public void start(int _connectionId, Connections<T> _connections) {
        connections = _connections;
        connectionId = _connectionId;
    }

    @Override
    public void process(T message) {
    boolean isSuccesAct=false;
    String ans="";
    String command="";
    String messageDetails="";
    int index=((String)message).indexOf(' ');
        if(index==-1){
         command= (String) message;
        }
        else {
            command = ((String) message).substring(0, index);
            messageDetails = ((String) message).substring(index);
        }
    if(command.compareTo("REGISTER")==0){
        registerMessage act=new registerMessage(messageDetails,dataBase,connectionId);
        isSuccesAct= act.processMessage();
        ans=act.ans(isSuccesAct);
        connections.send(connectionId, (T) ans);
    }
    else if(command.compareTo("LOGIN")==0){
        logInMessage act=new logInMessage(messageDetails,dataBase,connections,connectionId);
        isSuccesAct=act.processMessage();
        ans=act.ans(isSuccesAct);
        connections.send(connectionId, (T) ans);
        //connections.disconnect(connectionId);
    }
    else if(command.compareTo("LOGOUT")==0){
        logOutMessage act=new logOutMessage(dataBase,connectionId);
        isSuccesAct=act.processMessage();
        ans=act.ans(isSuccesAct);
        connections.send(connectionId, (T) ans);
    }
    else if(command.compareTo("FOLLOW")==0){
        followMessage act=new followMessage(messageDetails,dataBase,connectionId);
        isSuccesAct=act.processMessage();
        ans=act.ans(isSuccesAct,messageDetails);
        connections.send(connectionId, (T) ans);
    }
    else if(command.compareTo("POST")==0){
        postMessage act = new postMessage(dataBase, connectionId);
        isSuccesAct=act.processMessage();
        ans=act.ans(isSuccesAct);
        connections.send(connectionId, (T) ans);
        if(isSuccesAct) {
            sendPost(messageDetails);
        }
    }
    else if(command.compareTo("PM")==0){
        PMMessage act=new PMMessage(messageDetails,dataBase, connectionId,connections);
      act.processMessage();
    }
    else if(command.compareTo("STAT")==0){
        statMessage act=new statMessage(messageDetails,dataBase,connectionId,connections);
        act.processMessage();
    }
    else if(command.compareTo("LOGSTAT")==0){
        logStatMessage act=new logStatMessage(dataBase,connectionId,connections);
        act.LogStatMessage();
    }
    else if(command.compareTo("BLOCK")==0){
        blockMessage act=new blockMessage(dataBase,connectionId,connections,messageDetails);
        act.processMessage();
    }
    }
private void sendPost(String message){
    ConcurrentHashMap<Integer, String> idToUser= dataBase.getConnectIdToUserHash();
    String currentUser=idToUser.get(connectionId);
    ConcurrentHashMap<String, StudentLogInDetails> userToStudent=dataBase.getConnectStudentToDetailsHash();
    Vector<String> followers =userToStudent.get(currentUser).getFollowers();
    String[]words=message.split("@");
    if(message.charAt(0)=='@') {
        for (int i = 0; i < words.length; i++) {
            String user = words[i].split(" ")[0];
            StudentLogInDetails studentToSend=dataBase.getConnectStudentToDetailsHash().get(user);
            if(studentToSend!=null){
                if(!followers.contains(user)&&(!studentToSend.getBlockedMe().contains(currentUser)&&!studentToSend.getIblocked().contains(currentUser))) {
                    if(studentToSend.getIsLogIn()) {
                        connections.send(studentToSend.getConnectionId(), (T) ("NOTIFICATION PUBLIC " + currentUser + (T) message));
                    }
                    else{
                        studentToSend.getMessagesWhenDisconnect().add("NOTIFICATION PUBLIC " + currentUser +message) ;
                    }
                }
            }
        }
    }
    else{
        for (int i = 1; i < words.length; i++) {
            String user = words[i].split(" ")[0];
            StudentLogInDetails studentToSend=dataBase.getConnectStudentToDetailsHash().get(user);
            if(studentToSend!=null&&(!studentToSend.getBlockedMe().contains(currentUser)&&!studentToSend.getIblocked().contains(currentUser))) {
                if (!followers.contains(user)) {
                    if(studentToSend.getIsLogIn()) {
                        connections.send(studentToSend.getConnectionId(), (T) ("NOTIFICATION PUBLIC " + currentUser + (T) message));
                    }
                    else{
                        studentToSend.getMessagesWhenDisconnect().add("NOTIFICATION PUBLIC " + currentUser +message) ;
                    }
                }
            }
        }
    }
    for(String current:followers){
        StudentLogInDetails studentToSend=userToStudent.get(current);
        if(studentToSend.getIsLogIn()) {
            connections.send(studentToSend.getConnectionId(), (T) ("NOTIFICATION PUBLIC " + currentUser + (T) message));
        }
        else{
            studentToSend.getMessagesWhenDisconnect().add("NOTIFICATION PUBLIC " + currentUser +message) ;
        }
    }
}
    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
