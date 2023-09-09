//
// Created by ziv seker on 30/12/2021.
//
#include "../include/taskForOutput.h"
#include <stdlib.h>
#include <thread>
#include <iostream>
#include <iostream>
#include "../include/connectionHandler.h"
#include <boost/lexical_cast.hpp>
#include <thread>
using namespace std;

TaskForOutput::TaskForOutput(ConnectionHandler &connectionHandler, bool* shouldTerminate): _connectionHandler(connectionHandler), shouldTerminate(shouldTerminate){}

void TaskForOutput::run() {//should seport 9-11
    while(!(*shouldTerminate)) {
        string opttional="";
        char serverMessage[2];
        string toPrint;
        _connectionHandler.getBytes(serverMessage,2);
        short op = bytesToShort(serverMessage);
        if(op == 9){
            toPrint =  "NOTIFICATION";
            string posting;
            _connectionHandler.getLine(posting);
            toPrint += posting;
            toPrint=toPrint.substr(0,toPrint.length()-1);
        }
        else if(op == 10){
            toPrint = "ACK";
            _connectionHandler.getLine(opttional);
               toPrint += opttional;
               toPrint=toPrint.substr(0,toPrint.length()-1);
        }
        else if(op == 11){
            toPrint = "ERROR";
            _connectionHandler.getLine(opttional);
            toPrint += opttional;
            toPrint=toPrint.substr(0,toPrint.length()-1);
        }
        cout<<toPrint<<endl;
        if(toPrint.compare("ACK 3")==0){
            *shouldTerminate=true;
        }
}
}
short TaskForOutput::bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}