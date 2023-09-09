package bgu.spl.net.api.bidi;

import java.time.LocalDate;
import java.time.Period;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class registerMessage {
    String message;
    DataBase dataBase;
    int connectionId;
    Connections connections;
    public registerMessage(String _message,DataBase _dataBase,int _connectionId){
        message=_message;
        dataBase=_dataBase;
        connectionId=_connectionId;
    }
    public boolean processMessage(){
        String userName="";
        String password="";
        String birthday="";
        StringTokenizer x=new StringTokenizer(message);
        userName=x.nextToken();
        password=x.nextToken();
        birthday=x.nextToken();
        StringTokenizer y=new StringTokenizer(birthday,"-");
        int day=Integer.valueOf(y.nextToken());
        int month=Integer.valueOf(y.nextToken());
        int year=Integer.valueOf(y.nextToken());
        LocalDate birth=LocalDate.of(year,month,day);
        LocalDate today=LocalDate.now();
        String age= String.valueOf(Period.between(birth,today).getYears());
        StudentLogInDetails student=new StudentLogInDetails(userName,password,birthday,false,connectionId,age);
        //System.out.println("userNAME IS:"+userName+" password is "+password+" birthday is "+birthday);
        return register(userName,student);
    }
    private boolean register(String userName,StudentLogInDetails student){//return false if need to return error
        ConcurrentHashMap<String,StudentLogInDetails> connectStudentToDetailsHash=dataBase.getConnectStudentToDetailsHash();
        ConcurrentHashMap<Integer,String> connectIdToUserHash=dataBase.getConnectIdToUserHash();
        if(connectStudentToDetailsHash.get(userName)!=null){
            return false;
        }
        else {
            connectStudentToDetailsHash.put(userName, student);
            connectIdToUserHash.put(connectionId,userName);
            return true;
        }
    }
    public String ans(boolean isSuccess){
        if(isSuccess){
            return "ACK 1";
        }
        else{
            return "ERROR 1";
        }
    }
}
