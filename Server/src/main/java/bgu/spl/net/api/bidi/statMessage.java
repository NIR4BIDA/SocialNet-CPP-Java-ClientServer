package bgu.spl.net.api.bidi;

import java.util.concurrent.ConcurrentHashMap;

public class statMessage {
    String message;
    DataBase dataBase;
    int connectionId;
    Connections connections;

    public statMessage(String _message, DataBase _dataBase,int _connectionId,Connections _connections) {
        message = _message;
        dataBase = _dataBase;
        connectionId=_connectionId;
        connections=_connections;
    }
    public void processMessage(){
        ConcurrentHashMap<String, StudentLogInDetails> connectStudentToDetailsHash = dataBase.getConnectStudentToDetailsHash();
        ConcurrentHashMap<Integer, String> connectIdToUserHash = dataBase.getConnectIdToUserHash();
        String currentUserName = connectIdToUserHash.get(connectionId);
        StudentLogInDetails currentStudent=connectStudentToDetailsHash.get(currentUserName);
        boolean ans=message();
        if(!ans){
            connections.send(connectionId,"ERROR 8");
        }
        else{
          sendAck(message,connectStudentToDetailsHash,currentUserName,currentStudent);
        }

    }
    public boolean message(){
        ConcurrentHashMap<String, StudentLogInDetails> connectStudentToDetailsHash = dataBase.getConnectStudentToDetailsHash();
        ConcurrentHashMap<Integer, String> connectIdToUserHash = dataBase.getConnectIdToUserHash();
        String currentUserName = connectIdToUserHash.get(connectionId);
         if(currentUserName==null){
             return false;
         }
        StudentLogInDetails currentStudent=connectStudentToDetailsHash.get(currentUserName);
         if(currentStudent==null){
             return false;
         }
        if(!currentStudent.getIsLogIn()){
            return false;
        }
                return true;
    }
    public void sendAck(String content,ConcurrentHashMap<String, StudentLogInDetails> connectStudentToDetailsHash,String currentUserName,StudentLogInDetails currentStudent){
        content=content.substring(1);
        String userNameToAdd="";
      String ans="ACK 8 ";
      boolean isBlocked=false;
      for(int i=0;i<content.length()&&!isBlocked;i++) {
          if (content.charAt(i) != '|') {
              userNameToAdd = userNameToAdd + content.charAt(i);
          } else {
              StudentLogInDetails otherStudent = connectStudentToDetailsHash.get(userNameToAdd);
              if (otherStudent != null) {
                  if(currentStudent.getBlockedMe().contains(userNameToAdd)||currentStudent.getIblocked().contains(userNameToAdd)){
                      isBlocked=true;
                      ans="ERROR 8";
                  }
                  else {
                      ans = ans + otherStudent.toString() + " ";
                  }
              }
              userNameToAdd = "";
          }
      }
          StudentLogInDetails lastStudent=connectStudentToDetailsHash.get(userNameToAdd);
          if(currentStudent!=null&&!isBlocked){
              if(currentStudent.getBlockedMe().contains(userNameToAdd)||currentStudent.getIblocked().contains(userNameToAdd)){
                ans= ans="ERROR 8";
              }
              else {
                  if(lastStudent!=null) {
                      ans = ans + lastStudent.toString();
                  }
                  else{
                      ans="ERROR 8";
                  }
              }
          }
          if(ans.isEmpty()){
              ans="ERROR 8";
          }
        connections.send(connectionId,ans);
      }

    }
