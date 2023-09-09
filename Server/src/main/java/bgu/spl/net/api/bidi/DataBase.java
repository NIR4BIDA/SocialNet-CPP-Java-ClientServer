package bgu.spl.net.api.bidi;

import java.util.concurrent.ConcurrentHashMap;

public class DataBase {
   private ConcurrentHashMap<String,StudentLogInDetails> connectStudentToDetailsHash;
   private ConcurrentHashMap<Integer,String> connectIdToUserHash;
   String[]filterWords={"bad","devil","trump","iran"};
    private DataBase(){
        connectStudentToDetailsHash=new ConcurrentHashMap<String,StudentLogInDetails>();
        connectIdToUserHash=new ConcurrentHashMap<Integer,String>();
    }
        private static class singletonHolder{
        private static DataBase instance=new DataBase();
    }
        public static DataBase getInstance(){
        return DataBase.singletonHolder.instance;
    }
    public ConcurrentHashMap<String, StudentLogInDetails> getConnectStudentToDetailsHash() {
        return connectStudentToDetailsHash;
    }

    public ConcurrentHashMap<Integer, String> getConnectIdToUserHash() {
        return connectIdToUserHash;
    }

    public String[] getFilterWords() {
        return filterWords;
    }
}

