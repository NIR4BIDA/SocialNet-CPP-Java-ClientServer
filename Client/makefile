CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -pthread

all: bin/BGRSclient

bin/BGRSclient: bin/client.o bin/connectionHandler.o bin/taskForInput.o bin/taskForOutput.o
	g++ -o bin/BGRSclient bin/client.o bin/connectionHandler.o bin/taskForInput.o bin/taskForOutput.o $(LDFLAGS)

bin/client.o: src/client.cpp
	g++ $(CFLAGS) -o bin/client.o src/client.cpp

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/taskForInput.o: src/taskForInput.cpp
	g++ $(CFLAGS) -o bin/taskForInput.o src/taskForInput.cpp

bin/taskForOutput.o: src/taskForOutput.cpp
	g++ $(CFLAGS) -o bin/taskForOutput.o src/taskForOutput.cpp

.PHONY: clean
clean:
	rm -f bin/*