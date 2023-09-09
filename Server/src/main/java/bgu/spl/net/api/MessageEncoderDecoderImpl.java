package bgu.spl.net.api;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
public class MessageEncoderDecoderImpl<T> implements MessageEncoderDecoder<T>  {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    public MessageEncoderDecoderImpl(){

    }
    //functions that helps encode decode
    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }
    private String setStringOptCode(String shortOptResult){
        if(shortOptResult.compareTo("01")==0){
        return "REGISTER";
        }
        else if(shortOptResult.compareTo("02")==0){
        return "LOGIN";
        }
        else if(shortOptResult.compareTo("03")==0){
        return "LOGOUT";
        }
        else if(shortOptResult.compareTo("04")==0){
        return "FOLLOW";
        }
        else if(shortOptResult.compareTo("05")==0){
        return "POST";
        }
        else if(shortOptResult.compareTo("06")==0){
        return "PM";
        }
        else if(shortOptResult.compareTo("07")==0){
        return "LOGSTAT";
        }
        else if(shortOptResult.compareTo("08")==0){
        return "STAT";
        }
        else {
        return "BLOCK";
        }
    }
    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String shortOptResult=new String(bytes,0,2,StandardCharsets.UTF_8);
        String Result= setStringOptCode(shortOptResult)+" ";
        for(int i=2;i<len;i=i+1){
            if(bytes[i]==0){
                Result=Result+" ";
            }
            else {
                Result = Result+new String(bytes,i,1,StandardCharsets.UTF_8);
            }
        }
        len = 0;
        String totalResult=Result;
        return totalResult;
    }
    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    //decode encode
    @Override
    public T decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == ';') {
            return ((T)popString());
        }
        else {
            pushByte(nextByte);
            return null; //not a line yet
        }
    }

    @Override
    public byte[] encode(T message) {
        int messageLength=((String)message).length();
        short optCode=0;
        String seifa;
        if(((String)message).indexOf("ERROR")==0){
            optCode=11;
            seifa=((String) message).substring("ERROR".length(),messageLength);
        }
        else if(((String)message).indexOf("ACK")==0){
            optCode=10;
            seifa=((String) message).substring("Ack".length(),messageLength);
        }
        else{
            optCode=9; //notification message
            seifa=((String) message).substring("notification".length(),messageLength);
        }
        byte[] optBytes=shortToBytes(optCode);
        byte[]messageSeifaBytes=(seifa+";").getBytes();
        byte[]totalMessage=new byte[optBytes.length+messageSeifaBytes.length];
        System.arraycopy(optBytes,0,totalMessage,0,2);
        System.arraycopy(messageSeifaBytes,0,totalMessage,2,messageSeifaBytes.length);
       return totalMessage;
    }
}

