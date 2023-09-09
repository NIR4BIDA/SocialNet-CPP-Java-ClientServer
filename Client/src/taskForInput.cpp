//
// Created by ziv seker on 30/12/2021.
//
#include "taskForInput.h"
#include <stdlib.h>
#include <connectionHandler.h>
#include <thread>
#include <iostream>
using namespace std;

TaskForInput::TaskForInput(ConnectionHandler &connectionHandler, bool* shouldTerminate): _connectionHandler(connectionHandler), shouldTerminate(shouldTerminate) {}

void TaskForInput::run() {
    while(!(*shouldTerminate)) {
        string op;
        string toServer;
        char line[1024];
        cin.getline(line, 1024);
        string line2(line);
        istringstream ss(line2);
        getline(ss, op, ' ');

        if(op.compare("REGISTER")==0){
            toServer = "01" ;
            string temp;
            getline(ss, temp, ' ');
            toServer += temp + '\0';
            getline(ss, temp, ' ');
            toServer += temp+ '\0';
            getline(ss, temp, ' ');
            toServer += temp;
        } else if(op.compare("LOGIN")==0){
            toServer = "02";
            string temp;
            getline(ss, temp, ' ');
            toServer += temp + '\0';
            getline(ss, temp, ' ');
            toServer += temp + '\0';
            getline(ss, temp, ' ');
            toServer += temp;


        } else if(op.compare("LOGOUT")==0) {
            toServer = "03";
        } else if(op.compare("FOLLOW")==0){
            toServer = "04";
            string temp;
            getline(ss, temp, ' ');
            toServer += temp+'\0';
            getline(ss, temp, ' ');
            toServer += temp;

        } else if(op.compare("POST")==0){
            toServer = "05";
            string temp;
            getline(ss, temp);
            toServer += temp;

        } else if(op.compare("PM")==0){
            toServer = "06";
            string temp;
            getline(ss, temp, ' ');
            toServer += temp + '\0';
            getline(ss, temp);
            toServer += temp;

        } else if(op.compare("LOGSTAT")==0){
            toServer = "07";

        } else if(op.compare("STAT")==0){
            toServer = "08";
            string temp;
            getline(ss, temp, ' ');
            toServer += temp;

        }

        else if(op.compare("BLOCK")==0){
            toServer = "12";
            string temp;
            getline(ss, temp, ' ');
            toServer += temp;
        }


        _connectionHandler.sendLine(toServer);
    }

}
void TaskForInput::shortToBytes(short num, char* bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}