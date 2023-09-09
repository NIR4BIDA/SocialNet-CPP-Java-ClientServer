package bgu.spl.net.api.bidi;

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class blockMessage {
    String userNameToBlock;
    DataBase dataBase;
    int connectionId;
    Connections connections;

    public blockMessage(DataBase _dataBase, int _connectionId, Connections _connections,String _userNameToBlock){
        connections=_connections;
        dataBase = _dataBase;
        connectionId = _connectionId;
        connectionId = _connectionId;
        userNameToBlock=_userNameToBlock;
    }
    public void processMessage(){
        userNameToBlock=userNameToBlock.substring(1);
        ConcurrentHashMap<Integer,String> connectIdToUserHash = dataBase.getConnectIdToUserHash();
        ConcurrentHashMap<String, StudentLogInDetails> connectStudentToDetailsHash = dataBase.getConnectStudentToDetailsHash();
        String currentUser = connectIdToUserHash.get(connectionId);
        StudentLogInDetails studentToBlock = connectStudentToDetailsHash.get(userNameToBlock);
        boolean ans=sendMessage(connectStudentToDetailsHash,userNameToBlock,currentUser,studentToBlock);
        if(ans){
            connections.send(connectionId, "ACK 12");
        }
        else{
            connections.send(connectionId,"ERROR 12");
        }
    }
    public boolean sendMessage( ConcurrentHashMap<String, StudentLogInDetails> connectStudentToDetailsHash,String userNameToBlock,String currentUser,StudentLogInDetails studentToBlock){
        if(currentUser == null){
        return false;
        }
        StudentLogInDetails currentStudent=connectStudentToDetailsHash.get(currentUser);
        if(currentStudent==null){
        return false;
        }
        if(studentToBlock==null){
            return false;
        }
        Vector<String> following=currentStudent.getFollowing();
        if(following.contains(userNameToBlock)){
            following.remove(userNameToBlock);
        }
        Vector otherFollowers=studentToBlock.getFollowers();
        if(otherFollowers.contains(currentUser)){
            otherFollowers.remove(currentUser) ;
        }
        if( currentStudent.getIblocked().contains(userNameToBlock)){
            return false;
        }
        currentStudent.getIblocked().add(userNameToBlock);
        studentToBlock.getBlockedMe().add(currentUser);
        return true;
    }

}
