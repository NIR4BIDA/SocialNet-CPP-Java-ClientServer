//
// Created by ziv seker on 30/12/2021.
//

#ifndef CLIENT_TASKFORINPUT_H
#define CLIENT_TASKFORINPUT_H

#include <stdlib.h>
#include <thread>
#include <iostream>
#include <iostream>
using namespace std;
#include "connectionHandler.h"

class TaskForInput {
private:
    ConnectionHandler& _connectionHandler;
    bool* shouldTerminate;

public:
    TaskForInput(ConnectionHandler &connectionHandler,bool* _shouldTerminate);
    void run();
    void shortToBytes(short num, char* bytesArr);
};

#endif //CLIENT_TASKFORINPUT_H