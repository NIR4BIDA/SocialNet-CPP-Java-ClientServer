run commands:
for reactor server:
mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.reactorMain" -Dexec.args="7777 10"

for thread per client server:
mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.MainThreadPerClient" -Dexec.args="7777"

for client:
bin/./BGRSclient 127.0.0.1 7777

example for each message:
register message:
REGISTER NIR 8 01-08-1997
login message:
LOGIN NIR 8 1
logout message:
LOGOUT
follow/unfollow message(0 means to follow after him):
FOLLOW 0 IDO 
post message:
POST hello everyone and hello @NIR
pm message:
PM NIR hi nir how are you
logstat message:
LOGSTAT
stat message:
STAT NIR|IDO|HILA
block message:
BLOCK NIR
ack message:
ACK 1
ACK 2
ACK 3
ACK 4 NIR
ACK 5
ACK 6
ack 7+8 seperated by spaces all the answers:
ACK 7 24 2 1 1 26 1 1 1
ACK 8 24 2 1 1 26 1 1 1
ACK 12
notification message:
NITIFICATION PUBLIC hi everyone
NOTIFICATION PM hi you

the filter words are stored in data base.
