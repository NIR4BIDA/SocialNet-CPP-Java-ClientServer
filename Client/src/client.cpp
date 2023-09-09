//
// Created by ziv seker on 31/12/2021.
//
#include <thread>
#include <iostream>
#include "../include/connectionHandler.h"
#include "../include/taskForOutput.h"
#include "../include/taskForInput.h"
using namespace std;

int main(int argc, char *argv[]){
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    bool *shouldTerminate = new bool(false);
    TaskForInput task1(connectionHandler, shouldTerminate);
    TaskForOutput task2(connectionHandler, shouldTerminate);
    thread thread1 = thread(&TaskForInput::run, task1);
    task2.run();
    thread1.detach();
    delete shouldTerminate;
    return 0;
}