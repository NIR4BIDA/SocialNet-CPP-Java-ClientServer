package bgu.spl.net.api.bidi;

import java.util.Vector;

public class StudentLogInDetails {
    int connectionId;
    String userName;
    String password;
    String birthday;
    String age;
    boolean isLogIn;
    Vector<String> followers;
    Vector<String> following;
    Vector<String> posts;
    Vector<String> PMPost;
    Vector<String> blockedMe;
    Vector<String> Iblocked;
    Vector<String>messagesWhenDisconnect;


    public StudentLogInDetails(String _userName, String _password, String _birthday, boolean _isLogIn,int _connectionId,String _age) {
        connectionId=_connectionId;
        userName = _userName;
        password = _password;
        birthday = _birthday;
        isLogIn = _isLogIn;
        age=_age;
        followers = new Vector<String>();
        following = new Vector<String>();
        posts = new Vector<String>();
        PMPost=new Vector<String>();
        blockedMe = new Vector<String>();
        Iblocked = new Vector<String>();
        messagesWhenDisconnect=new Vector<String>();
    }

    //is login functions
    public boolean getIsLogIn() {
        return isLogIn;
    }

    public void setLogIn(boolean logIn) {
        isLogIn = logIn;
    }

    //password function
    public String getPassword() {
        return password;
    }

    public void setPosts(String post){
        posts.add(post);
    }

    public String toString() {
        String s=age+" "+posts.size()+" "+followers.size()+" "+following.size();
        return s;
    }
    //following followers methods

    public Vector<String> getFollowers() {
        return followers;
    }

    public Vector<String> getFollowing() {
        return following;
    }
    //connection id functions
    public int getConnectionId(){
        return connectionId;
    }

    public Vector<String> getPMPost() {
        return PMPost;
    }
    public Vector<String> getBlockedMe(){
        return blockedMe;
    }
    public Vector<String> getIblocked(){
        return Iblocked;
    }

    public Vector<String> getMessagesWhenDisconnect() {
        return messagesWhenDisconnect;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }
}