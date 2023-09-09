//
// Created by ziv seker on 30/12/2021.
//

#ifndef CLIENT_TASKFOROUTPUT_H
#define CLIENT_TASKFOROUTPUT_H

#include <thread>
#include <iostream>
#include <iostream>
#include "connectionHandler.h"
using namespace std;

using std::mutex;

class TaskForOutput{
private:
    ConnectionHandler& _connectionHandler;
    bool* shouldTerminate;

public:
    TaskForOutput(ConnectionHandler &connectionHandler,bool* shouldTerminate);
    void run();
    short bytesToShort(char* bytesArr);
};

#endif //CLIENT_TASKFOROUTPUT_H