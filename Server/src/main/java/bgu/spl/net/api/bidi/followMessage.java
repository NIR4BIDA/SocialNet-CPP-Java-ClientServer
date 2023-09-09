package bgu.spl.net.api.bidi;

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class followMessage {
    String message;
    DataBase dataBase;
    int connectionId;
    public followMessage(String _message,DataBase _dataBase,int _connectionId){
        message=_message;
        dataBase=_dataBase;
        connectionId=_connectionId;
    }
    public boolean processMessage(){
        String followOrUnfollow="";
        String otherUserName="";
        StringTokenizer x=new StringTokenizer(message);
        followOrUnfollow=x.nextToken();
        otherUserName=x.nextToken();
        return followStudent(otherUserName,followOrUnfollow);
    }
    private boolean followStudent(String otherUserName,String followOrUnfollow){
        ConcurrentHashMap<String,StudentLogInDetails> connectStudentToDetailsHash=dataBase.getConnectStudentToDetailsHash();
        ConcurrentHashMap<Integer,String>connectIdToUserHash=dataBase.getConnectIdToUserHash();
        String userName=connectIdToUserHash.get(connectionId);
        if(userName==null){
            return false;
        }
        StudentLogInDetails student=connectStudentToDetailsHash.get(userName);
        if(student==null){
            return false;
        }
        StudentLogInDetails otherStudent=connectStudentToDetailsHash.get(otherUserName);
        if(otherStudent==null){
            return false;
        }
        if(!student.getIsLogIn()){
            return false;
        }
        if(student.getBlockedMe().contains(otherUserName)||student.getIblocked().contains(otherUserName)){
            return false;
        }
        Vector<String> myFollowing=student.getFollowing();
        Vector<String> myFollowers=student.getFollowers();
        Vector<String> otherFollowing=otherStudent.getFollowing();
        Vector<String> otherFollowers=otherStudent.getFollowers();
        if(followOrUnfollow.compareTo("0")==0){ //follow request
           if(myFollowing.contains(otherUserName)) {  //if i already follow other user
               return false;
           }
           else{ //if i not already follow other user
               myFollowing.add(otherUserName);
               otherFollowers.add(userName);
               return true;
           }
        }
        else{ //unfollow request
         if(!myFollowing.contains(otherUserName)) { //if i already not follow other user
          return false;
         }
         else{
             myFollowing.remove(otherUserName);
             otherFollowers.remove(userName);
             return true;
         }
        }
    }
    public String ans(boolean isSuccess,String message){
        StringTokenizer x=new StringTokenizer(message);
        String followOrUnfollow=x.nextToken();
        String otherUserName=x.nextToken();
        if(isSuccess){
            return "ACK 4 "+otherUserName;
        }
        else{
            return "ERROR 4";
        }
    }
}
